/*
 * Powered By  huangzl QQ: 272950754
 * Web Site: http://www.hehenian.com
 * Since 2008 - 2018
 */


package com.dce.manager.action.goods;

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
import com.dce.business.entity.goods.CTGoodsDo;
import com.dce.business.entity.page.NewPagination;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.page.PageDoUtil;
import com.dce.business.service.goods.ICTGoodsService;
import com.dce.manager.action.BaseAction;
import com.dce.manager.util.ResponseUtils;



/**
 * @author  huangzl QQ: 272950754
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping("/goods")
public class GoodsController extends BaseAction{
	//默认多列排序,example: username desc,createTime asc
	//protected static final String DEFAULT_SORT_COLUMNS = null; 
	@Resource
	private ICTGoodsService goodsService;

	/**
     * 去列表页面
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model){
        return "goods/listGoods";
    }
	
	@RequestMapping("/listGoods")
    public void listGoods(NewPagination<CTGoodsDo> pagination,
    							  ModelMap model,
    							  HttpServletResponse response) {

        logger.info("----listGoods----");
        try{
            PageDo<CTGoodsDo> page = PageDoUtil.getPage(pagination);
            String companyName = getString("searchPolicyName");
            Map<String,Object> param = new HashMap<String,Object>();
           
            String title = getString("title");
			if (StringUtils.isNotBlank(title)) {
				param.put("title", title);
				model.addAttribute("title", title);
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
            
            page = goodsService.getGoodsPage(param, page);
            /*List<CommonComboxConstants> statusList = CommonComboxConstants.getStatusList();*/
            //model.addAttribute("statusList", statusList);
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
    @RequestMapping("/addGoods")
    public String addGoods(String id, ModelMap modelMap, HttpServletResponse response) {
        logger.info("----addGoods----");
        try{
            if(StringUtils.isNotBlank(id)){
                CTGoodsDo CTGoodsDo = goodsService.selectById(Long.valueOf(id));
                if(null != CTGoodsDo){
                    modelMap.addAttribute("goods", CTGoodsDo);
                }
            }
            return "goods/addGoods";
        }catch(Exception e){
            logger.error("跳转到数据字典编辑页面异常",e);
            throw new BusinessException("系统繁忙，请稍后再试");
        }

    }
    
    
    /**
     * 删除
     */
    @RequestMapping("/deleteGoodsById")
    public void deleteGoodsById(String id,HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("----deleteGoodsById----");
         try{
             if (StringUtils.isBlank(id) || !id.matches("\\d+")) {
            	 logger.info(id);
                 ResponseUtils.renderJson(response, null, "{\"ret\":-1}");
                 return;
             }
             int ret = goodsService.deleteGoodsService(Integer.valueOf(id));
             ResponseUtils.renderJson(response, null, "{\"ret\":" + ret + "}");
         }catch(Exception e){
             logger.error("删除商品异常",e);
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
    @RequestMapping("/saveGoods")
    @ResponseBody
    public void saveGoods(CTGoodsDo CTGoodsDo, 
    							  HttpServletRequest request, 
    							  HttpServletResponse response) {
        logger.info("----saveGoods------");
        try{
        	Integer id = CTGoodsDo.getGoodsId();
            
            int i = 0;
            if (id != null && id.intValue()>0) {
            	if(CTGoodsDo.getStatus()==1){
            		CTGoodsDo.setSaleTime(new Date());
            	}
                i = goodsService.updateGoodsById(CTGoodsDo);
            } else {
            	CTGoodsDo.setCreateTime(new Date());
                if(goodsService.insertSelectiveService(CTGoodsDo)){
                	i = 1;
                }
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
        logger.info("----end saveGoods--------");
    }
   
}