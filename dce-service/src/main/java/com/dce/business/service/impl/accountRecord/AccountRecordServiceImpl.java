package com.dce.business.service.impl.accountRecord;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dce.business.dao.etherenum.IEthAccountDetailDao;
import com.dce.business.entity.etherenum.EthAccountDetailDo;
import com.dce.business.service.accountRecord.AccountRecordService;

@Service("accountRecordService")
public class AccountRecordServiceImpl implements AccountRecordService {
	
	@Resource 
	private IEthAccountDetailDao accountDetailDao;

	//插入交易流水
	@Override
	public int insert(EthAccountDetailDo record) {
		if(record != null){
			return accountDetailDao.insert(record);
		}
		return 0;
	}

	//查询当前用户的所有交易流水
	@Override
	public List<EthAccountDetailDo> selectByUserId(Integer userId) {
		
		return accountDetailDao.selectByUserId(userId);
	}

}
