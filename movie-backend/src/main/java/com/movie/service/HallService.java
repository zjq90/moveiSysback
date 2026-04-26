package com.movie.service;

import com.movie.entity.Hall;

import java.util.List;

/**
 * 放映厅服务接口
 * 
 * @author Movie Team
 */
public interface HallService {

    /**
     * 根据放映厅ID查询放映厅
     * 
     * @param hallId 放映厅ID
     * @return 放映厅对象
     */
    Hall findHallById(Long hallId);

    /**
     * 根据影院ID查询所有放映厅
     * 
     * @param cinemaId 影院ID
     * @return 放映厅列表
     */
    List<Hall> findHallByCinemaId(Long cinemaId);

    /**
     * 根据影院名称和放映厅名称查询放映厅
     * 
     * @param cinemaName 影院名称
     * @param hallName 放映厅名称
     * @return 放映厅对象
     */
    Hall findHallByCinemaAndHallName(String cinemaName, String hallName);

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
    int deleteHall(Long hallId);
}
