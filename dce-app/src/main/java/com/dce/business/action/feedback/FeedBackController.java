package com.dce.business.action.feedback;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.actions.common.BaseController;
import com.dce.business.common.result.Result;
import com.dce.business.entity.feedback.FeedBackDo;
import com.dce.business.service.feedback.IFeedBackService;

@RestController
@RequestMapping("/feedBack")
public class FeedBackController extends BaseController {/*
	
	private final static Logger logger = Logger.getLogger(FeedBackController.class);


	@Resource
	private IFeedBackService feedBackService;
	
	@RequestMapping(value = "/addFeedBack", method = RequestMethod.POST)
	public Result<?> addApply() {
		logger.info("用户反馈....");
		
		FeedBackDo feedback = new FeedBackDo();
		
    	//获取前台传过来的反馈信息
    	String feedBackContent = getString("feedBackContent") == null ? "" : getString("feedBackContent");
    	
    	feedback.setFeedbackcontent(feedBackContent);
    	
    	Result<?> result = feedBackService.feedBack(feedback);
    	
		return result;
	}

*/}
