package com.dce.business.service.impl.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.dce.business.common.alipay.util.AlipayConfig;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.Constants;
import com.dce.business.common.util.DateUtil;
import com.dce.business.common.util.OrderCodeUtil;
import com.dce.business.common.wxPay.handler.PrepayIdRequestHandler;
import com.dce.business.common.wxPay.util.MD5Util;
import com.dce.business.common.wxPay.util.WXPayConfig;
import com.dce.business.common.wxPay.util.WXUtil;
import com.dce.business.dao.order.IOrderDao;
import com.dce.business.dao.order.OrderDetailMapper;
import com.dce.business.dao.trade.IKLineDao;
import com.dce.business.entity.alipaymentOrder.AlipaymentOrder;
import com.dce.business.entity.goods.CTGoodsDo;
import com.dce.business.entity.order.Order;
import com.dce.business.entity.order.OrderDetail;
import com.dce.business.entity.order.OrderSendOut;
import com.dce.business.entity.page.PageDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.dict.ICtCurrencyService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.goods.ICTGoodsService;
import com.dce.business.service.order.IOrderSendoutService;
import com.dce.business.service.order.IOrderService;

@Service("orderService")
public class OrderServiceImpl implements IOrderService {
	private final static Logger logger = Logger.getLogger(OrderServiceImpl.class);
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
	@Resource
	private IOrderSendoutService orderSendOutService;
	@Resource
	private ICTGoodsService ctGoodsService;
	@Resource
	private AlipaymentOrderService alipaymentOrderService;

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

	// 查询总业绩
	@Override
	public Map<String, Object> selectSum(Map<String, Object> paraMap) {

		return orderDao.selectSum(paraMap);
	}

	/**
	 * 计算赠品的差价
	 * 
	 * @return
	 */
	private Double countPremiumPriceSpread(List<OrderDetail> premiumList) {

		if (premiumList.isEmpty()) {
			return 0.0;
		}

		double price = 40.0; // 每盒差价
		double totalprice = 0; // 总差价

		// 当赠品为男版或者女版时
		if (premiumList.size() <= 1) {
			if (premiumList.get(0).getOrderid() == 1001) { // 男版
				totalprice = 0;
			} else { // 女版差价
				totalprice = premiumList.get(0).getQty() * price;
			}

			// 当赠品为混合版时
		} else {
			for (int j = 0; j < premiumList.size(); j++) {
				// 计算出需补的差价
				if (premiumList.get(j + 1).getPrice() > premiumList.get(j).getPrice()) {
					totalprice = premiumList.get(j).getQty() * price;
				}
			}
		}
		return totalprice;
	}

	/**
	 * 保存订单和订单明细，获取签名后的订单信息，并返回加签后的订单信息
	 * 
	 * @param chooseGoodsLst
	 */
	public Result<String> saveOrder(List<OrderDetail> premiumList, List<OrderDetail> chooseGoodsLst, Order order, HttpServletRequest request, HttpServletResponse response) {

		// 选择的商品信息为空
		if (chooseGoodsLst.size() == 0) {
			logger.debug("获取的商品清单为空=====》》》");
			return Result.successResult("商品清单为空");
		}

		if (order == null) {
			logger.debug("获取的商品信息为空（地址id、支付方式、用户id）=====》》》");
			return Result.successResult("获取的商品信息为空（地址id、支付方式、用户id）");
		}

		// 计算是否需要补赠品的差价
		Double priceSpread = countPremiumPriceSpread(premiumList);

		// 产生订单编号
		String orderCode = OrderCodeUtil.genOrderCode(order.getUserid());

		Integer quantity = 0; // 商品总数量
		BigDecimal totalprice = new BigDecimal(0); // 商品总价格
		for (OrderDetail orderDetail : chooseGoodsLst) { // 循环遍历出商品信息，计算商品总价格和商品总数量
			CTGoodsDo goods = ctGoodsService.selectById(Long.valueOf(orderDetail.getGoodsId()));
			orderDetail.setGoodsName(goods.getTitle()); // 获取商品名称
			quantity += orderDetail.getQty(); // 商品总数量
			totalprice = totalprice.multiply(BigDecimal.valueOf(orderDetail.getPrice())); // 商品总价格
		}

		if (priceSpread != 0) { // 总金额加上赠品需要补的差价
			totalprice = totalprice.multiply(BigDecimal.valueOf(priceSpread));
		}

		// 创建订单
		order.setOrdercode(orderCode); // 订单号
		Date date = new Date();
		order.setCreatetime(DateUtil.dateformat(date));// 订单创建时间
		order.setOrderstatus(0); // 未发货状态
		order.setPaystatus(0); // 未支付状态
		order.setQty(quantity); // 商品总数量
		order.setTotalprice(totalprice); // 商品总价格
		order.setOrderDetailList(chooseGoodsLst); // 订单明细

		// 判断支付方式，生成预支付订单
		if (order.getOrdertype() == 1) { // 微信支付
			try {
				return getWXPayStr(request,response,buyOrder(order));
			} catch (Exception e) {
				logger.info("获取微信预支付订单出错");
				e.printStackTrace();
			}

		} else if (order.getOrdertype() == 2) { // 支付宝支付
			return getAlipayorderStr(buyOrder(order));

		}
			logger.debug("===========获取支付方式失败，生成预支付订单失败！！！！");
			return Result.failureResult("获取支付方式失败，生成预支付订单失败");
	}

