package com.dce.business.service.award.test;

import javax.annotation.Resource;

import org.junit.Test;

import com.dce.business.service.award.AwardLaterService;
import com.dce.test.BaseTest;

public class AwardLaterServiceTest extends BaseTest {
	
    @Resource
    private AwardLaterService awardLaterService;
    
    @Test
    public void selectByPrimaryKey() {
        awardLaterService.selectByPrimaryKey(1);
    }
}
