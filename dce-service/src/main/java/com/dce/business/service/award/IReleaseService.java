package com.dce.business.service.award;

import java.math.BigDecimal;

import com.dce.business.common.enums.AccountType;
import com.dce.business.entity.user.UserStaticDo;

public interface IReleaseService {

    /**
     * 计算释放奖励   <br/>
     * 日息释放、奖金释放
     * @param fromAccount 
     * @param toAccount 
     * @param userId
     * @param amount
     */
    void release(AccountType fromAccount, AccountType toAccount, Integer userId, BigDecimal amount);
    
    void release(UserStaticDo userStaticDo);
}
