package com.dce.manager.action.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
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

		String userId = getString("userId");
		String userName = getString("userName");
		String trueName = getString("trueName");
		String mobile = getString("mobile");
		String login_password = getString("login_password");
		String seconde_password = getString("seconde_password");
		String userLevel = getString("userLevel");
		String isBlankOrder = getString("isBlankOrder");

		logger.info("修改用户信息:userId=" + userId);
		logger.info("修改用户信息:userName=" + userName);
		logger.info("修改用户信息:trueName=" + trueName);
		logger.info("修改用户信息:mobile=" + mobile);
		logger.info("修改用户信息:login_password=" + login_password);
		logger.info("修改用户信息:seconde_password=" + seconde_password);
		logger.info("修改用户信息:userLevel=" + userLevel);
		logger.info("修改用户信息:isBlankOrder=" + isBlankOrder);

		if (StringUtils.isBlank(isBlankOrder)) {
			outPrint(response, JSON.toJSONString(Result.failureResult("请选择是否空单用户!")));
			return;
		}

		UserDo user = new UserDo();
		if (StringUtils.isBlank(userId)) {
			outPrint(response, JSON.toJSONString(Result.failureResult("请选择要修改的用户!")));
			return;
		}

		if (StringUtils.isNotBlank(userLevel)) {
			user.setUserLevel(Byte.parseByte(userLevel));
		}
		if (StringUtils.isNotBlank(trueName)) {
			user.setTrueName(trueName);
			;
		}
		if (StringUtils.isNotBlank(mobile)) {
			user.setMobile(mobile);
			;
		}
		if (StringUtils.isNotBlank(login_password)) {
			user.setUserPassword(DataEncrypt.encrypt(login_password));
		}
		if (StringUtils.isNotBlank(seconde_password)) {
			user.setTwoPassword(DataEncrypt.encrypt(seconde_password));
			;
		}

		try {
			user.setId(Integer.parseInt(userId));
			Result<?> flag = Result.failureResult("信息修改失败!");
			if ("1".equals(isBlankOrder)) {
				logger.info("空单用户修改,只改变用户信息:userId=" + userId + ",userName=" + userName);
				flag = userService.update(user);
			} else {
				logger.info("非空单用户修改,只改变用户信息:userId=" + userId + ",userName=" + userName);
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
	 * 活动
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

		logger.info("激活用户信息:userId=" + userId);
		logger.info("激活用户信息:userLevel=" + userLevel);

		UserDo user = new UserDo();
		if (StringUtils.isBlank(userId)) {
			outPrint(response, Result.failureResult("请选择要激活的用户!"));
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

				outPrint(response, JSON.toJSONString(Result.successResult("用户激活成功!")));
			} else {

				outPrint(response, JSON.toJSONString(Result.failureResult("用户激活失败!")));
			}
		} catch (BusinessException e) {
			logger.info("充值报错BusinessException:", e);
			outPrint(response, JSON.toJSONString(Result.failureResult("用户激活失败!")));
		} catch (Exception e) {
			logger.info("充值报错Exception:", e);
			outPrint(response, JSON.toJSONString(Result.failureResult("用户激活失败!")));
		}
	}

}
