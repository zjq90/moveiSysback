package com.movie.entity;

import lombok.Data;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * 电影实体类
 * 对应数据库表 movie
 * 
 * @author Movie Team
 */
@Data
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 电影ID
     */
    private Long movieId;

    /**
     * 电影名称（中文）
     */
    private String movieCnName;

    /**
     * 电影名称（外语）
     */
    private String movieFgName;

    /**
     * 电影演职人员
     */
    private String movieActor;

    /**
     * 电影导演
     */
    private String movieDirector;

    /**
     * 电影详情
     */
    private String movieDetail;

    /**
     * 电影时长
     */
    private String movieDuration;

    /**
     * 电影类型
     */
    private String movieType;

    /**
     * 电影评分
     */
    private Float movieScore;

    /**
     * 电影票房
     */
    private Float movieBoxOffice;

    /**
     * 电影参评人数
     */
    private Long movieCommentCount;

    /**
     * 电影上映时间
     */
    private Date movieReleaseDate;

    /**
     * 电影制片地区
     */
    private String movieCountry;

    /**
     * 电影海报地址
     */
    private String moviePicture;

    /**
     * 电影状态
     * 1：在线
     * 0：下架
     */
    private Integer movieState;

    /**
     * 电影评论列表（非数据库字段）
     */
    private List<Comment> commentList;
}
