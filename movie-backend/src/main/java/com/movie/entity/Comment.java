package com.movie.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 评论实体类
 * 对应数据库表 comment
 * 
 * @author Movie Team
 */
@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 所属用户ID
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 所属电影ID
     */
    private Long movieId;

    /**
     * 评论时间
     */
    private Date commentTime;

    /**
     * 评论用户（非数据库字段）
     */
    private User commentUser;

    /**
     * 评论所属电影（非数据库字段）
     */
    private Movie commentMovie;
}
