package com.dce.business.service.impl.award;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.dao.user.IUserRefereeDao;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.district.Regionalawards;
import com.dce.business.entity.order.Order;
import com.dce.business.entity.user.UserAddressDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.district.IRegionalawardsService;
import com.dce.business.service.order.IOrderService;
import com.dce.business.service.user.IUserService;
import com.dce.business.service.user.UserAdressService;

/**
 * 区域奖金计算类
 * 
 * @author harry
 *
 */
@Service("areaAwardCalculator")
public class AreaAwardCalculator implements IAwardCalculator {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private IOrderService orderService;

	@Resource
	private UserAdressService userAdressService;

	@Resource
	private IUserService userService;

	@Resource
	private IRegionalawardsService regionalawardsService;

	// 账户
	@Resource
	private IAccountService accountService;

	@Resource
	private IUserRefereeDao userRefereeDao;

	/**
	 * 计算奖励的方法
	 * 
	 * @param buyUserId
	 *            购买者
	 * @param buyQty
	 *            购买数量
	 * @param orderId
	 *            购买订单
	 * @return
	 */
	@Override
	public void doAward(int buyUserId, int buyQty, Integer orderId) {
		Assert.notNull(buyUserId, "购买者用户ID不能为空");
		Assert.notNull(buyQty, "购买数量不能为空");
		Assert.notNull(orderId, "购买订单ID不能为空");

		// 获取订单信息
		Order order = orderService.selectByPrimaryKey(Integer.parseInt(String.valueOf(orderId)));

		if(order==null){
			throw new BusinessException("无效的订单ID", "error-buyerAward-003");
		}
		// 获取地址信息
		UserAddressDo useraddress = userAdressService.selectByPrimaryKey(order.getAddressid());
		if(useraddress==null){
			logger.warn("订单地址ID无效");
			return;
		}


		// 获取区域代表信息
		Map<String, Object> map = new HashMap<>();
		map.put("district", useraddress.getAddress());
		List<UserDo> userLst = userService.selectUserCondition(map);

		if (userLst == null || userLst.size()<1) {
			logger.warn("此区域无代理,结束奖励");
			return;
		}

		// 获取奖励记录
		if (userLst != null) {
			Map<String, Object> maps = gainAward(buyUserId, 0, buyQty);
			// 多种奖励办法以;分隔
			String buyerAward = maps.get("money").toString();
			String[] bAwardLst = buyerAward.split(";");
			oneAward((int) maps.get("userId"), bAwardLst);
		}

		UserDo usertwo = userService.getUser(userLst.get(0).getRefereeid());
		if (usertwo != null) {
			Map<String, Object> maps = gainAward(usertwo.getRefereeid(), 1, buyQty);
			// 多种奖励办法以;分隔
			String buyerAward =  maps.get("money").toString();
			String[] bAwardLst = buyerAward.split(";");
			oneAward(Integer.valueOf(maps.get("userId").toString()), bAwardLst);
		}

	}

	/**
	 * 逐个奖励处理
	 * 
	 * @param buyUserId
	 * @param bAwardLst
	 */
	private void oneAward(int buyUserId, String[] bAwardLst) {
		for (String oneAward : bAwardLst) {
			// 奖励金额
			Integer wardAmount = getAmtByAward(oneAward);

			// 奖励进入那个账户类型
			String accountType ="wallet_money";
			
			if (wardAmount.intValue() > 0) {
				UserAccountDo accont = new UserAccountDo(new BigDecimal(wardAmount), buyUserId, accountType);
				// 账户对象增加金额
				accountService.updateUserAmountById(accont, IncomeType.TYPE_AWARD_BUYER);
			}
		}
	}

	/**
	 * 
	 * @param userId
	 *            区域人id
	 * @param resfor
	 *            区分区域人奖励和推荐人奖励
	 * @return
	 */
	public Map<String, Object> gainAward(Integer userId, int resfor, int count) {
		Map<String, Object> maps = new HashMap<>();
		Regionalawards regions = new Regionalawards();
		if (resfor == 0) {
			Regionalawards region = new Regionalawards();
			region.setAlgebra(0);
			regions = regionalawardsService.selectByPrimaryKeySelective(region).get(0);
			maps.put("userId", userId);
			maps.put("money", getAmtByAward(regions.getRewardbalance()) * count);
		}

		if (resfor == 1) {

			maps = twentyAward(userService.getUser(userId).getRefereeid(), count);
		}
		return maps;

	}


	/**
	 * 根据配置 用 - 分隔 ，获取奖励次数或金额，如果没有配置报错 配置格式： 1-wallet_travel-isFirst-A001-4人港澳游
	 * 表示 1次，旅游账户 奖励 4人港澳游 ， wallet_travel 查看{@link AccountType}
	 * 
	 * @param oneAward
	 * @return
	 */
	private Integer getAmtByAward(String oneAward) {
		String[] awds = oneAward.split("-");
		
		return new Integer(awds[0].trim());
	}

	// 推荐人id count 数量
	public Map<String, Object> twentyAward(Integer id, int count) {
		// 返回值
		Map<String, Object> maps = new HashMap<>();

		try {
			// 查询推荐人信息
			UserDo user = userService.getUser(id);
			if (user == null) {
				throw new BusinessException("该区域的推荐人无推荐人", "error-buyerAward-003");
			}
			// 防止空指针
			if (user != null) {
				// 判断用户是否为城市合伙人
				if (user.getUserLevel() >= 3) {
					Regionalawards region = new Regionalawards();
					region.setAlgebra(1);
					region =  regionalawardsService.selectByPrimaryKeySelective(region).get(0);
					maps.put("userId", user.getId());
					maps.put("money", Integer.valueOf(region.getRewardbalance()) * count);
					return maps;
				} else {
					// 如果该用户没有推荐人，则终止循环不派送20元奖励
					if (user.getRefereeid() == null || user.getRefereeid() == 0) {
						return null;
					}
					// 递归循环，直到找到城市合伙人
					twentyAward(user.getRefereeid(), count);
				}
			}
		} catch (Exception e) {
			logger.error("发放区域20元奖励异常");
		}
		return maps;
	}
}
