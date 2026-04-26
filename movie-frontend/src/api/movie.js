import request from '@/utils/request'

export function getMovieList() {
  return request({
    url: '/movie/findAllMovies',
    method: 'get'
  })
}

export function getMovieById(movieId) {
  return request({
    url: '/movie/findMovieById',
    method: 'get',
    params: { movieId }
  })
}

export function getMoviesByName(name) {
  return request({
    url: '/movie/findMoviesByName',
    method: 'get',
    params: { name }
  })
}

export function getMoviesByType(type) {
  return request({
    url: '/movie/findMoviesByType',
    method: 'get',
    params: { type }
  })
}

export function sortMovies(order) {
  return request({
    url: '/movie/sortAllMovies',
    method: 'get',
    params: { order }
  })
}

export function addMovie(formData) {
  return request({
    url: '/movie/addMovie',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: formData
  })
}

export function updateMovie(formData) {
  return request({
    url: '/movie/updateMovie',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: formData
  })
}

export function deleteMovie(movieId) {
  return request({
    url: '/movie/deleteMovie',
    method: 'post',
    params: { movieId }
  })
}

export function getBoxOfficeMovies() {
  return request({
    url: '/movie/findBoxOfficeMovies',
    method: 'get'
  })
}
