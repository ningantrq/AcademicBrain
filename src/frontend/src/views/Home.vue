<template>
  <div class="home-container">
    <!-- 导航栏 -->
    <el-header class="header">
      <div class="header-content">
        <div class="logo-section">
          <img class="logo" src="@/assets/logo.svg" alt="Academic Brain" />
          <span class="title">Academic Brain</span>
        </div>
        
        <div class="nav-menu">
          <el-menu
            mode="horizontal"
            :default-active="activeIndex"
            class="nav-menu-items"
            @select="handleSelect"
          >
            <el-menu-item index="1">首页</el-menu-item>
            <el-menu-item index="2">知识库</el-menu-item>
            <el-menu-item index="3">学习计划</el-menu-item>
            <el-menu-item index="4">讨论区</el-menu-item>
          </el-menu>
        </div>
        
        <div class="user-section">
          <template v-if="userStore.isLoggedIn">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-avatar :size="32" :src="userInfo?.avatar">
                  <el-icon><User /></el-icon>
                </el-avatar>
                <span class="username">{{ userInfo?.username }}</span>
                <el-icon class="arrow-down"><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="settings">设置</el-dropdown-item>
                  <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="$router.push('/login')">登录</el-button>
            <el-button @click="$router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </el-header>

    <!-- 主要内容区域 -->
    <el-main class="main-content">
      <!-- 欢迎横幅 -->
      <div class="welcome-banner">
        <div class="banner-content">
          <h1>欢迎来到 Academic Brain</h1>
          <p>智能学术助手，让学习更高效</p>
          <div class="banner-actions">
            <el-button type="primary" size="large" @click="startLearning">
              开始学习
            </el-button>
            <el-button size="large" @click="exploreFeatures">
              探索功能
            </el-button>
          </div>
        </div>
        <div class="banner-image">
          <el-icon size="200" color="#409eff"><Reading /></el-icon>
        </div>
      </div>

      <!-- 功能模块 -->
      <div class="feature-modules">
        <h2>核心功能</h2>
        <el-row :gutter="24">
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-card class="feature-card" shadow="hover" @click="navigateToFeature('knowledge')">
              <div class="feature-icon">
                <el-icon size="48" color="#67c23a"><Collection /></el-icon>
              </div>
              <h3>知识库管理</h3>
              <p>构建个人知识体系，智能分类整理</p>
            </el-card>
          </el-col>
          
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-card class="feature-card" shadow="hover" @click="navigateToFeature('plan')">
              <div class="feature-icon">
                <el-icon size="48" color="#e6a23c"><Calendar /></el-icon>
              </div>
              <h3>学习计划</h3>
              <p>制定个性化学习路径，追踪进度</p>
            </el-card>
          </el-col>
          
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-card class="feature-card" shadow="hover" @click="navigateToFeature('discussion')">
              <div class="feature-icon">
                <el-icon size="48" color="#f56c6c"><ChatDotRound /></el-icon>
              </div>
              <h3>学术讨论</h3>
              <p>与同行交流，分享学术见解</p>
            </el-card>
          </el-col>
          
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-card class="feature-card" shadow="hover" @click="navigateToFeature('ai')">
              <div class="feature-icon">
                <el-icon size="48" color="#909399"><Cpu /></el-icon>
              </div>
              <h3>AI 助手</h3>
              <p>智能问答，学习建议和内容推荐</p>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 最近活动 -->
      <div class="recent-activity" v-if="userStore.isLoggedIn">
        <h2>最近活动</h2>
        <el-timeline>
          <el-timeline-item
            v-for="activity in recentActivities"
            :key="activity.id"
            :timestamp="activity.timestamp"
            :color="activity.color"
          >
            {{ activity.content }}
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  User, 
  ArrowDown, 
  Reading, 
  Collection, 
  Calendar, 
  ChatDotRound, 
  Cpu 
} from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const activeIndex = ref('1')

const userInfo = computed(() => userStore.userInfo)

const recentActivities = ref([
  {
    id: 1,
    content: '完成了《机器学习基础》章节学习',
    timestamp: '2024-01-15 14:30',
    color: '#67c23a'
  },
  {
    id: 2,
    content: '参与了"深度学习"讨论',
    timestamp: '2024-01-14 16:20',
    color: '#409eff'
  },
  {
    id: 3,
    content: '添加了新的学习笔记',
    timestamp: '2024-01-13 10:15',
    color: '#e6a23c'
  }
])

const handleSelect = (key: string) => {
  activeIndex.value = key
  // 根据选择的菜单项导航
  switch (key) {
    case '1':
      // 首页，不需要导航
      break
    case '2':
      navigateToFeature('knowledge')
      break
    case '3':
      navigateToFeature('plan')
      break
    case '4':
      navigateToFeature('discussion')
      break
  }
}

const handleCommand = async (command: string) => {
  switch (command) {
    case 'profile':
      ElMessage.info('个人中心功能开发中...')
      break
    case 'settings':
      ElMessage.info('设置功能开发中...')
      break
    case 'logout':
      await userStore.logoutUser()
      ElMessage.success('已退出登录')
      router.push('/login')
      break
  }
}

const startLearning = () => {
  if (userStore.isLoggedIn) {
    navigateToFeature('knowledge')
  } else {
    router.push('/login')
  }
}

const exploreFeatures = () => {
  ElMessage.info('功能探索页面开发中...')
}

const navigateToFeature = (feature: string) => {
  ElMessage.info(`${feature} 功能开发中...`)
}

onMounted(async () => {
  // 检查登录状态
  if (userStore.checkLoginStatus()) {
    try {
      await userStore.fetchUserInfo()
    } catch (error) {
      console.error('获取用户信息失败:', error)
    }
  }
})
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
  height: 64px;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  width: 40px;
  height: 40px;
  border-radius: 8px;
}

.title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.nav-menu {
  flex: 1;
  display: flex;
  justify-content: center;
}

.nav-menu-items {
  border-bottom: none;
}

.user-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #333;
}

.arrow-down {
  font-size: 12px;
  color: #999;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.welcome-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 60px;
  margin-bottom: 60px;
  color: white;
}

.banner-content h1 {
  font-size: 48px;
  margin-bottom: 16px;
  font-weight: bold;
}

.banner-content p {
  font-size: 20px;
  margin-bottom: 32px;
  opacity: 0.9;
}

.banner-actions {
  display: flex;
  gap: 16px;
}

.banner-image {
  opacity: 0.3;
}

.feature-modules {
  margin-bottom: 60px;
}

.feature-modules h2 {
  font-size: 32px;
  text-align: center;
  margin-bottom: 40px;
  color: #333;
}

.feature-card {
  text-align: center;
  padding: 20px;
  cursor: pointer;
  transition: transform 0.3s;
  margin-bottom: 20px;
}

.feature-card:hover {
  transform: translateY(-4px);
}

.feature-icon {
  margin-bottom: 16px;
}

.feature-card h3 {
  font-size: 18px;
  margin-bottom: 12px;
  color: #333;
}

.feature-card p {
  color: #666;
  line-height: 1.6;
}

.recent-activity h2 {
  font-size: 24px;
  margin-bottom: 24px;
  color: #333;
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 16px;
  }
  
  .nav-menu {
    display: none;
  }
  
  .welcome-banner {
    flex-direction: column;
    text-align: center;
    padding: 40px 20px;
  }
  
  .banner-content h1 {
    font-size: 32px;
  }
  
  .banner-image {
    margin-top: 20px;
  }
  
  .main-content {
    padding: 20px 16px;
  }
}
</style> 