package com.dce.business.actions.account;

import java.math.BigDecimal;
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
import com.dce.business.common.util.DateUtil;
import com.dce.business.entity.account.UserAccountDetailDo;
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
		
		Map<String,Object> map = new HashMap<String,Object>();
		Integer userId = getUserId();
		logger.info("获取当前用户的id："+userId);
		List<UserAccountDetailDo> list = accountService.selectUserAccountDetailByUserId(userId);
		
		if(list.isEmpty() || list.size() ==0){
			map.put("code", "0");
			map.put("msg", "用户交易流水记录为空");
			map.put("data", "");
			return map;
		}
		
		List<Map<String,Object>> accountlist = new ArrayList<>();
		for(UserAccountDetailDo userAccountDetail : list){
			Map<String,Object> map2 = new HashMap<>();
			map2.put("id", userAccountDetail.getId());
			map2.put("userId", userAccountDetail.getUserId());
			map2.put("amount", userAccountDetail.getAmount());
			map2.put("moreOrLess", userAccountDetail.getMoreOrLess());
			map2.put("balanceAmount", userAccountDetail.getBalanceAmount());
			map2.put("createTime", DateUtil.dateToString(userAccountDetail.getCreateTime()));
			map2.put("remark", userAccountDetail.getRemark());
			map2.put("seqId", userAccountDetail.getSeqId());
			accountlist.add(map2);
		}
		logger.info("======获取的交易流水记录======》》》"+accountlist);
		BigDecimal balance = accountService.getUserAmount(userId);
		map.put("balance", balance); //当前用户的账户余额
		map.put("data", accountlist);
		
		// 查询当前账户的最新总余额失败
		if(balance.compareTo(BigDecimal.ZERO) == 0){
			map.put("code", "0");
			map.put("msg", "获取当前账户的总余额为0");
			map.put("data", "");
			return map;
		}
		map.put("code", "0");
		map.put("msg", "获取交易流水记录成功");
		
		return map;
	}
	
}
