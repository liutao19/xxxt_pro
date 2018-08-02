package com.dce.manager.action.login;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dce.manager.action.BaseAction;

@Controller
@RequestMapping("/auth/*")
public class LoginController extends BaseAction{




    @RequestMapping(value = "/tologin", method = RequestMethod.GET)
    public String tologin(ModelMap model) {
        return "/login/login";
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model) {
    	String mobile = getString("mobile");
    	String password = getString("password");
    	logger.info("mobile=" + mobile + ",password=" + password);
    	return "/login/login";
    }

    /**
     * 密码过期，返回重置密码页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/resetPage", method = RequestMethod.GET)
    public String resetPage(ModelMap model,HttpServletResponse response) {
    	return "/login/resetPwd";
    }
    
   
}
