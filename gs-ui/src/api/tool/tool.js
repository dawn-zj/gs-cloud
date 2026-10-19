import request from '@/utils/request'
import requestVue from '@/utils/requestLocal'

const filePath = 'formCreate/list.json'
// 获取
export const list = () => {
  return request({
    url: '/json/list',
    method: 'post',
    data: {
      filePath: filePath
    }
  })
}

export const add = (data) => {
  return request({
    url: '/json/add',
    method: 'post',
    data: {
      filePath: filePath,
      data: data
    }
  })
}

export const update = (index, data) => {
  return request({
    url: '/json/update',
    method: 'post',
    data: {
      filePath: filePath,
      index: index,
      data: data
    }
  })
}

export const del = (index) => {
  return request({
    url: '/json/delete',
    method: 'post',
    data: {
      filePath: filePath,
      index: index
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
