package com.dce.business.service.goods;

import com.dce.business.common.result.Result;
import com.dce.business.entity.goods.CTUserAddressDo;

public interface ICTUserAddressService {

	public Result<?> save(CTUserAddressDo address);
	
}
