<template>
  <div class="user-orders-container">
    <HeaderComponent />
    
    <div class="main-content">
      <div class="content-wrapper container">
        <div class="user-layout flex">
          <div class="sidebar card">
            <div class="user-profile text-center p-20">
              <el-avatar :size="80" :src="userInfo?.userHeadImg || defaultAvatar">
                <el-icon :size="40"><User /></el-icon>
              </el-avatar>
              <h4 class="user-name mt-10">{{ userInfo?.userName }}</h4>
              <el-tag :type="userInfo?.userRole === 1 ? 'danger' : 'primary'" size="small">
                {{ userInfo?.userRole === 1 ? '管理员' : '普通用户' }}
              </el-tag>
            </div>
            
            <el-menu
              :default-active="activeMenu"
              background-color="#fff"
              text-color="#606266"
              active-text-color="#409eff"
              router
            >
              <el-menu-item index="/user/info">
                <el-icon><User /></el-icon>
                <span>个人信息</span>
              </el-menu-item>
              <el-menu-item index="/user/orders">
                <el-icon><Ticket /></el-icon>
                <span>我的订单</span>
              </el-menu-item>
              <el-menu-item index="/user/refunds">
                <el-icon><Money /></el-icon>
                <span>退票申请</span>
              </el-menu-item>
            </el-menu>
          </div>
          
          <div class="content-area">
            <div class="card p-20">
              <h3 class="section-title">我的订单</h3>
              
              <el-tabs v-model="activeTab" @tab-change="handleTabChange">
                <el-tab-pane label="全部订单" name="all"></el-tab-pane>
                <el-tab-pane label="待付款" name="pending"></el-tab-pane>
                <el-tab-pane label="已付款" name="paid"></el-tab-pane>
                <el-tab-pane label="退票中" name="refunding"></el-tab-pane>
              </el-tabs>
              
              <div v-loading="loading" class="order-list">
                <div 
                  v-for="order in orders" 
                  :key="order.orderId" 
                  class="order-item card"
                >
                  <div class="order-header flex-between">
                    <div class="order-info-left">
                      <span class="order-no">订单号: {{ order.orderId }}</span>
                      <span class="order-time">{{ formatTime(order.orderTime) }}</span>
                    </div>
                    <el-tag :type="getOrderStatusType(order.orderState)">
                      {{ getOrderStatusText(order.orderState) }}
                    </el-tag>
                  </div>
                  
                  <div class="order-content flex">
                    <div class="movie-poster">
                      <img :src="getMoviePosterUrl(order.moviePicture)" :alt="order.movieCnName" />
                    </div>
                    <div class="order-detail">
                      <h4 class="movie-name">{{ order.movieCnName }}</h4>
                      <div class="detail-row">
                        <span class="detail-label">影院:</span>
                        <span class="detail-value">{{ order.cinemaName }}</span>
                      </div>
                      <div class="detail-row">
                        <span class="detail-label">场次:</span>
                        <span class="detail-value">
                          {{ formatTime(order.scheduleStartTime) }} - {{ formatEndTime(order.scheduleStartTime, order.movieDuration) }}
                        </span>
                      </div>
                      <div class="detail-row">
                        <span class="detail-label">放映厅:</span>
                        <span class="detail-value">{{ order.hallName }}</span>
                      </div>
                      <div class="detail-row">
                        <span class="detail-label">座位:</span>
                        <span class="detail-value">{{ order.orderSeat }}</span>
                      </div>
                    </div>
                    <div class="order-price">
                      <div class="price-info">
                        <span class="price-label">订单金额</span>
                        <span class="price-value">¥{{ order.orderPrice }}</span>
                      </div>
                      <div class="order-actions">
                        <el-button 
                          v-if="order.orderState === 1" 
                          type="danger" 
                          size="small"
                          @click="applyRefund(order.orderId)"
                        >
                          申请退票
                        </el-button>
                      </div>
                    </div>
                  </div>
                </div>
                
                <el-empty v-if="orders.length === 0 && !loading" description="暂无订单" />
              </div>
              
              <div class="pagination-wrapper mt-20" v-if="total > 0">
                <el-pagination
                  v-model:current-page="currentPage"
                  v-model:page-size="pageSize"
                  :page-sizes="[10, 20, 50]"
                  :total="total"
                  layout="total, sizes, prev, pager, next, jumper"
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Ticket, Money } from '@element-plus/icons-vue'
import HeaderComponent from '@/components/Header.vue'
import { getOrdersByUserName, applyForRefund } from '@/api/order'
import { useUserStore } from '@/store/user'
import dayjs from 'dayjs'

