<template>
  <div class="rank-container">
    <HeaderComponent />
    
    <div class="main-content">
      <div class="content-wrapper container">
        <div class="page-header">
          <h2 class="page-title">电影榜单</h2>
        </div>
        
        <div class="rank-layout flex">
          <div class="rank-section left-section">
            <div class="section-card">
              <div class="section-header">
                <h3 class="section-title section-title-red">票房榜</h3>
              </div>
              <div class="rank-list">
                <div 
                  v-for="(movie, index) in boxOfficeList" 
                  :key="movie.movieId" 
                  class="rank-item"
                  @click="goToMovieDetail(movie.movieId)"
                >
                  <template v-if="index === 0">
                    <div class="top-ranking">
                      <div class="top-badge">
                        <span class="top-num">1</span>
                      </div>
                      <img :src="getMoviePosterUrl(movie.moviePicture)" :alt="movie.movieCnName" class="top-poster" />
                      <div class="top-info">
                        <div class="top-name">{{ movie.movieCnName }}</div>
                        <div class="top-box-office">
                          <span class="box-office-num">{{ movie.movieBoxOffice || 0 }}</span>
                          <span class="box-office-unit">万</span>
                        </div>
                      </div>
                    </div>
                  </template>
                  <template v-else>
                    <span class="rank-num" :class="{ 'rank-highlight': index < 3 }">{{ index + 1 }}</span>
                    <img :src="getMoviePosterUrl(movie.moviePicture)" :alt="movie.movieCnName" class="rank-poster" />
                    <div class="rank-info">
                      <div class="rank-name">{{ movie.movieCnName }}</div>
                      <div class="rank-meta">
                        <span class="rank-score" v-if="movie.movieScore > 0">评分 {{ movie.movieScore }}</span>
                        <span class="rank-type">{{ movie.movieType }}</span>
                      </div>
                    </div>
                    <div class="rank-box-office">
                      <span class="box-office-num">{{ movie.movieBoxOffice || 0 }}</span>
                      <span class="box-office-unit">万</span>
                    </div>
                  </template>
                </div>
              </div>
            </div>
          </div>
          
          <div class="rank-section right-section">
            <div class="section-card mb-20">
              <div class="section-header">
                <h3 class="section-title section-title-blue">热映电影</h3>
              </div>
              <div class="mini-rank-list">
                <div 
                  v-for="(movie, index) in hotMovies" 
                  :key="movie.movieId" 
                  class="mini-rank-item"
                  @click="goToMovieDetail(movie.movieId)"
                >
                  <span class="mini-rank-num" :class="{ 'mini-rank-highlight': index < 3 }">{{ index + 1 }}</span>
                  <div class="mini-rank-info">
                    <span class="mini-rank-name">{{ movie.movieCnName }}</span>
                    <span class="mini-rank-score" v-if="movie.movieScore > 0">{{ movie.movieScore }}</span>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="section-card">
              <div class="section-header">
                <h3 class="section-title section-title-orange">最受期待</h3>
              </div>
              <div class="mini-rank-list">
                <div 
                  v-for="(movie, index) in upcomingMovies" 
                  :key="movie.movieId" 
                  class="mini-rank-item"
                  @click="goToMovieDetail(movie.movieId)"
                >
                  <span class="mini-rank-num" :class="{ 'mini-rank-highlight': index < 3 }">{{ index + 1 }}</span>
                  <div class="mini-rank-info">
                    <span class="mini-rank-name">{{ movie.movieCnName }}</span>
                    <span class="mini-rank-date">{{ formatDate(movie.movieReleaseDate) }}上映</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import HeaderComponent from '@/components/Header.vue'
import { getMovieList } from '@/api/movie'
import dayjs from 'dayjs'

const router = useRouter()

const boxOfficeList = ref([])
const hotMovies = ref([])
const upcomingMovies = ref([])

const getMoviePosterUrl = (url) => {
  if (!url) return 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png'
  if (url.startsWith('http') || url.startsWith('https')) return url
  if (url.startsWith('../')) return '/api' + url.substring(2)
  if (url.startsWith('/')) return '/api' + url
  return '/api/' + url
}

const formatDate = (date) => {
  if (!date) return ''
  return dayjs(date).format('MM月DD日')
}

const goToMovieDetail = (movieId) => {
  router.push(`/movie/${movieId}`)
}

