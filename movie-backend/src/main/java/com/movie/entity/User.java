package com.movie.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户实体类
 * 对应数据库表 user
 * 
 * @author Movie Team
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPwd;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户权限
     * 0：普通会员
     * 1：管理员
     */
    private Integer userRole;

    /**
     * 用户头像地址
     */
    private String userHeadImg;
}
