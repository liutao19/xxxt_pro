package com.dce.business.service.third;

import java.math.BigDecimal;
import java.util.Map;

import com.dce.business.common.result.Result;
import com.dce.business.entity.etherenum.EthereumAccountDo;

public interface IEthereumService {

    /** 
     * 以太坊开户
     * @param userId 用户id
     * @param password 以太坊密码
     * @return  
     */
    Result<?> creatAccount(Integer userId, String password);

    /** 
     * 查询市场行情<br/>
     * usd ---美元价格</br>
     * btc ---比特币价格</br>
     * gasPrice  ---gas(交易费)单位价格, 以10e-18以太币为单位  </br>
     * @return  
     */
    BigDecimal getMarketPrice();

    /**
     * 根据用户id查询用户的以太坊账户
     * @param userId
     * @return
     */
    EthereumAccountDo getByUserId(Integer userId);

    /** 
     * 以太坊转账
     * @param fromAccount 转出账户地址
     * @param toAccount 转入账户地址
     * @param password 以太坊密码
     * @param amount 转账数额
     * @param gas 交易费
     * @param gasLimit 最大交易费
     * @return  
     */
//    Result<?> trans(String fromAccount, String toAccount, String password, BigDecimal amount, BigDecimal gas, BigDecimal gasLimit);

    
    /** 
     * 以太坊转账
     * @param userId 用户id
     * @param fromAccount 转出账户地址
     * @param toAccount 转入账户地址
     * @param password 以太坊密码
     * @param amount 转账数额
     * @param poinitAmount 美元点金额
     * @param type 类型
     * @param fee  提现手续费
     * @return  
     */
    Result<?> trans(Integer userId, String fromAccount, String toAccount, String password, BigDecimal amount, String pointAmount, Integer type,BigDecimal fee,Integer withdrawId);
    
    /** 
     * 以太坊转账
     * @param userId 用户id
     * @param fromAccount 转出账户地址
     * @param toAccount 转入账户地址
     * @param password 以太坊密码
     * @param amount 转账数额
     * @param poinitAmount 美元点金额
     * @param type 类型
     * @param fee  提现手续费
     * @return  
     */
    Result<?> trans(Integer userId, String fromAccount, String toAccount, String password, BigDecimal amount, String pointAmount, Integer type,BigDecimal fee);

    /**
     * 转账时返回的hash值 
     * @param hash
     * @return  
     */
    Result<?> getTransResult(String hash);

    /**
     * 查询账户余额 
     * @param account
     * @return  
     */
    Map<String, String> getBalance(String account);
}
