package com.dce.business.service.message;

import java.util.List;

import com.dce.business.entity.message.NewsDo;

public interface INewsService {

	
	public List<NewsDo> selectNewsList(int pageNum,int rows);
	
	public NewsDo selectNewsDetail(Integer newsId);
	
	public NewsDo selectLatestNews();
	
}
