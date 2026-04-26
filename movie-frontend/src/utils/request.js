import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

request.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    const token = userStore.getToken()
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    const res = response.data
    
    if (res.code !== 0) {
      ElMessage({
        message: res.msg || '请求失败',
        type: 'error',
        duration: 3000
      })
      
      if (res.code === 401) {
        ElMessageBox.confirm('登录状态已过期，请重新登录', '提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const userStore = useUserStore()
          userStore.logout()
          router.push('/login')
        })
      }
      
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
    
    return res
  },
  error => {
    console.error('响应错误:', error)
    let message = '网络错误'
    if (error.response) {
      switch (error.response.status) {
        case 404:
          message = '请求的资源不存在'
          break
        case 500:
          message = '服务器内部错误'
          break
        case 403:
          message = '权限不足'
          break
        default:
          message = error.message
      }
    }
    
    ElMessage({
      message: message,
      type: 'error',
      duration: 3000
    })
    
    return Promise.reject(error)
  }
)

export default request
