package com.dce.business.dao.message;

import java.util.List;
import java.util.Map;

import com.dce.business.entity.message.NewsDo;

public interface INewsDao {

	List<NewsDo> select(Map<String,Object> params);
	
	NewsDo selectByPrimaryKey(Integer id);
	
}
