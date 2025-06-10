package com.yanhuo.platform.integration;

import com.yanhuo.platform.config.IntegrationTestConfig;
import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("integration-test")
@Import(IntegrationTestConfig.class)
@DisplayName("Simple Integration Tests")
public class SimpleIntegrationTest {

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
    @DisplayName("Should create and retrieve user in integration test")
    @Transactional
    @Rollback
    void shouldCreateAndRetrieveUser() {
        // Given
        User user = new User();
        user.setUsername("integrationUser");
        user.setEmail("integration@test.com");
        user.setDescription("Integration Test User");
        user.setPassword("integrationPassword");

        // When
        boolean saveResult = userService.save(user);

        // Then
        assertThat(saveResult).isTrue();
        assertThat(user.getId()).isNotNull();

        // Verify retrieval
        User retrievedUser = userService.getById(user.getId());
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getUsername()).isEqualTo("integrationUser");
        assertThat(retrievedUser.getEmail()).isEqualTo("integration@test.com");
    }

    @Test
    @DisplayName("Should handle database operations correctly")
    @Transactional
    @Rollback
    void shouldHandleDatabaseOperations() {
        // Given
        User user1 = createTestUser("dbUser1", "db1@test.com");
        User user2 = createTestUser("dbUser2", "db2@test.com");

        // When
        boolean save1 = userService.save(user1);
        boolean save2 = userService.save(user2);

        // Then
        assertThat(save1).isTrue();
        assertThat(save2).isTrue();

        // Verify both users exist
        User retrieved1 = userService.getById(user1.getId());
        User retrieved2 = userService.getById(user2.getId());

        assertThat(retrieved1).isNotNull();
        assertThat(retrieved2).isNotNull();
        assertThat(retrieved1.getUsername()).isEqualTo("dbUser1");
        assertThat(retrieved2.getUsername()).isEqualTo("dbUser2");
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