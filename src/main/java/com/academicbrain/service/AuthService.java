package com.academicbrain.service;

import com.academicbrain.dto.UserDTO;
import com.academicbrain.model.User;

import java.util.Map;

/**
 * 认证服务接口
 * @author AcademicBrain
 */
public interface AuthService {

    /**
     * 用户登录
     * @param userDTO 用户登录信息
     * @return 登录结果，包含token和用户信息
     */
    Map<String, Object> login(UserDTO userDTO);

    /**
     * 用户注册
     * @param userDTO 用户注册信息
     * @return 注册结果
     */
    Map<String, Object> register(UserDTO userDTO);

    /**
     * 根据token获取用户信息
     * @param token JWT token
     * @return 用户信息
     */
    User getUserByToken(String token);

    /**
     * 用户登出
     * @param userId 用户ID
     */
    void logout(String userId);

    /**
     * 刷新token
     * @param refreshToken 刷新token
     * @return 新的token信息
     */
    Map<String, Object> refreshToken(String refreshToken);
} 