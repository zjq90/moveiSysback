import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    params: data
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/user/updateUser',
    method: 'post',
    data
  })
}

export function modifyPassword(data) {
  return request({
    url: '/user/modifyUserPwd',
    method: 'post',
    params: data
  })
}

export function getUserList(params) {
  return request({
    url: '/user/findAllUser',
    method: 'get',
    params
  })
}

export function getUserById(userId) {
  return request({
    url: '/user/findUserById',
    method: 'get',
    params: { userId }
  })
}

export function uploadHeadImg(formData) {
  return request({
    url: '/user/uploadHeadImg',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: formData
  })
}
