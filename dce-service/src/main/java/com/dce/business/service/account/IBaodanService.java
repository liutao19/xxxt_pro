package com.dce.business.service.account;

import java.math.BigDecimal;

import com.dce.business.common.result.Result;
import com.dce.business.entity.user.UserDo;

public interface IBaodanService {
    /**
     * 报单
     * @param userId
     * @param cashAmount
     * @param scoreAmount
     * @param userLevel
     * @return
     */
    void baodan(Integer userId, BigDecimal cashAmount, BigDecimal scoreAmount, Integer userLevel);
    
    /**
     * 用户报单升级  或空单激活
     * 用户空单激活 或用户非空单等级修改时  都需要修改用户的释放表中的记录  改变其业绩和释放量
     * @param userId
     * @param userLevel
     * @return
     */
    Result<?> upgradeLevel(UserDo userDo,Integer userLevel);

}
