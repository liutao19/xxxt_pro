package com.dce.business.service.impl.account;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.DictCode;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.DateUtil;
import com.dce.business.dao.user.IUserDao;
import com.dce.business.dao.user.IUserStaticDao;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.dict.LoanDictDo;
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.entity.user.UserStaticDo;
import com.dce.business.entity.user.UserStaticDo.StaticType;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.account.IBaodanService;
import com.dce.business.service.award.IAwardService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.user.IUserStaticService;

@Service("baodanService")
public class BaodanServiceImpl implements IBaodanService {
    private final static Logger logger = Logger.getLogger(BaodanServiceImpl.class);
    @Resource
    private IUserStaticDao userStaticDao;
    @Resource(name = "awardServiceAsync")
    private IAwardService awardService;
    @Resource
    private IUserDao userDao;
    @Resource
    private ILoanDictService loanDictService;
    @Resource
    private IAccountService accountService;
    @Resource
    private IUserStaticService userStaticService;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<?> reCast(Integer userId, BigDecimal qty, String accountType) {
        UserDo user = userDao.selectByPrimaryKey(userId);
        if (user == null) {
            logger.info("用户不存在");
            return Result.failureResult("用户不存在!");
        }

        List<UserStaticDo> oldBaoDan = this.getBaoDan(userId);

        if (CollectionUtils.isEmpty(oldBaoDan)) {
            logger.info("用户还未报单,不能复投. userId" + userId);
            return Result.failureResult("你还未报单,不能复投!");
        }
        //获取复投金额 为报单金额
        BigDecimal oldQty = oldBaoDan.get(0).getTotalmoney();
        if (oldQty.compareTo(qty) > 0) {
            return Result.failureResult("复投不能小于原报单金额");
        }

        //复投修改级别
        String userLevel = getLevelByQty(qty);

        /*
        BigDecimal originalQty = calBaodanByLevel(userLevel, qty);
        UserDo record = new UserDo();
        record.setId(userId);
        record.setUserLevel( new Byte(userLevel));
        userDao.updateByPrimaryKeySelective(record );
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
        userStatic.setType(StaticType.FuTou.getType());
        userStatic.setTotalmoney(originalQty);
        userStatic.setEndDate(DateUtil.getDate(new Date(), 365)); //365天结束
        userStaticService.insert(userStatic);
        */

        int model = 2;//2 代表复投
        this.changeAccountByBaodan(userId, qty, accountType, Integer.parseInt(userLevel), model);

        return Result.successResult("复投成功");
    }

