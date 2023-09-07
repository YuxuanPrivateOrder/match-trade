package com.yuxuan66.ecmc.modules.system.rest;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import com.yuxuan66.ecmc.modules.system.entity.dto.RegisterDto;
import com.yuxuan66.ecmc.modules.system.entity.dto.LoginDto;
import com.yuxuan66.ecmc.modules.system.entity.dto.SmsLoginDto;
import com.yuxuan66.ecmc.modules.system.entity.dto.UpdatePasswordDto;
import com.yuxuan66.ecmc.modules.system.service.UserService;
import com.yuxuan66.ecmc.support.base.BaseController;
import com.yuxuan66.ecmc.support.base.resp.Rs;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用来处理用户注册登陆的控制器
 * @author Sir丶雨轩
 * @since 2023/9/4
 */
@SaIgnore
@RestController
@RequiredArgsConstructor
public class LoginController extends BaseController<UserService> {

    /**
     * 用户登录
     *
     * @param loginDto 登录信息
     * @return 标准返回
     */
    @PostMapping(path = "/login")
    public Rs login(@RequestBody LoginDto loginDto) {
        Map<String, Object> tokenInfo = MapUtil.newHashMap(1);
        tokenInfo.put("token", baseService.login(loginDto));
        return Rs.ok(tokenInfo);
    }

    /**
     * 手机验证码登陆
     * @param smsLoginDto 登陆信息
     * @return 标准返回
     */
    @PostMapping(path = "/loginSms")
    public Rs login(@RequestBody SmsLoginDto smsLoginDto){
        Map<String, Object> tokenInfo = MapUtil.newHashMap(1);
        tokenInfo.put("token", baseService.login(smsLoginDto));
        return Rs.ok(tokenInfo);
    }


    /**
     * 通过手机验证码找回密码
     * @param updatePasswordDto 找回密码dto
     * @return 是否成功
     */
    @PutMapping(path = "/retrievePassword")
    public Rs retrievePassword(@RequestBody UpdatePasswordDto updatePasswordDto){
        baseService.retrievePassword(updatePasswordDto);
        return Rs.ok();
    }

    /**
     * 注册用户
     *
     * @param registerDto 注册信息
     */
    @PutMapping(path = "/register")
    public Rs register(@RequestBody RegisterDto registerDto){
        baseService.register(registerDto);
        return Rs.ok();
    }

    /**
     * 用户登出
     * @return 标准返回
     */
    @GetMapping(path = "/logout")
    public Rs logout(){
        StpUtil.logout();
        return Rs.ok();
    }
}