const loadMovies = async () => {
  try {
    const res = await getMovieList()
    const data = res.data || {}
    boxOfficeList.value = data.boxOfficeList || []
    hotMovies.value = (data.onlineList || []).slice(0, 10)
    upcomingMovies.value = (data.upcomingList || []).slice(0, 10)
  } catch (error) {
    console.error('加载榜单失败:', error)
  }
}

onMounted(() => {
  loadMovies()
})
</script>

<style scoped lang="scss">
.rank-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.main-content {
  padding: 20px 0 40px;
}

.content-wrapper {
  padding: 0 20px;
}

.page-header {
  margin-bottom: 20px;
  
  .page-title {
    font-size: 24px;
    font-weight: bold;
    color: #303133;
    padding-left: 12px;
    border-left: 4px solid #409eff;
    margin: 0;
  }
}

.rank-layout {
  display: flex;
  gap: 20px;
}

.rank-section {
  &.left-section {
    flex: 1;
  }
  
  &.right-section {
    width: 320px;
    flex-shrink: 0;
  }
}

.section-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.section-header {
  margin-bottom: 20px;
  
  .section-title {
    font-size: 18px;
    font-weight: bold;
    margin: 0;
    padding-left: 10px;
    border-left: 3px solid #e54d42;
    
    &.section-title-red {
      border-left-color: #e54d42;
      color: #e54d42;
    }
    
    &.section-title-blue {
      border-left-color: #409eff;
      color: #409eff;
    }
    
    &.section-title-orange {
      border-left-color: #ff9800;
      color: #ff9800;
    }
  }
}

.rank-list {
  .rank-item {
    cursor: pointer;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;
    display: flex;
    align-items: center;
    transition: background 0.3s ease;
    
    &:last-child {
      border-bottom: none;
    }
    
    &:hover {
      background: #f9f9f9;
    }
    
    .top-ranking {
      display: flex;
      align-items: center;
      gap: 16px;
      width: 100%;
      
      .top-badge {
        width: 48px;
        height: 48px;
        background: linear-gradient(135deg, #ff9800 0%, #ff6b6b 100%);
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        
        .top-num {
          font-size: 24px;
          font-weight: bold;
          color: #fff;
        }
      }
      
      .top-poster {
        width: 70px;
        height: 98px;
        object-fit: cover;
        border-radius: 6px;
      }
      
      .top-info {
        flex: 1;
        
        .top-name {
          font-size: 18px;
          font-weight: bold;
          color: #303133;
          margin-bottom: 8px;
        }
        
        .top-box-office {
          .box-office-num {
            font-size: 24px;
            font-weight: bold;
            color: #ff6b6b;
          }
          
          .box-office-unit {
            font-size: 14px;
            color: #909399;
          }
        }
      }
    }
    
    .rank-num {
      width: 32px;
      height: 32px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f5f5f5;
      border-radius: 4px;
      font-size: 14px;
      font-weight: bold;
      color: #909399;
      margin-right: 12px;
      
      &.rank-highlight {
        background: #ff9800;
        color: #fff;
      }
    }
    
    .rank-poster {
      width: 40px;
      height: 56px;
      object-fit: cover;
      border-radius: 4px;
      margin-right: 12px;
    }
    
    .rank-info {
      flex: 1;
      
      .rank-name {
        font-size: 15px;
        font-weight: bold;
        color: #303133;
        margin-bottom: 4px;
      }
      
      .rank-meta {
        font-size: 12px;
        color: #909399;
        
        .rank-score {
          color: #ff9800;
          margin-right: 12px;
        }
      }
    }
    
    .rank-box-office {
      .box-office-num {
        font-size: 16px;
        font-weight: bold;
        color: #ff6b6b;
      }
      
      .box-office-unit {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

.mini-rank-list {
  .mini-rank-item {
    cursor: pointer;
    display: flex;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid #f0f0f0;
    transition: background 0.3s ease;
    
    &:last-child {
      border-bottom: none;
    }
    
    &:hover {
      background: #f9f9f9;
    }
    
    .mini-rank-num {
      width: 24px;
      height: 24px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f5f5f5;
      border-radius: 4px;
      font-size: 12px;
      font-weight: bold;
      color: #909399;
      margin-right: 10px;
      
      &.mini-rank-highlight {
        background: #e54d42;
        color: #fff;
      }
    }
    
    .mini-rank-info {
      flex: 1;
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .mini-rank-name {
        font-size: 14px;
        color: #303133;
        max-width: 160px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      
      .mini-rank-score {
        font-size: 14px;
        font-weight: bold;
        color: #ff9800;
      }
      
      .mini-rank-date {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}
</style>
