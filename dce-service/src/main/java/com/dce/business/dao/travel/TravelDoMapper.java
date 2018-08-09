package com.dce.business.dao.travel;

import com.dce.business.entity.travel.TravelDo;
import com.dce.business.entity.travel.TravelDoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TravelDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_apply_travel
     *
     * @mbg.generated Thu Aug 09 09:53:39 CST 2018
     */
    long countByExample(TravelDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_apply_travel
     *
     * @mbg.generated Thu Aug 09 09:53:39 CST 2018
     */
    int deleteByExample(TravelDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_apply_travel
     *
     * @mbg.generated Thu Aug 09 09:53:39 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_apply_travel
     *
     * @mbg.generated Thu Aug 09 09:53:39 CST 2018
     */
    int insert(TravelDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_apply_travel
     *
     * @mbg.generated Thu Aug 09 09:53:39 CST 2018
     */
    int insertSelective(TravelDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_apply_travel
     *
     * @mbg.generated Thu Aug 09 09:53:39 CST 2018
     */
    List<TravelDo> selectByExample(TravelDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_apply_travel
     *
     * @mbg.generated Thu Aug 09 09:53:39 CST 2018
     */
    TravelDo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_apply_travel
     *
     * @mbg.generated Thu Aug 09 09:53:39 CST 2018
     */
    int updateByExampleSelective(@Param("record") TravelDo record, @Param("example") TravelDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_apply_travel
     *
     * @mbg.generated Thu Aug 09 09:53:39 CST 2018
     */
    int updateByExample(@Param("record") TravelDo record, @Param("example") TravelDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_apply_travel
     *
     * @mbg.generated Thu Aug 09 09:53:39 CST 2018
     */
    int updateByPrimaryKeySelective(TravelDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ct_apply_travel
     *
     * @mbg.generated Thu Aug 09 09:53:39 CST 2018
     */
    int updateByPrimaryKey(TravelDo record);
}