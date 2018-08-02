package com.dce.business.service.impl.award;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dce.business.dao.bonus.IBonusLogDao;
import com.dce.business.entity.bonus.BonusLogDo;
import com.dce.business.service.award.IBonusLogService;

@Service("bonusLogService")
public class BonusLogServiceImpl implements IBonusLogService {
 
    @Resource 
    private IBonusLogDao bonusLogDao;

    

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public BonusLogDo selectBonusLogById(Integer recordId) {
		return bonusLogDao.selectBonusLogById(recordId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int updateRedoStatusById(Integer recordId) {
		return bonusLogDao.updateRedoStatusById(recordId);
	}
}
