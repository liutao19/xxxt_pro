package com.dce.business.service.award.test;


import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.dce.business.dao.user.IUserRefereeDao;
import com.dce.business.service.award.IAwardService;
import com.dce.test.BaseTest;

public class AwardServiceTest extends BaseTest {
	
    @Resource
    private IAwardService awardService;
    @Resource
    private IUserRefereeDao userreferee;
    
    @Test
<<<<<<< HEAD
   @Rollback(false)
    public void testCalcAward() {
    	Integer buyQty = 5;
		Integer orderId = 97;
		Integer buyUserId =724;
		awardService.calcAward(buyUserId , orderId);
=======
    @Rollback(false)
    public void testCalcAward() {
		Integer orderId = 20;
		Integer buyUserId =711;
		awardService.calcAward(buyUserId ,orderId);
>>>>>>> cd10215ca9db558f3b148e9487e2d3d921607e84
    }
    
}
