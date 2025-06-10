package com.yanhuo.platform.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * 集成测试配置类
 * 用于禁用或模拟一些在测试环境中不需要的组件
 */
@TestConfiguration
@Profile({"integration-test", "unit-test"})
public class IntegrationTestConfig {

    /**
     * 创建一个Mock的XXL-Job配置来替代真实的配置
     * 完全禁用XXL-Job相关功能
     */
    @Bean(name = "xxlJobConfig")
    @Primary
    @ConditionalOnMissingBean(name = "xxlJobConfig")
    public Object mockXxlJobConfig() {
        return new Object();
    }

    /**
     * 创建一个Mock的XXL-Job执行器来替代真实的执行器
     * 避免在测试环境中初始化定时任务
     */
    @Bean(name = "xxlJobExecutor")
    @Primary
    @ConditionalOnMissingBean(name = "xxlJobExecutor")
    public Object mockXxlJobExecutor() {
        return new Object() {
            // 空实现的mock对象
        };
    }
} 