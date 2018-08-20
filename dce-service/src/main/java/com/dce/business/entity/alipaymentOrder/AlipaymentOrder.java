package com.dce.business.entity.alipaymentOrder;

import java.math.BigDecimal;
import java.util.Date;

public class AlipaymentOrder {

	private Integer id;

	private Integer orderid;

	private String ordercode;

	//交易状态：0交易创建并等待买家付款 1未付款交易超时关闭或支付完成后全额退款 2交易支付成功 3交易结束并不可退款
	private Integer orderstatus; 

	private BigDecimal totalamount;

	private BigDecimal receptamount;

	private Date createtime;

	private Date updatetime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public Integer getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(Integer orderstatus) {
		this.orderstatus = orderstatus;
	}

	public BigDecimal getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(BigDecimal totalamount) {
		this.totalamount = totalamount;
	}

	public BigDecimal getReceptamount() {
		return receptamount;
	}

	public void setReceptamount(BigDecimal receptamount) {
		this.receptamount = receptamount;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

}