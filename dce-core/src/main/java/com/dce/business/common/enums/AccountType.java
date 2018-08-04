package com.dce.business.common.enums;

/** 
 * 账户类型
 * @author parudy
 * @date 2018年3月26日 
 * @version v1.0
 */
public enum AccountType {
	/**原始仓钱包*/
	wallet_original("wallet_original", "原始币钱包"),
	/**释放币钱包*/
	wallet_original_release("wallet_original_release", "释放币钱包"),
	/**日息钱包*/
	wallet_interest("wallet_interest", "日息币钱包"),
	/**日息释放钱包*/
	//wallet_interest_release("wallet_interest_release", "日息币释放钱包"),
	/**奖金钱包*/
	wallet_bonus("wallet_bonus", "奖金币钱包"),
	/**奖金释放钱包*/
	//wallet_bonus_release("wallet_bonus_release", "奖金币释放钱包"),
	/**积分钱包*/
	wallet_score("wallet_score", "流通币钱包"),
	/**现金币钱包*/
	wallet_cash("wallet_cash", "现金币钱包"),
	/**可提币钱包*/
	wallet_release_release("wallet_release_release", "可提币钱包"),
	
    /**
     * 原始仓账户
     */
    original("original", "原始仓账户"),
    /** 
     * 现持仓账户
     * @return  
     */
    current("current", "现持仓账户"),
    /** 
     * 美元点账户
     * @return  
     */
    point("point", "美元点账户"),
    /** 
     * 锁仓账户
     * @return  
     */
    locked("locked", "锁仓"),
    /**
     * 积分账户
     */
    score("score", "积分账户");

    private String accountType;
    private String remark;

    AccountType(String accountType, String remark) {
        this.accountType = accountType;
        this.remark = remark;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static AccountType getAccountType(String accountType) {
        for (AccountType type : AccountType.values()) {
            if (type.getAccountType().equals(accountType)) {
                return type;
            }
        }

        return null;
    }
}
