# Academic Brain - 智能学术助手平台

## 项目简介

Academic Brain 是一个基于 Spring Boot + Vue 3 的智能学术助手平台，旨在为学术研究者和学生提供知识管理、学习计划制定、学术讨论和AI辅助功能的一站式解决方案。

## 🚀 技术栈

### 后端技术栈
- **框架**: Spring Boot 2.7.0
- **语言**: Java 8
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **ORM**: MyBatis Plus 3.5.2
- **认证**: JWT
- **文档**: Swagger/Knife4j
- **工具库**: Hutool 5.8.5

### 前端技术栈
- **框架**: Vue 3 (Composition API)
- **语言**: TypeScript
- **UI组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP客户端**: Axios
- **构建工具**: Vite
- **包管理器**: npm

## 📁 项目架构

### 后端结构
```
AcademicBrain/src/main/java/com/academicbrain/
├── controller/           # 控制器层
│   └── AuthController.java
├── service/             # 服务层
│   ├── AuthService.java
│   └── impl/
│       └── AuthServiceImpl.java
├── repository/          # 数据访问层
│   └── UserRepository.java
├── model/              # 实体类
│   └── User.java
├── dto/                # 数据传输对象
│   └── UserDTO.java
├── config/             # 配置类
│   ├── SwaggerConfig.java
│   └── RedisConfig.java
├── utils/              # 工具类
│   ├── JwtUtils.java
│   └── Result.java
└── Application.java    # 启动类

resources/
├── application.yml     # 配置文件
└── sql/
    └── init.sql       # 数据库初始化脚本
```

### 前端结构
```
AcademicBrain/src/frontend/
├── src/
│   ├── api/            # API接口
│   │   └── auth.ts
│   ├── assets/         # 静态资源
│   │   └── logo.svg
│   ├── components/     # 公共组件
│   ├── router/         # 路由配置
│   │   └── index.ts
│   ├── store/          # 状态管理
│   │   └── user.ts
│   ├── utils/          # 工具函数
│   │   └── request.ts
│   ├── views/          # 页面组件
│   │   ├── Home.vue
│   │   ├── Login.vue
│   │   └── Register.vue
│   ├── App.vue
│   └── main.ts
├── package.json
├── vite.config.ts
├── tsconfig.json
└── README.md
```

## 🛠️ 环境要求

### 后端环境
- JDK 8 或更高版本
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 前端环境
- Node.js 16.0+ 
- npm 8.0+

## 📥 安装与运行

### 📋 环境要求

#### 必需软件
- **JDK 8 或更高版本**
- **Maven 3.6+**
- **Node.js 16.0+**
- **npm 8.0+**
- **MySQL 8.0+**
- **Redis 6.0+**

#### 开发工具（推荐）
- IntelliJ IDEA 或 Eclipse
- Visual Studio Code
- Navicat 或其他MySQL客户端
- Redis Desktop Manager

### 🚀 详细部署步骤

#### 第一步：准备开发环境

##### 1.1 安装JDK 8
```bash
# macOS (使用Homebrew)
brew install openjdk@8

# 设置JAVA_HOME环境变量
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
```

##### 1.2 安装Maven
```bash
# macOS
brew install maven

# 验证安装
mvn -version
```

##### 1.3 安装Node.js和npm
```bash
# macOS
brew install node

# 验证安装
node --version
npm --version
```

##### 1.4 安装MySQL 8.0
```bash
# macOS
brew install mysql@8.0

# 启动MySQL服务
brew services start mysql@8.0

# 设置root密码（首次安装时）
mysql_secure_installation
```

##### 1.5 安装Redis
```bash
# macOS
brew install redis

# 启动Redis服务
brew services start redis

# 测试Redis连接
redis-cli ping
```

#### 第二步：获取项目代码

##### 2.1 克隆项目
```bash
cd /Users/ning/Course
git clone <repository-url>
cd AcademicBrain
```

#### 第三步：数据库配置

##### 3.1 创建数据库
```sql
-- 连接MySQL
mysql -u root -p
输入空密码
登陆后重设密码：
ALTER USER 'root'@'localhost' IDENTIFIED BY '你的新密码';
FLUSH PRIVILEGES;

-- 创建数据库
CREATE DATABASE academic_brain DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 验证数据库创建
SHOW DATABASES;
```

##### 3.2 执行初始化脚本
```bash
# 在项目根目录下执行
mysql -u root -p academic_brain < src/main/resources/sql/init.sql
```

##### 3.3 验证数据库表创建
```sql
-- 连接到数据库
mysql -u root -p academic_brain

-- 查看表结构
SHOW TABLES;
DESC t_user;

-- 查看测试数据
SELECT * FROM t_user;
```

#### 第四步：后端配置与启动

##### 4.1 配置数据库连接
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/academic_brain?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_mysql_password  # 替换为你的MySQL密码
  
  redis:
    host: localhost
    port: 6379
    password:  # 如果Redis设置了密码，在这里填写
    database: 0
