// 查询
import request from "@/utils/request";
import requestVue from '@/utils/requestLocal'

export function list(query) {
  return requestVue({
    url: '/json/log/debug.log',
    method: 'get',
    params: query
  })
}
