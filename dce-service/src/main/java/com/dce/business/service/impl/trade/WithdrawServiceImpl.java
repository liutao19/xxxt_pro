package com.dce.business.service.impl.trade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.common.enums.MessageType;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.dao.etherenum.IEthereumTransInfoDao;
import com.dce.business.dao.trade.IWithdrawalsDao;
import com.dce.business.dao.user.IUserDao;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.etherenum.EthereumTransInfoDo;
import com.dce.business.entity.message.MessageAndNewsDo;
import com.dce.business.entity.trade.WithdrawalsDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.message.IMessageService;
import com.dce.business.service.trade.IWithdrawService;

/** 
 * 交易
 * @author parudy
 * @date 2018年3月25日 
 * @version v1.0
 */
@Service("withdrawService")
public class WithdrawServiceImpl implements IWithdrawService {

    @Resource
    private IWithdrawalsDao withdrawDao;
    @Resource
    private IAccountService accountService;
    @Resource
    private IMessageService messageService;
    @Resource
    private IUserDao userDao;
    @Resource
    private IEthereumTransInfoDao ethereumTransInfoDao;

    @Override
    public List<Map<String, Object>> getWithdrawRecords(Map<String, Object> param) {
        return withdrawDao.selectWithdraw(param);
    }

    /**
     * @param auditResult 2 : 同意支付; 3:拒绝
     */
    @Override
    public void auditWithdrawById(String auditResult, Integer withdrawId) {
        WithdrawalsDo withdrawDo = withdrawDao.selectByPrimaryKey(withdrawId);
        
        WithdrawalsDo withdraw = new WithdrawalsDo();
        withdraw.setId(withdrawId);
        withdraw.setProcessStatus(auditResult);
        withdraw.setConfirmDate(new Date().getTime()/1000);
        
        //同意支付
        if ("2".equals(auditResult)||"4".equals(auditResult)) {
            check(withdrawId); //重复性校验
            accountService.withdraw(withdrawDo.getId(),withdrawDo.getUserid(), withdrawDo.getAmount());
        } else if ("3".equals(auditResult)) {
            // 返还美元点
            UserAccountDo updateAccount = new UserAccountDo();
            updateAccount.setAccountType(AccountType.point.getAccountType());
            updateAccount.setAmount(withdrawDo.getAmount());
            updateAccount.setUserId(withdrawDo.getUserid());
            accountService.updateUserAmountById(updateAccount, IncomeType.TYPE_BACK_WITHDRAW);
           
            addMessage(withdrawDo.getUserid(), withdrawDo.getAmount());
        }
        
        //更新
        if ( "4".equals(auditResult)) {
            withdrawDao.auditWithdrawById4(withdraw);
        }else if("2".equals(auditResult)){
        	withdrawDao.auditWithdrawById2(withdraw);
        }else if("3".equals(auditResult)){
        	withdrawDao.auditWithdrawById3(withdraw);
        }
    }

    private void check(Integer withdrawId) {
        Map<String, Object> params = new HashMap<>();
        params.put("withdrawalsId", withdrawId);
        List<EthereumTransInfoDo> list = ethereumTransInfoDao.select(params);
        if (!CollectionUtils.isEmpty(list)) {
            throw new BusinessException("提现正在处理中，请勿重复操作");
        }
    }

    /** 
     * 用户提现被拒绝，写日志
     * @param userId
     * @param qty  
     */
    private void addMessage(Integer userId, BigDecimal qty) {
        UserDo userDo = userDao.selectByPrimaryKey(userId);
        String content = "用户" + userDo.getUserName() + "提现美元点" + qty.toString() + "被拒绝";
        MessageAndNewsDo msg = new MessageAndNewsDo();
        msg.setContent(content);
        msg.setTo_uid(userId);
        msg.setType(MessageType.TYPE_SYSMSG.getType());
        messageService.addMessage(msg);
    }

	@Override
	public int selectWithdrawCount(Map<String, Object> param) {
		
		return withdrawDao.selectWithdrawCount(param);
	}
}