    private String getLevelByQty(BigDecimal qty) {
        List<LoanDictDtlDo> loanDictLst = loanDictService.queryDictDtlListByDictCode(DictCode.BaoDanFei.getCode());
        Assert.notEmpty(loanDictLst, "报单费未配置");
        for (LoanDictDtlDo dtl : loanDictLst) {
            if (qty.compareTo(new BigDecimal(dtl.getRemark())) <= 0) {
                return dtl.getCode();
            }
        }
        return "5";
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void baodan(Integer userId, BigDecimal qty, String accountType, Integer userLevel) {

    	LoanDictDo loanDict = loanDictService.getLoanDict("baodanOnOff");
    	if(loanDict != null && "off".equalsIgnoreCase(loanDict.getRemark())){
             throw new BusinessException("停止报单");
    	}
    	
    	
        logger.info("用户报单，userId:" + userId + "; qty:" + qty + "; accountType:" + accountType + "; userlevel:" + userLevel);

        //校验  用户是否已经报过单
        UserDo user = userDao.selectByPrimaryKey(userId);

        if (user == null) {
            logger.info("用户不存在 userId:" + userId);
            throw new BusinessException("用户不存在");
        }
        if (!CollectionUtils.isEmpty(getBaoDan(userId))) {
            logger.info("用户已报过单 不能在报单.userId:" + userId);
            throw new BusinessException("你已报过单, 不能在报单!");
        }

        //报单成功累计推荐人
        userDao.addRefereeNumber(user.getRefereeid());
        //int model = 1; //model=1表示报单
        changeAccountByBaodan(userId, qty, accountType, userLevel, 1);

        //计算业绩
        //userService.updateAllPerformance(qty, userId);

        //异步调用 
        awardService.calAward(userId, qty, userLevel);
    }

    private void changeAccountByBaodan(Integer userId, BigDecimal qty, String accountType, Integer userLevel, int model) {
        //根据报单级别查看报单所需金额
        LoanDictDtlDo loanDictDtl = loanDictService.getLoanDictDtl(DictCode.BaoDanFei.getCode(), userLevel.toString());
        Assert.notNull(loanDictDtl, "报单费未配置");
        Assert.hasText(loanDictDtl.getRemark(), "报单费未配置");
        String range = loanDictDtl.getRemark();
        if (qty.compareTo(new BigDecimal(range)) != 0) {
            throw new BusinessException("报单数量与用户等级不匹配");
        }

        //查询报单赠送比例
        loanDictDtl = loanDictService.getLoanDictDtl(DictCode.BaoDanZengSong.getCode(), userLevel.toString());
        Assert.notNull(loanDictDtl, "报单赠送未配置");
        Assert.hasText(loanDictDtl.getRemark(), "报单赠送未配置");

        BigDecimal targetQty = qty.multiply(new BigDecimal(loanDictDtl.getRemark())).setScale(6, RoundingMode.HALF_UP);
        //维护账户资金变动
        try {
            //先查询是否有对应账户  且账户中是否有足够余额
            UserAccountDo account = accountService.selectUserAccount(userId, accountType);
            if (account == null) {
                throw new BusinessException("报单失败，余额不足!");
            }

            BigDecimal sourceQty = qty;
            if (AccountType.current.name().equals(accountType)) {
                if (account.getAmount().compareTo(qty) < 0) {
                    throw new BusinessException("当前现持仓额度【" + account.getAmount() + "】小于要加金的额度【" + qty + "】");
                }
            } else if (AccountType.point.name().equals(accountType)) {
                //计算，根据当前价格 计算需要多少个美元点
                BigDecimal point = account.getAmount();
                /** sourceQty = qty * 当天dce当天价格  /7  **/
                //查询美元点比DCE币的比例 N:1
                LoanDictDtlDo MYDBXCC = loanDictService.getLoanDictDtl(DictCode.MYDBXCC.getCode());
                BigDecimal decp = new BigDecimal(MYDBXCC.getRemark());
                //计算需要的美元点
                sourceQty = qty.multiply(decp);

                if (point.compareTo(sourceQty) < 0) {
                    throw new BusinessException("当前美元点账户余额不足");
                }
            }

            IncomeType actionFrom = IncomeType.TYPE_AWARD_BAODAN;
            IncomeType actionTo = IncomeType.TYPE_AWARD_BAODAN;
            if (2 == model) {//复投
                actionFrom = IncomeType.TYPE_AWARD_FUTOU;
                actionTo = IncomeType.TYPE_AWARD_FUTOU;
            }

            convertBetweenAccount(userId, userId, sourceQty, accountType, AccountType.original.name(), actionFrom, actionTo, targetQty);

            UserStaticDo userStatic = new UserStaticDo();
            userStatic.setUserid(userId);
            userStatic.setMoney(calMoney(targetQty));
            userStatic.setStatus((byte) 1);
            if (1 == model) {
                userStatic.setType(StaticType.BaoDan.getType());
            } else {
                userStatic.setType(StaticType.FuTou.getType());
            }
            userStatic.setTotalmoney(targetQty);
            userStatic.setEndDate(DateUtil.getDate(new Date(), 365)); //365天结束
            userStaticService.insert(userStatic);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            logger.error("账户金额不足", e);
            throw new BusinessException("账户金额不足");
        }

        //更新用户级别
        try {
            UserDo record = new UserDo();
            record.setId(userId);
            record.setUserLevel(userLevel.byteValue());
            record.setActivationTime(new Date().getTime());
            record.setBaodan_amount(qty);
            userDao.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            logger.error("更新用户级别出错", e);
            throw new BusinessException("更新用户级别出错");
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void convertBetweenAccount(Integer sourceUserId, Integer targetUserId, BigDecimal amount, String fromAccount, String toAccount,
            IncomeType sourceMsg, IncomeType targetMsg, BigDecimal addAmount) {

        UserAccountDo source = new UserAccountDo();
        source.setUserId(sourceUserId);
        source.setAmount(amount.negate());
        source.setAccountType(fromAccount);
        accountService.updateUserAmountById(source, sourceMsg);

        UserAccountDo target = new UserAccountDo();
        target.setUserId(targetUserId);
        target.setAmount(addAmount);
        target.setAccountType(toAccount);
        accountService.updateUserAmountById(target, targetMsg);

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
     * 检查是否已报单
     * @param userId
     * @return
     */
    private List<UserStaticDo> getBaoDan(Integer userId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", 1);
        params.put("userid", userId);
        List<UserStaticDo> retLst = userStaticDao.select(params);
        if (retLst == null) {
            return Collections.EMPTY_LIST;
        }

        Collections.sort(retLst, new Comparator<UserStaticDo>() {
            @Override
            public int compare(UserStaticDo o1, UserStaticDo o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        return retLst;
    }

}
