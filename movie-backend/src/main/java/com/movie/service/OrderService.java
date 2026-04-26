package com.movie.service;

import com.github.pagehelper.PageInfo;
import com.movie.entity.Order;

import java.util.List;

/**
 * 订单服务接口
 * 
 * @author Movie Team
 */
public interface OrderService {

    /**
     * 根据订单ID查询订单
     * 
     * @param orderId 订单ID
     * @return 订单对象
     */
    Order findOrderById(String orderId);

    /**
     * 查询所有订单
     * 
     * @return 订单列表
     */
    List<Order> findAllOrders();

    /**
     * 分页查询所有订单
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param keyword 搜索关键词
     * @return 分页信息
     */
    PageInfo<Order> findAllOrdersBySplitPage(Integer page, Integer limit, String keyword);

    /**
     * 根据场次ID查询订单
     * 
     * @param scheduleId 场次ID
     * @return 订单列表
     */
    List<Order> findOrdersByScheduleId(Long scheduleId);

    /**
     * 根据状态分页查询订单
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param orderState 订单状态
     * @return 分页信息
     */
    PageInfo<Order> findOrdersByState(Integer page, Integer limit, Integer orderState);

    /**
     * 根据用户名分页查询订单
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param userName 用户名
     * @return 分页信息
     */
    PageInfo<Order> findOrdersByUserName(Integer page, Integer limit, String userName);

    /**
     * 根据用户名查询退票中的订单
     * 
     * @param userName 用户名
     * @return 订单列表
     */
    List<Order> findRefundOrderByUserName(String userName);

    /**
     * 新增订单
     * 
     * @param order 订单对象
     * @return 影响行数
     */
    int addOrder(Order order);

    /**
     * 更新订单状态为退票中
     * 
     * @param orderId 订单ID
     * @return 影响行数
     */
    int updateOrderStateToRefund(String orderId);

    /**
     * 更新订单状态为退票成功
     * 
     * @param orderId 订单ID
     * @return 影响行数
     */
    int updateOrderStateToRefunded(String orderId);
}
