package com.yanhuo.platform.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * 验收测试运行器
 * 使用Cucumber执行BDD验收测试
 */
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:features",
    glue = "com.yanhuo.platform.acceptance",
    plugin = {
        "pretty",
        "html:target/cucumber-reports",
        "json:target/cucumber-reports/cucumber.json",
        "junit:target/cucumber-reports/cucumber.xml"
    },
    tags = "not @ignore"
)
public class AcceptanceTestRunner {
    // Cucumber会自动执行测试
} 