package com.dce.business.service.award;

import java.math.BigDecimal;

public interface IAwardService {

    /**
     * 计算推荐奖、领导奖、碰撞奖 
     * @param userId
     * @param amount  
     */
    void calAward(Integer userId, BigDecimal amount,Integer userLevel);
}
