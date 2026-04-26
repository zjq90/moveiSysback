<template>
  <div class="cinemas-container">
    <HeaderComponent />
    
    <div class="main-content">
      <div class="content-wrapper container">
        <div class="page-header">
          <h2 class="page-title">影院列表</h2>
        </div>
        
        <div v-if="loading" class="loading-container flex-center">
          <el-icon class="is-loading" :size="40"><Loading /></el-icon>
        </div>
        
        <div v-else-if="cinemaList.length > 0" class="cinema-grid">
          <div 
            v-for="cinema in cinemaList" 
            :key="cinema.cinemaId" 
            class="cinema-card"
          >
            <div class="cinema-info">
              <h3 class="cinema-name">{{ cinema.cinemaName }}</h3>
              <p class="cinema-address">
                <el-icon><Location /></el-icon>
                <span>{{ cinema.cinemaAddress }}</span>
              </p>
              <p class="cinema-phone">
                <el-icon><Phone /></el-icon>
                <span>{{ cinema.cinemaPhone || '暂无联系电话' }}</span>
              </p>
              <div class="cinema-tags">
                <el-tag type="success" size="small" v-if="cinema.cinemaService1">{{ cinema.cinemaService1 }}</el-tag>
                <el-tag type="primary" size="small" v-if="cinema.cinemaService2">{{ cinema.cinemaService2 }}</el-tag>
              </div>
            </div>
            <div class="cinema-action">
              <el-button type="primary" link>查看详情</el-button>
            </div>
          </div>
        </div>
        
        <div v-else class="empty-container text-center p-40">
          <el-empty description="暂无影院数据" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Loading, Location, Phone } from '@element-plus/icons-vue'
import HeaderComponent from '@/components/Header.vue'
import { getAllCinemas } from '@/api/cinema'

const loading = ref(false)
const cinemaList = ref([])

const loadCinemas = async () => {
  loading.value = true
  try {
    const res = await getAllCinemas()
    cinemaList.value = res.data || []
  } catch (error) {
    console.error('加载影院列表失败:', error)
    cinemaList.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadCinemas()
})
</script>

<style scoped lang="scss">
.cinemas-container {
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

.loading-container {
  height: 300px;
}

.cinema-grid {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.cinema-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  
  &:hover {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  }
  
  .cinema-info {
    flex: 1;
    
    .cinema-name {
      font-size: 18px;
      font-weight: bold;
      color: #303133;
      margin-bottom: 12px;
    }
    
    .cinema-address,
    .cinema-phone {
      display: flex;
      align-items: flex-start;
      gap: 8px;
      font-size: 14px;
      color: #606266;
      margin-bottom: 8px;
      
      .el-icon {
        flex-shrink: 0;
        margin-top: 2px;
      }
      
      span {
        flex: 1;
        line-height: 1.5;
      }
    }
    
    .cinema-tags {
      margin-top: 12px;
      
      :deep(.el-tag) {
        margin-right: 8px;
      }
    }
  }
  
  .cinema-action {
    flex-shrink: 0;
    margin-left: 20px;
  }
}

.empty-container {
  min-height: 300px;
}
</style>
