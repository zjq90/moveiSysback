package com.movie.mapper;

import com.movie.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据访问接口
 * 
 * @author Movie Team
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象
     */
    User findUserById(@Param("userId") Long userId);

    /**
     * 根据用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户列表
     */
    List<User> findUserByName(@Param("userName") String userName);

    /**
     * 模糊查询用户
     * 
     * @param keyword 关键词
     * @return 用户列表
     */
    List<User> findUserLikeName(@Param("keyword") String keyword);

    /**
     * 查询所有用户
     * 
     * @return 用户列表
     */
    List<User> findAllUser();

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
    int updateUser(User user);

    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteUser(@Param("userId") Long userId);
}
