package com.dce.business.service.impl.award;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.award.Awardlist;
import com.dce.business.entity.order.Order;
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.award.IAwardlistService;
import com.dce.business.service.user.IUserService;


/**
 * 购买用户奖金计算类
 * @author harry
 *
 */
@Service("buyerAwardCalculator")
public class BuyerAwardCalculator implements IAwardCalculator {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Resource
	private IAwardlistService awardlistService;
	
	@Resource
	private IUserService userService;
	
	// 账户
	@Resource
	private IAccountService accountService;

	/**
	 * 根据购买者购买数量确定用户会员等级和给会员的奖励
	 * 计算奖励的方法
	 * @param buyUserId 购买者
	 * @param orderId   购买订单
	 * @return
	 */
	@Override
	public void doAward(UserDo buyer, Order order) {
		
		// 得到奖励记录
		Awardlist award = awardlistService.getAwardConfigByQtyAndBuyerLevel(buyer.getUserLevel(),order.getQty());
		
		if(award == null){
			throw new BusinessException("找不到购买者对应的奖励办法，请检查奖励办法的配置","error-buyerAward-001");
		}
		
		String buyerAward = award.getBuyerAward();
		if(StringUtils.isBlank(buyerAward)){
			throw new BusinessException("购买者对应的奖励办法没有正确配置，请检查奖励办法的配置","error-buyerAward-002");
		}
		
		//多种奖励办法以;分隔
		String[]  bAwardLst = buyerAward.split(";");
		oneAward(buyer.getId(),bAwardLst);
		
	}

	
	/**
	 * 逐个奖励处理
	 * @param buyUserId
	 * @param bAwardLst
	 */
	private void oneAward(int buyUserId, String[] bAwardLst) {
		for(String oneAward : bAwardLst){
			//奖励金额
			BigDecimal wardAmount = getAmtByAward(oneAward);
			
			//奖励进入那个账户类型
			String accountType = getAccountTypeByAward(oneAward);
			
			//是否首次才能奖励
		//	boolean isFirst = getIsFirstByAward(oneAward);
			
			/*
			//仅首次才给的奖励
			if(isFirst){
				//给每个奖励一个编号，根据编号查询是否已经获取过该奖励
				String awardNo = getAwardNoByAward(oneAward);
				if(userIsFirst(buyUserId,awardNo,accountType) == false){
					return;
				}
			}
			*/
			if(wardAmount.compareTo(BigDecimal.ZERO)>0){
				UserAccountDo accont = new UserAccountDo(wardAmount, buyUserId,accountType);
				//账户对象增加金额
				accountService.updateUserAmountById(accont, IncomeType.TYPE_AWARD_BUYER);
			}
		}
	}
	
	
	/**
	 * 判断用户是否是 首次获取该奖励
	 * @param buyUserId
	 * @param awardNo
	 * @return
	 */
	/*private boolean userIsFirst(int buyUserId, String awardNo,String accountType) {
		UserAccountDo userAccount = accountService.selectUserAccount(buyUserId, accountType);
		//TODO 判断是否存在
		
		return false;
	}*/


	/**
	 * 根据配置 用 - 分隔 ，获取奖励次数或金额，如果没有配置报错
	 * 配置格式： 1-wallet_travel-isFirst-A001-4人港澳游       表示 1次，旅游账户  奖励  4人港澳游 ， wallet_travel 查看{@link AccountType}
	 * @param oneAward
	 * @return
	 */
	private String getAwardNoByAward(String oneAward) {
		String[] awds = oneAward.split("-");
		if(awds.length<5){
			throw new BusinessException("购买者对应的奖励办法没有正确配置，请检查奖励办法的配置","error-buyerAward-005");
		}
		return awds[4].trim();
	}


	/**
	 * 根据配置 用 - 分隔 ，获取奖励次数或金额，如果没有配置报错
	 * 配置格式： 1-wallet_travel-isFirst-A001-4人港澳游       表示 1次，旅游账户  奖励  4人港澳游 ， wallet_travel 查看{@link AccountType}
	 * @param oneAward
	 * @return
	 */
	private boolean getIsFirstByAward(String oneAward) {
		String[] awds = oneAward.split("-");
		if(awds.length<4){
			throw new BusinessException("购买者对应的奖励办法没有正确配置，请检查奖励办法的配置","error-buyerAward-005");
		}
		return "isFirst".equalsIgnoreCase(awds[3].trim());
	}


	/**
	 * 根据配置 用 - 分隔 ，获取奖励次数或金额，如果没有配置报错
	 * 配置格式： 1-wallet_travel-isFirst-A001-4人港澳游       表示 1次，旅游账户  奖励  4人港澳游 ， wallet_travel 查看{@link AccountType}
	 * 
	 * @param oneAward
	 * @return
	 */
	private BigDecimal getAmtByAward(String oneAward) {
		String[] awds = oneAward.split("-");
		if(awds.length<2){
			throw new BusinessException("购买者对应的奖励办法没有正确配置，请检查奖励办法的配置","error-buyerAward-003");
		}
		return new BigDecimal(awds[0].trim());
	}

	/**
	 * 根据配置 用 - 分隔 ，配置进什么账户类型，如果没有配置报错
	 * 配置格式： 1-wallet_travel-4人港澳游       表示 1次，旅游账户  奖励  4人港澳游 ， wallet_travel 查看{@link AccountType}
	 * 
	 * @param oneAward
	 * @return
	 */
	private String getAccountTypeByAward(String oneAward) {
		String[] awds = oneAward.split("-");
		if(awds.length<2){
			throw new BusinessException("购买者对应的奖励办法没有正确配置，请检查奖励办法的配置","error-buyerAward-004");
		}
		return awds[1];
	}

}