const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const loading = ref(false)
const activeTab = ref('all')
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const orders = ref([])

const userInfo = computed(() => userStore.getUserInfo())

const getMoviePosterUrl = (url) => {
  if (!url) return 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png'
  if (url.startsWith('http') || url.startsWith('https')) return url
  if (url.startsWith('../')) return '/api' + url.substring(2)
  if (url.startsWith('/')) return '/api' + url
  return '/api/' + url
}

const formatTime = (time) => {
  if (!time) return ''
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

const formatEndTime = (startTime, duration) => {
  if (!startTime) return ''
  const minutes = parseInt(duration) || 0
  return dayjs(startTime).add(minutes, 'minute').format('HH:mm')
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

const loadOrders = async () => {
  if (!userInfo.value) return
  
  loading.value = true
  try {
    const params = {
      userName: userInfo.value.userName,
      currentPage: currentPage.value,
      pageSize: pageSize.value
    }
    
    const res = await getOrdersByUserName(params)
    orders.value = res.data || []
    total.value = res.count || 0
  } catch (error) {
    console.error('加载订单失败:', error)
    orders.value = []
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  currentPage.value = 1
  loadOrders()
}

const handleSizeChange = () => {
  currentPage.value = 1
  loadOrders()
}

const handleCurrentChange = () => {
  loadOrders()
}

const applyRefund = async (orderId) => {
  try {
    await ElMessageBox.confirm('确定要申请退票吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await applyForRefund(orderId)
    if (res.code === 0) {
      ElMessage.success('退票申请已提交')
      loadOrders()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('退票申请失败:', error)
    }
  }
}

onMounted(() => {
  if (userStore.isLoggedIn()) {
    loadOrders()
  }
})
</script>

<style scoped lang="scss">
.user-orders-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.main-content {
  padding: 20px 0 40px;
}

.content-wrapper {
  padding: 0 20px;
}

.user-layout {
  display: flex;
  gap: 20px;
}

.sidebar {
  width: 240px;
  flex-shrink: 0;
  border-radius: 8px;
  overflow: hidden;

  .user-profile {
    border-bottom: 1px solid #f0f0f0;

    .user-name {
      font-size: 16px;
      font-weight: bold;
      color: #303133;
    }
  }

  :deep(.el-menu) {
    border-right: none;
  }

  :deep(.el-menu-item) {
    height: 50px;
    line-height: 50px;
  }
}

.content-area {
  flex: 1;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  padding-left: 12px;
  border-left: 4px solid #409eff;
  margin-bottom: 20px;
}

.order-list {
  .order-item {
    margin-bottom: 16px;
    border: none;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    .order-header {
      padding: 12px 16px;
      background: #f9f9f9;
      border-radius: 4px 4px 0 0;

      .order-info-left {
        .order-no {
          font-size: 13px;
          color: #606266;
          margin-right: 20px;
        }

        .order-time {
          font-size: 13px;
          color: #909399;
        }
      }
    }

    .order-content {
      padding: 16px;
      display: flex;
      gap: 20px;

      .movie-poster {
        width: 100px;
        flex-shrink: 0;

        img {
          width: 100%;
          height: 140px;
          object-fit: cover;
          border-radius: 4px;
        }
      }

      .order-detail {
        flex: 1;

        .movie-name {
          font-size: 16px;
          font-weight: bold;
          color: #303133;
          margin-bottom: 12px;
        }

        .detail-row {
          margin-bottom: 8px;
          font-size: 13px;

          .detail-label {
            color: #909399;
            margin-right: 8px;
          }

          .detail-value {
            color: #606266;
          }
        }
      }

      .order-price {
        width: 140px;
        flex-shrink: 0;
        text-align: right;
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        .price-info {
          .price-label {
            display: block;
            font-size: 13px;
            color: #909399;
            margin-bottom: 4px;
          }

          .price-value {
            display: block;
            font-size: 20px;
            font-weight: bold;
            color: #ff6b6b;
          }
        }

        .order-actions {
          margin-top: 12px;
        }
      }
    }
  }
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
}
</style>
