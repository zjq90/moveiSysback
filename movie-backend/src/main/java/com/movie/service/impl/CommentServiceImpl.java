package com.movie.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.movie.entity.Comment;
import com.movie.mapper.CommentMapper;
import com.movie.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评论服务实现类
 * 
 * @author Movie Team
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 根据评论ID查询评论
     * 
     * @param commentId 评论ID
     * @return 评论对象
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Comment findCommentById(Long commentId) {
        return commentMapper.findCommentById(commentId);
    }

    /**
     * 查询所有评论
     * 
     * @return 评论列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Comment> findAllComments() {
        return commentMapper.findAllComments();
    }

    /**
     * 分页查询所有评论
     * 支持关键词模糊搜索评论内容
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param keyword 搜索关键词
     * @return 分页信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageInfo<Comment> findAllCommentsBySplitPage(Integer page, Integer limit, String keyword) {
        PageHelper.startPage(page, limit);
        List<Comment> list;
        if (keyword != null && !keyword.trim().equals("")) {
            list = commentMapper.findCommentsLikeContent(keyword);
        } else {
            list = commentMapper.findAllComments();
        }
        return new PageInfo<>(list);
    }

    /**
     * 根据用户名分页查询评论
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param userName 用户名
     * @return 分页信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageInfo<Comment> findCommentsByUserName(Integer page, Integer limit, String userName) {
        PageHelper.startPage(page, limit);
        List<Comment> list = commentMapper.findCommentsByUserName(userName);
        return new PageInfo<>(list);
    }

    /**
     * 新增评论
     * 用户发表评论时调用
     * 注意：需要同时更新电影的评论数
     * 
     * @param comment 评论对象
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addComemnt(Comment comment) {
        return commentMapper.addComment(comment);
    }

    /**
     * 更新评论
     * 用户修改评论时调用
     * 
     * @param comment 评论对象
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateComment(Comment comment) {
        return commentMapper.updateComment(comment);
    }

    /**
     * 删除评论
     * 管理员删除评论时调用
     * 注意：需要同时更新电影的评论数
     * 
     * @param commentId 评论ID
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteComment(Long commentId) {
        return commentMapper.deleteComment(commentId);
    }
}
