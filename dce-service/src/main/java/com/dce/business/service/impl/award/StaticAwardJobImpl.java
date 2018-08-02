package com.dce.business.service.impl.award;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dce.business.entity.user.UserStaticDo;

@Service("staticAwardJob")
public class StaticAwardJobImpl implements IStaticAwardJob {

    private final static Logger logger = LoggerFactory.getLogger(StaticAwardJobImpl.class);

    @Resource
    private OnePersonStaticService onePersonStaticService;
    
    /* (non-Javadoc)
	 * @see com.dce.business.service.impl.award.ICalStaticAwardJob#calStaticAwardJob(java.util.List)
	 */
	@Override
	@Async
	public void calStaticAwardJob(List<UserStaticDo> list) {
		for (UserStaticDo order : list) {
			try{
				onePersonStaticService.calOnePersonStaticAward(order);
			}catch(Exception e ){
				logger.error("user id{} 释放出错", order.getUserid());
				logger.error("释放出错", e);
			}
		}
	}
	
}
