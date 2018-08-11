package com.dce.business.action.travel;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.actions.common.BaseController;
import com.dce.business.common.result.Result;
import com.dce.business.entity.notice.NoticeDo;
import com.dce.business.entity.travel.TravelDo;
import com.dce.business.service.travel.ICheckTravelService;
import com.dce.business.service.travel.ITravelApplyService;
import com.dce.business.service.travel.ITravelPathService;

@RestController
@RequestMapping("/travelApply")
public class TravelApplyController extends BaseController {
	private final static Logger logger = Logger.getLogger(TravelApplyController.class);

	@Resource
	private ITravelApplyService travelApplyService;
	
	@Resource
	private ICheckTravelService checkTravelService;
	
	
	@Resource
	private ITravelPathService travelPathService;

	/**
	 * 旅游申请
	 * @return
	 */
	@RequestMapping(value = "/addApply", method = RequestMethod.POST)
	public Result<?> addApply() {
		logger.info("申请旅游....");
		
		TravelDo travel = new TravelDo();
		
		//获取用户id
    	Integer userId = getUserId();
    	
    	//获取前台传过来的用户信息
    	String sex = getString("sex") == null ? "" : getString("sex");
    	String nation = getString("nation") == null ? "" : getString("nation");
    	String identity = getString("identity") == null ? "" : getString("identity");
    	String phone = getString("phone") == null ? "" : getString("phone");
    	String address = getString("address") == null ? "" : getString("address");
    	String pathid = getString("pathId") == null ? "" : getString("pathId");
    	String isBenn = getString("IsBenn") == null ? "" : getString("IsBenn");
    	String people = getString("people") == null ? "" : getString("people");
    	
    	travel.setUserid(userId);
    	travel.setSex(Integer.parseInt(sex));
    	travel.setNation(nation);
    	travel.setIdentity(identity);
    	travel.setPhone(phone);
    	travel.setAddress(address);
    	travel.setPathid(Integer.parseInt(pathid));
    	travel.setIsbeen(Integer.parseInt(isBenn));
    	travel.setPeople(Integer.parseInt(people));
    	
    	
    	
    	Result<?> result = travelApplyService.travelApply(travel);
    	
		return result;
	}
	
	@RequestMapping(value = "/checkTravel", method = RequestMethod.GET)
	public Result<?> checkTravel() {
		logger.info("查看活动....");
		
		
		//获取用户id
    	Integer userId = getUserId();
		
		List<TravelDo> travelList = checkTravelService.userApplyTravelList(userId);
		List<Map<String, Object>> result = new ArrayList<>();
		if (!CollectionUtils.isEmpty(travelList)) {
            for (TravelDo message : travelList) {

                Map<String, Object> map = new HashMap<>();
                map.put("id", message.getId());
                //获取到路线id
                int pathid = message.getPathid();
                //调用travelPathService查询出对应路线id的对象 并取出路线名称
                String lineName = (travelPathService.selectByPrimaryKey(pathid)).getLinename();
                map.put("lineName", lineName);
                map.put("createTime", message.getCreatetime());
                map.put("state", message.getState());
                result.add(map);
            }
        }

        return Result.successResult("查询成功", result);
    	
	}

}