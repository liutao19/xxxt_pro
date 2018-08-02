package com.dce.business.entity.etherenum;

import java.util.Date;

public class EthAccountDetailDo {
    private Long id;

    private Integer userId;

    private String serialNo;

    private String ethAccount;

    private Integer ethAccountType;

    private Integer transType;

    private String amount;

    private String gas;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getEthAccount() {
        return ethAccount;
    }

    public void setEthAccount(String ethAccount) {
        this.ethAccount = ethAccount;
    }

    public Integer getEthAccountType() {
        return ethAccountType;
    }

    public void setEthAccountType(Integer ethAccountType) {
        this.ethAccountType = ethAccountType;
    }

    public Integer getTransType() {
        return transType;
    }

    public void setTransType(Integer transType) {
        this.transType = transType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}