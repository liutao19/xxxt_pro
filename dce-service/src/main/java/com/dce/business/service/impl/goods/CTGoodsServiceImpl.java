package com.dce.business.service.impl.goods;

import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.dce.business.common.result.Result;
import com.dce.business.common.util.Constants;
import com.dce.business.common.util.OrderCodeUtil;
import com.dce.business.dao.goods.ICTGoodsDao;
import com.dce.business.dao.goods.ICTUserAddressDao;
import com.dce.business.dao.order.IOrderDao;
import com.dce.business.entity.goods.CTGoodsDo;
import com.dce.business.entity.goods.CTUserAddressDo;
import com.dce.business.entity.order.OrderDo;
import com.dce.business.entity.page.PageDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.account.IPayService;
import com.dce.business.service.goods.ICTGoodsService;

@Service("ctGoodsService")
public class CTGoodsServiceImpl implements ICTGoodsService {

	private final static Logger logger = Logger.getLogger(CTGoodsServiceImpl.class);

	@Resource
	private ICTGoodsDao ctGoodsDao;
	@Resource
	private ICTUserAddressDao ctUserAddressDao;
	@Resource
	private IOrderDao orderDao;

	@Resource
	private IAccountService accountService;
	@Resource
	private IPayService payService;

	@Override
	public List<CTGoodsDo> selectByPage(int pageNum, int pageCount) {
		Map<String, Object> params = new HashMap<String, Object>();
		pageNum = pageNum > 0 ? pageNum - 1 : pageNum;
		int offset = pageNum * pageCount;
		int rows = pageCount;
		params.put("offset", offset);
		params.put("rows", rows);
		return ctGoodsDao.selectByPage4App(params);
	}

	@Override
	public CTGoodsDo selectById(Long id) {
		return ctGoodsDao.selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Result<?> buyGoods(OrderDo order, Integer addressId) {
		if (order.getGoodsId() == null || order.getQty().intValue() <= 0) {

			logger.error("购买商品参数错误:goodsId=" + order.getGoodsId() + ",qty=" + order.getQty());
			return Result.failureResult("购买商品参数错误");
		}

		CTGoodsDo goods = selectById(order.getGoodsId());
		// 判断购买数量是否大于库存量
		if (order.getQty().intValue() > goods.getGoodsStock()) {
			return Result.failureResult("购买商品数量大于库存量");
		}

		// 如果没传收货地址,则查询默认且有效的收货地址
		if (addressId == null) {

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", order.getUserId());
			params.put("isDefault", 1);
			params.put("addressFlag", 1);

			List<CTUserAddressDo> addressList = ctUserAddressDao.select(params);
			if (CollectionUtils.isEmpty(addressList)) {
				return Result.failureResult("请先设置收货地址");
			}
			order.setRecAddress(addressList.get(0).getAddress());
		} else {
			CTUserAddressDo address = ctUserAddressDao.selectByPrimaryKey(addressId);
			order.setRecAddress(address.getAddress());
		}

		order.setCreateTime(new Date());
		order.setOrderCode(OrderCodeUtil.genOrderCode(order.getUserId()));
		order.setOrderStatus(2);
		order.setOrderType(3);
		order.setPayStatus(1);
		order.setPrice(goods.getShopPrice());
		order.setTotalPrice(order.getQty().multiply(order.getPrice()).setScale(6, RoundingMode.HALF_UP));
		order.setPayTime(new Date());
		/*
		int flag = orderDao.insertSelective(order);

		if (flag < 1) {
			return Result.failureResult("购买订单保存失败");
		}
		*/

		
		CTGoodsDo _goods = new CTGoodsDo();
		_goods.setGoodsId(goods.getGoodsId());
		_goods.setBookQuantity(order.getQty().longValue());
		// 修改库存
		try {
			int ret = ctGoodsDao.updateBookQty(_goods);
			if (ret < 1) {
				return Result.failureResult("购买失败库存不够");
			}
		} catch (Exception e) {
			logger.error(e);
			return Result.failureResult("购买失败库存不够");
		}

		return payService.buyGoods(order.getUserId(), order.getTotalPrice());

	}

	@Override
	public boolean insertSelectiveService(CTGoodsDo goods) {

		boolean flag = false;
		if (goods != null) {
			flag = ctGoodsDao.insertSelective(goods) > 0;
		}

		return flag;
	}

	@Override
	public boolean deleteGoodsService(Integer goodsid) {
		boolean flag=false;
		if(goodsid!=null&&goodsid!=0){
			flag=ctGoodsDao.deleteByPrimaryKey(goodsid)>0;
		}
		
		return flag;
	}
	
	/**
	 * 分页查询
	 * @param param
	 * @param page
	 * @return
	 */
	public PageDo<CTGoodsDo> getGoodsPage(Map<String, Object> param, PageDo<CTGoodsDo> page){
		logger.info("----getGoodsPage----"+param);
        param.put(Constants.MYBATIS_PAGE, page);
        List<CTGoodsDo> list =  ctGoodsDao.queryListPage(param);
        page.setModelList(list);
        return page;
	}
	
	/**
	 * 更新
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int updateGoodsById(CTGoodsDo newGoodsDo){
		logger.debug("updateGoods(GoodsDo: "+newGoodsDo);
		return  ctGoodsDao.updateByPrimaryKeySelective(newGoodsDo);		
	}

}
