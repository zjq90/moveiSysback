<template>
  <div class="halls-container">
    <h3 class="page-title">影厅管理</h3>
    
    <div class="search-bar card p-20 mb-20">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="选择影院">
          <el-select v-model="searchForm.cinemaId" placeholder="请选择影院" clearable @change="handleCinemaChange">
            <el-option v-for="cinema in cinemaList" :key="cinema.cinemaId" :label="cinema.cinemaName" :value="cinema.cinemaId" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      
      <div class="actions">
        <el-button type="primary" @click="handleAdd" :disabled="!searchForm.cinemaId">
          <el-icon><Plus /></el-icon>
          添加影厅
        </el-button>
      </div>
    </div>
    
    <div class="card p-20">
      <el-table :data="hallList" v-loading="loading" stripe>
        <el-table-column prop="hallId" label="ID" width="80" />
        <el-table-column prop="hallName" label="影厅名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="cinemaName" label="所属影院" min-width="150" show-overflow-tooltip />
        <el-table-column prop="hallCapacity" label="容纳人数" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-empty v-if="!loading && hallList.length === 0 && searchForm.cinemaId" description="该影院暂无影厅" />
      <el-empty v-if="!loading && hallList.length === 0 && !searchForm.cinemaId" description="请先选择影院" />
    </div>
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" @close="closeDialog">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="影厅名称" prop="hallName">
          <el-input v-model="form.hallName" placeholder="请输入影厅名称" />
        </el-form-item>
        
        <el-form-item label="所属影院" prop="cinemaId">
          <el-select v-model="form.cinemaId" placeholder="请选择影院" style="width: 100%;">
            <el-option v-for="cinema in cinemaList" :key="cinema.cinemaId" :label="cinema.cinemaName" :value="cinema.cinemaId" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="容纳人数" prop="hallCapacity">
          <el-input-number v-model="form.hallCapacity" :min="1" :max="500" />
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
import { getAllCinemas } from '@/api/cinema'
import { getHallsByCinemaId, addHall, updateHall, deleteHall } from '@/api/hall'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)

const cinemaList = ref([])
const hallList = ref([])
const isEdit = ref(false)

const searchForm = reactive({
  cinemaId: undefined
})

const form = reactive({
  hallId: '',
  hallName: '',
  cinemaId: undefined,
  hallCapacity: 144
})

const dialogTitle = computed(() => isEdit.value ? '编辑影厅' : '添加影厅')

const rules = {
  hallName: [
    { required: true, message: '请输入影厅名称', trigger: 'blur' }
  ],
  cinemaId: [
    { required: true, message: '请选择影院', trigger: 'change' }
  ]
}

const loadCinemas = async () => {
  try {
    const res = await getAllCinemas()
    cinemaList.value = res.data || []
  } catch (error) {
    console.error('加载影院列表失败:', error)
    cinemaList.value = []
  }
}

const loadHalls = async () => {
  if (!searchForm.cinemaId) {
    hallList.value = []
    return
  }
  
  loading.value = true
  try {
    const res = await getHallsByCinemaId(searchForm.cinemaId)
    let list = res.data || []
    
    const cinema = cinemaList.value.find(c => c.cinemaId === searchForm.cinemaId)
    if (cinema) {
      list = list.map(item => ({
        ...item,
        cinemaName: cinema.cinemaName
      }))
    }
    
    hallList.value = list
  } catch (error) {
    console.error('加载影厅列表失败:', error)
    hallList.value = []
  } finally {
    loading.value = false
  }
}

const handleCinemaChange = () => {
  loadHalls()
}

const handleSearch = () => {
  loadHalls()
}

const resetSearch = () => {
  searchForm.cinemaId = undefined
  hallList.value = []
}

const handleAdd = () => {
  isEdit.value = false
  resetForm()
  form.cinemaId = searchForm.cinemaId
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该影厅吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await deleteHall(row.hallId)
    if (res.code === 0 || res.data === 'success') {
      ElMessage.success('删除成功')
      loadHalls()
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
    hallId: '',
    hallName: '',
    cinemaId: undefined,
    hallCapacity: 144
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
    const data = {
      hallName: form.hallName,
      cinemaId: form.cinemaId,
      hallCapacity: form.hallCapacity
    }
    
    let res
    if (isEdit.value) {
      res = await updateHall({ ...data, hallId: form.hallId })
    } else {
      res = await addHall(data)
    }

    if (res.code === 0 || res.data === 'success') {
      ElMessage.success(isEdit.value ? '编辑成功' : '添加成功')
      closeDialog()
      loadHalls()
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
.halls-container {
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
