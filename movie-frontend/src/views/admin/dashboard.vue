<template>
  <div class="dashboard-container">
    <h3 class="page-title">数据概览</h3>
    
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <div class="stat-card total-movies">
          <div class="stat-icon">
            <el-icon :size="40"><Film /></el-icon>
          </div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.totalMovies }}</span>
            <span class="stat-label">电影总数</span>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card total-orders">
          <div class="stat-icon">
            <el-icon :size="40"><Ticket /></el-icon>
          </div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.totalOrders }}</span>
            <span class="stat-label">订单总数</span>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card total-users">
          <div class="stat-icon">
            <el-icon :size="40"><User /></el-icon>
          </div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.totalUsers }}</span>
            <span class="stat-label">用户总数</span>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card total-revenue">
          <div class="stat-icon">
            <el-icon :size="40"><Money /></el-icon>
          </div>
          <div class="stat-info">
            <span class="stat-value">{{ stats.totalRevenue.toFixed(2) }}</span>
            <span class="stat-label">总票房(万)</span>
          </div>
        </div>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="chart-section">
      <el-col :span="16">
        <div class="card p-20">
          <h4 class="card-title">近期票房趋势</h4>
          <div class="chart-placeholder">
            <el-empty description="图表功能需要引入ECharts" />
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="card p-20">
          <h4 class="card-title">热门电影排行</h4>
          <div class="movie-ranking">
            <div v-for="(movie, index) in hotMovies" :key="movie.movieId" class="ranking-item">
              <span class="rank" :class="{ 'top-three': index < 3 }">{{ index + 1 }}</span>
              <span class="movie-name">{{ movie.movieCnName }}</span>
              <span class="movie-boxOffice">{{ movie.movieBoxOffice.toFixed(2) }}亿</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <div class="card p-20">
          <h4 class="card-title">待处理退票</h4>
          <el-table :data="pendingRefunds" v-loading="loading" stripe>
            <el-table-column prop="orderId" label="订单号" width="180" />
            <el-table-column prop="userName" label="用户" width="100" />
            <el-table-column prop="movieCnName" label="电影" min-width="120" show-overflow-tooltip />
            <el-table-column prop="orderTime" label="下单时间" width="160">
              <template #default="{ row }">
                {{ formatTime(row.orderTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button type="primary" link size="small" @click="goToRefunds">处理</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card p-20">
          <h4 class="card-title">最近订单</h4>
          <el-table :data="recentOrders" v-loading="loading" stripe>
            <el-table-column prop="orderId" label="订单号" width="180" />
            <el-table-column prop="userName" label="用户" width="100" />
            <el-table-column prop="movieCnName" label="电影" min-width="120" show-overflow-tooltip />
            <el-table-column prop="orderState" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getOrderStatusType(row.orderState)" size="small">
                  {{ getOrderStatusText(row.orderState) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="orderTime" label="下单时间" width="160">
              <template #default="{ row }">
                {{ formatTime(row.orderTime) }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Film, Ticket, User, Money } from '@element-plus/icons-vue'
import { getMovieList, getBoxOfficeMovies } from '@/api/movie'
import { getAllOrders, getAllRefundOrders } from '@/api/order'
import { getUserList } from '@/api/user'
import dayjs from 'dayjs'

const router = useRouter()
const loading = ref(false)

const stats = reactive({
  totalMovies: 0,
  totalOrders: 0,
  totalUsers: 0,
  totalRevenue: 0
})

const hotMovies = ref([])
const pendingRefunds = ref([])
const recentOrders = ref([])

const formatTime = (time) => {
  if (!time) return ''
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

const getOrderStatusType = (state) => {
  switch (state) {
    case 0: return 'warning'
    case 1: return 'success'
    case 2: return 'info'
    default: return 'info'
  }
}

const getOrderStatusText = (state) => {
  switch (state) {
    case 0: return '退票中'
    case 1: return '已付款'
    case 2: return '退票成功'
    default: return '未知'
  }
}

const loadStats = async () => {
  loading.value = true
  try {
    const [movieRes, orderRes, userRes, boxOfficeRes] = await Promise.all([
      getMovieList(),
      getAllOrders({ currentPage: 1, pageSize: 100 }),
      getUserList({ currentPage: 1, pageSize: 1 }),
      getBoxOfficeMovies()
    ])
    
    stats.totalMovies = (movieRes.onlineList?.length || 0) + (movieRes.upcomingList?.length || 0)
    stats.totalOrders = orderRes.count || 0
    stats.totalUsers = userRes.count || 0
    stats.totalRevenue = (boxOfficeRes.data || []).reduce((sum, m) => sum + (m.movieBoxOffice || 0), 0)
    
    hotMovies.value = (boxOfficeRes.data || []).slice(0, 5)
    
    recentOrders.value = (orderRes.data || []).slice(0, 5)
    
    const refundRes = await getAllRefundOrders({ currentPage: 1, pageSize: 5 })
    pendingRefunds.value = refundRes.data || []
  } catch (error) {
    console.error('加载统计数据失败:', error)
  } finally {
    loading.value = false
  }
}

const goToRefunds = () => {
  router.push('/admin/refunds')
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped lang="scss">
.dashboard-container {
  .page-title {
    font-size: 20px;
    font-weight: bold;
    color: #303133;
    margin-bottom: 20px;
  }

  .stat-cards {
    margin-bottom: 20px;
  }

  .stat-card {
    display: flex;
    align-items: center;
    padding: 20px;
    border-radius: 8px;
    color: #fff;

    .stat-icon {
      margin-right: 20px;
    }

    .stat-info {
      flex: 1;

      .stat-value {
        display: block;
        font-size: 28px;
        font-weight: bold;
      }

      .stat-label {
        display: block;
        font-size: 14px;
        opacity: 0.8;
        margin-top: 4px;
      }
    }

    &.total-movies {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }

    &.total-orders {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }

    &.total-users {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }

    &.total-revenue {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }
  }

  .chart-section {
    margin-bottom: 20px;
  }

  .card-title {
    font-size: 16px;
    font-weight: bold;
    color: #303133;
    margin-bottom: 16px;
  }

  .chart-placeholder {
    height: 300px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .movie-ranking {
    .ranking-item {
      display: flex;
      align-items: center;
      padding: 12px 0;
      border-bottom: 1px solid #f0f0f0;

      &:last-child {
        border-bottom: none;
      }

      .rank {
        width: 30px;
        font-size: 16px;
        font-weight: bold;
        color: #909399;
        text-align: center;

        &.top-three {
          color: #ff9800;
        }
      }

      .movie-name {
        flex: 1;
        font-size: 14px;
        color: #606266;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin-right: 10px;
      }

      .movie-boxOffice {
        font-size: 14px;
        font-weight: bold;
        color: #ff6b6b;
      }
    }
  }
}
</style>
