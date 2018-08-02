package com.dce.business.service.impl.award;

import java.util.List;

import com.dce.business.entity.user.UserStaticDo;

public interface IStaticAwardJob {

	public abstract void calStaticAwardJob(List<UserStaticDo> list);

}