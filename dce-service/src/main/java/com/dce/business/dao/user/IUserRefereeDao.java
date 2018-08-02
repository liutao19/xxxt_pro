package com.dce.business.dao.user;

import java.util.List;
import java.util.Map;

import com.dce.business.entity.user.UserRefereeDo;

public interface IUserRefereeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRefereeDo record);

    int insertSelective(UserRefereeDo record);

    UserRefereeDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRefereeDo record);

    int updateByPrimaryKey(UserRefereeDo record);
    
    List<UserRefereeDo> select(Map<String, Object> params);

    /**
	 * 查看直推树
	 * @return
	 */
	List<Map<String, Object>> listMyRef(Map<String, Object> params);
	
	
	/**
     * 查询团队成员
     */
    List<Map<String,Object>> selectMyGroup(Map<String,Object> params);
	
}