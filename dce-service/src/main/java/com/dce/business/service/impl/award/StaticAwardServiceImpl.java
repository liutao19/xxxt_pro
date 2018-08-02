package com.dce.business.service.impl.award;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dce.business.common.util.DateUtil;
import com.dce.business.entity.user.UserStaticDo;
import com.dce.business.entity.user.UserStaticDo.StaticType;
import com.dce.business.service.account.IAccountService;
import com.dce.business.service.award.IStaticAwardService;
import com.dce.business.service.user.IUserService;
import com.dce.business.service.user.IUserStaticService;

@Service("staticAwardService")
public class StaticAwardServiceImpl implements IStaticAwardService {
    private final static Logger logger = LoggerFactory.getLogger(StaticAwardServiceImpl.class);

    @Resource
    private IAccountService accountService;
    @Resource
    private IUserService userService;
    @Resource
    private IUserStaticService userStaticService;

    @Resource
    private IStaticAwardJob staticAwardJob;
    
    @Override
    public void calStatic() {
        Date date = DateUtil.getDate(new Date(), -365);
        logger.info("计算静态释放，起始日期：" + DateUtil.dateToString(date));

        //分页查询用户
        Map<String, Object> params = new HashMap<>();
        //params.put("startTime", date.getTime() / 1000);
        params.put("typeList", Arrays.asList(new Byte[] { StaticType.BaoDan.getType(), StaticType.JiaJin.getType() , StaticType.FuTou.getType()})); //保单、加金
        params.put("status", 1); //状态1--正在返
        int pageSize = 10000;
        params.put("rows", pageSize); //每次查询1000条
        int offset = 0;
        while (true) {
            params.put("offset", offset);
            List<UserStaticDo> list = userStaticService.select(params);
            if (CollectionUtils.isEmpty(list)) {
                break;
            }
            offset += pageSize;
            staticAwardJob.calStaticAwardJob(list);
        }

        logger.info("静态释放计算完毕");
    }
}
