package com.dce.business.service.impl.travel;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dce.business.common.result.Result;
import com.dce.business.dao.travel.TravelDoMapper;
import com.dce.business.entity.travel.TravelDo;
import com.dce.business.service.travel.ITravelApplyService;

@Service("travelApplyService")
public class TravelServiceImpl implements ITravelApplyService {

	@Resource
	private TravelDoMapper travelApplyDao;

	@Override
	public Result<?> travelApply(TravelDo travelDo) {
		int result = travelApplyDao.insertSelective(travelDo);

		return result > 0 ? Result.successResult("申请成功!") : Result.failureResult("系统繁忙");
	}

}
