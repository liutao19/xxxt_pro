package com.dce.business.service.award.test;

import javax.annotation.Resource;

import org.junit.Test;

import com.dce.business.service.award.IAwardService;
import com.dce.test.BaseTest;

public class AwardServiceTest extends BaseTest {
	
    @Resource
    private IAwardService awardService;
    
    @Test
    public void testCalcAward() {
    	Integer buyQty = 1;
		Long orderId = 10L;
		Integer buyUserId =710;
		awardService.calcAward(buyUserId , buyQty, orderId);
    }
}
