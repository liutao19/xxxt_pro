package com.dce.business.actions.order;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.dce.business.actions.common.BaseController;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.pay.util.AlipayConfig;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.DateUtil;
import com.dce.business.dao.account.IUserAccountDetailDao;
import com.dce.business.entity.alipaymentOrder.AlipaymentOrder;
import com.dce.business.entity.goods.CTGoodsDo;
import com.dce.business.entity.order.Order;
import com.dce.business.entity.order.OrderDetail;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.accountRecord.AccountRecordService;
import com.dce.business.service.bonus.IBonusLogService;
import com.dce.business.service.goods.ICTGoodsService;
import com.dce.business.service.order.IOrderService;
import com.dce.business.service.user.UserAdressService;

@RestController
@RequestMapping("order")
public class OrderController extends BaseController {
	private final static Logger logger = Logger.getLogger(OrderController.class);

	@Resource
	private IOrderService orderService;
	@Resource
	private AccountRecordService accountRecordService;
	@Resource
	private UserAdressService addressService;
	@Resource
	private IAccountService accountService;
	@Resource
	private IBonusLogService bonusServiceLog;
	@Resource
	private IUserAccountDetailDao userAccountDetailDao;
	@Resource
	private ICTGoodsService ctGoodsService;

	/**
	 * 用户订单列表显示
	 * 
	 * @return
	 */
	@RequestMapping(value = "/orderInquiry", method = RequestMethod.POST)
	public Result<?> getOrder() {
		Integer userId = getUserId();

		List<Order> orderLitst = orderService.selectByUesrIdOneToMany(userId);
		logger.info("获取当前用户的所有订单:" + orderLitst);

		// 设置商品名称
		for (Order order : orderLitst) {
			if (order.getOrderDetailLst() != null) {
				for (OrderDetail orderDetail : order.getOrderDetailLst()) {
					long id = Long.valueOf(orderDetail.getGoodsId());
					logger.info("获取订单里面的商品id：" + orderDetail.getGoodsId());

					CTGoodsDo goods = ctGoodsService.selectById(id);
					logger.info("商品名称：" + goods.getTitle());

					orderDetail.setGoodsName(goods.getTitle());
				}
			}
		}

		return Result.successResult("获取订单成功", orderLitst);
	}

	/**
	 * 用户选择商品，添加订单和订单明细，计算奖励，并返回商品清单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/createOrder", method = RequestMethod.POST)
	public Result<?> insertOrder(HttpServletRequest request) {

		Integer userId = getUserId();

		// 获取商品信息
		String goods = request.getParameter("cart");

		// 获取赠品信息
		String premium = request.getParameter("premium");

		// 获取地址id
		String addressId = getString("addressId");

		// 获取支付方式
		String orderType = getString("orderType");
		Order order = new Order();
		order.setUserid(userId);
		order.setAddress(addressId);
		order.setOrdertype(Integer.valueOf(orderType));

		logger.info("======用户选择的商品信息：" + goods + "=====获取的赠品信息：" + premium + "=====获取的地址id：" + addressId
				+ "=====获取的支付方式：" + orderType);

		// 将商品信息的JSON数据解析为list集合
		List<OrderDetail> chooseGoodsLst = convertGoodsFromJson(goods);

		// 将赠品信息的JSON数据解析为list集合
		List<OrderDetail> premiumList = convertGoodsFromJson(premium);

		// 生成预付单，保存订单和订单明显
		return orderService.saveOrder(premiumList, chooseGoodsLst, order);

	}

	

	/**
	 * 支付宝支付成功后异步请求该接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/notify_url", method = RequestMethod.POST)
	@ResponseBody
	public String notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("==================支付宝异步返回支付结果开始");
		// 1.从支付宝回调的request域中取值
		// 获取支付宝返回的参数集合
		Map<String, String[]> aliParams = request.getParameterMap();
		// 用以存放转化后的参数集合
		Map<String, String> conversionParams = new HashMap<String, String>();
		for (Iterator<String> iter = aliParams.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			String[] values = aliParams.get(key);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "uft-8");
			conversionParams.put(key, valueStr);
		}
		logger.info("==================返回参数集合：" + conversionParams);
		String status = orderService.notify(conversionParams);
		return status;
	}

	/**
	 * 支付成功
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public Result<?> orderPay() {

		// 获取前台传过来的订单信息
		String orderCode = getString("orderCode") == "" ? null : getString("orderCode");
		String addressId = getString("addressId") == "" ? null : getString("addressId");
		String orderPayType = getString("orderPayType") == "" ? null : getString("orderPayType");

		// 根据订单编号查询出订单
		Order order = orderService.selectByOrderCode(orderCode);
		logger.info("根据订单编号查询出的订单：" + orderCode);
		order.setOrdertype(Integer.valueOf(orderPayType)); // 支付方式
		order.setPaystatus(1); // 付款状态为已支付
		order.setAlipayStatus(1); // 支付成功
		order.setAddressid(Integer.valueOf(addressId));
		Date date = new Date();
		order.setPaytime(DateUtil.dateformat(date)); // 支付时间

		// 更新订单状态
		orderService.updateByOrderCodeSelective(order);

		// 记录到交易流水表中
		// accountService.addUserAccountDetail(order.getUserid(),
		// order.getTotalprice(), "减少", 902);

		return Result.successResult("测试", null);
	}

	/**
	 * json 转OrderDetail对象 json 格式 [ { "goodsId": "0001", "qty": "20",
	 * "price":"597" }, { "goodsId": "0002", "qty": "10", "price": "637" } ]
	 * 
	 * @param goods
	 * @return
	 */
	private List<OrderDetail> convertGoodsFromJson(String goods) {
		if (StringUtils.isBlank(goods)) {
			throw new BusinessException("请选择商品");
		}
		return (List<OrderDetail>) JSONArray.parse(goods);
	}

}