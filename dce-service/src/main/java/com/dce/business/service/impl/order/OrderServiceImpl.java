package com.dce.business.service.impl.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.CurrencyType;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.common.enums.OrderStatus;
import com.dce.business.common.enums.OrderType;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.Constants;
import com.dce.business.common.util.OrderCodeUtil;
import com.dce.business.dao.order.IOrderDao;
import com.dce.business.dao.trade.IKLineDao;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.dict.CtCurrencyDo;
import com.dce.business.entity.order.Order;
import com.dce.business.entity.order.OrderDo;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.trade.KLineDo;
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
	private IAccountService accountService;
	@Resource
	private IKLineDao kLineDao;
	@Resource
	private ILoanDictService loanDictService;
	@Resource
	private ICtCurrencyService ctCurrencyService;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Long addOrder(OrderDo orderDo) {
		//orderDao.insertSelective(orderDo);
		return orderDo.getOrderId();
	}


	/**
	 * 买卖挂单前判断
	 * 
	 * @param userId
	 * @param orderId
	 * @param qty
	 * @param orderType
	 *            对应操作的目标订单类型 比如当前是要买入挂单中的额度 那么目标订单的类型需要为卖出(2)类型 如果是卖额度给挂单中的订单
	 *            那么目标订单需要为买入(1)类型
	 * @return
	 */
	private Result<?> beforMatchOrder(Integer userId, OrderDo matchOrder, BigDecimal qty, int orderType) {
		if (matchOrder == null) {
			return Result.failureResult("未查询到对应挂单!");
		}
		// 判断对应挂单是否有效
		if (matchOrder.getOrderStatus() == 0 || matchOrder.getPayStatus() == 1
				|| matchOrder.getOrderType().intValue() != orderType) {
			return Result.failureResult("无效的挂单!");
		}
		// 判断不能匹配买入自己的订单
		if (userId.intValue() == matchOrder.getUserId().intValue()) {
			return Result.failureResult("不能买卖自己挂的单");
		}

		// 判断挂出的订单中是否还有足够的币(>=qty)卖给userId
		BigDecimal salqty = matchOrder.getSalqty();
		if (salqty == null) {
			salqty = BigDecimal.ZERO;
		}
		BigDecimal gd_qty = matchOrder.getQty().subtract(salqty);
		if (gd_qty.compareTo(qty) < 0) {
			return Result.failureResult("当前挂单中没有足够余额可售出!");
		}

		// 判断用户美元点账户是否有足够的美元点来买入qty数量的DEC币
		// 计算公式 所需美元点 = qty * 挂单价格
		// LoanDictDtlDo MYDBXCC =
		// loanDictService.getLoanDictDtl(DictCode.MYDBXCC.getCode());
		// if(MYDBXCC == null){
		// return Result.failureResult("未查询到美元点与现持仓转换比例,请联系管理员!");
		// }
		UserAccountDo pointAcc = null;
		/**
		 * 如果订单是卖出 那么是当前用户在买入 所以需要查询当前用户(userId)的美元点账户是否有足够额度 如果订单是买入
		 * 那么当前用户是在卖出DEC币 所以需要查询挂单用户的美元点账户是否有足够的额度来买当前用户的DEC币
		 */
		if (orderType == OrderType.GD_SAL.getOrderType()) {
			pointAcc = accountService.selectUserAccount(userId, matchOrder.getAccountType());
		} else {
			pointAcc = accountService.selectUserAccount(matchOrder.getUserId(), matchOrder.getAccountType());
		}
		BigDecimal needpoint = qty.multiply(matchOrder.getPrice()).setScale(6, RoundingMode.HALF_UP);
		// needpoint = needpoint.divide(new
		// BigDecimal(rmb2point.getRemark()),6,RoundingMode.HALF_UP);
		if (pointAcc == null || pointAcc.getAmount().compareTo(needpoint) < 0) {
			return Result.failureResult("当前现金币账户余额不足");
		}

		return Result.successResult("条件判断成功,符合买卖条件", needpoint);

	}

	private void afterMatchOrder(OrderDo matchOrder) {
		// 记录K线数据
		KLineDo kLineDo = new KLineDo();
		kLineDo.setPrice(matchOrder.getPrice());
		kLineDo.setQty(matchOrder.getQty());
		kLineDo.setTotalAmount(matchOrder.getTotalPrice());
		kLineDo.setCtime(new Date());
		kLineDao.insertSelective(kLineDo);
	}


	public Map<String, Object> getBaseInfo(String date) {

		Map<String, Object> result = orderDao.getBaseInfo(date);
		if (result == null) {
			result = new HashMap<String, Object>();
		}
		return result;
	}


	@Override
	public PageDo<Map<String, Object>> selectOrderByPage(PageDo<Map<String, Object>> page, Map<String, Object> params) {
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		params.put(Constants.MYBATIS_PAGE, page);
		List<Map<String, Object>> list = orderDao.selectOrderByPage(params);
		page.setModelList(list);
		;
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
		//订单明细表插入数据
		
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
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Order selectByOrderCode(String orderCode) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Order> selectByUesrIdOneToMany(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int updateByOrderCodeSelective(Order order) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Integer addOrder(Order order) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Order getOrder(Integer orderId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<OrderDo> selectOrder(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Order buyOrder(Order order) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map<String, Object> selectSum(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
