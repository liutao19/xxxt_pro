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

}
