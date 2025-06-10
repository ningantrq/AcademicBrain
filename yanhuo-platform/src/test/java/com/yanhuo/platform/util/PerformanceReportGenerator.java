package com.yanhuo.platform.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 性能测试报告生成器
 * 用于生成美观的HTML性能测试报告
 */
public class PerformanceReportGenerator {
    
    private static final List<PerformanceTestResult> results = new ArrayList<>();
    
    public static class PerformanceTestResult {
        private String testName;
        private int threads;
        private int requestsPerThread;
        private int totalRequests;
        private int successCount;
        private int errorCount;
        private long durationMs;
        private double requestsPerSecond;
        private double avgResponseTime;
        
        public PerformanceTestResult(String testName, int threads, int requestsPerThread, 
                                   int totalRequests, int successCount, int errorCount, 
                                   long durationMs, double requestsPerSecond) {
            this.testName = testName;
            this.threads = threads;
            this.requestsPerThread = requestsPerThread;
            this.totalRequests = totalRequests;
            this.successCount = successCount;
            this.errorCount = errorCount;
            this.durationMs = durationMs;
            this.requestsPerSecond = requestsPerSecond;
            this.avgResponseTime = (double) durationMs / totalRequests;
        }
        
        // Getters
        public String getTestName() { return testName; }
        public int getThreads() { return threads; }
        public int getRequestsPerThread() { return requestsPerThread; }
        public int getTotalRequests() { return totalRequests; }
        public int getSuccessCount() { return successCount; }
        public int getErrorCount() { return errorCount; }
        public long getDurationMs() { return durationMs; }
        public double getRequestsPerSecond() { return requestsPerSecond; }
        public double getAvgResponseTime() { return avgResponseTime; }
        public double getSuccessRate() { return (double) successCount / totalRequests * 100; }
    }
    
    public static void addResult(String testName, int threads, int requestsPerThread, 
                               int totalRequests, int successCount, int errorCount, 
                               long durationMs, double requestsPerSecond) {
        results.add(new PerformanceTestResult(testName, threads, requestsPerThread, 
                                            totalRequests, successCount, errorCount, 
                                            durationMs, requestsPerSecond));
    }
    
