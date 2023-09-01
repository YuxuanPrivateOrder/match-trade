import {defHttp} from '/@/utils/http/axios';
import {
  GetUserInfoModel,
  ListParam,
  LoginParams,
  LoginResultModel,
  RoleListGetResultModel,
  UserListGetResultModel,
} from './model/userModel';
import {ErrorMessageMode} from '/#/axios';
import JSEncrypt from 'jsencrypt';

const publicKey =
  'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzKYx4aie8aXprDV8osKA\n' +
  'MqZ6LUGk5OFfK31P3urfqaDkO+WE6wI4+ZQELUHQ6DMuPjwt+uxv26e7a1OFXa8s\n' +
  'seErYDFrWOuTv3zm2s7UISci4Psjs/95LK9ILIN74jkvQgC3TATiG4JjKfkf0iUD\n' +
  '1m8zpgPX/2yZUBckxbwTtsm8XdBajRwD8HCI/yj5Z0Su1Zu/C9cXk6NYs2FR0Gyl\n' +
  'vulzFmBtUN4Q+e0e6Qkp1CmYAKsBm35JRDwmyqttiNQhuvfG1Z9ikaOPm53XB205\n' +
  'obtCJfpVIOEJ1zRuAEVYE6vf+LPaq024ipE3Dx4MOnXbd8VcnorttVOMP5OrLTw0\n' +
  'zwIDAQAB'

enum Api {
  Base = '/user',
  Login = '/user/login',
  Logout = '/logout',
  GetUserInfo = '/user/getUserInfo',
  GetPermCode = '/user/getPermCode',
  AllRole = '/role/all',
  CheckFieldExist = "/user/checkFieldExist/"
}

/**
 * rsa加密
 * @param str 密文
 */
export function encryptedStr(str): string {
  const encrypt = new JSEncrypt();
  encrypt.setPublicKey(`-----BEGIN PUBLIC KEY-----${publicKey}-----END PUBLIC KEY-----`);
  const data = encrypt.encrypt(str);
  if (!data) {
    return '';
  }
  return data
}

/**
 * 用户登录
 */
export function loginApi(params: LoginParams, mode: ErrorMessageMode = 'modal') {
  params['password'] = encryptedStr(params['password']);
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
 * 获取当前登录用户的详细信息
 */
export function getUserInfo() {
  return defHttp.get<GetUserInfoModel>({url: Api.GetUserInfo}, {errorMessageMode: 'none'});
}

/**
 * 获取用户列表
 */
export const list = (params: ListParam) => {
  return defHttp.get<UserListGetResultModel>({url: Api.Base, params});
};

/**
 * 删除用户
 * @param ids
 */
export const del = (ids?:object) => {
  return defHttp.delete<boolean>({url: Api.Base, params:ids});
}
/**
 * 添加一个用户
 * @param user
 */
export const add = (user?: object) => {
  return defHttp.post({url: Api.Base, params: user});
}

/**
 * 编辑一个用户
 * @param user
 */
export const edit = (user?: object) => {
  return defHttp.put({url: Api.Base,params:user});
}

/**
 * 导出列表
 * @param params
 */
export function exportList(params?: object) {
  return defHttp.request(
    { url: Api.Base+"/download", params: params, method: 'get', responseType: 'blob' },
    { isReturnNativeResponse: true },
  );
}
/**
 * 获取系统所有角色列表
 */
export function getAllRoleList() {
  return defHttp.get<RoleListGetResultModel>({url: Api.AllRole});
}

/**
 * 判断字段是否在使用
 * @param field 字段
 * @param value 数据
 * @param id id
 */
export function checkFieldExist(field, value, id) {
  return defHttp.get<boolean>({url: Api.CheckFieldExist + field + '/' + value, params: {id: id}});
}

/**
 * 获取当前登录用户的权限编码
 */
export function getPermCode() {
  return defHttp.get<string[]>({url: Api.GetPermCode});
}

export function doLogout() {
  return defHttp.get({url: Api.Logout});
}
