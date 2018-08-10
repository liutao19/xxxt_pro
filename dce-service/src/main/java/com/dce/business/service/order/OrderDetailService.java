package com.dce.business.service.order;

import com.dce.business.entity.goods.CTGoodsDo;

/**
 * 订单明细
 * @author Administrator
 *
 */
public interface OrderDetailService {

	//根据商品id查询出商品的价格
	CTGoodsDo selectByGoodsId(Integer goodsId);
}
