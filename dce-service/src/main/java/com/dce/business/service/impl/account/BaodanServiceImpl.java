package com.dce.business.service.impl.account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.DictCode;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.DateUtil;
import com.dce.business.dao.user.IUserDao;
import com.dce.business.dao.user.IUserStaticDao;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.entity.user.UserStaticDo;
import com.dce.business.entity.user.UserStaticDo.StaticType;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.account.IBaodanService;
import com.dce.business.service.award.IAwardService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.user.IUserStaticService;

@Service("baodanService")
public class BaodanServiceImpl implements IBaodanService {
	private final static Logger logger = Logger.getLogger(BaodanServiceImpl.class);
	@Resource
	private IUserStaticDao userStaticDao;
	@Resource(name = "awardServiceAsync")
	private IAwardService awardService;
	@Resource
	private IUserDao userDao;
	@Resource
	private ILoanDictService loanDictService;
	@Resource
	private IAccountService accountService;
	@Resource
	private IUserStaticService userStaticService;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void baodan(Integer userId, BigDecimal cashAmount, BigDecimal scoreAmount, Integer userLevel) {
		logger.info("用户报单，userId:" + userId);
		logger.info("cashAmount:" + cashAmount);
		logger.info("scoreAmount:" + scoreAmount);
		logger.info("userLevel:" + userLevel);

		//校验  用户是否已经报过单
		UserDo user = userDao.selectByPrimaryKey(userId);
		if (user == null) {
			logger.info("用户不存在 userId:" + userId);
			throw new BusinessException("用户不存在");
		}

		//检查用户是否已报单
		checkHasBaoDan(userId);

		//报单成功累计推荐人
		userDao.addRefereeNumber(user.getRefereeid());

		//根据报单级别查看报单所需金额
		LoanDictDtlDo loanDictDtl = loanDictService.getLoanDictDtl(DictCode.BaoDanFei.getCode(), userLevel.toString());
		Assert.notNull(loanDictDtl, "报单费未配置");
		Assert.hasText(loanDictDtl.getRemark(), "报单费未配置");

		LoanDictDtlDo CashToIBAC = loanDictService.getLoanDictDtl(DictCode.CashToIBAC.getCode());
		LoanDictDtlDo ScoreToIBAC = loanDictService.getLoanDictDtl(DictCode.ScoreToIBAC.getCode());
		
		BigDecimal baodanFei = new BigDecimal(loanDictDtl.getRemark());
		
		BigDecimal cashToIBAC = new BigDecimal(CashToIBAC.getRemark());
		BigDecimal scoreToIBAC = new BigDecimal(ScoreToIBAC.getRemark());
		
		BigDecimal totalAmount = cashAmount.multiply(cashToIBAC).setScale(2, RoundingMode.HALF_DOWN).add(scoreAmount.multiply(scoreToIBAC).setScale(2, RoundingMode.HALF_DOWN)); //现金+积分
		if (totalAmount.compareTo(baodanFei) != 0) {
			throw new BusinessException("报单金额与用户等级不匹配");
		}

		//判断积分比例
		loanDictDtl = loanDictService.getLoanDictDtl(DictCode.ScoreShiFangRate.getCode(), userLevel.toString());
		Assert.notNull(loanDictDtl, "报单流通币比例未配置");
		Assert.hasText(loanDictDtl.getRemark(), "报单流通币比例未配置");
		BigDecimal rate = new BigDecimal(loanDictDtl.getRemark());
		BigDecimal maxScoreAmount = baodanFei.multiply(rate).setScale(6, RoundingMode.HALF_UP); //报单时至多只能用这么多积分，其余的必须用现金券
		if (scoreAmount.compareTo(maxScoreAmount) > 0) {
			throw new BusinessException("流通币金额不能大于" + maxScoreAmount.toString());
		}

		try {
			checkAccountAmount(userId, AccountType.wallet_cash, cashAmount.multiply(cashToIBAC).setScale(2, RoundingMode.HALF_DOWN));
			checkAccountAmount(userId, AccountType.wallet_score, scoreAmount.multiply(scoreToIBAC).setScale(2, RoundingMode.HALF_DOWN));

			//更新账户
			updateAccountAmount(userId, cashAmount.multiply(cashToIBAC).setScale(2, RoundingMode.HALF_DOWN), scoreAmount.multiply(scoreToIBAC).setScale(2, RoundingMode.HALF_DOWN), baodanFei);

			UserStaticDo userStatic = new UserStaticDo();
			userStatic.setUserid(userId);
			userStatic.setMoney(calMoney(totalAmount, userLevel));
			userStatic.setStatus((byte) 1);
			userStatic.setType(StaticType.BaoDan.getType());
			userStatic.setTotalmoney(totalAmount);
			userStatic.setEndDate(DateUtil.getDate(new Date(), 365)); //365天结束
			userStaticService.insert(userStatic);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			logger.error("账户金额不足", e);
			throw new BusinessException("账户金额不足");
		}

		//更新用户级别
		try {
			UserDo record = new UserDo();
			record.setId(userId);
			record.setUserLevel(userLevel.byteValue());
			record.setActivationTime(new Date().getTime());
			record.setBaodan_amount(baodanFei);
			record.setIsActivated(1);
			userDao.updateByPrimaryKeySelective(record);
		} catch (Exception e) {
			logger.error("更新用户级别出错", e);
			throw new BusinessException("更新用户级别出错");
		}

		//异步调用 ，统计业绩
		awardService.calAward(userId, totalAmount, userLevel);
	}

