package com.academicbrain.controller;

import com.academicbrain.dto.UserDTO;
import com.academicbrain.model.User;
import com.academicbrain.service.AuthService;
import com.academicbrain.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 认证控制器
 * @author AcademicBrain
 */
@Api(tags = "用户认证")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody UserDTO userDTO) {
        try {
            Map<String, Object> result = authService.login(userDTO);
            return Result.success("登录成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<Map<String, Object>> register(@Valid @RequestBody UserDTO userDTO) {
        try {
            Map<String, Object> result = authService.register(userDTO);
            return Result.success("注册成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据token获取用户信息
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/userinfo")
    public Result<User> getUserInfo(@RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            User user = authService.getUserByToken(token);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(401, e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            
            // 从简单token中提取用户ID
            if (token.startsWith("simple_token_")) {
                String[] parts = token.split("_");
                if (parts.length >= 3) {
                    String userId = parts[2];
                    authService.logout(userId);
                }
            }
            
            return Result.success("登出成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 刷新token
     */
    @ApiOperation("刷新token")
    @PostMapping("/refresh")
    public Result<Map<String, Object>> refreshToken(@RequestParam String refreshToken) {
        try {
            Map<String, Object> result = authService.refreshToken(refreshToken);
            return Result.success("刷新成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 