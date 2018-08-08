package com.dce.business.dao.realgoods;

import java.util.List;
import java.util.Map;

import com.dce.business.entity.Realgoods.goods;
import com.dce.business.entity.Realgoods.goodsWithBLOBs;

public interface GoodsDao {
    int deleteByPrimaryKey(Integer goodsid);

    int insert(goodsWithBLOBs record);

    int insertSelective(goodsWithBLOBs record);

    goodsWithBLOBs selectByPrimaryKey(Integer goodsid);
    
    List<goodsWithBLOBs> selectgoods(Map<String,Object> map);

    int updateByPrimaryKeySelective(goodsWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(goodsWithBLOBs record);

    int updateByPrimaryKey(goods record);
}