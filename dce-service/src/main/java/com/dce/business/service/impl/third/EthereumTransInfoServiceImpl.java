package com.dce.business.service.impl.third;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dce.business.dao.etherenum.IEthereumTransInfoDao;
import com.dce.business.service.third.IEthereumTransInfoService;

@Service("ethereumTransInfoService")
public class EthereumTransInfoServiceImpl implements IEthereumTransInfoService {

	@Resource
	private IEthereumTransInfoDao  ethTransDao;
	
	/**
	 * 查询以太坊流水
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String,Object>> queryEthTrans(Map<String,Object> params){
		return ethTransDao.queryEthTrans(params);
	}

	@Override
	public int queryEthTransCount(Map<String, Object> params) {
		return ethTransDao.queryEthTransCount(params);
	}
}
