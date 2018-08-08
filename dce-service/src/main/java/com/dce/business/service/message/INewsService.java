package com.dce.business.service.message;

import java.util.List;

import com.dce.business.entity.message.NewsDo;

public interface INewsService {

	
	public List<NewsDo> selectNewsList();
	
	public NewsDo selectNewsDetail(Integer newsId);
	
	public NewsDo selectLatestNews();
	
}
