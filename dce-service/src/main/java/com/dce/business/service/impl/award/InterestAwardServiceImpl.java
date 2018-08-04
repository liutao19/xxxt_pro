package com.dce.business.service.impl.award;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.DictCode;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.entity.user.UserStaticDo;
import com.dce.business.entity.user.UserStaticDo.StaticType;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.award.IAwardService;
import com.dce.business.service.award.IAwardSwitchService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.user.IUserService;
import com.dce.business.service.user.IUserStaticService;

/**
 * 计算日息
 */
@Service("interestAwardService")
public class InterestAwardServiceImpl extends AwardSwitchServiceImpl implements IAwardService, IAwardSwitchService {

	private final static Logger logger = LoggerFactory.getLogger(InterestAwardServiceImpl.class);

	@Resource
	private IUserService userService;
	@Resource
	private IAccountService accountService;
	@Resource
	private ILoanDictService loanDictService;
	@Resource
	private IUserStaticService userStaticService;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void calAward(Integer userId, BigDecimal amount, Integer userLevel) {
		logger.info("计算日息，userId:" + userId);
		
		if (!isReleaseSwitchOn(userId)) {
			return;
		}
		
		BigDecimal baodanAmount = getBaoDanAmount(userId);
		if (baodanAmount.compareTo(BigDecimal.ZERO) <= 0) {
			logger.info("报单金额不正确，不计算生息:" + baodanAmount);
		}

		//日息钱包
		UserAccountDo userAccountDo = accountService.selectUserAccount(userId, AccountType.wallet_interest.getAccountType());

		UserDo userDo = userService.getUser(userId);
		if (userDo.getUserLevel() == null || userDo.getUserLevel().intValue() <= 0) {
			logger.info("用户没有级别，不计算生息奖励, userId:" + userDo.getId());
			return;
		}

		//查询日息比例
		BigDecimal rate = getInterestRate(userDo.getUserLevel().intValue());

		//生息
		BigDecimal interest = baodanAmount.add(userAccountDo.getAmount()).multiply(rate).setScale(6, RoundingMode.HALF_UP);

		UserAccountDo accountDo = new UserAccountDo();
		accountDo.setUserId(userId);
		accountDo.setAmount(interest);
		accountDo.setAccountType(AccountType.wallet_interest.getAccountType());
		accountService.updateUserAmountById(accountDo, IncomeType.TYPE_INTEREST);
	}

	/**
	 * 查询报单金额
	 * @param userId
	 * @return
	 */
	private BigDecimal getBaoDanAmount(Integer userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userid", userId);
		params.put("type", StaticType.BaoDan.getType());

		List<UserStaticDo> list = userStaticService.select(params);
		if (CollectionUtils.isEmpty(list)) {
			return BigDecimal.ZERO;
		}

		return list.get(0).getTotalmoney();
	}

	/**
	 * 查询持币生息比例设置
	 * @param userLevel
	 * @return
	 */
	private BigDecimal getInterestRate(Integer userLevel) {
		LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.ProfitRate.getCode(), userLevel.toString());
		Assert.notNull(loanDictDtlDo, "未设置持币生息比例");
		Assert.hasText(loanDictDtlDo.getRemark(), "未设置持币生息比例");

		return new BigDecimal(loanDictDtlDo.getRemark());
	}
}