```

##### 4.2 验证Maven配置
```bash
# 检查Maven配置
mvn --version

# 检查项目依赖
mvn dependency:tree
```

##### 4.3 编译后端项目
```bash
# 清理并编译项目
mvn clean compile

# 下载依赖并打包（跳过测试）
mvn clean package -Dmaven.test.skip=true
```

##### 4.4 启动后端服务
```bash
# 方式1：使用Maven直接运行
mvn spring-boot:run

# 方式2：运行打包后的JAR文件
java -jar target/academic-brain-1.0.0.jar

# 方式3：使用IDE运行（推荐开发时使用）
# 在IDE中运行 src/main/java/com/academicbrain/Application.java
```

##### 4.5 验证后端服务
```bash
# 检查服务是否启动
curl http://localhost:8080/actuator/health

# 访问API文档
# 在浏览器打开：http://localhost:8080/doc.html
```

#### 第五步：前端配置与启动

##### 5.1 进入前端目录
```bash
cd src/frontend
```

##### 5.2 安装依赖
```bash
# 清理npm缓存（如果之前有问题）
npm cache clean --force

# 安装依赖
npm install

# 如果网络问题，可以使用淘宝镜像
npm install --registry=https://registry.npmmirror.com
```

##### 5.3 配置环境变量
创建 `.env.development` 文件：
```bash
# 创建开发环境配置文件
cat > .env.development << EOF
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_TITLE=Academic Brain (开发环境)
EOF
```

##### 5.4 启动前端开发服务器
```bash
# 启动开发服务器
npm run dev

# 如果端口冲突，可以指定端口
npx vite --port 3000
```

##### 5.5 验证前端服务
```bash
# 前端应用将在以下地址启动：
# http://localhost:5173 (Vite默认端口)
# 或 http://localhost:3000 (如果指定了端口)
```

#### 第六步：测试完整功能

##### 6.1 测试用户登录
- 访问前端应用：`http://localhost:5173`
- 使用测试账号登录：
  - 用户名：`admin`
  - 密码：`123456`

##### 6.2 测试API接口
```bash
# 测试用户登录API
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

#### 第七步：开发环境优化

##### 7.1 配置IDE
**IntelliJ IDEA配置：**
- 安装Lombok插件
- 设置Maven配置
- 配置代码格式化规则
- 设置Git集成

**VS Code配置（前端）：**
- 安装Vue 3扩展
- 安装TypeScript扩展
- 配置ESLint和Prettier

##### 7.2 设置热重载
```bash
# 后端热重载（在IDE中开启自动编译）
# 或添加spring-boot-devtools依赖

# 前端已默认支持热重载
```

##### 7.3 配置日志输出
编辑 `src/main/resources/application.yml`：
```yaml
logging:
  level:
    com.academicbrain: debug
    org.springframework.web: debug
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

## 🐛 常见问题解决

### 数据库连接问题
```bash
# 检查MySQL服务状态
brew services list | grep mysql

# 重启MySQL服务
brew services restart mysql@8.0

# 检查数据库连接
mysql -u root -p -e "SELECT 1"
```

### Redis连接问题
```bash
# 检查Redis服务状态
brew services list | grep redis

# 重启Redis服务
brew services restart redis

# 测试Redis连接
redis-cli ping
```

### 端口占用问题
```bash
# 检查端口占用
lsof -i :8080  # 后端端口
lsof -i :5173  # 前端端口

# 杀死占用端口的进程
kill -9 <PID>
```

### Maven依赖下载失败
```bash
# 清理Maven缓存
mvn dependency:purge-local-repository

# 重新下载依赖
mvn clean install -U
```

### npm安装失败
```bash
# 清理npm缓存
npm cache clean --force

# 删除node_modules重新安装
rm -rf node_modules package-lock.json
npm install
```

## 🎯 已实现功能

### 后端功能
- ✅ 用户注册/登录
- ✅ JWT Token认证
- ✅ Redis缓存支持
- ✅ Swagger API文档
- ✅ 统一结果封装
- ✅ 全局异常处理

### 前端功能
- ✅ 用户登录/注册界面
- ✅ 响应式设计
- ✅ 路由管理
- ✅ 状态管理（Pinia）
- ✅ HTTP请求封装
- ✅ Element Plus UI组件

## 🚧 待实现功能

- [ ] 知识库管理
- [ ] 学习计划制定
- [ ] 学术讨论区
- [ ] AI智能助手
- [ ] 文件上传
- [ ] 个人中心
- [ ] 系统设置
- [ ] 权限管理

## 📚 开发指南

### 添加新的后端功能

1. **创建实体类**
   ```java
   // 在 model 包下创建实体类
   @Data
   @TableName("t_example")
   public class Example {
       @TableId(type = IdType.ASSIGN_UUID)
       private String id;
       // 其他字段...
   }
   ```

