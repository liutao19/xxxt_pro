/*
 * Powered By  huangzl QQ: 272950754
 * Web Site: http://www.hehenian.com
 * Since 2008 - 2018
 */

package com.dce.manager.action.district;

import java.util.Map;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.entity.district.District;
import com.dce.business.entity.page.NewPagination;
import com.dce.business.entity.page.PageDo;
import com.dce.business.entity.page.PageDoUtil;
import com.dce.business.entity.user.UserDo;
import com.dce.business.service.district.IDistrictService;
import com.dce.business.service.user.IUserService;
import com.dce.manager.action.BaseAction;
import com.dce.manager.util.ResponseUtils;

/**
 * @author huangzl QQ: 272950754
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping("/district")
public class DistrictController extends BaseAction {
	// 默认多列排序,example: username desc,createTime asc
	// protected static final String DEFAULT_SORT_COLUMNS = null;
	@Resource
	private IDistrictService districtService;

	@Resource
	private IUserService userService;

	
	/**
	 * 去列表页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/index")
	public String index(Model model) {
		return "district/listDistrict";
	}

	@RequestMapping("/listDistrict")
	public void listDistrict(NewPagination<District> pagination, ModelMap model, HttpServletResponse response) {

		logger.info("----listDistrict----");
		try {
			PageDo<District> page = PageDoUtil.getPage(pagination);
			String distrct_name = getString("distrct_name");
			String true_name=getString("true_name");
			System.err.println("userId--->>"+distrct_name+"-----true_name----》》"+true_name);
			Map<String, Object> param = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(distrct_name)) {
				param.put("distrct_name", distrct_name);
				model.addAttribute("distrct_name", distrct_name);
			}
			if (StringUtils.isNotBlank(true_name)) {
				param.put("true_name", true_name);
				model.addAttribute("true_name", true_name);
			}
			page = districtService.getDistrictPage(param, page);
			/*
			 * List<CommonComboxConstants> statusList =
			 * CommonComboxConstants.getStatusList();
			 * model.addAttribute("statusList", statusList);
			 */
			pagination = PageDoUtil.getPageValue(pagination, page);
			outPrint(response, JSONObject.toJSON(pagination));
		} catch (Exception e) {
			logger.error("查询清单异常", e);
			throw new BusinessException("系统繁忙，请稍后再试");
		}
	}

	/**
	 * 编辑页面
	 *
	 * @return
	 */
	@RequestMapping("/addDistrict")
	public String addDistrict(String id, ModelMap modelMap, HttpServletResponse response) {
		logger.info("----addDistrict----");
		try {
			if (StringUtils.isNotBlank(id)) {
				District districtDo = districtService.getById(Integer.valueOf(id));
				if(districtDo.getDistrctName().equals("未封地给他（她）")){
					districtDo.setDistrctName("");
				}
				System.err.println("update------>>" + id+"districtDo--->>"+districtDo);
				if (null != districtDo) {
					modelMap.addAttribute("district", districtDo);
				}
			}
			return "district/addDistrict";
		} catch (Exception e) {
			logger.error("跳转到数据字典编辑页面异常", e);
			throw new BusinessException("系统繁忙，请稍后再试");
		}

	}

	/**
	 * 保存更新
	 *
	 * @return
	 * @author: huangzlmf
	 * @date: 2015年4月21日 12:49:05
	 */
	@RequestMapping("/saveDistrict")
	@ResponseBody
	public void saveDistrict(District districtDo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("----saveDistrict------");
		try {
			Integer id = districtDo.getDistrictId();
			//Long userId = new Long(this.getUserId());
			int i = 0;
			if (id != null && id.intValue() > 0) {
				/*
				 * districtDo.setUpdateBy(userId); districtDo.setUpdateTime(new
				 * Date());
				 */
				districtDo.setDistrictStatus(1);
				i = districtService.updateDistrictById(districtDo);
				UserDo user=new UserDo();
				user.setId(districtDo.getUserId());
				user.setDistrict(districtDo.getDistrctName());
				if(userService.updateLevel(user)){
					logger.error("修改用户区域成功");
				}
			} else {
				/*
				 * districtDo.setCreateBy(userId); districtDo.setCreateTime(new
				 * Date());
				 */
				districtDo.setDistrictStatus(1);

				i = districtService.addDistrict(districtDo);
			}

			if (i <= 0) {
				outPrint(response, this.toJSONString(Result.failureResult("操作失败")));
				return;
			}
			outPrint(response, this.toJSONString(Result.successResult("操作成功")));
		} catch (Exception e) {
			logger.error("保存更新失败", e);
			outPrint(response, this.toJSONString(Result.failureResult("操作失败")));
		}
		logger.info("----end saveDistrict--------");
	}

	/**
	 * 删除
	 */
	@RequestMapping("/deleteDistrict")
	public void deleteDistrict(String districtId,String userId, HttpServletRequest request, HttpServletResponse response) {
		logger.info("----deleteDistrict------id---"+districtId+"----usrid----"+userId);
		try {
			if (StringUtils.isBlank(districtId) || !districtId.matches("\\d+")) {
				ResponseUtils.renderJson(response, null, "{\"ret\":-1}");
				return;
			}
			int ret = districtService.deleteById(Integer.valueOf(districtId));
			UserDo userDo=new UserDo();
			userDo.setId(Integer.valueOf(userId));
			userDo.setDistrict("");
			Result<?> re=userService.update(userDo);
			ResponseUtils.renderJson(response, null, "{\"ret\":" + ret + "}");
			
		} catch (Exception e) {
			logger.error("删除区域异常", e);
			ResponseUtils.renderJson(response, null, "{\"ret\":-1}");
		}
	}
	
	
	
	/**
	 * 导出数据
	 *//*
		 * @RequestMapping("/export") public void export(HttpServletResponse
		 * response) throws IOException { try { Long time =
		 * System.currentTimeMillis(); DistrictExample example = new
		 * DistrictExample(); String companyName =
		 * getString("searchPolicyName");
		 * 
		 * if(StringUtils.isNotBlank(companyName)){
		 * example.createCriteria().andPolicyNameLike(companyName); } String
		 * managerName = getString("searManagerName");
		 * if(StringUtils.isNotBlank(managerName)){
		 * example.createCriteria().andManagerNameEqualTo(managerName); }
		 * List<DistrictDo> districtLst =
		 * districtService.selectDistrict(example);
		 * 
		 * String excelHead = "数据导出"; String date = new
		 * SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); String
		 * fileName = URLEncoder.encode(excelHead + date + ".xls", "utf-8");
		 * List<String[]> excelheaderList = new ArrayList<String[]>(); String[]
		 * excelheader = { "保险公司名称", "保险公司简称", "联系人姓名", "联系人手机号码", "跟进单员",
		 * "合作状态", "记录状态" }; excelheaderList.add(0, excelheader); String[]
		 * excelData = { "policyName", "shortName", "contactName",
		 * "contactPhone", "managerName", "partnerStatus", "status" };
		 * HSSFWorkbook wb = ExeclTools.execlExport(excelheaderList, excelData,
		 * excelHead, districtLst);
		 * response.setContentType("application/vnd.ms-excel;charset=utf-8");
		 * response.setHeader("Content-Disposition", "attachment;filename=" +
		 * fileName); wb.write(response.getOutputStream()); time =
		 * System.currentTimeMillis() - time; logger.info("导出数据，导出耗时(ms)：" +
		 * time); } catch (Exception e) {
		 * response.setContentType("text/html;charset=utf-8");
		 * response.getWriter().println("下载失败"); logger.error("导出数据，Excel下载失败",
		 * e); logger.error("导出数据异常", e); throw new
		 * BusinessException("系统繁忙，请稍后再试"); } finally { response.flushBuffer();
		 * }
		 * 
		 * }
		 */

}
