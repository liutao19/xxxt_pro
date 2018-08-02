package com.dce.business.actions.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dce.business.actions.common.BaseController;
import com.dce.business.common.enums.AccountType;
import com.dce.business.common.enums.IncomeType;
import com.dce.business.common.exception.BusinessException;
import com.dce.business.common.result.Result;
import com.dce.business.common.util.DateUtil;
import com.dce.business.common.util.NumberUtil;
import com.dce.business.entity.account.UserAccountDetailDo;
import com.dce.business.entity.account.UserAccountDo;
import com.dce.business.entity.etherenum.EthereumAccountDo;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.account.IBaodanService;
import com.dce.business.service.third.IEthereumService;

@RestController
@RequestMapping("/account")
public class AccountController extends BaseController {
    private final static Logger logger = Logger.getLogger(AccountController.class);
    @Resource
    private IAccountService accountService;
    @Resource
    private IEthereumService etservice;
    @Resource
    private IBaodanService baodanService;

    /** 
     * 财务流水
     * @param userDo
     * @param bindingResult
     * @return  
     */
    @RequestMapping(value = "/flow", method = RequestMethod.POST)
    public Result<?> flow() {
        Integer userId = getUserId();
        String accountType = getString("accountType");

        Assert.hasText(accountType, "账户类型不能为空");
        logger.info("财务流水, userId:" + userId);

        String startTime = getString("startTime");
        String endTime = getString("endTime");

        Map<String, Object> params = new HashMap<>();
        params.put("accountType", accountType);
        params.put("userId", userId);
        if (StringUtils.isNotBlank(startTime)) {
            params.put("startTime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endTime", endTime);
        }
        List<UserAccountDetailDo> list = accountService.selectUserAccountDetail(params);
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserAccountDetailDo detail : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", detail.getUserId()); //用户名
            map.put("userName", detail.getUserName()); //用户名

            if(null != detail.getIncomeType()){
	            IncomeType type = IncomeType.getByType(detail.getIncomeType());
	            map.put("flowType", type == null?"":type.getRemark()); //流水类型
            }else{
            	map.put("flowType",""); //流水类型
            }

            map.put("amount", NumberUtil.formatterBigDecimal(detail.getAmount().abs())); //变更数量
            map.put("balanceAmount", NumberUtil.formatterBigDecimal(detail.getBalanceAmount())); //余额
            map.put("createTime", DateUtil.dateToString(detail.getCreateTime()));
            map.put("remark", detail.getRemark());
            map.put("transactionObject", detail.getSeqId());
            result.add(map);
        }

