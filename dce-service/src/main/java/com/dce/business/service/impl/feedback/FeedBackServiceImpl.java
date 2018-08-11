package com.dce.business.service.impl.feedback;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dce.business.common.result.Result;
import com.dce.business.dao.feedback.FeedBackMapper;
import com.dce.business.entity.feedback.FeedBackDo;
import com.dce.business.service.feedback.IFeedBackService;

@Service("feedBackService")
public class FeedBackServiceImpl implements IFeedBackService {

	@Resource
	private FeedBackMapper feedBackDao;

	@Override
	public Result<?> feedBack(FeedBackDo feedBackDo) {
		int result = feedBackDao.insertSelective(feedBackDo);

		return result > 0 ? Result.successResult("反馈成功!") : Result.failureResult("系统繁忙");
	}

}
