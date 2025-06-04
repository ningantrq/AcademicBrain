# Academic Brain 前端项目

> Academic Brain 智能学术助手平台的前端应用，基于 Vue 3 + TypeScript + Element Plus 构建。

## 🛠️ 技术栈

- **框架**: Vue 3 (Composition API)
- **语言**: TypeScript
- **UI 组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP 客户端**: Axios
- **构建工具**: Vite
- **包管理器**: npm

## 📁 项目结构

```
src/
├── api/                    # API 接口层
│   └── auth.ts            # 认证相关接口
├── assets/                # 静态资源
│   └── logo.svg          # 项目 Logo
├── components/            # 公共组件（待扩展）
├── router/                # 路由配置
│   └── index.ts          # 路由定义
├── store/                 # 状态管理
│   └── user.ts           # 用户状态管理
├── utils/                 # 工具函数
│   └── request.ts        # Axios 请求封装
├── views/                 # 页面组件
│   ├── Home.vue          # 首页
│   ├── Login.vue         # 登录页
│   └── Register.vue      # 注册页
├── App.vue               # 根组件
└── main.ts               # 应用入口
```

## ⚡ 快速开始

### 环境要求
- Node.js >= 16.0.0
- npm >= 8.0.0

### 安装和运行
```bash
# 进入前端目录
cd src/frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 预览生产版本
npm run preview
```

## 🎯 已实现功能

### 用户认证
- [x] 登录页面（支持二维码登录展示）
- [x] 注册页面（表单验证）
- [x] 自动登录检查
- [x] Token 管理和刷新

### UI/UX
- [x] 响应式设计（支持移动端）
- [x] 现代化界面设计
- [x] Element Plus 组件集成
- [x] 统一的样式规范

### 核心功能
- [x] 路由管理
- [x] 状态管理（Pinia）
- [x] HTTP 请求封装（拦截器）
- [x] 首页功能模块展示

## 🚧 开发中功能

- [ ] 知识库管理界面
- [ ] 学习计划组件
- [ ] 学术讨论区
- [ ] AI 助手界面
- [ ] 个人中心
- [ ] 系统设置

## 📚 开发指南

### 添加新页面

1. **创建页面组件**
   ```bash
   # 在 src/views/ 下创建新的 Vue 组件
   touch src/views/NewPage.vue
   ```

2. **组件模板**
   ```vue
   <template>
     <div class="new-page-container">
       <h1>新页面</h1>
       <!-- 页面内容 -->
     </div>
   </template>

   <script setup lang="ts">
   import { ref } from 'vue'
   
   // 组件逻辑
   const data = ref('')
   </script>

   <style scoped>
   .new-page-container {
     padding: 20px;
   }
   </style>
   ```

3. **添加路由**
   ```typescript
   // 在 src/router/index.ts 中添加
   {
     path: '/new-page',
     name: 'NewPage',
     component: () => import('@/views/NewPage.vue')
   }
   ```

### 添加API接口

1. **创建接口文件**
   ```typescript
   // src/api/example.ts
   import request from '@/utils/request'

   export interface ExampleData {
     id: string
     name: string
   }

   export const getExampleList = () => {
     return request.get<ExampleData[]>('/example/list')
   }

   export const createExample = (data: Partial<ExampleData>) => {
     return request.post('/example', data)
   }
   ```

2. **在组件中使用**
   ```vue
   <script setup lang="ts">
   import { ref, onMounted } from 'vue'
   import { getExampleList, type ExampleData } from '@/api/example'

   const dataList = ref<ExampleData[]>([])

   const fetchData = async () => {
     try {
       dataList.value = await getExampleList()
     } catch (error) {
       console.error('获取数据失败:', error)
     }
   }

   onMounted(() => {
     fetchData()
   })
   </script>
   ```

### 状态管理

1. **创建新的 Store**
   ```typescript
   // src/store/example.ts
   import { defineStore } from 'pinia'
   import { ref } from 'vue'

   export const useExampleStore = defineStore('example', () => {
     const data = ref<any[]>([])
     const loading = ref(false)

     const fetchData = async () => {
       loading.value = true
       try {
         // 调用 API
         // data.value = await api.getData()
       } finally {
         loading.value = false
       }
     }

     return {
       data,
       loading,
       fetchData
     }
   })
   ```

2. **在组件中使用**
   ```vue
   <script setup lang="ts">
   import { useExampleStore } from '@/store/example'

   const exampleStore = useExampleStore()
   </script>
   ```

## 🎨 样式指南

### CSS 规范
- 使用 `scoped` 样式避免样式冲突
- 遵循 BEM 命名规范
- 使用 CSS 变量定义主题色彩

### 响应式设计
```css
/* 移动端优先 */
.container {
  padding: 16px;
}

/* 平板端 */
@media (min-width: 768px) {
  .container {
    padding: 24px;
  }
}

/* 桌面端 */
@media (min-width: 1024px) {
  .container {
    padding: 32px;
  }
}
```

### Element Plus 主题定制
```css
/* 在 main.ts 或全局样式中 */
:root {
  --el-color-primary: #409eff;
  --el-color-success: #67c23a;
  --el-color-warning: #e6a23c;
  --el-color-danger: #f56c6c;
}
```

## 🔧 配置说明

### 环境变量
```bash
# .env.development
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_TITLE=Academic Brain (开发环境)

# .env.production
VITE_API_BASE_URL=https://api.yourdomain.com/api
VITE_APP_TITLE=Academic Brain
```

### Vite 配置
```typescript
// vite.config.ts
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

## 🧪 测试

### 单元测试（待实现）
```bash
# 运行单元测试
npm run test:unit

# 测试覆盖率
npm run test:coverage
```

### E2E 测试（待实现）
```bash
# 运行端到端测试
npm run test:e2e
```

## 🚀 构建和部署

### 构建优化
```bash
# 分析构建包大小
npm run build -- --analyze

# 生成构建报告
npm run build:report
```

### 部署配置
```nginx
# nginx 配置示例
server {
  listen 80;
  server_name yourdomain.com;
  root /var/www/html;
  index index.html;

  location / {
    try_files $uri $uri/ /index.html;
  }

  location /api {
    proxy_pass http://backend:8080;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
  }
}
```

## 📝 代码规范

### TypeScript
- 启用严格模式
- 为 props 和 emits 定义类型
- 使用接口定义数据结构

### Vue 3 最佳实践
- 优先使用 Composition API
- 合理使用 `ref` 和 `reactive`
- 组件命名使用 PascalCase

### 提交规范
```bash
# 功能开发
git commit -m "feat: 添加用户管理页面"

# 问题修复
git commit -m "fix: 修复登录状态检查问题"

# 样式调整
git commit -m "style: 优化首页布局"
```

## 🐛 问题排查

### 常见问题

1. **依赖包错误**
   ```bash
   # 清除缓存重新安装
   rm -rf node_modules package-lock.json
   npm install
   ```

2. **TypeScript 类型错误**
   ```bash
   # 重新生成类型声明
   npm run type-check
   ```

3. **开发服务器启动失败**
   ```bash
   # 检查端口占用
   lsof -ti:3000
   # 或使用其他端口
   npm run dev -- --port 3001
   ```

## 📞 支持

如遇到前端相关问题，请：
1. 查看控制台错误信息
2. 检查网络请求状态
3. 确认后端服务是否正常运行
4. 提交 Issue 并附上错误详情

---

更多详细信息请参考 [项目主 README](../../README.md) 