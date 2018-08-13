package com.dce.business.service.order;

import java.util.List;
import java.util.Map;

import com.dce.business.entity.order.Order;
import com.dce.business.entity.order.OrderDo;
import com.dce.business.entity.page.PageDo;

public interface IOrderService {

	// 获取当前用户所有的订单
	List<Order> selectByUesrId(Integer userId);

	// 生成一个订单
	int insertOrder(Order order);

	// 根据主键id查询订单
	Order selectByPrimaryKey(Integer orderId);

	// 根据订单编号查询订单
	Order selectByOrderCode(String orderCode);

	// 一对多联表查询订单
	List<Order> selectByUesrIdOneToMany(Integer userId);

	// 根据订单编号更新订单
	int updateByOrderCodeSelective(Order order);

	Integer addOrder(Order order);

	Order getOrder(Integer orderId);

	List<OrderDo> selectOrder(Map<String, Object> params);

	int updateOrder(Order order);

	public Map<String, Object> getBaseInfo(String date);

	public PageDo<Map<String, Object>> selectOrderByPage(PageDo<Map<String, Object>> page, Map<String, Object> params);

	/**
	 * 购买的时候保存订单，并返回优惠明细
	 * 
	 * @param order
	 * @return
	 */
	Order buyOrder(Order order);
	
    
    //根据主键id查询订单
    Order selectByPrimaryKey(long orderId);
    
    /** 
     * 添加订单
     * @param orderDo  
     */
    Long addOrder(OrderDo orderDo);

    // 查询总业绩
 	Map<String, Object> selectSum(Map<String, Object> paraMap);

}
