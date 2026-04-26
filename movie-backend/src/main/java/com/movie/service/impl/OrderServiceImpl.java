package com.movie.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.movie.entity.Order;
import com.movie.mapper.OrderMapper;
import com.movie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单服务实现类
 * 
 * @author Movie Team
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 根据订单ID查询订单
     * 
     * @param orderId 订单ID
     * @return 订单对象
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Order findOrderById(String orderId) {
        return orderMapper.findOrderById(orderId);
    }

    /**
     * 查询所有订单
     * 
     * @return 订单列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Order> findAllOrders() {
        return orderMapper.findAllOrders();
    }

    /**
     * 分页查询所有订单
     * 支持按订单ID搜索
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param keyword 搜索关键词（订单ID）
     * @return 分页信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageInfo<Order> findAllOrdersBySplitPage(Integer page, Integer limit, String keyword) {
        PageHelper.startPage(page, limit);
        List<Order> list = orderMapper.findAllOrders();
        return new PageInfo<>(list);
    }

    /**
     * 根据场次ID查询订单
     * 用于计算场次收入
     * 
     * @param scheduleId 场次ID
     * @return 订单列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Order> findOrdersByScheduleId(Long scheduleId) {
        return orderMapper.findOrdersByScheduleId(scheduleId);
    }

    /**
     * 根据状态分页查询订单
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param orderState 订单状态
     * @return 分页信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageInfo<Order> findOrdersByState(Integer page, Integer limit, Integer orderState) {
        PageHelper.startPage(page, limit);
        List<Order> list = orderMapper.findOrdersByState(orderState);
        return new PageInfo<>(list);
    }

    /**
     * 根据用户名分页查询订单
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param userName 用户名
     * @return 分页信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageInfo<Order> findOrdersByUserName(Integer page, Integer limit, String userName) {
        PageHelper.startPage(page, limit);
        List<Order> list = orderMapper.findOrdersByUserName(userName);
        return new PageInfo<>(list);
    }

    /**
     * 根据用户名查询退票中的订单
     * 
     * @param userName 用户名
     * @return 订单列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Order> findRefundOrderByUserName(String userName) {
        return orderMapper.findRefundOrderByUserName(userName);
    }

    /**
     * 新增订单
     * 购票时调用
     * 
     * @param order 订单对象
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addOrder(Order order) {
        return orderMapper.addOrder(order);
    }

    /**
     * 更新订单状态为退票中
     * 用户申请退票时调用
     * 
     * @param orderId 订单ID
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateOrderStateToRefund(String orderId) {
        return orderMapper.updateOrderStateToRefund(orderId);
    }

    /**
     * 更新订单状态为退票成功
     * 管理员同意退票时调用
     * 
     * @param orderId 订单ID
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateOrderStateToRefunded(String orderId) {
        return orderMapper.updateOrderStateToRefunded(orderId);
    }
}
