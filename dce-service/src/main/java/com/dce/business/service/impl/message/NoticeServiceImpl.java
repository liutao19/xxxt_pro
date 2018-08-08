package com.dce.business.service.impl.message;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dce.business.dao.notice.NoticeDoMapper;
import com.dce.business.entity.notice.NoticeDo;
import com.dce.business.service.message.INoticeService;

@Service("noticeService")
public class NoticeServiceImpl implements INoticeService {

	@Resource
	private NoticeDoMapper noticeDao;
	
	@Override
	public List<NoticeDo> selectNoticeList() {
		
		return noticeDao.selectByExample(null);
	}

}
