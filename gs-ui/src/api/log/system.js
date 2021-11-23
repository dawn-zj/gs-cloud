// 查询
import request from "@/utils/request";

export function list(query) {
  return request({
    url: '/json/log/info.json',
    method: 'get',
    params: query
  })
}
