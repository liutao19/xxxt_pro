package com.dce.business.service.impl.user;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.DictCode;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.Constants;
import com.dce.business.common.util.DataEncrypt;
import com.dce.business.dao.account.IUserAccountDao;
import com.dce.business.dao.touch.ITouchBonusRecordDao;
import com.dce.business.dao.user.IUserDao;
import com.dce.business.dao.user.IUserParentDao;
import com.dce.business.dao.user.IUserRefereeDao;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.entity.user.UserParentDo;
import com.dce.business.entity.user.UserRefereeDo;
import com.dce.business.service.bonus.IPerformanceDailyService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.third.IEthereumService;
import com.dce.business.service.user.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Resource
    private IUserDao userDao;
    @Resource
    private IUserParentDao userParentDao;
    @Resource
    private IUserRefereeDao userRefereeDao;
    @Resource
    private IUserAccountDao  userAccountDao;
    @Resource
    private ILoanDictService loanDictService;
    @Resource
    private IEthereumService ethereumService;
    @Resource
    private ITouchBonusRecordDao touchBonusRecordDao;
    @Resource
	private IPerformanceDailyService performanceDailyService;
//    @Resource
//    private IBaodanService baodanService;
    
    @Override
    public UserDo getUser(String userName) {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        List<UserDo> list = userDao.selectUser(params);

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public UserDo getUser(Integer userId) {
        return userDao.selectByPrimaryKey(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<?> reg(UserDo userDo) {
    	// 判断注册用户名是否为空
    	userDo.setUserName(userDo.getUserName().trim());    	
    	userDo.setRefereeid(userDo.getRefereeid());// 判断注册用户名是否为空refereeid
    	//userDo.setRefereeUserName(userDo.getRefereeUserName().trim());
     
       /* UserDo ref = getUser(userDo.getRefereeUserName());
        if(ref == null){
            return Result.failureResult("推荐人不存在");
        }*/
     
        UserDo oldUser = getUser(userDo.getUserName());
        if (oldUser != null) {
            return Result.failureResult("用户已存在");
        }
        
        
        /*UserDo par = getUser(userDo.getParentUserName());
        if(par == null){
            return Result.failureResult("接点人不存在!");
        }
        
        //判断所在位置是否已经有用户
        Map<String, Object> params = new HashMap<>();
        params.put("parentid", par.getId());
        params.put("distance", 1);
        params.put("lrDistrict", userDo.getPos());
        List<UserParentDo> list = userParentDao.select(params);
        if (!CollectionUtils.isEmpty(list)) {
            return Result.failureResult("接点人左（右）区已有用户，请重新选择!");
        }*/
        
        String twoPassword = userDo.getTwoPassword(); //加密前交易密码，用来以太坊开户
      /*  userDo.setRefereeid(ref.getId());
        userDo.setParentid(par.getId());*/
        userDo.setRegTime(new Date().getTime());
        userDo.setUserPassword(DataEncrypt.encrypt(userDo.getUserPassword())); //密码加密处理
        userDo.setTwoPassword(DataEncrypt.encrypt(userDo.getTwoPassword()));
        
        //用户注册
        int result = userDao.insertSelective(userDo);

       /* //维护父节点关系
        maintainUserParent(userDo.getId(), par.getId(), userDo.getPos());

        //维护推荐人关系
        maintainUserReferee(userDo.getId(), ref.getId());
*/        
       /* //维护賬戶
        maintainUserAccount(userDo.getId());
        
        //以太坊开户
        ethereumService.creatAccount(userDo.getId(), twoPassword);*/
        
        return result > 0?Result.successResult("注册成功!"):Result.failureResult("注册失败");
    }
    
	//维护賬戶
	private void maintainUserAccount(Integer userId) {
		String[] accountTypes = new String[] { AccountType.wallet_original.name(), AccountType.wallet_original_release.name(),
				AccountType.wallet_bonus.name(), AccountType.wallet_interest.name(),
				AccountType.wallet_release_release.name(), AccountType.wallet_cash.name(), AccountType.wallet_score.name() };

		for (String accountType : accountTypes) {
			UserAccountDo record = new UserAccountDo();
			record.setUserId(userId);
			record.setAccountType(accountType);
			record.setAmount(BigDecimal.ZERO);
			record.setUpdateTime(new Date());
			userAccountDao.insert(record);
		}
	}

	/**
     * 维护父节点关系表 
     * @param userId
     * @param parentId  
     */
    private void maintainUserParent(Integer userId, Integer parentId, Byte lr) {
        //1、接点人的下级人数加一
        userDao.addSonNumber(parentId);

        //2、直接父级
        UserParentDo userParentDo = new UserParentDo();
        userParentDo.setParentid(parentId);
        userParentDo.setUserid(userId);
        userParentDo.setDistance(1);
        userParentDo.setPosition(lr.toString());
        userParentDo.setNetwork(null); 
        userParentDo.setLrDistrict(lr);
        userParentDao.insertSelective(userParentDo);

        //3、间接父级
        Map<String, Object> params = new HashMap<>();
        params.put("userid", parentId);
        List<UserParentDo> list = userParentDao.select(params);
        for (UserParentDo temp : list) {
            UserParentDo up = new UserParentDo();
            up.setUserid(userId);
            up.setParentid(temp.getParentid());
            up.setDistance(temp.getDistance() + 1);
            up.setPosition(getPosition(temp.getPosition(), lr));
            up.setNetwork(null);
            up.setLrDistrict(temp.getLrDistrict());
            userParentDao.insertSelective(up);
            userDao.addSonNumber(temp.getParentid()); //接点人的下级人数加一
        }
    }
    
    /**
     * 查询所属位置 
     * position=(parentPosition-1)*2 + lr
     * @param parentPosition
     * @param lr
     * @return  
     */
    private String getPosition(String parentPosition, Byte lr) {
        try {
            Integer position = (Integer.valueOf(parentPosition) - 1) * 2 + lr;
            return position.toString();
        } catch (Exception e) {
            logger.error("position计算异常：", e);
        }

        return "0";
    }

    /**
     * 维护推荐人关系表 
     * @param userId
     * @param parentId  
     */
    private void maintainUserReferee(Integer userId, Integer refereeId) {
        //1、推荐人的推荐人数加一,没有激活的时候不累计，报单才累计
        //userDao.addRefereeNumber(refereeId);

        //2、直接推荐人
        UserRefereeDo userRefereeDo = new UserRefereeDo();
        userRefereeDo.setUserid(userId);
        userRefereeDo.setRefereeid(refereeId);
        userRefereeDo.setDistance(1); //直接推荐人
        userRefereeDao.insertSelective(userRefereeDo);

        //3、间接推荐人
        Map<String, Object> params = new HashMap<>();//userid
        params.put("userid", refereeId);
        List<UserRefereeDo> list = userRefereeDao.select(params);
        for (UserRefereeDo temp : list) {
            UserRefereeDo ur = new UserRefereeDo();
            ur.setUserid(userId);
            ur.setRefereeid(temp.getRefereeid()); //间接推荐人
            ur.setDistance(temp.getDistance() + 1);
            userRefereeDao.insertSelective(ur);
        }
    }

    @Override
    public List<UserDo> list(String userName) {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        return userDao.list(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateStatic(BigDecimal staticAmount, int userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("staticAmount", staticAmount);
        userDao.updateStatic(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateAllPerformance(BigDecimal staticAmount, Integer userId) {
        //查询所有的父节点
        Map<String, Object> params = new HashMap<>();
        params.put("userid", userId);
        List<UserParentDo> list = userParentDao.select(params);

        //更新自己的
        updatePerformance(staticAmount, userId);

        //更新父节点
        for (UserParentDo parent : list) {
            updatePerformance(staticAmount, parent.getParentid());
        }
    }

    private void updatePerformance(BigDecimal performance, int userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("performance", performance);
        userDao.updatePerformance(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateTouched(BigDecimal touchedAmount, int userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("touchedAmount", touchedAmount);
        userDao.updateTouched(params);
    }

    @Override
    public List<UserDo> selectUser(Map<String, Object> params) {
        return userDao.selectUser(params);
    }

    @Override
    public Result<?> update(UserDo userDo) {
        if (userDo == null || userDo.getId() == null) {
            return Result.failureResult("修改用户信息参数错误!");
        }
       
        int flag = userDao.updateByPrimaryKeySelective(userDo);
        if (flag > 0) {
            return Result.successResult("修改成功");
        } else {

            return Result.failureResult("修改失败");
        }
    }

    /**
     * 查看团队成员
     */
    @Override
    public List<Map<String, Object>> getMyMember(Map<String,Object> params) {
    	
        return userRefereeDao.selectMyGroup(params);
    }

    /**
     * 此方法注释，重写， 2018-07-08
     */
    /*
    @SuppressWarnings("unchecked")
	@Override
    public List<Map<String, Object>> listMyOrg(Integer userId,int level) {
    	List<Map<String, Object>> ret =  userParentDao.listMyOrg(userId,level);
    	if(CollectionUtils.isEmpty(ret)){
    		return Collections.EMPTY_LIST;
    	}
    	
    	BigDecimal leftPerformance = BigDecimal.ZERO;
    	BigDecimal rightPerformance = BigDecimal.ZERO;
    	
    	//填充左右业绩
    	for(Map<String,Object> m : ret){
    		Long recordUserId = (Long)m.get("id");
    		
    		calPerformance(recordUserId, m);
    		
    		//统计root节点左右业绩
    		if(Integer.parseInt(m.get("id").toString()) != userId.intValue() && "2".equals(m.get("lr_district").toString())){  //右区
    			
    			rightPerformance = rightPerformance.add((BigDecimal) m.get("lr_amount"));
    			rightPerformance = rightPerformance.add((BigDecimal) m.get("lf_amount"));
    			rightPerformance = rightPerformance.add((BigDecimal) m.get("baodan_amount")).setScale(2, RoundingMode.HALF_DOWN);
    		}
    		if(Integer.parseInt(m.get("id").toString()) != userId.intValue() && "1".equals(m.get("lr_district").toString())){  //左区
    			
    			leftPerformance = leftPerformance.add((BigDecimal) m.get("lr_amount"));
    			leftPerformance = leftPerformance.add((BigDecimal) m.get("lf_amount"));
    			leftPerformance = leftPerformance.add((BigDecimal) m.get("baodan_amount")).setScale(2, RoundingMode.HALF_DOWN);
    		}
    	}
    	
		
    	for(Map<String,Object> m : ret){
    		Long recordUserId = (Long)m.get("id");
    		
    		if(recordUserId.intValue() == userId.intValue()){
    			m.put("lf_amount", leftPerformance);
    			m.put("lr_amount", rightPerformance);
    			break;
    		}
    	}
    	return ret;
    }
    

	private void calPerformance(Long userId, Map<String, Object> m) {
		BigDecimal todayPerformance = BigDecimal.ZERO;
		BigDecimal totalPerformance = BigDecimal.ZERO;
		BigDecimal leftPerformance = BigDecimal.ZERO;
		BigDecimal rightPerformance = BigDecimal.ZERO;
		BigDecimal todayLeftPerformance = BigDecimal.ZERO;
		BigDecimal todayRightPerformance = BigDecimal.ZERO;
		try {
			//总业绩
			TouchBonusRecordDo touchBonusRecordDo = touchBonusRecordDao.getUserTouchBonusRecord(userId.intValue());
			if (touchBonusRecordDo != null) {
				if (touchBonusRecordDo.getBalanceLeft() != null) {
					totalPerformance = totalPerformance.add(touchBonusRecordDo.getBalanceLeft());
					leftPerformance = leftPerformance.add(touchBonusRecordDo.getBalanceLeft());
				}
				if (touchBonusRecordDo.getBalanceRight() != null) {
					totalPerformance = totalPerformance.add(touchBonusRecordDo.getBalanceRight());
					rightPerformance = rightPerformance.add(touchBonusRecordDo.getBalanceRight());
				}
			}

			//当日业绩
			PerformanceDailyDo performanceDailyDo = performanceDailyService.getPerformanceDaily(userId.intValue(), new Date());
			if (performanceDailyDo != null) {
				if (performanceDailyDo.getBalance() != null) {
					todayPerformance = todayPerformance.add(performanceDailyDo.getBalance());
				}
				if (performanceDailyDo.getBalance_left() != null) {
					todayLeftPerformance = todayLeftPerformance.add(performanceDailyDo.getBalance_left());
				}
				if (performanceDailyDo.getBalance_right() != null) {
					todayRightPerformance = todayRightPerformance.add(performanceDailyDo.getBalance_right());
				}
			}
		} catch (Exception e) {
			logger.error("统计总业绩异常：", e);
		}
		m.put("today_performance", todayPerformance);
		m.put("total_performance", totalPerformance);
		m.put("lf_amount", leftPerformance);
		m.put("lr_amount", rightPerformance);
		m.put("today_lf_amount", todayLeftPerformance);
		m.put("today_lr_amount", todayRightPerformance);
	}
*/
    @SuppressWarnings("unchecked")
   	@Override
       public List<Map<String, Object>> listMyOrg(Integer userId,int level) {
       	List<Map<String, Object>> ret =  userParentDao.listMyOrg(userId,level);
       	if(CollectionUtils.isEmpty(ret)){
       		return Collections.EMPTY_LIST;
       	}
       	
//       	BigDecimal leftPerformance = BigDecimal.ZERO;
//       	BigDecimal rightPerformance = BigDecimal.ZERO;
       	
       	//填充左右业绩
       	for(Map<String,Object> m : ret){
       		Long recordUserId = (Long)m.get("id");
       		getyj(m, 1, recordUserId);  //统计左区总业绩
       		getyj(m, 2, recordUserId);  //统计右区总业绩
       		
       		getTodayyj(m, 1, recordUserId);  //统计左区总业绩
       		getTodayyj(m, 2, recordUserId);  //统计右区总业绩
       		
       	}
       	return ret;
       }

	private void getyj(Map<String, Object> m, int lr, Long recordUserId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentid", recordUserId); 
		params.put("lr_district", lr);
		//左右业绩
		BigDecimal yj = BigDecimal.ZERO;
		Map<String, Object> yjMap = userParentDao.getYJ(params);
		if (null != yjMap && !yjMap.isEmpty()) {
			yj = (BigDecimal) yjMap.get("yj");
		}
		
		if(yj == null){
			yj = BigDecimal.ZERO;
		}

		yj = yj.setScale(2, RoundingMode.HALF_UP);
		if (1 == lr) {
			m.put("lf_amount", yj);
		} else {
			m.put("lr_amount", yj);
		}

		BigDecimal totalPerformance = BigDecimal.ZERO;
		if (m.get("total_performance") != null) {
			totalPerformance = (BigDecimal) m.get("total_performance");
		}
		
		totalPerformance = totalPerformance.add(yj).setScale(2, RoundingMode.HALF_UP);
		m.put("total_performance", totalPerformance); //总业绩
	}
       
	/**
	 * 计算今日业绩
	 * @param m
	 * @param lr
	 * @param recordUserId
	 */
	private void getTodayyj(Map<String, Object> m, int lr, Long recordUserId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentid", recordUserId); 
		params.put("lr_district", lr);
		//左右业绩
		BigDecimal yj = BigDecimal.ZERO;
		Map<String, Object> yjMap = userParentDao.getTodayYJ(params);
		if (null != yjMap && !yjMap.isEmpty()) {
			yj = (BigDecimal) yjMap.get("yj");
		}


		yj = yj.setScale(2, RoundingMode.HALF_UP);
		if (1 == lr) {
			m.put("today_lf_amount", yj);
		} else {
			m.put("today_lr_amount", yj);
		}

		BigDecimal totalPerformance = BigDecimal.ZERO;
		if (m.get("today_performance") != null) {
			totalPerformance = (BigDecimal) m.get("today_performance");
		}
		
		totalPerformance = totalPerformance.add(yj).setScale(2, RoundingMode.HALF_UP);
		m.put("today_performance", totalPerformance); //总业绩
	}
	

//   	private void calPerformance(Long userId, Map<String, Object> m) {
//   		BigDecimal todayPerformance = BigDecimal.ZERO;
//   		BigDecimal totalPerformance = BigDecimal.ZERO;
//   		BigDecimal leftPerformance = BigDecimal.ZERO;
//   		BigDecimal rightPerformance = BigDecimal.ZERO;
//   		BigDecimal todayLeftPerformance = BigDecimal.ZERO;
//   		BigDecimal todayRightPerformance = BigDecimal.ZERO;
//   		try {
//   			//总业绩
//   			TouchBonusRecordDo touchBonusRecordDo = touchBonusRecordDao.getUserTouchBonusRecord(userId.intValue());
//   			if (touchBonusRecordDo != null) {
//   				if (touchBonusRecordDo.getBalanceLeft() != null) {
//   					totalPerformance = totalPerformance.add(touchBonusRecordDo.getBalanceLeft());
//   					leftPerformance = leftPerformance.add(touchBonusRecordDo.getBalanceLeft());
//   				}
//   				if (touchBonusRecordDo.getBalanceRight() != null) {
//   					totalPerformance = totalPerformance.add(touchBonusRecordDo.getBalanceRight());
//   					rightPerformance = rightPerformance.add(touchBonusRecordDo.getBalanceRight());
//   				}
//   			}
//
//   			//当日业绩
//   			PerformanceDailyDo performanceDailyDo = performanceDailyService.getPerformanceDaily(userId.intValue(), new Date());
//   			if (performanceDailyDo != null) {
//   				if (performanceDailyDo.getBalance() != null) {
//   					todayPerformance = todayPerformance.add(performanceDailyDo.getBalance());
//   				}
//   				if (performanceDailyDo.getBalance_left() != null) {
//   					todayLeftPerformance = todayLeftPerformance.add(performanceDailyDo.getBalance_left());
//   				}
//   				if (performanceDailyDo.getBalance_right() != null) {
//   					todayRightPerformance = todayRightPerformance.add(performanceDailyDo.getBalance_right());
//   				}
//   			}
//   		} catch (Exception e) {
//   			logger.error("统计总业绩异常：", e);
//   		}
//   		m.put("today_performance", todayPerformance);
//   		m.put("total_performance", totalPerformance);
//   		m.put("lf_amount", leftPerformance);
//   		m.put("lr_amount", rightPerformance);
//   		m.put("today_lf_amount", todayLeftPerformance);
//   		m.put("today_lr_amount", todayRightPerformance);
//   	}
    
    @Override
    public List<Map<String, Object>> listMyRef(Integer userId, int startRow,
            int pageSize) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("refereeId", userId);
        params.put("startRow", startRow);
        params.put("pageSize", pageSize); 
        params.put("distance", 1);
        return userRefereeDao.listMyRef(params);
    }

    @Override
    public List<UserDo> selectPage(Map<String, Object> params) {
        return userDao.selectFenYe(params);
    }

	@Override
	public Result<?> setUserLevel(String userCode, String userLevel) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userName", userCode);
		List<UserDo> users = userDao.selectUser(params);
		if(CollectionUtils.isEmpty(users)){
			throw new BusinessException("用户" + userCode + "不存在");
		}
		if(users.size() > 1){
			throw new BusinessException("用户名" + userCode + "存在多个");
		}
		
		List<LoanDictDtlDo> listDtl = loanDictService.queryDictDtlListByDictCode(DictCode.KHJB.getCode());
		if(CollectionUtils.isEmpty(listDtl)){
			throw new BusinessException("用户级别未设置!");
		}
		//判断级别是否在库中存在
		boolean haveIn = false;
		for(LoanDictDtlDo dtl : listDtl){
			if(userLevel.equals(dtl.getCode())){
				haveIn = true;
				break;
			}
		}
		if(!haveIn){
			throw new BusinessException("用户级别不存在!");
		}
		
		UserDo user = users.get(0);
		//修改用户的等级
		UserDo _user = new UserDo();
		_user.setId(user.getId());
		_user.setUserLevel(Byte.parseByte(userLevel));
		_user.setActivationTime(new Date().getTime());
		_user.setIsEmpty(new Byte("1")); //1 空单 ； 0 实单
		userDao.updateByPrimaryKeySelective(_user);
		//修改推荐人的推荐数
		//userDao.addRefereeNumber(user.getRefereeid());
		return Result.successResult("修改成功!");
	}

	@Override
	public PageDo<UserDo> selectUserByPage(PageDo<UserDo> page,
			Map<String, Object> params) {
		
		if(params == null){
			params = new HashMap<String, Object>();
		}
        params.put(Constants.MYBATIS_PAGE, page);
        List<UserDo> list = userDao.selectByPage(params);
        page.setModelList(list);
		return page;
	}

	@Override
	public PageDo<UserDo> selectEthAccountByPage(PageDo<UserDo> page, Map<String, Object> params) {
		if(params == null){
			params = new HashMap<String,Object>();
		}
		params.put(Constants.MYBATIS_PAGE, page);
		List<UserDo> list = userDao.selectEthAccountByPage(params);
		page.setModelList(list);
		return page;
	}

	@Override
	public Long selectBaoDanAmount(Map<String, Object> params) {
		return userDao.selectBaoDanAmount(params);
	}

}
