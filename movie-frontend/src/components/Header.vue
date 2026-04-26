<template>
  <header class="header-container">
    <div class="header-content container flex-between">
      <div class="logo-section flex-center" @click="goHome">
        <el-icon :size="32" color="#409eff"><Film /></el-icon>
        <span class="logo-text">鹰眼电影</span>
      </div>
      
      <div class="search-section">
        <el-autocomplete
          v-model="searchKeyword"
          :fetch-suggestions="querySearch"
          placeholder="搜索电影"
          :trigger-on-focus="false"
          @select="handleSelect"
          size="large"
          clearable
        >
          <template #default="{ item }">
            <div class="search-result-item">
              <img :src="getMoviePosterUrl(item.moviePicture)" class="result-poster" />
              <div class="result-info">
                <div class="result-name">{{ item.movieCnName }}</div>
                <div class="result-score">评分: {{ item.movieScore }}</div>
              </div>
            </div>
          </template>
        </el-autocomplete>
      </div>
      
      <div class="nav-section flex-center">
        <el-menu mode="horizontal" :default-active="activeMenu" :router="true" background-color="transparent" text-color="#606266" active-text-color="#409eff">
          <el-menu-item index="/home">首页</el-menu-item>
          <el-menu-item index="/movies">电影</el-menu-item>
          <el-menu-item index="/cinemas">影院</el-menu-item>
          <el-menu-item index="/rank">榜单</el-menu-item>
          <el-sub-menu v-if="userStore.isAdmin()" index="admin">
            <template #title>管理后台</template>
            <el-menu-item index="/admin/dashboard">数据概览</el-menu-item>
            <el-menu-item index="/admin/movies">电影管理</el-menu-item>
            <el-menu-item index="/admin/schedules">场次管理</el-menu-item>
            <el-menu-item index="/admin/orders">订单管理</el-menu-item>
            <el-menu-item index="/admin/refunds">退票审核</el-menu-item>
          </el-sub-menu>
        </el-menu>
        
        <div v-if="userStore.isLoggedIn()" class="user-dropdown">
          <el-dropdown @command="handleCommand">
            <span class="user-info flex-center">
              <el-avatar :size="32" :src="userInfo?.userHeadImg || defaultAvatar">
                <el-icon><User /></el-icon>
              </el-avatar>
              <span class="user-name">{{ userInfo?.userName }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="orders">我的订单</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        
        <el-button v-else type="primary" @click="goLogin">登录/注册</el-button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Film, User, ArrowDown } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import { getMoviesByName, getMovieList } from '@/api/movie'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const searchKeyword = ref('')
const movieList = ref([])
const activeMenu = ref('/home')
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

const userInfo = computed(() => userStore.getUserInfo())

const getMoviePosterUrl = (url) => {
  if (!url) return 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png'
  if (url.startsWith('http') || url.startsWith('https')) return url
  if (url.startsWith('../')) return '/api' + url.substring(2)
  if (url.startsWith('/')) return '/api' + url
  return '/api/' + url
}

watch(() => route.path, (newPath) => {
  if (newPath.startsWith('/admin')) {
    activeMenu.value = 'admin'
  } else if (newPath.startsWith('/movie')) {
    activeMenu.value = '/movies'
  } else if (newPath.startsWith('/cinemas')) {
    activeMenu.value = '/cinemas'
  } else if (newPath.startsWith('/rank')) {
    activeMenu.value = '/rank'
  } else if (newPath.startsWith('/user')) {
    activeMenu.value = '/home'
  } else {
    activeMenu.value = newPath
  }
}, { immediate: true })

const querySearch = async (queryString, cb) => {
  if (!queryString) {
    cb([])
    return
  }
  try {
    const res = await getMoviesByName(queryString)
    cb(res.data || [])
  } catch (error) {
    cb([])
  }
}

const handleSelect = (item) => {
  router.push(`/movie/${item.movieId}`)
  searchKeyword.value = ''
}

const goHome = () => {
  router.push('/home')
}

const goLogin = () => {
  router.push('/login')
}

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/user/info')
      break
    case 'orders':
      router.push('/user/orders')
      break
    case 'logout':
      handleLogout()
      break
  }
}

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/home')
}

onMounted(async () => {
  try {
    const res = await getMovieList()
    movieList.value = res.data?.onlineList || []
  } catch (error) {
    console.error('加载电影列表失败:', error)
  }
})
</script>

<style scoped lang="scss">
.header-container {
  height: 60px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-content {
  height: 100%;
}

.logo-section {
  cursor: pointer;

  .logo-text {
    font-size: 20px;
    font-weight: bold;
    color: #409eff;
    margin-left: 8px;
  }
}

.search-section {
  width: 400px;

  :deep(.el-input__wrapper) {
    border-radius: 20px;
  }
}

.search-result-item {
  display: flex;
  align-items: center;
  padding: 8px;

  .result-poster {
    width: 40px;
    height: 56px;
    object-fit: cover;
    border-radius: 4px;
  }

  .result-info {
    margin-left: 12px;
    flex: 1;

    .result-name {
      font-size: 14px;
      font-weight: bold;
      color: #303133;
    }

    .result-score {
      font-size: 12px;
      color: #909399;
      margin-top: 4px;
    }
  }
}

.nav-section {
  gap: 20px;

  :deep(.el-menu) {
    border-bottom: none;
  }

  :deep(.el-menu-item) {
    height: 60px;
    line-height: 60px;
    border-bottom: none;
  }

  :deep(.el-sub-menu__title) {
    height: 60px;
    line-height: 60px;
  }
}

.user-info {
  cursor: pointer;
  padding: 0 8px;
  border-radius: 4px;
  transition: background 0.3s ease;

  &:hover {
    background: #f5f7fa;
  }

  .user-name {
    margin: 0 8px;
    color: #606266;
    max-width: 80px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}
</style>
