# 测试文档

## 概述

本项目实现了完整的测试体系，包括单元测试、集成测试、性能测试和验收测试，覆盖前端和后端的所有主要功能。

## 测试架构

```
测试体系
├── 后端测试 (Java/Spring Boot)
│   ├── 单元测试 (Unit Tests)
│   ├── 集成测试 (Integration Tests)
│   ├── 性能测试 (Performance Tests)
│   └── 验收测试 (Acceptance Tests)
└── 前端测试 (Vue3/TypeScript)
    ├── 单元测试 (Vitest)
    ├── 组件测试 (Vue Test Utils)
    └── 端到端测试 (Playwright)
```

## 测试工具和框架

### 后端测试
- **JUnit 5**: 单元测试框架
- **Mockito**: 模拟对象框架
- **Spring Boot Test**: Spring Boot测试支持
- **TestContainers**: 集成测试容器化
- **REST Assured**: API测试
- **JMeter**: 性能测试
- **H2 Database**: 测试数据库
- **JaCoCo**: 代码覆盖率

### 前端测试
- **Vitest**: 单元测试框架
- **Vue Test Utils**: Vue组件测试
- **Playwright**: 端到端测试
- **JSDOM**: DOM环境模拟
- **C8**: 代码覆盖率

## 快速开始

### 1. 运行所有测试
```bash
./test-runner.sh
```

### 2. 运行特定类型的测试
```bash
# 仅运行单元测试
./test-runner.sh unit

# 仅运行集成测试
./test-runner.sh integration

# 仅运行性能测试
./test-runner.sh performance

# 仅运行验收测试
./test-runner.sh acceptance

# 仅运行前端测试
./test-runner.sh frontend

# 仅运行后端测试
./test-runner.sh backend
```

### 3. 查看帮助信息
```bash
./test-runner.sh help
```

## 详细测试指南

### 后端测试

#### 单元测试
位置：`yanhuo-platform/src/test/java/com/yanhuo/platform/service/`

运行命令：
```bash
mvn test -Dtest="**/*Test.java"
```

特点：
- 测试单个类或方法的功能
- 使用Mockito模拟依赖
- 快速执行，无外部依赖
- 覆盖业务逻辑和边界条件

#### 集成测试
位置：`yanhuo-platform/src/test/java/com/yanhuo/platform/integration/`

运行命令：
```bash
mvn verify -Dtest="**/*IntegrationTest.java"
```

特点：
- 测试多个组件的协作
- 使用TestContainers启动真实数据库
- 测试完整的请求-响应流程
- 验证数据持久化和事务

#### 性能测试
位置：`yanhuo-platform/src/test/java/com/yanhuo/platform/performance/`

运行命令：
```bash
mvn test -Dtest="**/*PerformanceTest.java"
```

特点：
- 测试系统在负载下的表现
- 并发用户模拟
- 响应时间和吞吐量测试
- 资源使用监控

#### 验收测试
位置：`yanhuo-platform/src/test/java/com/yanhuo/platform/acceptance/`

运行命令：
```bash
mvn verify -Dtest="**/*AcceptanceTest.java"
```

特点：
- 模拟真实用户场景
- 端到端业务流程验证
- 用户故事验证
- 系统行为验证

### 前端测试

#### 单元测试
位置：`yanhuo-web/src/tests/components/`

运行命令：
```bash
cd yanhuo-web
npm run test:unit
```

特点：
- 测试Vue组件的行为
- 测试工具函数和业务逻辑
- 快速反馈
- 高代码覆盖率

#### 端到端测试
位置：`yanhuo-web/tests/e2e/`

运行命令：
```bash
cd yanhuo-web
npm run test:e2e
```

特点：
- 真实浏览器环境测试
- 用户交互模拟
- 跨浏览器兼容性测试
- 响应式设计验证

## 测试配置

### 后端测试配置
- **测试配置文件**: `yanhuo-platform/src/test/resources/application-test.yml`
- **基础测试类**: `yanhuo-platform/src/test/java/com/yanhuo/platform/test/BaseTest.java`
- **Maven配置**: `pom.xml` 中的测试插件配置

### 前端测试配置
- **Vitest配置**: `yanhuo-web/vitest.config.ts`
- **Playwright配置**: `yanhuo-web/playwright.config.ts`
- **测试设置**: `yanhuo-web/src/tests/setup.ts`

## 代码覆盖率

### 查看覆盖率报告

#### 后端覆盖率
```bash
mvn jacoco:report
# 报告位置: target/site/jacoco/index.html
```

#### 前端覆盖率
```bash
cd yanhuo-web
npm run test:coverage
# 报告位置: coverage/index.html
```

### 覆盖率目标
- **单元测试覆盖率**: ≥ 80%
- **集成测试覆盖率**: ≥ 60%
- **整体代码覆盖率**: ≥ 70%

## 持续集成

### GitHub Actions 配置示例
```yaml
name: 测试流水线

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: 设置 Java
        uses: actions/setup-java@v3
        with:
          java-version: '11'
      - name: 设置 Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
      - name: 运行测试
        run: ./test-runner.sh
      - name: 上传覆盖率报告
        uses: codecov/codecov-action@v3
```

## 测试最佳实践

### 1. 测试命名规范
- **单元测试**: `MethodName_Scenario_ExpectedResult`
- **集成测试**: `Feature_Scenario_ExpectedResult`
- **验收测试**: `UserStory_Scenario_ExpectedResult`

### 2. 测试数据管理
- 使用测试专用的数据库
- 每个测试独立的数据准备
- 测试后自动清理数据

### 3. 测试隔离
- 测试之间不应相互依赖
- 使用事务回滚保证数据隔离
- 模拟外部依赖

### 4. 性能测试指导
- 建立性能基准
- 监控关键指标
- 设置合理的性能阈值

## 故障排除

### 常见问题

#### 1. 测试数据库连接失败
```bash
# 检查H2数据库配置
# 确保测试配置文件正确
```

#### 2. 前端测试环境问题
```bash
# 清理node_modules重新安装
rm -rf node_modules package-lock.json
npm install
```

#### 3. 性能测试超时
```bash
# 增加超时时间配置
# 检查系统资源使用情况
```

### 调试技巧
- 使用IDE的调试功能
- 添加详细的日志输出
- 使用测试专用的配置文件
- 分步骤验证测试逻辑

## 测试报告

### 自动生成报告
测试运行完成后，系统会自动生成以下报告：

1. **JUnit测试报告**: `target/surefire-reports/`
2. **代码覆盖率报告**: `target/site/jacoco/`
3. **性能测试报告**: `target/jmeter/`
4. **前端测试报告**: `yanhuo-web/test-results/`

### 报告内容
- 测试执行统计
- 成功/失败用例详情
- 代码覆盖率分析
- 性能指标统计
- 错误日志和堆栈跟踪

## 贡献指南

### 添加新测试
1. 确定测试类型和位置
2. 遵循现有的命名规范
3. 添加适当的注释和文档
4. 确保测试的独立性和可重复性

### 测试审查清单
- [ ] 测试覆盖了主要功能路径
- [ ] 测试覆盖了边界条件和异常情况
- [ ] 测试具有清晰的断言和验证
- [ ] 测试运行稳定且可重复
- [ ] 测试文档完整且准确

## 联系方式

如有测试相关问题，请联系：
- 开发团队: dev@academicbrain.com
- 测试团队: qa@academicbrain.com

---

**注意**: 请确保在提交代码前运行完整的测试套件，确保所有测试通过。 