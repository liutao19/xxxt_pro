package com.dce.business.actions.order;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.dce.business.actions.common.BaseController;
import com.dce.business.common.enums.OrderStatus;
import com.dce.business.common.enums.PayStatus;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.CellFormatter;
import com.dce.business.common.util.DateUtil;
import com.dce.business.common.util.ExeclTools;
import com.dce.business.common.util.NumberUtil;
import com.dce.business.common.util.OrderCodeUtil;
import com.dce.business.entity.bonus.BonusLogDo;
import com.dce.business.entity.order.OrderDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.account.IBaodanService;
import com.dce.business.service.award.IAwardService;
import com.dce.business.service.award.IBonusLogService;
import com.dce.business.service.order.IOrderService;

@RestController
@RequestMapping("order")
public class OrderController extends BaseController {
    private final static Logger logger = Logger.getLogger(OrderController.class);

    @Resource
    private IOrderService orderService;
    @Resource
    private IAccountService accountService;
    @Resource
    private IBaodanService baodanService;
    
    @Resource
    private IBonusLogService bonusServiceLog;
    
    @Resource(name = "awardServiceAsync")
    private IAwardService awardService;
    
    /** 
     * 重新计算奖金
     * @return  
     */
    @RequestMapping(value = "/redoAwardForError", method = RequestMethod.POST)
    public Result<?> redoAwardForError() {
    	 String redoId = getString("redoId");
    	 if(StringUtils.isBlank(redoId)){
    		 return  Result.failureResult("无效参数");
    	 }
    	 BonusLogDo  bonusLog =  bonusServiceLog.selectBonusLogById(Integer.valueOf(redoId));
    	 awardService.calAward(bonusLog.getUserId(), bonusLog.getAmount(), bonusLog.getUserLevel());
    	 bonusServiceLog.updateRedoStatusById(Integer.valueOf(redoId));
    	 return Result.successResult("提交成功");
    }
    

    /** 
     * 挂单
     * @return  
     */
    @RequestMapping(value = "/guadan", method = RequestMethod.POST)
    public Result<?> guadan() {
        Integer userId = getUserId();
        String price = getString("price");
        String qty = getString("qty");
        String orderType = getString("orderType");//1--买入；2--卖出
        logger.info("用户挂单, userId:" + userId + "; price:" + price + "; qty:" + qty + "; orderType:" + orderType);
        if(StringUtils.isBlank(price)){
        	logger.error("买入价格不能为空");
        	Assert.hasText(price,"挂单价格不能为空");
        	return Result.failureResult("买入价格不能为空");
        }
        
        if(StringUtils.isBlank(qty)){
        	logger.error("买入数量不能为空");
        	Assert.hasText(qty, "买入数量不能为空");
        	return Result.failureResult("买入数量不能为空");
        }else{
        	BigDecimal sal_qty = new BigDecimal(qty);
        	if(sal_qty.compareTo(BigDecimal.ZERO) <= 0){
        		return Result.failureResult("买入数量不能小于0个");
        	}
        }
        
        if(StringUtils.isBlank(orderType)){
        	logger.error("挂单类型不能为空");
        	Assert.hasText(orderType, "挂单类型不能为空");
        	return Result.failureResult("挂单类型不能为空");
        }

        try{
	        OrderDo orderDo = new OrderDo();
	        orderDo.setOrderCode(OrderCodeUtil.genOrderCode(userId));
	        orderDo.setOrderType(Integer.valueOf(orderType));
	        orderDo.setUserId(userId);
	        orderDo.setPrice(new BigDecimal(price));
	        orderDo.setQty(new BigDecimal(qty));
	        orderDo.setTotalPrice(orderDo.getQty().multiply(orderDo.getPrice()).setScale(6, RoundingMode.HALF_UP));
	        orderDo.setOrderStatus(OrderStatus.effective.getCode()); //有效
	        orderDo.setPayStatus(PayStatus.paying.getCode());   //未成交
	        orderDo.setCreateTime(new Date());
	
	        return orderService.guadan(orderDo);
        }catch(Exception e){
        	logger.error("挂单报错:" , e);
        	return Result.failureResult("挂单报错!");
        }
    }

