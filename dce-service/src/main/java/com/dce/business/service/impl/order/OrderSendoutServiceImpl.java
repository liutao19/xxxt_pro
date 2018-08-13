package com.dce.business.service.impl.order;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dce.business.common.util.Constants;
import com.dce.business.dao.order.IOrderDao;
import com.dce.business.entity.order.OrderSendoutDo;
import com.dce.business.entity.page.PageDo;
import com.dce.business.service.order.IOrderSendoutService;

import org.springframework.transaction.annotation.Propagation;

@Service("orderSendoutService")
public class OrderSendoutServiceImpl implements IOrderSendoutService {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private IOrderSendoutService oreersendoutserice;
	@Resource
	private OrderSendoutDo redersendout;
	@Resource
	private IOrderDao orderdao;

	/**
	 * 根据ID 查询
	 * 
	 * @parameter id
	 */
	@Override
	public OrderSendoutDo getById(Long id) {
		return oreersendoutserice.getById(id);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<OrderSendoutDo> selectOrderSendout(Map example) {
		return oreersendoutserice.selectOrderSendout(example);
	}

	/**
	 * 更新
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int updateOrderSendoutById(OrderSendoutDo newOrderSendoutDo) {
		logger.debug("updateOrderSendout(OrderSendoutDo: " + newOrderSendoutDo);
		return oreersendoutserice.updateOrderSendoutById(newOrderSendoutDo);
	}

	/**
	 * 新增
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int addOrderSendout(OrderSendoutDo newOrderSendoutDo) {
		logger.debug("addOrderSendout: " + newOrderSendoutDo);
		return oreersendoutserice.addOrderSendout(newOrderSendoutDo);
	}

	// /**
	// * 分页查询
	// *
	// * @param param
	// * @param page
	// * @return
	// */
	// public PageDo<OrderSendoutDo> getOrderSendoutPage(Map<String, Object>
	// param, PageDo<OrderSendoutDo> page) {
	// logger.info("----getOrderSendoutPage----" + param);
	// param.put(Constants.MYBATIS_PAGE, page);
	// List<OrderSendoutDo> list = oreersendoutserice.queryListPage(param);
	// page.setModelList(list);
	// return page;
	// }

	/**
	 * 删除
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int deleteById(Long id) {
		logger.debug("deleteByIdOrderSendout:" + id);
		return oreersendoutserice.deleteById(id);
	}

}
