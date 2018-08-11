package com.dce.business.service.feedback;

import com.dce.business.common.result.Result;
import com.dce.business.entity.feedback.FeedBackDo;

public interface IFeedBackService {
	public Result<?> feedBack(FeedBackDo feedBackDo);
}
