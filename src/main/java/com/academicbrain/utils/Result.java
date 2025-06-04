package com.academicbrain.utils;

import lombok.Data;

/**
 * 统一结果封装类
 * @author AcademicBrain
 */
@Data
public class Result<T> {
    
    private Integer code;
    private String message;
    private T data;

    public Result() {}

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功");
    }

    /**
     * 成功响应带数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 成功响应带消息和数据
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error() {
        return new Result<>(500, "操作失败");
    }

    /**
     * 失败响应带消息
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message);
    }

    /**
     * 失败响应带状态码和消息
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message);
    }
} 