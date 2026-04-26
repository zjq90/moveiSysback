<template>
  <div class="buy-tickets-container">
    <HeaderComponent />
    
    <div class="main-content">
      <div class="content-wrapper container">
        <el-steps :active="currentStep" align-center class="mb-20">
          <el-step title="选择影院" />
          <el-step title="选择场次" />
          <el-step title="确认订单" />
        </el-steps>
        
        <div v-if="currentStep === 0" class="step-content card p-20">
          <h3 class="section-title">选择影院</h3>
          
          <div class="movie-info-bar flex-between mb-20">
            <div class="movie-info">
              <img :src="getMoviePosterUrl(movieInfo.moviePicture)" class="mini-poster" />
              <div class="movie-text">
                <h4>{{ movieInfo.movieCnName }}</h4>
                <p>{{ movieInfo.movieType }} | {{ movieInfo.movieDuration }}</p>
              </div>
            </div>
          </div>
          
          <div class="cinema-list">
            <div 
              v-for="cinema in cinemas" 
              :key="cinema.cinemaId" 
              class="cinema-item"
              :class="{ active: selectedCinema?.cinemaId === cinema.cinemaId }"
              @click="selectCinema(cinema)"
            >
              <div class="cinema-info">
                <h4 class="cinema-name">{{ cinema.cinemaName }}</h4>
                <p class="cinema-address">{{ cinema.cinemaAddress }}</p>
                <p class="cinema-phone">{{ cinema.cinemaPhone }}</p>
              </div>
              <div class="cinema-price">
                <span class="price-label">起</span>
                <span class="price-value">¥{{ cinema.lowestPrice || 0 }}</span>
              </div>
            </div>
            
            <el-empty v-if="cinemas.length === 0" description="暂无影院排片" />
          </div>
          
          <div class="step-actions">
            <el-button type="primary" :disabled="!selectedCinema" @click="goToNextStep">
              下一步
            </el-button>
          </div>
        </div>
        
        <div v-if="currentStep === 1" class="step-content card p-20">
          <h3 class="section-title">选择场次</h3>
          
          <div class="cinema-info-bar mb-20">
            <el-button text @click="goToPrevStep">
              <el-icon><ArrowLeft /></el-icon> 返回影院列表
            </el-button>
            <h4 class="cinema-name">{{ selectedCinema?.cinemaName }}</h4>
          </div>
          
          <div class="date-selector mb-20">
            <div 
              v-for="(date, index) in dateList" 
              :key="index"
              class="date-item"
              :class="{ active: selectedDate === date.value }"
              @click="selectDate(date.value)"
            >
              <span class="date-day">{{ date.day }}</span>
              <span class="date-text">{{ date.text }}</span>
            </div>
          </div>
          
          <div class="schedule-list">
            <div class="schedule-row" v-for="schedule in filteredSchedules" :key="schedule.scheduleId">
              <div class="schedule-time">
                <span class="start-time">{{ formatTime(schedule.scheduleStartTime) }}</span>
                <span class="end-time">{{ formatEndTime(schedule.scheduleStartTime, schedule.movieDuration) }}</span>
              </div>
              <div class="schedule-hall">
                <span>{{ schedule.hallName }}</span>
              </div>
              <div class="schedule-price">
                <span class="price">¥{{ schedule.schedulePrice }}</span>
              </div>
              <div class="schedule-seats">
                <span class="seat-count">{{ schedule.scheduleRemain }} 座</span>
              </div>
              <div class="schedule-action">
                <el-button 
                  type="primary" 
                  size="small"
                  :disabled="schedule.scheduleRemain === 0"
                  @click="selectSchedule(schedule)"
                >
                  选座购票
                </el-button>
              </div>
            </div>
            
            <el-empty v-if="filteredSchedules.length === 0" description="该日期暂无场次" />
          </div>
        </div>
        
        <div v-if="currentStep === 2" class="step-content card p-20">
          <h3 class="section-title">确认订单</h3>
          
          <el-button text class="mb-20" @click="goToPrevStep">
            <el-icon><ArrowLeft /></el-icon> 返回场次列表
          </el-button>
          
          <div class="order-summary">
            <div class="summary-row">
              <span class="summary-label">电影:</span>
              <span class="summary-value">{{ movieInfo.movieCnName }}</span>
            </div>
            <div class="summary-row">
              <span class="summary-label">影院:</span>
              <span class="summary-value">{{ selectedCinema?.cinemaName }}</span>
            </div>
            <div class="summary-row">
              <span class="summary-label">场次:</span>
              <span class="summary-value">
                {{ formatTime(selectedSchedule?.scheduleStartTime) }} - {{ formatEndTime(selectedSchedule?.scheduleStartTime, movieInfo.movieDuration) }}
              </span>
            </div>
            <div class="summary-row">
              <span class="summary-label">放映厅:</span>
              <span class="summary-value">{{ selectedSchedule?.hallName }}</span>
            </div>
            <div class="summary-row">
              <span class="summary-label">票价:</span>
              <span class="summary-value price">¥{{ selectedSchedule?.schedulePrice }}</span>
            </div>
          </div>
          
          <div class="seat-selection" v-if="selectedSchedule">
            <h4 class="seat-title">选择座位</h4>
            
            <div class="seat-legend mb-20">
              <div class="legend-item">
                <div class="seat-box available"></div>
                <span>可选</span>
              </div>
              <div class="legend-item">
                <div class="seat-box selected"></div>
                <span>已选</span>
              </div>
              <div class="legend-item">
                <div class="seat-box occupied"></div>
                <span>已售</span>
              </div>
            </div>
            
            <div class="screen">银幕</div>
            
            <div class="seat-grid" v-if="seatLayout.length > 0">
              <div v-for="(row, rowIndex) in seatLayout" :key="rowIndex" class="seat-row">
                <span class="row-label">{{ rowIndex + 1 }}排</span>
                <div v-for="(seat, colIndex) in row" :key="colIndex" class="seat-wrapper">
                  <div 
                    class="seat"
                    :class="{ 
                      'available': !seat.occupied, 
                      'occupied': seat.occupied,
                      'active': selectedSeats.includes(`${rowIndex}-${colIndex}`)
                    }"
                    @click="toggleSeat(rowIndex, colIndex, seat.occupied)"
                  >
                    {{ colIndex + 1 }}
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="order-actions">
            <div class="order-total">
              <span class="total-label">共{{ selectedSeats.length }}张票，合计:</span>
              <span class="total-price">¥{{ totalPrice }}</span>
            </div>
            <el-button 
              type="primary" 
              size="large"
              :disabled="selectedSeats.length === 0"
              :loading="submitting"
              @click="submitOrder"
            >
              确认购买
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import HeaderComponent from '@/components/Header.vue'
import { getMovieById } from '@/api/movie'
import { getCinemasByMovieId, getSchedulesByCinemaAndMovie } from '@/api/schedule'
import { buyTickets } from '@/api/order'
import { useUserStore } from '@/store/user'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const currentStep = ref(0)
const loading = ref(false)
const submitting = ref(false)

