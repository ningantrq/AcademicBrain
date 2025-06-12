package com.yanhuo.platform.acceptance;

import com.yanhuo.platform.config.IntegrationTestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
 
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "xxl.job.executor.enabled=false",
    "spring.cloud.nacos.discovery.enabled=false",
    "spring.cloud.nacos.config.enabled=false"
})
@ActiveProfiles("integration-test")
@Import(IntegrationTestConfig.class)
public class CucumberSpringConfiguration {
} 