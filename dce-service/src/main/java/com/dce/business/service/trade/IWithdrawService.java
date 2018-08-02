package com.dce.business.service.trade;

import java.util.List;
import java.util.Map;

import com.dce.business.entity.trade.WithdrawalsDo;

public interface IWithdrawService {

    /**
     * 查询申请提现记录 
     * @param userId
     * @return  
     */
    List<Map<String,Object>> getWithdrawRecords(Map<String,Object> param);
    
    
    /**
     * 审批申请提现记录 
     * @param withdrawId 提现记录id
     * @param auditResult 审批意见
     * @return  
     */
    void auditWithdrawById(String auditResult,Integer withdrawId);
    
    int selectWithdrawCount(Map<String,Object> param);
}
