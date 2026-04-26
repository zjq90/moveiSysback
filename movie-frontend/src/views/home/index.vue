<template>
  <div class="home-container">
    <HeaderComponent />
    
    <div class="main-content">
      <el-carousel height="368px" class="banner-carousel" :interval="5000" indicator-position="outside">
        <el-carousel-item v-for="(movie, index) in bannerMovies" :key="index">
          <div class="banner-item" @click="goToMovieDetail(movie.movieId)">
            <div class="banner-bg" :style="{ backgroundImage: 'url(' + getMoviePosterUrl(movie.moviePicture) + ')' }"></div>
            <div class="banner-content">
              <div class="movie-info-left">
                <img :src="getMoviePosterUrl(movie.moviePicture)" :alt="movie.movieCnName" class="movie-poster-large" />
              </div>
              <div class="movie-info-right">
                <h2 class="movie-title">{{ movie.movieCnName }}</h2>
                <p class="movie-en" v-if="movie.movieFgName">{{ movie.movieFgName }}</p>
                <div class="movie-meta">
                  <span class="meta-item">{{ movie.movieType }}</span>
                  <span class="meta-item">{{ movie.movieCountry }}</span>
                  <span class="meta-item">{{ movie.movieDuration }}</span>
                </div>
                <div class="movie-score-section" v-if="movie.movieScore > 0">
                  <span class="score-label">评分</span>
                  <span class="score-value">{{ movie.movieScore }}</span>
                </div>
                <el-button type="primary" size="large" class="buy-btn" @click.stop="goToBuyTickets(movie.movieId)">
                  立即购票
                </el-button>
              </div>
            </div>
          </div>
        </el-carousel-item>
        
        <el-carousel-item v-if="bannerMovies.length === 0">
          <div class="banner-item default-banner">
            <div class="banner-content default-content">
              <h2>新片上映</h2>
              <p>精彩电影，尽在鹰眼电影</p>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
      
      <div class="content-wrapper container">
        <div class="main-layout flex">
          <div class="left-content">
            <div class="movie-section">
              <div class="section-header flex-between">
                <h3 class="section-title">正在热映（{{ hotMovies.length }}部）</h3>
                <el-button type="primary" link @click="router.push('/movies')">
                  全部 <el-icon><ArrowRight /></el-icon>
                </el-button>
              </div>
              <div class="movie-grid">
                <div 
                  v-for="movie in hotMovies" 
                  :key="movie.movieId" 
                  class="movie-item"
                  @click="goToMovieDetail(movie.movieId)"
                >
                  <div class="movie-poster-wrapper">
                    <img :src="getMoviePosterUrl(movie.moviePicture)" :alt="movie.movieCnName" class="movie-poster" />
                    <div class="movie-overlay">
                      <div class="movie-score-badge">
                        <span class="score">{{ movie.movieScore || '暂无' }}</span>
                      </div>
                      <div class="movie-info-overlay">
                        <div class="movie-name-overlay">{{ movie.movieCnName }}</div>
                        <div class="movie-type-overlay">{{ movie.movieType }}</div>
                      </div>
                    </div>
                  </div>
                  <div class="movie-action">
                    <el-button type="primary" size="small" class="buy-btn-small" @click.stop="goToBuyTickets(movie.movieId)">
                      购票
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="movie-section">
              <div class="section-header flex-between">
                <h3 class="section-title section-title-blue">即将上映（{{ upcomingMovies.length }}部）</h3>
                <el-button type="primary" link @click="router.push('/movies')">
                  全部 <el-icon><ArrowRight /></el-icon>
                </el-button>
              </div>
              <div class="movie-grid">
                <div 
                  v-for="movie in upcomingMovies" 
                  :key="movie.movieId" 
                  class="movie-item"
                  @click="goToMovieDetail(movie.movieId)"
                >
                  <div class="movie-poster-wrapper">
                    <img :src="getMoviePosterUrl(movie.moviePicture)" :alt="movie.movieCnName" class="movie-poster" />
                    <div class="movie-overlay">
                      <div class="movie-date-badge">
                        <span>{{ formatDate(movie.movieReleaseDate) }} 上映</span>
                      </div>
                      <div class="movie-info-overlay">
                        <div class="movie-name-overlay">{{ movie.movieCnName }}</div>
                        <div class="movie-type-overlay">{{ movie.movieType }}</div>
                      </div>
                    </div>
                  </div>
                  <div class="movie-action">
                    <span class="wish-count">{{ getRandomWishCount() }}人想看</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="right-sidebar">
            <div class="sidebar-section">
              <div class="sidebar-header">
                <h4 class="sidebar-title sidebar-title-red">总体票房</h4>
              </div>
              <div class="box-office-list-sidebar">
                <div 
                  v-for="(movie, index) in boxOfficeMovies" 
                  :key="movie.movieId" 
                  class="box-office-item-sidebar"
                  @click="goToMovieDetail(movie.movieId)"
                >
                  <template v-if="index === 0">
                    <div class="top-ranking">
                      <div class="top-icon">
                        <el-icon><Top /></el-icon>
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
                    <span class="movie-name-sidebar">{{ movie.movieCnName }}</span>
                    <span class="box-office-sidebar">
                      <span class="box-office-num">{{ movie.movieBoxOffice || 0 }}</span>
                      <span class="box-office-unit">万</span>
                    </span>
                  </template>
                </div>
              </div>
            </div>
            
            <div class="sidebar-section mt-20" v-if="upcomingMovies.length > 0">
              <div class="sidebar-header">
                <h4 class="sidebar-title sidebar-title-orange">最受期待</h4>
              </div>
              <div class="expect-list">
                <div 
                  v-for="(movie, index) in upcomingMovies.slice(0, 5)" 
                  :key="movie.movieId" 
                  class="expect-item"
                  @click="goToMovieDetail(movie.movieId)"
                >
                  <span class="expect-rank">{{ index + 1 }}</span>
                  <div class="expect-info">
                    <div class="expect-name">{{ movie.movieCnName }}</div>
                    <div class="expect-date">上映时间：{{ formatDate(movie.movieReleaseDate) }}</div>
                    <div class="expect-wish">
                      <span class="wish-num">{{ getRandomWishCount() }}</span>人想看
                    </div>
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
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowRight, Top } from '@element-plus/icons-vue'
import HeaderComponent from '@/components/Header.vue'
import { getMovieList } from '@/api/movie'
import dayjs from 'dayjs'

