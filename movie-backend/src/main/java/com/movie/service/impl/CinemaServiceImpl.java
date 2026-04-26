package com.movie.service.impl;

import com.movie.entity.Cinema;
import com.movie.mapper.CinemaMapper;
import com.movie.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 影院服务实现类
 * 
 * @author Movie Team
 */
@Service
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    private CinemaMapper cinemaMapper;

    /**
     * 根据影院ID查询影院
     * 
     * @param cinemaId 影院ID
     * @return 影院对象
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Cinema findCinemaById(Long cinemaId) {
        return cinemaMapper.findCinemaById(cinemaId);
    }

    /**
     * 根据影院名称查询影院
     * 
     * @param cinemaName 影院名称
     * @return 影院对象
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Cinema findCinemaByName(String cinemaName) {
        return cinemaMapper.findCinemaByName(cinemaName);
    }

    /**
     * 查询所有影院
     * 
     * @return 影院列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Cinema> findAllCinemas() {
        return cinemaMapper.findAllCinemas();
    }

    /**
     * 根据电影ID查询放映该电影的影院列表
     * 通过多表关联查询：影院 -> 放映厅 -> 场次 -> 电影
     * 
     * @param movieId 电影ID
     * @return 影院列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Cinema> findCinemasByMovieId(Long movieId) {
        return cinemaMapper.findCinemasByMovieId(movieId);
    }

    /**
     * 新增影院
     * 
     * @param cinema 影院对象
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addCinema(Cinema cinema) {
        return cinemaMapper.addCinema(cinema);
    }

    /**
     * 更新影院信息
     * 
     * @param cinema 影院对象
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateCinema(Cinema cinema) {
        return cinemaMapper.updateCinema(cinema);
    }

    /**
     * 删除影院
     * 
     * @param cinemaId 影院ID
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteCinema(Long cinemaId) {
        return cinemaMapper.deleteCinema(cinemaId);
    }
}
