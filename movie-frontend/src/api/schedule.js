import request from '@/utils/request'

export function getScheduleById(scheduleId) {
  return request({
    url: '/schedule/findScheduleById',
    method: 'get',
    params: { scheduleId }
  })
}

export function getScheduleListByState(params) {
  return request({
    url: '/schedule/findAllScheduleByState',
    method: 'get',
    params
  })
}

export function getAllSchedules(params) {
  return request({
    url: '/schedule/findAllSchedule',
    method: 'get',
    params
  })
}

export function getSchedulesByMovieName(params) {
  return request({
    url: '/schedule/findScheduleByMovieName',
    method: 'get',
    params
  })
}

export function getOffSchedulesByMovieName(params) {
  return request({
    url: '/schedule/findOffScheduleByMovieName',
    method: 'get',
    params
  })
}

export function getSchedulesByCinemaAndMovie(cinemaId, movieId) {
  return request({
    url: '/schedule/findScheduleByCinemaAndMovie',
    method: 'get',
    params: { cinemaId, movieId }
  })
}

export function addSchedule(data) {
  return request({
    url: '/schedule/addSchedule',
    method: 'post',
    params: data
  })
}

export function updateSchedulePrice(scheduleId, schedulePrice) {
  return request({
    url: '/schedule/updateSchedulePrice',
    method: 'post',
    params: { scheduleId, schedulePrice }
  })
}

export function offlineSchedule(scheduleId) {
  return request({
    url: '/schedule/offlineSchedule',
    method: 'post',
    params: { scheduleId }
  })
}

export function getCinemasByMovieId(movieId) {
  return request({
    url: '/schedule/findCinemasByMovieId',
    method: 'get',
    params: { movieId }
  })
}
