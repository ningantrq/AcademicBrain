<template>
  <div class="register-container">
    <div class="register-modal">
      <div class="register-content">
        <div class="left-section">
          <img class="logo" src="@/assets/logo.svg" alt="Academic Brain" />
          <div class="welcome-text">
            <h2>欢迎加入</h2>
            <p>Academic Brain</p>
            <p>开启您的学术之旅</p>
          </div>
        </div>
        
        <div class="right-section">
          <div class="title">创建账号</div>
          
          <el-form 
            ref="registerFormRef" 
            :model="registerForm" 
            :rules="registerRules"
            class="register-form"
          >
            <el-form-item prop="username">
              <el-input
                v-model="registerForm.username"
                placeholder="输入用户名"
                size="large"
                clearable
              >
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            
            <el-form-item prop="email">
              <el-input
                v-model="registerForm.email"
                placeholder="输入邮箱"
                size="large"
                clearable
              >
                <template #prefix>
                  <el-icon><Message /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            
            <el-form-item prop="password">
              <el-input
                v-model="registerForm.password"
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
            
            <el-form-item prop="confirmPassword">
              <el-input
                v-model="registerForm.confirmPassword"
                type="password"
                placeholder="确认密码"
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
                class="register-btn"
                :loading="loading"
                @click="handleRegister"
              >
                注册
              </el-button>
            </el-form-item>
          </el-form>
          
          <div class="login-link">
            <span>已有账号？</span>
            <router-link to="/login" class="link">立即登录</router-link>
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
import { User, Lock, Message } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import type { RegisterForm } from '@/api/auth'

const router = useRouter()
const userStore = useUserStore()

const registerFormRef = ref<FormInstance>()
const loading = ref(false)

const registerForm = reactive<RegisterForm>({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const registerRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.registerUser(registerForm)
        ElMessage.success('注册成功')
        router.push('/')
      } catch (error: any) {
        ElMessage.error(error.message || '注册失败')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.register-modal {
  background: white;
  border-radius: 12px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  width: 100%;
  max-width: 900px;
}

.register-content {
  display: flex;
  min-height: 600px;
}

.left-section {
  flex: 1;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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

.welcome-text {
  text-align: center;
}

.welcome-text h2 {
  font-size: 32px;
  margin-bottom: 10px;
  font-weight: bold;
}

.welcome-text p {
  font-size: 16px;
  margin: 5px 0;
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

.register-form {
  width: 100%;
}

.register-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
  border-radius: 8px;
}

.login-link {
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
  .register-content {
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