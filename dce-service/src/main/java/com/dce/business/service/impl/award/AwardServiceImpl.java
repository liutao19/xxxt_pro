package com.dce.business.service.impl.award;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dce.business.service.award.IAwardService;

@Service("awardService")
public class AwardServiceImpl implements IAwardService {
    @Resource(name = "liangPengAwardService")
    private IAwardService liangPengAwardService;
    @Resource(name = "zhiTuiAwardService")
    private IAwardService zhiTuiAwardService;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void calAward(Integer userId, BigDecimal amount, Integer userLevel) {
    	//计算业绩
        liangPengAwardService.calAward(userId, amount, userLevel);
        
        //计算直推奖
        zhiTuiAwardService.calAward(userId, amount, userLevel);
    }
}
