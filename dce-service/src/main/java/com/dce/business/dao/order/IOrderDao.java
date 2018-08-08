package com.dce.business.dao.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dce.business.entity.order.Order;
import com.dce.business.entity.order.OrderDo;

public interface IOrderDao {
    int deleteByPrimaryKey(Long orderId);

    int insert(OrderDo record);

    int insertSelective(OrderDo record);

    OrderDo selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(OrderDo record);

    int updateByPrimaryKey(OrderDo record);
    
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
}