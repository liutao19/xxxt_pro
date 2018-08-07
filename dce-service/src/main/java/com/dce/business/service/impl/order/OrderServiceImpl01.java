package com.dce.business.service.impl.order;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dce.business.dao.order.OrderMapper;
import com.dce.business.entity.order.Order;
import com.dce.business.service.order.OrderService;

@Service("orderService01")
public class OrderServiceImpl01 implements OrderService {

	//private static Logger logger = Logger.getLogger(OrderServiceImpl01.class);
	
	@Resource
	private OrderMapper orderdao;

	@Override
	public List<Order> selectByUserId(Integer userId) {

		return orderdao.selectByUserId(userId);
	}
	
	
}
