package com.movie.service;

import com.movie.entity.Cinema;

import java.util.List;

/**
 * 影院服务接口
 * 
 * @author Movie Team
 */
public interface CinemaService {

    /**
     * 根据影院ID查询影院
     * 
     * @param cinemaId 影院ID
     * @return 影院对象
     */
    Cinema findCinemaById(Long cinemaId);

    /**
     * 根据影院名称查询影院
     * 
     * @param cinemaName 影院名称
     * @return 影院对象
     */
    Cinema findCinemaByName(String cinemaName);

    /**
     * 查询所有影院
     * 
     * @return 影院列表
     */
    List<Cinema> findAllCinemas();

    /**
     * 根据电影ID查询放映该电影的影院列表
     * 
     * @param movieId 电影ID
     * @return 影院列表
     */
    List<Cinema> findCinemasByMovieId(Long movieId);

    /**
     * 新增影院
     * 
     * @param cinema 影院对象
     * @return 影响行数
     */
    int addCinema(Cinema cinema);

    /**
     * 更新影院信息
     * 
     * @param cinema 影院对象
     * @return 影响行数
     */
    int updateCinema(Cinema cinema);

    /**
     * 删除影院
     * 
     * @param cinemaId 影院ID
     * @return 影响行数
     */
    int deleteCinema(Long cinemaId);
}
