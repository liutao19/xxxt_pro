package com.dce.manager.action.applyTravel;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

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
import com.dce.business.entity.page.NewPagination;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.page.PageDoUtil;
import com.dce.business.entity.travel.TravelDo;
import com.dce.business.service.travel.ITravelApplyService;
import com.dce.manager.action.BaseAction;
import com.dce.manager.util.ResponseUtils;


@Controller
@RequestMapping("/applytravel")
public class ApplyTravelController extends BaseAction{
	@Resource
	private ITravelApplyService travelApplyService;

	/**
     * 去列表页面
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model){
        return "applytravel/listApplyTravel";
    }
	
	@RequestMapping("/listApplyTravel")
    public void listApplyTravel(NewPagination<TravelDo> pagination,
    							  ModelMap model,
    							  HttpServletResponse response) {

		logger.info("----listApplyTravel----");
        try{
            PageDo<TravelDo> page = PageDoUtil.getPage(pagination);
            String companyName = getString("searchPolicyName");
            Map<String,Object> param = new HashMap<String,Object>();
            
        	String userName = getString("userName");
        	System.out.println(userName);
			if (StringUtils.isNotBlank(userName)) {
				param.put("userName", userName);
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
            
            page = travelApplyService.getTravelapplyTravelPage(param, page);
            logger.info(page.getModelList().toString());
            pagination = PageDoUtil.getPageValue(pagination, page);
            outPrint(response, JSONObject.toJSON(pagination));
        }catch(Exception e){
            logger.error("查询清单异常",e);
            throw new BusinessException("系统繁忙，请稍后再试");
        }
    }
	
    /**
     * 同意申请
     */
    @RequestMapping("/agreeApply")
    public void agreeApply(String id,HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("----agreeApply----");
         try{
             if (StringUtils.isBlank(id) || !id.matches("\\d+")) {
                 ResponseUtils.renderJson(response, null, "{\"ret\":-1}");
                 return;
             }
             int ret = travelApplyService.updateapplyStateById(Integer.valueOf(id));
             ResponseUtils.renderJson(response, null, "{\"ret\":" + ret + "}");
         }catch(Exception e){
             logger.error("审批申请异常",e);
             ResponseUtils.renderJson(response, null, "{\"ret\":-1}");
         }
     }
	  
    /**
     * 编辑页面
     *
     * @return
     */
    @RequestMapping("/addApplyTravel")
    public String addApplyTravel(String id, ModelMap modelMap, HttpServletResponse response) {
        logger.info("----addApplyTravel----");
        try{
            if(StringUtils.isNotBlank(id)){
                TravelDo TravelDo = travelApplyService.selectByPrimaryKey(Integer.valueOf(id));
                if(null != TravelDo){
                    modelMap.addAttribute("applytravel", TravelDo);
                }
            }
            return "applytravel/addApplyTravel";
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
    @RequestMapping("/saveApplyTravel")
    @ResponseBody
    public void saveApplyTravel(TravelDo TravelDo, 
    							  HttpServletRequest request, 
    							  HttpServletResponse response) {
        logger.info("----saveApplyTravel------");
        try{
        	Integer id = TravelDo.getId();
            
            int i = 0;
            if (id != null && id.intValue()>0) {
                i = travelApplyService.updateapplyTravelById(TravelDo);
            } else {
            	TravelDo.setCreatetime(new Date());
                i = travelApplyService.addapplyTravel(TravelDo);
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
        logger.info("----end saveApplyTravel--------");
    }
    
}

