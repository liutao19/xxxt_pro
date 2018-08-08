package com.dce.business.service.impl.award;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dce.business.dao.award.AwardConfigDao;
import com.dce.business.entity.award.AwardConfig;
import com.dce.business.service.award.AwardConfigService;

public class AwardConfigServiceImpl implements AwardConfigService{

	
	private final static Logger logger = LoggerFactory.getLogger(AwardConfigServiceImpl.class);

	@Resource
    private AwardConfigDao awardConfigDao;
	/*id条件删除等级
	 * (non-Javadoc)
	 * @see com.dce.business.service.award.AwardConfigService#deleteByPrimaryKey(java.lang.Long)
	 */
	@Override
	public boolean deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		logger.info("id条件删除等级记录");
		boolean flag=false;
		if(id!=null&&id!=0){
			flag=awardConfigDao.deleteByPrimaryKey(id)>0;
		}
		
		return flag;
	}

	/**
	 * 添加等级
	 */
	@Override
	public boolean insert(AwardConfig record) {
		// TODO Auto-generated method stub
		logger.info("添加等级");
		boolean flag=false;
		if(record!=null){
			flag=awardConfigDao.insert(record)>0;
		}
		return flag;
	}

	/**
	 * 条件添加等级
	 */
	@Override
	public boolean insertSelective(AwardConfig record) {
		// TODO Auto-generated method stub
		logger.info("条件添加等级");
		boolean flag=false;
		if(record!=null){
			flag=awardConfigDao.insert(record)>0;
		}
		return flag;
	}

	/**
	 * id条件查询等级
	 */
	@Override
	public AwardConfig selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		logger.info("id条件查询等级");
		AwardConfig fig=null;
		if(id!=null&&id!=0){
			fig=awardConfigDao.selectByPrimaryKey(id);
		}
		return fig;
	}

	/**
	 * id条件修改部分信息
	 */
	@Override
	public boolean updateByPrimaryKeySelective(AwardConfig record) {
		// TODO Auto-generated method stub
		logger.info("id条件修改部分信息");
		boolean flag=false;
		if(record!=null){
			flag=awardConfigDao.updateByPrimaryKeySelective(record)>0;
		}
		return flag;
	}
	/**
	 * id条件修改全部信息
	 */

	@Override
	public boolean updateByPrimaryKey(AwardConfig record) {
		// TODO Auto-generated method stub
		logger.info("id条件修改全部信息");
		boolean flag=false;
		if(record!=null){
			flag=awardConfigDao.updateByPrimaryKeySelective(record)>0;
		}
		return flag;
	}

	/**
	 * 查询全部等级信息
	 */
	@Override
	public List<AwardConfig> selectAward() {
		// TODO Auto-generated method stub
		
		return awardConfigDao.selectAward();
	}

}
