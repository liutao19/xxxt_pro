package com.dce.business.service.impl.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dce.business.dao.message.INewsDao;
import com.dce.business.entity.message.NewsDo;
import com.dce.business.service.message.INewsService;

@Service("newsService")
public class NewsServiceImpl implements INewsService {
	@Resource
	private INewsDao newsDao;
	

	@Override
	public List<NewsDo> selectNewsList() {
	
		return newsDao.select();
	}

	@Override
	public NewsDo selectNewsDetail(Integer newsId) {
		return newsDao.selectByPrimaryKey(newsId);
	}

	@Override
	public NewsDo selectLatestNews() {
		// TODO Auto-generated method stub
		return null;
	}
	

/*	@Override
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
*/
}
