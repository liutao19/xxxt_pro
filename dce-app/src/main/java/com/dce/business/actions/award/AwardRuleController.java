package com.dce.business.actions.award;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dce.business.actions.common.BaseController;
import com.dce.business.entity.dict.LoanDictDtlDo;
import com.dce.business.service.bonus.IBonusCountService;
import com.dce.business.service.dict.ILoanDictService;

@RestController
@RequestMapping("/award")
public class AwardRuleController extends BaseController {

	@Resource
	ILoanDictService loanDictService;
	@Resource
	IBonusCountService bonusCountService;
	
	/**
	 * 奖金查询
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/queryBonus", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView queryjj(ModelMap modelMap){
		ModelAndView mav = new ModelAndView("jjzd/bonus");
		
		int pageSize = 10;
		int startPage = 1;
		Map<String,Object> paraMap = new HashMap<String,Object>();		
		List<Map<String,Object>> bonusLst= bonusCountService.selectBonus(paraMap ,startPage, pageSize);
		modelMap.addAttribute("bonusList", bonusLst);
		return mav;
	}
	
	
	
	@RequestMapping(value = "/query", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView query(ModelMap modelMap){
		ModelAndView mav = new ModelAndView("jjzd/award");
		List<LoanDictDtlDo> KHJB = loanDictService.queryDictDtlListByDictCode("KHJB");
		modelMap.addAttribute("KHJB", KHJB);
		List<LoanDictDtlDo> BaoDanFei = loanDictService.queryDictDtlListByDictCode("BaoDanFei");
		modelMap.addAttribute("BaoDanFei", BaoDanFei);
		List<LoanDictDtlDo> BaoDanZengSong = loanDictService.queryDictDtlListByDictCode("BaoDanZengSong");
		modelMap.addAttribute("BaoDanZengSong", BaoDanZengSong);
		List<LoanDictDtlDo> CHANNENG = loanDictService.queryDictDtlListByDictCode("CHANNENG");
		modelMap.addAttribute("CHANNENG", CHANNENG);
		
		List<LoanDictDtlDo> LiangPeng = loanDictService.queryDictDtlListByDictCode("LiangPeng");
		for(LoanDictDtlDo dtl : LiangPeng){
			dtl = convertBL(dtl);
		}
		modelMap.addAttribute("LiangPeng", LiangPeng);
		
		List<LoanDictDtlDo> LiangPengFengDing = loanDictService.queryDictDtlListByDictCode("LiangPengFengDing");
		modelMap.addAttribute("LiangPengFengDing", LiangPengFengDing);
		List<LoanDictDtlDo> JiaJin = loanDictService.queryDictDtlListByDictCode("JiaJin");
		modelMap.addAttribute("JiaJin", JiaJin);
		
		List<LoanDictDtlDo> ZhiTui = loanDictService.queryDictDtlListByDictCode("ZhiTui");
		for(LoanDictDtlDo dtl : ZhiTui){
			dtl = convertBL(dtl);
		}
		modelMap.addAttribute("ZhiTui", ZhiTui);
		
		LoanDictDtlDo JXCCLJFBL = loanDictService.getLoanDictDtl("JXCCLJFBL");
		JXCCLJFBL = convertBL(JXCCLJFBL);
		modelMap.addAttribute("JXCCLJFBL", JXCCLJFBL);
		
		//量碰层级
		LoanDictDtlDo LPCJ = loanDictService.getLoanDictDtl("LPCJ");
		modelMap.addAttribute("LPCJ", LPCJ);
		
		LoanDictDtlDo SCSFCXSJ = loanDictService.getLoanDictDtl("SCSFCXSJ");
		modelMap.addAttribute("SCSFCXSJ", SCSFCXSJ);
		
		LoanDictDtlDo LiangPengRate = loanDictService.getLoanDictDtl("LiangPengRate");
		LiangPengRate = convertBL(LiangPengRate);
		modelMap.addAttribute("LiangPengRate", LiangPengRate);
		
		
		LoanDictDtlDo ZTDS = loanDictService.getLoanDictDtl("ZTDS");
		modelMap.addAttribute("ZTDS", ZTDS);
		
		LoanDictDtlDo HuZhuJiaQuan = loanDictService.getLoanDictDtl("HuZhuJiaQuan");
		HuZhuJiaQuan = convertBL(HuZhuJiaQuan);
		modelMap.addAttribute("HuZhuJiaQuan", HuZhuJiaQuan);
		
		LoanDictDtlDo LingDao = loanDictService.getLoanDictDtl("LingDao");
		LingDao = convertBL(LingDao);
		modelMap.addAttribute("LingDao", LingDao);
		
		LoanDictDtlDo TRANS_RATE = loanDictService.getLoanDictDtl("TRANS_RATE");
		TRANS_RATE = convertBL(TRANS_RATE);
		modelMap.addAttribute("TRANS_RATE", TRANS_RATE);
		
		List<LoanDictDtlDo> GPJGFW = loanDictService.queryDictDtlListByDictCode("GPJGFW");
		for(LoanDictDtlDo dtl : GPJGFW){
			dtl = convertBL(dtl);
		}
		modelMap.addAttribute("GPJGFW", GPJGFW);
		
		LoanDictDtlDo MCBMJMD = loanDictService.getLoanDictDtl("MCBMJMD");
		MCBMJMD = convertBL(MCBMJMD);
		modelMap.addAttribute("MCBMJMD", MCBMJMD);
		
		LoanDictDtlDo MCBMJMDZDZG = loanDictService.getLoanDictDtl("MCBMJMDZDZG");
		MCBMJMDZDZG = convertBL(MCBMJMDZDZG);
		modelMap.addAttribute("MCBMJMDZDZG", MCBMJMDZDZG);
		
		LoanDictDtlDo MCBMJMDXD = loanDictService.getLoanDictDtl("MCBMJMDXD");
		MCBMJMDXD = convertBL(MCBMJMDXD);
		modelMap.addAttribute("MCBMJMDXD", MCBMJMDXD);
		
		LoanDictDtlDo MRBMCMD = loanDictService.getLoanDictDtl("MRBMCMD");
		modelMap.addAttribute("MRBMCMD", MRBMCMD);
		
		LoanDictDtlDo MRBMCMDZDZG = loanDictService.getLoanDictDtl("MRBMCMDZDZG");
		MRBMCMDZDZG = convertBL(MRBMCMDZDZG);
		modelMap.addAttribute("MRBMCMDZDZG", MRBMCMDZDZG);
		
		LoanDictDtlDo MRBMCMDXD = loanDictService.getLoanDictDtl("MRBMCMDXD");
		modelMap.addAttribute("MRBMCMDXD", MRBMCMDXD);
		
		LoanDictDtlDo JYYEZB = loanDictService.getLoanDictDtl("JYYEZB");
		JYYEZB = convertBL(JYYEZB);
		modelMap.addAttribute("JYYEZB", JYYEZB);
		
		LoanDictDtlDo YZGMCS = loanDictService.getLoanDictDtl("YZGMCS");
		modelMap.addAttribute("YZGMCS", YZGMCS);
		
		LoanDictDtlDo YSCSFTS = loanDictService.getLoanDictDtl("YSCSFTS");
		modelMap.addAttribute("YSCSFTS", YSCSFTS);
		
		LoanDictDtlDo Point2RMB = loanDictService.getLoanDictDtl("Point2RMB");
		modelMap.addAttribute("Point2RMB", Point2RMB);
		LoanDictDtlDo ZSBTX = loanDictService.getLoanDictDtl("ZSBTX");
		modelMap.addAttribute("ZSBTX", ZSBTX);
		
		LoanDictDtlDo TXSXF = loanDictService.getLoanDictDtl("TXSXF");
		TXSXF = convertBL(TXSXF);
		modelMap.addAttribute("TXSXF", TXSXF);
		
		LoanDictDtlDo RMB2Point = loanDictService.getLoanDictDtl("RMB2Point");
		modelMap.addAttribute("RMB2Point", RMB2Point);
		
		LoanDictDtlDo TXKG = loanDictService.getLoanDictDtl("TXKG");
		modelMap.addAttribute("TXKG", TXKG);
		LoanDictDtlDo MYDBXCC = loanDictService.getLoanDictDtl("MYDBXCC");
		modelMap.addAttribute("MYDBXCC", MYDBXCC);
		
		return mav;
	}
	
	private LoanDictDtlDo convertBL(LoanDictDtlDo dtl){
		float remark = Float.parseFloat(dtl.getRemark()) * 100;
		dtl.setRemark(subZeroAndDot(String.valueOf(remark)));
		return dtl;
	}
	
	  /** 
     * 使用java正则表达式去掉多余的.与0 
     * @param s 
     * @return  
     */  
    private String subZeroAndDot(String s){  
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }
    
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/save", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView save(HttpServletRequest request){
		
		Enumeration<String> paraNames=request.getParameterNames();
		
		List<LoanDictDtlDo> list = new ArrayList<LoanDictDtlDo>();
		
		for(Enumeration<String> e=paraNames;e.hasMoreElements();){
		      String thisName=e.nextElement().toString();
		      String[] names = thisName.split("-");
		      LoanDictDtlDo dtl = new LoanDictDtlDo();
		      String thisValue=request.getParameter(thisName);
		      dtl.setId(Long.parseLong(names[1]));
		      if(names.length == 3){
		    	  float _t = Float.parseFloat(thisValue);
		    	  _t = _t/100;
		    	  dtl.setRemark(String.valueOf(_t));
		      }else{
		    	  dtl.setRemark(thisValue);
		      }
		      list.add(dtl);
		}
		loanDictService.updateDictDtlRemark(list);
		return new ModelAndView("redirect:/award/query.do");
	}
	
}
