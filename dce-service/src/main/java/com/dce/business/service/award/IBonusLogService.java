package com.dce.business.service.award;

import com.dce.business.entity.bonus.BonusLogDo;

public interface IBonusLogService {

	BonusLogDo selectBonusLogById(Integer recordId);
	
	int updateRedoStatusById(Integer recordId);

}
