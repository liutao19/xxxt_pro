package com.dce.manager.action.awared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.entity.award.AwardConfig;
import com.dce.business.service.award.AwardConfigService;
import com.dce.business.service.award.AwardService;
import com.dce.manager.action.BaseAction;


@RestController
@RequestMapping("/award")
public class AwardController  extends BaseAction{
	
	@Resource
	private AwardConfigService awardConfigService;
	
	/*@Resource
	private AwardService awardway;*/
	
	/*@Resource
	private IUserService iuser;*/
	
	@Resource
	private AwardService awardLaterService;
	
	@RequestMapping(value="/selectAwardConfig",method={RequestMethod.GET,RequestMethod.POST})
	public List<Map<String,Object>> selectAwardConfig(){
		
		List<AwardConfig> list=awardConfigService.selectAward();
		
		List<Map<String,Object>> listmap=new ArrayList<>();
		if(!CollectionUtils.isEmpty(list)){
			 for(AwardConfig award : list){
				 Map<String,Object> map = new HashMap<String,Object>();
				 map.put("awardconfigId", award.getId());
				 map.put("awardTypeName", award.getAwardtypename());
				 listmap.add(map);
			 }
		 }
		return listmap;
		
	}
	@RequestMapping(value="/addAwardConfig",method={RequestMethod.GET,RequestMethod.POST})
	public void addAwardConfig(){
		String awardTypeName=getString("awardTypeName");
		AwardConfig record=new AwardConfig();
		record.setAwardtypename(awardTypeName);
		if(awardTypeName!=null){
			if(awardConfigService.insertSelective(record)){
				logger.error("添加等级信息成功");
			}else{
				logger.error("添加等级信息失败");
			}
		}else{
			logger.error("awardTypeName为空");
		}
		
	}
	
	@RequestMapping(value="/deleteAwardConfig",method={RequestMethod.GET,RequestMethod.POST})
	public void deleteAwardConfig(){
		String id=getString("awardTypeid");
		AwardConfig record=new AwardConfig();
		Long awardTypeid=(long) Integer.parseInt(id);
		record.setId(awardTypeid);
		if(awardTypeid!=null&&awardTypeid!=0){
			if(awardConfigService.deleteByPrimaryKey(awardTypeid)){
				logger.error("删除等级信息成功");
			}else{
				logger.error("删除等级信息失败");
			}
		}else{
			logger.error("awardTypeid为空");
		}
	}
	@RequestMapping(value="/updataAwardConfig",method={RequestMethod.GET,RequestMethod.POST})
	public void updataAwardConfig(){
		String id=getString("awardTypeid");
		Long awardTypeid=(long) Integer.parseInt(id);
		String awardTypeName=getString("awardTypeName");
		AwardConfig record=new AwardConfig();
		record.setId(awardTypeid);
		record.setAwardtypename(awardTypeName);
		
		if(record!=null&&record.getAwardtypename()!=null&&record.getId()!=null&&record.getId()!=0){
			if(awardConfigService.updateByPrimaryKeySelective(record)){
				logger.error("修改等级信息成功");
			}else{
				logger.error("修改等级信息失败");
			}
		}else{
			logger.error("获取信息空值");
		}

	}
	
	@RequestMapping(value="/selectAward",method={RequestMethod.GET,RequestMethod.POST})
	private List<Map<String,Object>> selectAward(){
		
		String userid=getString("userid");
		
		
		return null;
		
		
	}
	
	
	/*@RequestMapping(value="/selectAward",method={RequestMethod.GET,RequestMethod.POST})
	public void deleteAward(){
		
		String id=getString("awardid");
		Integer awardid= Integer.parseInt(id);
		if(id!=null){
			
		if(awardway.deleteByPrimaryKey(awardid)){
			
			logger.error("删除奖励记录成功");
		}else{
			logger.error("删除奖励记录失败");
		}
		}else{
			logger.error("获取awardid为空");
		}
	}
	*/
	
	@RequestMapping(value="/addAward",method={RequestMethod.GET,RequestMethod.POST})
	public void addAward(){
		int count=Integer.parseInt(getString("count"));
		String id=getString("userId");
		Integer userID= Integer.parseInt(id);
		
	    //这个是判断用户是否有推荐人，现在方法还没有写好，假设为有
		//判断推荐人是否是会员以上的级别，一样，现在假设是
		if(true&&true){
			
			
			
		}else{
			//第一个参数，用户的级别，第二个参数购买的数量
			userupgrade(1,count);
		}			
		
		
	}
	
	
	//判断用户是否可以升级方法
	public String  userupgrade(int rank,int count){
		//判断用户等级优惠
		if(rank==0){//普通用户
			//普通用户升级
			
		}else if(rank==1&&count>=5){//会员用户
			//会员升级
			
		}else if(rank==2&&count>=50){//vip用户
			//vip升级
			
		}else if(rank==3){//城市合伙人用户
			//调用用户查询方法，查看用户推荐了几个城市合伙人，是否有5个
			
		}else{
			
		}
		
		return null;
		
	}
	
	//判断用户推荐奖金多少,第一个参数奖励人的等级，购买的数量
	public boolean  bonus(int userid,int count){
		//判断用户是否是会员
		if(userid==1){
			//查询会员是否享用过，调用查询奖励记录来判断
			if(awardLaterService.selectByPrimaryKey(userid)==null){
				
			}
		}
		
		
		
		return false;
		
	}
	

}