	/**
	 * 获取签名后的订单信息，并返回加签后的订单信息
	 * 
	 * @param order
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Result<String> getAlipayorderStr(Order order) {

		String orderString = "";
		logger.info("============支付宝下单获取，获取的商户订单号为：===========" + order.getOrdercode());

		if (Double.valueOf(order.getTotalprice().toString()) <= 0) // 一些必要的验证，防止抓包恶意修改支付金额
		{
			logger.debug("支付金额错误！！！");
			return Result.failureResult("支付金额错误");
		}

		// 创建支付宝订单记录
		AlipaymentOrder alipaymentOrder = new AlipaymentOrder();
		alipaymentOrder.setOrderid(order.getOrderid());
		alipaymentOrder.setOrdercode(order.getOrdercode());
		alipaymentOrder.setTotalamount(order.getTotalprice());
		alipaymentOrder.setReceptamount(order.getTotalprice());
		alipaymentOrder.setOrderstatus(0);
		alipaymentOrder.setRemark("支付宝支付");

		// 实例化客户端（参数：网关地址、商户APPID、商户私钥、编码、支付宝公钥、加密类型）获取预付订单信息
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
					AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
					AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);

			// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
			AlipayTradeAppPayRequest ali_request = new AlipayTradeAppPayRequest();

			// SDK已经封装掉了公共参数，这里只需要传入业务参数
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			// 订单明细
			model.setBody(order.getOrderDetailList().toString());

			/*
			 * 设置未付款支付宝交易的超时时间，一旦超时，该笔交易就会自动被关闭。当用户进入支付宝收银台页面（不包括登录页面），
			 * 会触发即刻创建支付宝交易，此时开始计时。
			 * 取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）
			 * model.setTimeoutExpress(AlipayConfig.timeoutExpress);
			 */

			// 【必填】商户订单号
			model.setOutTradeNo(order.getOrdercode());
			// 【必填】支付金额
			//model.setTotalAmount(order.getTotalprice().toString());
			model.setTotalAmount("0.01");
			// 【必填】销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
			model.setProductCode("QUICK_MSECURITY_PAY");
			// 【必填】商品的标题/交易标题/订单标题/订单关键字等
			model.setSubject("湘信商城消费");

