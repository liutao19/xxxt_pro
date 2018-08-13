/*
 * Powered By  huangzl QQ: 272950754
 * Web Site: http://www.hehenian.com
 * Since 2008 - 2018
 */


package com.dce.manager.action.feedback;

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
import com.dce.business.entity.feedback.FeedBackDo;
import com.dce.business.entity.page.NewPagination;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.page.PageDoUtil;
import com.dce.business.service.feedback.IFeedBackService;
import com.dce.manager.action.BaseAction;



/**
 * @author  huangzl QQ: 272950754
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping("/userfeedback")
public class UserFeedbackController extends BaseAction{
	@Resource
	private IFeedBackService feedBackService;

	/**
     * 去列表页面
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model){
        return "feedback/listUserFeedback";
    }
	
	@RequestMapping("/listUserFeedback")
    public void listUserFeedback(NewPagination<FeedBackDo> pagination,
    							  ModelMap model,
    							  HttpServletResponse response) {

        logger.info("----listUserFeedback----");
        try{
            PageDo<FeedBackDo> page = PageDoUtil.getPage(pagination);
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
            page = feedBackService.getUserFeedbackPage(param, page);
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
    @RequestMapping("/addUserFeedback")
    public String addUserFeedback(String id, ModelMap modelMap, HttpServletResponse response) {
        logger.info("----addUserFeedback----");
        try{
            if(StringUtils.isNotBlank(id)){
                FeedBackDo userfeedbackDo = feedBackService.getById(Long.valueOf(id));
                if(null != userfeedbackDo){
                    modelMap.addAttribute("userfeedback", userfeedbackDo);
                }
            }
            return "feedback/addUserFeedback";
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
     ***/
    @RequestMapping("/saveUserFeedback")
    @ResponseBody
    public void saveUserFeedback(FeedBackDo userfeedbackDo, 
    							  HttpServletRequest request, 
    							  HttpServletResponse response) {
        logger.info("----saveUserFeedback------");
        try{
        	Integer id = userfeedbackDo.getFeedbackid();
            Integer userId = this.getUserId();
            
            int i = 0;
            if (id != null && id.intValue()>0) {
            	userfeedbackDo.setUserid(userId.intValue());
            	userfeedbackDo.setReplytime(new Date());
                i = feedBackService.updateUserFeedbackById(userfeedbackDo);
            } else {
                feedBackService.feedBack(userfeedbackDo);
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
        logger.info("----end saveUserFeedback--------");
    }
	
}