        return Result.successResult("查询成功", result);
    }

    @RequestMapping(value = "/ethereum", method = { RequestMethod.POST, RequestMethod.GET })
    public Result<?> ethereum() {
        Integer userId = getUserId();

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal ethereumNum = accountService.getEthernumAmount(userId);
        EthereumAccountDo etAccount = etservice.getByUserId(userId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("ethereumNum", NumberUtil.formatterBigDecimal(ethereumNum));
        result.put("totalIncome", NumberUtil.formatterBigDecimal(totalIncome));
        result.put("etAccount", etAccount==null?"":etAccount.getAccount());
        
        return Result.successResult("查询成功!", result);
    }

    /** 
     * 账户基本信息
     * @return  
     */
    @RequestMapping(value = "/baseInfo", method = RequestMethod.GET)
    public Result<?> getTradeBaseInfo() {
        Integer userId = getUserId();
        logger.info("账户基本信息, userId:" + userId);

        UserAccountDo account = accountService.selectUserAccount(userId, AccountType.point.name());
        Map<String, Object> map = new HashMap<>();
        if (account != null && account.getAmount() != null) {

            map.put("pointAmount", account.getAmount()); //美元点余额
        } else {

            map.put("pointAmount", "0.0"); //美元点余额
        }

        UserAccountDo current = accountService.selectUserAccount(userId, AccountType.current.name());
        
        if(current != null && current.getAmount() != null){
        	
        	map.put("coinAmount", current.getAmount()); //奖金币余额
        }else{
        	
        	map.put("coinAmount", "0.0"); //奖金币余额
        }

        return Result.successResult("查询成功", map);
    }

    /**
     * 查询 现持仓、原始仓、美元点 余额
     * @return
     */
    @RequestMapping(value = "/amount", method = {RequestMethod.GET,RequestMethod.POST})
    public Result<?> amount() {

        String accountType = getString("accountType");

        if(StringUtils.isBlank(accountType)){
        	return Result.failureResult("请选择查询的账户类别!");
        }
        Integer userId = getUserId();

        logger.info("查询账户余额:accountType=" + accountType);

        UserAccountDo account = accountService.selectUserAccount(userId, accountType);

        Map<String, Object> result = new HashMap<String, Object>();
        if (account == null || account.getAmount() == null) {
        	
        	result.put("totalIncome", "0.0");
        	result.put("amount", "0.0");
            return Result.successResult("获取账户信息成功!",result);
        } else {
        	result.put("totalIncome", account.getTotalInocmeAmount());
        	result.put("amount", account.getAmount());
            return Result.successResult("获取账户信息成功!", result);
        }
    }

    /**
     * 原始仓加金、复投 | 报单初始化
     * @return
     */
    @RequestMapping(value = "/currentInit", method = { RequestMethod.GET, RequestMethod.POST })
    public Result<?> currentInit() {
        Integer userId = getUserId();
        String accountType = getString("accountType");

        if(StringUtils.isBlank(accountType)){
        	return Result.failureResult("请选择查询的账户类别!");
        }
        
        AccountType type = AccountType.getAccountType(accountType);
        if(type == null){
        	return Result.failureResult("账户类别不存在");
        }
        return accountService.currentInit(userId,type);
    }

    /**
     * 原始仓加金
     * @return
     */
    @RequestMapping(value = "/currentAddMoney", method = RequestMethod.POST)
    public Result<?> currentAddMoney() {

        String qty = getString("qty");

        if (StringUtils.isBlank(qty)) {
            return Result.failureResult("加金金额不能为空!");
        }
        Integer userId = getUserId();
        logger.info("现持仓加金:qty=" + qty);
        try {

            return accountService.currentAddMoney(userId, new BigDecimal(qty));
        } catch (BusinessException be) {
            logger.error("加金失败:" + be);
            be.printStackTrace();
            return Result.failureResult(be.getMessage());
        } catch (Exception e) {
            logger.error("原始仓加金出错!" + e);
            return Result.failureResult("加金出错!");
        }
    }

    /**
     * 原始仓复投
     * @return
     */
    @RequestMapping(value = "/reCast", method = RequestMethod.POST)
    public Result<?> reCast() {

    	Integer userId = getUserId();
    	
        logger.info("原始仓复投:userId=" + userId);
        
        String qty = this.getString("qty");
        String accountType = this.getString("accountType");
        
        if (StringUtils.isBlank(qty)) {
            return Result.failureResult("数量不能为空!");
        }

        BigDecimal qtyVal = new BigDecimal(qty);
        if (qtyVal.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.failureResult("复投数量必须大于0!");
        }

        if (StringUtils.isBlank(accountType)) {
            return Result.failureResult("请选择美元点或现持仓!");
        }

        try {
            return baodanService.reCast(userId,qtyVal,accountType);
        } catch (BusinessException be) {
            logger.error("复投失败:" + be);
            be.printStackTrace();
            return Result.failureResult(be.getMessage());
        }catch (Exception e) {
            logger.error("复投失败:" + e);
            e.printStackTrace();
            return Result.failureResult("复投失败!");
        }
        
    }

    /**
     * 美元点转出
     * @return
     */
    @RequestMapping(value = "/pointOut", method = RequestMethod.POST)
    public Result<?> pointOut() {

        String qty = getString("qty");
        String receiver = getString("receiver");

        logger.info("美元点转出:qty=" + qty + ",receiver=" + receiver);

        if (StringUtils.isBlank(qty)) {
            return Result.failureResult("数量不能为空!");
        }

        if (StringUtils.isBlank(receiver)) {
            return Result.failureResult("接收人不能为空!");
        }

        BigDecimal qtyVal = new BigDecimal(qty);
        if (qtyVal.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.failureResult("报单数量必须大于0!");
        }

        Integer userId = getUserId();
        try {

            return accountService.pointOut(userId, qtyVal, receiver);
        } catch (Exception e) {
            logger.error("美元点转出报错:" + e);
            return Result.failureResult("美元点转出失败!");
        }
    }
}
