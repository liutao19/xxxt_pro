package com.dce.business.service.travel;

import java.util.List;

import com.dce.business.entity.travel.TravelPathDo;


public interface ITravelPathService {
	
	public List<TravelPathDo> selectAll();
	
	TravelPathDo selectByPrimaryKey(Integer pathid);

}
