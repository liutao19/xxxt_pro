package com.dce.business.dao.user;

import java.util.List;
import java.util.Map;

import com.dce.business.entity.user.userPromoteDo;


public interface IUserPromoteDo {
   
	
	
    int deleteByPrimaryKey(Integer promoteId);

  
    int insert(userPromoteDo record);

    
    int insertSelective(userPromoteDo record);

  
    userPromoteDo selectByPrimaryKey(Integer promoteId);

  

    int updateByPrimaryKeySelective(userPromoteDo record);

   
    int updateByPrimaryKey(userPromoteDo record);
    
    /**
     * 分页多条件查询
     * @param map
     * @return
     */
    List<userPromoteDo>  queryListPage(Map<String,Object> map);
    
    
    
    /**
     * 根据用户等级和购买的数量，判断用户升级的等级
     * @param map
     * @return
     */
    userPromoteDo selectUserLevelAntBuyQty(Byte userLevel, int buyQty);
}