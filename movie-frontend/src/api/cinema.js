import request from '@/utils/request'

export function getAllCinemas() {
  return request({
    url: '/cinema/findAllCinemas',
    method: 'get'
  })
}

export function getCinemaById(cinemaId) {
  return request({
    url: '/cinema/findCinemaById',
    method: 'get',
    params: { cinemaId }
  })
}

export function addCinema(data) {
  return request({
    url: '/cinema/addCinema',
    method: 'post',
    data
  })
}

export function updateCinema(data) {
  return request({
    url: '/cinema/updateCinema',
    method: 'post',
    data
  })
}

export function deleteCinema(cinemaId) {
  return request({
    url: '/cinema/deleteCinema',
    method: 'post',
    params: { cinemaId }
  })
}

export function getCinemasByMovieId(movieId) {
  return request({
    url: '/cinema/findCinemasByMovieId',
    method: 'get',
    params: { movieId }
  })
}

export function getCinemasWithHalls() {
  return request({
    url: '/cinema/findCinemasWithHalls',
    method: 'get'
  })
}
