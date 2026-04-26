package com.movie.common;

import java.io.Serializable;

/**
 * 统一响应结果类
 * 用于封装所有API的返回结果
 * 
 * @param <T> 数据类型
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码：0表示成功，其他表示失败
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 数据总量（用于分页）
     */
    private Long count;

    /**
     * 响应数据
     */
    private T data;

    public Result() {
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(Integer code, String msg, Long count, T data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(0, "操作成功");
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "操作成功", data);
    }

    /**
     * 成功响应（带消息和数据）
     */
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(0, msg, data);
    }

    /**
     * 成功响应（分页数据）
     */
    public static <T> Result<T> success(Long count, T data) {
        return new Result<>(0, "", count, data);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error(String msg) {
        return new Result<>(200, msg);
    }

    /**
     * 失败响应（带状态码）
     */
    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<>(code, msg);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
