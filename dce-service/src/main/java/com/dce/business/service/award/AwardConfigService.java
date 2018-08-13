package com.dce.business.service.award;

import java.util.List;

import com.dce.business.entity.award.AwardConfig;

public interface AwardConfigService {
	

	/**
	 * 条件删除某个等级
	 * @param id
	 * @return
	 */
    boolean deleteByPrimaryKey(Long id);

    /**
     * 添加一个等级
     * @param record
     * @return
     */
    boolean insert(AwardConfig record);

    /**
     * 条件添加等级
     * @param record
     * @return
     */
    boolean insertSelective(AwardConfig record);

    /**
     * 根据id条件查询等级
     * @param id
     * @return
     */
    AwardConfig selectByPrimaryKey(Long id);
    
    /**
     * 查询全部等级信息
     */
    List<AwardConfig>   selectAward();

    /**
     * id条件修改等级部分地方
     * @param record
     * @return
     */
    boolean updateByPrimaryKeySelective(AwardConfig record);

    /**
     * id条件修改等级全部地方
     * @param record
     * @return
     */
    boolean updateByPrimaryKey(AwardConfig record);


    /**
     * 用户升级方法
     * 
     */
    
    int userUpgrade(Integer userid,int count);
    
    /**
     * 判断用户是否是否有资格升级为股东
     */
    
    int  upgradePartner(Integer userid);
}
