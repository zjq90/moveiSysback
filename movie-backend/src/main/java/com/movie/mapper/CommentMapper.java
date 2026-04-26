package com.movie.mapper;

import com.movie.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论数据访问接口
 * 
 * @author Movie Team
 */
@Mapper
public interface CommentMapper {

    /**
     * 根据评论ID查询评论
     * 
     * @param commentId 评论ID
     * @return 评论对象
     */
    Comment findCommentById(@Param("commentId") Long commentId);

    /**
     * 查询所有评论
     * 
     * @return 评论列表
     */
    List<Comment> findAllComments();

    /**
     * 根据用户名查询评论
     * 
     * @param userName 用户名
     * @return 评论列表
     */
    List<Comment> findCommentsByUserName(@Param("userName") String userName);

    /**
     * 模糊查询评论
     * 
     * @param keyword 关键词
     * @return 评论列表
     */
    List<Comment> findCommentsLikeContent(@Param("keyword") String keyword);

    /**
     * 新增评论
     * 
     * @param comment 评论对象
     * @return 影响行数
     */
    int addComment(Comment comment);

    /**
     * 更新评论
     * 
     * @param comment 评论对象
     * @return 影响行数
     */
    int updateComment(Comment comment);

    /**
     * 删除评论
     * 
     * @param commentId 评论ID
     * @return 影响行数
     */
    int deleteComment(@Param("commentId") Long commentId);
}