2. **创建Repository**
   ```java
   // 在 repository 包下创建
   @Mapper
   public interface ExampleRepository extends BaseMapper<Example> {
   }
   ```

3. **创建Service**
   ```java
   // 接口
   public interface ExampleService {
       // 定义业务方法
   }
   
   // 实现类
   @Service
   public class ExampleServiceImpl implements ExampleService {
       // 实现业务逻辑
   }
   ```

4. **创建Controller**
   ```java
   @Api(tags = "示例模块")
   @RestController
   @RequestMapping("/api/example")
   @CrossOrigin(origins = "*")
   public class ExampleController {
       // 定义API接口
   }
   ```

### 添加新的前端功能

1. **创建API接口**
   ```typescript
   // 在 src/api/ 下创建相应的API文件
   import request from '@/utils/request'
   
   export const getExampleList = () => {
     return request.get('/example/list')
   }
   ```

2. **创建页面组件**
   ```vue
   <!-- 在 src/views/ 下创建Vue组件 -->
   <template>
     <div class="example-container">
       <!-- 页面内容 -->
     </div>
   </template>
   
   <script setup lang="ts">
   // 组件逻辑
   </script>
   
   <style scoped>
   /* 样式 */
   </style>
   ```

3. **添加路由**
   ```typescript
   // 在 src/router/index.ts 中添加路由
   {
     path: '/example',
     name: 'Example',
     component: () => import('@/views/Example.vue')
   }
   ```

4. **状态管理（如需要）**
   ```typescript
   // 在 src/store/ 下创建状态管理文件
   import { defineStore } from 'pinia'
   
   export const useExampleStore = defineStore('example', () => {
     // 状态和方法
   })
   ```

## 🔧 配置说明

### 环境变量配置

#### 后端环境变量
在 `application.yml` 中配置：
```yaml
# 开发环境
spring:
  profiles:
    active: dev

---
# 开发环境配置
spring:
  profiles: dev
  datasource:
    url: jdbc:mysql://localhost:3306/academic_brain_dev
    
---
# 生产环境配置
spring:
  profiles: prod
  datasource:
    url: jdbc:mysql://prod-host:3306/academic_brain
```

#### 前端环境变量
创建 `.env.development` 和 `.env.production`：
```bash
# .env.development
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_TITLE=Academic Brain (开发)

# .env.production
VITE_API_BASE_URL=https://api.yourdomain.com/api
VITE_APP_TITLE=Academic Brain
```

## 🚀 部署指南

### Docker部署（推荐）

1. **构建后端镜像**
   ```dockerfile
   # Dockerfile
   FROM openjdk:8-jre-slim
   COPY target/academic-brain-1.0.0.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java","-jar","/app.jar"]
   ```

2. **构建前端镜像**
   ```dockerfile
   # 前端Dockerfile
   FROM node:16-alpine as builder
   WORKDIR /app
   COPY package*.json ./
   RUN npm install
   COPY . .
   RUN npm run build
   
   FROM nginx:alpine
   COPY --from=builder /app/dist /usr/share/nginx/html
   COPY nginx.conf /etc/nginx/nginx.conf
   EXPOSE 80
   ```

3. **Docker Compose**
   ```yaml
   version: '3.8'
   services:
     backend:
       build: .
       ports:
         - "8080:8080"
       depends_on:
         - mysql
         - redis
     
     frontend:
       build: ./src/frontend
       ports:
         - "80:80"
       depends_on:
         - backend
     
     mysql:
       image: mysql:8.0
       environment:
         MYSQL_ROOT_PASSWORD: root
         MYSQL_DATABASE: academic_brain
     
     redis:
       image: redis:6.0-alpine
   ```

### 传统部署

1. **后端部署**
   ```bash
   # 构建JAR包
   mvn clean package -Dmaven.test.skip=true
   
   # 上传到服务器并启动
   nohup java -jar academic-brain-1.0.0.jar --spring.profiles.active=prod > app.log 2>&1 &
   ```

2. **前端部署**
   ```bash
   # 构建静态文件
   npm run build
   
   # 上传dist目录到Web服务器
   scp -r dist/* user@server:/var/www/html/
   ```

## 🧪 测试

### 后端测试
```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify
```

### 前端测试
```bash
cd src/frontend

# 运行单元测试
npm run test

# 运行E2E测试
npm run test:e2e
```

## 📝 代码规范

### 后端规范
- 遵循阿里巴巴Java开发手册
- 使用Lombok减少样板代码
- 统一异常处理
- API文档完整

### 前端规范
- 使用TypeScript类型检查
- 遵循Vue 3 Composition API规范
- 组件名使用PascalCase
- 使用ESLint进行代码检查

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证。详情请参阅 [LICENSE](LICENSE) 文件。

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 Issue
- 发送邮件：contact@academicbrain.com
- 项目主页：https://github.com/your-org/academic-brain

## 🙏 致谢

感谢所有为此项目做出贡献的开发者和用户！
