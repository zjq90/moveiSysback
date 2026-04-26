import request from '@/utils/request'

export function getAllComments(params) {
  return request({
    url: '/comment/findAllCommentsPage',
    method: 'get',
    params
  })
}

export function getCommentsByUserName(params) {
  return request({
    url: '/comment/findCommentsByUserName',
    method: 'get',
    params
  })
}

export function addComment(data) {
  return request({
    url: '/comment/addCommentByUser',
    method: 'post',
    params: data
  })
}

export function updateComment(commentId, commentContent) {
  return request({
    url: '/comment/updateComment',
    method: 'post',
    params: { commentId, commentContent }
  })
}

export function deleteComment(commentId) {
  return request({
    url: '/comment/deleteComment',
    method: 'post',
    params: { commentId }
  })
}

export function getCommentById(commentId) {
  return request({
    url: '/comment/findCommentById',
    method: 'get',
    params: { commentId }
  })
}
