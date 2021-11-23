import request from '@/utils/request'

// 获取路由
export const getRouters = () => {
  return request({
    // url: '/getRouters',
    url: '/json/router.json',
    method: 'get'
  })
}
