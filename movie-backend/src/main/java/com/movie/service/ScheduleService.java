package com.movie.service;

import com.github.pagehelper.PageInfo;
import com.movie.entity.Schedule;

import java.util.List;

/**
 * 场次服务接口
 * 
 * @author Movie Team
 */
public interface ScheduleService {

    /**
     * 根据场次ID查询场次
     * 
     * @param scheduleId 场次ID
     * @return 场次对象
     */
    Schedule findScheduleById(Long scheduleId);

    /**
     * 根据状态查询所有场次
     * 
     * @param scheduleState 场次状态（1：上映中，0：下架）
     * @return 场次列表
     */
    List<Schedule> findScheduleByState(Integer scheduleState);

    /**
     * 分页按状态查询场次
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param scheduleState 场次状态
     * @return 分页信息
     */
    PageInfo<Schedule> findAllScheduleByState(Integer page, Integer limit, Integer scheduleState);

    /**
     * 分页查询所有场次
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @return 分页信息
     */
    PageInfo<Schedule> findAllSchedule(Integer page, Integer limit);

    /**
     * 根据影院ID和电影ID查询场次
     * 
     * @param cinemaId 影院ID
     * @param movieId 电影ID
     * @return 场次列表
     */
    List<Schedule> findScheduleByCineamIdAndMovieId(Long cinemaId, Long movieId);

    /**
     * 根据电影名称模糊查询场次
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param movieName 电影名称
     * @return 分页信息
     */
    PageInfo<Schedule> findScheduleByMovieName(Integer page, Integer limit, String movieName);

    /**
     * 根据电影名称模糊查询已下架场次
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param movieName 电影名称
     * @return 分页信息
     */
    PageInfo<Schedule> findOffScheduleByMovieName(Integer page, Integer limit, String movieName);

    /**
     * 新增场次
     * 
     * @param schedule 场次对象
     * @return 影响行数
     */
    int addSchedule(Schedule schedule);

    /**
     * 更新场次信息
     * 
     * @param schedule 场次对象
     * @return 影响行数
     */
    int updateSchedule(Schedule schedule);

    /**
     * 删除场次（下架）
     * 
     * @param scheduleId 场次ID
     * @return 影响行数
     */
    int deleteSchedule(Long scheduleId);

    /**
     * 增加场次剩余座位数
     * 
     * @param scheduleId 场次ID
     * @return 影响行数
     */
    int addScheduleRemain(Long scheduleId);

    /**
     * 减少场次剩余座位数
     * 
     * @param scheduleId 场次ID
     * @return 影响行数
     */
    int delScheduleRemain(Long scheduleId);
}
