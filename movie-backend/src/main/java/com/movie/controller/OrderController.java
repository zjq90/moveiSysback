package com.movie.controller;

import com.github.pagehelper.PageInfo;
import com.movie.common.Result;
import com.movie.entity.Movie;
import com.movie.entity.Order;
import com.movie.entity.Schedule;
import com.movie.entity.User;
import com.movie.service.MovieService;
import com.movie.service.OrderService;
import com.movie.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单管理控制器
 * 处理订单相关的HTTP请求：购票、退票、订单查询等
 * 
 * @author Movie Team
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MovieService movieService;

    /**
     * 根据订单ID查询订单
     * 
     * @param orderId 订单ID
     * @return 订单信息
     */
    @GetMapping("/findOrderById")
    public Result<List<Order>> findOrderById(@RequestParam("orderId") String orderId) {
        Order order = orderService.findOrderById(orderId);
        List<Order> list = new ArrayList<>();
        if (order != null) {
            list.add(order);
        }
        return Result.success((long) list.size(), list);
    }

    /**
     * 根据用户名查询订单（分页）
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param userName 用户名
     * @return 订单分页列表
     */
    @GetMapping("/findOrderByUserName")
    public Result<List<Order>> findOrderByUserName(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                     @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                                     @RequestParam("userName") String userName) {
        PageInfo<Order> info = orderService.findOrdersByUserName(page, limit, userName);
        return Result.success(info.getTotal(), info.getList());
    }

    /**
     * 根据用户名查询退票中的订单
     * 
     * @param userName 用户名
     * @return 退票订单列表
     */
    @GetMapping("/findRefundOrderByUser")
    public Result<List<Order>> findRefundOrderByUser(@RequestParam("userName") String userName) {
        List<Order> list = orderService.findRefundOrderByUserName(userName);
        return Result.success((long) list.size(), list);
    }

    /**
     * 查询所有订单
     * 
     * @return 订单列表
     */
    @GetMapping("/findAllOrders")
    public Result<List<Order>> findAllOrders() {
        List<Order> list = orderService.findAllOrders();
        return Result.success((long) list.size(), list);
    }

    /**
     * 分页查询所有订单
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param keyword 搜索关键词
     * @return 订单分页列表
     */
    @GetMapping("/findAllOrdersPage")
    public Result<List<Order>> findAllOrdersPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                                   @RequestParam(value = "keyword", required = false) String keyword) {
        PageInfo<Order> info = orderService.findAllOrdersBySplitPage(page, limit, keyword);
        return Result.success(info.getTotal(), info.getList());
    }

    /**
     * 分页查询退票中的订单
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @return 退票订单分页列表
     */
    @GetMapping("/findAllRefundOrder")
    public Result<List<Order>> findAllRefundOrder(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        PageInfo<Order> info = orderService.findOrdersByState(page, limit, 0);
        return Result.success(info.getTotal(), info.getList());
    }

    /**
     * 购票
     * 
     * @param scheduleId 场次ID
     * @param position 座位数组
     * @param price 总价
     * @param request HTTP请求对象
     * @return 购票结果
     */
    @PostMapping("/buyTickets")
    public Result<String> buyTickets(@RequestParam("scheduleId") Long scheduleId,
                                       @RequestParam("position") String[] position,
                                       @RequestParam("price") int price,
                                       HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return Result.error("您未登录，登录之后才可购票");
        }
        
        int successCount = 0;
        int orderPrice = price / position.length;
        
        String userIdStr = String.format("%04d", user.getUserId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String datePrefix = dateFormat.format(new Date());
        
        for (String pos : position) {
            String orderId = generateOrderId(datePrefix, userIdStr, pos);
            
            Order order = new Order();
            order.setOrderId(orderId);
            order.setOrderPosition(pos);
            order.setScheduleId(scheduleId);
            order.setUserId(user.getUserId());
            order.setOrderPrice(orderPrice);
            order.setOrderTime(new Date());
            
            int result = orderService.addOrder(order);
            if (result > 0) {
                scheduleService.delScheduleRemain(scheduleId);
                successCount++;
            }
        }
        
        if (successCount == position.length) {
            float boxOffice = (float) price / 10000;
            Schedule schedule = scheduleService.findScheduleById(scheduleId);
            if (schedule != null) {
                movieService.changeMovieBoxOffice(boxOffice, schedule.getMovieId());
            }
            return Result.success("购票成功");
        }
        
        return Result.error("购票失败");
    }

    /**
     * 生成订单ID
     * 格式：日期(8位) + 用户ID(4位补零) + 座位信息
     * 
     * @param datePrefix 日期前缀
     * @param userIdStr 用户ID字符串
     * @param position 座位信息
     * @return 订单ID
     */
    private String generateOrderId(String datePrefix, String userIdStr, String position) {
        StringBuilder indexBuilder = new StringBuilder();
        
        if (position.length() == 4) {
            indexBuilder.append("0");
            indexBuilder.append(position.replace("排", "0"));
            indexBuilder.deleteCharAt(indexBuilder.length() - 1);
        } else if (position.length() == 5) {
            if (position.charAt(2) >= '0' && position.charAt(2) <= '9') {
                indexBuilder.append("0");
                indexBuilder.append(position.replace("排", ""));
                indexBuilder.deleteCharAt(indexBuilder.length() - 1);
            } else {
                indexBuilder.append(position.replace("排", "0"));
                indexBuilder.deleteCharAt(indexBuilder.length() - 1);
            }
        } else if (position.length() == 6) {
            indexBuilder.append(position.replace("排", ""));
            indexBuilder.deleteCharAt(indexBuilder.length() - 1);
        }
        
        return datePrefix + userIdStr + indexBuilder.toString();
    }

    /**
     * 申请退票
     * 
     * @param orderId 订单ID
     * @return 申请结果
     */
    @PostMapping("/applyForRefund")
    public Result<String> applyForRefund(@RequestParam("orderId") String orderId) {
        Integer result = orderService.updateOrderStateToRefund(orderId);
        if (result > 0) {
            return Result.success("退票申请已发送");
        }
        return Result.error("操作失败");
    }

    /**
     * 同意退票
     * 
     * @param orderId 订单ID
     * @return 退票结果
     */
    @PostMapping("/agreeForRefund")
    public Result<String> agreeForRefund(@RequestParam("orderId") String orderId) {
        Integer result = orderService.updateOrderStateToRefunded(orderId);
        if (result > 0) {
            Order order = orderService.findOrderById(orderId);
            if (order != null) {
                int price = order.getOrderPrice();
                float refundAmount = (float) price / 10000;
                
                Schedule schedule = scheduleService.findScheduleById(order.getScheduleId());
                if (schedule != null) {
                    movieService.changeMovieBoxOffice(-refundAmount, schedule.getMovieId());
                    scheduleService.addScheduleRemain(order.getScheduleId());
                }
            }
            return Result.success("退票成功");
        }
        return Result.error("退票失败");
    }
}
