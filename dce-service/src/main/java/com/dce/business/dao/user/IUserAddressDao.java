package com.dce.business.dao.user;

import java.util.List;

import com.dce.business.entity.user.UserAddressDo;

public interface IUserAddressDao {

	// 按主键删除收货地址
	int deleteByPrimaryKey(Integer addressid);

	// 插入收货地址
	int insert(UserAddressDo record);

	// 按主键选择更新收货地址
	int updateByPrimaryKeySelective(UserAddressDo record);

	// 按主键更新收货地址
	int updateByPrimaryKey(UserAddressDo record);
	
	// 插人收货地址
	int insertSelective(UserAddressDo record);

	// 根据主键获取用户收货地址信息
	UserAddressDo selectByPrimaryKey(Integer addressid);

	// 获取当前用户所有的收货地址
	List<UserAddressDo> selectByUserId(Integer userId);

	// 添加收货地址
	UserAddressDo selectByPrimaryKeyAdd(UserAddressDo addaddress);
	
	// 修改收货地址
	UserAddressDo selectByPrimaryKeyUpdate(UserAddressDo updateaddress);


}