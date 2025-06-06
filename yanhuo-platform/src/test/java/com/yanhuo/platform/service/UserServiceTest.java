package com.yanhuo.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanhuo.platform.config.TestRedisConfig;
import com.yanhuo.platform.test.BaseTest;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.FollowerVo;
import com.yanhuo.xo.vo.NoteSearchVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * UserService 单元测试
 */
@DisplayName("用户服务单元测试")
@Import(TestRedisConfig.class)
class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 初始化测试数据
        testUser = new User();
        testUser.setId(TEST_USER_ID);
        testUser.setUsername(TEST_USERNAME);
        testUser.setEmail(TEST_EMAIL);
        testUser.setDescription("Test Description");
        testUser.setAvatar("test-avatar.jpg");
    }

    @Test
    @DisplayName("获取用户动态分页数据 - 成功")
    void getTrendPageByUser_Success() {
        // Given
        long currentPage = 1;
        long pageSize = 10;
        String userId = TEST_USER_ID;
        Integer type = 1;

        // When
        Page<NoteSearchVo> result = userService.getTrendPageByUser(currentPage, pageSize, userId, type);

        // Then
        assertNotNull(result);
        assertTrue(result.getSize() >= 0);
    }

    @Test
    @DisplayName("更新用户信息 - 成功")
    void updateUser_Success() {
        // Given - 先保存一个用户
        User existingUser = new User();
        existingUser.setId(TEST_USER_ID);
        existingUser.setUsername(TEST_USERNAME);
        existingUser.setEmail(TEST_EMAIL);
        existingUser.setDescription("Original Description");
        existingUser.setAvatar("original-avatar.jpg");
        
        // 使用DAO直接保存用户（绕过service层的业务逻辑）
        userService.save(existingUser);
        
        // 准备更新数据
        User updateUser = new User();
        updateUser.setId(TEST_USER_ID);
        updateUser.setDescription("Updated Description");
        updateUser.setAvatar("updated-avatar.jpg");

        // When
        User result = userService.updateUser(updateUser);

        // Then
        assertNotNull(result);
        assertEquals("Updated Description", result.getDescription());
        assertEquals("updated-avatar.jpg", result.getAvatar());
    }

    @Test
    @DisplayName("根据关键词搜索用户 - 成功")
    void getUserPageByKeyword_Success() {
        // Given
        long currentPage = 1;
        long pageSize = 10;
        String keyword = "test";

        // When
        Page<FollowerVo> result = userService.getUserPageByKeyword(currentPage, pageSize, keyword);

        // Then
        assertNotNull(result);
        assertTrue(result.getSize() >= 0);
    }

    @Test
    @DisplayName("保存用户搜索记录 - 成功")
    void saveUserSearchRecord_Success() {
        // Given
        String keyword = "test search";

        // When & Then - 不应该抛出异常
        assertDoesNotThrow(() -> userService.saveUserSearchRecord(keyword));
    }

    @Test
    @DisplayName("获取用户动态 - 参数验证")
    void getTrendPageByUser_InvalidParameters() {
        // Given
        long currentPage = -1;
        long pageSize = 0;
        String userId = "";
        Integer type = null;

        // When & Then
        assertDoesNotThrow(() -> userService.getTrendPageByUser(currentPage, pageSize, userId, type));
    }

    @Test
    @DisplayName("更新用户信息 - 空用户对象")
    void updateUser_NullUser() {
        // Given
        User nullUser = null;

        // When & Then
        assertThrows(Exception.class, () -> userService.updateUser(nullUser));
    }
} 