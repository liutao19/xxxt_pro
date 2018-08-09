package com.dce.business.actions.account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.actions.common.BaseController;
import com.dce.business.common.result.Result;
import com.dce.business.service.account.IPayService;
import com.dce.business.service.trade.IWithdrawService;

@RestController
@RequestMapping("/withdraw")
public class WithDrawController extends BaseController {
    private final static Logger logger = Logger.getLogger(WithDrawController.class);

    @Resource
    private IPayService payService;
    @Resource
    private IWithdrawService withdrawService;
    

    /** 
     * 用户提现
     * @return  
     */
    @RequestMapping(value = "/withdraws", method = RequestMethod.POST)
    public Result<?> withdraw() {
        Integer userId = getUserId();
        String password = getString("password");
        String qty = getString("qty");
        String type=getString("type");

        Assert.hasText(password, "提现密码不能为空");
        Assert.hasText(qty, "提现数量不能为空");
        Assert.hasText(type,"提现方式不能为空");
        logger.info("用户提现, userId:" + userId + "; qty:" + qty+"type:"+type);

        return payService.withdraw(getUserId(), password,type, new BigDecimal(qty));
    }

    
    /**
     * 提现记录查询
     * @return
     */
    @RequestMapping(value="/info",method=RequestMethod.POST)
    public List<Map<String,Object>> withraw2(){

    	Map<String, Object> map=new HashMap<String,Object>();
    	
    	map.put("userId", getString("userId"));
    	
    	return withdrawService.getWithdrawRecords(map);
    			
    }
}
