package com.dce.business.service.user;

import java.util.List;

import com.dce.business.entity.user.UserAddressDo;

public interface UserAdressService {

	// 根据主键获取用户地址信息
	UserAddressDo selectByPrimaryKey(Integer addressid);

	// 获取当前用户所有的地址
	List<UserAddressDo> selectByUserId(Integer userId);

	// 添加收货地址
	UserAddressDo selectByPrimaryKeyAdd(UserAddressDo addaddress);
	
	// 修改收货地址
	UserAddressDo selectByPrimaryKeyUpdate(UserAddressDo updateaddress);
	
}
