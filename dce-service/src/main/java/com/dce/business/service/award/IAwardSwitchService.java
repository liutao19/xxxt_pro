package com.dce.business.service.award;

public interface IAwardSwitchService {

	/**
	 * 奖金开关
	 * @param userId 用户id
	 * @return
	 */
	boolean isAwardSwitchOn(Integer userId);
	
	/**
	 * 释放开关
	 * @param userId
	 * @return
	 */
	boolean isReleaseSwitchOn(Integer userId);
}
