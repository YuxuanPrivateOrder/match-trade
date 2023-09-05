import {defHttp} from '/@/utils/http/axios';


enum Api {
    Base = '/captcha/',
}

/**
 * 获取图形验证码
 */
export const imgCaptcha = () => {
    return defHttp.get<any>({url: Api.Base + 'imgCaptcha'});
}

/**
 * 获取短信验证码-用户登陆
 */
export const smsLogin = (phone:string,uuid:any,imgCode:string) => {
    return defHttp.get<any>({url: Api.Base + 'smsLogin', params: {phone,imgCode,uuid}});
};

/**
 * 获取短信验证码-用户注册
 */
export const smsRegister = (phone:string,uuid:any,imgCode:string) => {
    return defHttp.get<any>({url: Api.Base + 'smsRegister', params: {phone,imgCode,uuid}});
};

/**
 * 获取短信验证码-用户修改密码
 */
export const smsChangePassword = (phone:string,uuid:any,imgCode:string) => {
    return defHttp.get<any>({url: Api.Base + 'smsChangePassword', params: {phone,imgCode,uuid}});
};


