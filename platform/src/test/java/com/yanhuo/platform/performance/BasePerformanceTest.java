package com.yanhuo.platform.performance;

import com.yanhuo.platform.config.IntegrationTestConfig;
import com.yanhuo.platform.util.PerformanceReportGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "xxl.job.executor.enabled=false",
    "spring.cloud.nacos.discovery.enabled=false",
    "spring.cloud.nacos.config.enabled=false"
})
@ActiveProfiles("integration-test")
@Import(IntegrationTestConfig.class)
public abstract class BasePerformanceTest {

    @AfterAll
    static void generatePerformanceReport() {
        PerformanceReportGenerator.generateReport();
        System.out.println("🚀 性能测试完成，报告已生成！");
    }

    protected void runPerformanceTest(String testName, Runnable testLogic, 
                                    int threads, int requestsPerThread) {
        System.out.println("Starting performance test: " + testName);
        System.out.println("Threads: " + threads + ", Requests per thread: " + requestsPerThread);
        
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);
        
        Instant startTime = Instant.now();
        
        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        try {
                            testLogic.run();
                            successCount.incrementAndGet();
                        } catch (Exception e) {
                            errorCount.incrementAndGet();
                            System.err.println("Request failed: " + e.getMessage());
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        executor.shutdown();
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        
        int totalRequests = threads * requestsPerThread;
        double requestsPerSecond = totalRequests / (duration.toMillis() / 1000.0);
        
        System.out.println("=== Performance Test Results ===");
        System.out.println("Test: " + testName);
        System.out.println("Duration: " + duration.toMillis() + "ms");
        System.out.println("Total Requests: " + totalRequests);
        System.out.println("Successful: " + successCount.get());
        System.out.println("Failed: " + errorCount.get());
        System.out.println("Requests/sec: " + String.format("%.2f", requestsPerSecond));
        System.out.println("================================");
        
        // 添加到报告
        PerformanceReportGenerator.addResult(testName, threads, requestsPerThread, 
                                           totalRequests, successCount.get(), errorCount.get(), 
                                           duration.toMillis(), requestsPerSecond);
    }
} 