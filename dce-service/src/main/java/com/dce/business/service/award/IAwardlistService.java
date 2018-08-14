/*
 * Powered By  huangzl QQ: 272950754
 * Web Site: http://www.hehenian.com
 * Since 2008 - 2018
 */

package com.dce.business.service.award;

/**
 * @author  huangzl QQ: 272950754
 * @version 1.0
 * @since 1.0
 */

import java.util.Map;

import com.dce.business.entity.award.Awardlist;
import com.dce.business.entity.page.PageDo;


public interface IAwardlistService{

	/**
	 * 根据ID 查询
	 * @parameter id
	 */
	public Awardlist getById(Integer id);
	
	
	/**
	 * 更新
	 */
	public int  updateAwardlistById(Awardlist newAwardlistDo);
	
	/**
	 * 选择性更新信息
	 */
	public int updateAwardlistSelective(Awardlist newAwardlistDo);
	
	/**
	 * 新增
	 */
	public int addAwardlist(Awardlist newAwardlistDo);
	
	/**
	 * 选择性新增信息
	 */
	public int addAwardlistSelective(Awardlist newAwardlistDo);
	
	/**
	 * 分页查询
	 * @param param
	 * @param page
	 * @return
	 */
	public PageDo<Awardlist> getAwardlistPage(Map<String, Object> param, PageDo<Awardlist> page);
	
	
	/**
	 * 删除
	 */
	public int deleteById(Integer id);
	
}
