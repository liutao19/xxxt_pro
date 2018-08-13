package com.dce.business.service.impl.travel;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dce.business.common.result.Result;
import com.dce.business.common.util.Constants;
import com.dce.business.dao.travel.TravelDoMapper;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.travel.TravelDo;
import com.dce.business.service.travel.ITravelApplyService;

@Service("travelApplyService")
public class TravelServiceImpl implements ITravelApplyService {

	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private TravelDoMapper travelApplyDao;

	/**
	 * 新增
	 */
	@Override
	public Result<?> travelApply(TravelDo travelDo) {
		int result = travelApplyDao.insertSelective(travelDo);

		return result > 0 ? Result.successResult("申请成功!") : Result.failureResult("系统繁忙");
	}
	
	
	/**
	 * 根据ID 查询
	 * @parameter id
	 */
	@Override
	public TravelDo getById(Long id){
	  return travelApplyDao.selectByPrimaryKey(id.intValue());
	}
	
	/**
	 *根据条件查询列表
	 */
	@Override
	public List<TravelDo> selectApplyTravel(Map parameterMap){
		return travelApplyDao.selectApplyTravel(parameterMap);
	}
	
	
	
	/**
	 * 更新
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int updateApplyTravelById(TravelDo newApplyTravelDo){
		logger.debug("updateApplyTravel(TravelDo: "+newApplyTravelDo);
		return  travelApplyDao.updateByPrimaryKeySelective(newApplyTravelDo);		
	}
	
	
	
	/**
	 * 分页查询
	 * @param param
	 * @param page
	 * @return
	 */
	public PageDo<TravelDo> getApplyTravelPage(Map<String, Object> param, PageDo<TravelDo> page){
		logger.info("----getApplyTravelPage----"+param);
        param.put(Constants.MYBATIS_PAGE, page);
        List<TravelDo> list =  travelApplyDao.queryListPage(param);
        page.setModelList(list);
        return page;
	}
	
}
