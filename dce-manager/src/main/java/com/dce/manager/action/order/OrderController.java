package com.dce.manager.action.order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.DateUtil;
import com.dce.business.entity.order.Order;
import com.dce.business.entity.order.OrderDo;
import com.dce.business.entity.page.NewPagination;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.page.PageDoUtil;
import com.dce.business.service.order.IOrderService;
import com.dce.manager.action.BaseAction;

/**
 * @author 订单控制类
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping("/order")
public class OrderController extends BaseAction {
	@Resource
	private IOrderService orderService;
	
	/**
	 * 去列表页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/index")
	public String index(Model model) {
		return "order/listOrder";
	}

	/**
	 * 订单列表分页页面
	 * 
	 * @param pagination
	 * @param model
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/listOrder")
	public void listOrder(NewPagination<OrderDo> pagination, ModelMap model, HttpServletResponse response) {

		logger.info("----listOrder----");

		try {
			PageDo<Map<String, Object>> page = PageDoUtil.getPage(pagination);
			Map<String, Object> param = new HashMap<String, Object>();

			String userName = getString("userName");
			if (StringUtils.isNotBlank(userName)) {
				param.put("trueName", userName);
				model.addAttribute("userName", userName);
			}

			String startDate = getString("startDate");
			if (StringUtils.isNotBlank(startDate)) {
				param.put("startDate", startDate);
				model.addAttribute("startDate", startDate);
			}

			String endDate = getString("endDate");
			if (StringUtils.isNotBlank(endDate)) {
				param.put("endDate", endDate);
				model.addAttribute("endDate", endDate);
			}

			page = orderService.selectOrderByPage(page, param);
			pagination = PageDoUtil.getPageValue(pagination, page);

			outPrint(response, JSONObject.toJSON(pagination));
		} catch (Exception e) {
			logger.error("查询订单异常", e);
			throw new BusinessException("系统繁忙，请稍后再试");
		}
	}
	
	@RequestMapping("/export")
	public void exportOrder(HttpServletResponse response){
		Order order = new Order();
		order.setOrderstatus(0);
		
	}

	/**
	 * 发货
	 * @param orderid
	 * @param modelMap
	 * @param response
	 */
	@RequestMapping("/sendOut")
	public void sendOut(String orderid, ModelMap modelMap, HttpServletResponse response) {
		logger.info("获取的订单编号：" + orderid);
		Integer userId = this.getUserId();
		
		try {
			if (StringUtils.isNotBlank(orderid)) {
				// 先查询出订单
				Order order = orderService.selectByPrimaryKey(Integer.valueOf(orderid));
				logger.info("先查询出的订单------》》》》》"+order);
				int i = 0;
				if (order != null) {
					// 更新订单状态发货
					order.setOrderstatus(1);
					i = orderService.updateByPrimaryKeySelective(order,userId);
				}
				if (i <= 0) {
					outPrint(response, this.toJSONString(Result.failureResult("发货失败")));
					return;
				}
				outPrint(response, this.toJSONString(Result.successResult("发货成功")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("系统繁忙，请稍后再试");
		}
	}

	/**
	 * 保存更新
	 *
	 * @return
	 * @author: huangzlmf
	 * @date: 2015年4月21日 12:49:05
	 */
	@RequestMapping("/saveOrder")
	@ResponseBody
	public void saveOrder(Order order, HttpServletRequest request, HttpServletResponse response) {
		logger.info("----saveOrder------");
		try {
			Integer id = order.getOrderid();
			Integer userId = this.getUserId();

			int i = 0;
			if (id != null && id.intValue() > 0) {
				order.setUserid(userId);
				order.setCreatetime(DateUtil.dateToString(new Date()));
				i = orderService.updateOrder(order);
			} else {
				order.setUserid(userId);
				order.setCreatetime(DateUtil.dateToString(new Date()));

				i = orderService.addOrder(order);
			}

			if (i <= 0) {
				outPrint(response, this.toJSONString(Result.failureResult("操作失败")));
				return;
			}
			outPrint(response, this.toJSONString(Result.successResult("操作成功")));
		} catch (Exception e) {
			logger.error("保存更新失败", e);
			outPrint(response, this.toJSONString(Result.failureResult("操作失败")));
		}
		logger.info("----end saveOrder--------");
	}

}
