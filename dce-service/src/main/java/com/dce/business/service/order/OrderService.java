package com.dce.business.service.order;

import java.util.List;

import com.dce.business.entity.order.Order;

public interface OrderService {

	/**
	 * 获取所有订单
	 * @return
	 */
	List<Order> selectByUserId(Integer userId);
}
