import request from '@/utils/request'

export function getHallsByCinemaId(cinemaId) {
  return request({
    url: '/hall/findHallByCinemaId',
    method: 'get',
    params: { cinemaId }
  })
}

export function getHallById(hallId) {
  return request({
    url: '/hall/findHallById',
    method: 'get',
    params: { hallId }
  })
}

export function addHall(data) {
  return request({
    url: '/hall/addHall',
    method: 'post',
    data
  })
}

export function updateHall(data) {
  return request({
    url: '/hall/updateHall',
    method: 'post',
    data
  })
}

export function deleteHall(hallId) {
  return request({
    url: '/hall/deleteHall',
    method: 'post',
    params: { hallId }
  })
}

export function getHallByCinemaAndHallName(cinemaName, hallName) {
  return request({
    url: '/hall/findHallByCinemaAndHallName',
    method: 'get',
    params: { cinemaName, hallName }
  })
}
