import { defHttp } from '/@/utils/http/axios'

enum Api {
  Base = '/qtUsers/',
}

export const data = (search:any) => {
  return defHttp.get<any>({url: Api.Base + 'data/'+search});
}