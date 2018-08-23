package com.dce.manager.action.withdraw;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.pay.util.Trans;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.DataDecrypt;
import com.dce.business.entity.message.NewsDo;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.page.PageDoUtil;
import com.dce.business.entity.page.Pagination;
import com.dce.business.entity.trade.WithdrawalsDo;
import com.dce.business.service.account.IPayService;
import com.dce.business.service.impl.account.PayServiceImpl;
import com.dce.business.service.trade.IWithdrawService;
import com.dce.manager.action.BaseAction;

@Controller
@RequestMapping("/withdraw")
public class WithDrawController extends BaseAction {

	@Resource
    private IWithdrawService withdrawService;
	@Resource
	private IPayService payService;
	
	@RequestMapping("/index")
	public String index() {
		return "/withdraw/index";
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/list")
	public void list(Pagination<Map<String, Object>> pagination,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			PageDo<Map<String, Object>> page = PageDoUtil.getPage(pagination);
			String userName = getString(request, "userName");
			String startDate = getString(request, "startDate");
			String endDate = getString(request, "endDate");

			Map<String, Object> params = new HashMap<String, Object>();

			if (StringUtils.isNotBlank(userName)) {
				params.put("userName", userName);
			}
			if (StringUtils.isNotBlank(startDate)) {
				params.put("startDate", startDate);
			}
			if (StringUtils.isNotBlank(endDate)) {
				params.put("endDate", endDate);
			}
			PageDo<Map<String, Object>> orderList = withdrawService.selectWithDrawByPage(page, params);
			Long amount = withdrawService.selectWithDrawTotallAmount(params);
			Map<String,Object> sum = new HashMap<String,Object>();
			if(StringUtils.isBlank(userName)){
				sum.put("user_name", "提现统计:");
			}else{
				sum.put("user_name", userName);
			}
			sum.put("amount", amount);
			orderList.getModelList().add(sum);
			
			pagination = PageDoUtil.getPageValue(pagination, orderList);
			outPrint(response, JSON.toJSONString(pagination));
		} catch (Exception e) {
			logger.error("显示用户数据异常", e);
			throw new BusinessException("系统繁忙，请稍后再试");
		}
	}
	 /**
     * 审批提现
     * @return
     */
    @RequestMapping(value = "/auditWithdraw", method = { RequestMethod.GET, RequestMethod.POST })
    public void auditWithdraw(HttpServletResponse response) {
        String withdrawId = getString("withdrawId");
        String auditResult = getString("auditResult");
        
        Result<?> result = Result.successResult("操作成功", withdrawId);
        try {
        	result = withdrawService.auditWithdrawById(auditResult, Integer.valueOf(withdrawId));
        } catch (BusinessException e) {
            logger.error("提现错误:", e);
            result = Result.failureResult(e.getMessage());
        } catch (Exception e) {
            logger.error("提现错误:", e);
            result = Result.failureResult("提现错误");
        }
        outPrint(response, JSON.toJSONString(result));
    }
    
    /**
     * 查看转账详情
     * @return 
     * @return
     */
    @RequestMapping(value = "/queryWithdraw")
    public String queryWithdraw(HttpServletResponse response,ModelMap modelMap) {
        String withdrawId = getString("withdrawId");
        System.out.println(withdrawId);
        try {
			if (StringUtils.isNotBlank(withdrawId)) {
				WithdrawalsDo withdraws = withdrawService.selectByPrimaryKey(Integer.parseInt(withdrawId));
				if (null != withdraws) {
					withdraws.setOrderId(DataDecrypt.decrypt(withdraws.getOrderId()));
					withdraws.setOutbizno(DataDecrypt.decrypt(withdraws.getOutbizno()));
					Trans trans=payService.withdraw(withdraws);
					System.out.println("getArrival_time_end:"+trans.getArrival_time_end());
					System.out.println("getFail_reason:"+trans.getFail_reason());
					System.out.println("getOrder_fee:"+trans.getOrder_fee());
					System.out.println("getOrder_id:"+trans.getOrder_id());
					System.out.println("getOut_biz_no:"+trans.getOut_biz_no());
					System.out.println("getPay_date:"+trans.getPay_date());
					modelMap.addAttribute("queryWithdraw", trans);
				}
			}
			return "withdraw/viewWithDraw";
		} catch (Exception e) {
			logger.error("跳转到数据字典编辑页面异常", e);
			throw new BusinessException("系统繁忙，请稍后再试");
		}
    }
}