const movieInfo = ref({})
const cinemas = ref([])
const schedules = ref([])

const selectedCinema = ref(null)
const selectedSchedule = ref(null)
const selectedDate = ref('')

const selectedSeats = ref([])
const seatLayout = ref([])

const getMoviePosterUrl = (url) => {
  if (!url) return 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png'
  if (url.startsWith('http') || url.startsWith('https')) return url
  if (url.startsWith('../')) return '/api' + url.substring(2)
  if (url.startsWith('/')) return '/api' + url
  return '/api/' + url
}

const dateList = computed(() => {
  const dates = []
  for (let i = 0; i < 7; i++) {
    const date = dayjs().add(i, 'day')
    dates.push({
      value: date.format('YYYY-MM-DD'),
      day: i === 0 ? '今天' : i === 1 ? '明天' : date.format('周') + ['日', '一', '二', '三', '四', '五', '六'][date.day()],
      text: date.format('MM月DD日')
    })
  }
  return dates
})

const filteredSchedules = computed(() => {
  if (!selectedDate.value) return schedules.value
  return schedules.value.filter(s => {
    const scheduleDate = dayjs(s.scheduleStartTime).format('YYYY-MM-DD')
    return scheduleDate === selectedDate.value
  })
})

const totalPrice = computed(() => {
  if (!selectedSchedule.value) return 0
  return selectedSeats.value.length * selectedSchedule.value.schedulePrice
})

const movieId = computed(() => route.params.id)

const formatTime = (time) => {
  if (!time) return ''
  return dayjs(time).format('HH:mm')
}

const formatEndTime = (startTime, duration) => {
  if (!startTime) return ''
  const minutes = parseInt(duration) || 0
  return dayjs(startTime).add(minutes, 'minute').format('HH:mm')
}

const loadMovieDetail = async () => {
  if (!movieId.value) return
  
  try {
    const res = await getMovieById(movieId.value)
    movieInfo.value = res.data || {}
  } catch (error) {
    console.error('加载电影详情失败:', error)
  }
}

