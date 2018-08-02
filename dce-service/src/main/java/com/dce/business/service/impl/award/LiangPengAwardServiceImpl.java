package com.dce.business.service.impl.award;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.DictCode;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.dao.touch.ITouchBonusRecordDao;
import com.dce.business.dao.user.IUserParentDao;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.entity.touch.TouchBonusRecordDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.entity.user.UserParentDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.award.IAwardService;
import com.dce.business.service.bonus.IBonusDailyService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.user.IUserService;

@Service("liangPengAwardService")
public class LiangPengAwardServiceImpl implements IAwardService {
    private Logger logger = Logger.getLogger(LiangPengAwardServiceImpl.class);
    
    @Resource
    private IAccountService accountService;
    @Resource
    private IUserService userService;
    @Resource
    private ILoanDictService loanDictService;
    @Resource
    private IUserParentDao userParentDao;
    @Resource
    private ITouchBonusRecordDao touchBonusRecordDao;
    @Resource
    private IBonusDailyService bonusDailyService;
    
    private Set<Integer> jsl001IdSet = new HashSet<>(); //jsl001用户以及所有子节点的id

    @Override
    @SuppressWarnings("unused")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void calAward(Integer userId, BigDecimal amount, Integer userLevel) {
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
        
        setJsl001();

        //计算量碰层级
        LoanDictDtlDo loanDictDtl = loanDictService.getLoanDictDtl("LPCJ");
        int calLevels = 1;
        int calCountLevels = loanDictDtl.getRemark() == null ? Integer.MAX_VALUE : Integer.parseInt(loanDictDtl.getRemark());
        for (UserParentDo userParentDo : list) {
            if (calLevels <= calCountLevels) {
                doCalTouchAward(userParentDo.getParentid(), userParentDo.getLrDistrict(), amount, userId);
            }
        }
    }

    private void doCalTouchAward(Integer parentId, Byte lrDistrict, BigDecimal amount, Integer relevantUserid) {
        //jsl001下面的所有用户不参与量碰，不拿量碰奖励
        if (jsl001IdSet.contains(parentId)) {
            return;
        }
        
        UserDo userDo = userService.getUser(parentId);

        //用户未激活，不参与量碰记录
        if (!userDo.isActivated()) {
            return;
        }
        
        BigDecimal left = BigDecimal.ZERO;
        BigDecimal right = BigDecimal.ZERO;
        
        //查询用户最后一次量碰记录
        TouchBonusRecordDo touchBonusRecordDo = touchBonusRecordDao.getUserTouchBonusRecord(parentId);
        if (touchBonusRecordDo != null) {
            left = touchBonusRecordDo.getBalanceLeft();
            right = touchBonusRecordDo.getBalanceRight();
        }
        
        TouchBonusRecordDo newTouchBonusRecordDo = new TouchBonusRecordDo();
        newTouchBonusRecordDo.setUserid(parentId);
        newTouchBonusRecordDo.setRelevantUserid(relevantUserid);
        
        
        //未量碰前先添加本次报单金额
        if (lrDistrict == 1) { //左区
            left = left.add(amount); 
        } else {
            right = right.add(amount);
        }
        
        newTouchBonusRecordDo.setBalanceLeft(left); 
        newTouchBonusRecordDo.setBalanceRight(right);
        
        
        //小区报单，需要进行量碰
        if (left.compareTo(BigDecimal.ZERO) > 0 && right.compareTo(BigDecimal.ZERO) > 0) {
            //计算本次量碰奖励
            //奖励 = 量碰数量*量碰收益比例（根据用户等级配置）
            BigDecimal ableTouch = left.compareTo(right) > 0 ? right : left;
            BigDecimal bonus = ableTouch.multiply(getTouchRate(userDo.getUserLevel().intValue())).setScale(6, RoundingMode.HALF_UP);
            
            //封顶处理
            bonus = getBonusAmount(bonus, userDo);
            
            //扣减本次量碰金额
            newTouchBonusRecordDo.setBalanceLeft(newTouchBonusRecordDo.getBalanceLeft().subtract(ableTouch));
            newTouchBonusRecordDo.setBalanceRight(newTouchBonusRecordDo.getBalanceRight().subtract(ableTouch));
            newTouchBonusRecordDo.setTouchNumber(ableTouch);
            newTouchBonusRecordDo.setBonus(bonus);
            
            //更新用户当日已量碰金额
            bonusDailyService.updateAmount(parentId, IncomeType.TYPE_AWARD_TOUCH.getIncomeType() + "", new Date(), bonus);

            //更新账户总收益，收益按照配置比例存放现持仓、锁仓
            updateTouchAmount(parentId, bonus, relevantUserid);
        }
        
        //记录量碰记录
        touchBonusRecordDao.insertSelective(newTouchBonusRecordDo);
    }

