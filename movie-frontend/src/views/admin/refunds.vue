<template>
  <div class="refunds-container">
    <h3 class="page-title">退票审核</h3>
    
    <div class="card p-20">
      <el-table :data="refundList" v-loading="loading" stripe>
        <el-table-column prop="orderId" label="订单号" width="180" />
        <el-table-column prop="userName" label="用户" width="100" />
        <el-table-column prop="movieCnName" label="电影" min-width="120" show-overflow-tooltip />
        <el-table-column prop="cinemaName" label="影院" min-width="120" show-overflow-tooltip />
        <el-table-column prop="hallName" label="放映厅" width="100" />
        <el-table-column prop="orderSeat" label="座位" min-width="100" show-overflow-tooltip />
        <el-table-column prop="orderPrice" label="退款金额" width="120">
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
        <el-table-column prop="orderTime" label="申请时间" width="160">
          <template #default="{ row }">
            {{ formatTime(row.orderTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button 
              v-if="row.orderState === 0" 
              type="success" 
              link 
              size="small"
              @click="handleAgreeRefund(row.orderId)"
            >
              同意退票
            </el-button>
            <el-tag v-else type="info" size="small">已处理</el-tag>
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
      
      <el-empty v-if="refundList.length === 0 && !loading" description="暂无退票申请" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllRefundOrders, agreeForRefund } from '@/api/order'
import dayjs from 'dayjs'

const loading = ref(false)

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const refundList = ref([])

const formatTime = (time) => {
  if (!time) return ''
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

const getOrderStatusType = (state) => {
  switch (state) {
    case 0: return 'warning'
    case 2: return 'success'
    default: return 'info'
  }
}

const getOrderStatusText = (state) => {
  switch (state) {
    case 0: return '待处理'
    case 2: return '已退款'
    default: return '未知'
  }
}

const loadRefunds = async () => {
  loading.value = true
  try {
    const params = {
      currentPage: currentPage.value,
      pageSize: pageSize.value
    }
    
    const res = await getAllRefundOrders(params)
    refundList.value = res.data || []
    total.value = res.count || 0
  } catch (error) {
    console.error('加载退票列表失败:', error)
    refundList.value = []
  } finally {
    loading.value = false
  }
}

const handleAgreeRefund = async (orderId) => {
  try {
    await ElMessageBox.confirm('确定要同意该退票申请吗？确认后将无法撤销。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await agreeForRefund(orderId)
    if (res.code === 0) {
      ElMessage.success('退票成功')
      loadRefunds()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('退票失败:', error)
    }
  }
}

const handleSizeChange = () => {
  currentPage.value = 1
  loadRefunds()
}

const handleCurrentChange = () => {
  loadRefunds()
}

onMounted(() => {
  loadRefunds()
})
</script>

<style scoped lang="scss">
.refunds-container {
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
