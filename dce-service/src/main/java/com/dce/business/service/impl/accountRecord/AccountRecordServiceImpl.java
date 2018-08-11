package com.dce.business.service.impl.accountRecord;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dce.business.common.util.AccountCodeutil;
import com.dce.business.dao.etherenum.IEthAccountDetailDao;
import com.dce.business.entity.etherenum.EthAccountDetailDo;
import com.dce.business.service.accountRecord.AccountRecordService;

@Service("accountRecordService")
public class AccountRecordServiceImpl implements AccountRecordService {
	
	@Resource
	private IEthAccountDetailDao ethAccountDetailDao;

	//插入交易流水
	public int insert(EthAccountDetailDo ethAccountDetailDo) {
		int s=0;
		System.out.println("ssssssssss222222===="+ethAccountDetailDo.getAmount());
		if(ethAccountDetailDo != null){
			System.out.println("liu======"+ethAccountDetailDo.getAmount());
			// 账户类型0-个人账户；1-平台账户
			ethAccountDetailDo.setEthAccountType(0); 
			//设置交易流水号
			ethAccountDetailDo.setSerialNo(AccountCodeutil.getAccountCode()); 
			System.out.println("getCode"+ethAccountDetailDo.getSerialNo());
			s=ethAccountDetailDao.insert(ethAccountDetailDo);
			System.err.println("插入结果-------》》》》》》"+s);
		}
		return s;
	}

	//查询当前用户的所有交易流水
	@Override
	public List<EthAccountDetailDo> selectByUserId(Integer userId) {
		
		return ethAccountDetailDao.selectByUserId(userId);
	}

}
