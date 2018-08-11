package com.dce.business.actions.user;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.actions.common.BaseController;
import com.dce.business.common.result.Result;
import com.dce.business.entity.user.UserAddressDo;
import com.dce.business.service.user.UserAdressService;

@RestController
@RequestMapping("address")
public class UserAddressController extends BaseController {

	private final static Logger logger = Logger.getLogger(UserAddressController.class);

	@Resource
	private UserAdressService addressService;

	/**
	 * 添加收货地址
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addAddress", method = { RequestMethod.POST })
	public Result<?> addAddress() {
		String addressId = getString("addressId");
		String username = getString("username");
		String userphone = getString("userphone");
		String address = getString("address");
		String addressDetails = getString("addressDetails");
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
	 * 添加收货地址
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listAddress", method = { RequestMethod.POST })
	public Result<?> listAddress() {
		// 获取当前用户的id
		Integer userId = getUserId();
		
		List<UserAddressDo> addressList = addressService.selectByUserId(userId);
		Result ret = Result.successResult("查看收货地址");
		ret.setData(addressList);
		return ret;
	}

}
