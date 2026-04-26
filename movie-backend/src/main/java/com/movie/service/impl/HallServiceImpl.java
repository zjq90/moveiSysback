package com.movie.service.impl;

import com.movie.entity.Hall;
import com.movie.mapper.HallMapper;
import com.movie.service.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 放映厅服务实现类
 * 
 * @author Movie Team
 */
@Service
public class HallServiceImpl implements HallService {

    @Autowired
    private HallMapper hallMapper;

    /**
     * 根据放映厅ID查询放映厅
     * 
     * @param hallId 放映厅ID
     * @return 放映厅对象
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Hall findHallById(Long hallId) {
        return hallMapper.findHallById(hallId);
    }

    /**
     * 根据影院ID查询所有放映厅
     * 
     * @param cinemaId 影院ID
     * @return 放映厅列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Hall> findHallByCinemaId(Long cinemaId) {
        return hallMapper.findHallByCinemaId(cinemaId);
    }

    /**
     * 根据影院名称和放映厅名称查询放映厅
     * 用于场次管理中根据名称查找放映厅
     * 
     * @param cinemaName 影院名称
     * @param hallName 放映厅名称
     * @return 放映厅对象
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Hall findHallByCinemaAndHallName(String cinemaName, String hallName) {
        return hallMapper.findHallByCinemaAndHallName(cinemaName, hallName);
    }

    /**
     * 新增放映厅
     * 
     * @param hall 放映厅对象
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addHall(Hall hall) {
        return hallMapper.addHall(hall);
    }

    /**
     * 更新放映厅信息
     * 
     * @param hall 放映厅对象
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateHall(Hall hall) {
        return hallMapper.updateHall(hall);
    }

    /**
     * 删除放映厅
     * 
     * @param hallId 放映厅ID
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteHall(Long hallId) {
        return hallMapper.deleteHall(hallId);
    }
}
