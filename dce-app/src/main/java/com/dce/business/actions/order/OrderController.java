package com.dce.business.actions.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.dce.business.actions.common.BaseController;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.DateUtil;
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
import com.dce.business.service.award.IBonusLogService;
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
	/*@Resource
	private IBaodanService baodanService;
	@Resource
	private IBonusLogService bonusServiceLog;
	@Resource(name = "awardServiceAsync")
	private IAwardService awardService;*/
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

	
}