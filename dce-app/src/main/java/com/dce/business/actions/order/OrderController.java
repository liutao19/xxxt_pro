package com.dce.business.actions.order;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
import org.jdom2.JDOMException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.dce.business.actions.common.BaseController;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.DateUtil;
import com.dce.business.common.wxPay.util.XMLUtil;
import com.dce.business.dao.account.IUserAccountDetailDao;
import com.dce.business.entity.alipaymentOrder.AlipaymentOrder;
import com.dce.business.entity.goods.CTGoodsDo;
import com.dce.business.entity.order.Order;
import com.dce.business.entity.order.OrderDetail;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.accountRecord.AccountRecordService;
import com.dce.business.service.bonus.IBonusLogService;
import com.dce.business.service.goods.ICTGoodsService;
import com.dce.business.service.impl.order.AlipaymentOrderService;
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
	@Resource
	private AlipaymentOrderService alipaymentOrderService;

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
	 * 用户选择商品，下单生成预支付订单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/createOrder", method = RequestMethod.POST)
	public Result<?> insertOrder(HttpServletRequest request, HttpServletResponse response) {

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
		return orderService.saveOrder(premiumList, chooseGoodsLst, order, request, response);

	}

	/**
	 * 支付宝支付成功后异步请求该接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
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
	 * 接收微信支付成功通知
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/app/tenpay/notify")
	public void getnotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("微信支付回调");
		PrintWriter writer = response.getWriter();
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		String result = new String(outSteam.toByteArray(), "utf-8");
		logger.info("微信支付通知结果：" + result);
		Map<String, String> map = null;
		try {
			/**
			 * 解析微信通知返回的信息
			 */
			map = XMLUtil.doXMLParse(result);
			
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		System.out.println("=========:" + result);
		// 若支付成功，则告知微信服务器收到通知
		if (map.get("return_code").equals("SUCCESS")) {
			if (map.get("result_code").equals("SUCCESS")) {
				AlipaymentOrder alipaymentOrder = alipaymentOrderService.selectByOrderCode(map.get("out_trade_no"));
				logger.info("订单号：" + Long.valueOf(map.get("out_trade_no")));
				logger.info("交易创建时间-----》》》alipaymentOrder.getGmtcreatetime："+alipaymentOrder.getGmtcreatetime());
				
				// 判断通知是否已处理，若已处理，则不予处理
				if (alipaymentOrder.getGmtcreatetime() == null) {
					logger.info("通知微信后台");
					alipaymentOrder.setOrderstatus(2);
					alipaymentOrder.setNotifytime(DateUtil.dateToString(new Date()));
					//更新交易记录状态
					alipaymentOrderService.updateByPrimaryKeySelective(alipaymentOrder);
					
					//支付成功处理
					orderService.orderPay(alipaymentOrder.getOrdercode(),alipaymentOrder.getCreatetime());
					
					String notifyStr = XMLUtil.setXML("SUCCESS", "");
					writer.write(notifyStr);
					writer.flush();
					writer.close();
				}
			}
		}
	}

	/**
	 * 支付宝订单查询接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/alipayQuery", method = RequestMethod.POST)
	public Result<?> queryOrderByOutTradeNo() {

		String outTradeNo = getString("outTradeNo") == null ? "" : getString("outTradeNo");
		logger.info("========获取的订单号======：" + outTradeNo);

		return orderService.alipayQuery(outTradeNo);
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