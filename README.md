# AcademicBrain - 学术社交平台

> 一个基于 Spring Boot 微服务架构 + Vue3 的现代化学术社交平台，支持笔记分享、实时通讯、内容搜索等功能。

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.3.4-4FC08D.svg)](https://vuejs.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📖 项目概述

AcademicBrain 是一个功能完善的学术社交平台，采用微服务架构设计，为用户提供笔记发布、内容分享、即时通讯、关注互动等功能。平台支持图片上传、评论系统、点赞收藏、搜索推荐等核心社交功能。

## 🏗️ 系统架构

### 后端微服务架构
```
AcademicBrain 后端
├── platform/          # 核心业务服务 (用户、笔记、评论、互动)
│   ├── src/main/java/
│   │   └── com/yanhuo/platform/
│   │       ├── controller/       # REST API控制器
│   │       ├── service/         # 业务逻辑层
│   │       ├── client/          # 外部服务客户端
│   │       └── PlatformApplication.java
│   └── src/test/               # 测试代码
├── auth/                       # 认证服务
├── gateway/                    # API网关
├── im/                         # 即时通讯
├── search/                     # 搜索服务
├── util/                       # 工具服务
├── common/                     # 公共模块
├── xo/                         # 数据对象层
├── web/                        # Vue3前端应用
│   ├── src/
│   │   ├── api/               # API接口
│   │   ├── components/        # Vue组件
│   │   ├── pages/            # 页面组件
│   │   ├── router/           # 路由配置
│   │   ├── store/            # 状态管理
│   │   └── utils/            # 工具函数
│   ├── tests/                # 测试文件
│   └── package.json
├── doc/                       # 项目文档
│   └── yanhuo-test.sql       # 数据库初始化脚本
├── test-runner.sh            # 测试运行脚本
└── pom.xml                   # Maven主配置
```

### 前端应用
- **web/**: Vue3 + TypeScript + Element Plus 前端应用

## ✨ 核心功能特性

### 🔐 用户系统
- 用户注册、登录、个人资料管理
- JWT Token 认证机制
- 手机验证码登录
- 用户关注/取消关注功能
- 个人主页和动态展示

### 📝 内容管理
- 富文本笔记发布与编辑
- 图片批量上传与处理
- 笔记分类和标签管理
- 专辑创建和笔记组织
- 笔记状态管理（公开/私密）

### 💬 社交互动
- 笔记点赞和收藏系统
- 多级评论和回复功能
- 关注用户动态推送
- 实时消息通知

### 🔍 内容搜索
- 全文搜索功能
- 用户和笔记检索
- 热门标签推荐
- 搜索历史记录

### 💬 即时通讯
- 实时私信聊天
- 消息状态同步
- 聊天记录管理

## 🛠️ 技术栈详情

### 后端技术
| 技术栈 | 版本 | 说明 |
|--------|------|------|
| Spring Boot | 2.7.0 | 微服务框架 |
| MyBatis Plus | 3.5.2 | ORM框架 |
| MySQL | 8.0+ | 主数据库 |
| Redis | 6.0+ | 缓存与会话 |
| JWT | 0.9.1 | 认证授权 |
| Knife4j | 3.0.3 | API文档 |
| Hutool | 5.8.5 | Java工具库 |
| Lombok | 1.18.24 | 代码简化 |

### 前端技术
| 技术栈 | 版本 | 说明 |
|--------|------|------|
| Vue | 3.3.4 | 前端框架 |
| TypeScript | 5.0.2 | 类型支持 |
| Vite | 4.4.5 | 构建工具 |
| Element Plus | 2.4.1 | UI组件库 |
| Pinia | 2.1.7 | 状态管理 |
| Vue Router | 4.2.5 | 路由管理 |
| Axios | 1.5.1 | HTTP客户端 |

### 开发与测试
- **测试框架**: JUnit 5, Mockito, TestContainers
- **性能测试**: JMeter集成
- **API测试**: REST Assured
- **前端测试**: Vitest, Playwright
- **代码质量**: ESLint, Prettier

## 📁 项目结构

```
AcademicBrain/
├── platform/                    # 核心业务服务
│   ├── src/main/java/
│   │   └── com/yanhuo/platform/
│   │       ├── controller/       # REST API控制器
│   │       ├── service/         # 业务逻辑层
│   │       ├── client/          # 外部服务客户端
│   │       └── PlatformApplication.java
│   └── src/test/               # 测试代码
├── auth/                       # 认证服务
├── gateway/                    # API网关
├── im/                         # 即时通讯
├── search/                     # 搜索服务
├── util/                       # 工具服务
├── common/                     # 公共模块
├── xo/                         # 数据对象层
├── web/                        # Vue3前端应用
│   ├── src/
│   │   ├── api/               # API接口
│   │   ├── components/        # Vue组件
│   │   ├── pages/            # 页面组件
│   │   ├── router/           # 路由配置
│   │   ├── store/            # 状态管理
│   │   └── utils/            # 工具函数
│   ├── tests/                # 测试文件
│   └── package.json
├── doc/                       # 项目文档
│   └── yanhuo-test.sql       # 数据库初始化脚本
├── test-runner.sh            # 测试运行脚本
└── pom.xml                   # Maven主配置
```

## 🚀 快速开始

### 环境要求
- **Java**: 11+
- **Node.js**: 16+
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Maven**: 3.6+

### 数据库初始化
```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE yanhuo_test CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 导入数据库结构和初始数据
mysql -u root -p yanhuo_test < doc/yanhuo-test.sql
```

### 后端服务启动
```bash
# 克隆项目
git clone <repository-url>
cd AcademicBrain

# 编译项目
mvn clean compile

# 启动各个微服务
# 1. 启动认证服务
cd auth && mvn spring-boot:run &

# 2. 启动工具服务
cd util && mvn spring-boot:run &

# 3. 启动搜索服务
cd search && mvn spring-boot:run &

# 4. 启动即时通讯服务
cd im && mvn spring-boot:run &

# 5. 启动核心业务服务
cd platform && mvn spring-boot:run &

# 6. 启动API网关（最后启动）
cd gateway && mvn spring-boot:run
```

### 前端应用启动
```bash
cd web

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

### 使用Docker快速部署
```bash
# 构建镜像
docker-compose build

# 启动所有服务
docker-compose up -d
```

## 🧪 测试

项目包含完整的测试体系，支持单元测试、集成测试、性能测试和端到端测试。

### 运行测试
```bash
# 运行所有测试
./test-runner.sh all

# 运行单元测试
./test-runner.sh unit

# 运行集成测试
./test-runner.sh integration

# 运行性能测试
./test-runner.sh performance

# 运行前端测试
./test-runner.sh frontend
```

### 测试覆盖率
- 查看后端测试覆盖率：`target/site/jacoco/index.html`
- 查看前端测试覆盖率：`web/coverage/index.html`

## 📊 API文档

启动后端服务后，可以访问 API 文档：

- **Knife4j文档**: http://localhost:8080/doc.html
- **平台服务API**: http://localhost:8081/doc.html
- **认证服务API**: http://localhost:8082/doc.html

## 🔧 配置说明

### 数据库配置
修改各模块的 `application.yml` 文件中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/yanhuo_test?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root
    password: your_password
```

### Redis配置
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📋 开发规范

- 后端遵循 RESTful API 设计规范
- 前端使用 ESLint + Prettier 进行代码格式化
- 提交信息遵循 Conventional Commits 规范
- 所有新功能需要编写相应的测试用例

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系我们

- 项目地址：[GitHub Repository]
- 问题反馈：[Issues]
- 技术交流：[Discussions]

---

**⭐ 如果这个项目对你有帮助，请给个Star支持一下！** 