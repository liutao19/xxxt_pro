package com.dce.business.service.impl.dict;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dce.business.common.enums.CurrencyType;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.dao.dict.ICtCurrencyDao;
import com.dce.business.entity.dict.CtCurrencyDo;
import com.dce.business.service.dict.ICtCurrencyService;

@Service("ctCurrencyService")
public class CtCurrencyServiceImpl implements ICtCurrencyService {

	@Resource
	private ICtCurrencyDao ctCurrencyDao;
	
	@Override
	public CtCurrencyDo selectByName(String currency_name) {
		
		return ctCurrencyDao.selectByName(currency_name);
	}

	
	@Override
	public BigDecimal rmb2Dce(BigDecimal rmb) {
		
		CtCurrencyDo ct = ctCurrencyDao.selectByName(CurrencyType.DCE.name());
		if(null == ct || ct.getPrice_open() == null || BigDecimal.ZERO.equals(ct.getPrice_open()) ){
			throw new BusinessException("没有配置最新价格");
		}
		return rmb.divide(ct.getPrice_open(), 6, RoundingMode.HALF_UP);
	}

	
}
