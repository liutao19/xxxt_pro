package com.dce.business.dao.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dce.business.entity.order.Order;
import com.dce.business.entity.order.OrderDo;

public interface IOrderDao {
    int deleteByPrimaryKey(Long orderId);

    int insertSelective(OrderDo record);

    Order selectByPrimaryKey(Integer orderId);

    int updateByPrimaryKeySelective(OrderDo record);

   // int updateByPrimaryKey(OrderDo record);
    
    List<OrderDo> selectOrder(Map<String, Object> paraMap);
    
    void updateOrderStatusByOldStatus(Map<String, Object> paraMap);
    
    int updateMatchOrder(OrderDo order);
    
    Map<String,Object> getBaseInfo(@Param("queryTime")String queryTime);
    
    List<Map<String,Object>> selectOrderForReport(Map<String, Object> paraMap);
    
    int selectOrderForReportCount(Map<String, Object> paraMap);

    List<Map<String,Object>> selectOrderByPage(Map<String, Object> paraMap);
    
    Long selectGuadanAmount(Map<String, Object> paraMap);
    
    //获取当前用户所有的订单
    List<Order> selectByUesrId(Integer userId);
    
    //生成一个订单
    int insertSelective(Order order);
    
    //根据主键id查询订单
    Order selectByPrimaryKey(long orderId); 
    
    //用户支付，更新订单状态
    int updateByPrimaryKey(Order order);
    
    //根据订单编号查询订单
    Order selectByOrderCode(String orderCode);
    
    //根据订单编号更新订单
    int updateByOrderCodeSelective(Order order);
    
    //一对多联表查询订单
    List<Order> selectByUesrIdOneToMany(Integer userId);
    

}