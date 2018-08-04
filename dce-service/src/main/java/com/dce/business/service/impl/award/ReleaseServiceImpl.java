package com.dce.business.service.impl.award;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.entity.user.UserStaticDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.award.IAwardSwitchService;
import com.dce.business.service.award.IReleaseService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.user.IUserService;
import com.dce.business.service.user.IUserStaticService;

@Service("releaseService")
public class ReleaseServiceImpl extends AwardSwitchServiceImpl implements IReleaseService, IAwardSwitchService {
	private final static Logger logger = LoggerFactory.getLogger(ReleaseServiceImpl.class);

	@Resource
	private IAccountService accountService;
	@Resource
	private IUserService userService;
	@Resource
	private ILoanDictService loanDictService;
	@Resource
	private IUserStaticService userStaticService;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void release(AccountType fromAccount, AccountType toAccount, Integer userId, BigDecimal amount) {
		if (!isReleaseSwitchOn(userId)) {
			return;
		}

		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			logger.warn("用户钱包余额已释放完毕，" + "userId:" + userId + ", fromAccount:" + fromAccount);
			return;
		}

		UserDo userDo = userService.getUser(userId);
		if (userDo.getUserLevel() == null || userDo.getUserLevel().intValue() <= 0) {
			logger.info("用户没有级别，不计算释放奖励, userId:" + userDo.getId());
			return;
		}

		BigDecimal rate = getReleaseRate(userDo.getUserLevel().intValue(), fromAccount);
		BigDecimal releaseAmount = amount.multiply(rate).setScale(6, RoundingMode.HALF_UP);
		IncomeType incomeType = getMessage(fromAccount);

		accountService.convertBetweenAccount(userDo.getId(), userDo.getId(), releaseAmount, releaseAmount, fromAccount.getAccountType(),
				toAccount.getAccountType(), incomeType, incomeType);
	}

	/**
	 * 查询释放比例设置
	 * @param userLevel
	 * @return
	 */
	private BigDecimal getReleaseRate(Integer userLevel, AccountType accountType) {
		if (AccountType.wallet_original_release.equals(accountType)) {
			LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.SecondShiFangRate.getCode(),
					DictCode.SecondShiFangRate.getCode());
			Assert.notNull(loanDictDtlDo, "未设置释放比例");
			Assert.hasText(loanDictDtlDo.getRemark(), "未设置释放比例");
			return new BigDecimal(loanDictDtlDo.getRemark());
		}
		
		LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(getDictCode(accountType), userLevel.toString());
		Assert.notNull(loanDictDtlDo, "未设置释放比例");
		Assert.hasText(loanDictDtlDo.getRemark(), "未设置释放比例");

		return new BigDecimal(loanDictDtlDo.getRemark());
	}

	private String getDictCode(AccountType accountType) {
		if (AccountType.wallet_original.equals(accountType)) {
			return DictCode.OrigShiFangRate.getCode();
		} else if (AccountType.wallet_interest.equals(accountType)) {
			return DictCode.DaysShiFangRate.getCode();
		} else if (AccountType.wallet_bonus.equals(accountType)) {
			return DictCode.AwardShiFangRate.getCode();
		}

		return "";
	}

	private IncomeType getMessage(AccountType accountType) {
		if (AccountType.wallet_original.equals(accountType)) {
			return IncomeType.TYPE_REALESE_ORIGINAL;
		} else if (AccountType.wallet_interest.equals(accountType)) {
			return IncomeType.TYPE_REALESE_INTEREST;
		} else if (AccountType.wallet_bonus.equals(accountType)) {
			return IncomeType.TYPE_REALESE_BONUS;
		} else if (AccountType.wallet_original_release.equals(accountType)) {
			return IncomeType.TYPE_REALESE_RELEASE;
		}

		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void release(UserStaticDo userStaticDo) {
		if (!isReleaseSwitchOn(userStaticDo.getUserid())) {
			return;
		}

		Integer userId = userStaticDo.getUserid();
		UserDo userDo = userService.getUser(userId);

		BigDecimal amount = getAmount(userStaticDo, userDo);
		if (amount.compareTo(BigDecimal.ZERO) <= 0 || userStaticDo.getYfBonus().compareTo(userStaticDo.getTotalmoney()) >= 0) {
			logger.warn("用户钱包余额已释放完毕，" + "userId:" + userId);
			return;
		}

		if (userDo.getUserLevel() == null || userDo.getUserLevel().intValue() <= 0) {
			logger.info("用户没有级别，不计算释放奖励, userId:" + userDo.getId());
			return;
		}

		//更新已释放金额
		userStaticService.updateStaticMoney(userStaticDo.getId());

		accountService.convertBetweenAccount(userDo.getId(), userDo.getId(), amount, amount, AccountType.wallet_original.getAccountType(),
				AccountType.wallet_original_release.getAccountType(), IncomeType.TYPE_REALESE_ORIGINAL, IncomeType.TYPE_REALESE_ORIGINAL);

	}

	/**
	 * 每次释放都是按照比例，避免空单升级这种
	 * @param userStaticDo
	 * @param userDo
	 * @return
	 */
	private BigDecimal getAmount(UserStaticDo userStaticDo, UserDo userDo) {
		LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.OrigShiFangRate.getCode(), userDo.getUserLevel().toString());
		Assert.notNull(loanDictDtlDo, "未设置释放比例");
		Assert.hasText(loanDictDtlDo.getRemark(), "未设置释放比例");

		BigDecimal totalAmount = userStaticDo.getTotalmoney();
		BigDecimal rate = new BigDecimal(loanDictDtlDo.getRemark());

		BigDecimal amount = totalAmount.multiply(rate).setScale(6, RoundingMode.HALF_UP);
		return amount;
	}
}
