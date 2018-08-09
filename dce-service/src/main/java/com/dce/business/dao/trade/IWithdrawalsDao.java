package com.dce.business.dao.trade;

import java.util.List;
import java.util.Map;

import com.dce.business.entity.trade.WithdrawalsDo;

public interface IWithdrawalsDao {
    int deleteByPrimaryKey(Integer id);

    int insert(WithdrawalsDo record);

    int insertSelective(WithdrawalsDo record);

    WithdrawalsDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WithdrawalsDo record);

    int updateByPrimaryKey(WithdrawalsDo record);

    /**
     * 查询提现记录
     * @param param
     * @return
     */
	List<Map<String,Object>> selectWithdraw(Map<String, Object> param);

	/**
	 * 审批提现
	 * @param auditResult
	 * @param withdrawId
	 */
	void auditWithdrawById2(WithdrawalsDo withdraw);
	
	/**
	 * 审批提现
	 * @param auditResult
	 * @param withdrawId
	 */
	void auditWithdrawById3(WithdrawalsDo withdraw);
	
	/**
	 * 审批提现
	 * @param auditResult
	 * @param withdrawId
	 */
	void auditWithdrawById4(WithdrawalsDo withdraw);
	
	int selectWithdrawCount(Map<String,Object> param);
	
	List<Map<String,Object>> selectWithDrawByPage(Map<String,Object> param);
	
	Long selectWithDrawTotallAmount(Map<String,Object> param);
}