const loadCinemas = async () => {
  if (!movieId.value) return
  
  try {
    const res = await getCinemasByMovieId(movieId.value)
    cinemas.value = res.data || []
    
    if (cinemas.value.length > 0) {
      cinemas.value.forEach(cinema => {
        cinema.lowestPrice = cinema.minPrice || 0
      })
    }
  } catch (error) {
    console.error('加载影院列表失败:', error)
  }
}

const selectCinema = (cinema) => {
  selectedCinema.value = cinema
}

const selectDate = (date) => {
  selectedDate.value = date
}

const selectSchedule = (schedule) => {
  selectedSchedule.value = schedule
  generateSeatLayout()
  currentStep.value = 2
}

const generateSeatLayout = () => {
  const rows = 8
  const cols = 10
  const layout = []
  
  for (let i = 0; i < rows; i++) {
    const row = []
    for (let j = 0; j < cols; j++) {
      row.push({
        occupied: Math.random() > 0.8
      })
    }
    layout.push(row)
  }
  
  seatLayout.value = layout
  selectedSeats.value = []
}

const toggleSeat = (row, col, occupied) => {
  if (occupied) {
    ElMessage.warning('该座位已售出')
    return
  }
  
  const seatKey = `${row}-${col}`
  const index = selectedSeats.value.indexOf(seatKey)
  
  if (index > -1) {
    selectedSeats.value.splice(index, 1)
  } else {
    if (selectedSeats.value.length >= 4) {
      ElMessage.warning('一次最多购买4张票')
      return
    }
    selectedSeats.value.push(seatKey)
  }
}

const goToNextStep = () => {
  if (currentStep.value === 0 && selectedCinema.value) {
    loadSchedules()
    currentStep.value++
    selectedDate.value = dateList.value[0].value
  }
}

const goToPrevStep = () => {
  if (currentStep.value > 0) {
    currentStep.value--
  }
}

const loadSchedules = async () => {
  if (!selectedCinema.value || !movieId.value) return
  
  loading.value = true
  try {
    const res = await getSchedulesByCinemaAndMovie(selectedCinema.value.cinemaId, movieId.value)
    schedules.value = res.data || []
  } catch (error) {
    console.error('加载场次失败:', error)
  } finally {
    loading.value = false
  }
}

const submitOrder = async () => {
  if (!userStore.isLoggedIn()) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  if (selectedSeats.value.length === 0) {
    ElMessage.warning('请选择座位')
    return
  }
  
  submitting.value = true
  try {
    const userInfo = userStore.getUserInfo()
    const seatNames = selectedSeats.value.map(seat => {
      const [row, col] = seat.split('-')
      return `${parseInt(row) + 1}排${parseInt(col) + 1}座`
    }).join(',')
    
    const res = await buyTickets({
      scheduleId: selectedSchedule.value.scheduleId,
      userId: userInfo.userId,
      userName: userInfo.userName,
      count: selectedSeats.value.length,
      seatName: seatNames
    })
    
    ElMessage.success('购票成功！')
    router.push(`/user/orders`)
  } catch (error) {
    console.error('购票失败:', error)
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadMovieDetail()
  loadCinemas()
  selectedDate.value = dateList.value[0].value
})
</script>

<style scoped lang="scss">
.buy-tickets-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.main-content {
  padding: 20px 0 40px;
}

.content-wrapper {
  padding: 0 20px;
}

.step-content {
  border-radius: 8px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  padding-left: 12px;
  border-left: 4px solid #409eff;
  margin-bottom: 20px;
}

.movie-info-bar {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;

  .movie-info {
    display: flex;
    gap: 16px;
  }

  .mini-poster {
    width: 60px;
    height: 80px;
    object-fit: cover;
    border-radius: 4px;
  }

  .movie-text {
    h4 {
      font-size: 16px;
      font-weight: bold;
      color: #303133;
      margin-bottom: 8px;
    }

    p {
      font-size: 13px;
      color: #909399;
    }
  }
}

.cinema-list {
  .cinema-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    margin-bottom: 12px;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      border-color: #409eff;
    }

    &.active {
      border-color: #409eff;
      background: #ecf5ff;
    }

    .cinema-info {
      flex: 1;

      .cinema-name {
        font-size: 16px;
        font-weight: bold;
        color: #303133;
        margin-bottom: 6px;
      }

      .cinema-address {
        font-size: 13px;
        color: #909399;
        margin-bottom: 4px;
      }

      .cinema-phone {
        font-size: 13px;
        color: #606266;
      }
    }

    .cinema-price {
      text-align: right;

      .price-label {
        font-size: 12px;
        color: #909399;
      }

      .price-value {
        font-size: 20px;
        font-weight: bold;
        color: #ff6b6b;
      }
    }
  }
}

