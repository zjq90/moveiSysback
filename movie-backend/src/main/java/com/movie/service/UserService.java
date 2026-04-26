package com.movie.service;

import com.github.pagehelper.PageInfo;
import com.movie.entity.User;

import java.util.List;

/**
 * 用户服务接口
 * 
 * @author Movie Team
 */
public interface UserService {

    /**
     * 用户登录
     * 
     * @param userName 用户名
     * @param userPwd 密码
     * @return 用户对象，登录失败返回null
     */
    User login(String userName, String userPwd);

    /**
     * 根据用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象
     */
    User findUserById(Long userId);

    /**
     * 根据用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户列表
     */
    List<User> findUserByName(String userName);

    /**
     * 新增用户
     * 
     * @param user 用户对象
     * @return 影响行数
     */
    int addUser(User user);

    /**
     * 更新用户信息
     * 
     * @param user 用户对象
     * @return 影响行数
     */
    int updateUserInfo(User user);

    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteUser(Long userId);

    /**
     * 分页查询所有用户
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param keyword 搜索关键词
     * @return 分页信息
     */
    PageInfo<User> findAllUserBySplitPage(Integer page, Integer limit, String keyword);

    /**
     * 查询所有用户
     * 
     * @return 用户列表
     */
    List<User> findAllUserInfos();

    /**
     * 模糊查询用户
     * 
     * @param name 用户名关键词
     * @return 用户列表
     */
    List<User> findUserLikeName(String name);
}
