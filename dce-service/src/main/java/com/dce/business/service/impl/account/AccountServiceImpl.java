package com.dce.business.service.impl.account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.CurrencyType;
import com.dce.business.common.enums.DictCode;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.DataDecrypt;
import com.dce.business.common.util.DataEncrypt;
import com.dce.business.common.util.DateUtil;
import com.dce.business.common.util.PayUtil;
import com.dce.business.dao.account.IUserAccountDao;
import com.dce.business.dao.account.IUserAccountDetailDao;
import com.dce.business.dao.etherenum.IEthereumTransInfoDao;
import com.dce.business.dao.trade.IWithdrawalsDao;
import com.dce.business.dao.user.IUserDao;
import com.dce.business.dao.user.IUserParentDao;
import com.dce.business.entity.account.UserAccountDetailDo;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.dict.CtCurrencyDo;
import com.dce.business.entity.dict.LoanDictDo;
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.entity.etherenum.EthAccountPlatformDo;
import com.dce.business.entity.etherenum.EthereumAccountDo;
import com.dce.business.entity.etherenum.EthereumTransInfoDo;
import com.dce.business.entity.trade.WithdrawalsDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.entity.user.UserParentDo;
import com.dce.business.entity.user.UserStaticDo;
import com.dce.business.entity.user.UserStaticDo.StaticType;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.dict.ICtCurrencyService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.third.IEthereumPlatformService;
import com.dce.business.service.third.IEthereumService;
import com.dce.business.service.user.IUserService;
import com.dce.business.service.user.IUserStaticService;

/** 
 * 用户资金账户 
 * @author parudy
 * @date 2018年3月25日 
 * @version v1.0
 */
@Service("accountService")
public class AccountServiceImpl implements IAccountService {
    private static Logger logger = Logger.getLogger(AccountServiceImpl.class);
    @Resource
    private IUserAccountDao userAccountDao;
    @Resource
    private IUserAccountDetailDao userAccountDetailDao;
    @Resource
    private IUserDao userDao;
    @Resource
    private IUserParentDao userParentDao;
    @Resource
    private IUserStaticService userStaticService;
    @Resource
    private IUserService userService;
    @Resource
    private ILoanDictService loanDictService;
    @Resource
    private IEthereumService ethereumService;
    @Resource
    private IEthereumTransInfoDao ethereumTransInfoDao;
    @Resource
    private IEthereumPlatformService ethereumPlatformService;
    
    @Resource
    private IWithdrawalsDao withdrawDao;
   

    @Value("#{sysconfig['huishang.openAccount.url']}")
    private String ethereum_blance_url;

    @Resource
    private ICtCurrencyService ctCurrencyService;

    
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean createAccount(UserAccountDo userAccountDo) {
        userAccountDo.setUpdateTime(new Date());

        int i = userAccountDao.insertSelective(userAccountDo);
        return i > 0;
    }

