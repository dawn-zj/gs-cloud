import request from '@/utils/request'

// 预览图章
export function viewStamp(data) {
  return request({
    url: '/tool/photo/viewStamp',
    method: 'post',
    data: data
  })
}

// 验签章
export function verify(data) {
  return request({
    url: '/seal/stamp/verify',
    method: 'post',
    data: data
  })
}
