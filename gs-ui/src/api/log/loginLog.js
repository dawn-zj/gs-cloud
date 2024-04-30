import request from '@/utils/request'

const filePath = 'log/login.json'
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
    url: '/loginLog/add',
    method: 'post',
    data: {
      filePath: filePath,
      data: data
    }
  })
}
