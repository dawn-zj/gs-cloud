import request from '@/utils/request'
import requestVue from '@/utils/requestLocal'
// 获取路由
export const getRouters = (isSuper) => {
  let url = '/json/userRouter.json'
  if (isSuper) {
    url = '/json/router.json'
  }
  return requestVue({
    // url: '/getRouters',
    url: url,
    method: 'get'
  })
}
