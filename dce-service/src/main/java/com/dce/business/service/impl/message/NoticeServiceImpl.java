package com.dce.business.service.impl.message;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dce.business.dao.message.TYsNoticeMapper;
import com.dce.business.entity.message.NoticeDo;
import com.dce.business.service.message.INoticeService;

@Service("noticeService")
public class NoticeServiceImpl implements INoticeService {

	@Resource
	private TYsNoticeMapper notice;
	
	@Override
	public List<NoticeDo> selectNoticeList() {
		
		return notice.select();
	}

}
