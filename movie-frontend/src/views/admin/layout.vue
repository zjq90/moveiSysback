<template>
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <div class="logo-section">
        <el-icon :size="32" color="#fff"><Film /></el-icon>
        <span class="logo-text">后台管理</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        router
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据概览</span>
        </el-menu-item>
        
        <el-sub-menu index="movie-manage">
          <template #title>
            <el-icon><Film /></el-icon>
            <span>电影管理</span>
          </template>
          <el-menu-item index="/admin/movies">电影列表</el-menu-item>
          <el-menu-item index="/admin/cinemas">影院管理</el-menu-item>
          <el-menu-item index="/admin/halls">影厅管理</el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="/admin/schedules">
          <el-icon><Calendar /></el-icon>
          <span>场次管理</span>
        </el-menu-item>
        
        <el-sub-menu index="order-manage">
          <template #title>
            <el-icon><Ticket /></el-icon>
            <span>订单管理</span>
          </template>
          <el-menu-item index="/admin/orders">订单列表</el-menu-item>
          <el-menu-item index="/admin/refunds">退票审核</el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </aside>
    
    <div class="admin-main">
      <header class="admin-header flex-between">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path">
              {{ item.name }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="header-right flex-center">
          <span class="user-name">{{ userInfo?.userName }}</span>
          <el-dropdown @command="handleCommand">
            <el-avatar :size="32" :src="userInfo?.userHeadImg || defaultAvatar">
              <el-icon><User /></el-icon>
            </el-avatar>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>
      
      <main class="admin-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Film, DataAnalysis, Calendar, Ticket, User } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

const userInfo = computed(() => userStore.getUserInfo())

const breadcrumbMap = {
  '/admin/dashboard': [{ path: '/admin/dashboard', name: '数据概览' }],
  '/admin/movies': [
    { path: '/admin/movies', name: '电影管理' },
    { path: '/admin/movies', name: '电影列表' }
  ],
  '/admin/cinemas': [
    { path: '/admin/movies', name: '电影管理' },
    { path: '/admin/cinemas', name: '影院管理' }
  ],
  '/admin/halls': [
    { path: '/admin/movies', name: '电影管理' },
    { path: '/admin/halls', name: '影厅管理' }
  ],
  '/admin/schedules': [{ path: '/admin/schedules', name: '场次管理' }],
  '/admin/orders': [
    { path: '/admin/orders', name: '订单管理' },
    { path: '/admin/orders', name: '订单列表' }
  ],
  '/admin/refunds': [
    { path: '/admin/orders', name: '订单管理' },
    { path: '/admin/refunds', name: '退票审核' }
  ],
  '/admin/users': [{ path: '/admin/users', name: '用户管理' }]
}

const breadcrumbs = computed(() => {
  return breadcrumbMap[route.path] || [{ path: '/admin/dashboard', name: '数据概览' }]
})

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/user/info')
      break
    case 'logout':
      userStore.logout()
      ElMessage.success('已退出登录')
      router.push('/login')
      break
  }
}
</script>

<style scoped lang="scss">
.admin-layout {
  display: flex;
  min-height: 100vh;
  background: #f5f5f5;
}

.admin-sidebar {
  width: 200px;
  background: #304156;
  flex-shrink: 0;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  overflow-y: auto;

  .logo-section {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    border-bottom: 1px solid #1f2d3d;

    .logo-text {
      font-size: 18px;
      font-weight: bold;
      color: #fff;
    }
  }

  :deep(.el-menu) {
    border-right: none;
  }

  :deep(.el-menu-item),
  :deep(.el-sub-menu__title) {
    height: 50px;
    line-height: 50px;
  }

  :deep(.el-menu-item:hover),
  :deep(.el-sub-menu__title:hover) {
    background-color: #263445 !important;
  }

  :deep(.el-menu-item.is-active) {
    background-color: #1f2d3d !important;
  }
}

.admin-main {
  flex: 1;
  margin-left: 200px;
  display: flex;
  flex-direction: column;
}

.admin-header {
  height: 60px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: sticky;
  top: 0;
  z-index: 100;

  .header-right {
    gap: 16px;

    .user-name {
      font-size: 14px;
      color: #606266;
    }
  }
}

.admin-content {
  flex: 1;
  padding: 20px;
}
</style>
