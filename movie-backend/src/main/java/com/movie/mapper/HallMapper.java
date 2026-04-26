package com.movie.mapper;

import com.movie.entity.Hall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 放映厅数据访问接口
 * 
 * @author Movie Team
 */
@Mapper
public interface HallMapper {

    /**
     * 根据放映厅ID查询放映厅
     * 
     * @param hallId 放映厅ID
     * @return 放映厅对象
     */
    Hall findHallById(@Param("hallId") Long hallId);

    /**
     * 根据影院ID查询所有放映厅
     * 
     * @param cinemaId 影院ID
     * @return 放映厅列表
     */
    List<Hall> findHallByCinemaId(@Param("cinemaId") Long cinemaId);

    /**
     * 根据影院名称和放映厅名称查询放映厅
     * 
     * @param cinemaName 影院名称
     * @param hallName 放映厅名称
     * @return 放映厅对象
     */
    Hall findHallByCinemaAndHallName(@Param("cinemaName") String cinemaName, @Param("hallName") String hallName);

    /**
     * 新增放映厅
     * 
     * @param hall 放映厅对象
     * @return 影响行数
     */
    int addHall(Hall hall);

    /**
     * 更新放映厅信息
     * 
     * @param hall 放映厅对象
     * @return 影响行数
     */
    int updateHall(Hall hall);

    /**
     * 删除放映厅
     * 
     * @param hallId 放映厅ID
     * @return 影响行数
     */
    int deleteHall(@Param("hallId") Long hallId);
}
