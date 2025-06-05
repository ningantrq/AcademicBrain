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

// export interface UserLogin {
//   phone: string;
//   email: string;
//   code: string;
//   username: string;
//   password: string;
// }
//
// export interface User {
//   id: string;
//   yxId: string;
//   username: string;
//   avatar: string;
//   gender: number;
//   description: string;
//   birthday: string;
//   address: string;
//   userCover: string;
//   trendCount: number;
//   followerCount: number;
//   fanCount: number;
// }
