package com.dce.business.entity.trade;

import java.math.BigDecimal;

public class WithdrawalsDo {
    private Integer id;

    private Long withdrawDate;

    private BigDecimal amount;

    private BigDecimal fackReceive;

    private Long paymentDate;

    private Long confirmDate;

    private Integer userid;

    private BigDecimal fee;

    private String name;

    private String bank;

    private String bankContent;

    private String bankNo;

    private String processStatus;

    private String type;

    private String moneyType;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

   

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFackReceive() {
        return fackReceive;
    }

    public void setFackReceive(BigDecimal fackReceive) {
        this.fackReceive = fackReceive;
    }


	public Long getWithdrawDate() {
		return withdrawDate;
	}

	public void setWithdrawDate(Long withdrawDate) {
		this.withdrawDate = withdrawDate;
	}

	public Long getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Long paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Long getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Long confirmDate) {
		this.confirmDate = confirmDate;
	}

	public void setWithdrawDate(long withdrawDate) {
		this.withdrawDate = withdrawDate;
	}


    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankContent() {
        return bankContent;
    }

    public void setBankContent(String bankContent) {
        this.bankContent = bankContent;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	@Override
	public String toString() {
		return "WithdrawalsDo [id=" + id + ", withdrawDate=" + withdrawDate
				+ ", amount=" + amount + ", fackReceive=" + fackReceive
				+ ", paymentDate=" + paymentDate + ", confirmDate="
				+ confirmDate + ", userid=" + userid + ", fee=" + fee
				+ ", name=" + name + ", bank=" + bank + ", bankContent="
				+ bankContent + ", bankNo=" + bankNo + ", processStatus="
				+ processStatus + ", type=" + type + ", moneyType=" + moneyType
				+ ", remark=" + remark + ", getId()=" + getId()
				+ ", getAmount()=" + getAmount() + ", getFackReceive()="
				+ getFackReceive() + ", getWithdrawDate()=" + getWithdrawDate()
				+ ", getPaymentDate()=" + getPaymentDate()
				+ ", getConfirmDate()=" + getConfirmDate() + ", getUserid()="
				+ getUserid() + ", getFee()=" + getFee() + ", getName()="
				+ getName() + ", getBank()=" + getBank()
				+ ", getBankContent()=" + getBankContent() + ", getBankNo()="
				+ getBankNo() + ", getProcessStatus()=" + getProcessStatus()
				+ ", getType()=" + getType() + ", getMoneyType()="
				+ getMoneyType() + ", getRemark()=" + getRemark()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
    
}