/*
 * Powered By  huangzl QQ: 272950754
 * Web Site: http://www.hehenian.com
 * Since 2008 - 2018
 */


package com.dce.manager.action.travelPath;

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
import com.dce.business.entity.page.NewPagination;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.page.PageDoUtil;
import com.dce.business.entity.travel.TravelPathDo;
import com.dce.business.service.travel.ITravelPathService;
import com.dce.manager.action.BaseAction;
import com.dce.manager.util.ResponseUtils;



/**
 * @author  huangzl QQ: 272950754
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping("/path")
public class PathController extends BaseAction{
	//默认多列排序,example: username desc,createTime asc
	//protected static final String DEFAULT_SORT_COLUMNS = null; 
	@Resource
	private ITravelPathService travelPathService;
	/**
     * 去列表页面
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model){
        return "path/listPath";
    }
	
	@RequestMapping("/listPath")
    public void listPath(NewPagination<TravelPathDo> pagination,
    							  ModelMap model,
    							  HttpServletResponse response) {

        logger.info("----listPath----");
        try{
            PageDo<TravelPathDo> page = PageDoUtil.getPage(pagination);
            Map<String,Object> param = new HashMap<String,Object>();
        
            String linename = getString("linename");
            System.out.println(linename);
			if (StringUtils.isNotBlank(linename)) {
				param.put("linename", linename);
				model.addAttribute("linename", linename);
			}
            
            page = travelPathService.getTravelPathPage(param, page);
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
    @RequestMapping("/addPath")
    public String addPath(String id, ModelMap modelMap, HttpServletResponse response) {
        logger.info("----addPath----");
        try{
            if(StringUtils.isNotBlank(id)){
                TravelPathDo TravelPathDo = travelPathService.selectByPrimaryKey(Integer.valueOf(id));
                if(null != TravelPathDo){
                    modelMap.addAttribute("path", TravelPathDo);
                }
            }
            return "path/addPath";
        }catch(Exception e){
            logger.error("跳转到数据字典编辑页面异常",e);
            throw new BusinessException("系统繁忙，请稍后再试");
        }

    }

    /**
     * 删除
     */
    @RequestMapping("/deletePath")
    public void deleteYsNotice(String id,HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("----deletePath----");
         try{
             if (StringUtils.isBlank(id) || !id.matches("\\d+")) {
            	 logger.info(id);
                 ResponseUtils.renderJson(response, null, "{\"ret\":-1}");
                 return;
             }
             int ret = travelPathService.deletePathById(Integer.valueOf(id));
             ResponseUtils.renderJson(response, null, "{\"ret\":" + ret + "}");
         }catch(Exception e){
             logger.error("删除公告异常",e);
             ResponseUtils.renderJson(response, null, "{\"ret\":-1}");
         }
     }
    
    
    /**
     * 保存更新
     *
     * @return
     * @author: huangzlmf
     * @date: 2015年4月21日 12:49:05
     */
    @RequestMapping("/savePath")
    @ResponseBody
    public void savePath(TravelPathDo TravelPathDo, 
    							  HttpServletRequest request, 
    							  HttpServletResponse response) {
        logger.info("----savePath------");
        try{
        	Integer id = TravelPathDo.getPathid();
        	
            int i = 0;
            if (id != null && id.intValue()>0) {
                i = travelPathService.updatePathById(TravelPathDo);
            } else {
                i = travelPathService.addPath(TravelPathDo);
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
        logger.info("----end savePath--------");
    }
    
	
	
}

