package com.dce.business.actions.user;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.actions.common.BaseController;
import com.dce.business.common.result.Result;
import com.dce.business.dao.user.IUserAddressDao;
import com.dce.business.entity.user.UserAddressDo;
import com.dce.business.service.user.UserAdressService;

@RestController
@RequestMapping("address")
public class UserAddressMaintain extends BaseController {

	private final static Logger logger = Logger.getLogger(UserAddressMaintain.class);

	@Resource
	private UserAdressService addressService;

	/**
	 * 添加收货地址
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addaddress", method = { RequestMethod.POST })
	public Result<?> addaddress() {
		String username = getString("username");
		String userphone = getString("userphone");
		String address = getString("address");
		String addressDetails = getString("addressDetails");
		// String postcode = getString("postcode");
		// String remark = getString("remark");
		Integer userId = getUserId();

		Assert.hasText(username, "收货人不能为空");
		Assert.hasText(userphone, "收货人电话不能为空");
		Assert.hasText(address, "收货地址区域不能为空");
		Assert.hasText(addressDetails, "收货地址详情不能为空");

		// 收货地址对像
		UserAddressDo addressadd = new UserAddressDo();
		addressadd.setUserid(userId);
		addressadd.setUsername(username);
		addressadd.setUserphone(userphone);
		addressadd.setAddress(address);
		addressadd.setAddressDetails(addressDetails);
		// addressadd.setPostcode(postcode);
		// addressadd.setRemark(remark);
		// ???
		// if(StringUtils.isNotBlank(addressId)){
		// addressadd.setAddressid(Integer.parseInt(addressId));
		// }
		return null;
	}

	// /**
	// * 修改收货地址
	// *
	// * @return
	// */
	// @RequestMapping(value = "/updateaddress", method = { RequestMethod.POST
	// })
	// public Result<?> updateaddress() {
	// String addressId = getString("addressId");
	// String username = getString("username");
	// String userphone = getString("userphone");
	// String address = getString("address");
	// String addressDetails = getString("addressDetails");
	// String postcode = getString("postcode");
	// String remark = getString("remark");
	// Integer userId = getUserId();
	//
	// Assert.hasText(username, "收货人不能为空");
	// Assert.hasText(userphone, "收货人电话不能为空");
	// Assert.hasText(address, "收货地址区域不能为空");
	// Assert.hasText(addressDetails, "收货地址详情不能为空");
	//
	// // 收货地址对像
	// UserAddressDo addressadd = new UserAddressDo();
	// addressadd.setUserid(userId);
	// addressadd.setUsername(username);
	// addressadd.setUserphone(userphone);
	// addressadd.setAddress(address);
	// addressadd.setAddressDetails(addressDetails);
	// addressadd.setPostcode(postcode);
	// addressadd.setRemark(remark);
	// // ???
	// if(StringUtils.isNotBlank(addressId)){
	// addressadd.setAddressid(Integer.parseInt(addressId));
	// }
	// return null; // addressService.save(addressDo)
	// }
}
