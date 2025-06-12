package com.yanhuo.platform.unit.service;

import com.yanhuo.platform.service.UserService;
import com.yanhuo.platform.service.impl.UserServiceImpl;
import com.yanhuo.xo.dao.UserDao;
import com.yanhuo.xo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("1");
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
    }

    @Test
    @DisplayName("Should return user when getting by ID")
    void whenGetUserById_thenReturnUser() {
        // Arrange
        when(userDao.selectById(any())).thenReturn(testUser);

        // Act
        User result = userService.getById("1");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testUser");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should return updated user when updating user")
    void whenUpdateUser_thenReturnUpdatedUser() {
        // Arrange
        User updateUser = new User();
        updateUser.setId("1");
        updateUser.setUsername("updatedUser");
        updateUser.setEmail("updated@example.com");
        when(userDao.updateById(any())).thenReturn(1);
        when(userDao.selectById(any())).thenReturn(updateUser);

        // Act
        User result = userService.updateUser(updateUser);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("updatedUser");
        assertThat(result.getEmail()).isEqualTo("updated@example.com");
    }
} 