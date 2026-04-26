package com.movie.mapper;

import com.movie.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单数据访问接口
 * 
 * @author Movie Team
 */
@Mapper
public interface OrderMapper {

    /**
     * 根据订单ID查询订单
     * 
     * @param orderId 订单ID
     * @return 订单对象
     */
    Order findOrderById(@Param("orderId") String orderId);

    /**
     * 查询所有订单
     * 
     * @return 订单列表
     */
    List<Order> findAllOrders();

    /**
     * 根据场次ID查询订单
     * 
     * @param scheduleId 场次ID
     * @return 订单列表
     */
    List<Order> findOrdersByScheduleId(@Param("scheduleId") Long scheduleId);

    /**
     * 根据状态查询订单
     * 
     * @param orderState 订单状态
     * @return 订单列表
     */
    List<Order> findOrdersByState(@Param("orderState") Integer orderState);

    /**
     * 根据用户名查询订单
     * 
     * @param userName 用户名
     * @return 订单列表
     */
    List<Order> findOrdersByUserName(@Param("userName") String userName);

    /**
     * 根据用户名查询退票中的订单
     * 
     * @param userName 用户名
     * @return 订单列表
     */
    List<Order> findRefundOrderByUserName(@Param("userName") String userName);

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
    int updateOrderStateToRefund(@Param("orderId") String orderId);

    /**
     * 更新订单状态为退票成功
     * 
     * @param orderId 订单ID
     * @return 影响行数
     */
    int updateOrderStateToRefunded(@Param("orderId") String orderId);
}
