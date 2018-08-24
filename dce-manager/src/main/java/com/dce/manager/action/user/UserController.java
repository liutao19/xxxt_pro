package com.dce.manager.action.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.DataDecrypt;
import com.dce.business.common.util.DataEncrypt;
import com.dce.business.common.util.DateUtil;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.page.PageDoUtil;
import com.dce.business.entity.page.Pagination;
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.user.IUserService;
import com.dce.manager.action.BaseAction;

@Controller
@RequestMapping("/user")
public class UserController extends BaseAction {

	@Autowired
	private IUserService userService;

	@RequestMapping("/index")
	public String index() {
		return "/user/index";
	}

	/**
	 * 封号管理
	 * 
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("/lockUser")
	public void lockUser(HttpServletRequest request, HttpServletResponse response) {
		String userId = getString("userId");
		String optType = getString("optType");
		if (StringUtils.isBlank(userId)) {
			outPrint(response, Result.failureResult("请选择用户!"));
			return;
		}
		UserDo user = userService.getUser(Integer.parseInt(userId));
		try {

			UserDo _user = new UserDo();
			_user.setId(user.getId());
			_user.setStatus(Byte.valueOf(optType));
			Result<?> result = userService.update(_user);
			logger.info("冻结用户结果:" + JSON.toJSONString(result));
			outPrint(response, JSON.toJSONString(result));
		} catch (BusinessException e) {
			logger.info("冻结用户报错BusinessException:", e);
			outPrint(response, Result.failureResult(e.getMessage()));
		} catch (Exception e) {
			logger.info("冻结用户报错Exception:", e);
			outPrint(response, Result.failureResult("修改失败!"));
		}
	}

	/**
	 * 报单总计
	 * 
	 * @param pagination
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/list")
	public void list(Pagination<UserDo> pagination, HttpServletRequest request, HttpServletResponse response) {
		try {
			PageDo<UserDo> page = PageDoUtil.getPage(pagination);
			String userName = getString(request, "userName");
			String userMobile = getString(request, "userMobile");
			String startDate = getString(request, "startDate");
			String endDate = getString(request, "endDate");

			Map<String, Object> params = new HashMap<String, Object>();

			if (StringUtils.isNotBlank(userName)) {
				params.put("userName", userName);
			}
			if (StringUtils.isNotBlank(userMobile)) {
				params.put("mobile", userMobile);
			}
			if (StringUtils.isNotBlank(startDate)) {
				long std = DateUtil.getTimeStamp(startDate);
				params.put("startDate", std);
			}
			if (StringUtils.isNotBlank(endDate)) {
				long etd = DateUtil.getTimeStamp(endDate);
				params.put("endDate", etd);
			}
			PageDo<UserDo> usersList = userService.selectUserByPage(page, params);

			Long amount = userService.selectBaoDanAmount(params);

			if (!CollectionUtils.isEmpty(usersList.getModelList())) {
				for (UserDo user : usersList.getModelList()) {
					user.setUserPassword(DataDecrypt.decrypt(user.getUserPassword()));
					user.setTwoPassword(DataDecrypt.decrypt(user.getTwoPassword()));
				}
			}

			UserDo sum = new UserDo();
			sum.setUserName("报单总计:" + (amount == null ? 0 : amount));
			usersList.getModelList().add(sum);

			pagination = PageDoUtil.getPageValue(pagination, usersList);
			outPrint(response, JSON.toJSONString(pagination));
		} catch (Exception e) {
			logger.error("显示用户数据异常", e);
			throw new BusinessException("系统繁忙，请稍后再试");
		}
	}

	@RequestMapping("/activityIndex")
	public String activityIndex() {
		return "/user/activitiUser";
	}

	/**
	 * 修改用户等级
	 * 
	 * @param response
	 */
	@RequestMapping(value = "/activityUser", method = { RequestMethod.GET, RequestMethod.POST })
	public void activityUser(HttpServletResponse response) {
		String userName = getString("userName");
		logger.info("修改用户等级:userCode=" + userName);
		if (StringUtils.isBlank(userName)) {
			outPrint(response, Result.failureResult("请输入用户编码!"));
			return;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName", userName);
		List<UserDo> list = userService.selectUser(params);
		if (CollectionUtils.isEmpty(list)) {
			outPrint(response, JSON.toJSONString(Result.failureResult("用户不存在!")));
			return;
		}

		if (list.size() > 1) {
			outPrint(response, Result.failureResult("该用户名以被占用,请换一个!"));
			return;
		}
		try {
			UserDo user = list.get(0);
			UserDo _user = new UserDo();
			_user.setId(user.getId());
			_user.setIsActivated(1);
			Result<?> result = userService.update(_user);
			logger.info("激活用户结果:" + JSON.toJSONString(result));
			outPrint(response, JSON.toJSONString(result));
		} catch (BusinessException e) {
			logger.info("激活用户报错BusinessException:", e);
			outPrint(response, Result.failureResult(e.getMessage()));
		} catch (Exception e) {
			logger.info("激活用户报错Exception:", e);
			outPrint(response, Result.failureResult("修改失败!"));
		}

	}

	@RequestMapping(value = "/orgtree", method = { RequestMethod.GET, RequestMethod.POST })
	public String orgtree(ModelMap modelMap) {
		String userId = getString("userId");
		getRequest().getSession().setAttribute("hadReq", "1");
		modelMap.addAttribute("userId", userId);
		return "/user/orgtree";
	}

	/**
	 * 查看直推树
	 * 
	 * @param response
	 */
	@RequestMapping(value = "/listMyRef", method = RequestMethod.POST)
	@ResponseBody
	public void listMyRef(HttpServletResponse response) {
		Integer userId = Integer.parseInt(getString("userId"));
		String refereeid = getString("userid");

		logger.info("查看直推树:refereeid=" + refereeid);
		if (StringUtils.isBlank(refereeid)) {
			refereeid = userId + "";
		}
		String pageNum = getString("pageNum");
		// 不传 默认查询第一页
		if (StringUtils.isBlank(pageNum)) {
			pageNum = "1";
		}

		// 默认每页显示10条
		int rows = 100;

		int offset = (Integer.parseInt(pageNum) - 1) * rows;

		List<Map<String, Object>> userList = userService.listMyRef(Integer.parseInt(refereeid), offset, rows);
		String hadReq = (String) getRequest().getSession().getAttribute("hadReq");
		if (userId.intValue() == Integer.parseInt(refereeid) && StringUtils.isNotBlank(hadReq)) {
			UserDo user = userService.getUser(userId);
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("user_name", user.getTrueName() + "[" + user.getUserName() + "]");
			root.put("total_performance", user.getTotalPerformance());
			root.put("referee_number", user.getRefereeNumber());
			root.put("user_level", user.getUserLevel());
			root.put("reg_time", user.getRegTime());
			root.put("userid", user.getId());
			root.put("refereeid", 0);
			root.put("open", true);

			userList.add(root);
		}

		getRequest().getSession().removeAttribute("hadReq");
		outPrint(response, JSON.toJSONString(Result.successResult("我的推荐", userList)));
	}

	// @RequestMapping(value = "/vipAdmin", method = { RequestMethod.GET,
	// RequestMethod.POST })
	// public String vipAdmin(ModelMap modelVip) {
	//
	// return "/user/vipAdmin";
	// }

	@RequestMapping("/vipAdmin")
	public String vipAdmin() {
		return "/user/vipAdmin";
		
	}
	
	/**
	 * 会员管理
	 * 
	 * @param userDo
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/memberAdmin", method = { RequestMethod.POST })
	@ResponseBody
	public void memberAdmin(@Valid UserDo userDo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {

		Result<?> result = null;
		// 新增会员
		if (bindingResult.hasErrors()) {// 参数校验
			List<ObjectError> errors = bindingResult.getAllErrors();
			logger.info("新增会员，参数校验错误：" + JSON.toJSONString(errors));
			Result.failureResult(errors.get(0).getDefaultMessage());
			return;
		}

		result = userService.addUserInfo(userDo);
		logger.info("用户新增结果:" + JSON.toJSONString(result));
		return;
	}

	@RequestMapping(value = "/edit", method = { RequestMethod.GET, RequestMethod.POST })
	public String edit(ModelMap model) {
		String userId = getString("userId");
		UserDo user = userService.getUser(Integer.parseInt(userId));
		user.setUserPassword(DataDecrypt.decrypt(user.getUserPassword()));
		user.setTwoPassword(DataDecrypt.decrypt(user.getTwoPassword()));
		model.put("user", user);
		return "/user/edit";
	}

	/**
	 * 修改用户信息
	 * 
	 * @param response
	 */
	@RequestMapping(value = "/saveEdit", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void saveEdit(HttpServletResponse response) {

		String userId = getString("userId");// 用户id
		String userName = getString("userName");// 用户名
		String trueName = getString("trueName");// 用户真实姓名
		String mobile = getString("mobile");// 用户手机号
		String userPassword = getString("userPassword");// 登录密码
		String twoPassword = getString("twoPassword");// 支付密码
		String userLevel = getString("userLevel");// 用户等级
		String refereeUserMobile = getString("refereeUserMobile");// 用户的推荐人
		String sex = getString("sex"); // 性别
		String idnumber = getString("idnumber");// 身份证号
		String banknumber = getString("banknumber");// 银行卡号
		String banktype = getString("banktype");// 银行卡的开户行

		logger.info("修改用户信息:userId=" + userId);
		logger.info("修改用户信息:userName=" + userName);
		logger.info("修改用户信息:trueName=" + trueName);
		logger.info("修改用户信息:mobile=" + mobile);
		logger.info("修改用户信息:login_password=" + userPassword);
		logger.info("修改用户信息:seconde_password=" + twoPassword);
		logger.info("修改用户信息:userLevel=" + userLevel);
		logger.info("修改用户信息:refereeUserMobile=" + refereeUserMobile);
		logger.info("修改用户信息:sex=" + sex);
		logger.info("修改用户信息:idunmber=" + idnumber);
		logger.info("修改用户信息:banknumber=" + banknumber);
		logger.info("修改用户信息:banktype=" + banktype);

		UserDo user = new UserDo();
		if (StringUtils.isBlank(userId)) {
			outPrint(response, JSON.toJSONString(Result.failureResult("请选择要修改的用户!")));
			return;
		}
		// 新增或编辑重复的用户名
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName", userName);
		List<UserDo> list = userService.selectUser(params);
		if (CollectionUtils.isEmpty(list)) {
			outPrint(response, JSON.toJSONString(Result.failureResult("用户不存在!")));
			return;
		}
		if (StringUtils.isNotBlank(userLevel)) {
			user.setUserLevel(Byte.parseByte(userLevel)); // 等级
		}
		if (StringUtils.isNotBlank(trueName)) {
			user.setTrueName(trueName);// 姓名
		}
		if (StringUtils.isNotBlank(mobile)) {
			user.setMobile(mobile); // 手机号
		}
		if (StringUtils.isNotBlank(userPassword)) {
			user.setUserPassword(DataEncrypt.encrypt(userPassword));// 登录密码
		}
		if (StringUtils.isNotBlank(twoPassword)) {
			user.setTwoPassword(DataEncrypt.encrypt(twoPassword));// 支付密码
		}
		if (StringUtils.isNotBlank(refereeUserMobile)) {
			user.setRefereeUserMobile(refereeUserMobile);// 推荐人
		}
		if (StringUtils.isNotBlank(sex)) {
			user.setSex(Integer.valueOf(sex));// 性别
		}
		if (StringUtils.isNotBlank(idnumber)) {
			user.setIdnumber(idnumber);// 身份证
		}
		if (StringUtils.isNotBlank(banknumber)) {
			user.setBanknumber(banknumber);// 银行卡号
		}
		if (StringUtils.isNotBlank(banktype)) {
			user.setBanktype(banktype);// 开户行
		}

		try {
			user.setId(Integer.parseInt(userId));
			Result<?> flag = Result.failureResult("信息修改失败!");
			if (StringUtils.isNotBlank(userId)) {// 判断用户id是否为空

				flag = userService.update(user);
			} else {

			}
			logger.info("修改结果:" + JSON.toJSONString(flag));

			outPrint(response, JSON.toJSONString(flag));
		} catch (BusinessException e) {
			logger.info("充值报错BusinessException:", e);
			outPrint(response, JSON.toJSONString(Result.failureResult("信息修改失败!")));
		} catch (Exception e) {
			logger.info("充值报错Exception:", e);
			outPrint(response, JSON.toJSONString(Result.failureResult("信息修改失败!")));
		}

	}

	/**
	 * 认证（活动）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toActivity", method = { RequestMethod.GET, RequestMethod.POST })
	public String toActivity(ModelMap model) {
		String userId = getString("userId");
		UserDo user = userService.getUser(Integer.parseInt(userId));
		user.setUserPassword(DataDecrypt.decrypt(user.getUserPassword()));
		user.setTwoPassword(DataDecrypt.decrypt(user.getTwoPassword()));
		model.put("user", user);
		return "/user/activitiUser";
	}

	@RequestMapping(value = "/saveActivity", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void saveActivity(HttpServletResponse response) {

		String userId = getString("userId");
		String userLevel = getString("userLevel");

		logger.info("认证用户信息:userId=" + userId);
		logger.info("认证用户信息:userLevel=" + userLevel);

		UserDo user = new UserDo();
		if (StringUtils.isBlank(userId)) {
			outPrint(response, Result.failureResult("请选择要认证的用户!"));
			return;
		}

		if (StringUtils.isNotBlank(userLevel)) {
			user.setUserLevel(Byte.parseByte(userLevel));
		}

		user.setIsActivated(1);
		user.setActivationTime(DateUtil.getCurrentDate().getTime());

		try {
			user.setId(Integer.parseInt(userId));
			Result<?> flag = userService.update(user);
			logger.info("修改结果:" + JSON.toJSONString(flag));
			if (flag.isSuccess()) {

				outPrint(response, JSON.toJSONString(Result.successResult("用户认证成功!")));
			} else {

				outPrint(response, JSON.toJSONString(Result.failureResult("用户认证失败!")));
			}
		} catch (BusinessException e) {
			logger.info("充值报错BusinessException:", e);
			outPrint(response, JSON.toJSONString(Result.failureResult("用户认证失败!")));
		} catch (Exception e) {
			logger.info("充值报错Exception:", e);
			outPrint(response, JSON.toJSONString(Result.failureResult("用户认证失败!")));
		}
	}

}
