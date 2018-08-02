package com.dce.business.service.goods;

import java.util.List;

import com.dce.business.common.result.Result;
import com.dce.business.entity.goods.CTGoodsDo;
import com.dce.business.entity.order.OrderDo;

public interface ICTGoodsService {

	public List<CTGoodsDo> selectByPage(int pageNum,int pageCount);
	
	public CTGoodsDo selectById(Long id);
	
	/**
	 * 购买商品
	 * @param goodsId 商品id
	 * @param qyt 购买数量
	 * @return
	 */
	public Result<?> buyGoods(OrderDo order,Integer addressId);
}
