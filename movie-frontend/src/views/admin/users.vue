<template>
  <div class="users-container">
    <h3 class="page-title">用户管理</h3>
    
    <div class="search-bar card p-20 mb-20">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="用户账号">
          <el-input v-model="searchForm.userName" placeholder="请输入用户账号" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      
      <div class="actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加用户
        </el-button>
      </div>
    </div>
    
    <div class="card p-20">
      <el-table :data="userList" v-loading="loading" stripe>
        <el-table-column prop="userId" label="ID" width="80" />
        <el-table-column prop="userName" label="用户账号" width="150" />
        <el-table-column prop="userPassword" label="密码" width="150" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="showPassword[row.userId]">{{ row.userPassword }}</span>
            <span v-else>******</span>
          </template>
        </el-table-column>
        <el-table-column prop="userEmail" label="邮箱" min-width="180" show-overflow-tooltip />
        <el-table-column prop="userRole" label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="row.userRole === 1 ? 'danger' : 'primary'" size="small">
              {{ row.userRole === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-wrapper mt-20" v-if="total > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" @close="closeDialog">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="用户账号" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户账号" :disabled="isEdit" />
        </el-form-item>
        
        <el-form-item label="用户密码" prop="userPassword">
          <el-input v-model="form.userPassword" placeholder="请输入用户密码" show-password />
        </el-form-item>
        
        <el-form-item label="用户邮箱" prop="userEmail">
          <el-input v-model="form.userEmail" placeholder="请输入用户邮箱" />
        </el-form-item>
        
        <el-form-item label="用户角色" prop="userRole">
          <el-radio-group v-model="form.userRole">
            <el-radio :value="0">普通用户</el-radio>
            <el-radio :value="1">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getUserList, getUserById, register, updateUser } from '@/api/user'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)
const showPassword = ref({})

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const userList = ref([])
const isEdit = ref(false)

const searchForm = reactive({
  userName: ''
})

const form = reactive({
  userId: '',
  userName: '',
  userPassword: '',
  userEmail: '',
  userRole: 0
})

const dialogTitle = computed(() => isEdit.value ? '编辑用户' : '添加用户')

const rules = {
  userName: [
    { required: true, message: '请输入用户账号', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入用户密码', trigger: 'blur' }
  ],
  userEmail: [
    { required: true, message: '请输入用户邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const loadUsers = async () => {
  loading.value = true
  try {
    const res = await getUserList({
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      userName: searchForm.userName || undefined
    })
    
    userList.value = res.data || []
    total.value = res.count || 0
    
    showPassword.value = {}
    userList.value.forEach(user => {
      showPassword.value[user.userId] = false
    })
  } catch (error) {
    console.error('加载用户列表失败:', error)
    userList.value = []
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadUsers()
}

const resetSearch = () => {
  searchForm.userName = ''
  currentPage.value = 1
  loadUsers()
}

const handleSizeChange = () => {
  currentPage.value = 1
  loadUsers()
}

const handleCurrentChange = () => {
  loadUsers()
}

const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const resetForm = () => {
  Object.assign(form, {
    userId: '',
    userName: '',
    userPassword: '',
    userEmail: '',
    userRole: 0
  })
}

const closeDialog = () => {
  dialogVisible.value = false
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    let res
    if (isEdit.value) {
      res = await updateUser({
        userId: form.userId,
        userName: form.userName,
        userPassword: form.userPassword,
        userEmail: form.userEmail
      })
    } else {
      res = await register({
        userName: form.userName,
        userPassword: form.userPassword,
        userEmail: form.userEmail
      })
    }

    if (res.code === 0 || res === 'success' || res.data === 'success') {
      ElMessage.success(isEdit.value ? '编辑成功' : '添加成功')
      closeDialog()
      loadUsers()
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped lang="scss">
.users-container {
  .page-title {
    font-size: 20px;
    font-weight: bold;
    color: #303133;
    margin-bottom: 20px;
  }

  .search-bar {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
  }
}
</style>
