package com.dce.business.service.impl.award;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dce.business.common.enums.IncomeType;
import com.dce.business.dao.account.IUserAccountDao;
import com.dce.business.dao.account.IUserAccountDetailDao;
import com.dce.business.dao.award.AwardConfigDao;
import com.dce.business.dao.award.AwardlistMapper;
import com.dce.business.dao.district.districtMapper;
import com.dce.business.dao.district.regionalawardsMapper;
import com.dce.business.dao.user.IUserDao;
import com.dce.business.entity.account.UserAccountDetailDo;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.award.AwardConfig;
import com.dce.business.entity.award.Awardlist;
import com.dce.business.entity.district.District;
import com.dce.business.entity.district.Regionalawards;
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.award.AwardConfigService;

@Service("awardConfigService")
public class AwardConfigServiceImpl implements AwardConfigService {

	private final static Logger logger = LoggerFactory.getLogger(AwardConfigServiceImpl.class);
	@Resource
	private IUserDao userDao;

	@Resource
	private AwardlistMapper awardlistDao;

	@Resource
	private AwardConfigDao awardConfigDao;

	@Resource
	private IUserAccountDao userAccoutDao;

	@Resource
	private IUserAccountDetailDao userAccountDetailDao;

	@Resource
	private districtMapper districtDao;

	@Resource
	private regionalawardsMapper regionlDao;

	/*
	 * id条件删除等级 (non-Javadoc)
	 * 
	 * @see
	 * com.dce.business.service.award.AwardConfigService#deleteByPrimaryKey(java
	 * .lang.Long)
	 */
	@Override
	public boolean deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		logger.info("id条件删除等级记录");
		boolean flag = false;
		if (id != null && id != 0) {
			flag = awardConfigDao.deleteByPrimaryKey(id) > 0;
		}

