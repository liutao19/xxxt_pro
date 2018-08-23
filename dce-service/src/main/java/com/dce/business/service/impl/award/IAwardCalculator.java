package com.dce.business.service.impl.award;

/**
 * 奖金计算的接口
 * @author harry
 *
 */
public interface IAwardCalculator  {
	
	/**
	 * 计算奖励的方法
	 * @param buyUserId 购买者
	 * @param buyQty    购买数量
	 * @param orderId   购买订单
	 * @return
	 */
	public void doAward(int buyUserId, int buyQty, Integer orderId);
	
}
