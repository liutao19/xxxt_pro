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
    @Resource(name = "lingDaoAwardService")
    private IAwardService lingDaoAwardService;
    @Resource(name = "huZhuAwardService")
    private IAwardService huZhuAwardService;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void calAward(Integer userId, BigDecimal amount, Integer userLevel) {
        liangPengAwardService.calAward(userId, amount, userLevel);
        zhiTuiAwardService.calAward(userId, amount, userLevel);
        lingDaoAwardService.calAward(userId, amount, userLevel);
        huZhuAwardService.calAward(userId, amount, userLevel);
    }
}
