package com.yanhuo.platform.unit;

import com.yanhuo.platform.util.TestReportGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith({MockitoExtension.class, TestReportGenerator.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
    "xxl.job.executor.enabled=false",
    "spring.cloud.nacos.discovery.enabled=false",
    "spring.cloud.nacos.config.enabled=false"
})
@ActiveProfiles("unit-test")
public abstract class BaseUnitTest {
    
    @AfterAll
    static void generateTestReport() {
        TestReportGenerator.generateReport();
        System.out.println("📊 单元测试完成，报告已生成！");
    }
    
    // Common test utilities and helper methods can be added here
} 