package com.dce.business.actions.account;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.actions.common.BaseController;
import com.dce.business.common.result.Result;
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
	public Result<?> selectAccountRecord(){
		
		Integer userId = getUserId();
		logger.info("获取当前用户的id："+userId);
		List<UserAccountDetailDo> list = accountService.selectUserAccountDetailByUserId(userId);
		
		return Result.successResult("获取交易流水记录成功", list);
	}
	
	
}
