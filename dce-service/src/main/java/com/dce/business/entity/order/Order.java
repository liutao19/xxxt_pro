package com.dce.business.entity.order;

import java.math.BigDecimal;
import java.util.Date;

public class Order {

	private Long orderid; //订单主键id

	private String ordercode; //订单编号

	private Integer userid; //用户id

	private BigDecimal qty; //商品总数量

	private BigDecimal totalprice; //订单总金额

	private String recaddress; 

	private String createtime; //订单创建之间

	private Integer orderstatus; //订单状态（1已发货2未发货）

	private Integer paystatus; //付款状态（1已付2待付）

	private String paytime; //支付时间

	private Integer ordertype; //订单支付方式（1微信2支付宝）

	private Long matchorderid; 

	private BigDecimal salqty;

	private String accounttype;

	private Long goodsid;

	private Long price;

	private Integer addressid; //收获地址表id，从该表中获取收货人的信息

	private String remark; //备注

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty2) {
		this.qty = qty2;
	}

	public BigDecimal getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(BigDecimal totalprice) {
		this.totalprice = totalprice;
	}

	public String getRecaddress() {
		return recaddress;
	}

	public void setRecaddress(String recaddress) {
		this.recaddress = recaddress;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Integer getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(Integer orderstatus) {
		this.orderstatus = orderstatus;
	}

	public Integer getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(Integer paystatus) {
		this.paystatus = paystatus;
	}

	public String getPaytime() {
		return paytime;
	}

	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}

	public Integer getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(Integer ordertype) {
		this.ordertype = ordertype;
	}

	public Long getMatchorderid() {
		return matchorderid;
	}

	public void setMatchorderid(Long matchorderid) {
		this.matchorderid = matchorderid;
	}

	public BigDecimal getSalqty() {
		return salqty;
	}

	public void setSalqty(BigDecimal salqty) {
		this.salqty = salqty;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public Long getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(Long goodsid) {
		this.goodsid = goodsid;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Integer getAddressid() {
		return addressid;
	}

	public void setAddressid(Integer addressid) {
		this.addressid = addressid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}