package com.yuxuan66.ecmc.common.sms;

import lombok.Getter;

/**
 * 定义一个短信发送场景的枚举
 * @author Sir丶雨轩
 * @since 2023/9/4
 */
public enum SmsScene {

    /**
     * 用户登陆
     */
    LOGIN("用户登陆","Login Verification Code"),
    PASSWORD("修改密码","Update Password Verification Code"),
    /**
     * 用户注册
     */
    REGISTER("用户注册","Register Verification Code");

    private final String descriptionCN;
    private final String description;


    SmsScene(String descriptionCN,String description) {
        this.descriptionCN = descriptionCN;
        this.description = description;
    }


    /**
     * 获取描述
     * TODO I18n处理
     * @return 描述
     */
    public String getDescription(){
        if("zh_CN".equals("zh_CN")){
            return descriptionCN;
        }else{
            return description;
        }
    }


}