			ali_request.setBizModel(model);
			// 设置后台异步通知的地址，在手机端支付成功后支付宝会通知后台，手机端的真实支付结果依赖于此地址
			ali_request.setNotifyUrl(AlipayConfig.notify_url);

			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse ali_response = alipayClient.sdkExecute(ali_request); // 返回支付宝订单信息(预处理)
			// 可以直接给客户端请求，无需再做处理
			orderString = ali_response.getBody();
			alipaymentOrderService.createAlipayMentOrder(alipaymentOrder);// 创建新的商户支付宝订单

		} catch (AlipayApiException e) {
			logger.debug("生成支付宝预付单失败！！！！");
			e.printStackTrace();
		}
		return Result.successResult("返回支付宝预付订单", orderString);
	}

	/**
	 * 支付宝异步通知处理，验签
	 */
	@Override
	public String notify(Map<String, String> conversionParams) {
		logger.info("==================支付宝异步请求逻辑处理=============");

		// 签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
		boolean signVerified = false;

		try {
			// 调用SDK验证签名
			signVerified = AlipaySignature.rsaCheckV1(conversionParams, AlipayConfig.ALIPAY_PUBLIC_KEY,
					AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);

		} catch (AlipayApiException e) {
			logger.info("==================验签失败 ！");
			e.printStackTrace();
		}

		// 对验签进行处理
		if (signVerified) {
			// 验签通过
			// 获取需要保存的数据
			String appId = conversionParams.get("app_id");// 支付宝分配给开发者的应用Id
			String notifyTime = conversionParams.get("notify_time");// 通知时间:yyyy-MM-dd
																	// HH:mm:ss
			String gmtCreate = conversionParams.get("gmt_create");// 交易创建时间:yyyy-MM-dd
																	// HH:mm:ss
			String gmtPayment = conversionParams.get("gmt_payment");// 交易付款时间
			String gmtRefund = conversionParams.get("gmt_refund");// 交易退款时间
			String gmtClose = conversionParams.get("gmt_close");// 交易结束时间
			String tradeNo = conversionParams.get("trade_no");// 支付宝的交易号
			String outTradeNo = conversionParams.get("out_trade_no");// 获取商户之前传给支付宝的订单号（商户系统的唯一订单号）
			// String outBizNo = conversionParams.get("out_biz_no");//
			// 商户业务号(商户业务ID，主要是退款通知中返回退款申请的流水号)
			String buyerLogonId = conversionParams.get("buyer_logon_id");// 买家支付宝账号
			String sellerId = conversionParams.get("seller_id");// 卖家支付宝用户号
			String sellerEmail = conversionParams.get("seller_email");// 卖家支付宝账号
			String totalAmount = conversionParams.get("total_amount");// 订单金额:本次交易支付的订单金额，单位为人民币（元）
			String receiptAmount = conversionParams.get("receipt_amount");// 实收金额:商家在交易中实际收到的款项，单位为元
			String invoiceAmount = conversionParams.get("invoice_amount");// 开票金额:用户在交易中支付的可开发票的金额
			String buyerPayAmount = conversionParams.get("buyer_pay_amount");// 付款金额:用户在交易中支付的金额
			String tradeStatus = conversionParams.get("trade_status");// 获取交易状态

			// 支付宝官方建议校验的值（out_trade_no、total_amount、sellerId、app_id）
			AlipaymentOrder alipaymentOrder = alipaymentOrderService.selectByOrderCode(outTradeNo); // 查询出对应的支付宝订单记录

			if (alipaymentOrder != null && totalAmount.equals(alipaymentOrder.getTotalamount().toString())
					&& AlipayConfig.APPID.equals(appId)) {
				// 修改数据库支付宝订单表(因为要保存每次支付宝返回的信息到数据库里，以便以后查证)
				alipaymentOrder.setNotifytime(notifyTime);
				alipaymentOrder.setGmtcreatetime(gmtPayment);
				alipaymentOrder.setGmtrefundtime(gmtRefund);
				alipaymentOrder.setCreatetime(gmtCreate);
				alipaymentOrder.setGmtclosetime(gmtClose);
				alipaymentOrder.setTradeno(tradeNo);
				alipaymentOrder.setBuyerlogonid(buyerLogonId);
				alipaymentOrder.setSelleremail(sellerEmail);
				alipaymentOrder.setSellerid(sellerId);
				alipaymentOrder.setReceptamount(new BigDecimal(receiptAmount));
				alipaymentOrder.setInvoiceamount(Double.valueOf(invoiceAmount));
				alipaymentOrder.setBuyerpayamount(Double.valueOf(buyerPayAmount));

				switch (tradeStatus) // 判断交易结果
				{
				case "TRADE_FINISHED": // 交易结束并不可退款
					alipaymentOrder.setOrderstatus(3);
					;
					break;
				case "TRADE_SUCCESS": // 交易支付成功
					alipaymentOrder.setOrderstatus(2);
					;
					break;
				case "TRADE_CLOSED": // 未付款交易超时关闭或支付完成后全额退款
					alipaymentOrder.setOrderid(1);
					;
					break;
				case "WAIT_BUYER_PAY": // 交易创建并等待买家付款
					alipaymentOrder.setOrderstatus(0);
					;
					break;
				default:
					break;
				}

				logger.info("======支付宝交易状态======》》》：" + tradeStatus);
				int returnResult = alipaymentOrderService.updateByPrimaryKeySelective(alipaymentOrder); // 更新交易表中状态

				if (tradeStatus.equals("TRADE_SUCCESS")) { // 只处理支付成功的订单

					// 支付成功，更新订单表状态
					orderPay(outTradeNo, gmtPayment);

					if (returnResult > 0) {
						return "success";
					} else {
						return "fail";
					}
				} else {
					return "fail";
				}
			} else {
				logger.info("==================支付宝官方建议校验的值（out_trade_no、total_amount、sellerId、app_id）,不一致！返回fail");
				return "fail";
			}

		} else { // 验签不通过
			logger.info("==================验签不通过 ！");
			return "fail";
		}
	}

	/**
	 * 向支付宝发起订单查询请求
	 * 
	 * @param outTradeNo
	 * @return
	 */
	@Override
	public Result<?> alipayQuery(String outTradeNo) {
		logger.info("==================向支付宝发起查询，查询商户订单号为：" + outTradeNo);

		if (outTradeNo == "") {
			logger.info("支付宝查询的订单编号为空！");
			return Result.failureResult("查询的支付宝订单号为空！！！");
		}
		try {
			// 实例化客户端（参数：网关地址、商户appid、商户私钥、格式、编码、支付宝公钥、加密类型）
			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
					AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
					AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);

			AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();
			alipayTradeQueryRequest.setBizContent("{" + "\"out_trade_no\":\"" + outTradeNo + "\"" + "}");
			AlipayTradeQueryResponse alipayTradeQueryResponse = alipayClient.execute(alipayTradeQueryRequest);
			if (alipayTradeQueryResponse.isSuccess()) {

				AlipaymentOrder alipaymentOrder = alipaymentOrderService.selectByOrderCode(outTradeNo);
				// 修改数据库支付宝订单表
				alipaymentOrder.setOrdercode(alipayTradeQueryResponse.getTradeNo());
				alipaymentOrder.setBuyerlogonid(alipayTradeQueryResponse.getBuyerLogonId());
				alipaymentOrder.setTotalamount(new BigDecimal(alipayTradeQueryResponse.getTotalAmount()));
				alipaymentOrder.setReceptamount(new BigDecimal(alipayTradeQueryResponse.getReceiptAmount()));
				alipaymentOrder.setInvoiceamount(Double.parseDouble(alipayTradeQueryResponse.getInvoiceAmount()));
				alipaymentOrder.setBuyerpayamount(Double.parseDouble(alipayTradeQueryResponse.getBuyerPayAmount()));
				switch (alipayTradeQueryResponse.getTradeStatus()) // 判断交易结果
				{
				case "TRADE_FINISHED": // 交易结束并不可退款
					alipaymentOrder.setOrderstatus(3);
					;
					break;
				case "TRADE_SUCCESS": // 交易支付成功
					alipaymentOrder.setOrderstatus(2);
					break;
				case "TRADE_CLOSED": // 未付款交易超时关闭或支付完成后全额退款
					alipaymentOrder.setOrderstatus(1);
					break;
				case "WAIT_BUYER_PAY": // 交易创建并等待买家付款
					alipaymentOrder.setOrderstatus(0);
					break;
				default:
					break;
				}
				alipaymentOrderService.updateByPrimaryKeySelective(alipaymentOrder); // 更新表记录
				return Result.successResult(alipaymentOrder.getOrderstatus() + "");

			} else {
				logger.info("==================调用支付宝查询接口失败！");
			}
		} catch (AlipayApiException e) {
			logger.error("程序异常，支付宝订单查询失败！");
			e.printStackTrace();
		}
		return Result.failureResult("向支付宝发起订单查询失败");
	}

	/**
	 * 微信生成预支付订单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Result<String> getWXPayStr(HttpServletRequest request, HttpServletResponse response, Order order)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取生成预支付订单的请求类
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(request, response);
		BigDecimal totalFee = order.getTotalprice();
		logger.info("======获取订单的总金额====="+totalFee);
		// 测试时，每次支付一分钱，微信支付所传的金额是以分为单位的，因此实际开发中需要x100
		int total_fee = Integer.valueOf(totalFee.multiply(new BigDecimal(100)).toString());
		logger.info("total_fee:" + total_fee);
		prepayReqHandler.setParameter("appid", WXPayConfig.APP_ID);
		prepayReqHandler.setParameter("body", WXPayConfig.BODY);
		prepayReqHandler.setParameter("mch_id", WXPayConfig.MCH_ID);
		String nonce_str = WXUtil.getNonceStr();
		prepayReqHandler.setParameter("nonce_str", nonce_str);
		prepayReqHandler.setParameter("notify_url", WXPayConfig.NOTIFY_URL);
		String out_trade_no = order.getOrdercode();
		prepayReqHandler.setParameter("out_trade_no", out_trade_no);
		prepayReqHandler.setParameter("spbill_create_ip", request.getRemoteAddr());
		String timestamp = WXUtil.getTimeStamp();
		prepayReqHandler.setParameter("time_start", timestamp);
		// 测试时，每次支付一分钱，上线之后需要放开此代码
		//prepayReqHandler.setParameter("total_fee", String.valueOf(total_fee));
		prepayReqHandler.setParameter("total_fee", "1");
		prepayReqHandler.setParameter("trade_type", "APP");
		/**
		 * 注意签名（sign）的生成方式，具体见官方文档（传参都要参与生成签名，且参数名按照字典序排序，最后接上APP_KEY,转化成大写）
		 */
		prepayReqHandler.setParameter("sign", prepayReqHandler.createSHA1Sign());
		prepayReqHandler.setGateUrl(WXPayConfig.GATEURL);
		String prepayid = prepayReqHandler.sendPrepay();
		// 若获取prepayid成功，将相关信息返回客户端
		if (prepayid != null && !prepayid.equals("")) {
			// 创建微信订单支付记录
			AlipaymentOrder alipaymentOrder = new AlipaymentOrder();
			alipaymentOrder.setRemark("微信支付");
			alipaymentOrder.setTotalamount(new BigDecimal(total_fee/100));
			alipaymentOrder.setCreatetime(DateUtil.dateToString(new Date()));
			alipaymentOrder.setOrdercode(out_trade_no);
			alipaymentOrder.setOrderid(order.getOrderid());
			alipaymentOrder.setOrderstatus(0); 
			
			alipaymentOrderService.createAlipayMentOrder(alipaymentOrder);
			String signs = "appid=" + WXPayConfig.APP_ID + "&noncestr=" + nonce_str + "&package=Sign=WXPay&partnerid="
					+ WXPayConfig.PARTNER_ID + "&prepayid=" + prepayid + "&timestamp=" + timestamp + "&key="
					+ WXPayConfig.APP_KEY;
			
			map.put("code", 0);
			map.put("info", "success");
			map.put("prepayid", prepayid);
			//签名方式与上面类似
			map.put("sign", MD5Util.MD5Encode(signs, "utf8").toUpperCase());
			map.put("appid", WXPayConfig.APP_ID);
			map.put("timestamp", timestamp); // 等于请求prepayId时的time_start
			map.put("noncestr", nonce_str); // 与请求prepayId时值一致
			map.put("package", "Sign=WXPay"); // 固定常量
			map.put("partnerid", WXPayConfig.PARTNER_ID);
			return Result.successResult("获取微信预支付订单成功",map.toString());
		} else {
			
			return Result.failureResult("获取prepayid失败");
		}
	}

	/**
	 * 支付成功，更新订单状态
	 * 
	 * @return
	 */
	public int orderPay(String orderCode, String gmtPayment) {

		// 根据订单编号查询出订单
		Order order = orderDao.selectByOrderCode(orderCode);
		logger.info("根据订单编号查询出的订单：" + orderCode);
		order.setPaystatus(1); // 付款状态为已支付
		order.setAlipayStatus(1); // 支付成功
		order.setPaytime(gmtPayment); // 支付时间

		// 更新订单状态
		orderDao.updateByOrderCodeSelective(order);

		// 调用奖励计算

		// 记录到交易流水表中
		// accountService.addUserAccountDetail(order.getUserid(),
		// order.getTotalprice(), "减少", 902);

		return 0;
	}

	/**
	 * 下单处理
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Order buyOrder(Order order) {

		// 插入订单
		orderDao.insertSelective(order);

		// 插入订单明细
		for (OrderDetail orderDetail : order.getOrderDetailLst()) {
			orderDetail.setOrderid(order.getOrderid());
			orderDetailDao.insertSelective(orderDetail);
		}

		// test 这里不计算明细，后期加上
		/*
		 * order.setAwardDetailLst(order.getOrderDetailLst()); if
		 * (order.getAwardDetailLst() != null) {
		 * 
		 * for (OrderDetail orderDetail : order.getAwardDetailLst()) {
		 * orderDetail.setOrderid(order.getOrderid());
		 * orderDetailDao.insertSelective(orderDetail); } }
		 */
		return order;
	}

	// 更新订单状态发货
	@Override
	public int updateByPrimaryKeySelective(Order order, Integer userId) {

		// 添加一条记录到订单发货表中
		OrderSendOut orderSendOut = new OrderSendOut();
		orderSendOut.setOrderId(Long.valueOf(order.getOrderid()));
		orderSendOut.setAddress(order.getAddress());
		orderSendOut.setCreateTime(DateUtil.YYYY_MM_DD_MM_HH_SS.format(new Date()));
		orderSendOut.setOperatorId(Long.valueOf(userId));
		int i = orderSendOutService.addOrderSendout(orderSendOut);

		if (i <= 0) {
			return 0;
		}

		return orderDao.updateByPrimaryKeySelective(order);
	}

}
