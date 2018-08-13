package com.dce.business.actions.order;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.dce.business.actions.common.BaseController;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.common.enums.PayStatus;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.CellFormatter;
import com.dce.business.common.util.DateUtil;
import com.dce.business.common.util.ExeclTools;
import com.dce.business.common.util.OrderCodeUtil;
import com.dce.business.dao.account.IUserAccountDetailDao;
import com.dce.business.entity.account.UserAccountDetailDo;
import com.dce.business.entity.goods.CTGoodsDo;
import com.dce.business.entity.order.Order;
import com.dce.business.entity.order.OrderDetail;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.account.IBaodanService;
import com.dce.business.service.accountRecord.AccountRecordService;
import com.dce.business.service.award.IAwardService;
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
	private ICTGoodsService ctGoodsService;
	@Resource
	private UserAdressService addressService;
	@Resource
	private IAccountService accountService;
	@Resource
	private IBaodanService baodanService;
	@Resource
	private IBonusLogService bonusServiceLog;
	@Resource(name = "awardServiceAsync")
	private IAwardService awardService;
	@Resource
    private IUserAccountDetailDao userAccountDetailDao;

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
		
		//设置商品名称
		for(Order order : orderLitst){
			if(order.getOrderDetailLst() != null){
				for(OrderDetail orderDetail : order.getOrderDetailLst()){
					long id =Long.valueOf(orderDetail.getGoodsId());
					logger.info("获取订单里面的商品id："+orderDetail.getGoodsId());
					
					CTGoodsDo goods = ctGoodsService.selectById(id);
					logger.info("商品名称："+goods.getTitle());
					
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

		// 获取商品信息
		String goods = request.getParameter("cart");
		logger.info("用户选择的goods信息：" + goods);

		// 将前台传过来的JSON数据解析为list集合
		List<OrderDetail> chooseGoodsLst = convertGoodsFromJson(goods);

		// 保存订单和订单明显
		Order order = saveOrder(chooseGoodsLst);
		return Result.successResult("返回商品清单", order);

	}

	/**
	 * 保存订单和订单明细
	 * 
	 * @param chooseGoodsLst
	 */
	private Order saveOrder(List<OrderDetail> chooseGoodsLst) {

		// 获取用户id
		Integer userId = this.getUserId();

		// 产生订单编号
		String orderCode = OrderCodeUtil.genOrderCode(userId);
		logger.info("产生的订单编号------》》》》》" + orderCode);

		Integer quantity = 0; // 商品总数量
		BigDecimal totalprice = new BigDecimal(0); //商品总价格
		for (OrderDetail orderDetail : chooseGoodsLst) { // 循环遍历出商品信息，计算商品总价格和商品总数量
			CTGoodsDo goods = ctGoodsService.selectById(Long.valueOf(orderDetail.getGoodsId())); 
			orderDetail.setGoodsName(goods.getTitle()); //获取商品名称
			quantity += orderDetail.getQty();
			totalprice = totalprice.multiply(BigDecimal.valueOf(orderDetail.getPrice()));
		}

		// 添加订单
		Order order = new Order();
		order.setUserid(userId);
		order.setOrdercode(orderCode); //订单号
		Date date = new Date();
		order.setCreatetime(DateUtil.dateformat(date));//订单创建时间
		order.setOrderstatus(0); // 未发货状态
		order.setPaystatus(0); // 未支付状态
		order.setQty(quantity); //商品总数量
		order.setTotalprice(totalprice); //商品总价格
		order.setOrderDetailList(chooseGoodsLst); //订单明细
		
		return orderService.buyOrder(order);
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
		logger.info("根据订单编号查询出的订单："+orderCode);
		order.setOrdertype(Integer.valueOf(orderPayType)); // 支付方式
		order.setPaystatus(1); // 付款状态为已支付
		order.setAlipayStatus(1); // 支付成功
		order.setAddressid(Integer.valueOf(addressId));
		Date date = new Date();
		order.setPaytime(DateUtil.dateformat(date)); // 支付时间
		
		// 更新订单状态
		orderService.updateByOrderCodeSelective(order);

		// 记录到交易流水表中
		UserAccountDetailDo userAccountDetail = new UserAccountDetailDo();
		userAccountDetail.setUserId(order.getUserid());
		userAccountDetail.setAmount(order.getTotalprice());
		userAccountDetail.setMoreOrLess("减少"); // 增加减少
		userAccountDetail.setIncomeType(902); // 商城消费
		userAccountDetail.setCreateTime(new Date());
		userAccountDetail.setRemark(IncomeType.TYPE_GOODS_BUY.getRemark());
		String seqId = UUID.randomUUID().toString();
		userAccountDetail.setSeqId(seqId);
		userAccountDetailDao.addUserAccountDetail(userAccountDetail);
		
		return Result.successResult("测试", null);
	}

	/**
	 * json 转OrderDetail对象 
	 * json 格式 
	 * [
	 * 	{ "goodsId": "0001", "qty": "20", "price":"597" }, 
	 * 	{ "goodsId": "0002", "qty": "10", "price": "637" } 
	 * ]
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

	/**
	 * 后台查询：交易流水 所有订单列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/selectOrderForReport", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView selectOrderForReport() {

		ModelAndView mav = new ModelAndView("listOrder");

		String type = getString("type"); // 类型：0-全部；1-待交易；2-交易中；3--已完成
		String orderType = getString("orderType"); // 类型
		String userName = getString("userName");
		String startDate = getString("startDate");
		String endDate = getString("endDate");
		String page = getString("p");

		Map<String, Object> params = new HashMap<>();

		if (StringUtils.isBlank(page)) {
			page = "1";
		}

		int rows = 10;
		int offset = (Integer.parseInt(page) - 1) * rows;
		params.put("offset", offset);
		params.put("rows", rows);

		if ("1".equals(type)) {
			params.put("payStatus", PayStatus.paying.getCode());
		} else if ("3".equals(type)) {
			params.put("payStatus", PayStatus.payed.getCode());
		}
		params.put("orderType", orderType);

		if (StringUtils.isNotBlank(userName)) {
			params.put("userName", userName);
		}
		if (StringUtils.isNotBlank(startDate)) {
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			params.put("endDate", endDate);
		}

		List<Map<String, Object>> orderList = orderService.selectOrderForReport(params);

		int total = orderService.selectOrderForReportCount(params);

		Integer totalPage = total / rows;
		if (total % rows != 0) {
			totalPage++;
		}

		Integer currentPage = 1;
		if (StringUtils.isNotBlank(page)) {
			currentPage = Integer.valueOf(page);
		}

		Integer startPage = getStartPage(currentPage);

		mav.addObject("orderList", orderList);
		mav.addObject("currentPage", currentPage);
		mav.addObject("startPage", startPage);
		mav.addObject("endPage", getEndPage(startPage, totalPage));
		mav.addObject("totalPage", totalPage);
		mav.addObject("userName", userName);
		mav.addObject("startDate", startDate);
		mav.addObject("endDate", endDate);

		return mav;

	}

	private List<Map<String, Object>> doQuery() {
		String type = getString("type"); // 类型：0-全部；1-待交易；2-交易中；3--已完成
		String orderType = getString("orderType"); // 类型
		String userName = getString("userName");
		String startDate = getString("startDate");
		String endDate = getString("endDate");

		Map<String, Object> params = new HashMap<>();
		if ("1".equals(type)) {
			params.put("payStatus", PayStatus.paying.getCode());
		} else if ("3".equals(type)) {
			params.put("payStatus", PayStatus.payed.getCode());
		}
		params.put("orderType", orderType);

		if (StringUtils.isNotBlank(userName)) {
			params.put("userName", userName);
		}
		if (StringUtils.isNotBlank(startDate)) {
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			params.put("endDate", endDate);
		}

		List<Map<String, Object>> orderList = orderService.selectOrderForReport(params);
		return orderList;
	}

	private Integer getStartPage(Integer currentPage) {
		Integer startPage = 1;
		if (currentPage > 5) {
			startPage = currentPage - 5;
		}

		return startPage;
	}

	private Integer getEndPage(Integer startPage, Integer totalPage) {
		Integer endPage = startPage + 10;
		if (endPage > totalPage) {
			endPage = totalPage;
		}

		return endPage;
	}

	/**
	 * 导出用户数据
	 */
	@RequestMapping("/export")
	public void export(HttpServletResponse response) throws IOException {

		try {
			Long time = System.currentTimeMillis();

			List<Map<String, Object>> orderList = doQuery();

			String excelHead = "数据导出";
			String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String fileName = URLEncoder.encode(excelHead + date + ".xls", "utf-8");
			List<String[]> excelheaderList = new ArrayList<String[]>();
			String[] excelheader = { "单号", "客户名", "客户级别", "总量", "单价", "总价", "交易类型", "成交时间", "状态" };
			excelheaderList.add(0, excelheader);

			String[] excelData = { "orderCode", "user_name", "user_level", "qty", "price", "totalPrice", "orderType",
					"payTime", "payStatus" };
			Map<String, CellFormatter> cellFormatterMap = new HashMap<String, CellFormatter>();
			cellFormatterMap.put("orderType", new CellFormatter() {
				public String formatterValue(Object input) {
					if (null == input) {
						return "";
					}
					if (StringUtils.isBlank(String.valueOf(input))) {
						return "";
					}
					String ret = "";
					switch (String.valueOf(input)) {
					case "1":
						ret = "买单";
						break;
					case "2":
						ret = "卖单";
						break;
					case "3":
						ret = "商城消费";
						break;
					default:
						break;
					}
					return ret;
				}
			});
			cellFormatterMap.put("payTime", new CellFormatter() {
				public String formatterValue(Object input) {
					if (null == input) {
						return "";
					}

					if (input instanceof String) {
						return (String) input;
					}
					if (input instanceof Date) {
						return DateUtil.YYYY_MM_DD_HH.format(input);
					}

					Date d = null;

					if (input instanceof Long) {
						d = new Date((Long) (input));
					}
					if (input instanceof Integer) {
						d = new Date((Integer) (input));
					}
					if (d == null) {
						return "";
					}
					return DateUtil.YYYY_MM_DD_HH.format(d);
				}
			});
			cellFormatterMap.put("payStatus", new CellFormatter() {
				public String formatterValue(Object input) {
					if (null == input) {
						return "";
					}
					if (StringUtils.isBlank(String.valueOf(input))) {
						return "";
					}
					String ret = "待交易";
					switch (String.valueOf(input)) {
					case "3":
						ret = "交易完成";
						break;
					default:
						break;
					}
					return ret;
				}
			});
			HSSFWorkbook wb = ExeclTools.execlExport(excelheaderList, excelData, excelHead, orderList,
					cellFormatterMap);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			wb.write(response.getOutputStream());
			time = System.currentTimeMillis() - time;
			logger.info("导出用户数据，导出耗时(ms)：" + time);
		} catch (Exception e) {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().println("下载失败");
			logger.error("导出用户数据，Excel下载失败", e);
			logger.error("导出用户数据异常", e);
			throw new BusinessException("系统繁忙，请稍后再试");
		} finally {
			response.flushBuffer();
		}

	}

}
