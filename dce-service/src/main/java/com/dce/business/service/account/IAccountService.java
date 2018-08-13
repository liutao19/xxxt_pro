package com.dce.business.service.account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.common.result.Result;
import com.dce.business.entity.account.UserAccountDetailDo;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.user.UserDo;

public interface IAccountService {

	/**
	 * 根据用户id查询当前用户的流水
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserAccountDetailDo> selectUserAccountDetailByUserId(Integer userId);

	/**
	 * 新建账户
	 * 
	 * @param userAccountDo
	 * @return
	 */
	boolean createAccount(UserAccountDo userAccountDo);

	/**
	 * 查询用户账户信息
	 * 
	 * @param userId
	 * @param accountType
	 * @return
	 */
	UserAccountDo getUserAccount(Integer userId, AccountType accountType);

	UserAccountDo selectUserAccount(Integer userId, String accountType);

	/**
	 * 查询用户账户
	 * 
	 * @param
	 * @return
	 */
	List<UserAccountDo> selectUserAccount(Map<String, Object> params);

	int updateUserAmountById(UserAccountDo userAccountDo, IncomeType type);

	void convertBetweenAccount(Integer sourceUserId, Integer targetUserId, BigDecimal amount, String fromAccount,
			String toAccount, IncomeType sourceMsg, IncomeType targetMsg);

	/**
	 * 两个账户之间金额转换
	 * 
	 * @param sourceUserId
	 *            转出用户id
	 * @param targetUserId
	 *            转入用户id
	 * @param sourceAmount
	 *            转出金额
	 * @param targetAmount
	 *            转入金额
	 * @param fromAccount
	 *            转出账户类型
	 * @param toAccount
	 *            转入账户类型
	 * @param sourceMsg
	 *            转出备注
	 * @param targetMsg
	 *            注入备注
	 */
	void convertBetweenAccount(Integer sourceUserId, Integer targetUserId, BigDecimal sourceAmount,
			BigDecimal targetAmount, String fromAccount, String toAccount, IncomeType sourceMsg, IncomeType targetMsg);

	List<UserAccountDetailDo> selectUserAccountDetail(Map<String, Object> params);

	/**
	 * 初始化账户
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	Result<?> currentInit(Integer userId, AccountType type);

	/**
	 * 我的收款码
	 * 
	 * @param userId
	 * @return
	 */
	String getMyQRCode(Integer userId);

	/**
	 * 扫描支付
	 * 
	 * @param userId
	 * @param qrCode
	 * @param amount
	 * @param pwd
	 * @return
	 */
	Result<?> payByQRCode(Integer userId, String qrCode, String amount, String pwd);

	/**
	 * 根据收款码获取收款人信息
	 * 
	 * @param qrcode
	 * @return
	 */
	UserDo getReceiverQRCode(String qrcode);

	/**
	 * 发送
	 * 
	 * @param userId
	 * @param qrCode
	 * @param amount
	 * @param pwd
	 * @return
	 */
	Result<?> send(Integer userId, String receiveAddress, String amount, String pwd);

	/**
	 * 分页分组查询用户账户信息
	 * 
	 * @param page
	 * @param params
	 * @return
	 */
	public PageDo<Map<String, Object>> selectAccountInfoByPage(PageDo<Map<String, Object>> page,
			Map<String, Object> params);

	List<UserAccountDo> sumAccount();

	/**
	 * 分页查询用户账户详细信息
	 * 
	 * @param parameterMap
	 * @return
	 */
	public PageDo<UserAccountDetailDo> selectUserAccountDetailByPage(PageDo<UserAccountDetailDo> page,
			Map<String, Object> parameterMap);

}
