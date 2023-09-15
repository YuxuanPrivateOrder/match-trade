package com.yuxuan66.ecmc.support.log.consumer;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.yuxuan66.ecmc.common.utils.Lang;
import com.yuxuan66.ecmc.common.utils.entity.IPInfo;
import com.yuxuan66.ecmc.common.utils.ip.IPUtil;
import com.yuxuan66.ecmc.consts.QueueConst;
import com.yuxuan66.ecmc.modules.system.entity.LogLogin;
import com.yuxuan66.ecmc.modules.system.entity.User;
import com.yuxuan66.ecmc.modules.system.mapper.LogLoginMapper;
import com.yuxuan66.ecmc.modules.system.mapper.UserMapper;
import com.yuxuan66.ecmc.support.base.BaseService;
import com.yuxuan66.ecmc.support.log.entity.LoginMQDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登陆日志保存消息消费者
 *
 * @author Sir丶雨轩
 * @since 2023/9/7
 */
@Component
public class LogLoginConsumer extends BaseService<LogLogin, LogLoginMapper> {

    @Resource
    private UserMapper userMapper;

    /**
     * 登陆日志保存消息消费
     *
     * @param message 消息
     */
    public void processMessage(String message) {
        LoginMQDto loginMQDto = JSONObject.parseObject(message, LoginMQDto.class);
        Long userId = null;

        // 解析登陆地址
        IPInfo ipInfo = IPUtil.searcher(loginMQDto.getLoginIp());
        String loginCity = String.join("", ipInfo.getCountry(), ipInfo.getProvince(), ipInfo.getCity(), ipInfo.getIsp());
        // 登陆成功
        if (loginMQDto.isLoginSuccess()) {
            userId = Convert.toLong(StpUtil.getLoginIdByToken(loginMQDto.getLoginToken()));
            // 登陆成功修改用户登陆时间
            User user = userMapper.selectById(userId);
            user.setLoginTime(Lang.getNowTimestamp());
            user.setLoginIp(loginMQDto.getLoginIp());
            user.setLoginCity(loginCity.contains("内网") ? "内网IP" : loginCity);
            user.updateById();
        } else {
            // 登陆失败获取尝试登陆的用户Id
            if (StrUtil.isNotBlank(loginMQDto.getLoginAccount())) {
                userId = new QueryChainWrapper<>(userMapper).eq("username", loginMQDto.getLoginAccount()).oneOpt().map(User::getId).orElse(null);
            } else if (StrUtil.isNotBlank(loginMQDto.getLoginPhone())) {
                userId = new QueryChainWrapper<>(userMapper).eq("phone", loginMQDto.getLoginPhone()).oneOpt().map(User::getId).orElse(null);
            }
        }
        LogLogin logLogin = getLogLogin(userId, loginMQDto, loginCity);
        // 保存登陆日志
        logLogin.insert();


    }

    /**
     * 获取登陆日志实体
     *
     * @param userId     用户Id
     * @param loginMQDto 登陆消息
     * @param loginCity  登陆城市
     * @return 登陆日志实体
     */
    @NotNull
    private static LogLogin getLogLogin(Long userId, LoginMQDto loginMQDto, String loginCity) {
        LogLogin logLogin = new LogLogin();
        logLogin.setUserId(userId);
        logLogin.setIp(loginMQDto.getLoginIp());
        logLogin.setCity(loginCity.contains("内网") ? "内网IP" : loginCity);
        logLogin.setToken(loginMQDto.getLoginToken());
        logLogin.setSuccess(loginMQDto.isLoginSuccess());
        logLogin.setErrorMsg(loginMQDto.getErrorMsg());
        logLogin.setUsername(loginMQDto.getLoginAccount());
        logLogin.setPhone(loginMQDto.getLoginPhone());
        logLogin.setParams(loginMQDto.getParams());
        logLogin.setHeader(loginMQDto.getHeader());
        return logLogin;
    }
}
