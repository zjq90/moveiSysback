import request from '@/utils/request'

export function getOrderById(orderId) {
  return request({
    url: '/order/findOrderById',
    method: 'get',
    params: { orderId }
  })
}

export function getOrdersByUserName(params) {
  return request({
    url: '/order/findOrderByUserName',
    method: 'get',
    params
  })
}

export function getRefundOrdersByUser(userName) {
  return request({
    url: '/order/findRefundOrderByUser',
    method: 'get',
    params: { userName }
  })
}

export function getAllOrders(params) {
  return request({
    url: '/order/findAllOrdersPage',
    method: 'get',
    params
  })
}

export function getAllRefundOrders(params) {
  return request({
    url: '/order/findAllRefundOrder',
    method: 'get',
    params
  })
}

export function buyTickets(data) {
  return request({
    url: '/order/buyTickets',
    method: 'post',
    params: data
  })
}

export function applyForRefund(orderId) {
  return request({
    url: '/order/applyForRefund',
    method: 'post',
    params: { orderId }
  })
}

export function agreeForRefund(orderId) {
  return request({
    url: '/order/agreeForRefund',
    method: 'post',
    params: { orderId }
  })
}
