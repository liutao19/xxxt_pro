package com.dce.business.service.award;

import com.dce.business.common.enums.AccountType;

public interface IAwardJobService {

	/**
	 * 计算持币生息
	 */
	void calInterest();
	
	/**
	 * 计算分享奖
	 */
	void calShared();

	/**
	 * 计算释放
	 * @param fromAccount
	 * @param toAccount
	 */
	void calRelease(AccountType fromAccount, AccountType toAccount);
	
	/**
	 * 计算原始仓释放
	 */
	void calRelease();
}