package com.dce.business.service.impl.travel;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dce.business.dao.travel.TravelPathMapper;
import com.dce.business.entity.travel.TravelPathDo;
import com.dce.business.service.travel.ITravelPathService;

@Service("travelPathService")
public class TravelPathServiceImpl implements ITravelPathService {

	@Resource
	private TravelPathMapper travelPathMapperdao;

	
	@Override
	public List<TravelPathDo> selectAll() {
		return travelPathMapperdao.selectPathAll();
	}


	@Override
	public TravelPathDo selectByPrimaryKey(Integer pathid) {
		// TODO Auto-generated method stub
		return travelPathMapperdao.selectByPrimaryKey(pathid);
	}

}
