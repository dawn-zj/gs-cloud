import request from '@/utils/request'
import requestVue from '@/utils/requestLocal'

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/login',
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return requestVue({
    // url: '/getInfo',
    url: '/json/userInfo.json',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return requestVue({
    // url: '/logout',
    // method: 'post'
    url: '/json/common.json',
    method: 'get'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    method: 'get'
  })
}
