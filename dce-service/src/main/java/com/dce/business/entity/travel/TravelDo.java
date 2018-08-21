package com.dce.business.entity.travel;

import java.util.Date;

public class TravelDo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_apply_travel.id
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_apply_travel.userid
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    private Integer userid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_apply_travel.sex
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    private String sex;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_apply_travel.nation
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    private String nation;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_apply_travel.identity
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    private String identity;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_apply_travel.phone
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    private String phone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_apply_travel.address
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    private String address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_apply_travel.isbeen
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    private String isbeen;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_apply_travel.people
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    private Integer people;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_apply_travel.pathid
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    private Integer pathid;
    
    public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}

	@Override
	public String toString() {
		return "TravelDo [id=" + id + ", userid=" + userid + ", sex=" + sex + ", nation=" + nation + ", identity="
				+ identity + ", phone=" + phone + ", address=" + address + ", isbeen=" + isbeen + ", people=" + people
				+ ", pathid=" + pathid + ", linename=" + linename + ", truename=" + truename + ", createtime="
				+ createtime + ", state=" + state + "]";
	}

	private String linename;
	
	public String getTruename() {
		return truename;
	}

	public void setTruename(String trueName) {
		this.truename = trueName;
	}

	private String truename;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_apply_travel.createtime
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    private Date createtime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_apply_travel.state
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    private String state;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_apply_travel.id
     *
     * @return the value of ct_apply_travel.id
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_apply_travel.id
     *
     * @param id the value for ct_apply_travel.id
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_apply_travel.userid
     *
     * @return the value of ct_apply_travel.userid
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_apply_travel.userid
     *
     * @param userid the value for ct_apply_travel.userid
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_apply_travel.sex
     *
     * @return the value of ct_apply_travel.sex
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public String getSex() {
        return sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_apply_travel.sex
     *
     * @param sex the value for ct_apply_travel.sex
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_apply_travel.nation
     *
     * @return the value of ct_apply_travel.nation
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public String getNation() {
        return nation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_apply_travel.nation
     *
     * @param nation the value for ct_apply_travel.nation
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public void setNation(String nation) {
        this.nation = nation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_apply_travel.identity
     *
     * @return the value of ct_apply_travel.identity
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public String getIdentity() {
        return identity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_apply_travel.identity
     *
     * @param identity the value for ct_apply_travel.identity
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public void setIdentity(String identity) {
        this.identity = identity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_apply_travel.phone
     *
     * @return the value of ct_apply_travel.phone
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_apply_travel.phone
     *
     * @param phone the value for ct_apply_travel.phone
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_apply_travel.address
     *
     * @return the value of ct_apply_travel.address
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_apply_travel.address
     *
     * @param address the value for ct_apply_travel.address
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_apply_travel.isbeen
     *
     * @return the value of ct_apply_travel.isbeen
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public String getIsbeen() {
        return isbeen;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_apply_travel.isbeen
     *
     * @param isbeen the value for ct_apply_travel.isbeen
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public void setIsbeen(String isbeen) {
        this.isbeen = isbeen;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_apply_travel.people
     *
     * @return the value of ct_apply_travel.people
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public Integer getPeople() {
        return people;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_apply_travel.people
     *
     * @param people the value for ct_apply_travel.people
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public void setPeople(Integer people) {
        this.people = people;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_apply_travel.pathid
     *
     * @return the value of ct_apply_travel.pathid
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public Integer getPathid() {
        return pathid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_apply_travel.pathid
     *
     * @param pathid the value for ct_apply_travel.pathid
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public void setPathid(Integer pathid) {
        this.pathid = pathid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_apply_travel.createtime
     *
     * @return the value of ct_apply_travel.createtime
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_apply_travel.createtime
     *
     * @param createtime the value for ct_apply_travel.createtime
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_apply_travel.state
     *
     * @return the value of ct_apply_travel.state
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public String getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_apply_travel.state
     *
     * @param state the value for ct_apply_travel.state
     *
     * @mbg.generated Thu Aug 09 21:14:24 CST 2018
     */
    public void setState(String state) {
        this.state = state;
    }
}