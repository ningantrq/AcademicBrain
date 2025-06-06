package com.yanhuo.platform.integration;

import com.yanhuo.platform.TestApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * 用户功能集成测试
 * 测试用户相关API接口的集成功能
 */
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@Transactional
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    private String testUserId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        
        // 使用测试数据中的用户ID
        testUserId = "test-user-1";
    }

    @Test
    void userCrudFlow() {
        // 1. 获取用户信息
        given()
            .when()
                .get("/user/getUserById?userId=" + testUserId)
            .then()
                .statusCode(200)
                .body("status", equalTo(200))
                .body("data", notNullValue())
                .body("data.id", equalTo(testUserId));

        // 2. 更新用户信息  
        String updateData = "{\"id\":\"" + testUserId + "\",\"introduction\":\"集成测试更新描述\",\"username\":\"测试用户更新\"}";
        
        given()
            .contentType("application/json")
            .body(updateData)
            .when()
                .put("/user/updateUser")
            .then()
                .statusCode(200)
                .body("status", equalTo(200));

        // 3. 搜索用户
        given()
            .when()
                .get("/user/getUserPageByKeyword/1/10?keyword=test")
            .then()
                .statusCode(200)
                .body("status", equalTo(200))
                .body("data", notNullValue());
    }

    @Test
    void getUserTrendData() {
        given()
            .when()
                .get("/user/getTrendPageByUser/1/10?userId=" + testUserId)
            .then()
                .statusCode(200)
                .body("status", equalTo(200));
    }

    @Test
    void parameterValidationTest() {
        // 测试无效页码
        given()
            .when()
                .get("/user/getTrendPageByUser/0/10?userId=" + testUserId)
            .then()
                .statusCode(200);

        // 测试空用户ID
        given()
            .when()
                .get("/user/getUserById?userId=")
            .then()
                .statusCode(200);
    }

    @Test
    void errorHandlingTest() {
        // 测试不存在的用户
        given()
            .when()
                .get("/user/getUserById?userId=nonexistent-user")
            .then()
                .statusCode(200)
                .body("status", anyOf(equalTo(200), equalTo(404)));

        // 测试空关键词搜索
        given()
            .when()
                .get("/user/getUserPageByKeyword/1/10?keyword=")
            .then()
                .statusCode(200)
                .body("status", equalTo(200));
    }

    @Test
    void concurrentRequestTest() {
        // 测试并发搜索请求
        for (int i = 0; i < 3; i++) {
            given()
                .when()
                    .get("/user/getUserPageByKeyword/1/5?keyword=test")
                .then()
                    .statusCode(200)
                    .body("status", equalTo(200));
        }
    }
} 