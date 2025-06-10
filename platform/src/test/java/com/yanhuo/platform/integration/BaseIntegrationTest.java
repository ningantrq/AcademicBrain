package com.yanhuo.platform.integration;

import com.yanhuo.platform.util.TestReportGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, TestReportGenerator.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("integration-test")
public abstract class BaseIntegrationTest {

    @AfterAll
    static void generateTestReport() {
        TestReportGenerator.generateReport();
        System.out.println("📊 集成测试完成，报告已生成！");
    }
} 