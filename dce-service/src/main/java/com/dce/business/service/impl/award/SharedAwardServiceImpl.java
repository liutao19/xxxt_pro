package com.dce.business.service.impl.award;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.DictCode;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.award.IAwardService;
import com.dce.business.service.award.IAwardSwitchService;
import com.dce.business.service.bonus.IBonusDailyService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.user.IUserService;
import com.dce.business.service.user.IUserStaticService;

/**
 * 小区分享奖
 */
@Service("sharedAwardService")
public class SharedAwardServiceImpl extends AwardSwitchServiceImpl implements IAwardService, IAwardSwitchService {

	private final static Logger logger = LoggerFactory.getLogger(SharedAwardServiceImpl.class);

	@Resource
	private IUserService userService;
	@Resource
	private IAccountService accountService;
	@Resource
	private ILoanDictService loanDictService;
	@Resource
	private IUserStaticService userStaticService;
	@Resource
	private IBonusDailyService bonusDailyService;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void calAward(Integer userId, BigDecimal amount, Integer userLevel) {
		logger.info("计算小区分享奖，userId:" + userId);

		if (!isAwardSwitchOn(userId)) {
			return;
		}

		UserDo userDo = userService.getUser(userId);
		if (userDo.getUserLevel() == null || userDo.getUserLevel().intValue() <= 0) {
			logger.info("用户没有级别，不计算分享奖, userId:" + userDo.getId());
		}

		//查询分享奖比例
		BigDecimal rate = getSharedRate(userDo.getUserLevel().intValue());
		BigDecimal bonus = amount.multiply(rate).setScale(6, RoundingMode.HALF_UP);

		//封顶设置
		bonus = getBonusAmount(bonus, userDo);

		//更新用户当日已获取奖励金额
		bonusDailyService.updateAmount(userDo.getId(), IncomeType.TYPE_AWARD_TOUCH.getIncomeType() + "", new Date(), bonus);

		UserAccountDo accountDo = new UserAccountDo();
		accountDo.setUserId(userId);
		accountDo.setAmount(bonus);
		accountDo.setAccountType(AccountType.wallet_bonus.getAccountType());
		accountService.updateUserAmountById(accountDo, IncomeType.TYPE_SHARED);
	}

	/**
	 * 查询分享奖比例设置
	 * @param userLevel
	 * @return
	 */
	private BigDecimal getSharedRate(Integer userLevel) {
		LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.ShareRate.getCode(), userLevel.toString());
		Assert.notNull(loanDictDtlDo, "未设置分享奖比例");
		Assert.hasText(loanDictDtlDo.getRemark(), "未设置分享奖比例");

		return new BigDecimal(loanDictDtlDo.getRemark());
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
