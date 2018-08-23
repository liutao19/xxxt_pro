package com.dce.business.service.impl.account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.dce.business.common.alipay.util.AlipayConfig;
import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.CurrencyType;
import com.dce.business.common.enums.DictCode;
import com.dce.business.common.enums.IncomeType;

import com.dce.business.common.pay.util.Trans;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.DataDecrypt;
import com.dce.business.common.util.DataEncrypt;
import com.dce.business.common.util.DateUtil;
import com.dce.business.dao.etherenum.IEthereumTransInfoDao;
import com.dce.business.dao.trade.IWithdrawalsDao;
import com.dce.business.dao.user.IUserDao;
import com.dce.business.dao.user.IUserParentDao;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.dict.CtCurrencyDo;
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.entity.etherenum.EthAccountPlatformDo;
import com.dce.business.entity.etherenum.EthereumAccountDo;
import com.dce.business.entity.etherenum.EthereumTransInfoDo;
import com.dce.business.entity.trade.WithdrawalsDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.entity.user.UserParentDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.account.IPayService;
import com.dce.business.service.account.ITransOutDailyService;
import com.dce.business.service.dict.ICtCurrencyService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.third.IEthereumPlatformService;
import com.dce.business.service.third.IEthereumService;
import com.dce.business.service.third.IEthereumTransInfoService;
import com.dce.business.service.user.IUserService;

@Service("payService")
public class PayServiceImpl implements IPayService {
	private final static Logger logger = Logger.getLogger(PayServiceImpl.class);

	@Resource
	private IUserService userService;
	@Resource
	private IEthereumService ethereumService;
	@Resource
	private IEthereumPlatformService ethereumPlatformService;
	@Resource
	private ICtCurrencyService ctCurrencyService;
	@Resource
	private IAccountService accountService;
	@Resource
	private ILoanDictService loanDictService;
	@Resource
	private IWithdrawalsDao withdrawDao;
	@Resource
	private IUserDao userDao;
	@Resource
	private IUserParentDao userParentDao;
	@Resource
	private ITransOutDailyService transOutDailyService;
	@Resource
	private IEthereumTransInfoDao etherenumTranInfodao;	
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Result<?> recharge(Integer userId, String password, BigDecimal qty) {

		//判断系统设置是否可交易
		CtCurrencyDo ct = ctCurrencyService.selectByName(CurrencyType.IBAC.name());
		if (ct == null || ct.getIs_ctstatus() == null || 0 == ct.getIs_ctstatus().intValue()) {
			logger.info("充币状态设置关闭,当前不允许充币....");
			return Result.failureResult("当前系统不允许充币!");
		}

		//充值qty个现金币所需的以太坊币数量
		BigDecimal ethqty = point2Eth(qty, 1);
		
		//1、校验用户金额是否足够
		BigDecimal ethereumAmount = ethereumService.getEthernumAmount(userId);
		logger.info("账户余额:" + ethereumAmount.toString() + ",输入金额:" + qty.toString() + "输入金额所需以太坊数量:" + ethqty);
		if (ethqty.compareTo(ethereumAmount) > 0) {
			logger.info("账户余额不足");
			return Result.failureResult("账户余额不足");
		}

		UserDo userDo = userService.getUser(userId);
		//2、校验密码是否正确
		password = DataEncrypt.encrypt(password);
		if (!userDo.getTwoPassword().equals(password)) {
			logger.info("交易密码不正确");
			return Result.failureResult("交易密码不正确");
		}

		//3、调用以太坊接口，异步
		EthereumAccountDo userAccount = ethereumService.getByUserId(userId);
		EthAccountPlatformDo platAccount = ethereumPlatformService.getPlatformAccount();
		Result<?> result = trans(userAccount, platAccount, ethqty,qty,  1, null);

		return result;
	}

	/**
	 * 提现申请
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Result<?> withdraw(Integer userId, String password, String type, BigDecimal qty, String bank_no) {

		/*
		 * //判断系统设置是否可交易 CtCurrencyDo ct =
		 * ctCurrencyService.selectByName(CurrencyType.IBAC.name()); if (ct ==
		 * null || ct.getIs_tbstatus() == null || 0 ==
		 * ct.getIs_tbstatus().intValue()) {
		 * logger.info("提币状态设置关闭,当前不允许提币...."); return
		 * Result.failureResult("当前系统不允许提币!"); }
		 */

		// 1、校验用户金额是否足够
		UserAccountDo account = accountService.getUserAccount(userId, AccountType.wallet_money);

		System.out.println("用户余额------》》》" + accountService.getUserAccount(userId, AccountType.wallet_money));
		if (account == null || account.getAmount() == null || qty.compareTo(account.getAmount()) > 0) {
			return Result.failureResult("现金币账户余额不足");
		}
		int nm=qty.intValue()%100;
		
