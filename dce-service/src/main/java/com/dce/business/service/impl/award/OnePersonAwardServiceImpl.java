package com.dce.business.service.impl.award;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.user.UserStaticDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.user.IUserService;
import com.dce.business.service.user.IUserStaticService;

@Service("onePersonAwardService")
public class OnePersonAwardServiceImpl implements OnePersonStaticService {
    private final static Logger logger = Logger.getLogger(OnePersonAwardServiceImpl.class);

    @Resource
    private IAccountService accountService;
    @Resource
    private IUserService userService;
    @Resource
    private IUserStaticService userStaticService;

    
	/* (non-Javadoc)
	 * @see com.dce.business.service.impl.award.OnePersonStaticService#calOnePersonStaticAward(com.dce.business.entity.user.UserStaticDo)
	 */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void calOnePersonStaticAward(UserStaticDo order) {
    	
    	//不释放的用户过滤
//    	boolean isGo = userStaticService.getUnStaticAward(order.getUserid());
//    	if(isGo == false){
//    		return;
//    	}
    	
		BigDecimal money = order.getMoney(); //每日返利金额

		//1、更新现持仓账户数据
		UserAccountDo accountDo = new UserAccountDo();
		accountDo.setUserId(order.getUserid());
		accountDo.setAmount(money);
		accountDo.setAccountType(AccountType.current.getAccountType());
		accountService.updateUserAmountById(accountDo, IncomeType.TYPE_STATIC);
		//更新原始仓余额
		UserAccountDo originalDo = new UserAccountDo();
		originalDo.setUserId(order.getUserid());
		originalDo.setAmount(money.negate());
		originalDo.setAccountType(AccountType.original.getAccountType());
		accountService.updateUserAmountById(originalDo, IncomeType.TYPE_STATIC);
		
		//2、更新静态表
		userStaticService.updateStaticMoney(order.getId());

		//3、更新用户的每日释放总静态
		userService.updateStatic(money, order.getUserid());
	}
}
