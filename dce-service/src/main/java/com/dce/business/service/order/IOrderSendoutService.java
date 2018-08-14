
package com.dce.business.service.order;

import java.util.List;
import java.util.Map;

import com.dce.business.entity.order.OrderSendoutDo;
import com.dce.business.entity.page.PageDo;

public interface IOrderSendoutService {

	/**
	 * 根据ID 查询
	 * 
	 * @parameter id
	 */
	public OrderSendoutDo getById(Long id);

	/**
	 * 根据条件查询列表
	 */
	public List<OrderSendoutDo> selectOrderSendout(Map example);

	/**
	 * 更新
	 */
	public int updateOrderSendoutById(OrderSendoutDo newOrderSendoutDo);

	/**
	 * 新增
	 */
	public int addOrderSendout(OrderSendoutDo newOrderSendoutDo);

	 /**
	 * 分页查询
	 * @param param
	 * @param page
	 * @return
	 */
	public PageDo<OrderSendoutDo> getOrderSendoutPage(Map<String, Object> param, PageDo<OrderSendoutDo> page);

	/**
	 * 删除
	 */
	public int deleteById(Long id);
}
