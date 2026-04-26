package com.movie.controller;

import com.github.pagehelper.PageInfo;
import com.movie.common.Result;
import com.movie.entity.User;
import com.movie.service.UserService;
import com.movie.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 用户管理控制器
 * 处理用户相关的HTTP请求：登录、注册、个人信息管理等
 * 
 * @author Movie Team
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * 
     * @param userName 用户名
     * @param userPwd 密码
     * @param request HTTP请求对象
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<User> login(@RequestParam("userName") String userName,
                              @RequestParam("userPwd") String userPwd,
                              HttpServletRequest request) {
        User user = userService.login(userName, userPwd);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            user.setUserPwd(null);
            return Result.success("登录成功", user);
        }
        return Result.error("账号或密码错误");
    }

    /**
     * 用户登出
     * 
     * @param request HTTP请求对象
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return Result.success("退出成功");
    }

    /**
     * 用户注册
     * 
     * @param user 用户对象
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        List<User> list = userService.findUserByName(user.getUserName());
        if (list.size() > 0) {
            return Result.error("该账号已被注册");
        }
        Integer result = userService.addUser(user);
        if (result > 0) {
            return Result.success("注册成功");
        }
        return Result.error("注册失败");
    }

    /**
     * 更新用户信息
     * 
     * @param user 用户对象
     * @return 更新结果
     */
    @PostMapping("/updateUser")
    public Result<String> updateUser(@RequestBody User user) {
        Integer result = userService.updateUserInfo(user);
        if (result > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    /**
     * 修改用户密码
     * 
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @param request HTTP请求对象
     * @return 修改结果
     */
    @PostMapping("/modifyUserPwd")
    public Result<String> modifyUserPwd(@RequestParam("oldPwd") String oldPwd,
                                         @RequestParam("newPwd") String newPwd,
                                         HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (!user.getUserPwd().equals(oldPwd)) {
            return Result.error("原密码错误");
        }
        user.setUserPwd(newPwd);
        userService.updateUserInfo(user);
        session.removeAttribute("user");
        return Result.success("密码修改成功，请重新登录");
    }

    /**
     * 分页查询所有用户（管理员）
     * 
     * @param page 当前页码
     * @param limit 每页数量
     * @param keyword 搜索关键词
     * @return 用户分页列表
     */
    @GetMapping("/findAllUser")
    public Result<List<User>> findAllUser(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                            @RequestParam(value = "keyword", required = false) String keyword) {
        PageInfo<User> info = userService.findAllUserBySplitPage(page, limit, keyword);
        return Result.success(info.getTotal(), info.getList());
    }

    /**
     * 查询所有用户列表
     * 
     * @return 用户列表
     */
    @GetMapping("/findAllUserInfos")
    public Result<List<User>> findAllUserInfos() {
        List<User> list = userService.findAllUserInfos();
        return Result.success((long) list.size(), list);
    }

    /**
     * 模糊查询用户
     * 
     * @param userName 用户名关键词
     * @return 用户列表
     */
    @GetMapping("/findUserInfosByName")
    public Result<List<User>> findUserInfosByName(@RequestParam("userName") String userName) {
        List<User> list = userService.findUserLikeName(userName);
        return Result.success((long) list.size(), list);
    }

    /**
     * 上传用户头像
     * 
     * @param file 头像文件
     * @param user 用户对象
     * @param request HTTP请求对象
     * @return 上传结果
     * @throws IOException IO异常
     */
    @PostMapping("/uploadHeadImg")
    public Result<User> uploadHeadImg(@RequestParam(value = "file", required = false) MultipartFile file,
                                        User user,
                                        HttpServletRequest request) throws IOException {
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUIDUtil.getUUID() + suffix;
            
            String uploadDir = request.getServletContext().getRealPath("/upload/head");
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            String filePath = uploadDir + "/" + fileName;
            file.transferTo(new File(filePath));
            
            user.setUserHeadImg("/upload/head/" + fileName);
        } else {
            User oldUser = userService.findUserById(user.getUserId());
            user.setUserHeadImg(oldUser.getUserHeadImg());
            user.setUserPwd(oldUser.getUserPwd());
        }
        
        Integer result = userService.updateUserInfo(user);
        if (result > 0) {
            return Result.success("上传成功", user);
        }
        return Result.error("上传失败");
    }

    /**
     * 根据用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象
     */
    @GetMapping("/findUserById")
    public Result<User> findUserById(@RequestParam("userId") Long userId) {
        User user = userService.findUserById(userId);
        if (user != null) {
            user.setUserPwd(null);
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }
}