.cinema-info-bar {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;

  .cinema-name {
    font-size: 18px;
    font-weight: bold;
    color: #303133;
    margin: 0;
  }
}

.date-selector {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;

  .date-item {
    padding: 12px 24px;
    background: #fff;
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    text-align: center;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      border-color: #409eff;
    }

    &.active {
      border-color: #409eff;
      background: #ecf5ff;
      color: #409eff;
    }

    .date-day {
      display: block;
      font-size: 14px;
      font-weight: bold;
      margin-bottom: 4px;
    }

    .date-text {
      display: block;
      font-size: 12px;
      color: #909399;
    }
  }
}

.schedule-list {
  .schedule-row {
    display: flex;
    align-items: center;
    padding: 16px;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    > div {
      flex: 1;
      text-align: center;
    }

    .schedule-time {
      flex: 1.5;
      text-align: left;

      .start-time {
        display: block;
        font-size: 18px;
        font-weight: bold;
        color: #303133;
      }

      .end-time {
        display: block;
        font-size: 12px;
        color: #909399;
      }
    }

    .schedule-hall {
      font-size: 14px;
      color: #606266;
    }

    .schedule-price {
      .price {
        font-size: 16px;
        font-weight: bold;
        color: #ff6b6b;
      }
    }

    .schedule-seats {
      .seat-count {
        font-size: 14px;
        color: #606266;
      }
    }

    .schedule-action {
      text-align: right;
    }
  }
}

.order-summary {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 24px;

  .summary-row {
    display: flex;
    margin-bottom: 12px;

    &:last-child {
      margin-bottom: 0;
    }

    .summary-label {
      width: 80px;
      color: #909399;
      font-size: 14px;
    }

    .summary-value {
      flex: 1;
      color: #303133;
      font-size: 14px;

      &.price {
        font-size: 18px;
        font-weight: bold;
        color: #ff6b6b;
      }
    }
  }
}

.seat-selection {
  margin-bottom: 24px;

  .seat-title {
    font-size: 16px;
    font-weight: bold;
    color: #303133;
    margin-bottom: 16px;
  }

  .seat-legend {
    display: flex;
    justify-content: center;
    gap: 32px;

    .legend-item {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 12px;
      color: #606266;

      .seat-box {
        width: 20px;
        height: 20px;
        border-radius: 4px;

        &.available {
          background: #e4e7ed;
          border: 1px solid #dcdfe6;
        }

        &.selected {
          background: #409eff;
          border: 1px solid #409eff;
        }

        &.occupied {
          background: #c0c4cc;
          border: 1px solid #c0c4cc;
        }
      }
    }
  }

  .screen {
    width: 60%;
    height: 30px;
    background: linear-gradient(180deg, #e4e7ed 0%, transparent 100%);
    margin: 20px auto 40px;
    text-align: center;
    line-height: 30px;
    font-size: 12px;
    color: #909399;
    border-radius: 0 0 50% 50%;
  }

  .seat-grid {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;

    .seat-row {
      display: flex;
      align-items: center;
      gap: 8px;

      .row-label {
        width: 40px;
        text-align: right;
        font-size: 12px;
        color: #909399;
      }

      .seat-wrapper {
        .seat {
          width: 24px;
          height: 24px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 10px;
          border-radius: 4px 4px 8px 8px;
          cursor: pointer;
          transition: all 0.2s ease;

          &.available {
            background: #e4e7ed;
            border: 1px solid #dcdfe6;
            color: #606266;

            &:hover {
              background: #409eff;
              border-color: #409eff;
              color: #fff;
            }
          }

          &.occupied {
            background: #c0c4cc;
            border: 1px solid #c0c4cc;
            color: #909399;
            cursor: not-allowed;
          }

          &.active {
            background: #409eff;
            border: 1px solid #409eff;
            color: #fff;
          }
        }
      }
    }
  }
}

.step-actions,
.order-actions {
  margin-top: 24px;
  text-align: right;

  .order-total {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    margin-bottom: 16px;

    .total-label {
      font-size: 14px;
      color: #606266;
    }

    .total-price {
      font-size: 24px;
      font-weight: bold;
      color: #ff6b6b;
      margin-left: 8px;
    }
  }
}
</style>
