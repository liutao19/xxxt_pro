package com.dce.business.service.third;

import java.util.List;
import java.util.Map;

public interface IEthereumTransInfoService {

	List<Map<String,Object>> queryEthTrans(Map<String,Object> params);
	
	int queryEthTransCount(Map<String, Object> params);
}
