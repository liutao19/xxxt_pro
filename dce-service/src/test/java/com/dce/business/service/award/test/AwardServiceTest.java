package com.dce.business.service.award.test;

import javax.annotation.Resource;

import org.junit.Test;

import com.dce.business.service.award.AwardLaterService;
import com.dce.business.service.award.IAwardService;
import com.dce.test.BaseTest;

public class AwardServiceTest extends BaseTest {
	
    @Resource
    private IAwardService awardService;
    
    @Test
    public void testCalcAward() {
    	Integer buyQty = 5;
		Long orderId = 1L;
		Integer buyUserId = 1;
		awardService.calcAward(buyUserId , buyQty, orderId);
    }
}
