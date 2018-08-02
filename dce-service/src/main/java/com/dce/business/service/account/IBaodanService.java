package com.dce.business.service.account;

import java.math.BigDecimal;

import com.dce.business.common.result.Result;

public interface IBaodanService {
    /**
     * 报单
     * @param userId
     * @param qty
     * @param accountType
     * @return
     */
    void baodan(Integer userId, BigDecimal qty, String accountType, Integer userLevel);

    /**
     * 原始仓复投
     * @param userId 用户id
     * @param accountType 账户类型：point 美元点,current 现持仓
     * @param qty 复投额度
     * @return
     */
    Result<?> reCast(Integer userId, BigDecimal qty, String accountType);
}
