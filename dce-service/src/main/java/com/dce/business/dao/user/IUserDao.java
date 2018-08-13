package com.dce.business.dao.user;

import java.util.List;
import java.util.Map;

import com.dce.business.entity.user.UserDo;

public interface IUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDo record);

    int insertSelective(UserDo record);

    UserDo selectByPrimaryKey(Integer id);

    /**
     * 用户个人信息修改
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UserDo record);
    
    /**
     * 登录密码
     * @param record
     * @return
     */
    int updateByPrimaryKeyPayPass(UserDo record);
    /**
     * 支付密码
     * @param record
     * @return
     */
    int updateByPrimaryKeyLogPass(UserDo record);

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
    List<UserDo> selectFenYe(Map<String, Object> params);
    
    /**
     * 分页查询  加入拦截器后的实现方式
     * @param params
     * @return
     */
    List<UserDo> selectByPage(Map<String, Object> params);
    
    /**
     * 
     * @param params
     * @return
     */
    List<UserDo> selectEthAccountByPage(Map<String, Object> params);
    
    /**
     * 统计报单金额
     * @param params
     * @return
     */
    Long selectBaoDanAmount(Map<String,Object> params);
}