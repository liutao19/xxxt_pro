package com.dce.business.service.travel;


import java.util.List;
import java.util.Map;

import com.dce.business.common.result.Result;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.travel.TravelDo;

public interface ITravelApplyService {
	
	/**
	 * 新增
	 */
	public Result<?> travelApply(TravelDo travelDo);
	
	
	/**
	 * 根据ID 查询
	 * @parameter id
	 */
	public TravelDo getById(Long id);
	
	/**
	 *根据条件查询列表
	 */
	public List<TravelDo> selectApplyTravel(Map parameterMap);
	
	/**
	 * 更新
	 */
	public int  updateApplyTravelById(TravelDo newApplyTravelDo);
	
	
	/**
	 * 分页查询
	 * @param param
	 * @param page
	 * @return
	 */
	public PageDo<TravelDo> getApplyTravelPage(Map<String, Object> param, PageDo<TravelDo> page);
	
	
}
