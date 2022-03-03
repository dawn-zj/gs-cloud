import request from '@/utils/request'
import requestVue from '@/utils/requestLocal'
// 获取路由
export const getRouters = () => {
  return requestVue({
    // url: '/getRouters',
    url: '/json/router.json',
    method: 'get'
  })
}
