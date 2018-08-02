package com.dce.business.service.impl.award;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.DictCode;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.dao.touch.ITouchBonusRecordDao;
import com.dce.business.dao.user.IUserRefereeDao;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.dict.LoanDictDo;
import com.dce.business.entity.touch.TouchBonusRecordDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.entity.user.UserRefereeDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.award.IAwardService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.user.IUserService;

@Service("huZhuAwardService")
public class HuZhuAwardServiceImpl implements IAwardService {

    private final static Logger logger = LoggerFactory.getLogger(HuZhuAwardServiceImpl.class);

    @Resource
    private IAccountService accountService;
    @Resource
    private IUserService userService;
    @Resource
    private ILoanDictService loanDictService;
    @Resource
    private IUserRefereeDao userRefereeDao;
    @Resource
    private ITouchBonusRecordDao touchBonusRecordDao;

    @Override
    @SuppressWarnings("unused")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void calAward(Integer userId, BigDecimal amount, Integer userLevel) {
        List<TouchBonusRecordDo> list = getTouchBonus(userId);

        for (TouchBonusRecordDo touchBounsRecord : list) {
            doCalAward(touchBounsRecord.getUserid(), touchBounsRecord.getBonus());
        }
    }

    private void doCalAward(Integer userId, BigDecimal bonus) {
        UserDo userDo = userService.getUser(userId);

//        UserDo refUser = userService.getUser(userDo.getRefereeid());
//        if (refUser == null) {
//            return;
//        }
//
//        if (!refUser.isActivated()) {
//            logger.info("用id:{}用户未激活，不享受互助奖励", refUser.getId());
//            return;
//        }
        
        //查询用户的推荐人数
        Map<String, Object> params = new HashMap<>();
        params.put("refereeid", userDo.getId());
        params.put("distance", 1);
        List<UserRefereeDo> list = userRefereeDao.select(params);
//        if (list.size() != userDo.getRefereeNumber()) {
//            throw new BusinessException("推荐人数统计错误");
//        }

        //该用户没有推荐人，不需要计算互助
        if (list.size() <= 0) {
            return;
        }

        //互助奖
        if(null == userDo.getRefereeNumber() || userDo.getRefereeNumber().intValue() <1){
        	 logger.error("推荐人refereenumber异常："+ userDo.getId());
        }
        
        BigDecimal huzhuAward = bonus.multiply(getWeighted()).divide(BigDecimal.valueOf(list.size()), 6, RoundingMode.HALF_UP);
        
        //流水说明
        String remark = "";
        for (UserRefereeDo userReferee : list) {
            try {
                String pattern = "{0}得量奖{1}，其直推已激活会员{2}人，每人得互助奖{3}";
                remark = MessageFormat.format(pattern, userDo.getUserName(), bonus.toString(), userDo.getRefereeNumber().toString(),
                        huzhuAward.toString());
            } catch (Exception e) {
                logger.error("互助奖励备注异常：", e);
            }
            
            UserDo tempUserDo = userService.getUser(userReferee.getUserid());
            if (!tempUserDo.isActivated()) {
                logger.info("用id:{}用户未激活，不享受互助奖励", tempUserDo.getId());
                continue;
            }

            UserAccountDo tempAccountDo = new UserAccountDo();
            tempAccountDo.setUserId(userReferee.getUserid());
            tempAccountDo.setAmount(huzhuAward);
            tempAccountDo.setAccountType(AccountType.current.getAccountType());
            tempAccountDo.setRemark(remark);
            accountService.updateUserAmountById(tempAccountDo, IncomeType.TYPE_AWARD_HUZHU);
        }
    }

    /** 
     * 查询互助加权
     * @return  
     */
    private BigDecimal getWeighted() {
        LoanDictDo loanDictDo = loanDictService.getLoanDict(DictCode.HuZhuJiaQuan.getCode());
        Assert.notNull(loanDictDo, "互助加权没有配置");
        Assert.hasText(loanDictDo.getRemark(), "互助加权没有配置");

        return new BigDecimal(loanDictDo.getRemark());
    }

    /** 
     * 查询由这个用户触发的碰撞，获得碰撞收益的节点
     * @param relevantUserid
     * @return  
     */
    private List<TouchBonusRecordDo> getTouchBonus(Integer relevantUserid) {
        Map<String, Object> params = new HashMap<>();
        params.put("relevantUserid", relevantUserid);

        List<TouchBonusRecordDo> list = touchBonusRecordDao.select(params);
        return list;
    }
}
