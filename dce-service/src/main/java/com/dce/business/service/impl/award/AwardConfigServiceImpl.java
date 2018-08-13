package com.dce.business.service.impl.award;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.dce.business.dao.award.AwardConfigDao;
import com.dce.business.entity.award.AwardConfig;
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.award.AwardConfigService;
import com.dce.business.service.user.IUserService;

@Service("awardConfigService")
public class AwardConfigServiceImpl implements AwardConfigService{

	
	private final static Logger logger = LoggerFactory.getLogger(AwardConfigServiceImpl.class);
	private IUserService  userService;

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

	/**
	 * 用户升级方法
	 */
	@Override
	public int userUpgrade(Integer userid, int count) {
		// 获取购买者的信息
		UserDo	user=userService.getUser(userid);
		//获取用户等级
		int userLevel=user.getUserLevel();
		switch(userLevel){
		case 0:
			if(count>=1&&5>count){
				//普通用户升级为会员
			}else if(count>=5&&50>count){
				//普通用户升级为vip
				
			}else if(count>=50){
				//普通用户升级为城市合伙人，升级为城市合伙人，要添加一条记录，并且判断用户推荐人是否有资格升级为股东
			}
			break;
		case 1:
			if(count>=5&&50>count){
				//会员用户升级为vip
				
			}else if(count>=50){
				//会员用户升级为城市合伙人，升级为城市合伙人，要添加一条记录，并且判断用户推荐人是否有资格升级为股东
			}
		case 2:
			if(count>=50){
				//vip用户升级为城市合伙人，升级为城市合伙人，要添加一条记录，并且判断用户推荐人是否有资格升级为股东
			}
		
		}
		
		
		
		
		return 0;
	}

	/**
	 * 用户升级为股东方法
	 */
	@Override
	public int upgradePartner(Integer userid) {
		// TODO Auto-generated method stub
		return 0;
	}

}
