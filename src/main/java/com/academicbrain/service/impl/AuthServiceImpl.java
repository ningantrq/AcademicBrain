package com.academicbrain.service.impl;

import com.academicbrain.dto.UserDTO;
import com.academicbrain.model.User;
import com.academicbrain.repository.UserRepository;
import com.academicbrain.service.AuthService;
import com.academicbrain.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.hutool.crypto.digest.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 * @author AcademicBrain
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Map<String, Object> login(UserDTO userDTO) {
        // 参数校验
        if (!StringUtils.hasText(userDTO.getUsername()) || !StringUtils.hasText(userDTO.getPassword())) {
            throw new RuntimeException("用户名或密码不能为空");
        }

        // 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        User user = userRepository.selectOne(queryWrapper);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        if (!BCrypt.checkpw(userDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != null && user.getStatus() == 1) {
            throw new RuntimeException("用户已被禁用");
        }

        // 生成token
        String accessToken = JwtUtils.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = JwtUtils.generateRefreshToken(user.getId());

        // 将token存储到Redis
        redisTemplate.opsForValue().set("access_token:" + user.getId(), accessToken, 24, TimeUnit.HOURS);
        redisTemplate.opsForValue().set("refresh_token:" + user.getId(), refreshToken, 7, TimeUnit.DAYS);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        
        // 用户信息（不包含密码）
        User userInfo = new User();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setDescription(user.getDescription());
        
        result.put("userInfo", userInfo);

        return result;
    }

    @Override
    public Map<String, Object> register(UserDTO userDTO) {
        // 参数校验
        if (!StringUtils.hasText(userDTO.getUsername()) || !StringUtils.hasText(userDTO.getPassword())) {
            throw new RuntimeException("用户名或密码不能为空");
        }

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new RuntimeException("两次密码输入不一致");
        }

        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        User existUser = userRepository.selectOne(queryWrapper);

        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt()));
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setStatus(0); // 正常状态
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        // 保存用户
        userRepository.insert(user);

        // 生成token
        String accessToken = JwtUtils.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = JwtUtils.generateRefreshToken(user.getId());

        // 将token存储到Redis
        redisTemplate.opsForValue().set("access_token:" + user.getId(), accessToken, 24, TimeUnit.HOURS);
        redisTemplate.opsForValue().set("refresh_token:" + user.getId(), refreshToken, 7, TimeUnit.DAYS);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        
        // 用户信息（不包含密码）
        User userInfo = new User();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        
        result.put("userInfo", userInfo);

        return result;
    }

    @Override
    public User getUserByToken(String token) {
        if (!JwtUtils.validateToken(token)) {
            throw new RuntimeException("Token无效");
        }

        String userId = JwtUtils.getUserIdFromToken(token);
        User user = userRepository.selectById(userId);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 清除密码信息
        user.setPassword(null);
        return user;
    }

    @Override
    public void logout(String userId) {
        // 从Redis中删除token
        redisTemplate.delete("access_token:" + userId);
        redisTemplate.delete("refresh_token:" + userId);
    }

    @Override
    public Map<String, Object> refreshToken(String refreshToken) {
        if (!JwtUtils.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh Token无效");
        }

        String userId = JwtUtils.getUserIdFromToken(refreshToken);
        User user = userRepository.selectById(userId);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 生成新的token
        String newAccessToken = JwtUtils.generateAccessToken(user.getId(), user.getUsername());
        String newRefreshToken = JwtUtils.generateRefreshToken(user.getId());

        // 更新Redis中的token
        redisTemplate.opsForValue().set("access_token:" + user.getId(), newAccessToken, 24, TimeUnit.HOURS);
        redisTemplate.opsForValue().set("refresh_token:" + user.getId(), newRefreshToken, 7, TimeUnit.DAYS);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", newAccessToken);
        result.put("refreshToken", newRefreshToken);

        return result;
    }
} 