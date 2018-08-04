package com.dce.business.service.impl.award;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.util.DateUtil;
import com.dce.business.dao.touch.ITouchBonusRecordDao;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.touch.TouchBonusRecordDo;
import com.dce.business.entity.user.UserStaticDo;
import com.dce.business.entity.user.UserStaticDo.StaticType;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.award.IAwardJobService;
import com.dce.business.service.award.IAwardService;
import com.dce.business.service.award.IReleaseService;
import com.dce.business.service.bonus.IPerformanceDailyService;
import com.dce.business.service.user.IUserStaticService;

@Service("awardJobService")
public class AwardJobServiceImpl implements IAwardJobService {

	private final static Logger logger = LoggerFactory.getLogger(AwardJobServiceImpl.class);

	@Resource
	private IUserStaticService userStaticService;
	@Resource
	private IPerformanceDailyService performanceDailyService;
	@Resource
	private IAccountService accountService;
	@Resource(name = "interestAwardService")
	private IAwardService interestAwardService;
	@Resource(name = "sharedAwardService")
	private IAwardService sharedAwardService;
	@Resource
	private IReleaseService releaseService;
	@Resource
    private ITouchBonusRecordDao touchBonusRecordDao;

	@Override
	public void calInterest() {
		logger.info("计算持币生息");
		int pageSize = 10000;

		//分页查询用户
		Map<String, Object> params = new HashMap<>();
		params.put("typeList", Arrays.asList(new Byte[] { StaticType.BaoDan.getType() }));
		params.put("rows", pageSize); //每次查询1000条
		int offset = 0;
		while (true) {
			params.put("offset", offset);
			List<UserStaticDo> list = userStaticService.select(params);
			if (CollectionUtils.isEmpty(list)) {
				break;
			}

			for (UserStaticDo userStaticDo : list) {
				try {
					interestAwardService.calAward(userStaticDo.getUserid(), null, null);
				} catch (Exception e) {
					logger.error("计算持币生息异常, userId:" + userStaticDo.getUserid(), e);
				}
			}
			offset += pageSize;
		}

		logger.info("计算持币生息完毕");

	}

	@Override
	public void calShared() {
		logger.info("计算分享奖");
		int pageSize = 10000;

		Date date = DateUtil.getDate(new Date(), -1); //前一天
		//分页查询用户
		Map<String, Object> params = new HashMap<>();
		params.put("date", DateUtil.dateToString(date));
		params.put("rows", pageSize); //每次查询1000条
		int offset = 0;
		while (true) {
			params.put("offset", offset);
			//List<PerformanceDailyDo> list = performanceDailyService.select(params);
			List<TouchBonusRecordDo> list = touchBonusRecordDao.selectList(params);
			if (CollectionUtils.isEmpty(list)) {
				break;
			}

			for (TouchBonusRecordDo touchBonusRecordDo : list) {
				try {
					BigDecimal left = touchBonusRecordDo.getBalanceLeft() == null ? BigDecimal.ZERO : touchBonusRecordDo.getBalanceLeft();
					BigDecimal right = touchBonusRecordDo.getBalanceRight() == null ? BigDecimal.ZERO
							: touchBonusRecordDo.getBalanceRight();
					if (left.compareTo(BigDecimal.ZERO) <= 0 && right.compareTo(BigDecimal.ZERO) <= 0) {
						logger.warn("左右区都没有业绩，不计算分享奖， userId:" + touchBonusRecordDo.getUserid());
						continue;
					}

					//小区业绩
					BigDecimal amount = left.compareTo(right) >= 0 ? right : left;

					sharedAwardService.calAward(touchBonusRecordDo.getUserid(), amount, null);
				} catch (Exception e) {
					logger.error("计算分享奖异常, userId:" + touchBonusRecordDo.getUserid(), e);
				}
			}
			offset += pageSize;
		}

		logger.info("计算分享奖完毕");
	}

	@Override
	public void calRelease(AccountType fromAccount, AccountType toAccount) {
		logger.info("计算释放， fromAccount:" + fromAccount + "; toAccount:" + toAccount);
		int pageSize = 10000;

		//分页查询用户
		Map<String, Object> params = new HashMap<>();
		params.put("accountType", fromAccount.getAccountType());
		params.put("rows", pageSize); //每次查询1000条
		int offset = 0;
		while (true) {
			params.put("offset", offset);
			List<UserAccountDo> list = accountService.selectUserAccount(params);
			if (CollectionUtils.isEmpty(list)) {
				break;
			}

			for (UserAccountDo userAccountDo : list) {
				try {
					releaseService.release(fromAccount, toAccount, userAccountDo.getUserId(), userAccountDo.getAmount());
				} catch (Exception e) {
					logger.error("计算释放异常, fromAccount:" + fromAccount + "; toAccount:" + toAccount, e);
				}
			}
			offset += pageSize;
		}

		logger.info("计算释放完毕， fromAccount:" + fromAccount + "; toAccount:" + toAccount);

	}

	@Override
	public void calRelease() {
		logger.info("计算静态释放");

		//分页查询用户
		Map<String, Object> params = new HashMap<>();
		params.put("typeList", Arrays.asList(new Byte[] { StaticType.BaoDan.getType() }));
		params.put("status", 1); //状态1--正在返
		int pageSize = 10000;
		params.put("rows", pageSize); //每次查询1000条
		int offset = 0;
		while (true) {
			params.put("offset", offset);
			List<UserStaticDo> list = userStaticService.select(params);
			if (CollectionUtils.isEmpty(list)) {
				break;
			}
			offset += pageSize;
			for (UserStaticDo userStaticDo : list) {
				try {
					releaseService.release(userStaticDo);
				} catch (Exception e) {
					logger.error("计算释放异常, userId:" + userStaticDo.getUserid(), e);
				}
			}
		}

		logger.info("静态释放计算完毕");

	}
}