    /**
     * 判断用户量碰金额是否已封顶 
     * @param bonus
     * @param userId
     * @return  
     */
    private BigDecimal getBonusAmount(BigDecimal bonus, UserDo userDo) {
        LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.LiangPengFengDing.getCode(), userDo.getUserLevel().toString());
        Assert.notNull(loanDictDtlDo, "量碰封顶未设置");
        Assert.notNull(loanDictDtlDo.getRemark(), "量碰封顶未设置");

        BigDecimal fengding = new BigDecimal(loanDictDtlDo.getRemark());

        //计算本次可量碰金额
        BigDecimal alreadyBonus = bonusDailyService.selectAmount(userDo.getId(), IncomeType.TYPE_AWARD_TOUCH.getIncomeType() + "");
        BigDecimal ableBonus = fengding.subtract(alreadyBonus);
        ableBonus = ableBonus.compareTo(BigDecimal.ZERO) > 0 ? ableBonus : BigDecimal.ZERO;

        if (ableBonus.compareTo(bonus) > 0) {
            return bonus;
        }

        return ableBonus;
    }

    /** 
     * 更新账户总收益，收益按照配置比例存放现持仓、锁仓
     * @param userId
     * @param bonus  
     */
    private void updateTouchAmount(Integer userId, BigDecimal bonus, Integer relevantUserid) {
        //金额为0时不需要记流水
        if (BigDecimal.ZERO.compareTo(bonus) >= 0) {
            return;
        }

        LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.LiangPengRate.getCode(), "1");
        Assert.notNull(loanDictDtlDo, "量碰分配比例未配置");
        Assert.hasText(loanDictDtlDo.getRemark(), "量碰分配比例未配置");
        
        //量碰流水说明
        String currentRemark = "";
        String lockedRemark = "";
        try {
            UserDo jiedian = userService.getUser(userId);
            UserDo huiyuan = userService.getUser(relevantUserid);
            BigDecimal touchRate = new BigDecimal(loanDictDtlDo.getRemark()).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);

            String pattern = "会员{0}入网，接点人{1}拿量碰，{2}%进入现持仓";
            currentRemark = MessageFormat.format(pattern, huiyuan.getUserName(), jiedian.getUserName(), touchRate.toString());
            
            touchRate = new BigDecimal("100").subtract(touchRate); //锁仓比例
            pattern = "会员{0}入网，接点人{1}拿量碰，{2}%进入锁仓";
            lockedRemark = MessageFormat.format(pattern, huiyuan.getUserName(), jiedian.getUserName(), touchRate.toString());
        } catch (Exception e) {
            logger.error("量碰备注异常：", e);
        }

        //现持仓比例
        BigDecimal currentBonus = bonus.multiply(new BigDecimal(loanDictDtlDo.getRemark())).setScale(6, RoundingMode.HALF_UP);
        UserAccountDo accountDo = new UserAccountDo();
        accountDo.setUserId(userId);
        accountDo.setAmount(currentBonus);
        accountDo.setAccountType(AccountType.current.getAccountType());
        accountDo.setRemark(currentRemark);
        accountService.updateUserAmountById(accountDo, IncomeType.TYPE_AWARD_TOUCH);

        //锁仓比例
        BigDecimal lockedBonus = bonus.subtract(currentBonus).setScale(6, RoundingMode.HALF_UP);
        accountDo = new UserAccountDo();
        accountDo.setUserId(userId);
        accountDo.setAmount(lockedBonus);
        accountDo.setAccountType(AccountType.locked.getAccountType());
        accountDo.setRemark(lockedRemark);
        accountService.updateUserAmountById(accountDo, IncomeType.TYPE_AWARD_TOUCH);
    }

    /** 
     * 查询用户量碰收益比例
     * @param userId
     * @return  
     */
    private BigDecimal getTouchRate(Integer userLevel) {
        //计算收益
        LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.LiangPeng.getCode(), userLevel.toString());
        Assert.notNull(loanDictDtlDo, "未设置量碰比例");
        Assert.hasText(loanDictDtlDo.getRemark(), "未设置量碰比例");

        return new BigDecimal(loanDictDtlDo.getRemark());
    }
    
    /**   
     * 添加jsl001以及下级节点的所有用户id
     */
    private void setJsl001() {
        UserDo jsl001 = userService.getUser("jsl001");
        if (jsl001 == null) {
            return;
        }
        jsl001IdSet.add(jsl001.getId());

        Map<String, Object> params = new HashMap<>();
        params.put("parentid", jsl001.getId());
        List<UserParentDo> list = userParentDao.select(params);
        for (UserParentDo userParentDo : list) {
            jsl001IdSet.add(userParentDo.getUserid());
        }
    }
}
