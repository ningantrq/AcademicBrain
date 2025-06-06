package com.yanhuo.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import com.yanhuo.platform.config.XxlJobConfig;
import com.yanhuo.platform.config.TestXxlJobConfig;

@SpringBootApplication(exclude = {
    // 排除可能的XXL-Job自动配置
})
@ComponentScan(
    basePackages = {
        "com.yanhuo.platform.controller",
        "com.yanhuo.platform.service", 
        "com.yanhuo.platform.config",
        "com.yanhuo.platform.im",
        "com.yanhuo.common",
        "com.yanhuo.xo"
    },
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = XxlJobConfig.class
        )
    }
)
@Import(TestXxlJobConfig.class)
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
} 