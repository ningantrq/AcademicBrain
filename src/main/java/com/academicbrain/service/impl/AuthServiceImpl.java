package com.academicbrain.service.impl;

import com.academicbrain.dto.UserDTO;
import com.academicbrain.model.User;
import com.academicbrain.repository.UserRepository;
import com.academicbrain.service.AuthService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

        // 验证密码 - 改为直接比较明文密码
        if (!userDTO.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != null && user.getStatus() == 1) {
            throw new RuntimeException("用户已被禁用");
        }

        // 暂时使用简单的token，避免JWT问题
        String accessToken = "simple_token_" + user.getId() + "_" + System.currentTimeMillis();
        String refreshToken = "refresh_token_" + user.getId() + "_" + System.currentTimeMillis();

        // 暂时注释掉Redis操作
        // redisTemplate.opsForValue().set("access_token:" + user.getId(), accessToken, 24, TimeUnit.HOURS);
        // redisTemplate.opsForValue().set("refresh_token:" + user.getId(), refreshToken, 7, TimeUnit.DAYS);

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
        user.setPassword(userDTO.getPassword()); // 直接存储明文密码
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setStatus(0); // 正常状态
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        // 保存用户
        userRepository.insert(user);

        // 暂时使用简单的token，避免JWT问题
        String accessToken = "simple_token_" + user.getId() + "_" + System.currentTimeMillis();
        String refreshToken = "refresh_token_" + user.getId() + "_" + System.currentTimeMillis();

        // 暂时注释掉Redis操作
        // redisTemplate.opsForValue().set("access_token:" + user.getId(), accessToken, 24, TimeUnit.HOURS);
        // redisTemplate.opsForValue().set("refresh_token:" + user.getId(), refreshToken, 7, TimeUnit.DAYS);

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
        // 暂时简化token验证，只检查token格式
        if (token == null || !token.startsWith("simple_token_")) {
            throw new RuntimeException("Token无效");
        }

        // 从简单token中提取用户ID
        String[] parts = token.split("_");
        if (parts.length < 3) {
            throw new RuntimeException("Token格式错误");
        }
        
        String userId = parts[2];
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
        // 暂时注释掉Redis操作
        // redisTemplate.delete("access_token:" + userId);
        // redisTemplate.delete("refresh_token:" + userId);
        
        // 在简化版本中，logout只是一个空操作
        System.out.println("用户 " + userId + " 已登出");
    }

    @Override
    public Map<String, Object> refreshToken(String refreshToken) {
        // 暂时简化token验证
        if (refreshToken == null || !refreshToken.startsWith("refresh_token_")) {
            throw new RuntimeException("Refresh Token无效");
        }

        // 从简单token中提取用户ID
        String[] parts = refreshToken.split("_");
        if (parts.length < 3) {
            throw new RuntimeException("Token格式错误");
        }
        
        String userId = parts[2];
        User user = userRepository.selectById(userId);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 生成新的简单token
        String newAccessToken = "simple_token_" + user.getId() + "_" + System.currentTimeMillis();
        String newRefreshToken = "refresh_token_" + user.getId() + "_" + System.currentTimeMillis();

        // 暂时注释掉Redis操作
        // redisTemplate.opsForValue().set("access_token:" + user.getId(), newAccessToken, 24, TimeUnit.HOURS);
        // redisTemplate.opsForValue().set("refresh_token:" + user.getId(), newRefreshToken, 7, TimeUnit.DAYS);

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", newAccessToken);
        result.put("refreshToken", newRefreshToken);

        return result;
    }
} 