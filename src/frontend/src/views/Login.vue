<template>
  <div class="login-container">
    <div class="login-modal">
      <div class="login-content">
        <div class="left-section">
          <img class="logo" src="@/assets/logo.svg" alt="Academic Brain" />
          <div class="qrcode">
            <div class="qrcode-placeholder">
              <el-icon size="60"><QrCode /></el-icon>
            </div>
          </div>
          <div class="tip">使用手机扫码登录</div>
        </div>
        
        <div class="right-section">
          <div class="title">账号登录</div>
          
          <el-form 
            ref="loginFormRef" 
            :model="loginForm" 
            :rules="loginRules"
            class="login-form"
          >
            <el-form-item prop="username">
              <el-input
                v-model="loginForm.username"
                placeholder="输入用户名"
                size="large"
                clearable
              >
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            
            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="输入密码"
                size="large"
                show-password
                clearable
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            
            <el-form-item>
              <el-button 
                type="primary" 
                size="large" 
                class="login-btn"
                :loading="loading"
                @click="handleLogin"
              >
                登录
              </el-button>
            </el-form-item>
          </el-form>
          
          <div class="register-link">
            <span>还没有账号？</span>
            <router-link to="/register" class="link">立即注册</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { User, Lock, QrCode } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import type { LoginForm } from '@/api/auth'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive<LoginForm>({
  username: '',
  password: ''
})

const loginRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.loginUser(loginForm)
        ElMessage.success('登录成功')
        router.push('/')
      } catch (error: any) {
        ElMessage.error(error.message || '登录失败')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-modal {
  background: white;
  border-radius: 12px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  width: 100%;
  max-width: 900px;
}

.login-content {
  display: flex;
  min-height: 500px;
}

.left-section {
  flex: 1;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: white;
}

.logo {
  width: 80px;
  height: 80px;
  margin-bottom: 30px;
  border-radius: 50%;
  background: white;
  padding: 10px;
}

.qrcode {
  margin-bottom: 20px;
}

.qrcode-placeholder {
  width: 150px;
  height: 150px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
}

.tip {
  font-size: 14px;
  opacity: 0.9;
}

.right-section {
  flex: 1;
  padding: 60px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.title {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 40px;
  text-align: center;
}

.login-form {
  width: 100%;
}

.login-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
  border-radius: 8px;
}

.register-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.link {
  color: #409eff;
  text-decoration: none;
  margin-left: 5px;
}

.link:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .login-content {
    flex-direction: column;
  }
  
  .left-section {
    padding: 30px;
  }
  
  .right-section {
    padding: 30px;
  }
}
</style> 