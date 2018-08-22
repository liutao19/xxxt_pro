package com.dce.business.entity.alipaymentOrder;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.Date;

public class AlipaymentOrderExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	public AlipaymentOrderExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<Criterion>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andIdIsNull() {
			addCriterion("id is null");
			return (Criteria) this;
		}

		public Criteria andIdIsNotNull() {
			addCriterion("id is not null");
			return (Criteria) this;
		}

		public Criteria andIdEqualTo(Integer value) {
			addCriterion("id =", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotEqualTo(Integer value) {
			addCriterion("id <>", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThan(Integer value) {
			addCriterion("id >", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("id >=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThan(Integer value) {
			addCriterion("id <", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThanOrEqualTo(Integer value) {
			addCriterion("id <=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdIn(List<Integer> values) {
			addCriterion("id in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotIn(List<Integer> values) {
			addCriterion("id not in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdBetween(Integer value1, Integer value2) {
			addCriterion("id between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotBetween(Integer value1, Integer value2) {
			addCriterion("id not between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andOrderidIsNull() {
			addCriterion("orderId is null");
			return (Criteria) this;
		}

		public Criteria andOrderidIsNotNull() {
			addCriterion("orderId is not null");
			return (Criteria) this;
		}

		public Criteria andOrderidEqualTo(Integer value) {
			addCriterion("orderId =", value, "orderid");
			return (Criteria) this;
		}

		public Criteria andOrderidNotEqualTo(Integer value) {
			addCriterion("orderId <>", value, "orderid");
			return (Criteria) this;
		}

		public Criteria andOrderidGreaterThan(Integer value) {
			addCriterion("orderId >", value, "orderid");
			return (Criteria) this;
		}

		public Criteria andOrderidGreaterThanOrEqualTo(Integer value) {
			addCriterion("orderId >=", value, "orderid");
			return (Criteria) this;
		}

		public Criteria andOrderidLessThan(Integer value) {
			addCriterion("orderId <", value, "orderid");
			return (Criteria) this;
		}

		public Criteria andOrderidLessThanOrEqualTo(Integer value) {
			addCriterion("orderId <=", value, "orderid");
			return (Criteria) this;
		}

		public Criteria andOrderidIn(List<Integer> values) {
			addCriterion("orderId in", values, "orderid");
			return (Criteria) this;
		}

		public Criteria andOrderidNotIn(List<Integer> values) {
			addCriterion("orderId not in", values, "orderid");
			return (Criteria) this;
		}

		public Criteria andOrderidBetween(Integer value1, Integer value2) {
			addCriterion("orderId between", value1, value2, "orderid");
			return (Criteria) this;
		}

		public Criteria andOrderidNotBetween(Integer value1, Integer value2) {
			addCriterion("orderId not between", value1, value2, "orderid");
			return (Criteria) this;
		}

		public Criteria andOrdercodeIsNull() {
			addCriterion("orderCode is null");
			return (Criteria) this;
		}

		public Criteria andOrdercodeIsNotNull() {
			addCriterion("orderCode is not null");
			return (Criteria) this;
		}

		public Criteria andOrdercodeEqualTo(String value) {
			addCriterion("orderCode =", value, "ordercode");
			return (Criteria) this;
		}

		public Criteria andOrdercodeNotEqualTo(String value) {
			addCriterion("orderCode <>", value, "ordercode");
			return (Criteria) this;
		}

		public Criteria andOrdercodeGreaterThan(String value) {
			addCriterion("orderCode >", value, "ordercode");
			return (Criteria) this;
		}

		public Criteria andOrdercodeGreaterThanOrEqualTo(String value) {
			addCriterion("orderCode >=", value, "ordercode");
			return (Criteria) this;
		}

		public Criteria andOrdercodeLessThan(String value) {
			addCriterion("orderCode <", value, "ordercode");
			return (Criteria) this;
		}

		public Criteria andOrdercodeLessThanOrEqualTo(String value) {
			addCriterion("orderCode <=", value, "ordercode");
			return (Criteria) this;
		}

		public Criteria andOrdercodeLike(String value) {
			addCriterion("orderCode like", value, "ordercode");
			return (Criteria) this;
		}

		public Criteria andOrdercodeNotLike(String value) {
			addCriterion("orderCode not like", value, "ordercode");
			return (Criteria) this;
		}

		public Criteria andOrdercodeIn(List<String> values) {
			addCriterion("orderCode in", values, "ordercode");
			return (Criteria) this;
		}

		public Criteria andOrdercodeNotIn(List<String> values) {
			addCriterion("orderCode not in", values, "ordercode");
			return (Criteria) this;
		}

		public Criteria andOrdercodeBetween(String value1, String value2) {
			addCriterion("orderCode between", value1, value2, "ordercode");
			return (Criteria) this;
		}

		public Criteria andOrdercodeNotBetween(String value1, String value2) {
			addCriterion("orderCode not between", value1, value2, "ordercode");
			return (Criteria) this;
		}

		public Criteria andOrderstatusIsNull() {
			addCriterion("orderStatus is null");
			return (Criteria) this;
		}

		public Criteria andOrderstatusIsNotNull() {
			addCriterion("orderStatus is not null");
			return (Criteria) this;
		}

		public Criteria andOrderstatusEqualTo(Integer value) {
			addCriterion("orderStatus =", value, "orderstatus");
			return (Criteria) this;
		}

		public Criteria andOrderstatusNotEqualTo(Integer value) {
			addCriterion("orderStatus <>", value, "orderstatus");
			return (Criteria) this;
		}

		public Criteria andOrderstatusGreaterThan(Integer value) {
			addCriterion("orderStatus >", value, "orderstatus");
			return (Criteria) this;
		}

		public Criteria andOrderstatusGreaterThanOrEqualTo(Integer value) {
			addCriterion("orderStatus >=", value, "orderstatus");
			return (Criteria) this;
		}

		public Criteria andOrderstatusLessThan(Integer value) {
			addCriterion("orderStatus <", value, "orderstatus");
			return (Criteria) this;
		}

		public Criteria andOrderstatusLessThanOrEqualTo(Integer value) {
			addCriterion("orderStatus <=", value, "orderstatus");
			return (Criteria) this;
		}

		public Criteria andOrderstatusIn(List<Integer> values) {
			addCriterion("orderStatus in", values, "orderstatus");
			return (Criteria) this;
		}

		public Criteria andOrderstatusNotIn(List<Integer> values) {
			addCriterion("orderStatus not in", values, "orderstatus");
			return (Criteria) this;
		}

		public Criteria andOrderstatusBetween(Integer value1, Integer value2) {
			addCriterion("orderStatus between", value1, value2, "orderstatus");
			return (Criteria) this;
		}

		public Criteria andOrderstatusNotBetween(Integer value1, Integer value2) {
			addCriterion("orderStatus not between", value1, value2, "orderstatus");
			return (Criteria) this;
		}

		public Criteria andTotalamountIsNull() {
			addCriterion("totalAmount is null");
			return (Criteria) this;
		}

		public Criteria andTotalamountIsNotNull() {
			addCriterion("totalAmount is not null");
			return (Criteria) this;
		}

		public Criteria andTotalamountEqualTo(BigDecimal value) {
			addCriterion("totalAmount =", value, "totalamount");
			return (Criteria) this;
		}

		public Criteria andTotalamountNotEqualTo(BigDecimal value) {
			addCriterion("totalAmount <>", value, "totalamount");
			return (Criteria) this;
		}

		public Criteria andTotalamountGreaterThan(BigDecimal value) {
			addCriterion("totalAmount >", value, "totalamount");
			return (Criteria) this;
		}

		public Criteria andTotalamountGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("totalAmount >=", value, "totalamount");
			return (Criteria) this;
		}

		public Criteria andTotalamountLessThan(BigDecimal value) {
			addCriterion("totalAmount <", value, "totalamount");
			return (Criteria) this;
		}

		public Criteria andTotalamountLessThanOrEqualTo(BigDecimal value) {
			addCriterion("totalAmount <=", value, "totalamount");
			return (Criteria) this;
		}

		public Criteria andTotalamountIn(List<BigDecimal> values) {
			addCriterion("totalAmount in", values, "totalamount");
			return (Criteria) this;
		}

		public Criteria andTotalamountNotIn(List<BigDecimal> values) {
			addCriterion("totalAmount not in", values, "totalamount");
			return (Criteria) this;
		}

		public Criteria andTotalamountBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("totalAmount between", value1, value2, "totalamount");
			return (Criteria) this;
		}

		public Criteria andTotalamountNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("totalAmount not between", value1, value2, "totalamount");
			return (Criteria) this;
		}

		public Criteria andReceptamountIsNull() {
			addCriterion("receptAmount is null");
			return (Criteria) this;
		}

		public Criteria andReceptamountIsNotNull() {
			addCriterion("receptAmount is not null");
			return (Criteria) this;
		}

		public Criteria andReceptamountEqualTo(BigDecimal value) {
			addCriterion("receptAmount =", value, "receptamount");
			return (Criteria) this;
		}

		public Criteria andReceptamountNotEqualTo(BigDecimal value) {
			addCriterion("receptAmount <>", value, "receptamount");
			return (Criteria) this;
		}

		public Criteria andReceptamountGreaterThan(BigDecimal value) {
			addCriterion("receptAmount >", value, "receptamount");
			return (Criteria) this;
		}

		public Criteria andReceptamountGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("receptAmount >=", value, "receptamount");
			return (Criteria) this;
		}

		public Criteria andReceptamountLessThan(BigDecimal value) {
			addCriterion("receptAmount <", value, "receptamount");
			return (Criteria) this;
		}

		public Criteria andReceptamountLessThanOrEqualTo(BigDecimal value) {
			addCriterion("receptAmount <=", value, "receptamount");
			return (Criteria) this;
		}

		public Criteria andReceptamountIn(List<BigDecimal> values) {
			addCriterion("receptAmount in", values, "receptamount");
			return (Criteria) this;
		}

		public Criteria andReceptamountNotIn(List<BigDecimal> values) {
			addCriterion("receptAmount not in", values, "receptamount");
			return (Criteria) this;
		}

		public Criteria andReceptamountBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("receptAmount between", value1, value2, "receptamount");
			return (Criteria) this;
		}

		public Criteria andReceptamountNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("receptAmount not between", value1, value2, "receptamount");
			return (Criteria) this;
		}

		public Criteria andCreatetimeIsNull() {
			addCriterion("createTime is null");
			return (Criteria) this;
		}

		public Criteria andCreatetimeIsNotNull() {
			addCriterion("createTime is not null");
			return (Criteria) this;
		}

		public Criteria andCreatetimeEqualTo(Date value) {
			addCriterion("createTime =", value, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeNotEqualTo(Date value) {
			addCriterion("createTime <>", value, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeGreaterThan(Date value) {
			addCriterion("createTime >", value, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeGreaterThanOrEqualTo(Date value) {
			addCriterion("createTime >=", value, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeLessThan(Date value) {
			addCriterion("createTime <", value, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeLessThanOrEqualTo(Date value) {
			addCriterion("createTime <=", value, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeIn(List<Date> values) {
			addCriterion("createTime in", values, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeNotIn(List<Date> values) {
			addCriterion("createTime not in", values, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeBetween(Date value1, Date value2) {
			addCriterion("createTime between", value1, value2, "createtime");
			return (Criteria) this;
		}

		public Criteria andCreatetimeNotBetween(Date value1, Date value2) {
			addCriterion("createTime not between", value1, value2, "createtime");
			return (Criteria) this;
		}

		public Criteria andNotifytimeIsNull() {
			addCriterion("notifyTime is null");
			return (Criteria) this;
		}

		public Criteria andNotifytimeIsNotNull() {
			addCriterion("notifyTime is not null");
			return (Criteria) this;
		}

		public Criteria andNotifytimeEqualTo(Date value) {
			addCriterion("notifyTime =", value, "notifytime");
			return (Criteria) this;
		}

		public Criteria andNotifytimeNotEqualTo(Date value) {
			addCriterion("notifyTime <>", value, "notifytime");
			return (Criteria) this;
		}

		public Criteria andNotifytimeGreaterThan(Date value) {
			addCriterion("notifyTime >", value, "notifytime");
			return (Criteria) this;
		}

		public Criteria andNotifytimeGreaterThanOrEqualTo(Date value) {
			addCriterion("notifyTime >=", value, "notifytime");
			return (Criteria) this;
		}

		public Criteria andNotifytimeLessThan(Date value) {
			addCriterion("notifyTime <", value, "notifytime");
			return (Criteria) this;
		}

		public Criteria andNotifytimeLessThanOrEqualTo(Date value) {
			addCriterion("notifyTime <=", value, "notifytime");
			return (Criteria) this;
		}

		public Criteria andNotifytimeIn(List<Date> values) {
			addCriterion("notifyTime in", values, "notifytime");
			return (Criteria) this;
		}

		public Criteria andNotifytimeNotIn(List<Date> values) {
			addCriterion("notifyTime not in", values, "notifytime");
			return (Criteria) this;
		}

		public Criteria andNotifytimeBetween(Date value1, Date value2) {
			addCriterion("notifyTime between", value1, value2, "notifytime");
			return (Criteria) this;
		}

		public Criteria andNotifytimeNotBetween(Date value1, Date value2) {
			addCriterion("notifyTime not between", value1, value2, "notifytime");
			return (Criteria) this;
		}

		public Criteria andGmtcreatetimeIsNull() {
			addCriterion("gmtCreateTime is null");
			return (Criteria) this;
		}

		public Criteria andGmtcreatetimeIsNotNull() {
			addCriterion("gmtCreateTime is not null");
			return (Criteria) this;
		}

		public Criteria andGmtcreatetimeEqualTo(Date value) {
			addCriterion("gmtCreateTime =", value, "gmtcreatetime");
			return (Criteria) this;
		}

		public Criteria andGmtcreatetimeNotEqualTo(Date value) {
			addCriterion("gmtCreateTime <>", value, "gmtcreatetime");
			return (Criteria) this;
		}

		public Criteria andGmtcreatetimeGreaterThan(Date value) {
			addCriterion("gmtCreateTime >", value, "gmtcreatetime");
			return (Criteria) this;
		}

		public Criteria andGmtcreatetimeGreaterThanOrEqualTo(Date value) {
			addCriterion("gmtCreateTime >=", value, "gmtcreatetime");
			return (Criteria) this;
		}

		public Criteria andGmtcreatetimeLessThan(Date value) {
			addCriterion("gmtCreateTime <", value, "gmtcreatetime");
			return (Criteria) this;
		}

		public Criteria andGmtcreatetimeLessThanOrEqualTo(Date value) {
			addCriterion("gmtCreateTime <=", value, "gmtcreatetime");
			return (Criteria) this;
		}

		public Criteria andGmtcreatetimeIn(List<Date> values) {
			addCriterion("gmtCreateTime in", values, "gmtcreatetime");
			return (Criteria) this;
		}

		public Criteria andGmtcreatetimeNotIn(List<Date> values) {
			addCriterion("gmtCreateTime not in", values, "gmtcreatetime");
			return (Criteria) this;
		}

		public Criteria andGmtcreatetimeBetween(Date value1, Date value2) {
			addCriterion("gmtCreateTime between", value1, value2, "gmtcreatetime");
			return (Criteria) this;
		}

		public Criteria andGmtcreatetimeNotBetween(Date value1, Date value2) {
			addCriterion("gmtCreateTime not between", value1, value2, "gmtcreatetime");
			return (Criteria) this;
		}

		public Criteria andGmtrefundtimeIsNull() {
			addCriterion("gmtRefundTime is null");
			return (Criteria) this;
		}

		public Criteria andGmtrefundtimeIsNotNull() {
			addCriterion("gmtRefundTime is not null");
			return (Criteria) this;
		}

		public Criteria andGmtrefundtimeEqualTo(Date value) {
			addCriterion("gmtRefundTime =", value, "gmtrefundtime");
			return (Criteria) this;
		}

		public Criteria andGmtrefundtimeNotEqualTo(Date value) {
			addCriterion("gmtRefundTime <>", value, "gmtrefundtime");
			return (Criteria) this;
		}

		public Criteria andGmtrefundtimeGreaterThan(Date value) {
			addCriterion("gmtRefundTime >", value, "gmtrefundtime");
			return (Criteria) this;
		}

		public Criteria andGmtrefundtimeGreaterThanOrEqualTo(Date value) {
			addCriterion("gmtRefundTime >=", value, "gmtrefundtime");
			return (Criteria) this;
		}

		public Criteria andGmtrefundtimeLessThan(Date value) {
			addCriterion("gmtRefundTime <", value, "gmtrefundtime");
			return (Criteria) this;
		}

		public Criteria andGmtrefundtimeLessThanOrEqualTo(Date value) {
			addCriterion("gmtRefundTime <=", value, "gmtrefundtime");
			return (Criteria) this;
		}

		public Criteria andGmtrefundtimeIn(List<Date> values) {
			addCriterion("gmtRefundTime in", values, "gmtrefundtime");
			return (Criteria) this;
		}

		public Criteria andGmtrefundtimeNotIn(List<Date> values) {
			addCriterion("gmtRefundTime not in", values, "gmtrefundtime");
			return (Criteria) this;
		}

		public Criteria andGmtrefundtimeBetween(Date value1, Date value2) {
			addCriterion("gmtRefundTime between", value1, value2, "gmtrefundtime");
			return (Criteria) this;
		}

		public Criteria andGmtrefundtimeNotBetween(Date value1, Date value2) {
			addCriterion("gmtRefundTime not between", value1, value2, "gmtrefundtime");
			return (Criteria) this;
		}

		public Criteria andGmtclosetimeIsNull() {
			addCriterion("gmtCloseTime is null");
			return (Criteria) this;
		}

		public Criteria andGmtclosetimeIsNotNull() {
			addCriterion("gmtCloseTime is not null");
			return (Criteria) this;
		}

		public Criteria andGmtclosetimeEqualTo(Date value) {
			addCriterion("gmtCloseTime =", value, "gmtclosetime");
			return (Criteria) this;
		}

		public Criteria andGmtclosetimeNotEqualTo(Date value) {
			addCriterion("gmtCloseTime <>", value, "gmtclosetime");
			return (Criteria) this;
		}

		public Criteria andGmtclosetimeGreaterThan(Date value) {
			addCriterion("gmtCloseTime >", value, "gmtclosetime");
			return (Criteria) this;
		}

		public Criteria andGmtclosetimeGreaterThanOrEqualTo(Date value) {
			addCriterion("gmtCloseTime >=", value, "gmtclosetime");
			return (Criteria) this;
		}

		public Criteria andGmtclosetimeLessThan(Date value) {
			addCriterion("gmtCloseTime <", value, "gmtclosetime");
			return (Criteria) this;
		}

		public Criteria andGmtclosetimeLessThanOrEqualTo(Date value) {
			addCriterion("gmtCloseTime <=", value, "gmtclosetime");
			return (Criteria) this;
		}

		public Criteria andGmtclosetimeIn(List<Date> values) {
			addCriterion("gmtCloseTime in", values, "gmtclosetime");
			return (Criteria) this;
		}

		public Criteria andGmtclosetimeNotIn(List<Date> values) {
			addCriterion("gmtCloseTime not in", values, "gmtclosetime");
			return (Criteria) this;
		}

		public Criteria andGmtclosetimeBetween(Date value1, Date value2) {
			addCriterion("gmtCloseTime between", value1, value2, "gmtclosetime");
			return (Criteria) this;
		}

		public Criteria andGmtclosetimeNotBetween(Date value1, Date value2) {
			addCriterion("gmtCloseTime not between", value1, value2, "gmtclosetime");
			return (Criteria) this;
		}

		public Criteria andTradenoIsNull() {
			addCriterion("tradeNo is null");
			return (Criteria) this;
		}

		public Criteria andTradenoIsNotNull() {
			addCriterion("tradeNo is not null");
			return (Criteria) this;
		}

		public Criteria andTradenoEqualTo(String value) {
			addCriterion("tradeNo =", value, "tradeno");
			return (Criteria) this;
		}

		public Criteria andTradenoNotEqualTo(String value) {
			addCriterion("tradeNo <>", value, "tradeno");
			return (Criteria) this;
		}

		public Criteria andTradenoGreaterThan(String value) {
			addCriterion("tradeNo >", value, "tradeno");
			return (Criteria) this;
		}

		public Criteria andTradenoGreaterThanOrEqualTo(String value) {
			addCriterion("tradeNo >=", value, "tradeno");
			return (Criteria) this;
		}

		public Criteria andTradenoLessThan(String value) {
			addCriterion("tradeNo <", value, "tradeno");
			return (Criteria) this;
		}

		public Criteria andTradenoLessThanOrEqualTo(String value) {
			addCriterion("tradeNo <=", value, "tradeno");
			return (Criteria) this;
		}

		public Criteria andTradenoLike(String value) {
			addCriterion("tradeNo like", value, "tradeno");
			return (Criteria) this;
		}

		public Criteria andTradenoNotLike(String value) {
			addCriterion("tradeNo not like", value, "tradeno");
			return (Criteria) this;
		}

		public Criteria andTradenoIn(List<String> values) {
			addCriterion("tradeNo in", values, "tradeno");
			return (Criteria) this;
		}

		public Criteria andTradenoNotIn(List<String> values) {
			addCriterion("tradeNo not in", values, "tradeno");
			return (Criteria) this;
		}

		public Criteria andTradenoBetween(String value1, String value2) {
			addCriterion("tradeNo between", value1, value2, "tradeno");
			return (Criteria) this;
		}

		public Criteria andTradenoNotBetween(String value1, String value2) {
			addCriterion("tradeNo not between", value1, value2, "tradeno");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidIsNull() {
			addCriterion("buyerLogonId is null");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidIsNotNull() {
			addCriterion("buyerLogonId is not null");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidEqualTo(String value) {
			addCriterion("buyerLogonId =", value, "buyerlogonid");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidNotEqualTo(String value) {
			addCriterion("buyerLogonId <>", value, "buyerlogonid");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidGreaterThan(String value) {
			addCriterion("buyerLogonId >", value, "buyerlogonid");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidGreaterThanOrEqualTo(String value) {
			addCriterion("buyerLogonId >=", value, "buyerlogonid");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidLessThan(String value) {
			addCriterion("buyerLogonId <", value, "buyerlogonid");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidLessThanOrEqualTo(String value) {
			addCriterion("buyerLogonId <=", value, "buyerlogonid");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidLike(String value) {
			addCriterion("buyerLogonId like", value, "buyerlogonid");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidNotLike(String value) {
			addCriterion("buyerLogonId not like", value, "buyerlogonid");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidIn(List<String> values) {
			addCriterion("buyerLogonId in", values, "buyerlogonid");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidNotIn(List<String> values) {
			addCriterion("buyerLogonId not in", values, "buyerlogonid");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidBetween(String value1, String value2) {
			addCriterion("buyerLogonId between", value1, value2, "buyerlogonid");
			return (Criteria) this;
		}

		public Criteria andBuyerlogonidNotBetween(String value1, String value2) {
			addCriterion("buyerLogonId not between", value1, value2, "buyerlogonid");
			return (Criteria) this;
		}

		public Criteria andSelleridIsNull() {
			addCriterion("sellerId is null");
			return (Criteria) this;
		}

		public Criteria andSelleridIsNotNull() {
			addCriterion("sellerId is not null");
			return (Criteria) this;
		}

		public Criteria andSelleridEqualTo(String value) {
			addCriterion("sellerId =", value, "sellerid");
			return (Criteria) this;
		}

		public Criteria andSelleridNotEqualTo(String value) {
			addCriterion("sellerId <>", value, "sellerid");
			return (Criteria) this;
		}

		public Criteria andSelleridGreaterThan(String value) {
			addCriterion("sellerId >", value, "sellerid");
			return (Criteria) this;
		}

		public Criteria andSelleridGreaterThanOrEqualTo(String value) {
			addCriterion("sellerId >=", value, "sellerid");
			return (Criteria) this;
		}

		public Criteria andSelleridLessThan(String value) {
			addCriterion("sellerId <", value, "sellerid");
			return (Criteria) this;
		}

		public Criteria andSelleridLessThanOrEqualTo(String value) {
			addCriterion("sellerId <=", value, "sellerid");
			return (Criteria) this;
		}

		public Criteria andSelleridLike(String value) {
			addCriterion("sellerId like", value, "sellerid");
			return (Criteria) this;
		}

		public Criteria andSelleridNotLike(String value) {
			addCriterion("sellerId not like", value, "sellerid");
			return (Criteria) this;
		}

		public Criteria andSelleridIn(List<String> values) {
			addCriterion("sellerId in", values, "sellerid");
			return (Criteria) this;
		}

		public Criteria andSelleridNotIn(List<String> values) {
			addCriterion("sellerId not in", values, "sellerid");
			return (Criteria) this;
		}

		public Criteria andSelleridBetween(String value1, String value2) {
			addCriterion("sellerId between", value1, value2, "sellerid");
			return (Criteria) this;
		}

		public Criteria andSelleridNotBetween(String value1, String value2) {
			addCriterion("sellerId not between", value1, value2, "sellerid");
			return (Criteria) this;
		}

		public Criteria andSelleremailIsNull() {
			addCriterion("sellerEmail is null");
			return (Criteria) this;
		}

		public Criteria andSelleremailIsNotNull() {
			addCriterion("sellerEmail is not null");
			return (Criteria) this;
		}

		public Criteria andSelleremailEqualTo(String value) {
			addCriterion("sellerEmail =", value, "selleremail");
			return (Criteria) this;
		}

		public Criteria andSelleremailNotEqualTo(String value) {
			addCriterion("sellerEmail <>", value, "selleremail");
			return (Criteria) this;
		}

		public Criteria andSelleremailGreaterThan(String value) {
			addCriterion("sellerEmail >", value, "selleremail");
			return (Criteria) this;
		}

		public Criteria andSelleremailGreaterThanOrEqualTo(String value) {
			addCriterion("sellerEmail >=", value, "selleremail");
			return (Criteria) this;
		}

		public Criteria andSelleremailLessThan(String value) {
			addCriterion("sellerEmail <", value, "selleremail");
			return (Criteria) this;
		}

		public Criteria andSelleremailLessThanOrEqualTo(String value) {
			addCriterion("sellerEmail <=", value, "selleremail");
			return (Criteria) this;
		}

		public Criteria andSelleremailLike(String value) {
			addCriterion("sellerEmail like", value, "selleremail");
			return (Criteria) this;
		}

		public Criteria andSelleremailNotLike(String value) {
			addCriterion("sellerEmail not like", value, "selleremail");
			return (Criteria) this;
		}

		public Criteria andSelleremailIn(List<String> values) {
			addCriterion("sellerEmail in", values, "selleremail");
			return (Criteria) this;
		}

		public Criteria andSelleremailNotIn(List<String> values) {
			addCriterion("sellerEmail not in", values, "selleremail");
			return (Criteria) this;
		}

		public Criteria andSelleremailBetween(String value1, String value2) {
			addCriterion("sellerEmail between", value1, value2, "selleremail");
			return (Criteria) this;
		}

		public Criteria andSelleremailNotBetween(String value1, String value2) {
			addCriterion("sellerEmail not between", value1, value2, "selleremail");
			return (Criteria) this;
		}

		public Criteria andInvoiceamountIsNull() {
			addCriterion("invoiceAmount is null");
			return (Criteria) this;
		}

		public Criteria andInvoiceamountIsNotNull() {
			addCriterion("invoiceAmount is not null");
			return (Criteria) this;
		}

		public Criteria andInvoiceamountEqualTo(Double value) {
			addCriterion("invoiceAmount =", value, "invoiceamount");
			return (Criteria) this;
		}

		public Criteria andInvoiceamountNotEqualTo(Double value) {
			addCriterion("invoiceAmount <>", value, "invoiceamount");
			return (Criteria) this;
		}

		public Criteria andInvoiceamountGreaterThan(Double value) {
			addCriterion("invoiceAmount >", value, "invoiceamount");
			return (Criteria) this;
		}

		public Criteria andInvoiceamountGreaterThanOrEqualTo(Double value) {
			addCriterion("invoiceAmount >=", value, "invoiceamount");
			return (Criteria) this;
		}

		public Criteria andInvoiceamountLessThan(Double value) {
			addCriterion("invoiceAmount <", value, "invoiceamount");
			return (Criteria) this;
		}

		public Criteria andInvoiceamountLessThanOrEqualTo(Double value) {
			addCriterion("invoiceAmount <=", value, "invoiceamount");
			return (Criteria) this;
		}

		public Criteria andInvoiceamountIn(List<Double> values) {
			addCriterion("invoiceAmount in", values, "invoiceamount");
			return (Criteria) this;
		}

		public Criteria andInvoiceamountNotIn(List<Double> values) {
			addCriterion("invoiceAmount not in", values, "invoiceamount");
			return (Criteria) this;
		}

		public Criteria andInvoiceamountBetween(Double value1, Double value2) {
			addCriterion("invoiceAmount between", value1, value2, "invoiceamount");
			return (Criteria) this;
		}

		public Criteria andInvoiceamountNotBetween(Double value1, Double value2) {
			addCriterion("invoiceAmount not between", value1, value2, "invoiceamount");
			return (Criteria) this;
		}

		public Criteria andBuyerpayamountIsNull() {
			addCriterion("buyerPayAmount is null");
			return (Criteria) this;
		}

		public Criteria andBuyerpayamountIsNotNull() {
			addCriterion("buyerPayAmount is not null");
			return (Criteria) this;
		}

		public Criteria andBuyerpayamountEqualTo(Double value) {
			addCriterion("buyerPayAmount =", value, "buyerpayamount");
			return (Criteria) this;
		}

		public Criteria andBuyerpayamountNotEqualTo(Double value) {
			addCriterion("buyerPayAmount <>", value, "buyerpayamount");
			return (Criteria) this;
		}

		public Criteria andBuyerpayamountGreaterThan(Double value) {
			addCriterion("buyerPayAmount >", value, "buyerpayamount");
			return (Criteria) this;
		}

		public Criteria andBuyerpayamountGreaterThanOrEqualTo(Double value) {
			addCriterion("buyerPayAmount >=", value, "buyerpayamount");
			return (Criteria) this;
		}

		public Criteria andBuyerpayamountLessThan(Double value) {
			addCriterion("buyerPayAmount <", value, "buyerpayamount");
			return (Criteria) this;
		}

		public Criteria andBuyerpayamountLessThanOrEqualTo(Double value) {
			addCriterion("buyerPayAmount <=", value, "buyerpayamount");
			return (Criteria) this;
		}

		public Criteria andBuyerpayamountIn(List<Double> values) {
			addCriterion("buyerPayAmount in", values, "buyerpayamount");
			return (Criteria) this;
		}

		public Criteria andBuyerpayamountNotIn(List<Double> values) {
			addCriterion("buyerPayAmount not in", values, "buyerpayamount");
			return (Criteria) this;
		}

		public Criteria andBuyerpayamountBetween(Double value1, Double value2) {
			addCriterion("buyerPayAmount between", value1, value2, "buyerpayamount");
			return (Criteria) this;
		}

		public Criteria andBuyerpayamountNotBetween(Double value1, Double value2) {
			addCriterion("buyerPayAmount not between", value1, value2, "buyerpayamount");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table ct_alipay_trade
	 * @mbg.generated  Mon Aug 20 20:07:26 CST 2018
	 */
	public static class Criterion {
		private String condition;
		private Object value;
		private Object secondValue;
		private boolean noValue;
		private boolean singleValue;
		private boolean betweenValue;
		private boolean listValue;
		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}

	/**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table ct_alipay_trade
     *
     * @mbg.generated do_not_delete_during_merge Mon Aug 20 09:29:33 CST 2018
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
}