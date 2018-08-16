package com.dce.business.service.impl.user;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dce.business.dao.user.IUserAddressDao;
import com.dce.business.entity.user.UserAddressDo;
import com.dce.business.service.user.UserAdressService;

@Service("userAdressService")
public class UserAdressServiceImpl implements UserAdressService {

	@Resource
	private IUserAddressDao userAdressDao;

	/**
	 * 根据id获取地址
	 */
	@Override
	public UserAddressDo selectByPrimaryKey(Integer addressid) {

		return userAdressDao.selectByPrimaryKey(addressid);
	}

	/**
	 * 获取当前用户的所有地址（查询）
	 */
	@Override
	public List<UserAddressDo> selectByUserId(Integer userId) {

		return userAdressDao.selectByUserId(userId);
	}

	/**
	 * 添加收货地址
	 */
	@Override
	public UserAddressDo selectByPrimaryKeyAdd(UserAddressDo addaddress) {

		return userAdressDao.selectByPrimaryKeyAdd(addaddress);
	}

	/**
	 * 修改收货地址
	 */
	@Override
	public UserAddressDo selectByPrimaryKeyUpdate(UserAddressDo updateaddress) {

		return userAdressDao.selectByPrimaryKeyUpdate(updateaddress);
	}

	/**
	 * 修改收货地址
	 */
	@Override
	public int updateByPrimaryKeySelective(UserAddressDo record) {

		return userAdressDao.updateByPrimaryKeySelective(record);
	}

	/**
	 * 添加收货地址
	 */
	@Override
	public int insertSelective(UserAddressDo record) {

		return userAdressDao.insertSelective(record);
	}

	/**
	 * 删除收货地址
	 * <== 费弃 ==>
	 */
	@Override
	public boolean deleteByPrimaryKeyBoo(Integer addressid) {
		boolean flag = false;
		if(addressid !=null && addressid != 0){
			flag = userAdressDao.deleteByPrimaryKey(addressid) > 0;
		}
		return false;
	}

	/**
	 * 按主键删除收货地址
	 */
	@Override
	public int deleteByPrimaryKeyInt(Integer addressid) {
		
		return userAdressDao.deleteByPrimaryKey(addressid);
	}

}
