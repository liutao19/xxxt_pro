package com.dce.business.actions.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dce.business.actions.common.BaseController;

@RestController
@RequestMapping("/organiza")
public class OrganizationController extends BaseController {

	@RequestMapping(value = "/ztree", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView ztree(){
		String userId = getString("userId");
		String ts = getString("ts");
		String sign = getString("sign");
		getRequest().getSession().setAttribute("hadReq", "1");
		ModelAndView mav = new ModelAndView("ztree");
		mav.addObject("userId", userId);
		mav.addObject("ts",ts);
		mav.addObject("sign",sign);
		return mav;
	}

}
