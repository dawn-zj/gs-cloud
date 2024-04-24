import request from '@/utils/request'
import requestVue from '@/utils/requestLocal'
// 获取
export const list = (filePath) => {
  return request({
    url: '/json/get',
    method: 'post',
    data: {
      filePath: filePath
    }
  })
}

export const add = (filePath, data) => {
  return request({
    url: '/json/add',
    method: 'post',
    data: {
      filePath: filePath,
      data: data
    }
  })
}

// 获取
export const listTool = () => {
  return requestVue({
    url: '/json/tool/list.json',
    method: 'get'
  })
}
