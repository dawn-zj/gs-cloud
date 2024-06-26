import request from '@/utils/request'

const filePath = 'user/userList.json'
// 查询用户列表
export const listUser = () => {
  return request({
    url: '/json/list',
    method: 'post',
    data: {
      filePath: filePath
    }
  })
}

// 查询用户详细
export function getUser(index) {
  return request({
    url: '/json/get',
    method: 'post',
    data: {
      filePath: filePath,
      index: index
    }
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/json/add',
    method: 'post',
    data: {
      filePath: filePath,
      data: data
    }
  })
}

// 修改用户
export function updateUser(index, data) {
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

// 删除用户
export function delUser(index) {
  return request({
    url: '/json/delete',
    method: 'post',
    data: {
      filePath: filePath,
      index: index
    }
  })
}

// 导出用户
export function exportUser(query) {
  return request({
    url: '/system/user/export',
    method: 'get',
    params: query
  })
}

// 用户密码重置
export function resetUserPwd(userId, password) {
  const data = {
    userId,
    password
  }
  return request({
    url: '/system/user/resetPwd',
    method: 'put',
    data: data
  })
}

// 用户状态修改
export function changeUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/system/user/changeStatus',
    method: 'put',
    data: data
  })
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/system/user/profile',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/system/user/profile',
    method: 'put',
    data: data
  })
}

// 用户密码重置
export function updateUserPwd(oldPassword, newPassword) {
  const data = {
    oldPassword,
    newPassword
  }
  return request({
    url: '/system/user/profile/updatePwd',
    method: 'put',
    params: data
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/system/user/profile/avatar',
    method: 'post',
    data: data
  })
}

// 下载用户导入模板
export function importTemplate() {
  return request({
    url: '/system/user/importTemplate',
    method: 'get'
  })
}
