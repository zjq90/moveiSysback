<template>
  <div class="orders-container">
    <h3 class="page-title">订单管理</h3>
    
    <div class="search-bar card p-20 mb-20">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.state" placeholder="全部状态" clearable>
            <el-option label="退票中" :value="0" />
            <el-option label="已付款" :value="1" />
            <el-option label="退票成功" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <div class="card p-20">
      <el-table :data="orderList" v-loading="loading" stripe>
        <el-table-column prop="orderId" label="订单号" width="180" />
        <el-table-column prop="userName" label="用户" width="100" />
        <el-table-column prop="movieCnName" label="电影" min-width="120" show-overflow-tooltip />
        <el-table-column prop="cinemaName" label="影院" min-width="120" show-overflow-tooltip />
        <el-table-column prop="hallName" label="放映厅" width="100" />
        <el-table-column prop="orderSeat" label="座位" min-width="100" show-overflow-tooltip />
        <el-table-column prop="orderPrice" label="金额" width="100">
          <template #default="{ row }">
            <span class="price">¥{{ row.orderPrice }}</span>
          </template>
        </el-table-column>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getAllOrders } from '@/api/order'
import dayjs from 'dayjs'

const loading = ref(false)

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const orderList = ref([])

const searchForm = reactive({
  state: undefined
})

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

const loadOrders = async () => {
  loading.value = true
  try {
    const params = {
      currentPage: currentPage.value,
      pageSize: pageSize.value
    }
    
    const res = await getAllOrders(params)
    orderList.value = res.data || []
    total.value = res.count || 0
  } catch (error) {
    console.error('加载订单列表失败:', error)
    orderList.value = []
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadOrders()
}

const resetSearch = () => {
  searchForm.state = undefined
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

onMounted(() => {
  loadOrders()
})
</script>

<style scoped lang="scss">
.orders-container {
  .page-title {
    font-size: 20px;
    font-weight: bold;
    color: #303133;
    margin-bottom: 20px;
  }

  .price {
    color: #ff6b6b;
    font-weight: bold;
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
  }
}
</style>