		System.err.println("整提----》》》"+nm);
		
		if(nm!=0){
			return Result.failureResult("只能整100整100的提");
		}
		
		
		if(qty.intValue()<100){
			return Result.failureResult("提现金额不能小于100");
		}

		UserDo userDo = userService.getUser(userId);
		// 2、校验密码是否正确
		password = DataEncrypt.encrypt(password);
		if (!userDo.getTwoPassword().equals(password)) {
			return Result.failureResult("交易密码不正确");
		}

		// 3,判断提现方式，提现到支付宝还是微信

		if (type.equals("1")) {

			System.out.println("提现到支付宝");

		} else {

			System.out.println("提现到微信");
		}

		// 账户校验
		EthereumAccountDo ethereumAccountDo = ethereumService.getByUserId(userId);
		if (ethereumAccountDo == null) {
			return Result.failureResult("请先获取以太坊地址再提现");
		}

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("qty", qty);
		param.put("userId", userId);

		// 用户余额减去
		int mk = accountService.updateMoney(param);

		if (mk != 0) {
			// 写提现表
			WithdrawalsDo record = new WithdrawalsDo();
			record.setUserid(userId);
			record.setProcessStatus("1");
			record.setAmount(qty);
			record.setWithdrawDate((new Date()).getTime() / 1000);
			record.setType(type);
			record.setBankNo(bank_no);
			withdrawDao.insertSelective(record);

			return Result.successResult("提现申请成功");

		}
		return Result.successResult("提现申请失败");

	}

	/**
	 * 提现
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Result<?> withdraw(Integer withdrawId, Integer userId, BigDecimal qty,String bankNo) {
		Result<?> result = Result.successResult("审核成功!") ;
		Map<String,String> resultMap=new HashMap<String,String>();

		//1、校验用户金额是否足够
		/*UserAccountDo account = accountService.getUserAccount(userId, AccountType.wallet_money);			
		//System.out.println("用户余额------》》》"+accountService.getUserAccount(userId, AccountType.wallet_money));
		if (account == null || account.getAmount() == null || qty.compareTo(account.getAmount()) > 0) {
			return Result.failureResult("账户余额不足");
		}*/
		WithdrawalsDo withdraw=new WithdrawalsDo();
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		String orderId=getOrderIdByUUId();
		request.setBizContent("{" +
		"\"out_biz_no\":"+orderId+","+
		"\"payee_type\":\"ALIPAY_LOGONID\"," +
		"\"payee_account\":\"13378068577\"," +
		"\"amount\":"+0.1+"," +
		"\"remark\":\"提现\"" +
		"}");
		AlipayFundTransToaccountTransferResponse response = null;
		try {
			response = alipayClient.execute(request);
			if("10000".equals(response.getCode())){
				result.setMsg("转账成功");
				withdraw.setWithdraw_status("1"); 
				withdraw.setOrderId(DataEncrypt.encrypt(response.getOrderId()));
				withdraw.setOutbizno(DataEncrypt.encrypt(response.getOutBizNo()));
				withdraw.setPaymentDate((new Date()).getTime() / 1000);
				EthereumTransInfoDo trans=new EthereumTransInfoDo();
				trans.setUserid(userId);      //用户id
				trans.setActualamount(qty.toString());     //转出金额

				trans.setAmount(qty.toString());    //转出金额

				trans.setCreatetime(new Date());

				trans.setStatus("true");      //状态
				trans.setType(2);      //类型
				trans.setToaccount(DataEncrypt.encrypt(bankNo));    //转入地址
				etherenumTranInfodao.insertSelective(trans);
			}else{
				withdraw.setWithdraw_status("0"); 
				return Result.failureResult(response.getSubMsg());
			}
			withdraw.setId(withdrawId);
			withdrawDao.updateWithDrawStatus(withdraw);
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Result.failureResult(response.getSubMsg());
		}
		return result;
	}
	public static String getOrderIdByUUId() {  
	    int machineId = 1;//最大支持1-9个集群机器部署  
		int hashCodeV = UUID.randomUUID().toString().hashCode();  
		if(hashCodeV < 0) {//有可能是负数  
		hashCodeV = - hashCodeV;  
		}  
		// 0 代表前面补充0       
		// 4 代表长度为4       
		// d 代表参数为正数型  
		return machineId+String.format("%015d", hashCodeV);  
	}  
	private Result<?> trans(EthereumAccountDo userAccount, EthAccountPlatformDo platAccount, BigDecimal amount, BigDecimal pointamount,
			Integer type, Integer withdrawId) {
		logger.info("PayServiceImpl.trans");
		String fromAccount = null;//转出账号
		String toAccount = null; //转入账号
		String password = null; //交易密码

		BigDecimal fee = BigDecimal.ZERO;
		if (type == 1) { //充值
			fromAccount = userAccount.getAccount();
			password = DataDecrypt.decrypt(userAccount.getPassword());
			toAccount = platAccount.getAccount();
		} else if (type == 2) { //提现
			fromAccount = platAccount.getAccount();
			password = DataDecrypt.decrypt(platAccount.getPassword());
			toAccount = userAccount.getAccount();

			//手续费，如果提现扣除手续费  
			LoanDictDtlDo withDrawFee = loanDictService.getLoanDictDtl(DictCode.WithDrawFee.getCode(), DictCode.WithDrawFee.getCode());
			if (withDrawFee != null && StringUtils.isNotBlank(withDrawFee.getRemark())) {

				fee = amount.multiply(new BigDecimal(withDrawFee.getRemark()));
			}
			amount = amount.subtract(fee);
		}

		ethereumService.trans(userAccount.getUserid(), fromAccount, toAccount, password, amount, pointamount.toString(),
				type, fee, withdrawId);

		return Result.successResult("充值正在处理中");
	}

	/** 
	 * 以太坊换算现金券
	 * @param ethAmount
	 * @return  
	 */
	public BigDecimal eth2Point(BigDecimal ethAmount) {
		BigDecimal usdPrice = ethereumService.getMarketPrice(); //美元价格
		
		//计算以太坊值多少美元
		BigDecimal ytf2$ = ethAmount.multiply(usdPrice).setScale(8, RoundingMode.HALF_UP);
		
		//如果getExRate()获得的是现金币与人民币的比例关系  那么下面这行代码是计算以太坊值多少人民币 6进7出
		//如果是美元比例  则下面这行代码需要注释掉
		ytf2$ = ytf2$.multiply(new BigDecimal(6));
		
		//现金币数量 = （以太坊人民币价值）/现金币与人民币比例
		BigDecimal pointQty = ytf2$.divide(getExRate(), 6, RoundingMode.HALF_UP);

		return pointQty;
	}

	/** 
	 * 充 pointAmount 现金券需要多少  以太坊 这里是充值转  所以是6进
	 * @param pointAmount 现金币数量
	 * @param type 转换类型 1：充值 2：提现   -----  如果是充值  则美元基数取6 如果是提现 则美元基数取7
	 * @return  
	 */
	private BigDecimal point2Eth(BigDecimal pointAmount,int type) {
		//以太坊美元价格
		BigDecimal usdPrice = ethereumService.getMarketPrice(); //美元价格
		
		//现金币转成美元  计算提现的现金币值多少美元
		BigDecimal xjb2$ = pointAmount.multiply(getExRate()).setScale(6, RoundingMode.HALF_UP);
		//如果getExRate()得到的是现金币和人民币的价格  则上面的价格计算的是现金币转人民币的价值  
		//转美元需要在除以7  6进7出   getExRate()是美元值  则下面这行代码可以注释掉
		if(type == 1){
			
			xjb2$ = xjb2$.divide(new BigDecimal(6),6, RoundingMode.HALF_UP);
		}else{
			
			xjb2$ = xjb2$.divide(new BigDecimal(7),6, RoundingMode.HALF_UP);
		}
		//以太坊币数量 = (现金币 转成美元值)/以太坊价格
		BigDecimal ethQty = xjb2$.divide(usdPrice, 8, RoundingMode.HALF_UP);

		return ethQty;
	}

	/**
	 * 获取现金币与人民币换算关系  1现金币=?人民币
	 * @return  
	 */
	private BigDecimal getExRate() {
		LoanDictDtlDo loanDictDtlDo = loanDictService.getLoanDictDtl(DictCode.Point2RMB.getCode(), DictCode.Point2RMB.getCode());
		Assert.notNull(loanDictDtlDo, "提现美元点转美元比例未设置");
		Assert.hasText(loanDictDtlDo.getRemark(), "提现美元点转美元比例未设置");
		BigDecimal rate = new BigDecimal(loanDictDtlDo.getRemark());
		return rate;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Result<?> transOut(Integer userId, BigDecimal qty, String accountType, String receiver,String password) {

		UserDo userDo = userService.getUser(userId);
		//2、校验密码是否正确
		password = DataEncrypt.encrypt(password);
		if (!userDo.getTwoPassword().equals(password)) {
			logger.info("交易密码不正确");
			return Result.failureResult("交易密码不正确");
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("userName", receiver);
		List<UserDo> receiverList = userDao.selectUser(params);
		if (CollectionUtils.isEmpty(receiverList)) {
			return Result.failureResult("未查询到接收人");
		}
		if (receiverList.size() > 1) {
			return Result.failureResult("根据编号查询到多个接收人,不能确定具体接收人!");
		}

		UserDo receiverUser = receiverList.get(0);

		//判断用户和接受用户是否在转出的范围
		//checkTransOut(userId, receiverUser);

		//查询转出用户的账号余额是否充足
		UserAccountDo account = accountService.selectUserAccount(userId, accountType);
		if (account == null) {
			return Result.failureResult("余额不足，转出失败");
		}

		if (account.getAmount().compareTo(qty) < 0) {
			return Result.failureResult("余额不足，转出失败");
		}

		//现金券钱包不做限制
		if (!AccountType.wallet_money.getAccountType().equals(accountType)) {
			if (!transOutDailyService.tryTransOut(userId, accountType, qty)) {
				return Result.failureResult("转出超出限制");
			}
		}

		try {
			AccountType toAccountType = AccountType.wallet_money; //转入钱包，默认是积分钱包
			if (AccountType.wallet_money.getAccountType().equals(accountType)) {
				toAccountType = AccountType.wallet_money; //如果是现金券钱包，默认转到现金券钱包
			}
			accountService.convertBetweenAccount(userId, receiverUser.getId(), qty, accountType, toAccountType.getAccountType(),
					IncomeType.TYPE_POINT_OUT, IncomeType.TYPE_POINT_IN);

			return Result.successResult("转出成功");
		} catch (Exception e) {
			logger.error("美元点转出报错:" + e);
			throw e;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Result<?> buyGoods(Integer userId, BigDecimal totalPrice) {
		UserAccountDo updateAccount = new UserAccountDo();
		updateAccount.setAccountType(AccountType.wallet_money.getAccountType());
		updateAccount.setAmount(totalPrice.negate());
		updateAccount.setUserId(userId);
		accountService.updateUserAmountById(updateAccount, IncomeType.TYPE_GOODS_BUY);
		return Result.successResult("购买商品成功");
	}

	@SuppressWarnings("unused")
	private Result<?> checkTransOut(Integer userId, UserDo receiverUser) {

		UserDo sourceUser = userService.getUser(userId);
		if (sourceUser.getTranDirect() != null && sourceUser.getTranDirect().contains("up,down")) {
			Map<String, Object> parentParams = new HashMap<String, Object>();
			parentParams.put("userid", userId);
			parentParams.put("parentid", receiverUser.getId());
			List<UserParentDo> relationLst = userParentDao.select(parentParams);
			if (CollectionUtils.isEmpty(relationLst)) {
				parentParams.put("userid", receiverUser.getId());
				parentParams.put("parentid", userId);
				relationLst = userParentDao.select(parentParams);
			}

			if (CollectionUtils.isEmpty(relationLst)) {
				return Result.failureResult("转出受限，接受人不是上下级关系");
			}

		} else if (sourceUser.getTranDirect() != null && sourceUser.getTranDirect().contains("up")) {
			Map<String, Object> parentParams = new HashMap<String, Object>();
			parentParams.put("userid", userId);
			parentParams.put("parentid", receiverUser.getId());
			List<UserParentDo> relationLst = userParentDao.select(parentParams);
			if (CollectionUtils.isEmpty(relationLst)) {
				return Result.failureResult("转出受限，接受人不是上级");
			}

		} else if (sourceUser.getTranDirect() != null && sourceUser.getTranDirect().contains("down")) {
			Map<String, Object> parentParams = new HashMap<String, Object>();
			parentParams.put("userid", receiverUser.getId());
			parentParams.put("parentid", userId);
			userParentDao.select(parentParams);
			List<UserParentDo> relationLst = userParentDao.select(parentParams);
			if (CollectionUtils.isEmpty(relationLst)) {
				return Result.failureResult("转出受限，接受人不是下级");
			}
		}

		return Result.successResult(null);
	}
	
	public Trans withdraw(WithdrawalsDo withdraw){
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);		AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
		request.setBizContent("{" +
		"\"out_biz_no\":"+withdraw.getOutbizno()+"," +
		"\"order_id\":"+withdraw.getOrderId()+"," +
		"  }");
		AlipayFundTransOrderQueryResponse response;
		Trans trans=new Trans();
		try {
			response = alipayClient.execute(request);
			String status = null;
			if(response.isSuccess()){
				System.out.println("调用成功");
				trans.setArrival_time_end(response.getArrivalTimeEnd());
				trans.setFail_reason(response.getFailReason());
				trans.setOrder_fee(response.getOrderFee());
				trans.setOrder_id(response.getOrderId());
				trans.setOut_biz_no(response.getOutBizNo());
				trans.setPay_date(response.getPayDate());
				if(response.getStatus().equals("SUCCESS")){
					status="成功";
				}
				else if(response.getStatus().equals("FAIL")){
					status=response.getFailReason();
				}
				else if(response.getStatus().equals("DEALING")){
					status="处理中";
				}
				else if(response.getStatus().equals("REFUND")){
					status="退票";
				}
				trans.setStatus(status);
			} else {
				System.out.println("调用失败");
			}
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return trans;

	}
}
