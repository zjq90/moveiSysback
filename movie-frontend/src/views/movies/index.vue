<template>
  <div class="movies-container">
    <HeaderComponent />
    
    <div class="main-content">
      <div class="content-wrapper container">
        <div class="filter-section card p-20 mb-20">
          <div class="filter-item">
            <span class="filter-label">类型:</span>
            <el-radio-group v-model="filterParams.type" @change="handleFilterChange">
              <el-radio-button label="">全部</el-radio-button>
              <el-radio-button label="动作">动作</el-radio-button>
              <el-radio-button label="喜剧">喜剧</el-radio-button>
              <el-radio-button label="爱情">爱情</el-radio-button>
              <el-radio-button label="科幻">科幻</el-radio-button>
              <el-radio-button label="动画">动画</el-radio-button>
              <el-radio-button label="悬疑">悬疑</el-radio-button>
            </el-radio-group>
          </div>
          <div class="filter-item">
            <span class="filter-label">排序:</span>
            <el-radio-group v-model="filterParams.sort" @change="handleFilterChange">
              <el-radio-button label="">默认</el-radio-button>
              <el-radio-button label="boxOffice">票房</el-radio-button>
              <el-radio-button label="score">评分</el-radio-button>
              <el-radio-button label="commentCount">评价</el-radio-button>
            </el-radio-group>
          </div>
        </div>
        
        <div class="movies-section card p-20">
          <div class="section-header flex-between">
            <h3 class="section-title">{{ activeTab === 'hot' ? '热映电影' : '即将上映' }}</h3>
            <el-tabs v-model="activeTab" @tab-change="handleTabChange">
              <el-tab-pane label="热映" name="hot"></el-tab-pane>
              <el-tab-pane label="即将上映" name="upcoming"></el-tab-pane>
            </el-tabs>
          </div>
          
          <div v-if="loading" class="loading-container flex-center">
            <el-icon class="is-loading" :size="40"><Loading /></el-icon>
          </div>
          
          <div v-else-if="movieList.length > 0" class="movie-grid">
            <div 
              v-for="movie in movieList" 
              :key="movie.movieId" 
              class="movie-card"
              @click="goToMovieDetail(movie.movieId)"
            >
              <div class="movie-poster-wrapper">
                <img :src="getMoviePosterUrl(movie.moviePicture)" :alt="movie.movieCnName" class="movie-poster" />
                <div class="movie-score-badge" v-if="movie.movieScore > 0">
                  <span class="score">{{ movie.movieScore }}</span>
                </div>
                <div class="movie-date-badge" v-else>
                  <span>{{ formatDate(movie.movieReleaseDate) }}上映</span>
                </div>
              </div>
              <div class="movie-info">
                <h4 class="movie-name">{{ movie.movieCnName }}</h4>
                <p class="movie-type">{{ movie.movieType }}</p>
                <p class="movie-detail" v-if="movie.movieScore > 0">
                  <span>评分 {{ movie.movieScore }}</span>
                  <span>评价 {{ movie.movieCommentCount }}</span>
                </p>
                <p class="movie-detail" v-else>
                  <span>{{ movie.movieCountry }}</span>
                  <span>{{ movie.movieLanguage }}</span>
                </p>
              </div>
            </div>
          </div>
          
          <div v-else class="empty-container text-center p-40">
            <el-empty description="暂无电影数据" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Loading } from '@element-plus/icons-vue'
import HeaderComponent from '@/components/Header.vue'
import { getMovieList, sortMovies, getMoviesByType } from '@/api/movie'
import dayjs from 'dayjs'

const router = useRouter()

const activeTab = ref('hot')
const loading = ref(false)
const movieList = ref([])

const filterParams = reactive({
  type: '',
  sort: ''
})

const formatDate = (date) => {
  if (!date) return ''
  return dayjs(date).format('MM月DD日')
}

const getMoviePosterUrl = (url) => {
  if (!url) return 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png'
  if (url.startsWith('http') || url.startsWith('https')) return url
  if (url.startsWith('../')) return '/api' + url.substring(2)
  if (url.startsWith('/')) return '/api' + url
  return '/api/' + url
}

const goToMovieDetail = (movieId) => {
  router.push(`/movie/${movieId}`)
}

const loadMovies = async () => {
  loading.value = true
  try {
    let res
    if (filterParams.sort) {
      res = await sortMovies(filterParams.sort)
      movieList.value = res.data || []
    } else if (filterParams.type) {
      res = await getMoviesByType(filterParams.type)
      movieList.value = res.data || []
    } else {
      res = await getMovieList()
      const data = res.data || {}
      if (activeTab.value === 'hot') {
        movieList.value = data.onlineList || []
      } else {
        movieList.value = data.upcomingList || []
      }
    }
  } catch (error) {
    console.error('加载电影列表失败:', error)
    movieList.value = []
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  loadMovies()
}

const handleFilterChange = () => {
  loadMovies()
}

onMounted(() => {
  loadMovies()
})
</script>

<style scoped lang="scss">
.movies-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.main-content {
  padding: 20px 0 40px;
}

.content-wrapper {
  padding: 0 20px;
}

.filter-section {
  .filter-item {
    display: flex;
    align-items: center;
    margin-bottom: 16px;

    &:last-child {
      margin-bottom: 0;
    }

    .filter-label {
      font-size: 14px;
      color: #606266;
      margin-right: 12px;
      width: 50px;
      flex-shrink: 0;
    }
  }
}

.movies-section {
  .section-header {
    margin-bottom: 24px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .section-title {
    font-size: 20px;
    font-weight: bold;
    color: #303133;
    padding-left: 12px;
    border-left: 4px solid #409eff;
    margin: 0;
  }

  :deep(.el-tabs__header) {
    margin: 0;
  }
}

.loading-container {
  height: 300px;
}

.movie-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 24px;
}

.movie-card {
  cursor: pointer;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);

    .movie-poster {
      transform: scale(1.05);
    }
  }

  .movie-poster-wrapper {
    position: relative;
    width: 100%;
    height: 280px;
    overflow: hidden;

    .movie-poster {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }

    .movie-score-badge,
    .movie-date-badge {
      position: absolute;
      top: 12px;
      right: 12px;
      background: rgba(0, 0, 0, 0.7);
      padding: 4px 10px;
      border-radius: 4px;
      font-weight: bold;

      .score {
        color: #ff9800;
        font-size: 16px;
      }
    }

    .movie-date-badge {
      color: #fff;
      font-size: 12px;
      font-weight: normal;
    }
  }

  .movie-info {
    padding: 16px;

    .movie-name {
      font-size: 16px;
      font-weight: bold;
      color: #303133;
      margin-bottom: 8px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .movie-type {
      font-size: 13px;
      color: #909399;
      margin-bottom: 8px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .movie-detail {
      font-size: 12px;
      color: #606266;

      span + span {
        margin-left: 12px;
      }
    }
  }
}

.empty-container {
  min-height: 300px;
}
</style>
