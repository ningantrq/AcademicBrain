package com.yanhuo.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanhuo.platform.TestApplication;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.platform.test.BaseTest;
import com.yanhuo.xo.entity.User;
import com.yanhuo.xo.vo.FollowerVo;
import com.yanhuo.xo.vo.NoteSearchVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserController 单元测试
 */
@DisplayName("用户控制器单元测试")
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = TestApplication.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Page<NoteSearchVo> mockNotePage;
    private Page<FollowerVo> mockFollowerPage;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testUser = new User();
        testUser.setId("1");
        testUser.setUsername("testuser");
        testUser.setDescription("Test Description");
        testUser.setEmail("test@example.com");

        // 模拟分页数据
        mockNotePage = new Page<>();
        mockFollowerPage = new Page<>();
    }

    @Test
    @DisplayName("获取用户动态分页 - 成功")
    void getTrendPageByUser_Success() throws Exception {
        // Given
        when(userService.getTrendPageByUser(anyLong(), anyLong(), anyString(), anyInt()))
                .thenReturn(mockNotePage);

        // When & Then
        mockMvc.perform(get("/user/getTrendPageByUser/1/10")
                        .param("userId", "1")
                        .param("type", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true));

        verify(userService).getTrendPageByUser(1L, 10L, "1", 1);
    }

    @Test
    @DisplayName("根据ID获取用户信息 - 成功")
    void getUserById_Success() throws Exception {
        // Given
        when(userService.getById(anyString())).thenReturn(testUser);

        // When & Then
        mockMvc.perform(get("/user/getUserById")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.data.username").value("testuser"));

        verify(userService).getById("1");
    }

    @Test
    @DisplayName("更新用户信息 - 成功")
    void updateUser_Success() throws Exception {
        // Given
        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setDescription("Updated Description");
        
        when(userService.updateUser(any(User.class))).thenReturn(updatedUser);

        // When & Then
        mockMvc.perform(post("/user/updateUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true));

        verify(userService).updateUser(any(User.class));
    }

    @Test
    @DisplayName("根据关键词搜索用户 - 成功")
    void getUserPageByKeyword_Success() throws Exception {
        // Given
        when(userService.getUserPageByKeyword(anyLong(), anyLong(), anyString()))
                .thenReturn(mockFollowerPage);

        // When & Then
        mockMvc.perform(get("/user/getUserPageByKeyword/1/10")
                        .param("keyword", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true));

        verify(userService).getUserPageByKeyword(1L, 10L, "test");
    }

    @Test
    @DisplayName("保存用户搜索记录 - 成功")
    void saveUserSearchRecord_Success() throws Exception {
        // Given
        doNothing().when(userService).saveUserSearchRecord(anyString());

        // When & Then
        mockMvc.perform(get("/user/saveUserSearchRecord")
                        .param("keyword", "test keyword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true));

        verify(userService).saveUserSearchRecord("test keyword");
    }

    @Test
    @DisplayName("更新用户信息 - 无效JSON格式")
    void updateUser_InvalidJson() throws Exception {
        // When & Then
        mockMvc.perform(post("/user/updateUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("获取用户动态 - 服务层异常")
    void getTrendPageByUser_ServiceException() throws Exception {
        // Given
        when(userService.getTrendPageByUser(anyLong(), anyLong(), anyString(), anyInt()))
                .thenThrow(new RuntimeException("Service error"));

        // When & Then
        mockMvc.perform(get("/user/getTrendPageByUser/1/10")
                        .param("userId", "1")
                        .param("type", "1"))
                .andExpect(status().isInternalServerError());
    }
} 