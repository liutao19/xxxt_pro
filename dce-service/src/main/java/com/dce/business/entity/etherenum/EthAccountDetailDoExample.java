package com.dce.business.entity.etherenum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EthAccountDetailDoExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    public EthAccountDetailDoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
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

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUseridIsNull() {
            addCriterion("userId is null");
            return (Criteria) this;
        }

        public Criteria andUseridIsNotNull() {
            addCriterion("userId is not null");
            return (Criteria) this;
        }

        public Criteria andUseridEqualTo(Integer value) {
            addCriterion("userId =", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotEqualTo(Integer value) {
            addCriterion("userId <>", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThan(Integer value) {
            addCriterion("userId >", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThanOrEqualTo(Integer value) {
            addCriterion("userId >=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThan(Integer value) {
            addCriterion("userId <", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThanOrEqualTo(Integer value) {
            addCriterion("userId <=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridIn(List<Integer> values) {
            addCriterion("userId in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotIn(List<Integer> values) {
            addCriterion("userId not in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridBetween(Integer value1, Integer value2) {
            addCriterion("userId between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotBetween(Integer value1, Integer value2) {
            addCriterion("userId not between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andSerialnoIsNull() {
            addCriterion("serialNo is null");
            return (Criteria) this;
        }

        public Criteria andSerialnoIsNotNull() {
            addCriterion("serialNo is not null");
            return (Criteria) this;
        }

        public Criteria andSerialnoEqualTo(String value) {
            addCriterion("serialNo =", value, "serialno");
            return (Criteria) this;
        }

        public Criteria andSerialnoNotEqualTo(String value) {
            addCriterion("serialNo <>", value, "serialno");
            return (Criteria) this;
        }

        public Criteria andSerialnoGreaterThan(String value) {
            addCriterion("serialNo >", value, "serialno");
            return (Criteria) this;
        }

        public Criteria andSerialnoGreaterThanOrEqualTo(String value) {
            addCriterion("serialNo >=", value, "serialno");
            return (Criteria) this;
        }

        public Criteria andSerialnoLessThan(String value) {
            addCriterion("serialNo <", value, "serialno");
            return (Criteria) this;
        }

        public Criteria andSerialnoLessThanOrEqualTo(String value) {
            addCriterion("serialNo <=", value, "serialno");
            return (Criteria) this;
        }

        public Criteria andSerialnoLike(String value) {
            addCriterion("serialNo like", value, "serialno");
            return (Criteria) this;
        }

        public Criteria andSerialnoNotLike(String value) {
            addCriterion("serialNo not like", value, "serialno");
            return (Criteria) this;
        }

        public Criteria andSerialnoIn(List<String> values) {
            addCriterion("serialNo in", values, "serialno");
            return (Criteria) this;
        }

        public Criteria andSerialnoNotIn(List<String> values) {
            addCriterion("serialNo not in", values, "serialno");
            return (Criteria) this;
        }

        public Criteria andSerialnoBetween(String value1, String value2) {
            addCriterion("serialNo between", value1, value2, "serialno");
            return (Criteria) this;
        }

        public Criteria andSerialnoNotBetween(String value1, String value2) {
            addCriterion("serialNo not between", value1, value2, "serialno");
            return (Criteria) this;
        }

        public Criteria andEthaccountIsNull() {
            addCriterion("ethAccount is null");
            return (Criteria) this;
        }

        public Criteria andEthaccountIsNotNull() {
            addCriterion("ethAccount is not null");
            return (Criteria) this;
        }

        public Criteria andEthaccountEqualTo(String value) {
            addCriterion("ethAccount =", value, "ethaccount");
            return (Criteria) this;
        }

        public Criteria andEthaccountNotEqualTo(String value) {
            addCriterion("ethAccount <>", value, "ethaccount");
            return (Criteria) this;
        }

        public Criteria andEthaccountGreaterThan(String value) {
            addCriterion("ethAccount >", value, "ethaccount");
            return (Criteria) this;
        }

        public Criteria andEthaccountGreaterThanOrEqualTo(String value) {
            addCriterion("ethAccount >=", value, "ethaccount");
            return (Criteria) this;
        }

        public Criteria andEthaccountLessThan(String value) {
            addCriterion("ethAccount <", value, "ethaccount");
            return (Criteria) this;
        }

        public Criteria andEthaccountLessThanOrEqualTo(String value) {
            addCriterion("ethAccount <=", value, "ethaccount");
            return (Criteria) this;
        }

        public Criteria andEthaccountLike(String value) {
            addCriterion("ethAccount like", value, "ethaccount");
            return (Criteria) this;
        }

        public Criteria andEthaccountNotLike(String value) {
            addCriterion("ethAccount not like", value, "ethaccount");
            return (Criteria) this;
        }

        public Criteria andEthaccountIn(List<String> values) {
            addCriterion("ethAccount in", values, "ethaccount");
            return (Criteria) this;
        }

        public Criteria andEthaccountNotIn(List<String> values) {
            addCriterion("ethAccount not in", values, "ethaccount");
            return (Criteria) this;
        }

        public Criteria andEthaccountBetween(String value1, String value2) {
            addCriterion("ethAccount between", value1, value2, "ethaccount");
            return (Criteria) this;
        }

        public Criteria andEthaccountNotBetween(String value1, String value2) {
            addCriterion("ethAccount not between", value1, value2, "ethaccount");
            return (Criteria) this;
        }

        public Criteria andEthaccounttypeIsNull() {
            addCriterion("ethAccountType is null");
            return (Criteria) this;
        }

        public Criteria andEthaccounttypeIsNotNull() {
            addCriterion("ethAccountType is not null");
            return (Criteria) this;
        }

        public Criteria andEthaccounttypeEqualTo(Integer value) {
            addCriterion("ethAccountType =", value, "ethaccounttype");
            return (Criteria) this;
        }

        public Criteria andEthaccounttypeNotEqualTo(Integer value) {
            addCriterion("ethAccountType <>", value, "ethaccounttype");
            return (Criteria) this;
        }

        public Criteria andEthaccounttypeGreaterThan(Integer value) {
            addCriterion("ethAccountType >", value, "ethaccounttype");
            return (Criteria) this;
        }

        public Criteria andEthaccounttypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("ethAccountType >=", value, "ethaccounttype");
            return (Criteria) this;
        }

        public Criteria andEthaccounttypeLessThan(Integer value) {
            addCriterion("ethAccountType <", value, "ethaccounttype");
            return (Criteria) this;
        }

        public Criteria andEthaccounttypeLessThanOrEqualTo(Integer value) {
            addCriterion("ethAccountType <=", value, "ethaccounttype");
            return (Criteria) this;
        }

        public Criteria andEthaccounttypeIn(List<Integer> values) {
            addCriterion("ethAccountType in", values, "ethaccounttype");
            return (Criteria) this;
        }

        public Criteria andEthaccounttypeNotIn(List<Integer> values) {
            addCriterion("ethAccountType not in", values, "ethaccounttype");
            return (Criteria) this;
        }

        public Criteria andEthaccounttypeBetween(Integer value1, Integer value2) {
            addCriterion("ethAccountType between", value1, value2, "ethaccounttype");
            return (Criteria) this;
        }

        public Criteria andEthaccounttypeNotBetween(Integer value1, Integer value2) {
            addCriterion("ethAccountType not between", value1, value2, "ethaccounttype");
            return (Criteria) this;
        }

        public Criteria andTranstypeIsNull() {
            addCriterion("transType is null");
            return (Criteria) this;
        }

        public Criteria andTranstypeIsNotNull() {
            addCriterion("transType is not null");
            return (Criteria) this;
        }

        public Criteria andTranstypeEqualTo(Integer value) {
            addCriterion("transType =", value, "transtype");
            return (Criteria) this;
        }

        public Criteria andTranstypeNotEqualTo(Integer value) {
            addCriterion("transType <>", value, "transtype");
            return (Criteria) this;
        }

        public Criteria andTranstypeGreaterThan(Integer value) {
            addCriterion("transType >", value, "transtype");
            return (Criteria) this;
        }

        public Criteria andTranstypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("transType >=", value, "transtype");
            return (Criteria) this;
        }

        public Criteria andTranstypeLessThan(Integer value) {
            addCriterion("transType <", value, "transtype");
            return (Criteria) this;
        }

        public Criteria andTranstypeLessThanOrEqualTo(Integer value) {
            addCriterion("transType <=", value, "transtype");
            return (Criteria) this;
        }

        public Criteria andTranstypeIn(List<Integer> values) {
            addCriterion("transType in", values, "transtype");
            return (Criteria) this;
        }

        public Criteria andTranstypeNotIn(List<Integer> values) {
            addCriterion("transType not in", values, "transtype");
            return (Criteria) this;
        }

        public Criteria andTranstypeBetween(Integer value1, Integer value2) {
            addCriterion("transType between", value1, value2, "transtype");
            return (Criteria) this;
        }

        public Criteria andTranstypeNotBetween(Integer value1, Integer value2) {
            addCriterion("transType not between", value1, value2, "transtype");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(String value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(String value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(String value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(String value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(String value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(String value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLike(String value) {
            addCriterion("amount like", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotLike(String value) {
            addCriterion("amount not like", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<String> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<String> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(String value1, String value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(String value1, String value2) {
            addCriterion("amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andGasIsNull() {
            addCriterion("gas is null");
            return (Criteria) this;
        }

        public Criteria andGasIsNotNull() {
            addCriterion("gas is not null");
            return (Criteria) this;
        }

        public Criteria andGasEqualTo(String value) {
            addCriterion("gas =", value, "gas");
            return (Criteria) this;
        }

        public Criteria andGasNotEqualTo(String value) {
            addCriterion("gas <>", value, "gas");
            return (Criteria) this;
        }

        public Criteria andGasGreaterThan(String value) {
            addCriterion("gas >", value, "gas");
            return (Criteria) this;
        }

        public Criteria andGasGreaterThanOrEqualTo(String value) {
            addCriterion("gas >=", value, "gas");
            return (Criteria) this;
        }

        public Criteria andGasLessThan(String value) {
            addCriterion("gas <", value, "gas");
            return (Criteria) this;
        }

        public Criteria andGasLessThanOrEqualTo(String value) {
            addCriterion("gas <=", value, "gas");
            return (Criteria) this;
        }

        public Criteria andGasLike(String value) {
            addCriterion("gas like", value, "gas");
            return (Criteria) this;
        }

        public Criteria andGasNotLike(String value) {
            addCriterion("gas not like", value, "gas");
            return (Criteria) this;
        }

        public Criteria andGasIn(List<String> values) {
            addCriterion("gas in", values, "gas");
            return (Criteria) this;
        }

        public Criteria andGasNotIn(List<String> values) {
            addCriterion("gas not in", values, "gas");
            return (Criteria) this;
        }

        public Criteria andGasBetween(String value1, String value2) {
            addCriterion("gas between", value1, value2, "gas");
            return (Criteria) this;
        }

        public Criteria andGasNotBetween(String value1, String value2) {
            addCriterion("gas not between", value1, value2, "gas");
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

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated do_not_delete_during_merge Sat Aug 11 12:12:00 CST 2018
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table ct_ethereum_account_detail
     *
     * @mbg.generated Sat Aug 11 12:12:00 CST 2018
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
}