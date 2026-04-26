package com.movie.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.movie.entity.User;
import com.movie.mapper.UserMapper;
import com.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务实现类
 * 
 * @author Movie Team
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * 根据用户名查询用户，验证密码是否匹配
     * 
     * @param userName 用户名
     * @param userPwd 密码
     * @return 用户对象，登录失败返回null
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public User login(String userName, String userPwd) {
        List<User> userList = userMapper.findUserByName(userName);
        for (User user : userList) {
            if (user.getUserPwd().equals(userPwd)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 根据用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public User findUserById(Long userId) {
        return userMapper.findUserById(userId);
    }

    /**
     * 根据用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<User> findUserByName(String userName) {
        return userMapper.findUserByName(userName);
    }

    /**
     * 新增用户
     * 
     * @param user 用户对象
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addUser(User user) {
        return userMapper.addUser(user);
    }

    /**
     * 更新用户信息
     * 
     * @param user 用户对象
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateUserInfo(User user) {
        return userMapper.updateUser(user);
    }

    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return 影响行数
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteUser(Long userId) {
        return userMapper.deleteUser(userId);
    }

    /**
     * 分页查询所有用户
     * 支持关键词模糊搜索
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param keyword 搜索关键词
     * @return 分页信息
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PageInfo<User> findAllUserBySplitPage(Integer page, Integer limit, String keyword) {
        PageHelper.startPage(page, limit);
        List<User> list = new ArrayList<>();
        if (keyword != null && !keyword.trim().equals("")) {
            list = userMapper.findUserLikeName(keyword);
        } else {
            list = userMapper.findAllUser();
        }
        return new PageInfo<>(list);
    }

    /**
     * 查询所有用户
     * 
     * @return 用户列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<User> findAllUserInfos() {
        return userMapper.findAllUser();
    }

    /**
     * 模糊查询用户
     * 
     * @param name 用户名关键词
     * @return 用户列表
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<User> findUserLikeName(String name) {
        return userMapper.findUserLikeName(name);
    }
}
