package com.yanhuo.platform.test;

import com.yanhuo.platform.TestApplication;
import com.yanhuo.platform.config.TestRedisConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基础测试类
 * 提供测试的通用配置和工具方法
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
@Transactional  // 每个测试方法执行后回滚
@Import(TestRedisConfig.class)
public abstract class BaseTest {
    
    /**
     * 测试用户ID
     */
    protected static final String TEST_USER_ID = "1";
    
    /**
     * 测试用户名
     */
    protected static final String TEST_USERNAME = "testuser";
    
    /**
     * 测试邮箱
     */
    protected static final String TEST_EMAIL = "test@example.com";
    
    /**
     * 获取测试JWT Token
     */
    protected String getTestToken() {
        // 这里可以生成一个测试用的JWT Token
        return "test-jwt-token";
    }
} 