<template>
  <div class="movie-detail-container">
    <HeaderComponent />
    
    <div class="main-content" v-loading="loading">
      <div class="movie-header">
        <div class="content-wrapper container">
          <div class="movie-info-section flex">
            <div class="movie-poster-wrapper">
              <img :src="getMoviePosterUrl(movieInfo.moviePicture)" :alt="movieInfo.movieCnName" class="movie-poster" />
            </div>
            <div class="movie-detail-info">
              <h1 class="movie-title">{{ movieInfo.movieCnName }}</h1>
              <p class="movie-en-name" v-if="movieInfo.movieEnName">{{ movieInfo.movieEnName }}</p>
              
              <div class="movie-meta">
                <div class="meta-item">
                  <span class="meta-label">类型:</span>
                  <span class="meta-value">{{ movieInfo.movieType }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">时长:</span>
                  <span class="meta-value">{{ movieInfo.movieDuration }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">上映日期:</span>
                  <span class="meta-value">{{ formatDate(movieInfo.movieReleaseDate) }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">国家/地区:</span>
                  <span class="meta-value">{{ movieInfo.movieCountry }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">语言:</span>
                  <span class="meta-value">{{ movieInfo.movieLanguage }}</span>
                </div>
              </div>
              
              <div class="movie-stats" v-if="movieInfo.movieState === 1">
                <div class="stat-item">
                  <span class="stat-value">{{ movieInfo.movieScore || 0 }}</span>
                  <span class="stat-label">评分</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ movieInfo.movieCommentCount || 0 }}</span>
                  <span class="stat-label">评价</span>
                </div>
                <div class="stat-item">
                  <span class="stat-value">{{ movieInfo.movieBoxOffice || 0 }}</span>
                  <span class="stat-label">票房(亿)</span>
                </div>
              </div>
              
              <div class="movie-actions">
                <el-button 
                  v-if="movieInfo.movieState === 1" 
                  type="primary" 
                  size="large" 
                  @click="goToBuyTickets"
                >
                  立即购票
                </el-button>
                <el-button 
                  v-else 
                  type="warning" 
                  size="large" 
                  disabled
                >
                  即将上映
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="content-wrapper container">
        <div class="movie-description card p-20 mb-20">
          <h3 class="section-title">剧情简介</h3>
          <p class="description-text">{{ movieInfo.movieIntroduction || '暂无简介' }}</p>
        </div>
        
        <div class="movie-comments card p-20 mb-20" v-if="movieInfo.movieState === 1">
          <h3 class="section-title">用户评论</h3>
          
          <div class="add-comment-section" v-if="userStore.isLoggedIn()">
            <el-input
              v-model="newComment"
              type="textarea"
              :rows="3"
              placeholder="写下你的评论..."
              maxlength="500"
              show-word-limit
            ></el-input>
            <div class="comment-actions text-right mt-10">
              <el-button type="primary" :loading="commentLoading" @click="submitComment">
                发表评论
              </el-button>
            </div>
          </div>
          
          <div class="comment-empty" v-else>
            <el-text type="info">请先登录后发表评论</el-text>
          </div>
          
          <div class="comments-list" v-if="comments.length > 0">
            <div v-for="comment in comments" :key="comment.commentId" class="comment-item">
              <div class="comment-header flex-center">
                <el-avatar :size="36" :src="comment.userHeadImg || defaultAvatar">
                  <el-icon><User /></el-icon>
                </el-avatar>
                <div class="comment-user-info">
                  <span class="user-name">{{ comment.userName }}</span>
                  <span class="comment-time">{{ formatTime(comment.commentTime) }}</span>
                </div>
              </div>
              <div class="comment-content">{{ comment.commentContent }}</div>
            </div>
          </div>
          
          <el-empty v-else description="暂无评论" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User } from '@element-plus/icons-vue'
import HeaderComponent from '@/components/Header.vue'
import { getMovieById } from '@/api/movie'
import { getCommentsByUserName, addComment } from '@/api/comment'
import { useUserStore } from '@/store/user'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const commentLoading = ref(false)
const movieInfo = ref({})
const comments = ref([])
const newComment = ref('')
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

const movieId = computed(() => route.params.id)

const getMoviePosterUrl = (url) => {
  if (!url) return 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png'
  if (url.startsWith('http') || url.startsWith('https')) return url
  if (url.startsWith('../')) return '/api' + url.substring(2)
  if (url.startsWith('/')) return '/api' + url
  return '/api/' + url
}

const formatDate = (date) => {
  if (!date) return ''
  return dayjs(date).format('YYYY年MM月DD日')
}

const formatTime = (time) => {
  if (!time) return ''
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

const loadMovieDetail = async () => {
  if (!movieId.value) return
  
  loading.value = true
  try {
    const res = await getMovieById(movieId.value)
    movieInfo.value = res.data || {}
    
    if (movieInfo.value.movieState === 1) {
      const commentRes = await getCommentsByUserName({ 
        movieId: movieId.value,
        currentPage: 1,
        pageSize: 100
      })
      comments.value = commentRes.data || []
    }
  } catch (error) {
    console.error('加载电影详情失败:', error)
  } finally {
    loading.value = false
  }
}

const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  
  if (!userStore.isLoggedIn()) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  commentLoading.value = true
  try {
    const userInfo = userStore.getUserInfo()
    await addComment({
      commentMovieId: movieId.value,
      commentUserId: userInfo.userId,
      commentContent: newComment.value.trim()
    })
    
    ElMessage.success('评论发表成功')
    newComment.value = ''
    
    loadMovieDetail()
  } catch (error) {
    console.error('发表评论失败:', error)
  } finally {
    commentLoading.value = false
  }
}

const goToBuyTickets = () => {
  router.push(`/buy-tickets/${movieId.value}`)
}

onMounted(() => {
  loadMovieDetail()
})
</script>

<style scoped lang="scss">
.movie-detail-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.main-content {
  padding-bottom: 40px;
}

.movie-header {
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
  padding: 40px 0;
  color: #fff;
}

.content-wrapper {
  padding: 0 20px;
}

.movie-info-section {
  display: flex;
  gap: 40px;

  .movie-poster-wrapper {
    flex-shrink: 0;
    width: 260px;

    .movie-poster {
      width: 100%;
      height: 360px;
      object-fit: cover;
      border-radius: 8px;
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
    }
  }

  .movie-detail-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .movie-title {
      font-size: 32px;
      font-weight: bold;
      margin-bottom: 8px;
    }

    .movie-en-name {
      font-size: 16px;
      color: rgba(255, 255, 255, 0.6);
      margin-bottom: 24px;
    }

    .movie-meta {
      margin-bottom: 24px;

      .meta-item {
        margin-bottom: 12px;
        font-size: 14px;

        .meta-label {
          color: rgba(255, 255, 255, 0.6);
          margin-right: 8px;
        }

        .meta-value {
          color: #fff;
        }
      }
    }

    .movie-stats {
      display: flex;
      gap: 40px;
      margin-bottom: 24px;

      .stat-item {
        text-align: center;

        .stat-value {
          display: block;
          font-size: 32px;
          font-weight: bold;
          color: #ff9800;
        }

        .stat-label {
          font-size: 12px;
          color: rgba(255, 255, 255, 0.6);
        }
      }
    }

    .movie-actions {
      .el-button {
        width: 160px;
        height: 48px;
        font-size: 16px;
        font-weight: bold;
      }
    }
  }
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  padding-left: 12px;
  border-left: 4px solid #409eff;
  margin-bottom: 16px;
}

.description-text {
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
}

.add-comment-section {
  margin-bottom: 24px;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 8px;
}

.comment-empty {
  margin-bottom: 24px;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 8px;
  text-align: center;
}

.comments-list {
  .comment-item {
    padding: 16px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .comment-header {
      margin-bottom: 12px;
      gap: 12px;

      .comment-user-info {
        display: flex;
        flex-direction: column;

        .user-name {
          font-size: 14px;
          font-weight: bold;
          color: #303133;
        }

        .comment-time {
          font-size: 12px;
          color: #909399;
        }
      }
    }

    .comment-content {
      font-size: 14px;
      color: #606266;
      line-height: 1.6;
      padding-left: 48px;
    }
  }
}
</style>
