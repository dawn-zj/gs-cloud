import requestVue from '@/utils/requestLocal'
// 获取
export const list = () => {
  return requestVue({
    url: '/json/tool/list.json',
    method: 'get'
  })
}

// 获取
export const listTab = () => {
  return requestVue({
    url: '/json/tool/tablist.json',
    method: 'get'
  })
}
