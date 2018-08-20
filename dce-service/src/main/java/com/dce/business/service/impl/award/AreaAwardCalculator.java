package com.dce.business.service.impl.award;

import org.springframework.stereotype.Service;

/**
 * 区域奖金计算类
 * @author harry
 *
 */
@Service("areaAwardCalculator")
public class AreaAwardCalculator implements IAwardCalculator {

	/**
	 * 计算奖励的方法
	 * @param buyUserId 购买者
	 * @param buyQty    购买数量
	 * @param orderId   购买订单
	 * @return
	 */
	@Override
	public void doAward(int buyUserId, int buyQty, Long orderId) {
	}

}
