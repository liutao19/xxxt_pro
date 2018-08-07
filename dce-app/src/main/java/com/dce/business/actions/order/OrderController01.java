package com.dce.business.actions.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.actions.common.BaseController;
import com.dce.business.common.result.Result;
import com.dce.business.entity.order.Order;
import com.dce.business.service.order.OrderService;

@RestController
@RequestMapping("orderTest")
public class OrderController01 extends BaseController {
	
	@Resource 
	private OrderService orderService01;

    @RequestMapping(value="/orderInquiry", method = RequestMethod.POST)
    public Result<?> orderList(){
    	Integer userId = getUserId();
    	System.out.println("查询订单");
    	
    	List<Order> orderList = orderService01.selectByUserId(userId);
    	List<Map<String,Object>> list = new ArrayList<>();
    	for(Order order : orderList){
    		Map<String,Object> map = new HashMap<>();
    		map.put("orderId ", order.getOrderid());
    		map.put("totalPrice ", order.getTotalprice());
    		map.put("orderType", order.getOrdertype());
    		map.put("qty ", order.getQty());
    		map.put("payTime ", order.getPaytime());
    		map.put("payStatus", order.getPaystatus());
    		map.put("orderStatus", order.getOrderstatus());
    		list.add(map);
    	}
    	System.err.println("------------>>>>"+list);
    	return Result.successResult("成功", list);
    }
	
}
