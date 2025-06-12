package com.yanhuo.platform.integration.service;

import com.yanhuo.platform.config.IntegrationTestConfig;
import com.yanhuo.platform.integration.BaseIntegrationTest;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Service Integration Tests")
@Import(IntegrationTestConfig.class)
public class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        // 手动创建表结构
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 删除已存在的表
            stmt.execute("DROP TABLE IF EXISTS t_user");
            
            // 创建用户表
            stmt.execute("CREATE TABLE t_user (" +
                "id VARCHAR(50) PRIMARY KEY, " +
                "yx_id BIGINT, " +
                "username VARCHAR(100), " +
                "password VARCHAR(255), " +
                "avatar VARCHAR(500), " +
                "gender INTEGER, " +
                "phone VARCHAR(20), " +
                "email VARCHAR(100), " +
                "description TEXT, " +
                "status INTEGER DEFAULT 1, " +
                "birthday VARCHAR(20), " +
                "address VARCHAR(500), " +
                "user_cover VARCHAR(500), " +
                "trend_count BIGINT DEFAULT 0, " +
                "follower_count BIGINT DEFAULT 0, " +
                "fan_count BIGINT DEFAULT 0, " +
                "creator VARCHAR(50), " +
                "create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "updater VARCHAR(50), " +
                "update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");
        }
    }

    @Test
    @DisplayName("Should create and retrieve user")
    @Transactional
    @Rollback
    void shouldCreateAndRetrieveUser() {
        // Given
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setDescription("Test User Description");
        user.setPassword("testPassword");

        // When
        boolean saveResult = userService.save(user);

        // Then
        assertThat(saveResult).isTrue();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testUser");
        assertThat(user.getEmail()).isEqualTo("test@example.com");

        // Verify retrieval
        User retrievedUser = userService.getById(user.getId());
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getUsername()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("Should update user successfully")
    @Transactional
    @Rollback
    void shouldUpdateUserSuccessfully() {
        // Given - Create a user first
        User user = new User();
        user.setUsername("originalUser");
        user.setEmail("original@example.com");
        user.setDescription("Original User Description");
        user.setPassword("originalPassword");
        
        boolean saveResult = userService.save(user);
        assertThat(saveResult).isTrue();
        
        // When - Update the user
        user.setUsername("updatedUser");
        user.setEmail("updated@example.com");
        User updatedUser = userService.updateUser(user);

        // Then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo("updatedUser");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    @DisplayName("Should handle user list operations")
    @Transactional
    @Rollback
    void shouldHandleUserListOperations() {
        // Given
        User user1 = createTestUser("user1", "user1@test.com");
        User user2 = createTestUser("user2", "user2@test.com");
        
        // When
        boolean save1 = userService.save(user1);
        boolean save2 = userService.save(user2);
        
        // Then - Verify saves were successful
        assertThat(save1).isTrue();
        assertThat(save2).isTrue();
        
        // Verify users exist (test getById method)
        User retrieved1 = userService.getById(user1.getId());
        User retrieved2 = userService.getById(user2.getId());
        
        assertThat(retrieved1).isNotNull();
        assertThat(retrieved2).isNotNull();
        assertThat(retrieved1.getUsername()).isEqualTo("user1");
        assertThat(retrieved2.getUsername()).isEqualTo("user2");
    }

    @Test
    @DisplayName("Should validate user business logic")
    @Transactional
    @Rollback
    void shouldValidateUserBusinessLogic() {
        // Given
        User user = createTestUser("businessUser", "business@test.com");
        boolean saveResult = userService.save(user);
        assertThat(saveResult).isTrue();

        // When - Test various service methods to increase coverage
        User foundUser = userService.getById(user.getId());
        
        // Then - Verify business logic execution
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getCreateDate()).isNotNull(); // Check audit fields
        
        // Test update logic
        foundUser.setDescription("Updated Description");
        User updatedUser = userService.updateUser(foundUser);
        assertThat(updatedUser.getDescription()).isEqualTo("Updated Description");
        assertThat(updatedUser.getUpdateDate()).isNotNull(); // Check update audit
    }

    private User createTestUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setDescription(username + " Description");
        user.setPassword("testPassword123");
        return user;
    }
} 