	public static void main(String[] args) {
		BigDecimal b1 = new BigDecimal("1.3");
		BigDecimal b2 = new BigDecimal("2.88");
		BigDecimal b3 = new BigDecimal("4");
		System.out.println(b3.compareTo(b1.add(b2)));
	}
	private void updateAccountAmount(Integer userId, BigDecimal cashAmount, BigDecimal scoreAmount, BigDecimal baodanFei) {
		String seqId = UUID.randomUUID().toString();

		//1、现金账户
		UserAccountDo cashAccount = new UserAccountDo();
		cashAccount.setUserId(userId);
		cashAccount.setAmount(cashAmount.negate());
		cashAccount.setAccountType(AccountType.wallet_cash.getAccountType());
		cashAccount.setSeqId(seqId);
		accountService.updateUserAmountById(cashAccount, IncomeType.TYPE_AWARD_BAODAN);

		//2、积分账户
		UserAccountDo scoreAccount = new UserAccountDo();
		scoreAccount.setUserId(userId);
		scoreAccount.setAmount(scoreAmount.negate());
		scoreAccount.setAccountType(AccountType.wallet_score.getAccountType());
		scoreAccount.setSeqId(seqId);
		accountService.updateUserAmountById(scoreAccount, IncomeType.TYPE_AWARD_BAODAN);

		//3、原始仓账户
		UserAccountDo originalAccount = new UserAccountDo();
		originalAccount.setUserId(userId);
		originalAccount.setAmount(baodanFei);
		originalAccount.setAccountType(AccountType.wallet_original.getAccountType());
		originalAccount.setSeqId(seqId);
		accountService.updateUserAmountById(originalAccount, IncomeType.TYPE_AWARD_BAODAN);
	}

	/**
	 * 判断账户余额是否充足
	 * @param userId
	 * @param accountType
	 * @param amount
	 */
	private void checkAccountAmount(Integer userId, AccountType accountType, BigDecimal amount) {
		UserAccountDo account = accountService.selectUserAccount(userId, accountType.getAccountType());
		if (account == null || account.getAmount() == null || account.getAmount().compareTo(amount) < 0) {
			throw new BusinessException("报单失败，余额不足!");
		}
	}

	/**
	 * 检查是否已报单
	 * @param userId
	 * @return
	 */
	private void checkHasBaoDan(Integer userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", StaticType.BaoDan.getType());
		params.put("userid", userId);
		List<UserStaticDo> list = userStaticDao.select(params);
		if (CollectionUtils.isEmpty(list)) {
			return;
		}

		throw new BusinessException("请勿重复报单");
	}

