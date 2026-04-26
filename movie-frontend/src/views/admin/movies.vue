<template>
  <div class="movies-container">
    <h3 class="page-title">电影管理</h3>
    
    <div class="search-bar card p-20 mb-20">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="电影名称">
          <el-input v-model="searchForm.name" placeholder="请输入电影名称" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="电影状态">
          <el-select v-model="searchForm.state" placeholder="全部状态" clearable>
            <el-option label="热映中" :value="1" />
            <el-option label="即将上映" :value="2" />
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
          添加电影
        </el-button>
      </div>
    </div>
    
    <div class="card p-20">
      <el-table :data="movieList" v-loading="loading" stripe>
        <el-table-column prop="movieId" label="ID" width="80" />
        <el-table-column prop="movieCnName" label="中文名称" min-width="120" show-overflow-tooltip />
        <el-table-column prop="movieEnName" label="英文名称" min-width="120" show-overflow-tooltip />
        <el-table-column prop="movieType" label="类型" width="120" />
        <el-table-column prop="movieDuration" label="时长" width="80" />
        <el-table-column prop="movieReleaseDate" label="上映日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.movieReleaseDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="movieState" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.movieState === 1 ? 'success' : 'warning'" size="small">
              {{ row.movieState === 1 ? '热映中' : '即将上映' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="movieScore" label="评分" width="80" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
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
        <el-form-item label="中文名称" prop="movieCnName">
          <el-input v-model="form.movieCnName" placeholder="请输入中文名称" />
        </el-form-item>
        
        <el-form-item label="英文名称">
          <el-input v-model="form.movieEnName" placeholder="请输入英文名称" />
        </el-form-item>
        
        <el-form-item label="电影类型" prop="movieType">
          <el-input v-model="form.movieType" placeholder="请输入电影类型" />
        </el-form-item>
        
        <el-form-item label="电影时长" prop="movieDuration">
          <el-input v-model="form.movieDuration" placeholder="请输入电影时长(分钟)" />
        </el-form-item>
        
        <el-form-item label="上映日期" prop="movieReleaseDate">
          <el-date-picker
            v-model="form.movieReleaseDate"
            type="date"
            placeholder="请选择上映日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="电影状态" prop="movieState">
          <el-radio-group v-model="form.movieState">
            <el-radio :value="1">热映中</el-radio>
            <el-radio :value="2">即将上映</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="国家地区">
          <el-input v-model="form.movieCountry" placeholder="请输入国家地区" />
        </el-form-item>
        
        <el-form-item label="电影语言">
          <el-input v-model="form.movieLanguage" placeholder="请输入电影语言" />
        </el-form-item>
        
        <el-form-item label="电影导演">
          <el-input v-model="form.movieDirector" placeholder="请输入电影导演" />
        </el-form-item>
        
        <el-form-item label="电影演员">
          <el-input v-model="form.movieActor" placeholder="请输入电影演员" type="textarea" :rows="2" />
        </el-form-item>
        
        <el-form-item label="电影简介">
          <el-input v-model="form.movieIntroduction" placeholder="请输入电影简介" type="textarea" :rows="3" />
        </el-form-item>
        
        <el-form-item label="电影海报">
          <el-upload
            :show-file-list="false"
            :before-upload="beforeAvatarUpload"
            :on-success="handlePosterSuccess"
            action="/api/movie/addMovie"
          >
            <img v-if="form.moviePicture" :src="form.moviePicture" class="poster-preview" />
            <el-icon v-else class="upload-icon"><Plus /></el-icon>
          </el-upload>
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
import { getMovieList, addMovie, updateMovie, deleteMovie, getMoviesByName } from '@/api/movie'
import dayjs from 'dayjs'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const movieList = ref([])
const isEdit = ref(false)

const searchForm = reactive({
  name: '',
  state: undefined
})

const form = reactive({
  movieId: '',
  movieCnName: '',
  movieEnName: '',
  movieType: '',
  movieDuration: '',
  movieReleaseDate: '',
  movieState: 1,
  movieCountry: '',
  movieLanguage: '',
  movieDirector: '',
  movieActor: '',
  movieIntroduction: '',
  moviePicture: '',
  movieScore: 0,
  movieBoxOffice: 0,
  movieCommentCount: 0
})

const dialogTitle = computed(() => isEdit.value ? '编辑电影' : '添加电影')

const rules = {
  movieCnName: [
    { required: true, message: '请输入中文名称', trigger: 'blur' }
  ],
  movieType: [
    { required: true, message: '请输入电影类型', trigger: 'blur' }
  ],
  movieDuration: [
    { required: true, message: '请输入电影时长', trigger: 'blur' }
  ],
  movieReleaseDate: [
    { required: true, message: '请选择上映日期', trigger: 'change' }
  ],
  movieState: [
    { required: true, message: '请选择电影状态', trigger: 'change' }
  ]
}

const formatDate = (date) => {
  if (!date) return ''
  return dayjs(date).format('YYYY-MM-DD')
}

const loadMovies = async () => {
  loading.value = true
  try {
    let res
    if (searchForm.name) {
      res = await getMoviesByName(searchForm.name)
      movieList.value = res.data || []
      total.value = res.data?.length || 0
    } else {
      res = await getMovieList()
      const data = res.data || {}
      const onlineList = data.onlineList || []
      const upcomingList = data.upcomingList || []
      
      if (searchForm.state === 1) {
        movieList.value = onlineList
      } else if (searchForm.state === 2) {
        movieList.value = upcomingList
      } else {
        movieList.value = [...onlineList, ...upcomingList]
      }
      total.value = movieList.value.length
    }
  } catch (error) {
    console.error('加载电影列表失败:', error)
    movieList.value = []
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadMovies()
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.state = undefined
  currentPage.value = 1
  loadMovies()
}

const handleSizeChange = () => {
  currentPage.value = 1
  loadMovies()
}

const handleCurrentChange = () => {
  loadMovies()
}

const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  form.movieReleaseDate = formatDate(row.movieReleaseDate)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该电影吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await deleteMovie(row.movieId)
    if (res.code === 0) {
      ElMessage.success('删除成功')
      loadMovies()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const resetForm = () => {
  Object.assign(form, {
    movieId: '',
    movieCnName: '',
    movieEnName: '',
    movieType: '',
    movieDuration: '',
    movieReleaseDate: '',
    movieState: 1,
    movieCountry: '',
    movieLanguage: '',
    movieDirector: '',
    movieActor: '',
    movieIntroduction: '',
    moviePicture: '',
    movieScore: 0,
    movieBoxOffice: 0,
    movieCommentCount: 0
  })
}

const closeDialog = () => {
  dialogVisible.value = false
  formRef.value?.resetFields()
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

const handlePosterSuccess = (response) => {
  if (response.code === 0) {
    form.moviePicture = response.data
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    const formData = new FormData()
    formData.append('movieCnName', form.movieCnName)
    formData.append('movieEnName', form.movieEnName || '')
    formData.append('movieType', form.movieType)
    formData.append('movieDuration', form.movieDuration)
    formData.append('movieReleaseDate', form.movieReleaseDate)
    formData.append('movieState', form.movieState)
    formData.append('movieCountry', form.movieCountry || '')
    formData.append('movieLanguage', form.movieLanguage || '')
    formData.append('movieDirector', form.movieDirector || '')
    formData.append('movieActor', form.movieActor || '')
    formData.append('movieIntroduction', form.movieIntroduction || '')
    if (isEdit.value) {
      formData.append('movieId', form.movieId)
    }

    let res
    if (isEdit.value) {
      res = await updateMovie(formData)
    } else {
      res = await addMovie(formData)
    }

    if (res.code === 0) {
      ElMessage.success(isEdit.value ? '编辑成功' : '添加成功')
      closeDialog()
      loadMovies()
    }
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadMovies()
})
</script>

<style scoped lang="scss">
.movies-container {
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

  .poster-preview {
    width: 120px;
    height: 160px;
    object-fit: cover;
    border-radius: 4px;
  }

  .upload-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 160px;
    border: 1px dashed #d9d9d9;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;

    &:hover {
      border-color: #409eff;
      color: #409eff;
    }
  }
}
</style>
