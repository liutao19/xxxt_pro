package com.dce.business.service.impl.award;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import com.dce.business.dao.user.IUserRefereeDao;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.award.IAwardService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.user.IUserService;

@Service("zhiTuiAwardService")
public class ZhiTuiAwardServiceImpl implements IAwardService {

	private final static Logger logger = LoggerFactory.getLogger(HuZhuAwardServiceImpl.class);
    
    @Resource
    private IAccountService accountService;
    @Resource
    private IUserService userService;
    @Resource
    private ILoanDictService loanDictService;
    @Resource
    private IUserRefereeDao userRefereeDao;

    @Override
    @SuppressWarnings("unused")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void calAward(Integer userId, BigDecimal amount, Integer userLevel) {
        UserDo userDo = userService.getUser(userId);

        //推荐人
        UserDo refereeDo = userService.getUser(userDo.getRefereeid());
        
        if (refereeDo == null) { //根节点没有推荐人，报单时
            return;
        }
        
        if (!refereeDo.isActivated()) {
            logger.info("用id：{}用户未激活，不享受领导奖励", refereeDo.getId());
            return;
        }

        BigDecimal rate = getRecomandRate(refereeDo.getRefereeNumber());
        //直推奖
        BigDecimal zhituiWard = amount.multiply(rate).setScale(6, RoundingMode.HALF_UP);

        String remark = "";
        try {
            String pattern = "{0}入网，{1}已推荐人数{2}，得直推奖{3}";
            remark = MessageFormat.format(pattern, userDo.getUserName(), refereeDo.getUserName(), refereeDo.getRefereeNumber(), zhituiWard);
        } catch (Exception e) {
            logger.error("直推奖励备注异常：", e);
        }
        
        UserAccountDo accountDo = new UserAccountDo();
        accountDo.setUserId(refereeDo.getId());
        accountDo.setAmount(zhituiWard);
        accountDo.setAccountType(AccountType.current.getAccountType());
        accountDo.setRemark(remark);
        accountService.updateUserAmountById(accountDo, IncomeType.TYPE_AWARD_REFEREE);
    }

    /** 
     * 根据用户推荐人数查找直推奖比例
     * @param refereeNum 用户推荐人数
     * @return  
     */
    private BigDecimal getRecomandRate(Integer refereeNum) {
        if (refereeNum == null || refereeNum == 0) {
            return BigDecimal.ZERO;
        }

        List<LoanDictDtlDo> list = loanDictService.queryDictDtlListByDictCode(DictCode.ZhiTui.getCode());
        Assert.isTrue(list.size() > 0, "直推奖比例未配置");
        Collections.sort(list, new Comparator<LoanDictDtlDo>() {
            @Override
            public int compare(LoanDictDtlDo o1, LoanDictDtlDo o2) {
                return Integer.valueOf(o2.getCode()).compareTo(Integer.valueOf(o1.getCode()));
            }
        });

        //从大到小
        for (LoanDictDtlDo dtl : list) {
            if (refereeNum.compareTo(Integer.valueOf(dtl.getCode())) >= 0) {
                return new BigDecimal(dtl.getRemark());
            }
        }

        return BigDecimal.ZERO;
    }

}
