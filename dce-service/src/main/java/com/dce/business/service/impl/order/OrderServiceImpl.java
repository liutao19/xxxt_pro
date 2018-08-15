package com.dce.business.service.impl.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dce.business.common.util.Constants;
import com.dce.business.dao.order.IOrderDao;
import com.dce.business.dao.trade.IKLineDao;
import com.dce.business.entity.order.Order;
import com.dce.business.entity.order.OrderDetail;
import com.dce.business.entity.page.PageDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.dict.ICtCurrencyService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.order.IOrderService;
import com.dce.business.dao.order.OrderDetailMapper;

@Service("orderService")
public class OrderServiceImpl implements IOrderService {

	@Resource
	private OrderDetailMapper orderDetailDao;
	@Resource
	private IOrderDao orderDao;
	@Resource
	private IAccountService accountService;
	@Resource
	private IKLineDao kLineDao;
	@Resource
	private ILoanDictService loanDictService;
	@Resource
	private ICtCurrencyService ctCurrencyService;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer addOrder(Order order) {
		orderDao.insertSelective(order);
		return order.getOrderid();
	}

	public Map<String, Object> getBaseInfo(String date) {

		Map<String, Object> result = orderDao.getBaseInfo(date);
		if (result == null) {
			result = new HashMap<String, Object>();
		}
		return result;
	}

	/**
	 * 后台分页查询
	 */
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

	/**
	 * 获取用户所有的订单
	 */
	@Override
	public List<Order> selectByUesrId(Integer userId) {

		return orderDao.selectByUesrId(userId);
	}

	/**
	 * 生成一个订单
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int insertOrder(Order order) {
		// 订单明细表插入数据
		return orderDao.insertSelective(order);
	}

	/**
	 * 根据id查询订单
	 */
	@Override
	public Order selectByPrimaryKey(long orderId) {

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

	@Override
	public Order selectByPrimaryKey(Integer orderId) {

		return orderDao.selectByPrimaryKey(orderId);
	}

	@Override
	public Order selectByOrderCode(String orderCode) {

		return orderDao.selectByOrderCode(orderCode);
	}

	@Override
	public List<Order> selectByUesrIdOneToMany(Integer userId) {

		return orderDao.selectByUesrIdOneToMany(userId);
	}

	@Override
	public int updateByOrderCodeSelective(Order order) {

		return orderDao.updateByOrderCodeSelective(order);
	}

	@Override
	public List<Order> selectOrder(Map<String, Object> params) {

		return orderDao.selectOrder(params);
	}

	/**
	 * 
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Order buyOrder(Order order) {

		// 插入订单明细
		orderDao.insertSelective(order);
		for (OrderDetail orderDetail : order.getOrderDetailLst()) {
			orderDetail.setOrderid(order.getOrderid());
			orderDetailDao.insertSelective(orderDetail);
		}

		// test 这里不计算明细，后期加上
		order.setAwardDetailLst(order.getOrderDetailLst());
		if (order.getAwardDetailLst() != null) {

			for (OrderDetail orderDetail : order.getAwardDetailLst()) {
				orderDetail.setOrderid(order.getOrderid());
				orderDetailDao.insertSelective(orderDetail);
			}
		}
		return order;
	}

	// 查询总业绩
	@Override
	public Map<String, Object> selectSum(Map<String, Object> paraMap) {

		return orderDao.selectSum(paraMap);
	}
}