const router = useRouter()

const hotMovies = ref([])
const upcomingMovies = ref([])
const boxOfficeMovies = ref([])
const bannerMovies = computed(() => hotMovies.value.slice(0, 5))

const formatDate = (date) => {
  if (!date) return ''
  return dayjs(date).format('MM月DD日')
}

const getMoviePosterUrl = (url) => {
  if (!url) return 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png'
  if (url.startsWith('http') || url.startsWith('https')) {
    return url
  }
  if (url.startsWith('../')) {
    return '/api' + url.substring(2)
  }
  return '/api' + url
}

const getRandomWishCount = () => {
  const base = [10000, 50000, 100000, 200000, 300000, 500000, 600000]
  return base[Math.floor(Math.random() * base.length)].toLocaleString()
}

const goToMovieDetail = (movieId) => {
  router.push(`/movie/${movieId}`)
}

const goToBuyTickets = (movieId) => {
  router.push(`/buy-tickets/${movieId}`)
}

const loadMovies = async () => {
  try {
    const res = await getMovieList()
    const data = res.data || {}
    hotMovies.value = data.onlineList || []
    upcomingMovies.value = data.upcomingList || []
    boxOfficeMovies.value = data.boxOfficeList || []
  } catch (error) {
    console.error('加载电影失败:', error)
  }
}

onMounted(() => {
  loadMovies()
})
</script>

<style scoped lang="scss">
.home-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.main-content {
  padding-bottom: 40px;
}

.banner-carousel {
  margin-bottom: 30px;

  .banner-item {
    width: 100%;
    height: 100%;
    position: relative;
    cursor: pointer;
    overflow: hidden;

    .banner-bg {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background-size: cover;
      background-position: center;
      filter: blur(20px) brightness(0.4);
      transform: scale(1.1);
    }

    .banner-content {
      position: relative;
      z-index: 1;
      display: flex;
      align-items: center;
      height: 100%;
      padding: 0 60px;

      .movie-info-left {
        flex-shrink: 0;

        .movie-poster-large {
          width: 220px;
          height: 310px;
          object-fit: cover;
          border-radius: 8px;
          box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
        }
      }

      .movie-info-right {
        margin-left: 40px;
        flex: 1;

        .movie-title {
          font-size: 32px;
          font-weight: bold;
          color: #fff;
          margin-bottom: 8px;
        }

        .movie-en {
          font-size: 16px;
          color: rgba(255, 255, 255, 0.7);
          margin-bottom: 20px;
        }

        .movie-meta {
          margin-bottom: 20px;

          .meta-item {
            font-size: 14px;
            color: rgba(255, 255, 255, 0.8);
            margin-right: 20px;
            padding: 4px 12px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 4px;
          }
        }

        .movie-score-section {
          margin-bottom: 24px;

          .score-label {
            font-size: 14px;
            color: rgba(255, 255, 255, 0.8);
            margin-right: 10px;
          }

          .score-value {
            font-size: 36px;
            font-weight: bold;
            color: #ff9800;
          }
        }

        .buy-btn {
          width: 160px;
          height: 48px;
          font-size: 16px;
          font-weight: bold;
        }
      }
    }

    &.default-banner {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

      .default-content {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: #fff;
        text-align: center;
      }
    }
  }
}

