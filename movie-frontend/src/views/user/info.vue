<template>
  <div class="user-info-container">
    <HeaderComponent />
    
    <div class="main-content">
      <div class="content-wrapper container">
        <div class="user-layout flex">
          <div class="sidebar card">
            <div class="user-profile text-center p-20">
              <el-avatar :size="80" :src="userInfo?.userHeadImg || defaultAvatar">
                <el-icon :size="40"><User /></el-icon>
              </el-avatar>
              <h4 class="user-name mt-10">{{ userInfo?.userName }}</h4>
              <el-tag :type="userInfo?.userRole === 1 ? 'danger' : 'primary'" size="small">
                {{ userInfo?.userRole === 1 ? '管理员' : '普通用户' }}
              </el-tag>
            </div>
            
            <el-menu
              :default-active="activeMenu"
              background-color="#fff"
              text-color="#606266"
              active-text-color="#409eff"
              router
            >
              <el-menu-item index="/user/info">
                <el-icon><User /></el-icon>
                <span>个人信息</span>
              </el-menu-item>
              <el-menu-item index="/user/orders">
                <el-icon><Ticket /></el-icon>
                <span>我的订单</span>
              </el-menu-item>
              <el-menu-item index="/user/refunds">
                <el-icon><Money /></el-icon>
                <span>退票申请</span>
              </el-menu-item>
            </el-menu>
          </div>
          
          <div class="content-area">
            <div class="card p-20">
              <h3 class="section-title">个人信息</h3>
              
              <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
                <el-form-item label="头像">
                  <div class="avatar-uploader">
                    <el-upload
                      class="avatar-uploader"
                      :show-file-list="false"
                      :before-upload="beforeAvatarUpload"
                      :on-success="handleAvatarSuccess"
                      action="/api/user/uploadHeadImg"
                    >
                      <img v-if="form.userHeadImg" :src="form.userHeadImg" class="avatar" />
                      <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                    </el-upload>
                    <span class="upload-tip">点击上传头像</span>
                  </div>
                </el-form-item>
                
                <el-form-item label="账号">
                  <el-input v-model="form.userName" disabled />
                </el-form-item>
                
                <el-form-item label="邮箱" prop="userEmail">
                  <el-input v-model="form.userEmail" placeholder="请输入邮箱" />
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" :loading="loading" @click="handleSubmit">保存修改</el-button>
                </el-form-item>
              </el-form>
            </div>
            
            <div class="card p-20 mt-20">
              <h3 class="section-title">修改密码</h3>
              
              <el-form :model="pwdForm" :rules="pwdRules" ref="pwdFormRef" label-width="120px">
                <el-form-item label="原密码" prop="oldPwd">
                  <el-input v-model="pwdForm.oldPwd" type="password" placeholder="请输入原密码" show-password />
                </el-form-item>
                
                <el-form-item label="新密码" prop="newPwd">
                  <el-input v-model="pwdForm.newPwd" type="password" placeholder="请输入新密码" show-password />
                </el-form-item>
                
                <el-form-item label="确认密码" prop="confirmPwd">
                  <el-input v-model="pwdForm.confirmPwd" type="password" placeholder="请确认新密码" show-password />
                </el-form-item>
                
                <el-form-item>
                  <el-button type="warning" :loading="pwdLoading" @click="handleModifyPwd">修改密码</el-button>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Ticket, Money, Plus } from '@element-plus/icons-vue'
import HeaderComponent from '@/components/Header.vue'
import { updateUser, modifyPassword } from '@/api/user'
import { useUserStore } from '@/store/user'

const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const loading = ref(false)
const pwdLoading = ref(false)
const formRef = ref(null)
const pwdFormRef = ref(null)
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

const userInfo = computed(() => userStore.getUserInfo())

const form = reactive({
  userName: '',
  userEmail: '',
  userHeadImg: ''
})

const pwdForm = reactive({
  oldPwd: '',
  newPwd: '',
  confirmPwd: ''
})

const validateConfirmPwd = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请确认新密码'))
  } else if (value !== pwdForm.newPwd) {
    callback(new Error('两次密码输入不一致'))
  } else {
    callback()
  }
}

const validateEmail = (rule, value, callback) => {
  const emailReg = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/
  if (!value) {
    callback(new Error('请输入邮箱'))
  } else if (!emailReg.test(value)) {
    callback(new Error('请输入正确的邮箱格式'))
  } else {
    callback()
  }
}

const rules = {
  userEmail: [
    { required: true, validator: validateEmail, trigger: 'blur' }
  ]
}

const pwdRules = {
  oldPwd: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPwd: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPwd: [
    { required: true, validator: validateConfirmPwd, trigger: 'blur' }
  ]
}

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传图片只能是 JPG 或 PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isJPG && isLt2M
}

const handleAvatarSuccess = (response) => {
  if (response.code === 0) {
    form.userHeadImg = response.data
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error('头像上传失败')
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const userData = {
      userId: userInfo.value.userId,
      userName: form.userName,
      userEmail: form.userEmail,
      userHeadImg: form.userHeadImg
    }
    
    const res = await updateUser(userData)
    if (res.code === 0) {
      userStore.setUserInfo({
        ...userInfo.value,
        userEmail: form.userEmail,
        userHeadImg: form.userHeadImg
      })
      ElMessage.success('修改成功')
    }
  } catch (error) {
    console.error('修改失败:', error)
  } finally {
    loading.value = false
  }
}

const handleModifyPwd = async () => {
  const valid = await pwdFormRef.value.validate().catch(() => false)
  if (!valid) return

  pwdLoading.value = true
  try {
    const res = await modifyPassword({
      userName: userInfo.value.userName,
      oldPwd: pwdForm.oldPwd,
      newPwd: pwdForm.newPwd
    })
    
    if (res.code === 0) {
      ElMessage.success('密码修改成功')
      pwdFormRef.value.resetFields()
    }
  } catch (error) {
    console.error('密码修改失败:', error)
  } finally {
    pwdLoading.value = false
  }
}

onMounted(() => {
  if (userInfo.value) {
    form.userName = userInfo.value.userName
    form.userEmail = userInfo.value.userEmail
    form.userHeadImg = userInfo.value.userHeadImg
  }
})
</script>

<style scoped lang="scss">
.user-info-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.main-content {
  padding: 20px 0 40px;
}

.content-wrapper {
  padding: 0 20px;
}

.user-layout {
  display: flex;
  gap: 20px;
}

.sidebar {
  width: 240px;
  flex-shrink: 0;
  border-radius: 8px;
  overflow: hidden;

  .user-profile {
    border-bottom: 1px solid #f0f0f0;

    .user-name {
      font-size: 16px;
      font-weight: bold;
      color: #303133;
    }
  }

  :deep(.el-menu) {
    border-right: none;
  }

  :deep(.el-menu-item) {
    height: 50px;
    line-height: 50px;
  }
}

.content-area {
  flex: 1;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  padding-left: 12px;
  border-left: 4px solid #409eff;
  margin-bottom: 20px;
}

.avatar-uploader {
  display: flex;
  align-items: center;
  gap: 16px;

  :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);

    &:hover {
      border-color: #409eff;
    }
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
  }

  .avatar {
    width: 100px;
    height: 100px;
    display: block;
    object-fit: cover;
  }

  .upload-tip {
    font-size: 12px;
    color: #909399;
  }
}
</style>
