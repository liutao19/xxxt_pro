package com.dce.business.service.impl.realgoods;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dce.business.dao.realgoods.GoodsDao;
import com.dce.business.entity.Realgoods.goods;
import com.dce.business.entity.Realgoods.goodsWithBLOBs;
import com.dce.business.service.realgoods.RealgoodsService;

public class RealGoodsServiceImpl implements RealgoodsService{

	
	@Resource
	private GoodsDao  goodsdao;
	
	/***
	 * 删除商品
	 */
	@Override
	public boolean deleteByPrimaryKey(Integer goodsid) {
		// TODO Auto-generated method stub
		boolean flag=false;
		if(goodsid!=null&&goodsid!=0){
			if(goodsdao.deleteByPrimaryKey(goodsid)>0){
				flag=true;
			}
		}
		return flag;
	}

	/**
	 * 添加商品
	 */
	@Override
	public boolean insert(goodsWithBLOBs record) {
		// TODO Auto-generated method stub
		boolean flag=false;
		if(record!=null){
			if(goodsdao.insert(record)>0){
				flag=true;
			}
		}
		return flag;
	}

	/**
	 * 选择性添加商品
	 */
	@Override
	public boolean insertSelective(goodsWithBLOBs record) {
		// TODO Auto-generated method stub
		boolean flag=false;
		if(record!=null){
			if(goodsdao.insertSelective(record)>0){
				flag=true;
			}
		}
		return flag;
	}

	/**
	 * 条件查询商品
	 */
	@Override
	public goodsWithBLOBs selectByPrimaryKey(Integer goodsid) {
		// TODO Auto-generated method stub
		goodsWithBLOBs 	goods=null;
		if(goodsid!=null&&goodsid!=0){
			goods=goodsdao.selectByPrimaryKey(goodsid);
		}
		return goods;
	}

	/**
	 * 选择性修改商品
	 */
	@Override
	public boolean updateByPrimaryKeySelective(goodsWithBLOBs record) {
		// TODO Auto-generated method stub
		boolean flag=false;
		if(record!=null){
			if(goodsdao.updateByPrimaryKeySelective(record)>0){
				flag=true;
			}
		}
		return flag;
	}

	/**
	 * 修改商品
	 */
	@Override
	public boolean updateByPrimaryKeyWithBLOBs(goodsWithBLOBs record) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 修改商品
	 */
	@Override
	public boolean updateByPrimaryKey(goods record) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 分页查询商品
	 */
	@Override
	public List<goodsWithBLOBs> selectgoods(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<goodsWithBLOBs> list=null;
		if(map !=null){
			list=goodsdao.selectgoods(map);
		}
		return list;
	}

}
