package com.dce.business.entity.feedback;

import java.util.Date;

public class FeedBackDo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_user_feedback.feedbackid
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    private Integer feedbackid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_user_feedback.userid
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    private Integer userid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_user_feedback.feedbackcontent
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    private String feedbackcontent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_user_feedback.createtime
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    private Date createtime;
    
    

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
     * This field corresponds to the database column ct_user_feedback.replycontent
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    private String replycontent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_user_feedback.replytime
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    private Date replytime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ct_user_feedback.managerId
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    private Integer managerid;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_user_feedback.feedbackid
     *
     * @return the value of ct_user_feedback.feedbackid
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public Integer getFeedbackid() {
        return feedbackid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_user_feedback.feedbackid
     *
     * @param feedbackid the value for ct_user_feedback.feedbackid
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public void setFeedbackid(Integer feedbackid) {
        this.feedbackid = feedbackid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_user_feedback.userid
     *
     * @return the value of ct_user_feedback.userid
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_user_feedback.userid
     *
     * @param userid the value for ct_user_feedback.userid
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_user_feedback.feedbackcontent
     *
     * @return the value of ct_user_feedback.feedbackcontent
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public String getFeedbackcontent() {
        return feedbackcontent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_user_feedback.feedbackcontent
     *
     * @param feedbackcontent the value for ct_user_feedback.feedbackcontent
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public void setFeedbackcontent(String feedbackcontent) {
        this.feedbackcontent = feedbackcontent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_user_feedback.createtime
     *
     * @return the value of ct_user_feedback.createtime
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_user_feedback.createtime
     *
     * @param createtime the value for ct_user_feedback.createtime
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_user_feedback.replycontent
     *
     * @return the value of ct_user_feedback.replycontent
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public String getReplycontent() {
        return replycontent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_user_feedback.replycontent
     *
     * @param replycontent the value for ct_user_feedback.replycontent
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public void setReplycontent(String replycontent) {
        this.replycontent = replycontent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_user_feedback.replytime
     *
     * @return the value of ct_user_feedback.replytime
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public Date getReplytime() {
        return replytime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_user_feedback.replytime
     *
     * @param replytime the value for ct_user_feedback.replytime
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public void setReplytime(Date replytime) {
        this.replytime = replytime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ct_user_feedback.managerId
     *
     * @return the value of ct_user_feedback.managerId
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public Integer getManagerid() {
        return managerid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ct_user_feedback.managerId
     *
     * @param managerid the value for ct_user_feedback.managerId
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    public void setManagerid(Integer managerid) {
        this.managerid = managerid;
    }
}