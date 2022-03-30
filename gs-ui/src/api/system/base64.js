import request from '@/utils/request'

// 查询参数列表
export function encode(query) {
  return request({
    url: '/tool/base64/encode',
    method: 'post',
    data: query
  })
}

// 查询参数详细
export function decode(query) {
  return request({
    url: '/tool/base64/decode',
    method: 'post',
    data: query
  })
}
