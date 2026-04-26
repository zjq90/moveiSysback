package com.movie.controller;

import com.github.pagehelper.PageInfo;
import com.movie.common.Result;
import com.movie.entity.Comment;
import com.movie.entity.User;
import com.movie.service.CommentService;
import com.movie.service.MovieService;
import com.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 评论管理控制器
 * 处理评论相关的HTTP请求：评论列表、增删改查等
 * 
 * @author Movie Team
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    /**
     * 查询所有评论
     * 
     * @return 评论列表
     */
    @GetMapping("/findAllComments")
    public Result<List<Comment>> findAllComments() {
        List<Comment> list = commentService.findAllComments();
        for (Comment comment : list) {
            User user = userService.findUserById(comment.getUserId());
            user.setUserPwd(null);
            comment.setCommentUser(user);
        }
        return Result.success((long) list.size(), list);
    }

    /**
     * 分页查询所有评论
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param keyword 搜索关键词
     * @return 评论分页列表
     */
    @GetMapping("/findAllCommentsPage")
    public Result<List<Comment>> findAllCommentsPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                                        @RequestParam(value = "keyword", required = false) String keyword) {
        PageInfo<Comment> info = commentService.findAllCommentsBySplitPage(page, limit, keyword);
        
        for (Comment comment : info.getList()) {
            User user = userService.findUserById(comment.getUserId());
            if (user != null) {
                user.setUserPwd(null);
            }
            comment.setCommentUser(user);
        }
        
        return Result.success(info.getTotal(), info.getList());
    }

    /**
     * 用户发表评论
     * 
     * @param movieId 电影ID
     * @param commentContent 评论内容
     * @param request HTTP请求对象
     * @return 发表结果
     */
    @PostMapping("/addCommentByUser")
    public Result<String> addCommentByUser(@RequestParam("movieId") Long movieId,
                                             @RequestParam("commentContent") String commentContent,
                                             HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return Result.error("您未登录，登录之后才可评论");
        }
        
        Comment comment = new Comment();
        comment.setCommentContent(commentContent);
        comment.setMovieId(movieId);
        comment.setUserId(user.getUserId());
        comment.setCommentTime(new Date());
        
        Integer result = commentService.addComemnt(comment);
        if (result > 0) {
            movieService.addCommentCount(movieId);
            return Result.success("评论成功");
        }
        
        return Result.error("评论失败");
    }

    /**
     * 更新评论
     * 
     * @param commentId 评论ID
     * @param commentContent 新评论内容
     * @return 更新结果
     */
    @PostMapping("/updateComment")
    public Result<String> updateComment(@RequestParam("commentId") Long commentId,
                                          @RequestParam("commentContent") String commentContent) {
        Comment comment = commentService.findCommentById(commentId);
        if (comment == null) {
            return Result.error("评论不存在");
        }
        
        comment.setCommentTime(new Date());
        comment.setCommentContent(commentContent);
        
        Integer result = commentService.updateComment(comment);
        if (result > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    /**
     * 删除评论
     * 
     * @param commentId 评论ID
     * @return 删除结果
     */
    @PostMapping("/deleteComment")
    public Result<String> deleteComment(@RequestParam("commentId") Long commentId) {
        Comment comment = commentService.findCommentById(commentId);
        if (comment == null) {
            return Result.error("评论不存在");
        }
        
        Integer result = commentService.deleteComment(commentId);
        if (result > 0) {
            movieService.delCommentCount(comment.getMovieId());
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 根据用户名分页查询评论
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param userName 用户名
     * @return 评论分页列表
     */
    @GetMapping("/findCommentsByUserName")
    public Result<List<Comment>> findCommentsByUserName(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                           @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                                           @RequestParam("userName") String userName) {
        PageInfo<Comment> info = commentService.findCommentsByUserName(page, limit, userName);
        
        for (Comment comment : info.getList()) {
            User user = userService.findUserById(comment.getUserId());
            if (user != null) {
                user.setUserPwd(null);
            }
            comment.setCommentUser(user);
        }
        
        return Result.success(info.getTotal(), info.getList());
    }

    /**
     * 根据评论ID查询评论
     * 
     * @param commentId 评论ID
     * @return 评论信息
     */
    @GetMapping("/findCommentById")
    public Result<Comment> findCommentById(@RequestParam("commentId") Long commentId) {
        Comment comment = commentService.findCommentById(commentId);
        if (comment != null) {
            User user = userService.findUserById(comment.getUserId());
            if (user != null) {
                user.setUserPwd(null);
            }
            comment.setCommentUser(user);
            return Result.success(comment);
        }
        return Result.error("评论不存在");
    }
}
