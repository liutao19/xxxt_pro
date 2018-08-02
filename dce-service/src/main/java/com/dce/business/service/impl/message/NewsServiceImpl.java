package com.dce.business.service.impl.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dce.business.dao.message.INewsDao;
import com.dce.business.entity.message.NewsDo;
import com.dce.business.service.message.INewsService;

@Service("newsService")
public class NewsServiceImpl implements INewsService {

	private static Logger logger = Logger.getLogger(NewsServiceImpl.class); 
	
	@Resource
	private INewsDao newsDao;
	

	@Override
	public List<NewsDo> selectNewsList(int pageNum,int rows) {
		Map<String,Object> params = new HashMap<String,Object>();
		int offset = (pageNum - 1) * rows;
		params.put("offset", offset);
		params.put("rows", rows);
		return newsDao.select(params);
	}

	@Override
	public NewsDo selectNewsDetail(Integer newsId) {
		return newsDao.selectByPrimaryKey(newsId);
	}
	

	@Override
	public NewsDo selectLatestNews() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("offset", 0);
		params.put("rows", 1);
		List<NewsDo> list = newsDao.select(params);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		return list.get(0);
	}

}
