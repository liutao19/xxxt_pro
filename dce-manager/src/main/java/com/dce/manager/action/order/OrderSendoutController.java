package com.dce.manager.action.order;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.ExeclTools;
import com.dce.business.entity.page.NewPagination;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.page.PageDoUtil;
import com.dce.business.service.order.IOrderSendoutService;
import com.dce.manager.action.BaseAction;


@Controller
@RequestMapping("/ordersendout")
public class OrderSendoutController extends BaseAction{
	@Resource
	private IOrderSendoutService orderSendoutService;

	/**
     * 去列表页面
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model){
        return "ordersendout/listOrderSendout";
    }
	
	@RequestMapping("/listOrderSendout")
    public void listOrderSendout(NewPagination<OrderSendoutDo> pagination,
    							  ModelMap model,
    							  HttpServletResponse response) {

        logger.info("----listOrderSendout----");
        try{
            PageDo<OrderSendoutDo> page = PageDoUtil.getPage(pagination);
            String companyName = getString("searchPolicyName");
            Map<String,Object> param = new HashMap<String,Object>();
            if(StringUtils.isNotBlank(companyName)){
                param.put("policyName",companyName);
                model.addAttribute("searchPolicyName",companyName);
            }
            String managerName = getString("searManagerName");
            if(StringUtils.isNotBlank(managerName)){
                param.put("managerName", managerName);
                model.addAttribute("searManagerName",managerName);
            }
            page = ordersendoutService.getOrderSendoutPage(param, page);
            List<CommonComboxConstants> statusList = CommonComboxConstants.getStatusList();
            model.addAttribute("statusList", statusList);
            pagination = PageDoUtil.getPageValue(pagination, page);
            outPrint(response, JSONObject.toJSON(pagination));
        }catch(Exception e){
            logger.error("查询清单异常",e);
            throw new BusinessException("系统繁忙，请稍后再试");
        }
    }
	
	
	  
    /**
     * 编辑页面
     *
     * @return
     */
    @RequestMapping("/addOrderSendout")
    public String addOrderSendout(String id, ModelMap modelMap, HttpServletResponse response) {
        logger.info("----addOrderSendout----");
        try{
            if(StringUtils.isNotBlank(id)){
                OrderSendoutDo ordersendoutDo = orderSendoutService.getById(Long.valueOf(id));
                if(null != ordersendoutDo){
                    modelMap.addAttribute("ordersendout", ordersendoutDo);
                }
            }
            return "ordersendout/addOrderSendout";
        }catch(Exception e){
            logger.error("跳转到数据字典编辑页面异常",e);
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
    @RequestMapping("/saveOrderSendout")
    @ResponseBody
    public void saveOrderSendout(OrderSendoutDo ordersendoutDo, 
    							  HttpServletRequest request, 
    							  HttpServletResponse response) {
        logger.info("----saveOrderSendout------");
        try{
            Long id = ordersendoutDo.getId();
            Long userId = new Long(this.getUserId());
            
            int i = 0;
            if (id != null && id.intValue()>0) {
                i = orderSendoutService.updateOrderSendoutById(ordersendoutDo);
            } else {
				ordersendoutDo.setCreateBy(userId);
            	ordersendoutDo.setCreateTime(new Date());
            	
                i = orderSendoutService.addOrderSendout(ordersendoutDo);
            }

            if (i <= 0) {
                outPrint(response,this.toJSONString(Result.failureResult("操作失败")));
                return;
            }
            outPrint(response, this.toJSONString(Result.successResult("操作成功")));
        }catch(Exception e){
            logger.error("保存更新失败",e);
            outPrint(response, this.toJSONString(Result.failureResult("操作失败")));
        }
        logger.info("----end saveOrderSendout--------");
    }
    
	/**
     * 导出数据
     */
    @RequestMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        try {
            Long time = System.currentTimeMillis();
            OrderSendoutExample example  = new OrderSendoutExample();
            String companyName = getString("searchPolicyName");
          
            if(StringUtils.isNotBlank(companyName)){
                example.createCriteria().andPolicyNameLike(companyName);
            }
            String managerName = getString("searManagerName");
            if(StringUtils.isNotBlank(managerName)){
            	example.createCriteria().andManagerNameEqualTo(managerName);
            }
            List<OrderSendoutDo> ordersendoutLst = ordersendoutService.selectOrderSendout(example);
            
            String excelHead = "数据导出";
            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String fileName = URLEncoder.encode(excelHead + date + ".xls", "utf-8");
            List<String[]> excelheaderList = new ArrayList<String[]>();
            String[] excelheader = { "保险公司名称", "保险公司简称", "联系人姓名", "联系人手机号码", "跟进单员", "合作状态", "记录状态" };
            excelheaderList.add(0, excelheader);
            String[] excelData = { "policyName", "shortName", "contactName", "contactPhone", "managerName", "partnerStatus", "status" };
            HSSFWorkbook wb = ExeclTools.execlExport(excelheaderList, excelData, excelHead, ordersendoutLst);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            wb.write(response.getOutputStream());
            time = System.currentTimeMillis() - time;
            logger.info("导出数据，导出耗时(ms)：" + time);
        } catch (Exception e) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("下载失败");
            logger.error("导出数据，Excel下载失败", e);
            logger.error("导出数据异常", e);
            throw new BusinessException("系统繁忙，请稍后再试");
        } finally {
            response.flushBuffer();
        }

    }
	
}

