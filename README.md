# AcademicBrain - 学术社交平台

## 📋 项目简介

AcademicBrain是一个基于Spring Boot + Vue3的学术社交平台，为用户提供笔记分享、学术交流、即时通讯等功能。

## 🏗️ 系统架构

### 后端模块
- **yanhuo-platform**: 核心业务平台，处理用户、笔记、评论、关注等功能
- **yanhuo-auth**: 用户认证授权服务，JWT token管理
- **yanhuo-gateway**: API网关，统一路由和鉴权
- **yanhuo-im**: 即时通讯服务，支持实时聊天
- **yanhuo-search**: 搜索服务，提供笔记和用户搜索
- **yanhuo-util**: 工具服务，包含文件上传(OSS)、短信服务
- **yanhuo-common**: 公共模块，通用工具类和配置
- **yanhuo-xo**: 数据传输对象(DTO/VO)定义

### 前端模块
- **yanhuo-web**: Vue3 + TypeScript前端应用

## 🚀 核心功能

### 用户功能
- 用户注册、登录、个人资料管理
- 用户关注/取消关注
- 个人主页和动态展示

### 内容管理
- 笔记发布、编辑、删除
- 图片上传和处理
- 笔记分类和标签管理
- 笔记专辑创建和管理

### 社交互动
- 笔记点赞和收藏
- 评论和回复功能
- 关注动态推送
- 实时聊天和消息通知

### 搜索与推荐
- 笔记内容搜索
- 用户搜索
- 个性化推荐

## 🛠️ 技术栈

### 后端技术
- **框架**: Spring Boot 2.7.0
- **数据库**: MySQL 8.0 + MyBatis Plus
- **缓存**: Redis
- **认证**: JWT
- **文档**: Knife4j (Swagger)
- **工具**: Hutool、Lombok

### 前端技术
- **框架**: Vue 3 + TypeScript
- **构建工具**: Vite
- **UI组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP客户端**: Axios
- **其他**: 瀑布流布局、图片懒加载、即时通讯SDK

## 📁 项目结构

```
AcademicBrain/
├── yanhuo-platform/     # 核心业务服务
├── yanhuo-auth/         # 认证授权服务
├── yanhuo-gateway/      # API网关
├── yanhuo-im/           # 即时通讯服务
├── yanhuo-search/       # 搜索服务
├── yanhuo-util/         # 工具服务
├── yanhuo-common/       # 公共模块
├── yanhuo-xo/          # 数据传输对象
├── yanhuo-web/         # Vue3前端应用
├── doc/                # 文档和数据库脚本
├── pom.xml            # Maven主配置
└── README.md          # 项目说明
```

## 🚀 快速开始

### 环境要求
- Java 11+
- Node.js 16+
- MySQL 8.0+
- Redis 6.0+

### 后端启动
```bash
# 导入数据库脚本
mysql -u root -p < doc/yanhuo-test.sql

# 启动各个服务
mvn spring-boot:run
```

### 前端启动
```bash
cd yanhuo-web
npm install
npm run dev
```

## 📝 API文档

启动后端服务后，访问 Knife4j 文档：
`http://localhost:8080/doc.html`

## 📄 开源协议

本项目基于 MIT 协议开源，详见 [LICENSE](LICENSE) 文件。 