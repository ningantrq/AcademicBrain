import request from '@/utils/request'

export interface LoginForm {
  username: string
  password: string
}

export interface RegisterForm {
  username: string
  password: string
  confirmPassword: string
  phone?: string
  email?: string
}

export interface LoginResponse {
  accessToken: string
  refreshToken: string
  userInfo: {
    id: string
    username: string
    avatar?: string
    email?: string
    phone?: string
    description?: string
  }
}

// 用户登录
export const login = (data: LoginForm) => {
  return request.post<LoginResponse>('/auth/login', data)
}

// 用户注册
export const register = (data: RegisterForm) => {
  return request.post<LoginResponse>('/auth/register', data)
}

// 获取用户信息
export const getUserInfo = () => {
  return request.get('/auth/userinfo')
}

// 用户登出
export const logout = () => {
  return request.post('/auth/logout')
}

// 刷新token
export const refreshToken = (refreshToken: string) => {
  return request.post('/auth/refresh', { refreshToken })
} 