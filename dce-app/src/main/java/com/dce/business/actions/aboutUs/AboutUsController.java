package com.dce.business.actions.aboutUs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.common.result.Result;
import com.dce.business.entity.aboutUs.AboutusDo;
import com.dce.business.entity.message.NewsDo;
import com.dce.business.service.aboutUs.IAboutusService;

@RestController
@RequestMapping("/aboutUs")
public class AboutUsController {
	private final static Logger logger = Logger.getLogger(AboutUsController.class);
	@Resource
	private IAboutusService aboutusService;
	
	 @RequestMapping("/index")
	 public Result<?> list() {
	 logger.info("查询关于湘信.....");

	 //List<NewsDo> newsList = newsService.selectNewsList(Integer.parseInt(pageNum), Integer.parseInt(rows));
	 List<AboutusDo> aboutUs = aboutusService.getAllAboutUs();
	 List<Map<String, Object>> result = new ArrayList<>();
	 if (!CollectionUtils.isEmpty(aboutUs)) {
	    AboutusDo message=aboutUs.get(0);
	    Map<String, Object> map = new HashMap<>();
	    map.put("url", message.getUrl());
	    result.add(map);
	  }
	 return Result.successResult("查询成功", result);
	 }

}