package com.dce.business.dao.user;

import com.dce.business.entity.user.UserAddressDo;

public interface IUserAddressDao {
    int deleteByPrimaryKey(Integer addressid);

    int insert(UserAddressDo record);

    int insertSelective(UserAddressDo record);

    UserAddressDo selectByPrimaryKey(Integer addressid);

    int updateByPrimaryKeySelective(UserAddressDo record);

    int updateByPrimaryKey(UserAddressDo record);
}