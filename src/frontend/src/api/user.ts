import request from "@/utils/request";

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

// 根据用户ID获取用户信息
export const getUserById = (userId: string) => {
  return request.get('/platform/user/getUserById', {
    params: { userId }
  });
};



// /**
//  *
//  * @param data
//  * @returns
//  */
// export const login = (data: any) => {
//   return request<any>({
//     url: "/auth/auth/login", // mock接口
//     method: "post",
//     data,
//   });
// };
//
// /**
//  *
//  * @param deptId
//  * @param file
//  * @returns
//  */
// export function importFile(deptId: number, file: File) {
//   const formData = new FormData();
//   formData.append("file", file);
//   return request({
//     url: "/api/v1/users/_import",
//     method: "post",
//     params: { deptId: deptId },
//     data: formData,
//     headers: {
//       "Content-Type": "multipart/form-data",
//     },
//   });
// }
//
// /**
//  *
//  * @param accessToken
//  * @returns
//  */
// export const getUserInfoByToken = (accessToken: string) => {
//   return request<any>({
//     url: "/auth/auth/getUserInfoByToken", // mock接口
//     method: "get",
//     params: {
//       accessToken,
//     },
//   });
// };
//
// /**
//  *
//  * @param refreshToken
//  * @returns
//  */
// export const refreshToken = (refreshToken: string) => {
//   return request<any>({
//     url: `/auth/auth/refreshToken/`, // mock接口
//     method: "get",
//     params: {
//       refreshToken,
//     },
//   });
// };
//
// /**
//  *
//  * @param data
//  * @returns
//  */
// export const loginByCode = (data: UserLogin) => {
//   return request<any>({
//     url: "/auth/auth/loginByCode", // mock接口
//     method: "post",
//     data,
//   });
// };
//
// /**
//  *
//  * @param currentPage
//  * @param pageSize
//  * @param userId
//  * @param type
//  * @returns
//  */
// export const getTrendPageByUser = (currentPage:number,pageSize:number,userId:string,type:number) => {
//   return request<any>({
//     url: `/platform/user/getTrendPageByUser/${currentPage}/${pageSize}`, // mock接口
//     method: "get",
//     params: {
//       userId,
//       type
//     },
//   });
// };
//
// /**
//  *
//  * @param userId
//  * @returns
//  */
// export const getUserById = (userId:string) => {
//   return request<any>({
//     url: `/platform/user/getUserById`, // mock接口
//     method: "get",
//     params: {
//       userId
//     },
//   });
// };
//
// /**
//  *
//  * @param userId
//  * @returns
//  */
// export const loginOut = (userId:string) => {
//   return request<any>({
//     url: `/auth/auth/loginOut`, // mock接口
//     method: "get",
//     params: {
//       userId
//     },
//   });
// };


