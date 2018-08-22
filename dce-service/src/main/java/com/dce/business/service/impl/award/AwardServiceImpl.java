package com.dce.business.service.impl.award;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.dce.business.service.award.IAwardService;

@Service("awardCalculator")
public class AwardServiceImpl implements IAwardService {

	private final static Logger logger = LoggerFactory.getLogger(AwardServiceImpl.class);
	
	private List<IAwardCalculator> awardCalculatorList;
	
	
	

	public void setAwardCalculatorList(List<IAwardCalculator> awardCalculatorList) {
		this.awardCalculatorList = awardCalculatorList;
	}


	
	public void calcAward(Integer buyUserId, Integer buyQty, Integer orderId) {
		Assert.notNull(buyUserId, "购买者用户ID不能为空");
		Assert.notNull(buyQty, "购买数量不能为空");
		Assert.notNull(orderId, "购买订单ID不能为空");
		for(IAwardCalculator awardCalc : awardCalculatorList){
			awardCalc.doAward(buyUserId, buyQty, orderId);
		}
	}
	
}
