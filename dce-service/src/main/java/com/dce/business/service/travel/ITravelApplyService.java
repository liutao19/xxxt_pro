package com.dce.business.service.travel;


import com.dce.business.common.result.Result;
import com.dce.business.entity.travel.TravelDo;

public interface ITravelApplyService {
	public Result<?> travelApply(TravelDo travelDo);
}
