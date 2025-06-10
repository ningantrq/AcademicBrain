package com.yanhuo.platform.integration.controller;

import com.yanhuo.platform.integration.BaseIntegrationTest;
import com.yanhuo.xo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void whenGetUser_thenReturnUser() {
        // Arrange
        String userId = "1";

        // Act
        ResponseEntity<User> response = restTemplate.getForEntity("/platform/user/{id}", User.class, userId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(userId);
    }
} 