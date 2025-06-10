package com.yanhuo.platform.unit.service;

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
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Real Logic Tests")
public class UserServiceRealTest {

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
        testUser.setDescription("Test User Description");
        testUser.setPassword("password123");
    }

    @Test
    @DisplayName("Should execute real getById logic")
    void shouldExecuteRealGetByIdLogic() {
        // Arrange
        when(userDao.selectById("1")).thenReturn(testUser);

        // Act - This will execute the real service method
        User result = userService.getById("1");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testUser");
        verify(userDao, times(1)).selectById("1");
    }

    @Test
    @DisplayName("Should execute real save logic")
    void shouldExecuteRealSaveLogic() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEmail("new@example.com");
        
        when(userDao.insert(any(User.class))).thenReturn(1);

        // Act - This will execute real service logic including validation
        boolean result = userService.save(newUser);

        // Assert
        assertThat(result).isTrue();
        verify(userDao, times(1)).insert(any(User.class));
    }

    @Test
    @DisplayName("Should execute real update logic")
    void shouldExecuteRealUpdateLogic() {
        // Arrange
        User updateUser = new User();
        updateUser.setId("1");
        updateUser.setUsername("updatedUser");
        updateUser.setEmail("updated@example.com");
        
        when(userDao.updateById(any(User.class))).thenReturn(1);
        when(userDao.selectById("1")).thenReturn(updateUser);

        // Act - This will execute real service update logic
        User result = userService.updateUser(updateUser);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("updatedUser");
        verify(userDao, times(1)).updateById(any(User.class));
        verify(userDao, times(1)).selectById("1");
    }

    @Test
    @DisplayName("Should handle null user gracefully")
    void shouldHandleNullUserGracefully() {
        // Arrange
        when(userDao.selectById("999")).thenReturn(null);

        // Act
        User result = userService.getById("999");

        // Assert
        assertThat(result).isNull();
        verify(userDao, times(1)).selectById("999");
    }

    @Test
    @DisplayName("Should validate user data in save")
    void shouldValidateUserDataInSave() {
        // Arrange
        User validUser = new User();
        validUser.setUsername("validUser");
        validUser.setEmail("valid@example.com");
        validUser.setPassword("validPassword");
        
        when(userDao.insert(any(User.class))).thenReturn(1);
        
        // Act - This executes real validation logic in the service
        boolean result = userService.save(validUser);

        // Assert
        assertThat(result).isTrue();
        verify(userDao, times(1)).insert(any(User.class));
    }
} 