package com.dce.business.service.impl.award;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.DictCode;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.common.util.DateUtil;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.award.IAwardService;
import com.dce.business.service.award.IAwardSwitchService;
import com.dce.business.service.bonus.IBonusDailyService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.user.IUserService;

@Service("zhiTuiAwardService")
public class ZhiTuiAwardServiceImpl extends AwardSwitchServiceImpl implements IAwardService, IAwardSwitchService {

	private final static Logger logger = LoggerFactory.getLogger(ZhiTuiAwardServiceImpl.class);

	@Resource
	private IAccountService accountService;
	@Resource
	private IUserService userService;
	@Resource
	private ILoanDictService loanDictService;
	@Resource
	private IBonusDailyService bonusDailyService;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void calAward(Integer userId, BigDecimal amount, Integer userLevel) {
		if (!isAwardSwitchOn(userId)) {
			return;
		}
		
		UserDo userDo = userService.getUser(userId);

		//推荐人
		UserDo refereeDo = userService.getUser(userDo.getRefereeid());

		if (refereeDo == null) { //根节点没有推荐人，报单时
			return;
		}

		if (!isBeforeLimitDate()) {
			logger.warn("当前时间超过系统设置直推奖截止日期");
			return;
		}

		//用户未激活，不统计业绩
		if (!refereeDo.isActivated()) {
			logger.info("推荐人未激活，不计算直推奖，refereeId:" + refereeDo.getId());
			return;
		}

		BigDecimal rate = getRecomandRate(refereeDo.getUserLevel().intValue());

		//直推奖=报单金额*奖金比例
		BigDecimal bonus = amount.multiply(rate).setScale(6, RoundingMode.HALF_UP);

		//封顶设置
		bonus = getBonusAmount(bonus, refereeDo);

		//更新用户当日已获取奖励金额
		bonusDailyService.updateAmount(refereeDo.getId(), IncomeType.TYPE_AWARD_TOUCH.getIncomeType() + "", new Date(), bonus);

		String remark = "";
		try {
			String pattern = "{0}报单，金额{1}，奖金比例{2}，{3}得直推奖{4}";
			remark = MessageFormat.format(pattern, userDo.getUserName(), amount, rate, refereeDo.getUserName(), bonus);
		} catch (Exception e) {
			logger.error("直推奖励备注异常：", e);
		}

		UserAccountDo accountDo = new UserAccountDo();
		accountDo.setUserId(refereeDo.getId());
		accountDo.setAmount(bonus);
		accountDo.setAccountType(AccountType.wallet_bonus.getAccountType());
		accountDo.setRemark(remark);
		accountService.updateUserAmountById(accountDo, IncomeType.TYPE_AWARD_REFEREE);
	}

	/**
	 * 查询直推比例设置
	 * @param userLevel
	 * @return
	 */
	private BigDecimal getRecomandRate(Integer userLevel) {
		LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.ZhiTui.getCode(), userLevel.toString());
		Assert.notNull(loanDictDtlDo, "未设置量碰比例");
		Assert.hasText(loanDictDtlDo.getRemark(), "未设置量碰比例");

		return new BigDecimal(loanDictDtlDo.getRemark());
	}

	/**
	 * 判断当前日期是否在截止日期之前
	 * @return
	 */
	private boolean isBeforeLimitDate() {
		try {
			LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.ZhiTuiTime.getCode(), "1");
			if (loanDictDtlDo == null || StringUtils.isBlank(loanDictDtlDo.getRemark())) {
				logger.warn("直推奖截止未设置");
				return true;
			}

			Date limitDate = DateUtil.YYYY_MM.parse(loanDictDtlDo.getRemark());
			Date now = new Date();
			logger.info("当前时间：" + now);
			logger.info("系统设置时间：" + limitDate);
			return now.before(limitDate);
		} catch (Exception e) {
			logger.error("查询直推截止日期异常：", e);
			return true;
		}
	}

	/**
	* 判断用户奖励是否已封顶 
	* @param bonus
	* @param userId
	* @return  
	*/
	private BigDecimal getBonusAmount(BigDecimal bonus, UserDo userDo) {
		LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.FengDin.getCode(), userDo.getUserLevel().toString());
		Assert.notNull(loanDictDtlDo, "奖金封顶未设置");
		Assert.notNull(loanDictDtlDo.getRemark(), "奖金封顶未设置");

		BigDecimal fengding = new BigDecimal(loanDictDtlDo.getRemark());

		//计算本次可量碰金额
		BigDecimal alreadyBonus = bonusDailyService.selectAmount(userDo.getId(), IncomeType.TYPE_AWARD_TOUCH.getIncomeType() + "");
		BigDecimal ableBonus = fengding.subtract(alreadyBonus);
		ableBonus = ableBonus.compareTo(BigDecimal.ZERO) > 0 ? ableBonus : BigDecimal.ZERO;

		if (ableBonus.compareTo(bonus) > 0) {
			return bonus;
		}

		return ableBonus;
	}
}
