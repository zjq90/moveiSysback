package com.movie.mapper;

import com.movie.entity.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 电影数据访问接口
 * 
 * @author Movie Team
 */
@Mapper
public interface MovieMapper {

    /**
     * 根据电影ID查询电影
     * 
     * @param movieId 电影ID
     * @return 电影对象
     */
    Movie findMovieById(@Param("movieId") Long movieId);

    /**
     * 根据电影名称查询电影
     * 
     * @param movieCnName 电影中文名
     * @return 电影对象
     */
    Movie findMovieByName(@Param("movieCnName") String movieCnName);

    /**
     * 根据状态查询所有电影
     * 
     * @param movieState 电影状态（1：在线，0：下架）
     * @return 电影列表
     */
    List<Movie> findAllMovies(@Param("movieState") Integer movieState);

    /**
     * 模糊查询电影（按名称）
     * 
     * @param keyword 关键词
     * @return 电影列表
     */
    List<Movie> findMoviesLikeName(@Param("keyword") String keyword);

    /**
     * 模糊查询电影（按类型）
     * 
     * @param movieType 电影类型
     * @return 电影列表
     */
    List<Movie> findMoviesLikeType(@Param("movieType") String movieType);

    /**
     * 按上映时间排序查询电影
     * 
     * @return 电影列表
     */
    List<Movie> sortMovieByDate();

    /**
     * 按评论数排序查询电影（热门）
     * 
     * @return 电影列表
     */
    List<Movie> sortMovieByCount();

    /**
     * 按评分排序查询电影
     * 
     * @return 电影列表
     */
    List<Movie> sortMovieByScore();

    /**
     * 按票房排序查询电影
     * 
     * @return 电影列表
     */
    List<Movie> sortMovieByBoxOffice();

    /**
     * 新增电影
     * 
     * @param movie 电影对象
     * @return 影响行数
     */
    int addMovie(Movie movie);

    /**
     * 更新电影信息
     * 
     * @param movie 电影对象
     * @return 影响行数
     */
    int updateMovie(Movie movie);

    /**
     * 删除电影（下架）
     * 
     * @param movieId 电影ID
     * @return 影响行数
     */
    int deleteMovie(@Param("movieId") Long movieId);

    /**
     * 更新电影票房
     * 
     * @param movieBoxOffice 票房变化量
     * @param movieId 电影ID
     * @return 影响行数
     */
    int changeMovieBoxOffice(@Param("movieBoxOffice") Float movieBoxOffice, @Param("movieId") Long movieId);

    /**
     * 增加电影评论数
     * 
     * @param movieId 电影ID
     * @return 影响行数
     */
    int addMovieCommentCount(@Param("movieId") Long movieId);

    /**
     * 减少电影评论数
     * 
     * @param movieId 电影ID
     * @return 影响行数
     */
    int deleteMovieCommentCount(@Param("movieId") Long movieId);
}
