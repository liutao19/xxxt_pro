package com.dce.business.actions.user;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.dce.business.actions.common.BaseController;
import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.DictCode;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.common.token.TokenUtil;
import com.dce.business.common.util.DataEncrypt;
import com.dce.business.common.util.NumberUtil;
import com.dce.business.dao.user.IUserAddressDao;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.entity.message.NewsDo;
import com.dce.business.entity.user.UserAddressDo;
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.award.IReleaseService;
import com.dce.business.service.dict.ILoanDictService;
import com.dce.business.service.message.INewsService;
import com.dce.business.service.user.IUserService;
import com.dce.business.service.user.UserAdressService;

/**
 * 账户处理器，注册、登录等
 * 
 * @author parudy
 * @date 2018年3月24日
 * @version v1.0
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
	private final static Logger logger = Logger.getLogger(UserController.class);

	@Resource
	private IUserService userService;
	@Resource
	private IAccountService accountService;
	@Resource
	private INewsService newsService;
	@Resource
	private ILoanDictService loanDictService;
	@Resource
	private UserAdressService addressService;
	// @Resource
	// private IReleaseService staticAwardService;

	/**
	 * 用户注册
	 * 
	 * @param userDo
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	public Result<?> reg(@Valid UserDo userDo, BindingResult bindingResult) {
		logger.info("用户注册");

		// 参数校验
		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			logger.info("用户注册，参数校验错误：" + JSON.toJSONString(errors));
			return Result.failureResult(errors.get(0).getDefaultMessage());
		}

		/*
		 * // 去掉手机号中的所有空格 if (StringUtils.isNotBlank(userDo.getMobile())) {
		 * userDo.setMobile(userDo.getMobile().replaceAll(" ", "")); //去掉所有空格 }
		 * 
		 * // 去掉身份号中的所有空格 if (StringUtils.isNotBlank(userDo.getIdnumber())) {
		 * String idnumber = userDo.getIdnumber().replaceAll(" ","");
		 * userDo.setIdnumber(idnumber); } // 去掉银行卡中的所有空格 if
		 * (StringUtils.isNotBlank(userDo.getBankContent())) { String
		 * bankContent = userDo.getBankContent().replaceAll(" ","");
		 * userDo.setBankContent(bankContent); }
		 */
		String refMobile = this.getString("refereeUserMobile");
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("mobile", refMobile);
		List<UserDo> refLst = userService.selectUser(params);
		if(refLst == null || refLst.size()<1){
			throw new BusinessException("无效的推荐人");
		}
		userDo.setRefereeid(refLst.get(0).getId());
		userDo.setParentid(refLst.get(0).getId());
		Result<?> result = userService.reg(userDo);

		logger.info("用户注册结果:" + JSON.toJSONString(result));
		return result;
	}

	/**
	 * 登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result<?> login() {
		// String mobile = getString("mobile");
		String userName = getString("userName");
		String password = getString("password");

		Assert.hasText(userName, "请输入用户名");
		Assert.hasText(password, "请输入密码");

		userName = userName.trim();
		password = password.trim();

		password = DataEncrypt.encrypt(password);

		logger.info("用户登录, userName:" + userName + "; password:" + password);
		UserDo userDo = userService.getUser(userName);
		Assert.notNull(userDo, "用户不存在");

		if (!userName.equals(userDo.getUserName()) || !password.equals(userDo.getUserPassword())) {
			return Result.failureResult("用户名或者密码不正确");
		}

		if (userDo.getStatus().intValue() != 0) {
			return Result.failureResult("当前用户已被锁定,不允许登录!");
		}
		String token = TokenUtil.createToken(userDo.getId());
		Map<String, Object> map = new HashMap<>();
		map.put("token", token);
		map.put("userId", userDo.getId());
		return Result.successResult("登录成功", map);
	}

	/**
	 * 注销
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public Result<?> logout() {
		Integer userId = getUserId();

		logger.info("用户注销, userId:" + userId);

		TokenUtil.deleteToken(userId);

		return Result.successResult("注销成功");
	}

	/**
	 * 个人中心查询个人信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/personalInfo", method = RequestMethod.GET)
	public Result<?> getUser() {
		Integer userId = getUserId();
		logger.info("查询用户基本信息，userId:" + userId);

		// 用户信息
		UserDo userDo = userService.getUser(userId);

		Map<String, Object> map = new HashMap<>();
		map.put("trueName", userDo.getTrueName()); // 姓名
		map.put("mobile", userDo.getMobile()); // 手机号码
		map.put("userLevel", userDo.getUserLevel()); // 用户等级
		map.put("idnumber", userDo.getIdnumber()); // 用户身份证号
		map.put("sex", userDo.getSex()); // 用户性别
		map.put("refereeid", userDo.getRefereeid()); // 用户推荐人
		// map.put("banktype", userDo.getBanktype()); //
		// map.put("bankUserName", userDo.getBankUserName());
		// map.put("banknumber", userDo.getBanknumber());
		// map.put("bankContent", userDo.getBankContent());
		// map.put("email", userDo.getEmail()); // 邮箱
		return Result.successResult("查询成功", map);
	}

	/**
	 * 修改登录密码
	 * 
	 * @return
	 */
	@RequestMapping(value = "/alterpas", method = RequestMethod.POST)
	public Result<?> updateUser() {
		try {
			Integer userId = getUserId();
			String password = getString("Password");
			logger.info("修改用户登录密码，userId:" + userId);
			// 用户信息
			UserDo userDo = new UserDo();
			userDo.setId(userId);
			// 登录密码加密
			if (StringUtils.isNotBlank(password)) {
				password = DataEncrypt.encrypt(password);
				// userDo.setTwoPassword(password);
			}
			return userService.update(userDo);
		} catch (Exception e) {
			return Result.failureResult("用户密码修改失败");
		}
	}

	@RequestMapping(value = "/addAddress", method = { RequestMethod.POST })
	public Result<?> addAddress() {
		String addressId = getString("addressId");
		String addressDetails = getString("addressDetails");
		Integer userId = getUserId();
		Assert.hasText(addressDetails, "收货地址详情不能为空");

		// 收货地址对像
		UserAddressDo addressadd = new UserAddressDo();
		addressadd.setUserid(userId);
		addressadd.setAddressDetails(addressDetails);

		// id为空是新记录 新增， 非空是已经存在的记录调用修改
		if (StringUtils.isNotBlank(addressId)) {
			addressadd.setAddressid(Integer.parseInt(addressId));
			addressService.updateByPrimaryKeySelective(addressadd);
		} else {
			addressService.insertSelective(addressadd);
		}
		return Result.successResult("地址修改成功");
	}

	/**
	 * 模糊搜索用户列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Result<?> list() {
		String userName = getString("userName");
		String type = getString("type"); // 查询类型，type=1表示推荐人、type=2表示接点人
		logger.info("模糊查询, userName:" + userName + "; type:" + type);

		Assert.hasText(userName, "请输入用户名");

		List<UserDo> list = userService.list(userName);

		return Result.successResult("查询成功", list);
	}

	/**
	 * 首页，查询用户基本信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public Result<?> getUserInfo() {
		Integer userId = getUserId();

		logger.info("查询用户基本信息，userId:" + userId);

		// 用户信息
		UserDo userDo = userService.getUser(userId);
		UserDo newUserDo = new UserDo();
		newUserDo.setId(userDo.getId());
		newUserDo.setUserName(userDo.getUserName());
		newUserDo.setTrueName(userDo.getTrueName());
		newUserDo.setUserLevel(userDo.getUserLevel());
		newUserDo.setReleaseTime(userDo.getReleaseTime()); // 释放时间
		newUserDo.setUserFace(userDo.getUserFace());
		newUserDo.setRefereeNumber(userDo.getRefereeNumber());
		newUserDo.setUserLevelName(userDo.getUserLevel() + "");
		// 用户等级
		List<LoanDictDtlDo> leves = loanDictService.queryDictDtlListByDictCode(DictCode.BaoDanFei.getCode());
		if (!CollectionUtils.isEmpty(leves)) {
			for (LoanDictDtlDo dtl : leves) {
				if (dtl.getCode().equals(userDo.getUserLevel() + "")) {
					newUserDo.setUserLevelName(dtl.getName());
					break;
				}
			}
		}
		// 推荐人
		if (userDo.getRefereeid() != null) {
			UserDo referee = userService.getUser(userDo.getRefereeid());
			if (referee != null) {
				newUserDo.setRefereeUserName(referee.getUserName());
			}
		}

		// 接点人
		if (userDo.getParentid() != null) {
			UserDo parent = userService.getUser(userDo.getParentid());
			if (parent != null) {
				newUserDo.setParentUserName(parent.getUserName());
			}
		}

		// 财务信息
		Map<String, Object> accountInfo = new HashMap<>();
		accountInfo.put("amount", NumberUtil.formatterBigDecimal(getAccountAmount(userId, AccountType.current))); // 现持仓
		accountInfo.put("originalAmount",
				NumberUtil.formatterBigDecimal(getAccountAmount(userId, AccountType.original))); // 原始仓
		accountInfo.put("pointAmount", NumberUtil.formatterBigDecimal(getAccountAmount(userId, AccountType.point))); // 美元点
		accountInfo.put("frozenDeposit", NumberUtil.formatterBigDecimal(getAccountAmount(userId, AccountType.locked))); // 锁仓

		BigDecimal _point = NumberUtil.formatterBigDecimal(getAccountAmount(userId, AccountType.original));
		BigDecimal _locked = NumberUtil.formatterBigDecimal(getAccountAmount(userId, AccountType.locked));

		Map<String, Object> topInfoMap = new HashMap<>();
		topInfoMap.put("decCount", "DEC/9901A");
		topInfoMap.put("total", _point.add(_locked));
		topInfoMap.put("canUse", _point);
		topInfoMap.put("apply", _locked);

		// DCE最新消息
		NewsDo message = newsService.selectLatestNews();
		if (message != null) {
			topInfoMap.put("dceMsg", message.getTitle());
		} else {

			topInfoMap.put("dceMsg", "");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("userInfo", newUserDo);
		map.put("userAccountDo", accountInfo);
		map.put("topInfo", topInfoMap);
		return Result.successResult("查询成功", map);
	}

	/**
	 * 查询账户余额
	 * 
	 * @param userId
	 * @param accountType
	 * @return
	 */
	private BigDecimal getAccountAmount(Integer userId, AccountType accountType) {
		UserAccountDo userAccountDo = accountService.getUserAccount(userId, accountType);
		if (userAccountDo != null && userAccountDo.getAmount() != null) {
			return userAccountDo.getAmount();
		}

		return BigDecimal.ZERO;
	}

	/**
	 * 用户信息认证
	 * 
	 * @return
	 */
	@RequestMapping(value = "/Authentication", method = RequestMethod.POST)
	public Result<?> Authentication() {
		try {
			Integer userId = getUserId();
			String trueName = getString("trueName");
			String mobile = getString("mobile");
			String idnumber = getString("idnumber");
			String sex = getString("sex");

			logger.info("用户信息，userId:" + userId);

			Assert.hasText(trueName, "姓名不能为空");
			Assert.hasText(mobile, "手机号码不能为空");
			Assert.hasText(idnumber, "身份证不能为空");
			Assert.hasText(sex, "性别不能为空");
			// 用户信息
			UserDo userDo = new UserDo();
			userDo.setId(userId);
			userDo.setTrueName(trueName);
			userDo.setIdnumber(idnumber);
			userDo.setSex(sex);

			return userService.Authentication(userDo);

		} catch (Exception e) {
			return Result.failureResult("用户信息认证失败");
		}
	}

	@RequestMapping(value = "toLevel", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView tosetLevel() {
		ModelAndView mv = new ModelAndView("jjzd/set_user_level");
		List<LoanDictDtlDo> KHJB = loanDictService.queryDictDtlListByDictCode(DictCode.KHJB.getCode());
		mv.addObject("KHJB", KHJB);
		return mv;
	}

	/**
	 * 空单激活
	 * 
	 * @return
	 */
	@RequestMapping(value = "/setUserLevel", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<?> setUserLevel() {
		String userCode = getString("userCode");
		String userLevel = getString("userLevel");
		logger.info("修改用户等级:userCode=" + userCode + ",userLevel=" + userLevel);
		if (StringUtils.isBlank(userCode)) {
			return Result.failureResult("请输入用户编码!");
		}
		if (StringUtils.isBlank(userLevel)) {
			return Result.failureResult("请选择用户级别");
		}
		try {
			Result<?> result = userService.setUserLevel(userCode.trim(), userLevel.trim());
			logger.info("修改用户等级结果:" + JSON.toJSONString(result));
			return result;
		} catch (BusinessException e) {
			logger.info("修改报错BusinessException:", e);
			return Result.failureResult(e.getMessage());
		} catch (Exception e) {
			logger.info("修改报错Exception:", e);
			return Result.failureResult("修改失败!");
		}
	}

	@RequestMapping(value = "listTotal", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listTotal() {
		ModelAndView mv = new ModelAndView("listTotal");
		List<LoanDictDtlDo> KHJB = loanDictService.queryDictDtlListByDictCode(DictCode.KHJB.getCode());
		mv.addObject("KHJB", KHJB);
		return mv;
	}
}
