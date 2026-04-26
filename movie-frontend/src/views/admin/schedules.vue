<template>
  <div class="schedules-container">
    <h3 class="page-title">场次管理</h3>
    
    <div class="search-bar card p-20 mb-20">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="电影名称">
          <el-input v-model="searchForm.movieName" placeholder="请输入电影名称" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="场次状态">
          <el-select v-model="searchForm.state" placeholder="全部状态" clearable>
            <el-option label="已上映" :value="1" />
            <el-option label="已下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
      
      <div class="actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加场次
        </el-button>
      </div>
    </div>
    
    <div class="card p-20">
      <el-table :data="scheduleList" v-loading="loading" stripe>
        <el-table-column prop="scheduleId" label="ID" width="80" />
        <el-table-column prop="movieCnName" label="电影名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="cinemaName" label="影院名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="hallName" label="影厅" width="100" />
        <el-table-column prop="scheduleStartTime" label="开始时间" width="160">
          <template #default="{ row }">
            {{ formatTime(row.scheduleStartTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="scheduleEndTime" label="结束时间" width="160">
          <template #default="{ row }">
            {{ formatTime(row.scheduleEndTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="schedulePrice" label="价格(元)" width="100" />
        <el-table-column prop="scheduleState" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.scheduleState === 1 ? 'success' : 'info'" size="small">
              {{ row.scheduleState === 1 ? '已上映' : '已下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.scheduleState === 1" type="warning" link size="small" @click="handleOffline(row)">下架</el-button>
            <el-button type="primary" link size="small" @click="handleEditPrice(row)">改价</el-button>
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
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" @close="closeDialog">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="选择电影" prop="movieId">
          <el-select v-model="form.movieId" placeholder="请选择电影" style="width: 100%;" filterable @change="handleMovieChange">
            <el-option v-for="movie in movieList" :key="movie.movieId" :label="movie.movieCnName" :value="movie.movieId" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="选择影院" prop="cinemaId">
          <el-select v-model="form.cinemaId" placeholder="请选择影院" style="width: 100%;" @change="handleCinemaChange">
            <el-option v-for="cinema in cinemaList" :key="cinema.cinemaId" :label="cinema.cinemaName" :value="cinema.cinemaId" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="选择影厅" prop="hallId">
          <el-select v-model="form.hallId" placeholder="请选择影厅" style="width: 100%;">
            <el-option v-for="hall in hallList" :key="hall.hallId" :label="hall.hallName" :value="hall.hallId" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="开始时间" prop="scheduleStartTime">
          <el-date-picker
            v-model="form.scheduleStartTime"
            type="datetime"
            placeholder="请选择开始时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%;"
          />
        </el-form-item>
        
        <el-form-item label="价格" prop="schedulePrice">
          <el-input-number v-model="form.schedulePrice" :min="1" :max="1000" :precision="2" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
    
    <el-dialog v-model="priceDialogVisible" title="修改票价" width="400px">
      <el-form :model="priceForm" label-width="80px">
        <el-form-item label="当前价格">
          <span>{{ currentSchedule?.schedulePrice }} 元</span>
        </el-form-item>
        <el-form-item label="新价格">
          <el-input-number v-model="priceForm.newPrice" :min="1" :max="1000" :precision="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="priceDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmitPrice">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getMovieList } from '@/api/movie'
import { getAllCinemas, getCinemaById } from '@/api/cinema'
import { getHallsByCinemaId, getHallById } from '@/api/hall'
import { 
  getAllSchedules, 
  getSchedulesByMovieName, 
  getOffSchedulesByMovieName,
  addSchedule,
  updateSchedulePrice,
  offlineSchedule
} from '@/api/schedule'
import dayjs from 'dayjs'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const priceDialogVisible = ref(false)
const formRef = ref(null)

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const scheduleList = ref([])
const movieList = ref([])
const cinemaList = ref([])
const hallList = ref([])
const isEdit = ref(false)
const currentSchedule = ref(null)

const searchForm = reactive({
  movieName: '',
  state: undefined
})

const form = reactive({
  scheduleId: '',
  movieId: undefined,
  cinemaId: undefined,
  hallId: undefined,
  scheduleStartTime: '',
  schedulePrice: 35
})

const priceForm = reactive({
  newPrice: 35
})

const dialogTitle = computed(() => isEdit.value ? '编辑场次' : '添加场次')

const rules = {
  movieId: [
    { required: true, message: '请选择电影', trigger: 'change' }
  ],
  cinemaId: [
    { required: true, message: '请选择影院', trigger: 'change' }
  ],
  hallId: [
    { required: true, message: '请选择影厅', trigger: 'change' }
  ],
  scheduleStartTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  schedulePrice: [
    { required: true, message: '请输入价格', trigger: 'blur' }
  ]
}

const formatTime = (time) => {
  if (!time) return ''
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

const loadMovies = async () => {
  try {
    const res = await getMovieList()
    const data = res.data || {}
    movieList.value = [
      ...(data.onlineList || []),
      ...(data.upcomingList || [])
    ]
  } catch (error) {
    console.error('加载电影列表失败:', error)
    movieList.value = []
  }
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

const loadSchedules = async () => {
  loading.value = true
  try {
    let res
    if (searchForm.movieName) {
      if (searchForm.state === 0) {
        res = await getOffSchedulesByMovieName({ 
          movieName: searchForm.movieName,
          currentPage: currentPage.value,
          pageSize: pageSize.value
        })
      } else {
        res = await getSchedulesByMovieName({ 
          movieName: searchForm.movieName,
          currentPage: currentPage.value,
          pageSize: pageSize.value
        })
      }
    } else {
      if (searchForm.state === 0) {
        res = await getScheduleListByState({
          scheduleState: 0,
          currentPage: currentPage.value,
          pageSize: pageSize.value
        })
      } else {
        res = await getAllSchedules({
          currentPage: currentPage.value,
          pageSize: pageSize.value
        })
      }
    }
    
    scheduleList.value = res.data || []
    total.value = res.count || 0
  } catch (error) {
    console.error('加载场次列表失败:', error)
    scheduleList.value = []
  } finally {
    loading.value = false
  }
}

const handleMovieChange = () => {
}

const handleCinemaChange = async () => {
  if (!form.cinemaId) {
    hallList.value = []
    return
  }
  try {
    const res = await getHallsByCinemaId(form.cinemaId)
    hallList.value = res.data || []
  } catch (error) {
    console.error('加载影厅列表失败:', error)
    hallList.value = []
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadSchedules()
}

const resetSearch = () => {
  searchForm.movieName = ''
  searchForm.state = undefined
  currentPage.value = 1
  loadSchedules()
}

const handleSizeChange = () => {
  currentPage.value = 1
  loadSchedules()
}

const handleCurrentChange = () => {
  loadSchedules()
}

const handleAdd = () => {
  isEdit.value = false
  resetForm()
  hallList.value = []
  dialogVisible.value = true
}

const handleEditPrice = (row) => {
  currentSchedule.value = row
  priceForm.newPrice = row.schedulePrice
  priceDialogVisible.value = true
}

const handleOffline = async (row) => {
  try {
    await ElMessageBox.confirm('确定要下架该场次吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await offlineSchedule(row.scheduleId)
    if (res.code === 0 || res.data === 'success') {
      ElMessage.success('下架成功')
      loadSchedules()
    } else {
      ElMessage.error(res.msg || '下架失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('下架失败:', error)
    }
  }
}

const resetForm = () => {
  Object.assign(form, {
    scheduleId: '',
    movieId: undefined,
    cinemaId: undefined,
    hallId: undefined,
    scheduleStartTime: '',
    schedulePrice: 35
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
      movieId: form.movieId,
      cinemaId: form.cinemaId,
      hallId: form.hallId,
      scheduleStartTime: form.scheduleStartTime,
      schedulePrice: form.schedulePrice
    }
    
    const res = await addSchedule(data)

    if (res.code === 0 || res.data === 'success') {
      ElMessage.success('添加成功')
      closeDialog()
      loadSchedules()
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

const handleSubmitPrice = async () => {
  if (!currentSchedule.value) return
  
  submitting.value = true
  try {
    const res = await updateSchedulePrice(
      currentSchedule.value.scheduleId,
      priceForm.newPrice
    )
    
    if (res.code === 0 || res.data === 'success') {
      ElMessage.success('修改成功')
      priceDialogVisible.value = false
      loadSchedules()
    } else {
      ElMessage.error(res.msg || '修改失败')
    }
  } catch (error) {
    console.error('修改失败:', error)
    ElMessage.error('修改失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadMovies()
  loadCinemas()
  loadSchedules()
})
</script>

<style scoped lang="scss">
.schedules-container {
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
