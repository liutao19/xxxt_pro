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
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.award.IAwardlistService;
import com.dce.business.service.user.IUserService;

/**
 * 推荐人奖金计算类
 * @author harry
 *
 */
@Service("refereeAwardCalculator")
public class RefereeAwardCalculator implements IAwardCalculator {

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
	 * @param buyQty    购买数量
	 * @param orderId   购买订单
	 * @return
	 */
	@Override
	public void doAward(int buyUserId, int buyQty, Long orderId) {
		
		UserDo  buyer = userService.getUser(buyUserId);
		
		//获取推荐人
		UserDo ref1 = userService.getUser( buyer.getRefereeid());
		if(ref1 == null){
			logger.info("会员userId="+buyUserId+"购买订单id="+orderId +"推荐人没有查找到");
			return;
		}
		//第二个推荐人
		UserDo ref2 = userService.getUser( ref1.getRefereeid());
		if(ref2 == null){
			logger.info("会员userId="+buyUserId+"购买订单id="+orderId +"推荐人没有查找到");
		}
					
		// 得到奖励记录
		Awardlist award = awardlistService.getAwardConfigByQtyAndBuyerLevel(buyer.getUserLevel(),buyQty);
		if(award == null){
			throw new BusinessException("找不到购买者对应的奖励办法，请检查奖励办法的配置","error-refereeAward-001");
		}
		
		
		if(ref1 != null){
			String awardConf = getAwardConfByRefLevel(ref1.getUserLevel(),1,award);
			//多种奖励办法以;分隔
			String[]  bAwardLst = awardConf.split(";");
			oneAward(ref1.getId(), bAwardLst);
		}
		
		//推荐人为空，下一个
		if(ref2 != null){
			String awardConf = getAwardConfByRefLevel(ref2.getUserLevel(),2,award);
			//多种奖励办法以;分隔
			String[]  bAwardLst = awardConf.split(";");
			oneAward(ref2.getId(), bAwardLst);
		}
		
		
		
	}


	/**
	 * 逐个奖励处理
	 * @param buyUserId
	 * @param bAwardLst
	 */
	private void oneAward(int buyUserId, String[] bAwardLst) {
		for(String oneAward : bAwardLst){
			//奖励金额
			BigDecimal wardAmount = getAmtByAwardNo(oneAward);
			String accountType = getAccountTypeByAwardNo(oneAward);
			
			if(wardAmount.compareTo(BigDecimal.ZERO)>0){
				UserAccountDo accont = new UserAccountDo(wardAmount, buyUserId,accountType);
				//账户对象增加金额
				accountService.updateUserAmountById(accont, IncomeType.TYPE_AWARD_REFEREE);
			}
		}
	}

	
	/**
	 * 
	 * @param refUser 推荐用户
	 * @param refSort 推荐人顺序 1： 第一推荐人， 2：第二推荐人
	 * @return
	 */
	private String getAwardConfByRefLevel(byte refUserLevel,int refSort,Awardlist award) {
		String awardConf = null;
		if(1== refSort){
			switch (refUserLevel) {
			case 0:
				awardConf = award.getP1Level0();
				break;
			case 1:
				awardConf = award.getP1Level1();
				break;
			case 2:
				awardConf = award.getP1Level2();
				break;
			case 3:
				awardConf = award.getP1Level3();
				break;
			case 4:
				awardConf = award.getP1Level4();
				break;
			}
		}
		if(2 == refSort){
			switch (refUserLevel) {
			case 0:
				awardConf = award.getP2Level0();
				break;
			case 1:
				awardConf = award.getP2Level1();
				break;
			case 2:
				awardConf = award.getP2Level2();
				break;
			case 3:
				awardConf = award.getP2Level3();
				break;
			case 4:
				awardConf = award.getP2Level4();
				break;
			}
		}
		
		if(awardConf == null){
			throw new BusinessException("购买者对应的奖励办法没有正确配置，请检查奖励办法的配置","error-refereeAward-002");
		}
		return awardConf;
	}


	/**
	 * 根据配置 用 - 分隔 ，获取奖励次数或金额，如果没有配置报错
	 * 配置格式： 1-wallet_travel-4人港澳游       表示 1次，旅游账户  奖励  4人港澳游 ， wallet_travel 查看{@link AccountType}
	 * 
	 * @param oneAward
	 * @return
	 */
	private BigDecimal getAmtByAwardNo(String oneAward) {
		String[] awds = oneAward.split("-");
		if(awds.length<2){
			throw new BusinessException("购买者对应的奖励办法没有正确配置，请检查奖励办法的配置","error-refereeAward-003");
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
	private String getAccountTypeByAwardNo(String oneAward) {
		String[] awds = oneAward.split("-");
		if(awds.length<2){
			throw new BusinessException("购买者对应的奖励办法没有正确配置，请检查奖励办法的配置","error-refereeAward-004");
		}
		return awds[1];
	}


}