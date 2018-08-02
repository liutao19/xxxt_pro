package com.dce.business.entity.message;

import java.util.Date;


public class NewsDo {

	  private Integer id ;//'消息ID',
	  private String title ;// '消息内容',
	  private String content ;// '消息内容',
	  private Integer newsType ;// '0系统消息,1评论消息,2私信消息',
	  private Integer is_read ;//'是否已读',
	  private Date creatDate  ;// '创建时间',
	  private Integer sort ;// '排序',
	  private Integer status ;// '状态',
	  
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Integer getIs_read() {
		return is_read;
	}
	public void setIs_read(Integer is_read) {
		this.is_read = is_read;
	}
	
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getNewsType() {
		return newsType;
	}
	public void setNewsType(Integer newsType) {
		this.newsType = newsType;
	}
	public Date getCreatDate() {
		return creatDate;
	}
	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}
	  
	  
}
