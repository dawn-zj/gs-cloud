import request from '@/utils/request'
import requestVue from '@/utils/requestLocal'

// 查询服务器详细
export function getServer() {
  return requestVue({
    url: '/json/monitor/server.json',
    method: 'get'
  })
}
