package com.dce.business.dao.user;

import java.util.List;

import com.dce.business.entity.user.UserAddressDo;

public interface IUserAddressDao {
    int deleteByPrimaryKey(Integer addressid);

    int insert(UserAddressDo record);

    int insertSelective(UserAddressDo record);

    //根据主键获取用户地址信息
    UserAddressDo selectByPrimaryKey(Integer addressid);
    
    //获取当前用户所有的地址
    List<UserAddressDo> selectByUserId(Integer userId);

    int updateByPrimaryKeySelective(UserAddressDo record);

    int updateByPrimaryKey(UserAddressDo record);
}