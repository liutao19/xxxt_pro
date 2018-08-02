package com.dce.business.service.account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.common.result.Result;
import com.dce.business.entity.account.UserAccountDetailDo;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.user.UserDo;

public interface IAccountService {

    /** 
     * 新建账户
     * @param userAccountDo
     * @return  
     */
    boolean createAccount(UserAccountDo userAccountDo);

    /**
     * 查询用户账户信息 
     * @param userId
     * @param accountType
     * @return  
     */
    UserAccountDo getUserAccount(Integer userId, AccountType accountType);

    UserAccountDo selectUserAccount(Integer userId, String accountType);

    int updateUserAmountById(UserAccountDo userAccountDo, IncomeType type);

    void convertBetweenAccount(Integer sourceUserId, Integer targetUserId, BigDecimal amount, String fromAccount, String toAccount,
            IncomeType sourceMsg, IncomeType targetMsg);

//    /**
//     * 账户间转账
//     * @param sourceUserId  转出用户id
//     * @param targetUserId 转入用户id
//     * @param amount 转出金额
//     * @param fromAccount 转出的账户类型
//     * @param toAccount 转入的账户类型
//     * @param sourceMsg 转出流水类型
//     * @param targetMsg 转入流水类型
//     * @param addAmount 转入金额  (原始仓与现持仓直接转换时与转出金额一致  ，原始仓、现持仓与美元点之间转换时需要按比例计算)
//     */
//    void convertBetweenAccount(Integer sourceUserId, Integer targetUserId, BigDecimal amount, String fromAccount, String toAccount,
//            IncomeType sourceMsg, IncomeType targetMsg, BigDecimal addAmount);

    List<UserAccountDetailDo> selectUserAccountDetail(Map<String, Object> params);

    Result<?> currentInit(Integer userId,AccountType type);

    /**
     * 原始仓加金
     * @param userId 加金用户
     * @param qty 加金金额
     * @return
     */
    Result<?> currentAddMoney(Integer userId, BigDecimal qty);

    /**
     * 美元点转出
     * @param userId 用户id
     * @param qty 转出额度
     * @param receiver 接收人
     * @return
     */
    Result<?> pointOut(Integer userId, BigDecimal qty, String receiver);

    /** 
     * 查询以太坊账户余额
     * @param userId
     * @return  
     */
    BigDecimal getEthernumAmount(Integer userId);

    /** 
     * 充值
     * @param userId
     * @param password
     * @param qty
     * @return  
     */
    Result<?> recharge(Integer userId, String password, BigDecimal qty);

    
    /**
     * 审批提现 
     * @param userId
     * @param password
     * @param qty
     * @return  
     */
    Result<?> withdraw(Integer withdrawId,Integer userId,  BigDecimal qty);
    
    /**
     * 提现 
     * @param userId
     * @param password
     * @param qty
     * @return  
     */
    Result<?> withdraw(Integer userId, String password, BigDecimal qty);
     
    /**   
     * 定时任务处理以太坊转账结果
     */
    void comfirmEthTransResult();

    /**
     * 我的收款码
     * @param userId
     * @return
     */
	String getMyQRCode(Integer userId);

	/**
	 * 扫描支付
	 * @param userId
	 * @param qrCode
	 * @param amount
	 * @param pwd
	 * @return
	 */
	Result<?> payByQRCode(Integer userId, String qrCode, String amount,
			String pwd);

	/**
	 * 根据收款码获取收款人信息
	 * @param qrcode
	 * @return
	 */
	UserDo getReceiverQRCode(String qrcode);

	/**
	 * 发送
	 * @param userId
	 * @param qrCode
	 * @param amount
	 * @param pwd
	 * @return
	 */
	Result<?> send(Integer userId, String receiveAddress, String amount, String pwd);

	/**
	 * 购买商品支付
	 * @param userId
	 * @param totalPrice
	 * @return
	 */
	Result<?> buyGoods(Integer userId, BigDecimal totalPrice);
}
