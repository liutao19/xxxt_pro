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

@Service("lingDaoAwardService")
public class LingDaoAwardServiceImpl implements IAwardService {

	private final static Logger logger = LoggerFactory.getLogger(LingDaoAwardServiceImpl.class); 
	  
	 
    @Resource
    private IAccountService accountService;
    @Resource
    private IUserService userService;
    @Resource
    private ILoanDictService loanDictService;
    @Resource
    private ITouchBonusRecordDao touchBonusRecordDao;
    @Resource
    private IUserRefereeDao userRefereeDao;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void calAward(Integer userId, BigDecimal amount,Integer userLevel) {
        List<TouchBonusRecordDo> list = getTouchBonus(userId);

        for (TouchBonusRecordDo touchBounsRecord : list) {
            doCalAward(touchBounsRecord.getUserid(), touchBounsRecord.getBonus());
        }
    }

    private void doCalAward(Integer userId, BigDecimal bonus) {
        BigDecimal leaderBonus = calLeaderAward(bonus);
        List<UserRefereeDo> list = getReferee(userId);

        UserDo userDo = userService.getUser(userId);
        //流水说明
        String remark = "";
        for (UserRefereeDo refereeDo : list) {
            UserDo refUser = userService.getUser(refereeDo.getRefereeid());
            if (!refUser.isActivated()) {
                logger.info("用id：{}用户未激活，不享受领导奖励", refereeDo.getRefereeid());
                continue;
            }
            
            //直推几人拿几代，20180428
            //判断直推人数比代数小，直接返回
            Integer refNumber = refUser.getRefereeNumber() == null ? 0 : refUser.getRefereeNumber(); //推荐人数
            Integer distance = refereeDo.getDistance() == null ? 0 : refereeDo.getDistance(); //代数
            if (refNumber.intValue() < distance) {
                logger.info("用id：" + refereeDo.getRefereeid() + ", 直推人数：" + refNumber + "; 代数：" + distance);
                continue;
            }

            try {
                String pattern = "{0}得量奖{1}，其上级领导得领导奖{2}";
                remark = MessageFormat.format(pattern, userDo.getUserName(), bonus.toString(), leaderBonus.toString());
            } catch (Exception e) {
                logger.error("互助奖励备注异常：", e);
            }

            UserAccountDo accountDo = new UserAccountDo();
            accountDo.setUserId(refereeDo.getRefereeid());
            accountDo.setAmount(leaderBonus);
            accountDo.setAccountType(AccountType.wallet_bonus.getAccountType());
            accountDo.setRemark(remark);
            accountService.updateUserAmountById(accountDo, IncomeType.TYPE_AWARD_LEADER);
        }
    }

    /** 
     * 计算领导奖
     * @param bonus 量碰奖励
     * @return  
     */
    private BigDecimal calLeaderAward(BigDecimal bonus) {
        LoanDictDo loanDictDo = loanDictService.getLoanDict(DictCode.LingDao.getCode());
        Assert.notNull(loanDictDo, "领导奖比例未设置");
        Assert.hasText(loanDictDo.getRemark(), "领导奖比例未设置");

        BigDecimal leaderBonus = bonus.multiply(new BigDecimal(loanDictDo.getRemark())).setScale(6, RoundingMode.HALF_UP);
        return leaderBonus;
    }

    /** 
     * 查询五代推荐人
     * @param userId
     * @return  
     */
    private List<UserRefereeDo> getReferee(Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userid", userId);
        params.put("maxDistance", 5);
        List<UserRefereeDo> list = userRefereeDao.select(params);

        return list;
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
