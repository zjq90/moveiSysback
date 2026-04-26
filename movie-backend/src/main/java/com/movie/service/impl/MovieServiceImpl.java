package com.movie.service.impl;

import com.movie.entity.Movie;
import com.movie.mapper.MovieMapper;
import com.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 电影服务实现类
 * 
 * @author Movie Team
 */
@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieMapper movieMapper;

    /**
     * 根据电影ID查询电影
     * 
     * @param movieId 电影ID
     * @return 电影对象
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Movie findMovieById(Long movieId) {
        return movieMapper.findMovieById(movieId);
    }

    /**
     * 根据电影名称查询电影
     * 
     * @param movieCnName 电影中文名
     * @return 电影对象
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Movie findMovieByName(String movieCnName) {
        return movieMapper.findMovieByName(movieCnName);
    }

    /**
     * 根据状态查询所有电影
     * 
     * @param movieState 电影状态（1：在线，0：下架）
     * @return 电影列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Movie> findAllMovies(Integer movieState) {
        return movieMapper.findAllMovies(movieState);
    }

    /**
     * 模糊查询电影（按名称）
     * 
     * @param name 关键词
     * @return 电影列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Movie> findMoviesLikeName(String name) {
        return movieMapper.findMoviesLikeName(name);
    }

    /**
     * 模糊查询电影（按类型）
     * 
     * @param type 电影类型
     * @return 电影列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Movie> findMoviesLikeType(String type) {
        return movieMapper.findMoviesLikeType(type);
    }

    /**
     * 按上映时间排序查询电影
     * 
     * @return 电影列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Movie> sortMovieByDate() {
        return movieMapper.sortMovieByDate();
    }

    /**
     * 按评论数排序查询电影（热门）
     * 
     * @return 电影列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Movie> sortMovieByCount() {
        return movieMapper.sortMovieByCount();
    }

    /**
     * 按评分排序查询电影
     * 
     * @return 电影列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Movie> sortMovieByScore() {
        return movieMapper.sortMovieByScore();
    }

    /**
     * 按票房排序查询电影
     * 
     * @return 电影列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Movie> sortMovieByBoxOffice() {
        return movieMapper.sortMovieByBoxOffice();
    }

    /**
     * 新增电影
     * 
     * @param movie 电影对象
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addMovie(Movie movie) {
        return movieMapper.addMovie(movie);
    }

    /**
     * 更新电影信息
     * 
     * @param movie 电影对象
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateMovie(Movie movie) {
        return movieMapper.updateMovie(movie);
    }

    /**
     * 删除电影（下架）
     * 
     * @param movieId 电影ID
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteMovie(Long movieId) {
        return movieMapper.deleteMovie(movieId);
    }

    /**
     * 更新电影票房
     * 
     * @param boxOffice 票房变化量
     * @param movieId 电影ID
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int changeMovieBoxOffice(Float boxOffice, Long movieId) {
        return movieMapper.changeMovieBoxOffice(boxOffice, movieId);
    }

    /**
     * 增加电影评论数
     * 
     * @param movieId 电影ID
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addCommentCount(Long movieId) {
        return movieMapper.addMovieCommentCount(movieId);
    }

    /**
     * 减少电影评论数
     * 
     * @param movieId 电影ID
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int delCommentCount(Long movieId) {
        return movieMapper.deleteMovieCommentCount(movieId);
    }
}
