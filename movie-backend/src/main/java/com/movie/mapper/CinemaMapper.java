package com.movie.mapper;

import com.movie.entity.Cinema;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 影院数据访问接口
 * 
 * @author Movie Team
 */
@Mapper
public interface CinemaMapper {

    /**
     * 根据影院ID查询影院
     * 
     * @param cinemaId 影院ID
     * @return 影院对象
     */
    Cinema findCinemaById(@Param("cinemaId") Long cinemaId);

    /**
     * 根据影院名称查询影院
     * 
     * @param cinemaName 影院名称
     * @return 影院对象
     */
    Cinema findCinemaByName(@Param("cinemaName") String cinemaName);

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
    List<Cinema> findCinemasByMovieId(@Param("movieId") Long movieId);

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
    int deleteCinema(@Param("cinemaId") Long cinemaId);
}
