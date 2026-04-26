package com.movie.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.movie.entity.Order;
import com.movie.entity.Schedule;
import com.movie.mapper.OrderMapper;
import com.movie.mapper.ScheduleMapper;
import com.movie.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 场次服务实现类
 * 
 * @author Movie Team
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 根据场次ID查询场次
     * 
     * @param scheduleId 场次ID
     * @return 场次对象
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Schedule findScheduleById(Long scheduleId) {
        Schedule schedule = scheduleMapper.findScheduleById(scheduleId);
        if (schedule != null) {
            List<Order> orderList = orderMapper.findOrdersByScheduleId(scheduleId);
            schedule.setOrderList(orderList);
        }
        return schedule;
    }

    /**
     * 根据状态查询所有场次
     * 
     * @param scheduleState 场次状态（1：上映中，0：下架）
     * @return 场次列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Schedule> findScheduleByState(Integer scheduleState) {
        return scheduleMapper.findScheduleByState(scheduleState);
    }

    /**
     * 分页按状态查询场次
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param scheduleState 场次状态
     * @return 分页信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageInfo<Schedule> findAllScheduleByState(Integer page, Integer limit, Integer scheduleState) {
        PageHelper.startPage(page, limit);
        List<Schedule> list = scheduleMapper.findScheduleByState(scheduleState);
        for (Schedule schedule : list) {
            List<Order> orderList = orderMapper.findOrdersByScheduleId(schedule.getScheduleId());
            schedule.setOrderList(orderList);
        }
        return new PageInfo<>(list);
    }

    /**
     * 分页查询所有场次
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @return 分页信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageInfo<Schedule> findAllSchedule(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Schedule> list = scheduleMapper.findAllSchedule();
        for (Schedule schedule : list) {
            List<Order> orderList = orderMapper.findOrdersByScheduleId(schedule.getScheduleId());
            schedule.setOrderList(orderList);
        }
        return new PageInfo<>(list);
    }

    /**
     * 根据影院ID和电影ID查询场次
     * 
     * @param cinemaId 影院ID
     * @param movieId 电影ID
     * @return 场次列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Schedule> findScheduleByCineamIdAndMovieId(Long cinemaId, Long movieId) {
        return scheduleMapper.findScheduleByCinemaAndMovie(cinemaId, movieId);
    }

    /**
     * 根据电影名称模糊查询场次
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param movieName 电影名称
     * @return 分页信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageInfo<Schedule> findScheduleByMovieName(Integer page, Integer limit, String movieName) {
        PageHelper.startPage(page, limit);
        List<Schedule> list = scheduleMapper.findScheduleByMovieName(movieName);
        for (Schedule schedule : list) {
            List<Order> orderList = orderMapper.findOrdersByScheduleId(schedule.getScheduleId());
            schedule.setOrderList(orderList);
        }
        return new PageInfo<>(list);
    }

    /**
     * 根据电影名称模糊查询已下架场次
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param movieName 电影名称
     * @return 分页信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageInfo<Schedule> findOffScheduleByMovieName(Integer page, Integer limit, String movieName) {
        PageHelper.startPage(page, limit);
        List<Schedule> list = scheduleMapper.findOffScheduleByMovieName(movieName);
        for (Schedule schedule : list) {
            List<Order> orderList = orderMapper.findOrdersByScheduleId(schedule.getScheduleId());
            schedule.setOrderList(orderList);
        }
        return new PageInfo<>(list);
    }

    /**
     * 新增场次
     * 
     * @param schedule 场次对象
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addSchedule(Schedule schedule) {
        return scheduleMapper.addSchedule(schedule);
    }

    /**
     * 更新场次信息
     * 
     * @param schedule 场次对象
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateSchedule(Schedule schedule) {
        return scheduleMapper.updateSchedule(schedule);
    }

    /**
     * 删除场次（下架）
     * 
     * @param scheduleId 场次ID
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteSchedule(Long scheduleId) {
        return scheduleMapper.deleteSchedule(scheduleId);
    }

    /**
     * 增加场次剩余座位数
     * 用于退票时恢复座位
     * 
     * @param scheduleId 场次ID
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addScheduleRemain(Long scheduleId) {
        return scheduleMapper.addScheduleRemain(scheduleId);
    }

    /**
     * 减少场次剩余座位数
     * 用于购票时减少座位
     * 
     * @param scheduleId 场次ID
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int delScheduleRemain(Long scheduleId) {
        return scheduleMapper.delScheduleRemain(scheduleId);
    }
}
