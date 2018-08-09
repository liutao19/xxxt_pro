package com.dce.business.service.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.dce.business.common.result.Result;
import com.dce.business.entity.order.Order;
import com.dce.business.entity.order.OrderDo;
import com.dce.business.entity.page.PageDo;

public interface IOrderService {
	
    //获取当前用户所有的订单
    List<Order> selectByUesrId(Integer userId);
    
    //生成一个订单
    int insertOrder(Order order);
    
    /** 
     * 添加订单
     * @param orderDo  
     */
    Long addOrder(OrderDo orderDo);

    OrderDo getOrderDo(Long orderId);

    /**
     * 购买挂单
     * @param userId  买订单的用户
     * @param orderId	要买的订单id
     * @param qty	要买的金额
     * @return
     */
    Result<?> buyOrder(Integer userId, Long orderId,BigDecimal qty);
    /**
     * 卖给挂单
     * @param userId  卖给挂单的用户
     * @param orderId	要卖给的订单id
     * @param qty	要卖的金额
     * @return
     */
    Result<?> salOrder(Integer userId, Long orderId,BigDecimal qty);

    /**
     * 买卖挂单
     * @param userId
     * @param orderId
     * @param qty
     * @return
     */
    Result<?> matchOrder(Integer userId, Long orderId,BigDecimal qty);
    
    List<OrderDo> selectOrder(Map<String, Object> params);
    
    int updateOrder(OrderDo orderDo);
    
    /**
     * 挂单
     * @param orderDo
     * @return
     */
    Result<?> guadan(OrderDo orderDo);
    
    Result<?> cancel(Long orderId,Integer userId);
    
    public Map<String,Object> getBaseInfo(String date);
    
    /**
     * 订单交易流水
     * @param paraMap
     * @return
     */
    List<Map<String,Object>> selectOrderForReport(Map<String, Object> paraMap);
    
    int selectOrderForReportCount(Map<String, Object> paraMap);
    
    public PageDo<Map<String,Object>> selectOrderByPage(PageDo<Map<String,Object>> page,Map<String, Object> params);
    
    /**
     * 统计挂单量
     * @param paraMap
     * @return
     */
    Long selectGuadanAmount(Map<String, Object> paraMap);
}
