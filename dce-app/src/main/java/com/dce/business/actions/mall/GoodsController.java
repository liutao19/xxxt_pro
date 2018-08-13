package com.dce.business.actions.mall;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.actions.common.BaseController;
import com.dce.business.common.result.Result;
import com.dce.business.entity.goods.CTGoodsDo;
import com.dce.business.entity.goods.CTUserAddressDo;
import com.dce.business.entity.order.OrderDo;
import com.dce.business.service.goods.ICTGoodsService;
import com.dce.business.service.goods.ICTUserAddressService;

@RestController
@RequestMapping("mall")
public class GoodsController extends BaseController {

	private final static Logger logger = Logger.getLogger(GoodsController.class);
	
	@Resource
	private ICTGoodsService ctGoodsService;
	@Resource
	private ICTUserAddressService ctUserAddressService;
	
/*	@Resource
	private RealgoodsService ctrealgoodsService;*/
	
	@RequestMapping(value = "/list", method = {RequestMethod.POST,RequestMethod.GET})
	public Result<?> list() {
		 String pageNum = getString("pageNum");  //当前页码
		 String rows = getString("rows");   //每页显示记录数
		 
		 logger.info("查询商品列表：查询页码--" + pageNum);
		 
		 if(StringUtils.isBlank(pageNum)){  //如果为空则查询第一页
			 pageNum = "1";
		 }
		 
		 if(StringUtils.isBlank(rows)){  //如果为空  默认显示10条
			 rows = "50";
		 }
		 
		 Map<String,Object> maps=new HashMap();
		 maps.put("pageNum", pageNum);
		 maps.put("rows", rows);
		// List<CTGoodsDo> resultList = ctGoodsService.selectByPage(Integer.parseInt(pageNum), Integer.parseInt(rows));
		 
		/* List<Map<String,Object>> retList = new ArrayList<>();
		 if(!CollectionUtils.isEmpty(resultList)){
			 for(CTGoodsDo good : resultList){
				 Map<String,Object> map = new HashMap<String,Object>();
				 map.put("productId", good.getGoodsId());
				 map.put("productName", good.getTitle());
				 map.put("imgUrl", good.getGoodsImg());
				 map.put("soldQty", good.getSaleCount());
				 map.put("price", good.getMarketPrice());
				 map.put("barginPrice", good.getShopPrice());
				 retList.add(map);
			 }
		 }*/
		 
		 List<CTGoodsDo> resultList=ctGoodsService.selectByPage(Integer.parseInt(pageNum), Integer.parseInt(rows));
		 
		 
		 List<Map<String,Object>> retList = new ArrayList<>();
		 if(!CollectionUtils.isEmpty(resultList)){
			 for(CTGoodsDo good : resultList){
				 Map<String,Object> map = new HashMap<String,Object>();
				 map.put("goodsId", good.getGoodsId());
				 map.put("title", good.getTitle());
				 map.put("goodsImg", good.getGoodsImg());
				 map.put("saleTime", good.getSaleTime());
				 map.put("shopPrice", good.getShopPrice());
				 map.put("goodsDtails", good.getGoodsDesc());
				 retList.add(map);
			 }
		 }
		 
		 return Result.successResult("商品获取成功!", retList);
	}
	
	@RequestMapping(value = "/product/detail", method = {RequestMethod.GET})
	public Result<?> detail(){
		
		String productId = getString("productId");
		Assert.hasText(productId, "要查询的商品id不能为空");
		
		CTGoodsDo goods = ctGoodsService.selectById(Long.parseLong(productId));
		
		if(goods != null){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("productName", goods.getTitle());
			map.put("soldQty", goods.getSaleCount());
			map.put("price", goods.getMarketPrice());
			map.put("barginPrice", goods.getShopPrice());
			map.put("imgUrl", goods.getGoodsImg());
			map.put("goodsStock", goods.getGoodsStock()); //库存
			return Result.successResult("商品查询成功!", map);
		}else{
			return Result.failureResult("所查商品不存在!");
		}
	}
	
}
