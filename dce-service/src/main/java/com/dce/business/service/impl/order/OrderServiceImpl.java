package com.dce.business.service.impl.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dce.business.common.util.Constants;
import com.dce.business.dao.order.IOrderDao;
import com.dce.business.dao.order.OrderDetailMapper;
import com.dce.business.dao.trade.IKLineDao;
import com.dce.business.entity.order.Order;
import com.dce.business.entity.order.OrderDetail;
import com.dce.business.entity.order.OrderDo;
import com.dce.business.entity.page.PageDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.dict.ICtCurrencyService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.order.IOrderService;

@Service("orderService")
public class OrderServiceImpl implements IOrderService {

	private static Logger logger = Logger.getLogger(OrderServiceImpl.class);

	@Resource
	private IOrderDao orderDao;
	@Resource
	private OrderDetailMapper orderDetailDao;
	@Resource
	private IAccountService accountService;
	@Resource
	private IKLineDao kLineDao;
	@Resource
	private ILoanDictService loanDictService;
	@Resource
	private ICtCurrencyService ctCurrencyService;

	/**
	 * 获取用户所有的订单
	 */
	@Override
	public List<Order> selectByUesrId(Integer userId) {
		logger.info("获取当前登录用户的id：" + userId);
		return orderDao.selectByUesrId(userId);
	}

	/**
	 * 生成一个订单
	 */
	@Override
	public int insertOrder(Order order) {
		if (order == null) {
			return 0;
		}
		return orderDao.insertSelective(order);
	}

	/**
	 * 根据id查询订单
	 */
	@Override
	public Order selectByPrimaryKey(Integer orderId) {

		return orderDao.selectByPrimaryKey(orderId);
	}

	/**
	 * （支付）更新订单
	 */
	@Override
	public int updateOrder(Order order) {
		if (order == null) {
			return 0;
		}
		return orderDao.updateByPrimaryKey(order);
	}

	/**
	 * 用户选择商品，添加订单和订单明细，并且显示商品优惠
	 * 
	 * @param order
	 * @return
	 */
	@Override
	public Order buyOrder(Order order) {

		// 添加订单
		orderDao.insertSelective(order);

		// 添加订单明细
		for (OrderDetail orderDetail : order.getOrderDetailLst()) {
			orderDetail.setOrderid(order.getOrderid());
			orderDetailDao.insertSelective(orderDetail);
		}

		// 计算奖励优惠，这里不计算明细，后期加上
		order.setAwardDetailLst(order.getOrderDetailLst());
		if (order.getAwardDetailLst() != null) {

			for (OrderDetail orderDetail : order.getAwardDetailLst()) {
				orderDetail.setOrderid(order.getOrderid());
				orderDetailDao.insertSelective(orderDetail);
			}
		}
		return order;
	}

	@Override
	public Map<String, Object> getBaseInfo(String date) {
		return orderDao.getBaseInfo(date);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer addOrder(Order order) {
		orderDao.insertSelective(order);
		return order.getOrderid();
	}

	@Override
	public Order getOrder(Integer orderId) {
		return orderDao.selectByPrimaryKey(orderId);
	}

	@Override
	public List<OrderDo> selectOrder(Map<String, Object> params) {
		return orderDao.selectOrder(params);
	}

	@Override
	public List<Map<String, Object>> selectOrderForReport(Map<String, Object> paraMap) {
		return orderDao.selectOrderForReport(paraMap);
	}

	@Override
	public int selectOrderForReportCount(Map<String, Object> paraMap) {
		return orderDao.selectOrderForReportCount(paraMap);
	}

	@Override
	public PageDo<Map<String, Object>> selectOrderByPage(PageDo<Map<String, Object>> page, Map<String, Object> params) {
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		params.put(Constants.MYBATIS_PAGE, page);
		List<Map<String, Object>> list = orderDao.selectOrderByPage(params);
		page.setModelList(list);
		return page;
	}

	@Override
	public Order selectByOrderCode(String orderCode) {
		
		return orderDao.selectByOrderCode(orderCode);
	}

	@Override
	public int updateByOrderCodeSelective(Order order) {

		return orderDao.updateByOrderCodeSelective(order);
	}

	@Override
	public List<Order> selectByUesrIdOneToMany(Integer userId) {

		return orderDao.selectByUesrIdOneToMany(userId);
	}

}
