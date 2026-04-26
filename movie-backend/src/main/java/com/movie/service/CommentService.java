package com.movie.service;

import com.github.pagehelper.PageInfo;
import com.movie.entity.Comment;

import java.util.List;

/**
 * 评论服务接口
 * 
 * @author Movie Team
 */
public interface CommentService {

    /**
     * 根据评论ID查询评论
     * 
     * @param commentId 评论ID
     * @return 评论对象
     */
    Comment findCommentById(Long commentId);

    /**
     * 查询所有评论
     * 
     * @return 评论列表
     */
    List<Comment> findAllComments();

    /**
     * 分页查询所有评论
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param keyword 搜索关键词
     * @return 分页信息
     */
    PageInfo<Comment> findAllCommentsBySplitPage(Integer page, Integer limit, String keyword);

    /**
     * 根据用户名分页查询评论
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param userName 用户名
     * @return 分页信息
     */
    PageInfo<Comment> findCommentsByUserName(Integer page, Integer limit, String userName);

    /**
     * 新增评论
     * 
     * @param comment 评论对象
     * @return 影响行数
     */
    int addComemnt(Comment comment);

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
    int deleteComment(Long commentId);
}