    public static void generateReport() {
        try {
            Path reportDir = Paths.get("target/performance-reports");
            Files.createDirectories(reportDir);
            
            String reportContent = generateHtmlReport();
            
            try (OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(reportDir.resolve("performance-report.html").toFile()), 
                    StandardCharsets.UTF_8)) {
                writer.write(reportContent);
            }
            
            System.out.println("📊 性能测试报告已生成: target/performance-reports/performance-report.html");
            
        } catch (IOException e) {
            System.err.println("Failed to generate performance report: " + e.getMessage());
        }
    }
    
    private static String generateHtmlReport() {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang='zh-CN'>\n");
        html.append("<head>\n");
        html.append("    <meta charset='UTF-8'>\n");
        html.append("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
        html.append("    <title>🚀 烟火平台性能测试报告</title>\n");
        html.append("    <style>\n");
        html.append(getCssStyles());
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class='container'>\n");
        html.append("        <header class='report-header'>\n");
        html.append("            <h1>🚀 烟火平台性能测试报告</h1>\n");
        html.append("            <p class='report-time'>报告生成时间: " + 
                   LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</p>\n");
        html.append("        </header>\n");
        
        // 汇总统计
        html.append(generateSummarySection());
        
        // 详细结果
        html.append(generateDetailSection());
        
        html.append("        <footer class='report-footer'>\n");
        html.append("            <p>📈 由 PerformanceReportGenerator 自动生成</p>\n");
        html.append("        </footer>\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>\n");
        
        return html.toString();
    }
    
    private static String generateSummarySection() {
        if (results.isEmpty()) {
            return "        <div class='summary-cards'>\n" +
                   "            <div class='summary-card'>\n" +
                   "                <h3>暂无性能测试数据</h3>\n" +
                   "            </div>\n" +
                   "        </div>\n";
        }
        
        double avgTps = results.stream().mapToDouble(PerformanceTestResult::getRequestsPerSecond).average().orElse(0);
        double avgSuccessRate = results.stream().mapToDouble(PerformanceTestResult::getSuccessRate).average().orElse(0);
        int totalRequests = results.stream().mapToInt(PerformanceTestResult::getTotalRequests).sum();
        
        StringBuilder summary = new StringBuilder();
        summary.append("        <div class='summary-cards'>\n");
        summary.append("            <div class='summary-card total'>\n");
        summary.append("                <h3>总测试数</h3>\n");
        summary.append("                <span class='number'>").append(results.size()).append("</span>\n");
        summary.append("            </div>\n");
        summary.append("            <div class='summary-card tps'>\n");
        summary.append("                <h3>平均TPS</h3>\n");
        summary.append("                <span class='number'>").append(String.format("%.1f", avgTps)).append("</span>\n");
        summary.append("            </div>\n");
        summary.append("            <div class='summary-card success'>\n");
        summary.append("                <h3>平均成功率</h3>\n");
        summary.append("                <span class='number'>").append(String.format("%.1f%%", avgSuccessRate)).append("</span>\n");
        summary.append("            </div>\n");
        summary.append("            <div class='summary-card requests'>\n");
        summary.append("                <h3>总请求数</h3>\n");
        summary.append("                <span class='number'>").append(totalRequests).append("</span>\n");
        summary.append("            </div>\n");
        summary.append("        </div>\n");
        
        return summary.toString();
    }
    
    private static String generateDetailSection() {
        StringBuilder detail = new StringBuilder();
        detail.append("        <div class='test-results'>\n");
        detail.append("            <h2>📊 详细性能测试结果</h2>\n");
        detail.append("            <table class='results-table'>\n");
        detail.append("                <thead>\n");
        detail.append("                    <tr>\n");
        detail.append("                        <th>测试名称</th>\n");
        detail.append("                        <th>并发数</th>\n");
        detail.append("                        <th>每线程请求数</th>\n");
        detail.append("                        <th>总请求数</th>\n");
        detail.append("                        <th>成功数</th>\n");
        detail.append("                        <th>失败数</th>\n");
        detail.append("                        <th>耗时(ms)</th>\n");
        detail.append("                        <th>TPS</th>\n");
        detail.append("                        <th>平均响应时间(ms)</th>\n");
        detail.append("                        <th>成功率</th>\n");
        detail.append("                    </tr>\n");
        detail.append("                </thead>\n");
        detail.append("                <tbody>\n");
        
        for (PerformanceTestResult result : results) {
            String successRateClass = result.getSuccessRate() >= 95 ? "excellent" : 
                                    result.getSuccessRate() >= 85 ? "good" : "poor";
            
            detail.append("                    <tr class='").append(successRateClass).append("'>\n");
            detail.append("                        <td>").append(result.getTestName()).append("</td>\n");
            detail.append("                        <td>").append(result.getThreads()).append("</td>\n");
            detail.append("                        <td>").append(result.getRequestsPerThread()).append("</td>\n");
            detail.append("                        <td>").append(result.getTotalRequests()).append("</td>\n");
            detail.append("                        <td>").append(result.getSuccessCount()).append("</td>\n");
            detail.append("                        <td>").append(result.getErrorCount()).append("</td>\n");
            detail.append("                        <td>").append(result.getDurationMs()).append("</td>\n");
            detail.append("                        <td><span class='tps-value'>").append(String.format("%.2f", result.getRequestsPerSecond())).append("</span></td>\n");
            detail.append("                        <td>").append(String.format("%.2f", result.getAvgResponseTime())).append("</td>\n");
            detail.append("                        <td><span class='success-rate ").append(successRateClass).append("'>").append(String.format("%.1f%%", result.getSuccessRate())).append("</span></td>\n");
            detail.append("                    </tr>\n");
        }
        
        detail.append("                </tbody>\n");
        detail.append("            </table>\n");
        detail.append("        </div>\n");
        
        return detail.toString();
    }
    
    private static String getCssStyles() {
        return "* { margin: 0; padding: 0; box-sizing: border-box; }\n" +
               "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }\n" +
               ".container { max-width: 1200px; margin: 0 auto; padding: 20px; }\n" +
               ".report-header { background: white; padding: 30px; border-radius: 10px; text-align: center; margin-bottom: 30px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }\n" +
               ".report-header h1 { color: #2c3e50; margin-bottom: 10px; }\n" +
               ".report-time { color: #7f8c8d; }\n" +
               ".summary-cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-bottom: 30px; }\n" +
               ".summary-card { background: white; padding: 25px; border-radius: 10px; text-align: center; box-shadow: 0 4px 6px rgba(0,0,0,0.1); transition: transform 0.3s; }\n" +
               ".summary-card:hover { transform: translateY(-5px); }\n" +
               ".summary-card h3 { color: #34495e; margin-bottom: 15px; }\n" +
               ".summary-card .number { font-size: 2.5em; font-weight: bold; }\n" +
               ".summary-card.total .number { color: #3498db; }\n" +
               ".summary-card.tps .number { color: #e74c3c; }\n" +
               ".summary-card.success .number { color: #27ae60; }\n" +
               ".summary-card.requests .number { color: #9b59b6; }\n" +
               ".test-results { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }\n" +
               ".test-results h2 { margin-bottom: 20px; color: #333; }\n" +
               ".results-table { width: 100%; border-collapse: collapse; }\n" +
               ".results-table th, .results-table td { padding: 12px; text-align: left; border-bottom: 1px solid #eee; }\n" +
               ".results-table th { background: #34495e; color: white; font-weight: 600; }\n" +
               ".results-table tr:hover { background: #f8f9fa; }\n" +
               ".results-table tr.excellent { border-left: 4px solid #27ae60; }\n" +
               ".results-table tr.good { border-left: 4px solid #f39c12; }\n" +
               ".results-table tr.poor { border-left: 4px solid #e74c3c; }\n" +
               ".tps-value { font-weight: bold; color: #e74c3c; }\n" +
               ".success-rate { padding: 4px 8px; border-radius: 12px; font-weight: bold; }\n" +
               ".success-rate.excellent { background: #d4edda; color: #155724; }\n" +
               ".success-rate.good { background: #fff3cd; color: #856404; }\n" +
               ".success-rate.poor { background: #f8d7da; color: #721c24; }\n" +
               ".report-footer { background: #2c3e50; color: white; padding: 20px; text-align: center; border-radius: 10px; margin-top: 30px; }\n";
    }
    
    public static void clearResults() {
        results.clear();
    }
} 