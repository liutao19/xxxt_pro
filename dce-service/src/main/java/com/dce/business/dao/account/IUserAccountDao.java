package com.dce.business.dao.account;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dce.business.entity.account.UserAccountDo;

public interface IUserAccountDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAccountDo record);

    int insertSelective(UserAccountDo record);

    UserAccountDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAccountDo record);

    int updateByPrimaryKey(UserAccountDo record);

    /**
     * 查询用户余额
     * @param params
     * @return
     */
    List<UserAccountDo> selectAccount(Map<String, Object> params);

    int updateUserAmountById(UserAccountDo bizUserAccountDo);

    /**
     * 我的收款码
     * @param userId
     * @return
     */
	String getMyQRCode(Integer userId);

	/**
	 * 创建收款码
	 * @param qrcode
	 * @param userId
	 */
	void insertQRCode(@Param("qrcode")String qrcode, @Param("userId") Integer userId);

	/**
	 * 根据收款码找用户id
	 * @param qrCode
	 * @return
	 */
	Integer getUserIdByQRCode(String qrCode);
	
	/**
	 * 分页分组查询用户账户信息
	 * @param prarams
	 * @return
	 */
	List<Map<String,Object>> selectAccountInfoByPage(Map<String,Object> prarams);

	List<UserAccountDo> sumAccount(Map<String,Object> emptyMap);
}
