package com.dce.business.service.impl.award;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dce.business.dao.touch.ITouchBonusRecordDao;
import com.dce.business.dao.user.IUserParentDao;
import com.dce.business.entity.touch.TouchBonusRecordDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.entity.user.UserParentDo;
import com.dce.business.service.award.IAwardService;
import com.dce.business.service.bonus.IPerformanceDailyService;
import com.dce.business.service.user.IUserService;

/**
 * 计算用户的报单，方便以后统计业绩
 */
@Service("liangPengAwardService")
public class LiangPengAwardServiceImpl implements IAwardService {
	private Logger logger = Logger.getLogger(LiangPengAwardServiceImpl.class);

	@Resource
	private IUserService userService;
	@Resource
	private IUserParentDao userParentDao;
	@Resource
	private ITouchBonusRecordDao touchBonusRecordDao;
	@Resource
	private IPerformanceDailyService performanceDailyService;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void calAward(Integer userId, BigDecimal amount, Integer userLevel) {
		logger.info("统计用户每日业绩");
		//查询所有父节点，排序
		Map<String, Object> params = new HashMap<>();
		params.put("userid", userId);
		List<UserParentDo> list = userParentDao.select(params);
		Collections.sort(list, new Comparator<UserParentDo>() {
			@Override
			public int compare(UserParentDo o1, UserParentDo o2) {
				return o1.getDistance().compareTo(o2.getDistance());
			}
		});

		for (UserParentDo userParentDo : list) {
			doCalTouchAward(userParentDo.getParentid(), userParentDo.getLrDistrict(), amount, userId);
		}
	}

	private void doCalTouchAward(Integer parentId, Byte lrDistrict, BigDecimal amount, Integer relevantUserid) {
		UserDo userDo = userService.getUser(parentId);

		//用户未激活，不统计业绩
		if (!userDo.isActivated()) {
			return;
		}

		BigDecimal left = BigDecimal.ZERO;
		BigDecimal right = BigDecimal.ZERO;
		BigDecimal dailyLeft = BigDecimal.ZERO;
		BigDecimal dailyRight = BigDecimal.ZERO;

		//查询用户最后一次量碰记录
		TouchBonusRecordDo touchBonusRecordDo = touchBonusRecordDao.getUserTouchBonusRecord(parentId);
		if (touchBonusRecordDo != null) {
			left = touchBonusRecordDo.getBalanceLeft();
			right = touchBonusRecordDo.getBalanceRight();
		}

		//未量碰前先添加本次报单金额
		if (lrDistrict == 1) { //左区
			left = left.add(amount);
			dailyLeft = amount;
		} else {
			right = right.add(amount);
			dailyRight = amount;
		}

		//记录业绩
		TouchBonusRecordDo newTouchBonusRecordDo = new TouchBonusRecordDo();
		newTouchBonusRecordDo.setUserid(parentId);
		newTouchBonusRecordDo.setRelevantUserid(relevantUserid);
		newTouchBonusRecordDo.setBalanceLeft(left);
		newTouchBonusRecordDo.setBalanceRight(right);
		touchBonusRecordDao.insertSelective(newTouchBonusRecordDo);

		//记录当日业绩
		performanceDailyService.updateBalance(parentId, new Date(), amount, dailyLeft, dailyRight);
	}

}
