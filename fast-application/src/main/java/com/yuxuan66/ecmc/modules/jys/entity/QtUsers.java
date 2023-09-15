package com.yuxuan66.ecmc.modules.jys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.yuxuan66.ecmc.support.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户表(DmQtUsers)实体类
 *
 * @author makejava
 * @since 2023-09-15 12:00:29
 */
@Data
@TableName("dm_qt_users")
public class QtUsers extends Model<QtUsers> implements Serializable {
/**
     * 用户ID
     */
    private Integer userId;
/**
     * 手机号
     */
    private String userPhone;
/**
     * 邮箱
     */
    private String userEmail;
/**
     * 用户头像
     */
    private Integer userAvatar;
/**
     * 账户余额
     */
    private Double userMoney;
/**
     * 冻结资金
     */
    private BigDecimal userBalance;
/**
     * 登录密码
     */
    private String userPasswd;
/**
     * 交易密码
     */
    private String userPassmd;
/**
     * 安全码
     */
    private String userSalt;
/**
     * 注册日期
     */
    private Integer regTime;
/**
     * 最后登录日期
     */
    private Integer lastTime;
/**
     * 最后登录IP
     */
    private String lastIp;
/**
     * 认证类型 0：待认证 1：个人认证 2：企业认证
     */
    private Integer realStatus;
/**
     * 用户状态 0：账户冻结 1：账户正常
     */
    private Integer loginStatus;
/**
     * 资金状态 0：交易冻结 1：交易正常
     */
    private Integer moneyStatus;
/**
     * 紧急联系人姓名
     */
    private String byName;
/**
     * 关系
     */
    private String byBond;
/**
     * 紧急联系人电话
     */
    private String byPhone;

    private String token;
/**
     * 用户是否注销：0未注销1已注销
     */
    private Integer isCancel;



}

