package com.dce.business.entity.order;

import java.math.BigDecimal;
import java.util.List;

public class Order {

	private Integer orderid; // 订单主键id

	private String ordercode; // 订单编号

	private Integer userid; // 用户id

	private Integer qty; // 商品总数量

	private BigDecimal totalprice; // 订单总金额

	private String recaddress;

	private String createtime; // 订单创建之间

	private Integer orderstatus; // 订单状态（1已发货2未发货）

	private Integer paystatus; // 付款状态（1已付2待付）

	private String paytime; // 支付时间

	private Integer ordertype; // 订单支付方式（1微信2支付宝）

	private Long matchorderid;

	private BigDecimal salqty;

	private String accounttype;

	private Long goodsid;

	private Long price;

	private Integer addressid; // 收获地址表id，从该表中获取收货人的信息

	private Integer alipayStatus; // 支付状态：0支付失败1支付成功2未确定状态

	private List<OrderDetail> orderDetailList; // 订单明细

	public List<OrderDetail> getOrderDetailList() {
		return orderDetailList;
	}

	public void setOrderDetailList(List<OrderDetail> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}

	private List<OrderDetail> awardDetailLst; // 订单优惠明细

	public Integer getAlipayStatus() {
		return alipayStatus;
	}

	public void setAlipayStatus(Integer alipayStatus) {
		this.alipayStatus = alipayStatus;
	}

	public List<OrderDetail> getOrderDetailLst() {
		return orderDetailList;
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

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty2) {
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


	public List<OrderDetail> getAwardDetailLst() {
		return awardDetailLst;
	}

	public void setAwardDetailLst(List<OrderDetail> awardDetailLst) {
		this.awardDetailLst = awardDetailLst;
	}

	@Override
	public String toString() {
		return "Order [orderid=" + orderid + ", ordercode=" + ordercode + ", userid=" + userid + ", qty=" + qty
				+ ", totalprice=" + totalprice + ", recaddress=" + recaddress + ", createtime=" + createtime
				+ ", orderstatus=" + orderstatus + ", paystatus=" + paystatus + ", paytime=" + paytime + ", ordertype="
				+ ordertype + ", matchorderid=" + matchorderid + ", salqty=" + salqty + ", accounttype=" + accounttype
				+ ", goodsid=" + goodsid + ", price=" + price + ", addressid=" + addressid + ", alipayStatus="
				+ alipayStatus + ", orderDetailLst=" + orderDetailList + ", awardDetailLst=" + awardDetailLst + "]";
	}

}