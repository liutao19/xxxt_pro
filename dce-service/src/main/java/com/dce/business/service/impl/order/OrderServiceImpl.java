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
        orderDao.insertSelective(orderDo);
        return orderDo.getOrderId();
    }

    @Override
    public OrderDo getOrderDo(Long orderId) {
        return orderDao.selectByPrimaryKey(orderId);
    }

    /**
     * 买卖挂单前判断
     * @param userId
     * @param orderId
     * @param qty
     * @param orderType 对应操作的目标订单类型 比如当前是要买入挂单中的额度 那么目标订单的类型需要为卖出(2)类型
     * 			如果是卖额度给挂单中的订单 那么目标订单需要为买入(1)类型
     * @return
     */
    private Result<?> beforMatchOrder(Integer userId, OrderDo matchOrder,BigDecimal qty,int orderType){
    	if(matchOrder == null){
    		return Result.failureResult("未查询到对应挂单!");
    	}
    	//判断对应挂单是否有效
    	if(matchOrder.getOrderStatus() == 0 || matchOrder.getPayStatus() == 1 || matchOrder.getOrderType().intValue() != orderType){
    		return Result.failureResult("无效的挂单!");
    	}
    	//判断不能匹配买入自己的订单
    	if(userId.intValue() == matchOrder.getUserId().intValue()){
    		return Result.failureResult("不能买卖自己挂的单");
    	}
    	
    	//判断挂出的订单中是否还有足够的币(>=qty)卖给userId
    	BigDecimal salqty = matchOrder.getSalqty();
    	if(salqty == null){
    		salqty = BigDecimal.ZERO;
    	}
    	BigDecimal gd_qty = matchOrder.getQty().subtract(salqty);
    	if(gd_qty.compareTo(qty) < 0){
    		return Result.failureResult("当前挂单中没有足够余额可售出!");
    	}
    	
    	//判断用户美元点账户是否有足够的美元点来买入qty数量的DEC币  
    	//计算公式  所需美元点 = qty * 挂单价格
//    	LoanDictDtlDo MYDBXCC = loanDictService.getLoanDictDtl(DictCode.MYDBXCC.getCode());
//    	if(MYDBXCC == null){
//    		return Result.failureResult("未查询到美元点与现持仓转换比例,请联系管理员!");
//    	}
    	UserAccountDo pointAcc = null;
    	/** 
    	 * 如果订单是卖出  那么是当前用户在买入  所以需要查询当前用户(userId)的美元点账户是否有足够额度
    	 * 如果订单是买入 那么当前用户是在卖出DEC币  所以需要查询挂单用户的美元点账户是否有足够的额度来买当前用户的DEC币
    	 */
    	if(orderType == OrderType.GD_SAL.getOrderType()){
    		pointAcc = accountService.selectUserAccount(userId, matchOrder.getAccountType());
    	}else{
    		pointAcc = accountService.selectUserAccount(matchOrder.getUserId(), matchOrder.getAccountType());
    	}
    	BigDecimal needpoint = qty.multiply(matchOrder.getPrice()).setScale(6, RoundingMode.HALF_UP);
//    	needpoint = needpoint.divide(new BigDecimal(rmb2point.getRemark()),6,RoundingMode.HALF_UP);
    	if(pointAcc == null || pointAcc.getAmount().compareTo(needpoint) < 0){
    		return Result.failureResult("当前现金币账户余额不足");
    	}
    	
    	return Result.successResult("条件判断成功,符合买卖条件",needpoint);
    	
    }
    
    private void afterMatchOrder(OrderDo matchOrder){
    	//记录K线数据
        KLineDo kLineDo = new KLineDo();
        kLineDo.setPrice(matchOrder.getPrice());
        kLineDo.setQty(matchOrder.getQty());
        kLineDo.setTotalAmount(matchOrder.getTotalPrice());
        kLineDo.setCtime(new Date());
        kLineDao.insertSelective(kLineDo);
    }
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<?> buyOrder(Integer userId, Long orderId,BigDecimal qty) {
    	logger.info("买入挂单 : userId=" + userId + ",要买入的订单orderId=" + orderId + ",qty=" + qty);
    	OrderDo matchOrder = getOrderDo(Long.valueOf(orderId));
    	
    	Result<?> bfResult = beforMatchOrder(userId, matchOrder, qty,OrderType.GD_SAL.getOrderType());
    	if(!bfResult.isSuccess()){
    		return bfResult;
    	}
    	//一切准备就绪  先修改挂单订单的状态,已售信息
    	OrderDo order = new OrderDo();
    	order.setOrderId(matchOrder.getOrderId());
    	
    	/** 如果 挂单中的已卖出量(salqty)+当前卖出量(qty) >= 挂单量(gd_qty)   则支付状态改为已支付 **/
    	BigDecimal salqty = matchOrder.getSalqty();
    	if(salqty == null){
    		salqty = BigDecimal.ZERO;
    	}
    	//挂单中已卖量+当前要卖的量
    	BigDecimal salqyt = salqty.add(qty);
    	if(salqyt.compareTo(matchOrder.getQty()) >=0){
    		order.setPayStatus(1);
    	}
    	order.setPayTime(new Date());
    	order.setSalqty(qty);
    	int i = orderDao.updateMatchOrder(order);
    	if(i <= 0){
    		return Result.failureResult("买入挂单失败!");
    	}
    	
    	BigDecimal needpoint = (BigDecimal) bfResult.getData();
    	/** 更新两个用户之间的美元点账户余额 **/
    	accountService.convertBetweenAccount(userId, matchOrder.getUserId(), needpoint, AccountType.wallet_cash.getAccountType(), AccountType.wallet_cash.getAccountType(), IncomeType.TYPE_PURCHASE, IncomeType.TYPE_SELL);
    	
        //需要扣减卖家手续费，美元点
        CtCurrencyDo ctCurrencyDo = ctCurrencyService.selectByName(CurrencyType.IBAC.name());
        if (ctCurrencyDo != null && ctCurrencyDo.getCurrency_sell_fee() != null) {
            BigDecimal feeRate = ctCurrencyDo.getCurrency_sell_fee().divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP);
            BigDecimal fee = feeRate.multiply(needpoint).setScale(6, RoundingMode.HALF_UP);
            logger.info("交易手续费：" + fee);

            UserAccountDo feeAccount = new UserAccountDo();
            feeAccount.setUserId(matchOrder.getUserId());
            feeAccount.setAccountType(AccountType.wallet_cash.getAccountType());
            feeAccount.setAmount(fee.negate());
            accountService.updateUserAmountById(feeAccount, IncomeType.TYPE_TRADE_FEE);
        }
    	
    	
    	/** 更新当前买入用户的现持仓额度 += qty **/
    	UserAccountDo currentAcc = new UserAccountDo();
    	currentAcc.setAccountType(matchOrder.getAccountType());
    	currentAcc.setAmount(qty);
    	currentAcc.setUserId(userId);
    	accountService.updateUserAmountById(currentAcc, IncomeType.TYPE_PURCHASE);
    	
    	/** 给当前用户买入挂单增加一笔订单流水 **/
        OrderDo newOrder = new OrderDo();
        newOrder.setCreateTime(new Date());
        newOrder.setGoodsId(matchOrder.getGoodsId());
        newOrder.setOrderStatus(OrderStatus.effective.getCode());
        newOrder.setQty(qty);
        newOrder.setPayStatus(1);
        newOrder.setUserId(userId);
        newOrder.setOrderType(matchOrder.getOrderType().intValue() == 1 ? 2 : 1);
        newOrder.setPrice(matchOrder.getPrice());
        newOrder.setTotalPrice(needpoint);
        newOrder.setOrderCode(OrderCodeUtil.genOrderCode(userId));
        newOrder.setMatchOrderId(matchOrder.getOrderId());
        newOrder.setPayTime(new Date());
        addOrder(newOrder);

        afterMatchOrder(matchOrder);
        return Result.successResult("购买订单成功!");
    }

    @Override
	public Result<?> salOrder(Integer userId, Long orderId, BigDecimal qty) {
    	logger.info("卖给挂单 : userId=" + userId + ",要买入的订单orderId=" + orderId + ",qty=" + qty);
    	
    	OrderDo matchOrder = getOrderDo(Long.valueOf(orderId));
    	Result<?> bfResult = beforMatchOrder(userId, matchOrder, qty, 1);
    	if(!bfResult.isSuccess()){
    		return bfResult;
    	}
    	
    	/** 判断现持仓是否有足够(>=qty)的币可以出售 **/
    	UserAccountDo currentAcc = accountService.selectUserAccount(userId, matchOrder.getAccountType());
    	if(currentAcc.getAmount().compareTo(qty) < 0){
    		return Result.failureResult("你当前现持仓没有足够的币可以出售!");
    	}
    	
    	/** 卖给挂单 **/
    	OrderDo order = new OrderDo();
    	order.setOrderId(matchOrder.getOrderId());
    	order.setPayTime(new Date());
    	order.setSalqty(qty);
    	
    	BigDecimal salqty = matchOrder.getSalqty();
    	if(salqty == null){
    		salqty = BigDecimal.ZERO;
    	}
    	salqty = salqty.add(qty);
    	
    	if(salqty.compareTo(matchOrder.getQty()) >= 0){
    		order.setPayStatus(1);
    	}
    	int i = orderDao.updateMatchOrder(order);
    	if(i <= 0){
    		return Result.failureResult("卖出挂单失败!");
    	}
    	
    	BigDecimal needpoint = (BigDecimal) bfResult.getData();
    	/** 更新两个用户之间的美元点账户余额 **/
    	accountService.convertBetweenAccount(matchOrder.getUserId(), userId,qty.negate(), needpoint, matchOrder.getAccountType(), AccountType.wallet_cash.getAccountType(), IncomeType.TYPE_SELL, IncomeType.TYPE_PURCHASE);
    	/** 更新当前卖出DEC用户的现持仓额度 -= qty **/
    	UserAccountDo _currentAcc = new UserAccountDo();
    	_currentAcc.setAccountType(matchOrder.getAccountType());
    	_currentAcc.setAmount(qty.negate());
    	_currentAcc.setUserId(userId);
    	accountService.updateUserAmountById(_currentAcc, IncomeType.TYPE_SELL);
    	
    	/** 给当前用户买入挂单增加一笔订单流水 **/
        OrderDo newOrder = new OrderDo();
        newOrder.setCreateTime(new Date());
        newOrder.setGoodsId(matchOrder.getGoodsId());
        newOrder.setOrderStatus(OrderStatus.effective.getCode());
        newOrder.setQty(qty);
        newOrder.setPayStatus(1);
        newOrder.setUserId(userId);
        newOrder.setOrderType(matchOrder.getOrderType().intValue() == 1 ? 2 : 1);
        newOrder.setPrice(matchOrder.getPrice());
        newOrder.setTotalPrice(needpoint);
        newOrder.setOrderCode(OrderCodeUtil.genOrderCode(userId));
        newOrder.setMatchOrderId(matchOrder.getOrderId());
        newOrder.setPayTime(new Date());
        addOrder(newOrder);
        
        afterMatchOrder(matchOrder);
        return Result.successResult("卖出订单成功!");
	}
    
    @Override
    public Result<?> matchOrder(Integer userId, Long orderId,BigDecimal qty){
    	
    	//判断系统设置是否可交易
		CtCurrencyDo ct = ctCurrencyService.selectByName(CurrencyType.IBAC.name());
		if(ct == null || ct.getIs_lock() == null || 0 == ct.getIs_lock().intValue()){
			logger.info("交易设置关闭,当前不允许买卖挂单交易....");
			return Result.failureResult("当前系统不允许交易!");
		}
    			
    	OrderDo matchOrder = getOrderDo(Long.valueOf(orderId));
    	if(matchOrder == null){
    		return Result.failureResult("订单不存在!");
    	}
    	//挂单类型为买   则是当前用户卖DEC  反之则是买DEC
    	if(matchOrder.getOrderType().intValue() == OrderType.GD_BUY.getOrderType()){
    		
    		return salOrder(userId, orderId, qty);
    	}else{
    		
    		return buyOrder(userId, orderId, qty);
    	}
    }
    
    @Override
    public List<OrderDo> selectOrder(Map<String, Object> params) {
        return orderDao.selectOrder(params);
    }

    @Override
    public int updateOrder(OrderDo orderDo) {
        if (orderDo == null) {
            return 0;
        }
        return orderDao.updateByPrimaryKeySelective(orderDo);
    }

	@Override
	public Result<?> guadan(OrderDo orderDo) {
		
		logger.info("开始挂单:" + JSON.toJSONString(orderDo));
		
		//判断系统设置是否可交易
		CtCurrencyDo ct = ctCurrencyService.selectByName(CurrencyType.IBAC.name());
		if(ct == null || ct.getIs_lock() == null || 0 == ct.getIs_lock().intValue()){
			logger.info("交易设置关闭,当前不允许挂单交易....");
			return Result.failureResult("当前系统不允许交易!");
		}
		
		//判断挂单价格是否合理  在当日交易价格的 上下10%浮动
		if(ct.getPrice_down() == null || ct.getPrice_up() == null){
			return Result.failureResult("当日浮动价格未设置,请联系管理员!");
		}
		if(orderDo.getPrice().compareTo(ct.getPrice_down()) < 0 || orderDo.getPrice().compareTo(ct.getPrice_up()) > 0){
			return Result.failureResult("所定价格不合理!");
		}
		
		if(orderDo.getOrderType().intValue() == OrderType.GD_BUY.getOrderType()){
			
			return guadanBuy(orderDo);
		}else if(orderDo.getOrderType().intValue() == OrderType.GD_SAL.getOrderType()){
			
			return guadanSal(orderDo);
		}else{
			return Result.failureResult("无效的挂单类型");
		}
	}
	/**
	 * 挂单买入
	 */
	private Result<?> guadanBuy(OrderDo orderDo){
		//判断当前用户现持仓是否有足够的DEC
		UserAccountDo currentAcc = accountService.selectUserAccount(orderDo.getUserId(), AccountType.wallet_cash.getAccountType());
		
		BigDecimal needpoint = orderDo.getQty().multiply(orderDo.getPrice()).setScale(6, RoundingMode.HALF_UP);
		if(currentAcc == null || currentAcc.getAmount() == null ||currentAcc.getAmount().compareTo(needpoint) < 0){
			logger.info("买入，没有足够美元点");
			return Result.failureResult("现金币余额不足");
		}
		
		//从美元点减 qty的额度用于挂单
		UserAccountDo _acct = new UserAccountDo();
		_acct.setAccountType(AccountType.wallet_cash.getAccountType());
		_acct.setAmount(needpoint.negate());
		_acct.setUserId(orderDo.getUserId());
		accountService.updateUserAmountById(_acct, IncomeType.TYPE_GD_BUY);
				
		Long id = addOrder(orderDo);
		if(id == null){
			return Result.failureResult("挂单失败");
		}else{
			return Result.successResult("挂单成功");
		}
		
	}
	/**
	 * 挂单卖出
	 * @param orderDo
	 * @return
	 */
	private Result<?> guadanSal(OrderDo orderDo){
		//判断当前用户现持仓是否有足够的DEC
		UserAccountDo currentAcc = accountService.selectUserAccount(orderDo.getUserId(), orderDo.getAccountType());
		if(currentAcc == null || currentAcc.getAmount() == null ||currentAcc.getAmount().compareTo(orderDo.getQty()) < 0){
			logger.info("当前现持仓没有足够的额度挂单,当前额度");
			return Result.failureResult("钱包的余额不足，挂单失败");
		}
		
		//从现持仓减 qty的额度用于挂单
		UserAccountDo _acct = new UserAccountDo();
		_acct.setAccountType(orderDo.getAccountType());
		_acct.setAmount(orderDo.getQty().negate());
		_acct.setUserId(orderDo.getUserId());
		int i = accountService.updateUserAmountById(_acct, IncomeType.TYPE_GD_SAL);
		
		//挂单
		Long id = addOrder(orderDo);
		
		if(i <= 0 || id == null){
			return Result.failureResult("挂单失败!");
		}else{
			return Result.successResult("挂单成功");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Result<?> cancel(Long orderId, Integer userId) {
		//1、订单状态校验，只有待交易的订单才允许撤销
		OrderDo order = getOrderDo(orderId);
		if(order.getPayStatus().intValue() == 1 || order.getOrderStatus().intValue() == 0){
			return Result.failureResult("当前订单状态不能撤销");
		}
        //2、只能撤销自己的订单
		if(order.getUserId().intValue() != userId.intValue()){
			return Result.failureResult("撤销的订单不属于当前用户!撤销失败!");
		}
		
		//如果是卖出订单  需要将订单中 剩余的DEC返回到用户的 现持仓账户
		if(order.getOrderType().intValue() == OrderType.GD_SAL.getOrderType()){
			
			logger.info("退回挂单中未卖出的DEC到用户现持仓.....");
			//计算当前订单中还有多少个DEC没卖出去
			BigDecimal salqty = order.getSalqty();
	    	if(salqty == null){
	    		salqty = BigDecimal.ZERO;
	    	}
			BigDecimal remain = order.getQty().subtract(salqty);
			
			UserAccountDo currentAcc = new UserAccountDo();
			currentAcc.setAccountType(order.getAccountType());
			currentAcc.setAmount(remain);
			currentAcc.setUserId(order.getUserId());
			accountService.updateUserAmountById(currentAcc, IncomeType.TYPE_CANCEL);
		}else if(order.getOrderType().intValue() == OrderType.GD_BUY.getOrderType()){
			logger.info("退回挂单中买单美元到用户美元点.....");
			UserAccountDo currentAcc = new UserAccountDo();
			currentAcc.setAccountType(AccountType.wallet_cash.getAccountType());
			currentAcc.setAmount(order.getTotalPrice());
			currentAcc.setUserId(order.getUserId());
			accountService.updateUserAmountById(currentAcc, IncomeType.TYPE_CANCEL_BUY);
		}
		
		//修改订单状态为撤销
		OrderDo _ord = new OrderDo();
		_ord.setOrderId(order.getOrderId());
		_ord.setPayStatus(1);
		_ord.setOrderStatus(OrderStatus.invalid.getCode());
		
		logger.info("修改订单状态 ,撤销订单...");
		int flag = updateOrder(_ord);
		if(flag > 0){
			return Result.successResult("订单撤销成功");
		}else{
			
			return Result.failureResult("订单撤销失败");
		}
	}

	@Override
	public Map<String, Object> getBaseInfo(String date) {
		
		Map<String,Object> result = orderDao.getBaseInfo(date);
		if(result == null){
			result = new HashMap<String,Object>();
		}
		return result;
	}

	@Override
	public List<Map<String,Object>> selectOrderForReport(Map<String, Object> paraMap) {
		return orderDao.selectOrderForReport(paraMap);
	}

	@Override
	public int selectOrderForReportCount(Map<String, Object> paraMap) {
		return orderDao.selectOrderForReportCount(paraMap);
	}

	@Override
	public PageDo<Map<String, Object>> selectOrderByPage(
			PageDo<Map<String, Object>> page, Map<String, Object> params) {
		if(params == null){
			params = new HashMap<String, Object>();
		}
        params.put(Constants.MYBATIS_PAGE, page);
        List<Map<String,Object>> list =  orderDao.selectOrderByPage(params);
        page.setModelList(list);
		return page;
	}

	@Override
	public Long selectGuadanAmount(Map<String, Object> paraMap) {
		return orderDao.selectGuadanAmount(paraMap);
	}
	
}
