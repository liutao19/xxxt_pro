/*
 * Powered By  huangzl QQ: 272950754
 * Web Site: http://www.hehenian.com
 * Since 2008 - 2018
 */

package com.dce.business.service.user;

import java.util.List;

/**
 * @author  huangzl QQ: 272950754
 * @version 1.0
 * @since 1.0
 */

import java.util.Map;

import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.user.userPromoteDo;


public interface IUserPromoteService{

	/**
	 * 根据ID 查询
	 * @parameter id
	 */
	public userPromoteDo getById(Integer id);
	
	/**
	 * 更新
	 */
	public int  updateUserPromoteById(userPromoteDo newUserPromoteDo);
	
	/**
	 * 新增
	 */
	public int addUserPromote(userPromoteDo newUserPromoteDo);
	
	/**
	 * 分页查询
	 * @param param
	 * @param page
	 * @return
	 */
	public PageDo<userPromoteDo> getUserPromotePage(Map<String, Object> param, PageDo<userPromoteDo> page);
	
	
	/**
	 * 删除
	 */
	public int deleteById(Integer id);
	
	/**
	 * 根据用户等级和购买的数量，判断用户升级的等级
	 */
	userPromoteDo selectUserLevelAntBuyQty(Integer userLevel, int buyQty);
}
