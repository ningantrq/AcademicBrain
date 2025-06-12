package com.yanhuo.platform.acceptance;

import com.yanhuo.platform.util.AcceptanceTestReportGenerator;
import io.cucumber.java.AfterAll;

/**
 * 验收测试报告处理器
 * 在所有测试完成后生成报告
 */
public class AcceptanceTestReportHandler {
    
    @AfterAll
    public static void generateFinalReport() {
        System.out.println("🎭 正在生成验收测试报告...");
        AcceptanceTestReportGenerator.generateReport();
        System.out.println("�� 验收测试完成！");
    }
} 