/*
 * Powered By  huangzl QQ: 272950754
 * Web Site: http://www.hehenian.com
 * Since 2008 - 2018
 */

package com.dce.business.dao.order;

/**
 * @author  huangzl QQ: 272950754
 * @version 1.0
 * @since 1.0
 */


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dce.business.entity.order.OrderSendoutDo;
public interface IOrderSendoutDao {

	/**
	 * 根据ID 查询
	 * @parameter id
	 */
	public OrderSendoutDo getById(int id);
	
	/**
	 *根据条件查询列表
	 */
	public List<OrderSendoutDo> selectOrderSendout(Map<String,Object> parameterMap);
	
	/**
	 * 更新
	 */
	public int  updateOrderSendoutById(OrderSendoutDo newOrderSendoutDo);
	
	/**
	 * 新增
	 */
	public int addOrderSendout(OrderSendoutDo newOrderSendoutDo);
	
	/**
	 * 删除
	 */
	public int deleteById(int id);

}