    /** 
     * 买入、卖出
     * @return  
     */
    @RequestMapping(value = "/matchOrder", method = RequestMethod.POST)
    public Result<?> matchOrder() {
        Integer userId = getUserId();
        String orderId = getString("orderId"); //订单id
        String qty = getString("qty");
        try{
	        Assert.notNull(orderId, "请选择要交易的订单");
	        Assert.hasText(qty, "请输入要买入的金额");
	
	        Result<?> result = orderService.matchOrder(userId, Long.parseLong(orderId),new BigDecimal(qty));
	        logger.info("买入卖出挂单结果:" + JSON.toJSONString(result));
	        
	        return result;
        
        }catch(Exception e){
        	logger.error("买卖单报错!",e);
        	return Result.failureResult("买卖单报错");
        }
    }

    /** 
     * 我的订单列表
     * @return  
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result<?> list() {
        Integer userId = getUserId();
        String type = getString("type"); //类型：0-全部；1-待交易；2-交易中；3--已完成

        Assert.hasText(type, "查询订单列表类型不能为空");

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        if ("1".equals(type)) {
            params.put("payStatus", PayStatus.paying.getCode());
            params.put("orderStatus", "1"); //有效订单
        } else if ("3".equals(type)) {
            params.put("payStatus", PayStatus.payed.getCode());
            params.put("orderStatus", "1"); //有效订单
        }

        List<OrderDo> orderList = orderService.selectOrder(params);
        List<Map<String, Object>> list = new ArrayList<>();
        for (OrderDo order : orderList) {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", order.getOrderId()); //订单id
            map.put("qty", order.getQty()); //数量
            map.put("price", order.getPrice()); //单价
            map.put("totalPrice", order.getTotalPrice()); //总额
            map.put("orderStatus", order.getOrderStatus()); //0-无效；1有效
            map.put("date", DateUtil.dateToString(order.getCreateTime())); //挂单时间
            map.put("orderType", order.getOrderType()); //订单类型
            map.put("payStatus", order.getPayStatus()); //支付状态 0 待付  1 已支付
            
            list.add(map);
        }
        return Result.successResult("成功", list);
    }
    
    
    /** 
     * 后台查询：交易流水
     * 所有订单列表
     * @return  
     */
    @RequestMapping(value = "/selectOrderForReport", method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView selectOrderForReport() {
    	
    	ModelAndView mav = new ModelAndView("listOrder");
    	
    	String type = getString("type"); //类型：0-全部；1-待交易；2-交易中；3--已完成
        String orderType = getString("orderType"); //类型
        String userName = getString("userName"); 
        String startDate = getString("startDate"); 
        String endDate = getString("endDate"); 
        String page = getString("p");
        
        Map<String, Object> params = new HashMap<>();
        
        if(StringUtils.isBlank(page)){
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
        if(StringUtils.isNotBlank(startDate)){
    		params.put("startDate", startDate);
    	}
    	if(StringUtils.isNotBlank(endDate)){
    		params.put("endDate", endDate);
    	}
    	
        List<Map<String,Object>> orderList = orderService.selectOrderForReport(params);

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
		String type = getString("type"); //类型：0-全部；1-待交易；2-交易中；3--已完成
        String orderType = getString("orderType"); //类型
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
        if(StringUtils.isNotBlank(startDate)){
    		params.put("startDate", startDate);
    	}
    	if(StringUtils.isNotBlank(endDate)){
    		params.put("endDate", endDate);
    	}
    	
        List<Map<String,Object>> orderList = orderService.selectOrderForReport(params);
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
     * 挂单列表
     * @return  
     */
    @RequestMapping(value = "/guadanList", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<?> guadanList() {
        Map<String, Object> params = new HashMap<>();
        params.put("payStatus", "0"); //查询待交易的订单
        params.put("orderStatus", "1"); //过滤撤销了，无效的订单

        List<OrderDo> orderList = orderService.selectOrder(params);
        List<Map<String, Object>> list = new ArrayList<>();
        for (OrderDo order : orderList) {
        	if(order.getOrderType().intValue() == 3){ //过滤商城订单
        		continue;
        	}
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", order.getOrderId()); //订单id
            map.put("qty",  NumberUtil.formatterBigDecimal(order.getRemainQty())); //剩余交易数量
            map.put("price", NumberUtil.formatterBigDecimal(order.getPrice())); //单价
            map.put("amount", NumberUtil.formatterBigDecimal(order.getTotalPrice())); //总额
            map.put("date", DateUtil.dateToString(order.getCreateTime())); //挂单时间
            map.put("orderType", order.getOrderType()); //订单类型
            list.add(map);
        }
        return Result.successResult("成功", list);
    }

    /**
     * cancel撤销订单
     * @return
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public Result<?> cancel(){
    	
    	String orderId = getString("orderId");
    	if(StringUtils.isBlank(orderId)){
    		return Result.failureResult("请选择要撤销的订单");
    	}
    	
    	Integer userId = getUserId();
    	
    	Result<?> result = orderService.cancel(Long.parseLong(orderId), userId);
    	logger.info("订单撤销结果:" + JSON.toJSON(result));
    	return result;
    }

    /**
     * 报单
     * @return
     */
    @RequestMapping(value = "/baodan", method = RequestMethod.POST)
    public Result<?> baodan() {
        String qty = getString("qty");
        String accountType = getString("buyType");
        String level = getString("userLevel");

        logger.info("报单：qty=" + qty + ",buyType=" + accountType);

        Assert.hasText(qty, "数量不能为空!");
        Assert.hasText(level, "用户级别不能为空!");
        Assert.hasText(accountType, "请选择美元点或现持仓!");

        BigDecimal qtyVal = BigDecimal.ZERO;
        try {
            qtyVal = new BigDecimal(qty);
            if (qtyVal.compareTo(BigDecimal.ZERO) <= 0) {
                return Result.failureResult("报单数量必须大于0!");
            }
        } catch (Exception e) {
        	logger.error("报单出错1",e);
            return Result.failureResult("无效的报单数量!");
        }

        Integer userId = getUserId();
        try {
            baodanService.baodan(userId, qtyVal, accountType, Integer.valueOf(level));
            return Result.successResult("报单成功!");
        } catch (Exception e) {
        	logger.error("报单出错1",e);
            return Result.failureResult(e.getMessage());
        }

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
            String[] excelheader = {"单号","客户名" ,"客户级别",  "总量", "单价", "总价", "交易类型", "成交时间", "状态"};
            excelheaderList.add(0, excelheader);			
			
            String[] excelData = { "orderCode", "user_name", "user_level", "qty", "price", "totalPrice", "orderType", "payTime","payStatus"};
            Map<String,CellFormatter> cellFormatterMap = new HashMap<String,CellFormatter>();
            cellFormatterMap.put("orderType", new CellFormatter(){
				public String formatterValue(Object input) {
					if(null == input){return "";}
					if(StringUtils.isBlank(String.valueOf(input))){return "";}
					String ret ="";
					switch (String.valueOf(input)) {
					case "1":
						ret="买单";
						break;
					case "2":
						ret="卖单";
						break;
					case "3":
						ret="商城消费";
						break;
					default:
						break;
					}
					return ret;
				}});
            cellFormatterMap.put("payTime", new CellFormatter(){
            	public String formatterValue(Object input) {
            		if(null == input){return "";}
            		
            		if(input instanceof String ){
            			return (String)input;
            		}
            		if(input instanceof Date ){
            			return DateUtil.YYYY_MM_DD_HH.format(input);
            		}
            		
            		Date d = null;
            		
            		if(input instanceof Long ){
            			d = new Date((Long)(input));
            		}
            		if(input instanceof Integer ){
            			d = new Date((Integer)(input));
            		}
            		if(d == null){return "";}
            		return DateUtil.YYYY_MM_DD_HH.format(d);
            	}});
            cellFormatterMap.put("payStatus", new CellFormatter(){
            	public String formatterValue(Object input) {
            		if(null == input){return "";}
            		if(StringUtils.isBlank(String.valueOf(input))){return "";}
            		String ret="待交易";
            		switch (String.valueOf(input)) {
					case "3":
						ret="交易完成";
						break;
					default:
						break;
					}
            		return ret;
            	}});
			HSSFWorkbook wb = ExeclTools.execlExport(excelheaderList, excelData, excelHead, orderList, cellFormatterMap);
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
