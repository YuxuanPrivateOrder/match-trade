package com.yuxuan66.ecmc.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yuxuan66.ecmc.support.base.BaseEntity;
import lombok.Data;

import java.io.Serial;
import java.util.Date;
import java.io.Serializable;

/**
 * 系统-登陆日志表(SysLogLogin)实体类
 *
 * @author makejava
 * @since 2023-09-07 13:37:40
 */
@Data
@TableName("sys_log_login")
public class LogLogin extends BaseEntity<LogLogin> {

    @Serial
    private static final long serialVersionUID = -59483722353218104L;
    /**
     * 请求参数
     */
    private String params;
    /**
     * 请求头
     */
    private String header;
    /**
     * 登陆用户ID
     */
    private Long userId;
    /**
     * 登陆用户名
     */
    private String username;
    /**
     * 登陆密码
     */
    private String phone;
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 登陆Token
     */
    private String token;
    /**
     * 错误原因
     */
    private String errorMsg;
    /**
     * 登陆IP
     */
    private String ip;
    /**
     * 登陆地址
     */
    private String city;

}

