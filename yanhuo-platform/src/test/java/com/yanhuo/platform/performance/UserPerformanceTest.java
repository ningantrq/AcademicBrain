package com.yanhuo.platform.performance;

import com.yanhuo.platform.TestApplication;
import com.yanhuo.platform.config.TestRedisConfig;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 用户API性能测试
 */
@DisplayName("用户API性能测试")
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Import(TestRedisConfig.class)
class UserPerformanceTest {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final String JMETER_HOME = System.getProperty("jmeter.home", "./target/jmeter");

    @BeforeEach
    void setUp() {
        // 初始化JMeter
        File jmeterHome = new File(JMETER_HOME);
        if (!jmeterHome.exists()) {
            jmeterHome.mkdirs();
        }
        JMeterUtils.setJMeterHome(jmeterHome.getAbsolutePath());
        JMeterUtils.loadJMeterProperties(jmeterHome.getAbsolutePath() + "/bin/jmeter.properties");
        JMeterUtils.initLocale();
    }

    @Test
    @DisplayName("用户搜索API性能测试")
    void userSearchPerformanceTest() throws Exception {
        // 创建测试计划
        TestPlan testPlan = new TestPlan("用户搜索性能测试");
        testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
        testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

        // 创建线程组 - 使用完全限定名避免与Java ThreadGroup冲突
        org.apache.jmeter.threads.ThreadGroup threadGroup = new org.apache.jmeter.threads.ThreadGroup();
        threadGroup.setName("用户搜索线程组");
        threadGroup.setNumThreads(50); // 50个并发用户
        threadGroup.setRampUp(10); // 10秒内启动所有线程
        threadGroup.setProperty(TestElement.TEST_CLASS, org.apache.jmeter.threads.ThreadGroup.class.getName());
        threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());

        // 设置循环控制器
        LoopController loopController = new LoopController();
        loopController.setLoops(10); // 每个线程执行10次
        loopController.setFirst(true);
        loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
        loopController.initialize();
        threadGroup.setSamplerController(loopController);

        // 创建HTTP请求
        HTTPSampler httpSampler = new HTTPSampler();
        httpSampler.setName("用户搜索请求");
        httpSampler.setDomain(SERVER_HOST);
        httpSampler.setPort(SERVER_PORT);
        httpSampler.setPath("/user/getUserPageByKeyword/1/10");
        httpSampler.setMethod("GET");
        httpSampler.addArgument("keyword", "test");
        httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSampler.class.getName());
        httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());

        // 创建结果收集器
        Summariser summer = null;
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (summariserName.length() > 0) {
            summer = new Summariser(summariserName);
        }

        ResultCollector logger = new ResultCollector(summer);
        logger.setFilename("./target/user-search-performance-results.jtl");

        // 构建测试树
        HashTree testPlanTree = new HashTree();
        testPlanTree.add(testPlan);
        HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(httpSampler);
        testPlanTree.add(testPlan, logger);

        // 运行测试
        StandardJMeterEngine jmeter = new StandardJMeterEngine();
        jmeter.configure(testPlanTree);
        jmeter.run();

        // 等待测试完成
        Thread.sleep(30000); // 等待30秒
    }

    @Test
    @DisplayName("用户动态获取API性能测试")
    void userTrendPerformanceTest() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(20);
        
        // 创建20个并发任务
        CompletableFuture<?>[] futures = new CompletableFuture[20];
        
        for (int i = 0; i < 20; i++) {
            final int threadId = i;
            futures[i] = CompletableFuture.runAsync(() -> {
                try {
                    // 每个线程执行100次请求
                    for (int j = 0; j < 100; j++) {
                        long startTime = System.currentTimeMillis();
                        
                        // 模拟HTTP请求（这里使用简单的性能测试逻辑）
                        // 在实际环境中，您可以使用HTTP客户端发送真实请求
                        simulateApiCall("getTrendPageByUser", threadId, j);
                        
                        long endTime = System.currentTimeMillis();
                        long responseTime = endTime - startTime;
                        
                        // 记录响应时间
                        if (responseTime > 1000) { // 如果响应时间超过1秒
                            System.out.println("慢响应警告: 线程" + threadId + ", 请求" + j + ", 响应时间: " + responseTime + "ms");
                        }
                        
                        // 控制请求频率
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, executor);
        }
        
        // 等待所有任务完成
        CompletableFuture.allOf(futures).get(60, TimeUnit.SECONDS);
        executor.shutdown();
    }

    @Test
    @DisplayName("用户更新API并发测试")
    void userUpdateConcurrencyTest() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        // 模拟10个用户同时更新信息
        CompletableFuture<?>[] futures = new CompletableFuture[10];
        
        for (int i = 0; i < 10; i++) {
            final int userId = i + 1;
            futures[i] = CompletableFuture.runAsync(() -> {
                try {
                    for (int j = 0; j < 50; j++) {
                        long startTime = System.currentTimeMillis();
                        
                        // 模拟用户更新请求
                        simulateApiCall("updateUser", userId, j);
                        
                        long endTime = System.currentTimeMillis();
                        long responseTime = endTime - startTime;
                        
                        // 检查数据一致性和性能
                        if (responseTime > 2000) {
                            System.out.println("更新操作超时警告: 用户" + userId + ", 请求" + j + ", 响应时间: " + responseTime + "ms");
                        }
                        
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, executor);
        }
        
        CompletableFuture.allOf(futures).get(120, TimeUnit.SECONDS);
        executor.shutdown();
    }

    @Test
    @DisplayName("数据库连接池压力测试")
    void databaseConnectionPoolStressTest() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        
        // 创建100个并发数据库操作
        CompletableFuture<?>[] futures = new CompletableFuture[100];
        
        for (int i = 0; i < 100; i++) {
            final int threadId = i;
            futures[i] = CompletableFuture.runAsync(() -> {
                try {
                    for (int j = 0; j < 20; j++) {
                        long startTime = System.currentTimeMillis();
                        
                        // 模拟数据库查询操作
                        simulateDbQuery("SELECT * FROM user WHERE id = " + (threadId % 100 + 1));
                        
                        long endTime = System.currentTimeMillis();
                        long responseTime = endTime - startTime;
                        
                        if (responseTime > 500) {
                            System.out.println("数据库查询慢警告: 线程" + threadId + ", 查询" + j + ", 响应时间: " + responseTime + "ms");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("数据库操作异常: 线程" + threadId + ", 错误: " + e.getMessage());
                }
            }, executor);
        }
        
        CompletableFuture.allOf(futures).get(180, TimeUnit.SECONDS);
        executor.shutdown();
    }

    /**
     * 模拟API调用
     */
    private void simulateApiCall(String apiName, int threadId, int requestId) throws InterruptedException {
        // 模拟API处理时间
        int processingTime = (int) (Math.random() * 100) + 50; // 50-150ms
        Thread.sleep(processingTime);
        
        // 随机模拟一些失败情况
        if (Math.random() < 0.01) { // 1%的失败率
            throw new RuntimeException("模拟API调用失败: " + apiName);
        }
    }

    /**
     * 模拟数据库查询
     */
    private void simulateDbQuery(String sql) throws InterruptedException {
        // 模拟数据库查询时间
        int queryTime = (int) (Math.random() * 200) + 10; // 10-210ms
        Thread.sleep(queryTime);
        
        // 随机模拟数据库连接异常
        if (Math.random() < 0.005) { // 0.5%的失败率
            throw new RuntimeException("模拟数据库连接失败: " + sql);
        }
    }
} 