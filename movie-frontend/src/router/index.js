import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/home/index.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/movie/:id',
    name: 'MovieDetail',
    component: () => import('@/views/movie-detail/index.vue'),
    meta: { title: '电影详情' }
  },
  {
    path: '/movies',
    name: 'MovieList',
    component: () => import('@/views/movies/index.vue'),
    meta: { title: '电影列表' }
  },
  {
    path: '/cinemas',
    name: 'CinemaList',
    component: () => import('@/views/cinemas/index.vue'),
    meta: { title: '影院列表' }
  },
  {
    path: '/rank',
    name: 'Rank',
    component: () => import('@/views/rank/index.vue'),
    meta: { title: '榜单' }
  },
  {
    path: '/buy/:id',
    name: 'BuyTickets',
    component: () => import('@/views/buy-tickets/index.vue'),
    meta: { title: '购票', requiresAuth: true }
  },
  {
    path: '/user/info',
    name: 'UserInfo',
    component: () => import('@/views/user/info.vue'),
    meta: { title: '个人信息', requiresAuth: true }
  },
  {
    path: '/user/orders',
    name: 'UserOrders',
    component: () => import('@/views/user/orders.vue'),
    meta: { title: '我的订单', requiresAuth: true }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/admin/layout.vue'),
    meta: { title: '管理后台', requiresAuth: true, requiresAdmin: true },
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/admin/dashboard.vue'),
        meta: { title: '数据概览' }
      },
      {
        path: 'movies',
        name: 'AdminMovies',
        component: () => import('@/views/admin/movies.vue'),
        meta: { title: '电影管理' }
      },
      {
        path: 'schedules',
        name: 'AdminSchedules',
        component: () => import('@/views/admin/schedules.vue'),
        meta: { title: '场次管理' }
      },
      {
        path: 'orders',
        name: 'AdminOrders',
        component: () => import('@/views/admin/orders.vue'),
        meta: { title: '订单管理' }
      },
      {
        path: 'refunds',
        name: 'AdminRefunds',
        component: () => import('@/views/admin/refunds.vue'),
        meta: { title: '退票审核' }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/users.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'comments',
        name: 'AdminComments',
        component: () => import('@/views/admin/comments.vue'),
        meta: { title: '评论管理' }
      },
      {
        path: 'cinemas',
        name: 'AdminCinemas',
        component: () => import('@/views/admin/cinemas.vue'),
        meta: { title: '影院管理' }
      },
      {
        path: 'halls',
        name: 'AdminHalls',
        component: () => import('@/views/admin/halls.vue'),
        meta: { title: '影厅管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 鹰眼电影` : '鹰眼电影'
  
  const userStore = useUserStore()
  const userInfo = userStore.getUserInfo()
  
  if (to.meta.requiresAuth && !userInfo) {
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }
  
  if (to.meta.requiresAdmin && userInfo?.userRole !== 1) {
    next({ path: '/home' })
    return
  }
  
  next()
})

export default router