    @Override
    public UserAccountDo getUserAccount(Integer userId, AccountType accountType) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("accountType", accountType.getAccountType());
        List<UserAccountDo> list = userAccountDao.selectAccount(params);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        return list.get(0);
    }

    //1、原始仓账户；2、美元点账户
    @Override
    public UserAccountDo selectUserAccount(Integer userId, String accountType) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("accountType", accountType);

        List<UserAccountDo> list = userAccountDao.selectAccount(params);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        return list.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public int updateUserAmountById(UserAccountDo userAccountDo, IncomeType type) {
        BigDecimal amount = userAccountDo.getAmount();
        
        //金额为0时不需要对账户变更操作，不需要记流水
        if (BigDecimal.ZERO.compareTo(amount) == 0) {
            return 0;
        }
        
        //增加用户收益、消费金额
        if (amount.compareTo(BigDecimal.ZERO) >= 0) {
            userAccountDo.setConsumeAmount(BigDecimal.ZERO);
            userAccountDo.setIncomeAmount(amount);
        } else {
            userAccountDo.setIncomeAmount(BigDecimal.ZERO);
            userAccountDo.setConsumeAmount(amount.negate());
        }

        Integer userId = userAccountDo.getUserId();
        String accountType = userAccountDo.getAccountType();
        //判断用户是否存在此帐户 没有刚增加
        UserAccountDo udo = selectUserAccount(userId, accountType);
        int result = 0;
        userAccountDo.setUpdateTime(new Date());
        if (null == udo && amount.compareTo(BigDecimal.ZERO) >= 0) {
            result = userAccountDao.insertSelective(userAccountDo);
        } else {
            result = userAccountDao.updateUserAmountById(userAccountDo);
        }

        if (result < 1) {
            throw new BusinessException("余额不足");
        }

        updateUserAmountDetail(amount, type, userId, accountType, userAccountDo.getRemark(),userAccountDo.getSeqId());

        return result;
    }

    /**
     * 更新用户明细贪信息
     * 
     * @param amount
     * @param useType
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private boolean updateUserAmountDetail(BigDecimal amount, 
    									   IncomeType type, 
    									   Integer userId, 
    									   String accountType, 
    									   String remark,
    									   String seqId) {
		//读取账户余额
    	Map<String, Object> params = new HashMap<String,Object>();
    	params.put("userId", userId);
    	params.put("accountType", accountType);
    	List<UserAccountDo> accounts = userAccountDao.selectAccount(params);
    	BigDecimal balance = accounts.get(0).getAmount();
    	
    	
    	// 增加用户消费列表
        UserAccountDetailDo uaDetail = new UserAccountDetailDo();
        uaDetail.setAmount(amount);
        uaDetail.setCreateTime(new Date());
        uaDetail.setIncomeType(type.getIncomeType());
        uaDetail.setMoreOrLess(moreOrLessStr(amount));
        uaDetail.setAccountType(accountType);
        uaDetail.setBalanceAmount(balance);
        uaDetail.setSeqId(seqId);
        if (StringUtils.isBlank(remark)) {
            uaDetail.setRemark(type.getRemark());
        } else {
            uaDetail.setRemark(remark);
        }
        uaDetail.setUserId(userId);
        userAccountDetailDao.addUserAccountDetail(uaDetail);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void convertBetweenAccount(Integer sourceUserId, Integer targetUserId, BigDecimal amount, String fromAccount, String toAccount,
            IncomeType sourceMsg, IncomeType targetMsg) {

        convertBetweenAccount(sourceUserId, targetUserId, amount, fromAccount, toAccount, sourceMsg, targetMsg, amount);
    }

    //@Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void convertBetweenAccount(Integer sourceUserId, Integer targetUserId, BigDecimal amount, String fromAccount, String toAccount,
            IncomeType sourceMsg, IncomeType targetMsg, BigDecimal addAmount) {
    	UserDo userDo =  this.userDao.selectByPrimaryKey(targetUserId);
    	String seqId = userDo == null? null :(userDo.getTrueName()+":"+userDo.getUserName());
    	userAccountDetailDao.addAccountTranLog(seqId,sourceUserId,targetUserId,amount,fromAccount,toAccount,sourceMsg);
        UserAccountDo source = new UserAccountDo();
        source.setUserId(sourceUserId);
        source.setAmount(amount.negate());
        source.setAccountType(fromAccount);
        source.setSeqId(seqId);
        this.updateUserAmountById(source, sourceMsg);

        UserAccountDo target = new UserAccountDo();
        target.setUserId(targetUserId);
        target.setAmount(addAmount);
        target.setAccountType(toAccount);
        target.setSeqId(seqId);
        this.updateUserAmountById(target, targetMsg);

    }

    private String moreOrLessStr(BigDecimal amount) {
        String type = "+";
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            type = "-";
        }

        return type;

    }

    @Override
    public List<UserAccountDetailDo> selectUserAccountDetail(Map<String, Object> params) {
        return userAccountDetailDao.selectUserAccountDetail(params);
    }

    @Override
    public Result<?> currentInit(Integer userId,AccountType type) {
        if (userId == null) {
            return Result.failureResult("用户ID为空!");
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();

        UserDo user = userDao.selectByPrimaryKey(userId);
        resultMap.put("userName", user.getUserName());
        resultMap.put("userLevelName", StringUtils.isBlank(user.getUserLevelName()) ? user.getUserLevel() : user.getUserLevelName());
        UserAccountDo account = selectUserAccount(userId, type.getAccountType());
        if (account != null) {
            resultMap.put("amount", account.getAmount());
        } else {

            resultMap.put("amount", "0.0");
        }
        //查询报单等级及等级额度范围
        List<LoanDictDtlDo> dtlList = loanDictService.queryDictDtlListByDictCode(DictCode.BaoDanFei.getCode());

        resultMap.put("userLeves", dtlList);

        return Result.successResult("查询成功!", resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<?> currentAddMoney(Integer userId, BigDecimal qty) {
    	
    	
    	LoanDictDo loanDict = loanDictService.getLoanDict("baodanOnOff");
    	if(loanDict != null && "off".equalsIgnoreCase(loanDict.getRemark())){
             throw new BusinessException("停止加金");
    	}
    	
    	
        if (userId == null || qty == null) {
            return Result.failureResult("参数错误,原始仓加金失败!");
        }
        //先查询当前用户现持仓账户中的额度
        UserAccountDo c_account = selectUserAccount(userId, AccountType.current.name());
        if (c_account == null) {
            return Result.failureResult("用户还未报单,原始仓加金失败!");
        }

        if (c_account.getAmount().compareTo(qty) < 0) {
            return Result.failureResult("当前现持仓额度【" + c_account.getAmount() + "】小于要加金的额度【" + qty + "】");
        }

        BigDecimal originalQty = calJiaJin(userId, qty);
        //原始仓账户添加
        UserAccountDo userAccountDo = new UserAccountDo();
        userAccountDo.setUserId(userId);
        userAccountDo.setAmount(originalQty);
        userAccountDo.setAccountType(AccountType.original.getAccountType());
        updateUserAmountById(userAccountDo, IncomeType.TYPE_AWARD_JIAJIN);

        //现持仓账户减少
        userAccountDo = new UserAccountDo();
        userAccountDo.setUserId(userId);
        userAccountDo.setAmount(qty.negate());
        userAccountDo.setAccountType(AccountType.current.getAccountType());
        updateUserAmountById(userAccountDo, IncomeType.TYPE_AWARD_JIAJIN);

        UserStaticDo userStatic = new UserStaticDo();
        userStatic.setUserid(userId);
        userStatic.setMoney(calMoney(originalQty));
        userStatic.setStatus((byte) 1);
        userStatic.setType(StaticType.JiaJin.getType());
        userStatic.setTotalmoney(originalQty);
        userStatic.setEndDate(DateUtil.getDate(new Date(), 365)); //365天结束
        userStaticService.insert(userStatic);

        return Result.successResult("加金成功");
    }

    /**
     * 按照用户等级计算加金产能 
     * @param userId
     * @param qty
     * @return  
     */
    private BigDecimal calJiaJin(Integer userId, BigDecimal qty) {
        UserDo userDo = userService.getUser(userId);
        LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.JiaJin.getCode(), userDo.getUserLevel().toString());
        Assert.notNull(loanDictDtlDo, "加金产能未配置");
        Assert.hasText(loanDictDtlDo.getRemark(), "加金产能未配置");

        BigDecimal jiajinBonus = qty.multiply(new BigDecimal(loanDictDtlDo.getRemark())).setScale(6, RoundingMode.HALF_UP);
        return jiajinBonus;
    }

    /** 
     * 计算每日返利
     * @param totalMoney
     * @return  
     */
    private BigDecimal calMoney(BigDecimal totalMoney) {
        return totalMoney.divide(new BigDecimal("365"), 6, RoundingMode.HALF_UP);
    }

   

	/**
     * 按照用户等级计算复投产能 
     * @param userId
     * @param qty
     * @return  
     */
