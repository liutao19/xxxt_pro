package com.dce.business.actions.account;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.actions.common.BaseController;
import com.dce.business.common.result.Result;
import com.dce.business.service.account.IAccountService;

@RestController
@RequestMapping("/recharge")
public class RechargeController extends BaseController {
    private final static Logger logger = Logger.getLogger(RechargeController.class);

    @Resource
    private IAccountService accountService;

    /** 
     * 用户充值
     * @return  
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Result<?> recharge() {
        Integer userId = getUserId();
        String password = getString("password");
        String qty = getString("qty");

        Assert.hasText(password, "充值密码不能为空");
        Assert.hasText(qty, "充值以太坊数量不能为空");
        logger.info("用户充值, userId:" + userId + "; qty:" + qty);

        return accountService.recharge(getUserId(), password, new BigDecimal(qty));
    }

    /**   
     * 每5分钟查询以太坊，看用户数据是否已确认
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void comfirmEthTransResult() {
        accountService.comfirmEthTransResult();
    }
}
