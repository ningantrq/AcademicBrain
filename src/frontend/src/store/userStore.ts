
// import { storage } from "@/utils/storage";
// import { refreshToken } from "@/api/user";
// import { store } from "@/store";
// import type { User } from "@/type/user";


import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { LoginForm, RegisterForm, LoginResponse } from '@/api/user'
import { login, register, getUserInfo, logout } from '@/api/user'



export const useUserStore = defineStore('user', () => {
  // 用户信息
  const userInfo = ref<any>(null)
  const isLoggedIn = ref(false)

  // 登录
  const loginUser = async (loginForm: LoginForm) => {
    try {
      const response = await login(loginForm) as LoginResponse

      // 保存token
      localStorage.setItem('accessToken', response.accessToken)
      localStorage.setItem('refreshToken', response.refreshToken)

      // 保存用户信息
      userInfo.value = response.userInfo
      isLoggedIn.value = true

      return response
    } catch (error) {
      throw error
    }
  }

  // 注册
  const registerUser = async (registerForm: RegisterForm) => {
    try {
      const response = await register(registerForm) as LoginResponse

      // 保存token
      localStorage.setItem('accessToken', response.accessToken)
      localStorage.setItem('refreshToken', response.refreshToken)

      // 保存用户信息
      userInfo.value = response.userInfo
      isLoggedIn.value = true

      return response
    } catch (error) {
      throw error
    }
  }

  // 获取用户信息
  const fetchUserInfo = async () => {
    try {
      const response = await getUserInfo()
      userInfo.value = response
      isLoggedIn.value = true
      return response
    } catch (error) {
      throw error
    }
  }

  // 登出
  const logoutUser = async () => {
    try {
      await logout()
    } catch (error) {
      // 即使接口调用失败，也要清除本地数据
    } finally {
      // 清除本地数据
      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')
      userInfo.value = null
      isLoggedIn.value = false
    }
  }

  // 检查登录状态
  const checkLoginStatus = () => {
    const token = localStorage.getItem('accessToken')
    if (token) {
      fetchUserInfo().catch(() => {
        // 如果获取用户信息失败，清除token
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
      })
    }
  }

  // const isLogin = () => {
  //   const user = localStorage.get("userInfo") as User;
  //   return user != null && user != undefined;
  // };
  const isLogin = () => {
    return isLoggedIn.value;
  }
  const getToken = () => {
    return localStorage.getItem('accessToken')
  }

  return {
    userInfo,
    isLoggedIn,
    loginUser,
    registerUser,
    fetchUserInfo,
    logoutUser,
    checkLoginStatus,
    isLogin,     //  新增
    getToken     //  新增
  }
})

