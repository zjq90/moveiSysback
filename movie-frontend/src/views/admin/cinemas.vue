<template>
  <div class="cinemas-container">
    <h3 class="page-title">影院管理</h3>
    
    <div class="search-bar card p-20 mb-20">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="影院名称">
          <el-input v-model="searchForm.name" placeholder="请输入影院名称" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      
      <div class="actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加影院
        </el-button>
      </div>
    </div>
    
    <div class="card p-20">
      <el-table :data="cinemaList" v-loading="loading" stripe>
        <el-table-column prop="cinemaId" label="ID" width="80" />
        <el-table-column prop="cinemaName" label="影院名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="cinemaAddress" label="影院地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="cinemaPhone" label="联系电话" width="120" />
        <el-table-column prop="cinemaService1" label="服务1" width="100" />
        <el-table-column prop="cinemaService2" label="服务2" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" @close="closeDialog">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="影院名称" prop="cinemaName">
          <el-input v-model="form.cinemaName" placeholder="请输入影院名称" />
        </el-form-item>
        
        <el-form-item label="影院地址" prop="cinemaAddress">
          <el-input v-model="form.cinemaAddress" placeholder="请输入影院地址" />
        </el-form-item>
        
        <el-form-item label="联系电话">
          <el-input v-model="form.cinemaPhone" placeholder="请输入联系电话" />
        </el-form-item>
        
        <el-form-item label="服务标签1">
          <el-input v-model="form.cinemaService1" placeholder="如：可停车、IMAX等" />
        </el-form-item>
        
        <el-form-item label="服务标签2">
          <el-input v-model="form.cinemaService2" placeholder="如：可停车、IMAX等" />
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
import { getAllCinemas, addCinema, updateCinema, deleteCinema } from '@/api/cinema'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)

const cinemaList = ref([])
const isEdit = ref(false)

const searchForm = reactive({
  name: ''
})

const form = reactive({
  cinemaId: '',
  cinemaName: '',
  cinemaAddress: '',
  cinemaPhone: '',
  cinemaService1: '',
  cinemaService2: ''
})

const dialogTitle = computed(() => isEdit.value ? '编辑影院' : '添加影院')

const rules = {
  cinemaName: [
    { required: true, message: '请输入影院名称', trigger: 'blur' }
  ],
  cinemaAddress: [
    { required: true, message: '请输入影院地址', trigger: 'blur' }
  ]
}

const loadCinemas = async () => {
  loading.value = true
  try {
    const res = await getAllCinemas()
    let list = res.data || []
    
    if (searchForm.name) {
      list = list.filter(item => 
        item.cinemaName?.includes(searchForm.name) ||
        item.cinemaAddress?.includes(searchForm.name)
      )
    }
    
    cinemaList.value = list
  } catch (error) {
    console.error('加载影院列表失败:', error)
    cinemaList.value = []
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  loadCinemas()
}

const resetSearch = () => {
  searchForm.name = ''
  loadCinemas()
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

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该影院吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await deleteCinema(row.cinemaId)
    if (res.code === 0 || res.data === 'success') {
      ElMessage.success('删除成功')
      loadCinemas()
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const resetForm = () => {
  Object.assign(form, {
    cinemaId: '',
    cinemaName: '',
    cinemaAddress: '',
    cinemaPhone: '',
    cinemaService1: '',
    cinemaService2: ''
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
      res = await updateCinema(form)
    } else {
      res = await addCinema(form)
    }

    if (res.code === 0 || res.data === 'success') {
      ElMessage.success(isEdit.value ? '编辑成功' : '添加成功')
      closeDialog()
      loadCinemas()
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
  loadCinemas()
})
</script>

<style scoped lang="scss">
.cinemas-container {
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
}
</style>
