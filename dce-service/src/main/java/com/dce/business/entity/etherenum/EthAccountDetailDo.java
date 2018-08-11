package com.dce.business.entity.etherenum;


public class EthAccountDetailDo {
    private Long id;

    private Integer userId; //用户id

    private String serialNo; //交易流水号

    private String ethAccount; 

    private Integer ethAccountType; //账户类型：默认0个人账户、1平台账户

    private Integer transType; //交易类型1转入2转出

    private String amount; //交易金额

    private String gas; //交易手续费

    private String createTime; //交易时间
    
    private String remark; //备注

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}