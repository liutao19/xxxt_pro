package com.dce.business.actions.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.actions.common.BaseController;
import com.dce.business.common.enums.AccountType;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.DateUtil;
import com.dce.business.entity.account.UserAccountDetailDo;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.service.account.IAccountService;

/**
 * 交易流水控制类
 * @author Administrator
 *
 */
@RestController
@RequestMapping("accountRecord")
public class AccountRecordController extends BaseController {

	private final static Logger logger = Logger.getLogger(AccountController.class);
	
	@Resource
	private IAccountService accountService;
	
	/**
	 * 获取用户交易流水记录
	 * @return
	 */
	@RequestMapping(value = "/selectAccountRecord", method=RequestMethod.GET)
	public Map<String,Object> selectAccountRecord(){
		
		Integer userId = getUserId();
		logger.info("获取当前用户的id："+userId);
		List<UserAccountDetailDo> list = accountService.selectUserAccountDetailByUserId(userId);
		
		List<Map<String,Object>> accountlist = new ArrayList<>();
		for(UserAccountDetailDo userAccountDetail : list){
			Map<String,Object> map = new HashMap<>();
			map.put("id", userAccountDetail.getId());
			map.put("userId", userAccountDetail.getUserId());
			map.put("amount", userAccountDetail.getAmount());
			map.put("moreOrLess", userAccountDetail.getMoreOrLess());
			map.put("balanceAmount", userAccountDetail.getBalanceAmount());
			map.put("createTime", DateUtil.dateToString(userAccountDetail.getCreateTime()));
			map.put("remark", userAccountDetail.getRemark());
			map.put("seqId", userAccountDetail.getSeqId());
			accountlist.add(map);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("balance", accountService.getUserAmount(userId)); //当前用户的账户余额
		map.put("data", accountlist);
		map.put("code", "0");
		map.put("msg", "获取交易流水记录成功");
		
		return map;
	}
	
}
