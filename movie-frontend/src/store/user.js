import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref(null)
  const token = ref(localStorage.getItem('token') || '')

  function setUserInfo(info) {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  function getUserInfo() {
    if (!userInfo.value) {
      const stored = localStorage.getItem('userInfo')
      if (stored) {
        userInfo.value = JSON.parse(stored)
      }
    }
    return userInfo.value
  }

  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function getToken() {
    if (!token.value) {
      token.value = localStorage.getItem('token') || ''
    }
    return token.value
  }

  function logout() {
    userInfo.value = null
    token.value = ''
    localStorage.removeItem('userInfo')
    localStorage.removeItem('token')
  }

  function isAdmin() {
    const info = getUserInfo()
    return info && info.userRole === 1
  }

  function isLoggedIn() {
    return !!getUserInfo()
  }

  return {
    userInfo,
    token,
    setUserInfo,
    getUserInfo,
    setToken,
    getToken,
    logout,
    isAdmin,
    isLoggedIn
  }
})
