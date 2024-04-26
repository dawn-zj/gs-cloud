// 查询
import request from "@/utils/request";

export function list(level) {
  return request({
    url: '/log/get',
    method: 'post',
    params: {
      level: level
    }
  })
}
