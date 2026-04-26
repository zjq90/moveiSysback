<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-left">
        <div class="logo">
          <el-icon :size="60" color="#409eff"><Film /></el-icon>
        </div>
        <h1 class="title">鹰眼电影</h1>
        <p class="subtitle">回首向来风雨茶香里</p>
        <p class="subtitle">眸中一点可是思念谁</p>
      </div>
      
      <div class="login-right">
        <el-tabs v-model="activeTab" class="login-tabs">
          <el-tab-pane label="登录" name="login">
            <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="0">
              <el-form-item prop="userName">
                <el-input v-model="loginForm.userName" placeholder="请输入账号" size="large" prefix-icon="User">
                </el-input>
              </el-form-item>
              <el-form-item prop="userPwd">
                <el-input v-model="loginForm.userPwd" type="password" placeholder="请输入密码" size="large" 
                  prefix-icon="Lock" @keyup.enter="handleLogin">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" size="large" class="login-btn" :loading="loading" @click="handleLogin">
                  登录
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          
          <el-tab-pane label="注册" name="register">
            <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef" label-width="0">
              <el-form-item prop="userName">
                <el-input v-model="registerForm.userName" placeholder="请输入账号" size="large" prefix-icon="User">
                </el-input>
              </el-form-item>
              <el-form-item prop="userPwd">
                <el-input v-model="registerForm.userPwd" type="password" placeholder="请输入密码" size="large" 
                  prefix-icon="Lock">
                </el-input>
              </el-form-item>
              <el-form-item prop="confirmPwd">
                <el-input v-model="registerForm.confirmPwd" type="password" placeholder="请确认密码" size="large" 
                  prefix-icon="Lock">
                </el-input>
              </el-form-item>
              <el-form-item prop="userEmail">
                <el-input v-model="registerForm.userEmail" placeholder="请输入邮箱" size="large" 
                  prefix-icon="Message">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="warning" size="large" class="register-btn" :loading="loading" @click="handleRegister">
                  注册
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Film, User, Lock, Message } from '@element-plus/icons-vue'
import { login, register } from '@/api/user'
import { useUserStore } from '@/store/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeTab = ref('login')
const loading = ref(false)
const loginFormRef = ref(null)
const registerFormRef = ref(null)

const loginForm = reactive({
  userName: '',
  userPwd: ''
})

const registerForm = reactive({
  userName: '',
  userPwd: '',
  confirmPwd: '',
  userEmail: ''
})

const validateConfirmPwd = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请确认密码'))
  } else if (value !== registerForm.userPwd) {
    callback(new Error('两次密码输入不一致'))
  } else {
    callback()
  }
}

const validateEmail = (rule, value, callback) => {
  const emailReg = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/
  if (value === '') {
    callback(new Error('请输入邮箱'))
  } else if (!emailReg.test(value)) {
    callback(new Error('请输入正确的邮箱格式'))
  } else {
    callback()
  }
}

const loginRules = {
  userName: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  userPwd: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const registerRules = {
  userName: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 2, max: 20, message: '账号长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  userPwd: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPwd: [
    { required: true, validator: validateConfirmPwd, trigger: 'blur' }
  ],
  userEmail: [
    { required: true, validator: validateEmail, trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await login(loginForm)
    userStore.setUserInfo(res.data)
    userStore.setToken('token_' + Date.now())
    
    ElMessage.success('登录成功')
    
    const redirect = route.query.redirect || (res.data.userRole === 1 ? '/admin' : '/home')
    router.push(redirect)
  } catch (error) {
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}

const handleRegister = async () => {
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const userData = {
      userName: registerForm.userName,
      userPwd: registerForm.userPwd,
      userEmail: registerForm.userEmail
    }
    
    await register(userData)
    ElMessage.success('注册成功，请登录')
    activeTab.value = 'login'
    loginForm.userName = registerForm.userName
    registerFormRef.value.resetFields()
  } catch (error) {
    console.error('注册失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  if (userStore.isLoggedIn()) {
    router.push('/home')
  }
})
</script>

<style scoped lang="scss">
.login-container {
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-wrapper {
  width: 900px;
  height: 500px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  display: flex;
  overflow: hidden;
}

.login-left {
  width: 400px;
  height: 100%;
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #fff;

  .logo {
    margin-bottom: 20px;
  }

  .title {
    font-size: 36px;
    font-weight: bold;
    margin-bottom: 20px;
    letter-spacing: 8px;
  }

  .subtitle {
    font-size: 16px;
    opacity: 0.9;
    margin-bottom: 8px;
    letter-spacing: 2px;
  }
}

.login-right {
  flex: 1;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-tabs {
  :deep(.el-tabs__header) {
    margin-bottom: 30px;
  }

  :deep(.el-tabs__nav-wrap::after) {
    display: none;
  }

  :deep(.el-tabs__item) {
    font-size: 18px;
    font-weight: bold;
    color: #606266;
    height: 40px;
    line-height: 40px;
  }

  :deep(.el-tabs__item.is-active) {
    color: #409eff;
  }

  :deep(.el-tabs__active-bar) {
    background-color: #409eff;
    height: 3px;
  }
}

.login-btn,
.register-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: bold;
  letter-spacing: 4px;
}
</style>
