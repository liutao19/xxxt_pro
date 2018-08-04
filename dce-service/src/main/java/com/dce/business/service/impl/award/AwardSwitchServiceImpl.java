package com.dce.business.service.impl.award;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.dce.business.common.enums.CurrencyType;
import com.dce.business.entity.dict.CtCurrencyDo;
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.award.IAwardSwitchService;
import com.dce.business.service.dict.ICtCurrencyService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.user.IUserService;

@Service("awardSwitchService")
public class AwardSwitchServiceImpl implements IAwardSwitchService {
	private final static Logger logger = LoggerFactory.getLogger(AwardSwitchServiceImpl.class);
	@Resource
	private ICtCurrencyService ctCurrencyService;
	@Resource
	private IUserService userService;
	@Resource
	private ILoanDictService loanDictService;

	@Override
	public boolean isAwardSwitchOn(Integer userId) {
		CtCurrencyDo ctCurrencyDo = ctCurrencyService.selectByName(CurrencyType.IBAC.name());
		if (ctCurrencyDo != null && ctCurrencyDo.getIs_award_switch() != null && ctCurrencyDo.getIs_award_switch() == 1) {
			return true;
		}

		logger.info("奖金开关已关闭，不计算奖金");

		try {
			//判断奖金开关关闭以后，是否在白名单以内
			LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl("awardSwitchWhiteList", "awardSwitchWhiteList");
			Assert.notNull(loanDictDtlDo, "未设置奖金开关");
			Assert.hasText(loanDictDtlDo.getRemark(), "未设置奖金开关");

			UserDo userDo = userService.getUser(userId);
			String userName = userDo.getUserName() + ";";
			if (loanDictDtlDo.getRemark().indexOf(userName) > -1) {
				return true;
			}
		} catch (Exception e) {
			logger.info("未设置奖金开关");
		}

		return false;
	}
	
	@Override
	public boolean isReleaseSwitchOn(Integer userId) {
		CtCurrencyDo ctCurrencyDo = ctCurrencyService.selectByName(CurrencyType.IBAC.name());
		if (ctCurrencyDo != null && ctCurrencyDo.getIs_shifang_switch() != null && ctCurrencyDo.getIs_shifang_switch() == 1) {
			return true;
		}

		logger.info("释放开关已关闭，不计算奖金");

		try {
			//判断奖金开关关闭以后，是否在白名单以内
			LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl("awardSwitchWhiteList", "awardSwitchWhiteList");
			Assert.notNull(loanDictDtlDo, "未设置奖金开关");
			Assert.hasText(loanDictDtlDo.getRemark(), "未设置奖金开关");

			UserDo userDo = userService.getUser(userId);
			String userName = userDo.getUserName() + ";";
			if (loanDictDtlDo.getRemark().indexOf(userName) > -1) {
				return true;
			}
		} catch (Exception e) {
			logger.info("未设置释放开关");
		}

		return false;
	}

}
