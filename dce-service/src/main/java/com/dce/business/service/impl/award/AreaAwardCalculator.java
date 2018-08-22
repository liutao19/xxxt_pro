package com.dce.business.service.impl.award;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.dce.business.common.exception.BusinessException;
import com.dce.business.entity.order.Order;
import com.dce.business.entity.user.UserAddressDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.order.IOrderService;
import com.dce.business.service.user.IUserService;
import com.dce.business.service.user.UserAdressService;

/**
 * 区域奖金计算类
 * @author harry
 *
 */
@Service("areaAwardCalculator")
public class AreaAwardCalculator implements IAwardCalculator {
	
	private final Logger logger = Logger.getLogger(this.getClass());

	
	@Resource
	private IOrderService orderService;
	
	@Resource
	private UserAdressService userAdressService;
	
	@Resource
	private IUserService userService;

	/**
	 * 计算奖励的方法
	 * @param buyUserId 购买者
	 * @param buyQty    购买数量
	 * @param orderId   购买订单
	 * @return
	 */
	@Override
	public void doAward(int buyUserId, int buyQty, Long orderId) {
		Assert.notNull(buyUserId, "购买者用户ID不能为空");
		Assert.notNull(buyQty, "购买数量不能为空");
		Assert.notNull(orderId, "购买订单ID不能为空");
		
		//获取订单信息
		Order order=orderService.selectByPrimaryKey(orderId);
		
		//获取地址信息
		UserAddressDo useraddress=userAdressService.selectByPrimaryKey(order.getAddressid());
		
		//获取区域代表信息
		Map<String,Object> map=new HashMap<>();
		map.put("district", useraddress.getAddress());
		List<UserDo> user=userService.selectUserCondition(map);
		
		if(user==null){
			logger.error("此区域无代理,结束奖励");
			return;
		}
	}
	
	/**
	 * 奖励区域代表奖励
	 */
	public void  area(Integer userId){
		
		
	}

}
