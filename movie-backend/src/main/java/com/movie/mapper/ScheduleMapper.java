package com.movie.mapper;

import com.movie.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 场次数据访问接口
 * 
 * @author Movie Team
 */
@Mapper
public interface ScheduleMapper {

    /**
     * 根据场次ID查询场次
     * 
     * @param scheduleId 场次ID
     * @return 场次对象
     */
    Schedule findScheduleById(@Param("scheduleId") Long scheduleId);

    /**
     * 根据状态查询所有场次
     * 
     * @param scheduleState 场次状态（1：上映中，0：下架）
     * @return 场次列表
     */
    List<Schedule> findScheduleByState(@Param("scheduleState") Integer scheduleState);

    /**
     * 查询所有场次
     * 
     * @return 场次列表
     */
    List<Schedule> findAllSchedule();

    /**
     * 根据影院ID和电影ID查询场次
     * 
     * @param cinemaId 影院ID
     * @param movieId 电影ID
     * @return 场次列表
     */
    List<Schedule> findScheduleByCinemaAndMovie(@Param("cinemaId") Long cinemaId, @Param("movieId") Long movieId);

    /**
     * 根据影院ID、电影ID和放映厅ID查询场次
     * 
     * @param cinemaId 影院ID
     * @param movieId 电影ID
     * @param hallId 放映厅ID
     * @return 场次列表
     */
    List<Schedule> findScheduleByCinemaAndMovieAndHall(@Param("cinemaId") Long cinemaId, 
                                                         @Param("movieId") Long movieId, 
                                                         @Param("hallId") Long hallId);

    /**
     * 根据电影名称模糊查询场次
     * 
     * @param movieName 电影名称
     * @return 场次列表
     */
    List<Schedule> findScheduleByMovieName(@Param("movieName") String movieName);

    /**
     * 根据电影名称模糊查询已下架场次
     * 
     * @param movieName 电影名称
     * @return 场次列表
     */
    List<Schedule> findOffScheduleByMovieName(@Param("movieName") String movieName);

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
    int deleteSchedule(@Param("scheduleId") Long scheduleId);

    /**
     * 增加场次剩余座位数
     * 
     * @param scheduleId 场次ID
     * @return 影响行数
     */
    int addScheduleRemain(@Param("scheduleId") Long scheduleId);

    /**
     * 减少场次剩余座位数
     * 
     * @param scheduleId 场次ID
     * @return 影响行数
     */
    int delScheduleRemain(@Param("scheduleId") Long scheduleId);
}
