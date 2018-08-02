package com.dce.business.service.user;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.dce.business.common.result.Result;
import com.dce.business.entity.user.UserDo;

public interface IUserService {

    /** 
     * 根据用户名查用户
     * @param userName
     * @return  
     */
    UserDo getUser(String userName);

    /**
     * 查询用户 
     * @param userId
     * @return  
     */
    UserDo getUser(Integer userId);

    /**
     * 用户注册 
     * @param userDo
     * @return  
     */
    Result<?> reg(UserDo userDo);

    /**
     * 模糊查询用户 
     * @param userName
     * @return  
     */
    List<UserDo> list(String userName);

    /** 
     * 更新用户的总释放静态
     * @param staticAmount
     * @param userId  
     */
    void updateStatic(BigDecimal staticAmount, int userId);

    /** 
     * 更新用户以及父节点的总释放静态
     * @param staticAmount
     * @param userId  
     */
    void updateAllPerformance(BigDecimal staticAmount, Integer userId);

    /**
     * 更新量碰数量 
     * @param touchedAmount
     * @param userId  
     */
    void updateTouched(BigDecimal touchedAmount, int userId);

    List<UserDo> selectUser(Map<String, Object> params);

    Result<?> update(UserDo userDo);

    /**
     * 查看我的团队
     * @param userId
     * @return
     */
    List<Map<String, Object>> getMyMember(Map<String,Object> params);


    /**
	 * 查看组织结构树
	 * @param 用户ID
	 * @param level 查询层级
	 * @return
	 */
	List<Map<String, Object>> listMyOrg(Integer userId,int level);

	/**
	 * 查看直推树
	 * @return
	 */
	List<Map<String, Object>> listMyRef(Integer userId,int startRow, int pageSize);
	
	/** 
     * 查询分页
     * @param params
     * @return  
     */
    List<UserDo> selectPage(Map<String, Object> params);
    
    /**
     * 设置用户级别
     * @param userCode
     * @param userLevel
     * @return
     */
    Result<?> setUserLevel(String userCode,String userLevel);
    
}