.content-wrapper {
  padding: 0 20px;
}

.main-layout {
  display: flex;
  gap: 20px;
}

.left-content {
  flex: 1;
}

.right-sidebar {
  width: 300px;
  flex-shrink: 0;
}

.movie-section {
  margin-bottom: 30px;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.section-header {
  margin-bottom: 20px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  padding-left: 12px;
  border-left: 4px solid #e54d42;
  margin: 0;

  &.section-title-blue {
    border-left-color: #409eff;
  }
}

.movie-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.movie-item {
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    .movie-poster {
      transform: scale(1.05);
    }
  }

  .movie-poster-wrapper {
    position: relative;
    width: 100%;
    height: 260px;
    border-radius: 8px;
    overflow: hidden;
    margin-bottom: 12px;

    .movie-poster {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }

    .movie-overlay {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(transparent 50%, rgba(0, 0, 0, 0.8));
      opacity: 0;
      transition: opacity 0.3s ease;

      .movie-score-badge,
      .movie-date-badge {
        position: absolute;
        top: 10px;
        right: 10px;
        background: rgba(0, 0, 0, 0.7);
        color: #ff9800;
        padding: 4px 10px;
        border-radius: 4px;
        font-weight: bold;

        .score {
          font-size: 14px;
        }
      }

      .movie-date-badge {
        color: #fff;
        font-size: 12px;
        font-weight: normal;
      }

      .movie-info-overlay {
        position: absolute;
        bottom: 10px;
        left: 10px;
        right: 10px;
        color: #fff;

        .movie-name-overlay {
          font-size: 14px;
          font-weight: bold;
          margin-bottom: 4px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .movie-type-overlay {
          font-size: 12px;
          opacity: 0.8;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }

    &:hover .movie-overlay {
      opacity: 1;
    }
  }

  .movie-action {
    text-align: center;
  }

  .wish-count {
    font-size: 12px;
    color: #909399;
  }
}

.sidebar-section {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.sidebar-header {
  margin-bottom: 16px;
}

.sidebar-title {
  font-size: 16px;
  font-weight: bold;
  margin: 0;

  &.sidebar-title-red {
    color: #e54d42;
  }

  &.sidebar-title-orange {
    color: #ff9800;
  }
}

.box-office-list-sidebar {
  .box-office-item-sidebar {
    cursor: pointer;
    padding: 8px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    &:hover {
      background: #f9f9f9;
    }

    .top-ranking {
      display: flex;
      gap: 12px;

      .top-icon {
        color: #ff9800;
        font-size: 24px;
      }

      .top-poster {
        width: 50px;
        height: 70px;
        object-fit: cover;
        border-radius: 4px;
      }

      .top-info {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;

        .top-name {
          font-size: 14px;
          font-weight: bold;
          color: #303133;
          margin-bottom: 4px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .top-box-office {
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

    .rank-num {
      width: 24px;
      height: 24px;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      background: #f5f5f5;
      border-radius: 4px;
      font-size: 12px;
      color: #909399;
      margin-right: 10px;

      &.rank-highlight {
        background: #e54d42;
        color: #fff;
      }
    }

    .movie-name-sidebar {
      font-size: 13px;
      color: #606266;
      margin-right: 10px;
      max-width: 100px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .box-office-sidebar {
      float: right;

      .box-office-num {
        font-size: 13px;
        font-weight: bold;
        color: #ff6b6b;
      }

      .box-office-unit {
        font-size: 11px;
        color: #909399;
      }
    }
  }
}

.expect-list {
  .expect-item {
    display: flex;
    align-items: center;
    cursor: pointer;
    padding: 8px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    &:hover {
      background: #f9f9f9;
    }

    .expect-rank {
      width: 20px;
      text-align: center;
      font-size: 14px;
      font-weight: bold;
      color: #ff9800;
      margin-right: 10px;
    }

    .expect-info {
      flex: 1;

      .expect-name {
        font-size: 13px;
        font-weight: bold;
        color: #303133;
        margin-bottom: 2px;
      }

      .expect-date {
        font-size: 11px;
        color: #909399;
        margin-bottom: 2px;
      }

      .expect-wish {
        font-size: 11px;
        color: #909399;

        .wish-num {
          color: #ff6b6b;
          font-weight: bold;
        }
      }
    }
  }
}
</style>
