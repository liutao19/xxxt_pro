package com.dce.business.service.impl.award;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dce.business.dao.bonus.IBonusLogDao;
import com.dce.business.entity.bonus.BonusLogDo;
import com.dce.business.service.award.IAwardService;

@Service("awardServiceAsync")
public class AwardServiceAsyncImpl implements IAwardService {
    private final static Logger logger = LoggerFactory.getLogger(AwardServiceAsyncImpl.class);

    @Resource(name = "awardService")
    private IAwardService awardService;
    @Resource
    private IBonusLogDao bonusLogDao;

    @Async
    @Override
    public void calAward(Integer userId, BigDecimal amount, Integer userLevel) {
        try {
            awardService.calAward(userId, amount, userLevel);
        } catch (Exception e) {
        	logger.error("计算奖金异常："+userId);
            logger.error("计算奖金异常：", e);
            BonusLogDo bonusLogDo = new BonusLogDo();
            bonusLogDo.setUserId(userId);
            bonusLogDo.setAmount(amount);
            bonusLogDo.setUserLevel(userLevel);
            bonusLogDo.setCreateTime(new Date());
            bonusLogDo.setUpdateTime(new Date());
            bonusLogDao.insertSelective(bonusLogDo);
        }
    }

}