		return flag;
	}

	/**
	 * 添加等级
	 */
	@Override
	public boolean insert(AwardConfig record) {
		// TODO Auto-generated method stub
		logger.info("添加等级");
		boolean flag = false;
		if (record != null) {
			flag = awardConfigDao.insert(record) > 0;
		}
		return flag;
	}

	/**
	 * 条件添加等级
	 */
	@Override
	public boolean insertSelective(AwardConfig record) {
		// TODO Auto-generated method stub
		logger.info("条件添加等级");
		boolean flag = false;
		if (record != null) {
			flag = awardConfigDao.insert(record) > 0;
		}
		return flag;
	}

	/**
	 * id条件查询等级
	 */
	@Override
	public AwardConfig selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		logger.info("id条件查询等级");
		AwardConfig fig = null;
		if (id != null && id != 0) {
			fig = awardConfigDao.selectByPrimaryKey(id);
		}
		return fig;
	}

	/**
	 * id条件修改部分信息
	 */
	@Override
	public boolean updateByPrimaryKeySelective(AwardConfig record) {
		// TODO Auto-generated method stub
		logger.info("id条件修改部分信息");
		boolean flag = false;
		if (record != null) {
			flag = awardConfigDao.updateByPrimaryKeySelective(record) > 0;
		}
		return flag;
	}

	/**
	 * id条件修改全部信息
	 */

	@Override
	public boolean updateByPrimaryKey(AwardConfig record) {
		// TODO Auto-generated method stub
		logger.info("id条件修改全部信息");
		boolean flag = false;
		if (record != null) {
			flag = awardConfigDao.updateByPrimaryKeySelective(record) > 0;
		}
		return flag;
	}

	/**
	 * 查询全部等级信息
	 */
	@Override
	public List<AwardConfig> selectAward() {
		// TODO Auto-generated method stub

		return awardConfigDao.selectAward();
	}

	/**
	 * 用户购买商品升级方法 userid 购买者id count 数量
	 */
	@Override
	public int userUpgrade(Integer userid, int count) {
		// 获取购买者的信息
		UserDo user = userDao.selectByPrimaryKey(userid);
		// 获取用户等级
		int userLevel = user.getUserLevel();
		UserDo updatauser = new UserDo();
		// 返回升级级别
		Integer level = null;
		switch (userLevel) {
		case 0:
			if (count >= 1 && 5 > count) {
				// 普通用户升级为会员
				updatauser.setUserLevel((byte) 1);
				if (userDao.updateByPrimaryKeySelective(user) > 0) {
					level = 1;
				}

			} else if (count >= 5 && 50 > count) {
				// 普通用户升级为vip
				updatauser.setUserLevel((byte) 2);
				if (userDao.updateByPrimaryKeySelective(user) > 0) {
					level = 2;
				}

			} else if (count >= 50) {
				// 普通用户升级为城市合伙人，升级为城市合伙人，要添加一条记录，并且判断用户推荐人是否有资格升级为股东
				updatauser.setUserLevel((byte) 3);
				if (userDao.updateByPrimaryKeySelective(user) > 0) {
					// 获得区域代表资格，生成一条数据，等待后台封地
					District dis = new District();
					dis.setUserId(userid);
					districtDao.insertSelective(dis);
					level = 3;
				}
			}
			break;
		case 1:
			if (count >= 5 && 50 > count) {
				// 会员用户升级为vip
				updatauser.setUserLevel((byte) 2);
				if (userDao.updateByPrimaryKeySelective(user) > 0) {

				}
				level = 2;
			} else if (count >= 50) {
				// 会员用户升级为城市合伙人，升级为城市合伙人，要添加一条记录，并且判断用户推荐人是否有资格升级为股东
				updatauser.setUserLevel((byte) 3);
				if (userDao.updateByPrimaryKeySelective(user) > 0) {
					// 获得区域代表资格，生成一条数据，等待后台封地
					District dis = new District();
					dis.setUserId(userid);
					districtDao.insertSelective(dis);
					level = 3;
				}

			}
			break;
		case 2:
			if (count >= 50) {
				// vip用户升级为，升级为城市合伙人，要添加一条记录，并且判断用户推荐人是否有资格升级为股东
				updatauser.setUserLevel((byte) 3);
				if (userDao.updateByPrimaryKeySelective(user) > 0) {
					// 获得区域代表资格，生成一条数据，等待后台封地
					District dis = new District();
					dis.setUserId(userid);
					districtDao.insertSelective(dis);
					level = 3;
				}
				// 判断该购买者的推荐人是否满足升级为股东的条件,首先必须为城市合伙人，如何推荐5人为城市合伙人
				if (user.getId() == 3) {
					if (upgradePartner((int) user.getUserLevel())) {
						level = 4;
					}
				}

			}
			break;
		default:
			level = -1;
			break;

		}

		return level;
	}

	/**
	 * 用户升级为股东方法 userid 推荐人id
	 */
	@Override
	public boolean upgradePartner(Integer userid) {

		// 返回值
		boolean flag = false;
		// 获取推荐人的信息
		UserDo user = userDao.selectByPrimaryKey(userid);
		// 推荐人必须为城市合伙人才能升级
		if (user.getUserLevel() == 3) {
			// 获取推荐人推荐城市合伙人的个数
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userLevel", 3);
			map.put("refereeid", userid);
			List<UserDo> listuser = userDao.selectUser(map);
			if (listuser.size() >= 5) {
				UserDo updatauser = new UserDo();
				// 推荐人升级为股东

				updatauser.setUserLevel((byte) 4);
				if (userDao.updateByPrimaryKeySelective(updatauser) > 0) {
					// 升级股东成功
					flag = true;
				} else {
					// 升级失败
				}
			}

		}

		return flag;
	}

	/**
	 * 发放奖励方法
	 * 
	 */
	@Override
	public boolean updateAward(int userid, int count, String area) {
		// 返回值
		boolean flag = false;

		// 区域奖励,先进行区域奖励，再进行升级
		areaAward(area, count);
		
		System.out.println("进去了");
		// 获取购买者的信息
		UserDo buyerUser = userDao.selectByPrimaryKey(userid);
		// 获取推荐人的信息refereeid
		UserDo presenterUser = userDao.selectByPrimaryKey(buyerUser.getRefereeid());

		// 获取第二代推荐人的信息
		UserDo presenterUserTow = userDao.selectByPrimaryKey(presenterUser.getRefereeid());

		// 推荐奖励
		if (count >= 1 && count < 5) {
			// 1-5盒没有到5
			System.out.println("1-54asdasdasdasd");
			awardone(buyerUser, presenterUser, presenterUserTow, count);
			flag = true;

		} else if (count >= 5 && count < 50) {
			// 5-50没有到50
			awardtwo(buyerUser, presenterUser, presenterUserTow, count);
			flag = true;
		} else if (count >= 50) {
			// 50盒以上
			awardthree(buyerUser, presenterUser, presenterUserTow, count);
			flag = true;
		}

		return flag;
	}

	/**
	 * 
	 * 发放区域奖励方法
	 */
	@Override
	public boolean areaAward(String area, int count) {
		// 查询购买者填写地址所在区域的管理者信息 实体类字段district 数据库字段district
		// 判断该地区是否有区域代表
		Map<String, Object> map = new HashMap<>();
		map.put("district", area);
		List<UserDo> user = userDao.selectUser(map);

		if (user != null && user.size() == 1) {

			// 进行区域代表奖励50/盒
			// 获取总奖金
			Map<String, Object> maps = new HashMap<>();
			maps.put("algebra", 0);
			List<Regionalawards> regional = regionlDao.queryListPage(maps);
			Double sum = regional.get(0).getRewardbalance() * count;
			// 获取账户当前余额
			UserAccountDo accont = new UserAccountDo();
			accont = userAccoutDao.selectByPrimaryKey(user.get(0).getId());
			// 相加之后余额
			accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
			accont.setIncomeAmount(BigDecimal.valueOf(sum));
			userAccoutDao.updateByPrimaryKeySelective(accont);

			// 生成流水记录
			UserAccountDetailDo detail = new UserAccountDetailDo();
			detail.setUserId(user.get(0).getId());
			detail.setAmount(BigDecimal.valueOf(sum));
			detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
			detail.setMoreOrLess("增加");
			detail.setIncomeType(321);
			detail.setCreateTime(new Date());
			detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
			detail.setSeqId(UUID.randomUUID().toString());
			userAccountDetailDao.addUserAccountDetail(detail);

			// 判断区域代表是否有推荐人
			if (user.get(0).getRefereeid() != null) {
				// 进行区域代表奖励50/盒
				// 获取总奖金
				Map<String, Object> mapstwo = new HashMap<>();
				maps.put("algebra", 1);
				List<Regionalawards> regionaltwo = regionlDao.queryListPage(mapstwo);
				Double sumtwo = regionaltwo.get(0).getRewardbalance() * count;
				// 获取账户当前余额
				UserAccountDo acconttwo = new UserAccountDo();
				acconttwo = userAccoutDao.selectByPrimaryKey(user.get(0).getRefereeid());
				// 相加之后余额
				acconttwo.setAmount(acconttwo.getAmount().add(BigDecimal.valueOf(sumtwo)));
				acconttwo.setIncomeAmount(BigDecimal.valueOf(sumtwo));
				userAccoutDao.updateByPrimaryKeySelective(acconttwo);

				// 生成流水记录
				UserAccountDetailDo detailtwo = new UserAccountDetailDo();
				detailtwo.setUserId(user.get(0).getRefereeid());
				detailtwo.setAmount(BigDecimal.valueOf(sumtwo));
				detailtwo.setBalanceAmount(acconttwo.getAmount().add(BigDecimal.valueOf(sumtwo)));
				detailtwo.setMoreOrLess("增加");
				detailtwo.setIncomeType(311);
				detailtwo.setCreateTime(new Date());
				detailtwo.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
				detailtwo.setSeqId(UUID.randomUUID().toString());
				userAccountDetailDao.addUserAccountDetail(detailtwo);
			}

		}

		return false;
	}

	/**
	 * 
	 * 判断1-5盒数量的方法
	 * 
	 * @param buyerUser
	 *            购买者的信息
	 * @param presenterUser
	 *            获取推荐人的信息
	 * @param presenterUserTow
	 *            获取第二代推荐人的信息
	 * @param count
	 *            商品数量
	 * @return
	 */
	public boolean awardone(UserDo buyerUser, UserDo presenterUser, UserDo presenterUserTow, int count) {
		Map<String, Object> map = new HashMap<>();
		List<Awardlist> awardlist = new ArrayList<Awardlist>();
		System.out.println(buyerUser.getUserLevel());
		// 购买者的等级分类
		switch (buyerUser.getUserLevel()) {
		// 购买者等级为普通用户
		case 0:

			userUpgrade(buyerUser.getId(), count);
			// 推荐者等级分类
			System.out.println(presenterUser.getUserLevel());
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠

				// 赠送给购买者旅游和商品没有写

				// 区域奖励没有写，直接调用方法吧┭┮﹏┭┮

				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金
				// 判断该会员是否是首次分享
				if (awardlistDao.selectByPrimaryKey(buyerUser.getRefereeid()) == null) {
					// 购买者等级
					map.put("buyerLecel", (int) buyerUser.getUserLevel());
					// 购买盒数
					map.put("buyQty", 1);
					awardlist = awardlistDao.queryListPage(map);
					if (awardlist.size() == 1) {
						// 生成账户记录
						// 获取总奖金
						Double sum = awardlist.get(0).getP1Level1();
						// 获取账户当前余额
						UserAccountDo accont = new UserAccountDo();
						accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
						// 相加之后余额
						accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						accont.setIncomeAmount(BigDecimal.valueOf(sum));
						userAccoutDao.updateByPrimaryKeySelective(accont);

						// 生成流水记录
						UserAccountDetailDo detail = new UserAccountDetailDo();
						detail.setUserId(presenterUser.getId());
						detail.setAmount(BigDecimal.valueOf(sum));
						detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						detail.setMoreOrLess("增加");
						detail.setIncomeType(311);
						detail.setCreateTime(new Date());
						detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
						detail.setSeqId(UUID.randomUUID().toString());
						userAccountDetailDao.addUserAccountDetail(detail);

					}
				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金
					Double sum = awardlist.get(0).getP1Level2() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level3() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level4() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;
		// 购买者等级为会员
		case 1:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠

				// 用户升级
				userUpgrade(buyerUser.getId(), count);
				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金

				// 判断该会员是否是首次分享
				if (awardlistDao.selectByPrimaryKey(buyerUser.getRefereeid()) == null) {
					// 购买者等级
					map.put("buyerLecel", (int) buyerUser.getUserLevel());
					// 购买盒数
					map.put("buyQty", 1);
					awardlist = awardlistDao.queryListPage(map);
					if (awardlist.size() == 1) {
						// 生成账户记录
						// 获取总奖金
						Double sum = awardlist.get(0).getP1Level1();
						// 获取账户当前余额
						UserAccountDo accont = new UserAccountDo();
						accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
						// 相加之后余额
						accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						accont.setIncomeAmount(BigDecimal.valueOf(sum));
						userAccoutDao.updateByPrimaryKeySelective(accont);

						// 生成流水记录
						UserAccountDetailDo detail = new UserAccountDetailDo();
						detail.setUserId(presenterUser.getId());
						detail.setAmount(BigDecimal.valueOf(sum));
						detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						detail.setMoreOrLess("增加");
						detail.setIncomeType(311);
						detail.setCreateTime(new Date());
						detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
						detail.setSeqId(UUID.randomUUID().toString());
						userAccountDetailDao.addUserAccountDetail(detail);

					}
				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level2() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level3() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level4() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游
				userUpgrade(buyerUser.getId(), count);
				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;
		// 购买者等级为vip
		case 2:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠
				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金
				// 判断该会员是否是首次分享
				if (awardlistDao.selectByPrimaryKey(buyerUser.getRefereeid()) == null) {
					// 购买者等级
					map.put("buyerLecel", (int) buyerUser.getUserLevel());
					// 购买盒数
					map.put("buyQty", 1);
					awardlist = awardlistDao.queryListPage(map);
					if (awardlist.size() == 1) {
						// 生成账户记录
						// 获取总奖金
						Double sum = awardlist.get(0).getP1Level1();
						// 获取账户当前余额
						UserAccountDo accont = new UserAccountDo();
						accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
						// 相加之后余额
						accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						accont.setIncomeAmount(BigDecimal.valueOf(sum));
						userAccoutDao.updateByPrimaryKeySelective(accont);

						// 生成流水记录
						UserAccountDetailDo detail = new UserAccountDetailDo();
						detail.setUserId(presenterUser.getId());
						detail.setAmount(BigDecimal.valueOf(sum));
						detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						detail.setMoreOrLess("增加");
						detail.setIncomeType(311);
						detail.setCreateTime(new Date());
						detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
						detail.setSeqId(UUID.randomUUID().toString());
						userAccountDetailDao.addUserAccountDetail(detail);

					}
				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level2() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写
				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level3() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写
				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level4() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;
		// 购买者等级为城市合伙人
		case 3:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠
				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金
				// 判断该会员是否是首次分享
				if (awardlistDao.selectByPrimaryKey(buyerUser.getRefereeid()) == null) {
					// 购买者等级
					map.put("buyerLecel", (int) buyerUser.getUserLevel());
					// 购买盒数
					map.put("buyQty", 1);
					awardlist = awardlistDao.queryListPage(map);
					if (awardlist.size() == 1) {
						// 生成账户记录
						// 获取总奖金
						Double sum = awardlist.get(0).getP1Level1();
						// 获取账户当前余额
						UserAccountDo accont = new UserAccountDo();
						accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
						// 相加之后余额
						accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						accont.setIncomeAmount(BigDecimal.valueOf(sum));
						userAccoutDao.updateByPrimaryKeySelective(accont);

						// 生成流水记录
						UserAccountDetailDo detail = new UserAccountDetailDo();
						detail.setUserId(presenterUser.getId());
						detail.setAmount(BigDecimal.valueOf(sum));
						detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						detail.setMoreOrLess("增加");
						detail.setIncomeType(311);
						detail.setCreateTime(new Date());
						detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
						detail.setSeqId(UUID.randomUUID().toString());
						userAccountDetailDao.addUserAccountDetail(detail);

					}
				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写
				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level2() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level3() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level4() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;
		// 购买者等级为股东
		case 4:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠
				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金
				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level2() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level3() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					Double sum = awardlist.get(0).getP1Level4() * count;
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;

		// 无此等级
		default:
			break;
		}

		return false;

	}

	/**
	 * 
	 * 判断50以上盒数量的方法
	 * 
	 * @param buyerUser
	 *            购买者的信息
	 * @param presenterUser
	 *            获取推荐人的信息
	 * @param presenterUserTow
	 *            获取第二代推荐人的信息
	 * @param count
	 *            商品数量
	 * @return
	 */
	public boolean awardthree(UserDo buyerUser, UserDo presenterUser, UserDo presenterUserTow, int count) {
		Map<String, Object> map = new HashMap<>();
		List<Awardlist> awardlist = new ArrayList<Awardlist>();

		// 单价/盒
		Double price = 300.00;
		// 购买者的等级分类
		switch (buyerUser.getUserLevel()) {
		// 购买者等级为普通用户
		case 0:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠

				// 用户升级
				userUpgrade(buyerUser.getId(), count);

				// 赠送给购买者旅游和商品没有写

				// 区域奖励没有写，直接调用方法吧┭┮﹏┭┮

				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金
				// 判断该会员是否是首次分享
				if (awardlistDao.selectByPrimaryKey(buyerUser.getRefereeid()) == null) {
					// 购买者等级
					map.put("buyerLecel", (int) buyerUser.getUserLevel());
					// 购买盒数
					map.put("buyQty", 1);
					awardlist = awardlistDao.queryListPage(map);
					if (awardlist.size() == 1) {
						// 生成账户记录
						// 获取总奖金
						Double sum = awardlist.get(0).getP1Level1();
						// 获取账户当前余额
						UserAccountDo accont = new UserAccountDo();
						accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
						// 相加之后余额
						accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						accont.setIncomeAmount(BigDecimal.valueOf(sum));
						userAccoutDao.updateByPrimaryKeySelective(accont);

						// 生成流水记录
						UserAccountDetailDo detail = new UserAccountDetailDo();
						detail.setUserId(presenterUser.getId());
						detail.setAmount(BigDecimal.valueOf(sum));
						detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						detail.setMoreOrLess("增加");
						detail.setIncomeType(311);
						detail.setCreateTime(new Date());
						detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
						detail.setSeqId(UUID.randomUUID().toString());
						userAccountDetailDao.addUserAccountDetail(detail);

					}
				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);

				if (awardlist.size() == 1) {
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level2();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

					/*
					 * //第二代奖励 Double sumtow = awardlist.get(0).getP2Level2();
					 * // 获取账户当前余额 UserAccountDo acconttow = new
					 * UserAccountDo(); acconttow =
					 * userAccoutDao.selectByPrimaryKey(presenterUserTow.getId()
					 * ); // 相加之后余额
					 * acconttow.setAmount(acconttow.getAmount().add(BigDecimal.
					 * valueOf(sumtow)));
					 * acconttow.setIncomeAmount(BigDecimal.valueOf(sum));
					 * userAccoutDao.updateByPrimaryKeySelective(acconttow);
					 * 
					 * 
					 * UserAccountDetailDo detailtwo = new
					 * UserAccountDetailDo();
					 * detailtwo.setUserId(presenterUserTow.getId());
					 * detailtwo.setAmount(BigDecimal.valueOf(sumtow));
					 * detailtwo.setBalanceAmount(acconttow.getAmount().add(
					 * BigDecimal.valueOf(sumtow)));
					 * detailtwo.setMoreOrLess("增加");
					 * detailtwo.setIncomeType(311); detailtwo.setCreateTime(new
					 * Date());
					 * detailtwo.setRemark(IncomeType.TYPE_AWARD_REFEREE.
					 * getRemark());
					 * detailtwo.setSeqId(UUID.randomUUID().toString());
					 * userAccountDetailDao.addUserAccountDetail(detailtwo);
					 */

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}
					sum += awardlist.get(0).getP1Level3();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

					/*
					 * //第二代奖励 Double sumtow = awardlist.get(0).getP2Level2();
					 * // 获取账户当前余额 UserAccountDo acconttow = new
					 * UserAccountDo(); acconttow =
					 * userAccoutDao.selectByPrimaryKey(presenterUserTow.getId()
					 * ); // 相加之后余额
					 * acconttow.setAmount(acconttow.getAmount().add(BigDecimal.
					 * valueOf(sumtow)));
					 * acconttow.setIncomeAmount(BigDecimal.valueOf(sum));
					 * userAccoutDao.updateByPrimaryKeySelective(acconttow);
					 * 
					 * 
					 * UserAccountDetailDo detailtwo = new
					 * UserAccountDetailDo();
					 * detailtwo.setUserId(presenterUserTow.getId());
					 * detailtwo.setAmount(BigDecimal.valueOf(sumtow));
					 * detailtwo.setBalanceAmount(acconttow.getAmount().add(
					 * BigDecimal.valueOf(sumtow)));
					 * detailtwo.setMoreOrLess("增加");
					 * detailtwo.setIncomeType(311); detailtwo.setCreateTime(new
					 * Date());
					 * detailtwo.setRemark(IncomeType.TYPE_AWARD_REFEREE.
					 * getRemark());
					 * detailtwo.setSeqId(UUID.randomUUID().toString());
					 * userAccountDetailDao.addUserAccountDetail(detailtwo);
					 */

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断推荐人是否有资格升级股东
				upgradePartner(presenterUser.getId());
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level4();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

					/*
					 * //第二代奖励 Double sumtow = awardlist.get(0).getP2Level2();
					 * // 获取账户当前余额 UserAccountDo acconttow = new
					 * UserAccountDo(); acconttow =
					 * userAccoutDao.selectByPrimaryKey(presenterUserTow.getId()
					 * ); // 相加之后余额
					 * acconttow.setAmount(acconttow.getAmount().add(BigDecimal.
					 * valueOf(sumtow)));
					 * acconttow.setIncomeAmount(BigDecimal.valueOf(sum));
					 * userAccoutDao.updateByPrimaryKeySelective(acconttow);
					 * 
					 * 
					 * UserAccountDetailDo detailtwo = new
					 * UserAccountDetailDo();
					 * detailtwo.setUserId(presenterUserTow.getId());
					 * detailtwo.setAmount(BigDecimal.valueOf(sumtow));
					 * detailtwo.setBalanceAmount(acconttow.getAmount().add(
					 * BigDecimal.valueOf(sumtow)));
					 * detailtwo.setMoreOrLess("增加");
					 * detailtwo.setIncomeType(311); detailtwo.setCreateTime(new
					 * Date());
					 * detailtwo.setRemark(IncomeType.TYPE_AWARD_REFEREE.
					 * getRemark());
					 * detailtwo.setSeqId(UUID.randomUUID().toString());
					 * userAccountDetailDao.addUserAccountDetail(detailtwo);
					 */

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;
		// 购买者等级为会员
		case 1:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠

				// 用户升级
				userUpgrade(buyerUser.getId(), count);
				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金

				// 判断该会员是否是首次分享
				if (awardlistDao.selectByPrimaryKey(buyerUser.getRefereeid()) == null) {
					// 购买者等级
					map.put("buyerLecel", (int) buyerUser.getUserLevel());
					// 购买盒数
					map.put("buyQty", 1);
					awardlist = awardlistDao.queryListPage(map);
					if (awardlist.size() == 1) {
						// 生成账户记录
						// 获取总奖金
						Double sum = awardlist.get(0).getP1Level1();
						// 获取账户当前余额
						UserAccountDo accont = new UserAccountDo();
						accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
						// 相加之后余额
						accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						accont.setIncomeAmount(BigDecimal.valueOf(sum));
						userAccoutDao.updateByPrimaryKeySelective(accont);

						// 生成流水记录
						UserAccountDetailDo detail = new UserAccountDetailDo();
						detail.setUserId(presenterUser.getId());
						detail.setAmount(BigDecimal.valueOf(sum));
						detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						detail.setMoreOrLess("增加");
						detail.setIncomeType(311);
						detail.setCreateTime(new Date());
						detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
						detail.setSeqId(UUID.randomUUID().toString());
						userAccountDetailDao.addUserAccountDetail(detail);

					}
				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level2();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

					/*
					 * //第二代奖励 Double sumtow = awardlist.get(0).getP2Level2();
					 * // 获取账户当前余额 UserAccountDo acconttow = new
					 * UserAccountDo(); acconttow =
					 * userAccoutDao.selectByPrimaryKey(presenterUserTow.getId()
					 * ); // 相加之后余额
					 * acconttow.setAmount(acconttow.getAmount().add(BigDecimal.
					 * valueOf(sumtow)));
					 * acconttow.setIncomeAmount(BigDecimal.valueOf(sum));
					 * userAccoutDao.updateByPrimaryKeySelective(acconttow);
					 * 
					 * 
					 * //流水记录 UserAccountDetailDo detailtwo = new
					 * UserAccountDetailDo();
					 * detailtwo.setUserId(presenterUserTow.getId());
					 * detailtwo.setAmount(BigDecimal.valueOf(sumtow));
					 * detailtwo.setBalanceAmount(acconttow.getAmount().add(
					 * BigDecimal.valueOf(sumtow)));
					 * detailtwo.setMoreOrLess("增加");
					 * detailtwo.setIncomeType(311); detailtwo.setCreateTime(new
					 * Date());
					 * detailtwo.setRemark(IncomeType.TYPE_AWARD_REFEREE.
					 * getRemark());
					 * detailtwo.setSeqId(UUID.randomUUID().toString());
					 * userAccountDetailDao.addUserAccountDetail(detailtwo);
					 */

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level3();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);
					/*
					 * //第二代奖励 Double sumtow = awardlist.get(0).getP2Level2();
					 * // 获取账户当前余额 UserAccountDo acconttow = new
					 * UserAccountDo(); acconttow =
					 * userAccoutDao.selectByPrimaryKey(presenterUserTow.getId()
					 * ); // 相加之后余额
					 * acconttow.setAmount(acconttow.getAmount().add(BigDecimal.
					 * valueOf(sumtow)));
					 * acconttow.setIncomeAmount(BigDecimal.valueOf(sum));
					 * userAccoutDao.updateByPrimaryKeySelective(acconttow);
					 * 
					 * 
					 * //流水记录 UserAccountDetailDo detailtwo = new
					 * UserAccountDetailDo();
					 * detailtwo.setUserId(presenterUserTow.getId());
					 * detailtwo.setAmount(BigDecimal.valueOf(sumtow));
					 * detailtwo.setBalanceAmount(acconttow.getAmount().add(
					 * BigDecimal.valueOf(sumtow)));
					 * detailtwo.setMoreOrLess("增加");
					 * detailtwo.setIncomeType(311); detailtwo.setCreateTime(new
					 * Date());
					 * detailtwo.setRemark(IncomeType.TYPE_AWARD_REFEREE.
					 * getRemark());
					 * detailtwo.setSeqId(UUID.randomUUID().toString());
					 * userAccountDetailDao.addUserAccountDetail(detailtwo);
					 */

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断推荐人是否有资格升级股东
				upgradePartner(presenterUser.getId());
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level4();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

					/*
					 * //第二代奖励 Double sumtow = awardlist.get(0).getP2Level2();
					 * // 获取账户当前余额 UserAccountDo acconttow = new
					 * UserAccountDo(); acconttow =
					 * userAccoutDao.selectByPrimaryKey(presenterUserTow.getId()
					 * ); // 相加之后余额
					 * acconttow.setAmount(acconttow.getAmount().add(BigDecimal.
					 * valueOf(sumtow)));
					 * acconttow.setIncomeAmount(BigDecimal.valueOf(sum));
					 * userAccoutDao.updateByPrimaryKeySelective(acconttow);
					 * 
					 * 
					 * //流水记录 UserAccountDetailDo detailtwo = new
					 * UserAccountDetailDo();
					 * detailtwo.setUserId(presenterUserTow.getId());
					 * detailtwo.setAmount(BigDecimal.valueOf(sumtow));
					 * detailtwo.setBalanceAmount(acconttow.getAmount().add(
					 * BigDecimal.valueOf(sumtow)));
					 * detailtwo.setMoreOrLess("增加");
					 * detailtwo.setIncomeType(311); detailtwo.setCreateTime(new
					 * Date());
					 * detailtwo.setRemark(IncomeType.TYPE_AWARD_REFEREE.
					 * getRemark());
					 * detailtwo.setSeqId(UUID.randomUUID().toString());
					 * userAccountDetailDao.addUserAccountDetail(detailtwo);
					 */

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;
		// 购买者等级为vip
		case 2:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠

				// 用户升级
				userUpgrade(buyerUser.getId(), count);
				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金
				// 判断该会员是否是首次分享
				if (awardlistDao.selectByPrimaryKey(buyerUser.getRefereeid()) == null) {
					// 购买者等级
					map.put("buyerLecel", (int) buyerUser.getUserLevel());
					// 购买盒数
					map.put("buyQty", 1);
					awardlist = awardlistDao.queryListPage(map);
					if (awardlist.size() == 1) {
						// 生成账户记录
						// 获取总奖金
						Double sum = awardlist.get(0).getP1Level1();
						// 获取账户当前余额
						UserAccountDo accont = new UserAccountDo();
						accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
						// 相加之后余额
						accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						accont.setIncomeAmount(BigDecimal.valueOf(sum));
						userAccoutDao.updateByPrimaryKeySelective(accont);

						// 生成流水记录
						UserAccountDetailDo detail = new UserAccountDetailDo();
						detail.setUserId(presenterUser.getId());
						detail.setAmount(BigDecimal.valueOf(sum));
						detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						detail.setMoreOrLess("增加");
						detail.setIncomeType(311);
						detail.setCreateTime(new Date());
						detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
						detail.setSeqId(UUID.randomUUID().toString());
						userAccountDetailDao.addUserAccountDetail(detail);

					}
				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level2();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写
				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level3();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断推荐人是否有资格升级股东
				upgradePartner(presenterUser.getId());
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写
				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level4();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;
		// 购买者等级为城市合伙人
		case 3:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠

				// 用户升级
				upgradePartner(presenterUser.getId());
				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金
				// 判断该会员是否是首次分享
				if (awardlistDao.selectByPrimaryKey(buyerUser.getRefereeid()) == null) {
					// 购买者等级
					map.put("buyerLecel", (int) buyerUser.getUserLevel());
					// 购买盒数
					map.put("buyQty", 1);
					awardlist = awardlistDao.queryListPage(map);
					if (awardlist.size() == 1) {
						// 生成账户记录
						// 获取总奖金
						Double sum = awardlist.get(0).getP1Level1();
						// 获取账户当前余额
						UserAccountDo accont = new UserAccountDo();
						accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
						// 相加之后余额
						accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						accont.setIncomeAmount(BigDecimal.valueOf(sum));
						userAccoutDao.updateByPrimaryKeySelective(accont);

						// 生成流水记录
						UserAccountDetailDo detail = new UserAccountDetailDo();
						detail.setUserId(presenterUser.getId());
						detail.setAmount(BigDecimal.valueOf(sum));
						detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						detail.setMoreOrLess("增加");
						detail.setIncomeType(311);
						detail.setCreateTime(new Date());
						detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
						detail.setSeqId(UUID.randomUUID().toString());
						userAccountDetailDao.addUserAccountDetail(detail);

					}
				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写
				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level2();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					/// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level3();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level4();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;
		// 购买者等级为股东
		case 4:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠
				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金
				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level2();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level3();

					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 50));
					}

					sum += awardlist.get(0).getP1Level4();

					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;

		// 无此等级
		default:
			break;
		}

		return false;

	}

	/**
	 * 
	 * 判断5-50盒数量的方法
	 * 
	 * @param buyerUser
	 *            购买者的信息
	 * @param presenterUser
	 *            获取推荐人的信息
	 * @param presenterUserTow
	 *            获取第二代推荐人的信息
	 * @param count
	 *            商品数量
	 * @return
	 */
	public boolean awardtwo(UserDo buyerUser, UserDo presenterUser, UserDo presenterUserTow, int count) {
		Map<String, Object> map = new HashMap<>();
		List<Awardlist> awardlist = new ArrayList<Awardlist>();

		// 单价/盒
		Double price = 300.00;
		// 购买者的等级分类
		switch (buyerUser.getUserLevel()) {
		// 购买者等级为普通用户
		case 0:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠

				// 用户升级
				userUpgrade(buyerUser.getId(), count);

				// 赠送给购买者旅游和商品没有写

				// 区域奖励没有写，直接调用方法吧┭┮﹏┭┮

				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金
				// 判断该会员是否是首次分享
				if (awardlistDao.selectByPrimaryKey(buyerUser.getRefereeid()) == null) {
					// 购买者等级
					map.put("buyerLecel", (int) buyerUser.getUserLevel());
					// 购买盒数
					map.put("buyQty", 1);
					awardlist = awardlistDao.queryListPage(map);
					if (awardlist.size() == 1) {
						// 生成账户记录
						// 获取总奖金
						Double sum = awardlist.get(0).getP1Level1();
						// 获取账户当前余额
						UserAccountDo accont = new UserAccountDo();
						accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
						// 相加之后余额
						accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						accont.setIncomeAmount(BigDecimal.valueOf(sum));
						userAccoutDao.updateByPrimaryKeySelective(accont);

						// 生成流水记录
						UserAccountDetailDo detail = new UserAccountDetailDo();
						detail.setUserId(presenterUser.getId());
						detail.setAmount(BigDecimal.valueOf(sum));
						detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						detail.setMoreOrLess("增加");
						detail.setIncomeType(311);
						detail.setCreateTime(new Date());
						detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
						detail.setSeqId(UUID.randomUUID().toString());
						userAccountDetailDao.addUserAccountDetail(detail);

					}
				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);

				if (awardlist.size() == 1) {
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level2();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

					// 第二代奖励
					Double sumtow = awardlist.get(0).getP2Level2();
					// 获取账户当前余额
					UserAccountDo acconttow = new UserAccountDo();
					acconttow = userAccoutDao.selectByPrimaryKey(presenterUserTow.getId());
					// 相加之后余额
					acconttow.setAmount(acconttow.getAmount().add(BigDecimal.valueOf(sumtow)));
					acconttow.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(acconttow);

					UserAccountDetailDo detailtwo = new UserAccountDetailDo();
					detailtwo.setUserId(presenterUserTow.getId());
					detailtwo.setAmount(BigDecimal.valueOf(sumtow));
					detailtwo.setBalanceAmount(acconttow.getAmount().add(BigDecimal.valueOf(sumtow)));
					detailtwo.setMoreOrLess("增加");
					detailtwo.setIncomeType(311);
					detailtwo.setCreateTime(new Date());
					detailtwo.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detailtwo.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detailtwo);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}
					sum += awardlist.get(0).getP1Level3();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

					// 第二代奖励
					Double sumtow = awardlist.get(0).getP2Level2();
					// 获取账户当前余额
					UserAccountDo acconttow = new UserAccountDo();
					acconttow = userAccoutDao.selectByPrimaryKey(presenterUserTow.getId());
					// 相加之后余额
					acconttow.setAmount(acconttow.getAmount().add(BigDecimal.valueOf(sumtow)));
					acconttow.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(acconttow);

					UserAccountDetailDo detailtwo = new UserAccountDetailDo();
					detailtwo.setUserId(presenterUserTow.getId());
					detailtwo.setAmount(BigDecimal.valueOf(sumtow));
					detailtwo.setBalanceAmount(acconttow.getAmount().add(BigDecimal.valueOf(sumtow)));
					detailtwo.setMoreOrLess("增加");
					detailtwo.setIncomeType(311);
					detailtwo.setCreateTime(new Date());
					detailtwo.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detailtwo.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detailtwo);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level4();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

					// 第二代奖励
					Double sumtow = awardlist.get(0).getP2Level2();
					// 获取账户当前余额
					UserAccountDo acconttow = new UserAccountDo();
					acconttow = userAccoutDao.selectByPrimaryKey(presenterUserTow.getId());
					// 相加之后余额
					acconttow.setAmount(acconttow.getAmount().add(BigDecimal.valueOf(sumtow)));
					acconttow.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(acconttow);

					UserAccountDetailDo detailtwo = new UserAccountDetailDo();
					detailtwo.setUserId(presenterUserTow.getId());
					detailtwo.setAmount(BigDecimal.valueOf(sumtow));
					detailtwo.setBalanceAmount(acconttow.getAmount().add(BigDecimal.valueOf(sumtow)));
					detailtwo.setMoreOrLess("增加");
					detailtwo.setIncomeType(311);
					detailtwo.setCreateTime(new Date());
					detailtwo.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detailtwo.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detailtwo);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;
		// 购买者等级为会员
		case 1:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠
				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金

				// 判断该会员是否是首次分享
				if (awardlistDao.selectByPrimaryKey(buyerUser.getRefereeid()) == null) {
					// 购买者等级
					map.put("buyerLecel", (int) buyerUser.getUserLevel());
					// 购买盒数
					map.put("buyQty", 1);
					awardlist = awardlistDao.queryListPage(map);
					if (awardlist.size() == 1) {
						// 生成账户记录
						// 获取总奖金
						Double sum = awardlist.get(0).getP1Level1();
						// 获取账户当前余额
						UserAccountDo accont = new UserAccountDo();
						accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
						// 相加之后余额
						accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						accont.setIncomeAmount(BigDecimal.valueOf(sum));
						userAccoutDao.updateByPrimaryKeySelective(accont);

						// 生成流水记录
						UserAccountDetailDo detail = new UserAccountDetailDo();
						detail.setUserId(presenterUser.getId());
						detail.setAmount(BigDecimal.valueOf(sum));
						detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						detail.setMoreOrLess("增加");
						detail.setIncomeType(311);
						detail.setCreateTime(new Date());
						detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
						detail.setSeqId(UUID.randomUUID().toString());
						userAccountDetailDao.addUserAccountDetail(detail);

					}
				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level2();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

					// 第二代奖励
					Double sumtow = awardlist.get(0).getP2Level2();
					// 获取账户当前余额
					UserAccountDo acconttow = new UserAccountDo();
					acconttow = userAccoutDao.selectByPrimaryKey(presenterUserTow.getId());
					// 相加之后余额
					acconttow.setAmount(acconttow.getAmount().add(BigDecimal.valueOf(sumtow)));
					acconttow.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(acconttow);

					// 流水记录
					UserAccountDetailDo detailtwo = new UserAccountDetailDo();
					detailtwo.setUserId(presenterUserTow.getId());
					detailtwo.setAmount(BigDecimal.valueOf(sumtow));
					detailtwo.setBalanceAmount(acconttow.getAmount().add(BigDecimal.valueOf(sumtow)));
					detailtwo.setMoreOrLess("增加");
					detailtwo.setIncomeType(311);
					detailtwo.setCreateTime(new Date());
					detailtwo.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detailtwo.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detailtwo);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level3();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

					// 第二代奖励
					Double sumtow = awardlist.get(0).getP2Level2();
					// 获取账户当前余额
					UserAccountDo acconttow = new UserAccountDo();
					acconttow = userAccoutDao.selectByPrimaryKey(presenterUserTow.getId());
					// 相加之后余额
					acconttow.setAmount(acconttow.getAmount().add(BigDecimal.valueOf(sumtow)));
					acconttow.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(acconttow);

					// 流水记录
					UserAccountDetailDo detailtwo = new UserAccountDetailDo();
					detailtwo.setUserId(presenterUserTow.getId());
					detailtwo.setAmount(BigDecimal.valueOf(sumtow));
					detailtwo.setBalanceAmount(acconttow.getAmount().add(BigDecimal.valueOf(sumtow)));
					detailtwo.setMoreOrLess("增加");
					detailtwo.setIncomeType(311);
					detailtwo.setCreateTime(new Date());
					detailtwo.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detailtwo.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detailtwo);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level4();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

					// 第二代奖励
					Double sumtow = awardlist.get(0).getP2Level2();
					// 获取账户当前余额
					UserAccountDo acconttow = new UserAccountDo();
					acconttow = userAccoutDao.selectByPrimaryKey(presenterUserTow.getId());
					// 相加之后余额
					acconttow.setAmount(acconttow.getAmount().add(BigDecimal.valueOf(sumtow)));
					acconttow.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(acconttow);

					// 流水记录
					UserAccountDetailDo detailtwo = new UserAccountDetailDo();
					detailtwo.setUserId(presenterUserTow.getId());
					detailtwo.setAmount(BigDecimal.valueOf(sumtow));
					detailtwo.setBalanceAmount(acconttow.getAmount().add(BigDecimal.valueOf(sumtow)));
					detailtwo.setMoreOrLess("增加");
					detailtwo.setIncomeType(311);
					detailtwo.setCreateTime(new Date());
					detailtwo.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detailtwo.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detailtwo);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;
		// 购买者等级为vip
		case 2:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠
				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金
				// 判断该会员是否是首次分享
				if (awardlistDao.selectByPrimaryKey(buyerUser.getRefereeid()) == null) {
					// 购买者等级
					map.put("buyerLecel", (int) buyerUser.getUserLevel());
					// 购买盒数
					map.put("buyQty", 1);
					awardlist = awardlistDao.queryListPage(map);
					if (awardlist.size() == 1) {
						// 生成账户记录
						// 获取总奖金
						Double sum = awardlist.get(0).getP1Level1();
						// 获取账户当前余额
						UserAccountDo accont = new UserAccountDo();
						accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
						// 相加之后余额
						accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						accont.setIncomeAmount(BigDecimal.valueOf(sum));
						userAccoutDao.updateByPrimaryKeySelective(accont);

						// 生成流水记录
						UserAccountDetailDo detail = new UserAccountDetailDo();
						detail.setUserId(presenterUser.getId());
						detail.setAmount(BigDecimal.valueOf(sum));
						detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						detail.setMoreOrLess("增加");
						detail.setIncomeType(311);
						detail.setCreateTime(new Date());
						detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
						detail.setSeqId(UUID.randomUUID().toString());
						userAccountDetailDao.addUserAccountDetail(detail);

					}
				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				userUpgrade(buyerUser.getId(), count);
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level2();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写
				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level3();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写
				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level4();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;
		// 购买者等级为城市合伙人
		case 3:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠
				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金
				// 判断该会员是否是首次分享
				if (awardlistDao.selectByPrimaryKey(buyerUser.getRefereeid()) == null) {
					// 购买者等级
					map.put("buyerLecel", (int) buyerUser.getUserLevel());
					// 购买盒数
					map.put("buyQty", 1);
					awardlist = awardlistDao.queryListPage(map);
					if (awardlist.size() == 1) {
						// 生成账户记录
						// 获取总奖金
						Double sum = awardlist.get(0).getP1Level1();
						// 获取账户当前余额
						UserAccountDo accont = new UserAccountDo();
						accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
						// 相加之后余额
						accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						accont.setIncomeAmount(BigDecimal.valueOf(sum));
						userAccoutDao.updateByPrimaryKeySelective(accont);

						// 生成流水记录
						UserAccountDetailDo detail = new UserAccountDetailDo();
						detail.setUserId(presenterUser.getId());
						detail.setAmount(BigDecimal.valueOf(sum));
						detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
						detail.setMoreOrLess("增加");
						detail.setIncomeType(311);
						detail.setCreateTime(new Date());
						detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
						detail.setSeqId(UUID.randomUUID().toString());
						userAccountDetailDao.addUserAccountDetail(detail);

					}
				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮
				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写
				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level2();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					/// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level3();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level4();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;
		// 购买者等级为股东
		case 4:

			// 推荐者等级分类
			switch (presenterUser.getUserLevel()) {
			case 0:
				// 推荐人为普通用户，无任何优惠
				break;
			case 1:
				// 推荐人为会员，有一次分享机会，获得300元奖金
				break;
			case 2:
				// 推荐人为vip，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金

					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level2();
					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 3:
				// 推荐人为城市合伙人，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level3();

					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;
			case 4:
				// 推荐人为股东，每分享一盒就奖励300元
				// 购买者等级
				map.put("buyerLecel", (int) buyerUser.getUserLevel());
				// 购买盒数
				map.put("buyQty", 1);
				awardlist = awardlistDao.queryListPage(map);
				if (awardlist.size() == 1) {
					// 获取总奖金
					Double sum = 0.00;
					// 判断用户是否购买5和以上
					if (count > 5) {
						sum = (price * (count - 5));
					}

					sum += awardlist.get(0).getP1Level4();

					// 获取账户当前余额
					UserAccountDo accont = new UserAccountDo();
					accont = userAccoutDao.selectByPrimaryKey(presenterUser.getId());
					// 相加之后余额
					accont.setAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					accont.setIncomeAmount(BigDecimal.valueOf(sum));
					userAccoutDao.updateByPrimaryKeySelective(accont);

					// 生成流水记录
					UserAccountDetailDo detail = new UserAccountDetailDo();
					detail.setUserId(presenterUser.getId());
					detail.setAmount(BigDecimal.valueOf(sum));
					detail.setBalanceAmount(accont.getAmount().add(BigDecimal.valueOf(sum)));
					detail.setMoreOrLess("增加");
					detail.setIncomeType(311);
					detail.setCreateTime(new Date());
					detail.setRemark(IncomeType.TYPE_AWARD_REFEREE.getRemark());
					detail.setSeqId(UUID.randomUUID().toString());
					userAccountDetailDao.addUserAccountDetail(detail);

				}
				// 区域奖励，写个发放直接调用吧┭┮﹏┭┮

				// 用户升级，直接调用发放吧┭┮﹏┭┮

				// 判断购买者是否已奖励过旅游

				// 赠送旅游和商品没有写

				break;

			default:
				// 获取推荐人无此等级
				break;
			}

			break;

		// 无此等级
		default:
			break;
		}

		return false;

	}

}
