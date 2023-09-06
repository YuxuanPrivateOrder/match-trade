import {defHttp} from '/@/utils/http/axios';
import {LoginParams, LoginResultModel, GetUserInfoModel, RoleListGetResultModel} from './model/userModel';

import {ErrorMessageMode} from '/#/axios';

enum Api {
    Base = '/user',
    Login = '/login',
    SmsLogin = '/loginSms',
    Logout = '/logout',
    RetrievePassword = '/retrievePassword',
    GetUserInfo = '/user/getUserInfo',
    GetPermCode = '/user/getPermCode',
    TestRetry = '/testRetry',
    AllRole = '/role/all',
    Register = '/register'


}

import JSEncrypt from 'jsencrypt'

const publicKey =
    'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzKYx4aie8aXprDV8osKA\n' +
    'MqZ6LUGk5OFfK31P3urfqaDkO+WE6wI4+ZQELUHQ6DMuPjwt+uxv26e7a1OFXa8s\n' +
    'seErYDFrWOuTv3zm2s7UISci4Psjs/95LK9ILIN74jkvQgC3TATiG4JjKfkf0iUD\n' +
    '1m8zpgPX/2yZUBckxbwTtsm8XdBajRwD8HCI/yj5Z0Su1Zu/C9cXk6NYs2FR0Gyl\n' +
    'vulzFmBtUN4Q+e0e6Qkp1CmYAKsBm35JRDwmyqttiNQhuvfG1Z9ikaOPm53XB205\n' +
    'obtCJfpVIOEJ1zRuAEVYE6vf+LPaq024ipE3Dx4MOnXbd8VcnorttVOMP5OrLTw0\n' +
    'zwIDAQAB'

/**
 * rsa加密
 * @param str 密文
 */
export function encryptedStr(str): string {
    const encrypt = new JSEncrypt()
    encrypt.setPublicKey(`-----BEGIN PUBLIC KEY-----${publicKey}-----END PUBLIC KEY-----`)
    const data = encrypt.encrypt(str)
    if (!data) {
        return ''
    }
    return data
}

/**
 * @description: user login api
 */
export function loginApi(params: LoginParams, mode: ErrorMessageMode = 'modal') {
    params['password'] = encryptedStr(params['password'])
    return defHttp.post<LoginResultModel>(
        {
            url: Api.Login,
            params,
        },
        {
            errorMessageMode: mode,
        },
    );
}

/**
 * 短信验证码登陆
 * @param params 验证码信息
 * @constructor
 */
export function loginSms(params: any) {
    return defHttp.post<LoginResultModel>(
        {
            url: Api.SmsLogin,
            params,
        }
    );
}

/**
 * @description: retrievePassword
 * @param params 找回密码参数
 */
export function retrievePassword(params: any) {
    return defHttp.put(
        {
            url: Api.RetrievePassword,
            params,
        }
    );
}

/**
 * @description: register
 * @param params 注册参数
 */
export function register(params: any) {
    return defHttp.put(
        {
            url: Api.Register,
            params,
        }
    );
}


/**
 * 判断字段是否在使用
 * @param field 字段
 * @param value 数据
 * @param id id
 */
export function checkFieldExist(field, value, id) {
    return defHttp.get<boolean>({ url: Api.Base + '/checkFieldExist/' + field + '/' + value, params: { id: id } })
}


/**
 * @description: getUserInfo
 */
export function getUserInfo() {
    return defHttp.get<GetUserInfoModel>({url: Api.GetUserInfo}, {errorMessageMode: 'none'});
}

export function getPermCode() {
    return defHttp.get<string[]>({url: Api.GetPermCode});
}

export function doLogout() {
    return defHttp.get({url: Api.Logout});
}

/**
 * 获取系统所有角色列表
 */
export function getAllRoleList() {
    return defHttp.get<RoleListGetResultModel>({ url: Api.AllRole })
}

/**
 * 获取用户列表
 */
export const list = (params: ListParam) => {
    return defHttp.get<UserListGetResultModel>({ url: Api.Base, params })
}
/**
 * 导出列表
 * @param params
 */
export function exportList(params?: object) {
    return defHttp.request(
        { url: Api.Base+'/download', params: params, method: 'get', responseType: 'blob' },
        { isReturnNativeResponse: true },
    )
}
/**
 * 删除用户
 * @param ids
 */
export const del = (ids?:object) => {
    return defHttp.delete<boolean>({ url: Api.Base, params: ids })
}
/**
 * 添加一个用户
 * @param user
 */
export const add = (user?: object) => {
    return defHttp.post({ url: Api.Base, params: user })
}

/**
 * 编辑一个用户
 * @param user
 */
export const edit = (user?: object) => {
    return defHttp.put({ url: Api.Base, params: user })
}
/**
 * 编辑当前登录用户的基本信息
 * @param user 用户
 */
export const editLogin = (user?: object) => {
    return defHttp.put({ url: Api.Base+'/editLogin', params: user })
}


export function testRetry() {
    return defHttp.get(
        {url: Api.TestRetry},
        {
            retryRequest: {
                isOpenRetry: true,
                count: 5,
                waitTime: 1000,
            },
        },
    );
}
