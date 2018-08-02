package com.dce.business.entity.account;

import java.math.BigDecimal;
import java.util.Date;

public class UserAccountDo {
    private Integer id;

    private Integer userId;

    private String accountType;

    private BigDecimal amount;


    private BigDecimal totalConsumeAmount;

    private BigDecimal totalInocmeAmount;

    private BigDecimal incomeAmount;

    private BigDecimal consumeAmount;

    private String withdrawTotalDeposit;

    private Date updateTime;

    private String remark;
    
    /**
     * 流水id
     */
    private String seqId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotalConsumeAmount() {
        return totalConsumeAmount;
    }

    public void setTotalConsumeAmount(BigDecimal totalConsumeAmount) {
        this.totalConsumeAmount = totalConsumeAmount;
    }

    public BigDecimal getTotalInocmeAmount() {
        return totalInocmeAmount;
    }

    public void setTotalInocmeAmount(BigDecimal totalInocmeAmount) {
        this.totalInocmeAmount = totalInocmeAmount;
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public BigDecimal getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(BigDecimal consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public String getWithdrawTotalDeposit() {
        return withdrawTotalDeposit;
    }

    public void setWithdrawTotalDeposit(String withdrawTotalDeposit) {
        this.withdrawTotalDeposit = withdrawTotalDeposit;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getSeqId() {
		return seqId;
	}

	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}
    
}