	/**
	 * 计算原始每日释放金额
	 * @param amount
	 * @param userLevel
	 * @return
	 */
	private BigDecimal calMoney(BigDecimal amount, Integer userLevel) {
		if(amount.compareTo(BigDecimal.ZERO) < 0){
			return BigDecimal.ZERO;
		}
		LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.OrigShiFangRate.getCode(), userLevel.toString());
		Assert.notNull(loanDictDtlDo, "未设置原始币钱包释放比例");
		Assert.hasText(loanDictDtlDo.getRemark(), "未设置原始币钱包释放比例");

		BigDecimal rate = new BigDecimal(loanDictDtlDo.getRemark());

		BigDecimal money = amount.multiply(rate).setScale(6, RoundingMode.HALF_UP);

		return money;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Result<?> upgradeLevel(UserDo userDo, Integer userLevel) {
		Integer userId = userDo.getId();
		UserDo user = userDao.selectByPrimaryKey(userId);
		if(user == null){
			logger.info("用户ID为:" + userId + "的用户不存在");
			return Result.failureResult("用户不存在!");
		}
		
		int flag = 0;
		//判断是否为空单激活 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", StaticType.BaoDan.getType());
		params.put("userid", userId);
		List<UserStaticDo> list = userStaticDao.select(params);
		//如果为空  表示该用户只是报了空单  但是没有产生过业绩，此时需要向 ct_user_static表中新增一条报单记录  用于记录业绩和释放
		if (CollectionUtils.isEmpty(list)) {
			//根据报单级别查看报单所需金额
			logger.info("报单释放记录为空,新增一条报单记录 ,用户ID userId=" + userId);
			LoanDictDtlDo loanDictDtl = loanDictService.getLoanDictDtl(DictCode.BaoDanFei.getCode(), userLevel.toString());
			Assert.notNull(loanDictDtl, "报单费未配置");
			Assert.hasText(loanDictDtl.getRemark(), "报单费未配置");
			
			BigDecimal totalAmount = new BigDecimal(loanDictDtl.getRemark());
			UserStaticDo userStatic = new UserStaticDo();
			userStatic.setUserid(userId);
			userStatic.setMoney(calMoney(totalAmount, userLevel));
			userStatic.setStatus((byte) 1);
			userStatic.setType(StaticType.BaoDan.getType());
			userStatic.setTotalmoney(totalAmount);
			userStatic.setEndDate(DateUtil.getDate(new Date(), 365)); //365天结束
			flag = userStaticService.insert(userStatic);
			
		}else{
				
				logger.info("用户报单级别升级 原等级为 :" + user.getUserLevel().intValue() + ",修改后等级为:" + userLevel.intValue());
				LoanDictDtlDo loanDictDtlNew = loanDictService.getLoanDictDtl(DictCode.BaoDanFei.getCode(), userLevel.toString());
				Assert.notNull(loanDictDtlNew, "报单费未配置");
				Assert.hasText(loanDictDtlNew.getRemark(), "报单费未配置");
				
				UserStaticDo  userStatic = list.get(0);
				BigDecimal totalAmount = new BigDecimal(loanDictDtlNew.getRemark());
				userStatic.setMoney(calMoney(totalAmount.subtract(userStatic.getYfBonus()), userLevel));
				userStatic.setType(StaticType.BaoDan.getType());
				userStatic.setTotalmoney(totalAmount);
				userStatic.setEndDate(DateUtil.getDate(new Date(), 365)); //365天结束
				flag = userStaticService.updateStatic(userStatic);
		}
		logger.info("报单升级结果flag=" + flag);
		if(flag > 0){
			//修改用户信息
			userDao.updateByPrimaryKeySelective(userDo);
			return Result.successResult("用户信息修改成功!");
		}else{
			
			return Result.failureResult("用户信息修改失败!");
		}
	}
}
