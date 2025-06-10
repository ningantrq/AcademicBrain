package com.yanhuo.platform.util;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义测试报告生成器
 * 生成详细的HTML格式测试报告
 */
public class TestReportGenerator implements TestWatcher {
    
    private static final List<TestResult> testResults = new ArrayList<>();
    private static final String REPORT_DIR = "target/custom-test-reports";
    
    @Override
    public void testSuccessful(ExtensionContext context) {
        testResults.add(new TestResult(
            context.getDisplayName(),
            context.getTestClass().map(Class::getSimpleName).orElse("Unknown"),
            "PASSED",
            null,
            LocalDateTime.now()
        ));
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        testResults.add(new TestResult(
            context.getDisplayName(),
            context.getTestClass().map(Class::getSimpleName).orElse("Unknown"),
            "FAILED",
            cause.getMessage(),
            LocalDateTime.now()
        ));
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        testResults.add(new TestResult(
            context.getDisplayName(),
            context.getTestClass().map(Class::getSimpleName).orElse("Unknown"),
            "SKIPPED",
            cause != null ? cause.getMessage() : "Test was aborted",
            LocalDateTime.now()
        ));
    }

    /**
     * 生成HTML测试报告
     */
    public static void generateReport() {
        try {
            Path reportDir = Paths.get(REPORT_DIR);
            Files.createDirectories(reportDir);
            
            String reportContent = generateHtmlReport();
            
            try (OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(reportDir.resolve("test-report.html").toFile()), 
                    StandardCharsets.UTF_8)) {
                writer.write(reportContent);
            }
            
            System.out.println("✅ 详细测试报告已生成: " + reportDir.resolve("test-report.html").toAbsolutePath());
            
        } catch (IOException e) {
            System.err.println("❌ 生成测试报告失败: " + e.getMessage());
        }
    }

    private static String generateHtmlReport() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime reportTime = LocalDateTime.now();
        
        long passedCount = testResults.stream().mapToLong(r -> "PASSED".equals(r.status) ? 1 : 0).sum();
        long failedCount = testResults.stream().mapToLong(r -> "FAILED".equals(r.status) ? 1 : 0).sum();
        long skippedCount = testResults.stream().mapToLong(r -> "SKIPPED".equals(r.status) ? 1 : 0).sum();
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
            .append("<html lang='zh-CN'>\n")
            .append("<head>\n")
            .append("    <meta charset='UTF-8'>\n")
            .append("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n")
            .append("    <title>测试报告 - 烟火平台</title>\n")
            .append("    <style>\n")
            .append(getReportStyles())
            .append("    </style>\n")
            .append("</head>\n")
            .append("<body>\n")
            .append("    <div class='container'>\n")
            .append("        <header class='report-header'>\n")
            .append("            <h1>🔥 烟火平台测试报告</h1>\n")
            .append("            <p class='report-time'>报告生成时间: ").append(reportTime.format(formatter)).append("</p>\n")
            .append("        </header>\n")
            .append("        \n")
            .append("        <div class='summary-cards'>\n")
            .append("            <div class='summary-card total'>\n")
            .append("                <h3>总测试数</h3>\n")
            .append("                <span class='number'>").append(testResults.size()).append("</span>\n")
            .append("            </div>\n")
            .append("            <div class='summary-card passed'>\n")
            .append("                <h3>✅ 通过</h3>\n")
            .append("                <span class='number'>").append(passedCount).append("</span>\n")
            .append("            </div>\n")
            .append("            <div class='summary-card failed'>\n")
            .append("                <h3>❌ 失败</h3>\n")
            .append("                <span class='number'>").append(failedCount).append("</span>\n")
            .append("            </div>\n")
            .append("            <div class='summary-card skipped'>\n")
            .append("                <h3>⏭️ 跳过</h3>\n")
            .append("                <span class='number'>").append(skippedCount).append("</span>\n")
            .append("            </div>\n")
            .append("        </div>\n")
            .append("        \n")
            .append("        <div class='test-results'>\n")
            .append("            <h2>📊 详细测试结果</h2>\n")
            .append("            <table class='results-table'>\n")
            .append("                <thead>\n")
            .append("                    <tr>\n")
            .append("                        <th>测试类</th>\n")
            .append("                        <th>测试方法</th>\n")
            .append("                        <th>状态</th>\n")
            .append("                        <th>执行时间</th>\n")
            .append("                        <th>错误信息</th>\n")
            .append("                    </tr>\n")
            .append("                </thead>\n")
            .append("                <tbody>\n");
        
        for (TestResult result : testResults) {
            String statusClass = result.status.toLowerCase();
            String statusIcon = getStatusIcon(result.status);
            
            html.append("                    <tr class='").append(statusClass).append("'>\n")
                .append("                        <td>").append(result.className).append("</td>\n")
                .append("                        <td>").append(result.testName).append("</td>\n")
                .append("                        <td><span class='status ").append(statusClass).append("'>")
                .append(statusIcon).append(" ").append(result.status).append("</span></td>\n")
                .append("                        <td>").append(result.executionTime.format(formatter)).append("</td>\n")
                .append("                        <td>").append(result.errorMessage != null ? result.errorMessage : "-").append("</td>\n")
                .append("                    </tr>\n");
        }
        
        html.append("                </tbody>\n")
            .append("            </table>\n")
            .append("        </div>\n")
            .append("        \n")
            .append("        <footer class='report-footer'>\n")
            .append("            <p>📈 测试覆盖率报告: <a href='../site/jacoco/index.html'>JaCoCo Coverage Report</a></p>\n")
            .append("            <p>📋 Maven Surefire报告: <a href='../surefire-reports/index.html'>Surefire Reports</a></p>\n")
            .append("        </footer>\n")
            .append("    </div>\n")
            .append("    \n")
            .append("    <script>\n")
            .append(getReportScript())
            .append("    </script>\n")
            .append("</body>\n")
            .append("</html>");
        
        return html.toString();
    }

    private static String getStatusIcon(String status) {
        switch (status) {
            case "PASSED": return "✅";
            case "FAILED": return "❌";
            case "SKIPPED": return "⏭️";
            default: return "❓";
        }
    }

    private static String getReportStyles() {
        return "* {\n" +
                "    margin: 0;\n" +
                "    padding: 0;\n" +
                "    box-sizing: border-box;\n" +
                "}\n" +
                "\n" +
                "body {\n" +
                "    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "    line-height: 1.6;\n" +
                "    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                "    min-height: 100vh;\n" +
                "    padding: 20px;\n" +
                "}\n" +
                "\n" +
                ".container {\n" +
                "    max-width: 1200px;\n" +
                "    margin: 0 auto;\n" +
                "    background: white;\n" +
                "    border-radius: 10px;\n" +
                "    box-shadow: 0 15px 35px rgba(0,0,0,0.1);\n" +
                "    overflow: hidden;\n" +
                "}\n" +
                "\n" +
                ".report-header {\n" +
                "    background: linear-gradient(135deg, #ff6b6b, #ee5a24);\n" +
                "    color: white;\n" +
                "    padding: 30px;\n" +
                "    text-align: center;\n" +
                "}\n" +
                "\n" +
                ".report-header h1 {\n" +
                "    font-size: 2.5em;\n" +
                "    margin-bottom: 10px;\n" +
                "}\n" +
                "\n" +
                ".report-time {\n" +
                "    opacity: 0.9;\n" +
                "    font-size: 1.1em;\n" +
                "}\n" +
                "\n" +
                ".summary-cards {\n" +
                "    display: grid;\n" +
                "    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));\n" +
                "    gap: 20px;\n" +
                "    padding: 30px;\n" +
                "    background: #f8f9fa;\n" +
                "}\n" +
                "\n" +
                ".summary-card {\n" +
                "    background: white;\n" +
                "    padding: 25px;\n" +
                "    border-radius: 8px;\n" +
                "    text-align: center;\n" +
                "    box-shadow: 0 4px 6px rgba(0,0,0,0.1);\n" +
                "    transition: transform 0.3s ease;\n" +
                "}\n" +
                "\n" +
                ".summary-card:hover {\n" +
                "    transform: translateY(-5px);\n" +
                "}\n" +
                "\n" +
                ".summary-card h3 {\n" +
                "    color: #666;\n" +
                "    font-size: 1em;\n" +
                "    margin-bottom: 10px;\n" +
                "}\n" +
                "\n" +
                ".summary-card .number {\n" +
                "    font-size: 2.5em;\n" +
                "    font-weight: bold;\n" +
                "    display: block;\n" +
                "}\n" +
                "\n" +
                ".summary-card.total .number { color: #3498db; }\n" +
                ".summary-card.passed .number { color: #27ae60; }\n" +
                ".summary-card.failed .number { color: #e74c3c; }\n" +
                ".summary-card.skipped .number { color: #f39c12; }\n" +
                "\n" +
                ".test-results {\n" +
                "    padding: 30px;\n" +
                "}\n" +
                "\n" +
                ".test-results h2 {\n" +
                "    margin-bottom: 20px;\n" +
                "    color: #333;\n" +
                "}\n" +
                "\n" +
                ".results-table {\n" +
                "    width: 100%;\n" +
                "    border-collapse: collapse;\n" +
                "    background: white;\n" +
                "    border-radius: 8px;\n" +
                "    overflow: hidden;\n" +
                "    box-shadow: 0 4px 6px rgba(0,0,0,0.1);\n" +
                "}\n" +
                "\n" +
                ".results-table th,\n" +
                ".results-table td {\n" +
                "    padding: 15px;\n" +
                "    text-align: left;\n" +
                "    border-bottom: 1px solid #eee;\n" +
                "}\n" +
                "\n" +
                ".results-table th {\n" +
                "    background: #34495e;\n" +
                "    color: white;\n" +
                "    font-weight: 600;\n" +
                "}\n" +
                "\n" +
                ".results-table tr:hover {\n" +
                "    background: #f8f9fa;\n" +
                "}\n" +
                "\n" +
                ".results-table tr.passed {\n" +
                "    border-left: 4px solid #27ae60;\n" +
                "}\n" +
                "\n" +
                ".results-table tr.failed {\n" +
                "    border-left: 4px solid #e74c3c;\n" +
                "}\n" +
                "\n" +
                ".results-table tr.skipped {\n" +
                "    border-left: 4px solid #f39c12;\n" +
                "}\n" +
                "\n" +
                ".status {\n" +
                "    padding: 5px 12px;\n" +
                "    border-radius: 20px;\n" +
                "    font-size: 0.9em;\n" +
                "    font-weight: 600;\n" +
                "}\n" +
                "\n" +
                ".status.passed {\n" +
                "    background: #d4edda;\n" +
                "    color: #155724;\n" +
                "}\n" +
                "\n" +
                ".status.failed {\n" +
                "    background: #f8d7da;\n" +
                "    color: #721c24;\n" +
                "}\n" +
                "\n" +
                ".status.skipped {\n" +
                "    background: #fff3cd;\n" +
                "    color: #856404;\n" +
                "}\n" +
                "\n" +
                ".report-footer {\n" +
                "    background: #2c3e50;\n" +
                "    color: white;\n" +
                "    padding: 20px 30px;\n" +
                "    text-align: center;\n" +
                "}\n" +
                "\n" +
                ".report-footer a {\n" +
                "    color: #3498db;\n" +
                "    text-decoration: none;\n" +
                "}\n" +
                "\n" +
                ".report-footer a:hover {\n" +
                "    text-decoration: underline;\n" +
                "}";
    }

    private static String getReportScript() {
        return "// 添加一些交互功能\n" +
                "document.addEventListener('DOMContentLoaded', function() {\n" +
                "    // 点击汇总卡片时滚动到相应的测试结果\n" +
                "    const cards = document.querySelectorAll('.summary-card');\n" +
                "    cards.forEach(card => {\n" +
                "        card.addEventListener('click', function() {\n" +
                "            document.querySelector('.test-results').scrollIntoView({\n" +
                "                behavior: 'smooth'\n" +
                "            });\n" +
                "        });\n" +
                "    });\n" +
                "    \n" +
                "    // 为表格行添加点击高亮效果\n" +
                "    const rows = document.querySelectorAll('.results-table tbody tr');\n" +
                "    rows.forEach(row => {\n" +
                "        row.addEventListener('click', function() {\n" +
                "            rows.forEach(r => r.style.backgroundColor = '');\n" +
                "            this.style.backgroundColor = '#e3f2fd';\n" +
                "        });\n" +
                "    });\n" +
                "});";
    }

    /**
     * 测试结果数据类
     */
    private static class TestResult {
        final String testName;
        final String className;
        final String status;
        final String errorMessage;
        final LocalDateTime executionTime;

        TestResult(String testName, String className, String status, String errorMessage, LocalDateTime executionTime) {
            this.testName = testName;
            this.className = className;
            this.status = status;
            this.errorMessage = errorMessage;
            this.executionTime = executionTime;
        }
    }
} 