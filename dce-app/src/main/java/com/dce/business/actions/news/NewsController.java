package com.dce.business.actions.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.actions.common.BaseController;
import com.dce.business.common.result.Result;
import com.dce.business.entity.message.NewsDo;
import com.dce.business.service.message.INewsService;

/** 
 * 新闻公告、消息列表
 * @author parudy
 * @date 2018年4月4日 
 * @version v1.0
 */
@RestController
@RequestMapping("/news")
public class NewsController extends BaseController {

    private final static Logger logger = Logger.getLogger(NewsController.class);

    @Resource
    private INewsService newsService;
 

    /** 
     * 新闻公告列表
     * @return  
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<?> list() {

        logger.info("查询新闻公告.....");

        String pageNum = getString("pageNum"); //当前页
        String rows = getString("rows"); //每页显示条数

        if (StringUtils.isBlank(pageNum)) {
            pageNum = "1";
        }

        if (StringUtils.isBlank(rows)) {
            rows = "10";
        }

        logger.info("查询新闻公告列表:pageNum=" + pageNum + ",rows=" + rows);

        //List<NewsDo> newsList = newsService.selectNewsList(Integer.parseInt(pageNum), Integer.parseInt(rows));
        List<NewsDo> newsList = newsService.selectNewsList();
        List<Map<String, Object>> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(newsList)) {
            for (NewsDo message : newsList) {

                Map<String, Object> map = new HashMap<>();
                map.put("id", message.getId());
                map.put("title", message.getTitle());
                map.put("content", message.getContent());
                map.put("create_date", message.getCreateDate());
                //map.put("image", message.getImage());
                result.add(map);
            }
        }

        return Result.successResult("查询成功", result);
    }
   
}
