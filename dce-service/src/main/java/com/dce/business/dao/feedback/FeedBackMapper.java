package com.dce.business.dao.feedback;

import com.dce.business.entity.feedback.FeedBackDo;
import com.dce.business.entity.feedback.FeedBackExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface FeedBackMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_user_feedback
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    long countByExample(FeedBackExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_user_feedback
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    int deleteByExample(FeedBackExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_user_feedback
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    int deleteByPrimaryKey(Integer feedbackid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_user_feedback
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    int insert(FeedBackDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_user_feedback
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    int insertSelective(FeedBackDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_user_feedback
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    List<FeedBackDo> selectByExample(FeedBackExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_user_feedback
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    FeedBackDo selectByPrimaryKey(Integer feedbackid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_user_feedback
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    int updateByExampleSelective(@Param("record") FeedBackDo record, @Param("example") FeedBackExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_user_feedback
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    int updateByExample(@Param("record") FeedBackDo record, @Param("example") FeedBackExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_user_feedback
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    int updateByPrimaryKeySelective(FeedBackDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_user_feedback
     *
     * @mbg.generated Tue Aug 07 14:20:15 CST 2018
     */
    int updateByPrimaryKey(FeedBackDo record);

	List<FeedBackDo> selectFeedBack(Map parameterMap);

	List<FeedBackDo> queryListPage(Map<String, Object> param);
}