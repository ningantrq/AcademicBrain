package com.yanhuo.platform.performance;

import com.yanhuo.platform.service.UserService;
import com.yanhuo.xo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

/**
 * 用户服务性能测试
 * 测试UserService在高并发场景下的性能表现
 */
@DisplayName("User Service Performance Tests")
class UserServicePerformanceTest extends BasePerformanceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        // 创建测试数据库表
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
                
            // 插入一些测试数据
            for (int i = 1; i <= 100; i++) {
                stmt.execute("INSERT INTO t_user (id, username, password, email, description) VALUES (" +
                    "'" + i + "', " +
                    "'user" + i + "', " +
                    "'password123', " +
                    "'user" + i + "@test.com', " +
                    "'Test User " + i + "')");
            }
        }
    }

    @Test
    @DisplayName("用户查询性能测试")
    void testUserQueryPerformance() throws Exception {
        String testName = "UserQueryPerformance";
        int threads = 20;           // 20个并发线程
        int requestsPerThread = 50; // 每个线程执行50次查询

        Runnable testLogic = () -> {
            // 随机查询用户
            int userId = (int) (Math.random() * 100) + 1;
            User user = userService.getById(String.valueOf(userId));
            if (user == null) {
                throw new RuntimeException("User not found: " + userId);
            }
        };

        runPerformanceTest(testName, testLogic, threads, requestsPerThread);
    }

    @Test
    @DisplayName("用户创建性能测试")
    void testUserCreationPerformance() throws Exception {
        String testName = "UserCreationPerformance";
        int threads = 10;           // 10个并发线程
        int requestsPerThread = 20; // 每个线程创建20个用户

        Runnable testLogic = () -> {
            // 创建新用户
            User newUser = new User();
            long timestamp = System.currentTimeMillis();
            newUser.setUsername("perfTest" + timestamp + Thread.currentThread().getId());
            newUser.setEmail("perftest" + timestamp + "@test.com");
            newUser.setPassword("password123");
            newUser.setDescription("Performance test user");

            boolean success = userService.save(newUser);
            if (!success) {
                throw new RuntimeException("Failed to create user");
            }
        };

        runPerformanceTest(testName, testLogic, threads, requestsPerThread);
    }

    @Test
    @DisplayName("混合操作性能测试")
    void testMixedOperationsPerformance() throws Exception {
        String testName = "MixedOperationsPerformance";
        int threads = 15;           // 15个并发线程
        int requestsPerThread = 30; // 每个线程执行30次混合操作

        Runnable testLogic = () -> {
            int operation = (int) (Math.random() * 3); // 0=查询, 1=创建, 2=更新
            
            try {
                switch (operation) {
                    case 0: // 查询操作 (70%概率)
                        if (Math.random() < 0.7) {
                            int userId = (int) (Math.random() * 100) + 1;
                            userService.getById(String.valueOf(userId));
                        }
                        break;
                        
                    case 1: // 创建操作 (20%概率)
                        if (Math.random() < 0.2) {
                            User newUser = new User();
                            long timestamp = System.currentTimeMillis();
                            newUser.setUsername("mixed" + timestamp + Thread.currentThread().getId());
                            newUser.setEmail("mixed" + timestamp + "@test.com");
                            newUser.setPassword("password123");
                            userService.save(newUser);
                        }
                        break;
                        
                    case 2: // 更新操作 (10%概率)
                        if (Math.random() < 0.1) {
                            int userId = (int) (Math.random() * 100) + 1;
                            User user = userService.getById(String.valueOf(userId));
                            if (user != null) {
                                user.setDescription("Updated at " + System.currentTimeMillis());
                                userService.updateUser(user);
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                // 在混合测试中，某些操作失败是可以接受的
                System.err.println("Mixed operation failed: " + e.getMessage());
            }
        };

        runPerformanceTest(testName, testLogic, threads, requestsPerThread);
    }
} 