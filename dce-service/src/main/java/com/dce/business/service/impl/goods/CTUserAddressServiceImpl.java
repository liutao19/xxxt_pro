package com.dce.business.service.impl.goods;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dce.business.common.result.Result;
import com.dce.business.common.util.DateUtil;
import com.dce.business.dao.goods.ICTUserAddressDao;
import com.dce.business.dao.user.IUserDao;
import com.dce.business.entity.goods.CTUserAddressDo;
import com.dce.business.service.goods.ICTUserAddressService;

@Service("ctUserAddressService")
public class CTUserAddressServiceImpl implements ICTUserAddressService {

	@Resource
	private ICTUserAddressDao ctUserAddressDao;
	@Resource
    private IUserDao userDao;
	
	@Override
	public Result<?> save(CTUserAddressDo address) {
		if(address == null){
			return Result.failureResult("保存的收货地址为空!");
		}
		
		int flag = 0;
		if(address.getAddressId() != null){
			flag = ctUserAddressDao.update(address);
		}else{
			flag = add(address);
		}
		
		if(flag > 0){
			return Result.successResult("收货地址保存成功!",address.getAddressId());
		}else{
			return Result.failureResult("收货地址保存失败!");
		}
	}
	
	private int add(CTUserAddressDo address){
		address.setAddressFlag(1);
		address.setCreateTime(DateUtil.dateToString(new Date()));
		ctUserAddressDao.insert(address);
		if(address.getAddressId() != null){
			return address.getAddressId();
		}
		return 0;
	}

}
