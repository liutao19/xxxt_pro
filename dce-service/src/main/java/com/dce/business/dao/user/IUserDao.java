package com.dce.business.dao.user;

import java.util.List;
import java.util.Map;

import com.dce.business.entity.user.UserDo;

public interface IUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDo record);

    int insertSelective(UserDo record);

    UserDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserDo record);

    int updateByPrimaryKeyWithBLOBs(UserDo record);

    int updateByPrimaryKey(UserDo record);

    List<UserDo> selectUser(Map<String, Object> params);

    List<UserDo> list(Map<String, Object> params);

    void updateStatic(Map<String, Object> params);

    void updateTouched(Map<String, Object> params);

    void updatePerformance(Map<String, Object> params);

    void addRefereeNumber(Integer id);
    
    void addSonNumber(Integer id);
    
    /** 
     * 查询分页
     * @param params
     * @return  
     */
    List<UserDo> selectPage(Map<String, Object> params);
}