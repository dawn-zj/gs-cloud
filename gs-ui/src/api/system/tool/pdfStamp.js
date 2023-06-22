import request from '@/utils/request'

// 查询参数列表
export function getPdfStamp(query) {
  return request({
    url: '/tool/pdf/getStamp',
    method: 'post',
    data: query
  })
}