//    private BigDecimal calBaodan(Integer userId, BigDecimal qty) {
//        UserDo userDo = userService.getUser(userId);
//        return calBaodanByLevel(String.valueOf(userDo.getUserLevel()),qty);
//    }

    /**
     * 按照用户等级计算复投产能 
     * @param userId
     * @param qty
     * @return  
     */
/*    private BigDecimal calBaodanByLevel(String level, BigDecimal qty) {
        LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.BaoDanZengSong.getCode(), level);
        Assert.notNull(loanDictDtlDo, "报单赠送未配置");
        Assert.hasText(loanDictDtlDo.getRemark(), "报单赠送未配置");

        BigDecimal baodanBonus = qty.multiply(new BigDecimal(loanDictDtlDo.getRemark())).setScale(6, RoundingMode.HALF_UP);
        return baodanBonus;
    }*/
    
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<?> pointOut(Integer userId, BigDecimal qty, String receiver) {
       
    	
    	Map<String, Object> params = new HashMap<>();
        params.put("userName", receiver);
        List<UserDo> receiverList = userDao.selectUser(params);
        if (CollectionUtils.isEmpty(receiverList)) {
            return Result.failureResult("未查询到接收人");
        }
        if (receiverList.size() > 1) {
            return Result.failureResult("根据编号查询到多个接收人,不能确定具体接收人!");
        }

        UserDo receiverUser = receiverList.get(0);

        //判断用户和接受用户是否在转出的范围
        UserDo sourceUser = userDao.selectByPrimaryKey(userId);
        if(sourceUser.getTranDirect() != null && sourceUser.getTranDirect().contains("up,down")){
        	Map<String, Object> parentParams = new HashMap<String,Object>();
        	parentParams.put("userid", userId);
        	parentParams.put("parentid", receiverUser.getId());
			List<UserParentDo> relationLst = userParentDao.select(parentParams );
			if(CollectionUtils.isEmpty(relationLst)){
				parentParams.put("userid", receiverUser.getId());
	        	parentParams.put("parentid", userId);
	        	relationLst = userParentDao.select(parentParams );
			}
			
			if(CollectionUtils.isEmpty(relationLst)){
				return Result.failureResult("转出受限，接受人不是上下级关系");
			}
        	
        }else if(sourceUser.getTranDirect() != null &&  sourceUser.getTranDirect().contains("up")){
        	Map<String, Object> parentParams = new HashMap<String,Object>();
        	parentParams.put("userid", userId);
        	parentParams.put("parentid", receiverUser.getId());
        	List<UserParentDo> relationLst=userParentDao.select(parentParams );
			if(CollectionUtils.isEmpty(relationLst)){
				return Result.failureResult("转出受限，接受人不是上级");
			}
        	
        }else if(sourceUser.getTranDirect() != null &&  sourceUser.getTranDirect().contains("down")){
        	Map<String, Object> parentParams = new HashMap<String,Object>();
        	parentParams.put("userid", receiverUser.getId());
        	parentParams.put("parentid", userId);
        	userParentDao.select(parentParams );
        	List<UserParentDo> relationLst=userParentDao.select(parentParams );
			if(CollectionUtils.isEmpty(relationLst)){
				return Result.failureResult("转出受限，接受人不是下级");
			}
        }
        
        //查询转出用户的账号并判断账户中是否有足够美元点
        UserAccountDo account = selectUserAccount(userId, AccountType.point.getAccountType());
        if (account == null) {
            return Result.failureResult("未查询到转出用户的美元点账户");
        }

        if (account.getAmount().compareTo(qty) < 0) {
            return Result.failureResult("当前用户美元点账户余额不足以扣取转出的额度!");
        }
        try {
            convertBetweenAccount(userId, receiverUser.getId(), qty, AccountType.point.getAccountType(), AccountType.point.getAccountType(),
                    IncomeType.TYPE_POINT_OUT, IncomeType.TYPE_POINT_IN);

            return Result.successResult("美元点转出成功");
        } catch (Exception e) {
            logger.error("美元点转出报错:" + e);
            throw e;
        }
    }

    @Override
    public BigDecimal getEthernumAmount(Integer userId) {
        BigDecimal ethereumAmount = BigDecimal.ZERO;
        EthereumAccountDo ethereumAccountDo = ethereumService.getByUserId(userId);
        if (ethereumAccountDo != null && StringUtils.isNotBlank(ethereumAccountDo.getAccount())) {
            Map<String, String> map = ethereumService.getBalance(ethereumAccountDo.getAccount());
            if (map != null) {
                ethereumAmount = new BigDecimal(map.get("balance"));
            }
        }
        return ethereumAmount;
    }

    @Async
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<?> recharge(Integer userId, String password, BigDecimal qty) {
    	
    	//判断系统设置是否可交易
		CtCurrencyDo ct = ctCurrencyService.selectByName(CurrencyType.DCE.name());
		if(ct == null || ct.getIs_ctstatus() == null || 0 == ct.getIs_ctstatus().intValue()){
			logger.info("充币提币状态设置关闭,当前不允许充币提币....");
			return Result.failureResult("当前系统不允许充币提币!");
		}
    			
        //1、校验用户金额是否足够
        BigDecimal ethereumAmount = getEthernumAmount(userId);
        if (qty.compareTo(ethereumAmount) > 0) {
            return Result.failureResult("以太坊账户余额不足");
        }

        UserDo userDo = userDao.selectByPrimaryKey(userId);
        //2、校验密码是否正确
        password = DataEncrypt.encrypt(password);
        if (!userDo.getTwoPassword().equals(password)) {
            return Result.failureResult("交易密码不正确");
        }
        
        //3、调用以太坊接口，异步
        EthereumAccountDo userAccount = ethereumService.getByUserId(userId);
        EthAccountPlatformDo platAccount = ethereumPlatformService.getPlatformAccount();
        Result<?> result = trans(userAccount, platAccount, qty, eth2Point(qty), 1,null);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<?> withdraw(Integer userId, String password, BigDecimal qty) {
    	
    	//判断系统设置是否可交易
		CtCurrencyDo ct = ctCurrencyService.selectByName(CurrencyType.DCE.name());
		if(ct == null || ct.getIs_ctstatus() == null || 0 == ct.getIs_ctstatus().intValue()){
			logger.info("充币提币状态设置关闭,当前不允许充币提币....");
			return Result.failureResult("当前系统不允许充币提币!");
		}
		
        //1、校验用户金额是否足够
        UserAccountDo account = getUserAccount(userId, AccountType.point);
        if (account == null || account.getAmount() == null || qty.compareTo(account.getAmount()) > 0) {
            return Result.failureResult("美元点账户余额不足");
        }

        UserDo userDo = userDao.selectByPrimaryKey(userId);
        //2、校验密码是否正确
        password = DataEncrypt.encrypt(password);
        if (!userDo.getTwoPassword().equals(password)) {
            return Result.failureResult("交易密码不正确");
        }
        
        //以太坊账户校验
        EthereumAccountDo ethereumAccountDo = ethereumService.getByUserId(userId);
        if (ethereumAccountDo == null) {
            return Result.failureResult("请先获取以太坊地址再提现");
        }
        
        //判断开户是否已有24小时
        if (ethereumAccountDo.getCreatetime() != null) {
            Date date = DateUtil.getDate(ethereumAccountDo.getCreatetime(), 1); 
            if (date.after(new Date())) {
                return Result.failureResult("获取以太坊地址24小时以后才可以提现");
            }
        }


        //3、扣减美元点账户
        UserAccountDo updateAccount = new UserAccountDo();
        updateAccount.setAccountType(AccountType.point.getAccountType());
        updateAccount.setAmount(qty.negate());
        updateAccount.setUserId(userId);
        updateUserAmountById(updateAccount, IncomeType.TYPE_WITHDRAW);
                
		//写提现表
        WithdrawalsDo record = new  WithdrawalsDo();
        record.setUserid(userId);
        record.setProcessStatus("1");
        record.setAmount(qty);
        record.setWithdrawDate((new Date()).getTime()/1000);
        withdrawDao.insertSelective(record );

        return Result.successResult("提现申请成功");
    }
    
    /**
     * 审批提现
     */
    @Async
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<?> withdraw(Integer withdrawId,Integer userId, BigDecimal qty) {
    	
    	//判断系统设置是否可交易
		CtCurrencyDo ct = ctCurrencyService.selectByName(CurrencyType.DCE.name());
		if(ct == null || ct.getIs_ctstatus() == null || 0 == ct.getIs_ctstatus().intValue()){
			logger.info("充币提币状态设置关闭,当前不允许充币提币....");
			return Result.failureResult("当前系统不允许充币提币!");
		}
		
		//以太坊账户校验
        EthereumAccountDo ethereumAccountDo = ethereumService.getByUserId(userId);
        if (ethereumAccountDo == null) {
            return Result.failureResult("请先获取以太坊地址再提现");
        }
        
        //判断开户是否已有24小时
        if (ethereumAccountDo.getCreatetime() != null) {
            Date date = DateUtil.getDate(ethereumAccountDo.getCreatetime(), 1); 
            if (date.after(new Date())) {
                return Result.failureResult("获取以太坊地址24小时以后才可以提现");
            }
        }

		
        //4、调用以太坊接口，异步
        EthereumAccountDo userAccount = ethereumService.getByUserId(userId);
        EthAccountPlatformDo platAccount =  ethereumPlatformService.getPlatformAccount();
        Result<?> result = trans(userAccount, platAccount, point2Eth(qty), qty, 2,withdrawId);

        return result;
    }

    private Result<?> trans(EthereumAccountDo userAccount, EthAccountPlatformDo platAccount, BigDecimal amount, BigDecimal pointamount, Integer type,Integer withdrawId) {
        String fromAccount = null;//转出账号
        String toAccount = null; //转入账号
        String password = null; //交易密码
        
        BigDecimal fee = BigDecimal.ZERO;
        if (type == 1) { //充值
            fromAccount = userAccount.getAccount();
            password = DataDecrypt.decrypt(userAccount.getPassword());
            toAccount = platAccount.getAccount();
        } else if (type == 2) { //提现
            fromAccount = platAccount.getAccount();
            password = DataDecrypt.decrypt(platAccount.getPassword());
            toAccount = userAccount.getAccount();
            
            //手续费，如果提现扣除手续费  万分之5
            fee = amount.multiply(new BigDecimal("0.0005"));
            amount = amount.subtract(fee);
        }
        
//        BigDecimal gas = getGas();
        Result<?> result = ethereumService.trans(userAccount.getUserid(), fromAccount, toAccount, password, amount, pointamount.toString(), type,fee,withdrawId);

        //以太坊转账失败
//        if (!result.isSuccess()) {
//            return result;
//        }
//
//        Map<String, Object> map = (Map<String, Object>) result.getData();
//
//        //记录转账流水
//        EthereumTransInfoDo transInfo = new EthereumTransInfoDo();
//        transInfo.setUserid(userAccount.getUserid());
//        transInfo.setFromaccount(fromAccount);
//        transInfo.setToaccount(toAccount);
//        transInfo.setAmount(amount.toString());
//        //transInfo.setActualamount(map.get("value").toString());
//        transInfo.setPointamount(pointamount.toString());
//        transInfo.setGas(gas.toString());
//        transInfo.setGaslimit(gas.toString());
//        //transInfo.setActualgas(map.get("gas").toString());
//        transInfo.setHash(map.get("hash").toString());
//        transInfo.setType(type);
//        transInfo.setCreatetime(new Date());
//        transInfo.setUpdatetime(new Date());
//        ethereumTransInfoDao.insertSelective(transInfo);
        
        return result;
    }

    /** 
     * 以太坊交易费
     * @return  
     */
//    private BigDecimal getGas() {
//        CtCurrencyDo ct = ctCurrencyService.selectByName(CurrencyType.DCE.name());
//
//        if (ct != null && ct.getWith_fee() != null) {
//            return ct.getWith_fee();
//        }
//
//        return BigDecimal.ZERO;
//    }

    /** 
     * 以太坊换算美元点
     * @param ethAmount
     * @return  
     */
    private BigDecimal eth2Point(BigDecimal ethAmount) {
        BigDecimal usdPrice = ethereumService.getMarketPrice(); //美元价格
        BigDecimal rmbPrice = usdPrice.multiply(getExRate()).setScale(6, RoundingMode.HALF_UP); //eth的人民币价格

        LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.RMB2Point.getCode(), DictCode.RMB2Point.getCode());
        Assert.notNull(loanDictDtlDo, "充值美元转美元点比例未设置");
        Assert.hasText(loanDictDtlDo.getRemark(), "充值美元转美元点比例未设置");
        BigDecimal rate = new BigDecimal(loanDictDtlDo.getRemark());

        //美元点数量 = （以太坊币数量 * 以太坊人民币价格）/美元点转人民币比例
        BigDecimal pointQty = ethAmount.multiply(rmbPrice).divide(rate,8, RoundingMode.HALF_UP).setScale(6, RoundingMode.HALF_UP);

        return pointQty;
    }

    /** 
     * 美元点转以太坊
     * @param pointAmount
     * @return  
     */
    private BigDecimal point2Eth(BigDecimal pointAmount) {
        BigDecimal usdPrice = ethereumService.getMarketPrice(); //美元价格
        BigDecimal rmbPrice = usdPrice.multiply(getExRate()).setScale(6, RoundingMode.HALF_UP); //eth的人民币价格

        LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.Point2RMB.getCode(), DictCode.Point2RMB.getCode());
        Assert.notNull(loanDictDtlDo, "提现美元点转美元比例未设置");
        Assert.hasText(loanDictDtlDo.getRemark(), "提现美元点转美元比例未设置");
        BigDecimal rate = new BigDecimal(loanDictDtlDo.getRemark());

        //以太坊币数量 = (美元点数量 * 美元点转人民币比例)/以太坊人民币价格
        BigDecimal ethQty = pointAmount.multiply(rate).divide(rmbPrice,8, RoundingMode.HALF_UP).setScale(6, RoundingMode.HALF_UP);

        return ethQty;
    }
    
    /**
     * 获取美元与人民币换算关系 
     * @return  
     */
    private BigDecimal getExRate() {
//        LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.ExRate.getCode(), DictCode.ExRate.getCode());
//        Assert.notNull(loanDictDtlDo, "汇率未设置");
//        Assert.notNull(loanDictDtlDo.getRemark(), "汇率未设置");
//
//        return new BigDecimal(loanDictDtlDo.getRemark());
        return BigDecimal.ONE; //直接按美元来换算
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void comfirmEthTransResult() {
        Integer rows = 200; //每次做200条
        Integer offset = 0;
        while (true) {
            Map<String, Object> params = new HashMap<>();
            params.put("status", "true");// 有效
            params.put("confirmed", "false"); //未确认
            params.put("offset", offset);
            params.put("rows", rows);
            List<EthereumTransInfoDo> list = ethereumTransInfoDao.select(params);
            if (CollectionUtils.isEmpty(list)) {
                break;
            }

            for (EthereumTransInfoDo transInfo : list) {
                Result<?> result = ethereumService.getTransResult(transInfo.getHash());
                if (!result.isSuccess()) {//结果尚未确认
                    continue;
                }

                Map<String, Object> map = (Map<String, Object>) result.getData();

                EthereumTransInfoDo newTransInfo = new EthereumTransInfoDo();
                newTransInfo.setId(transInfo.getId());
                newTransInfo.setActualamount(map.get("value").toString());
                newTransInfo.setActualgas(map.get("gas").toString());
                newTransInfo.setConfirmed("true");
                newTransInfo.setUpdatetime(new Date());
                ethereumTransInfoDao.updateByPrimaryKeySelective(newTransInfo);

                //如果是充值，成功以后增加美元点账户
                if (1 == transInfo.getType()) {
                    UserAccountDo updateAccount = new UserAccountDo();
                    updateAccount.setAccountType(AccountType.point.getAccountType());
                    updateAccount.setAmount(new BigDecimal(transInfo.getPointamount()));
                    updateAccount.setUserId(transInfo.getUserid());
                    updateUserAmountById(updateAccount, IncomeType.TYPE_RECHARGE);
                }
            }

            offset += rows;
        }
    }

	@Override
	public String getMyQRCode(Integer userId) {
		String qrcode= userAccountDao.getMyQRCode(userId);
		if(StringUtils.isBlank(qrcode)){
			qrcode = PayUtil.getPayCode(UUID.randomUUID().toString());
			userAccountDao.insertQRCode(qrcode,userId);
		}
		return qrcode;
	}

	@Override
	public Result<?> payByQRCode(Integer userId, String qrCode, String amount,
			String pwd) {
		
		Result<?> result = Result.successResult("扫描码支付成功");
		UserDo user = userDao.selectByPrimaryKey(userId);
		//判断用户是否允许扫描支付
		if(user.getUndoOpts() != null && user.getUndoOpts().contains("601")){
			result.setCode(Result.failureCode);
			result.setMsg("扫描支付功能受限");
			return result;
		}
		
		//校验密码是否正确
		pwd = DataEncrypt.encrypt(pwd);
		//if(StringUtils.isBlank(pwd) || !MD5Encrypt.getMessageDigest(pwd).equals(user.getTwoPassword())){
		if(StringUtils.isBlank(pwd) || !pwd.equals(user.getTwoPassword())){
			result.setCode(Result.failureCode);
			result.setMsg("密码错误");
			return result;
		}
		
		BigDecimal amt = null;
		try{
			//校验金额
			amt = new BigDecimal(amount);
			if(amt.compareTo(BigDecimal.ZERO)<=0){
				result.setCode(Result.failureCode);
				result.setMsg("金额错误");
				return result;
			}
		}catch(Exception e ){
			result.setCode(Result.failureCode);
			result.setMsg("金额错误");
			return result;
		}
		
		Integer targetUserId = userAccountDao.getUserIdByQRCode(qrCode);
		
		//人民币转DCE币
		BigDecimal dceAmt = loanDictService.rmb2Dce(amt);
		
		//查询美元点比DCE币的比例 N:1
        LoanDictDtlDo MYDBXCC = loanDictService.getLoanDictDtl(DictCode.MYDBXCC.getCode());
        BigDecimal decp = new BigDecimal(MYDBXCC.getRemark());
        //计算需要的美元点
        BigDecimal targetAmt = dceAmt.multiply(decp);
        targetAmt = targetAmt.divide(new BigDecimal(1), 6, RoundingMode.HALF_UP);
		
		this.convertBetweenAccount(userId, targetUserId, dceAmt, AccountType.current.name(), AccountType.point.name(), IncomeType.TYPE_PAY_QRCODE, IncomeType.TYPE_PAY_QRCODE,targetAmt );
		
		return result;
	}
	
	

	/**
	 * 发送给其他账号
	 * @param userId
	 * @param qrCode
	 * @param amount
	 * @param pwd
	 * @return
	 */
	@Async
	@Override
	public Result<?> send(Integer userId, String receiveAddress, String amount,
			String pwd) {
		
		Result<?> result = Result.successResult("发送成功");
		UserDo user = userDao.selectByPrimaryKey(userId);
		
		//校验密码是否正确
		pwd = DataEncrypt.encrypt(pwd);
		if(StringUtils.isBlank(pwd) || !pwd.equals(user.getTwoPassword())){
			result.setCode(Result.failureCode);
			result.setMsg("密码错误");
			return result;
		}
		
		BigDecimal amt = null;
		try{
			//校验金额
			amt = new BigDecimal(amount);
			if(amt.compareTo(BigDecimal.ZERO)<=0){
				result.setCode(Result.failureCode);
				result.setMsg("金额错误");
				return result;
			}
			
			if(amt.compareTo(new BigDecimal("0.1"))<=0){
				result.setCode(Result.failureCode);
				result.setMsg("发送金额不能小于0.1以太坊");
				return result;
			}
			
		}catch(Exception e ){
			result.setCode(Result.failureCode);
			result.setMsg("金额错误");
			return result;
		}
		
        String toAccount = receiveAddress; //转入账号
        EthereumAccountDo ethAccount = ethereumService.getByUserId(userId);
        String password = ethAccount.getPassword(); //交易密码
        String fromAccount = ethAccount.getAccount();//转出账号
        
//        BigDecimal gas = getGas();
        result = ethereumService.trans(userId, fromAccount, toAccount, DataDecrypt.decrypt(password), amt, amount.toString(), 3,BigDecimal.ZERO);

//        //以太坊转账失败
//        if (!result.isSuccess()) {
//            return result;
//        }
//
//        Map<String, Object> map = (Map<String, Object>) result.getData();
//
//        //记录转账流水
//        EthereumTransInfoDo transInfo = new EthereumTransInfoDo();
//        transInfo.setUserid(userId);
//        transInfo.setFromaccount(fromAccount);
//        transInfo.setToaccount(toAccount);
//        transInfo.setAmount(amount.toString());
//        //transInfo.setActualamount(map.get("value").toString());
//        transInfo.setPointamount(amount);
//        transInfo.setGas(gas.toString());
//        transInfo.setGaslimit(gas.toString());
//        //transInfo.setActualgas(map.get("gas").toString());
//        transInfo.setHash(map.get("hash").toString());
//        transInfo.setType(3); //1.充值， 2. 提现 3.发送
//        transInfo.setCreatetime(new Date());
//        transInfo.setUpdatetime(new Date());
//        ethereumTransInfoDao.insertSelective(transInfo);   
		return result;
	}
	

	/**
	 * 根据收款码获取收款人信息
	 */
	@Override
	public UserDo getReceiverQRCode(String qrCode) {
		Integer targetUserId = userAccountDao.getUserIdByQRCode(qrCode);
		return userDao.selectByPrimaryKey(targetUserId);
	}

	/**
	 * 购买商品支付,购买商品扣锁仓
	 */
	@Override
	public Result<?> buyGoods(Integer userId, BigDecimal totalPrice) {
		 UserAccountDo updateAccount = new UserAccountDo();
         updateAccount.setAccountType(AccountType.locked.getAccountType());
         updateAccount.setAmount(totalPrice.negate());
         updateAccount.setUserId(userId);
         updateUserAmountById(updateAccount, IncomeType.TYPE_GOODS_BUY);
		return Result.successResult("购买商品成功");	
	}
	
}
