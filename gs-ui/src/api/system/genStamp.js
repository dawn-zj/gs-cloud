import request from '@/utils/request'

// 预览图章
export function viewStamp(data) {
  return request({
    url: '/tool/photo/viewStamp',
    method: 'post',
    data: data
  })
}


// 签章
export function pdfStamp(data) {
  return request({
    url: '/tool/pdf/pdfStamp',
    method: 'post',
    data: data,
    responseType: 'blob'
  })
}

// 验签章
export function verify(data) {
  return request({
    url: '/tool/pdf/pdfVerify',
    method: 'post',
    data: data
  })
}
