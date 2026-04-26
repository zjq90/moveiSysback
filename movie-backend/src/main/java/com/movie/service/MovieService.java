package com.movie.service;

import com.github.pagehelper.PageInfo;
import com.movie.entity.Movie;

import java.util.List;

/**
 * 电影服务接口
 * 
 * @author Movie Team
 */
public interface MovieService {

    /**
     * 根据电影ID查询电影
     * 
     * @param movieId 电影ID
     * @return 电影对象
     */
    Movie findMovieById(Long movieId);

    /**
     * 根据电影名称查询电影
     * 
     * @param movieCnName 电影中文名
     * @return 电影对象
     */
    Movie findMovieByName(String movieCnName);

    /**
     * 根据状态查询所有电影
     * 
     * @param movieState 电影状态（1：在线，0：下架）
     * @return 电影列表
     */
    List<Movie> findAllMovies(Integer movieState);

    /**
     * 模糊查询电影（按名称）
     * 
     * @param name 关键词
     * @return 电影列表
     */
    List<Movie> findMoviesLikeName(String name);

    /**
     * 模糊查询电影（按类型）
     * 
     * @param type 电影类型
     * @return 电影列表
     */
    List<Movie> findMoviesLikeType(String type);

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
    int deleteMovie(Long movieId);

    /**
     * 更新电影票房
     * 
     * @param boxOffice 票房变化量
     * @param movieId 电影ID
     * @return 影响行数
     */
    int changeMovieBoxOffice(Float boxOffice, Long movieId);

    /**
     * 增加电影评论数
     * 
     * @param movieId 电影ID
     * @return 影响行数
     */
    int addCommentCount(Long movieId);

    /**
     * 减少电影评论数
     * 
     * @param movieId 电影ID
     * @return 影响行数
     */
    int delCommentCount(Long movieId);
}
