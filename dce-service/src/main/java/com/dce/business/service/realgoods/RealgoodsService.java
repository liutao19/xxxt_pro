package com.dce.business.service.realgoods;

import java.util.List;
import java.util.Map;

import com.dce.business.entity.Realgoods.goods;
import com.dce.business.entity.Realgoods.goodsWithBLOBs;

public interface RealgoodsService {
	
	/**
	 * 删除商品
	 * @param goodsid
	 * @return
	 */
	boolean deleteByPrimaryKey(Integer goodsid);

	/**
	 * 添加商品
	 * @param record
	 * @return
	 */
	boolean insert(goodsWithBLOBs record);

    /**
     * 选择性添加商品
     * @param record
     * @return
     */
	boolean insertSelective(goodsWithBLOBs record);

    /**
     * 条件查询商品
     * @param goodsid
     * @return
     */
    goodsWithBLOBs selectByPrimaryKey(Integer goodsid);
    
    
    /**
     *分页查询商品
     * @param map
     * @return
     */
    List<goodsWithBLOBs> selectgoods(Map<String,Object> map);


    /**
     * 选择性修改商品
     * @param record
     * @return
     */
    boolean updateByPrimaryKeySelective(goodsWithBLOBs record);

    /**
     * 修改商品
     * @param record
     * @return
     */
    boolean updateByPrimaryKeyWithBLOBs(goodsWithBLOBs record);

    /**
     * 修改商品
     * @param record
     * @return
     */
    boolean updateByPrimaryKey(goods record);

}
