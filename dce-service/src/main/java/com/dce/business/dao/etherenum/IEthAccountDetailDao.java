package com.dce.business.dao.etherenum;

import java.util.List;

import com.dce.business.entity.etherenum.EthAccountDetailDo;

public interface IEthAccountDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(EthAccountDetailDo record);
    
    List<EthAccountDetailDo> selectByUserId(Integer userId);

    int insertSelective(EthAccountDetailDo record);

    EthAccountDetailDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EthAccountDetailDo record);

    int updateByPrimaryKey(EthAccountDetailDo record);
}