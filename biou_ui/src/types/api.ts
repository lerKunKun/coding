/**
 * API响应基础类型
 */
export interface ApiResponse<T = any> {
  code: number;
  message: string;
  data: T;
  timestamp: number;
}

/**
 * 分页查询参数
 */
export interface PageQuery {
  current: number;
  size: number;
}

/**
 * 分页响应数据
 */
export interface PageResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

/**
 * 审计日志查询参数
 */
export interface AuditLogQuery extends PageQuery {
  userId?: number;
  username?: string;
  operationType?: string;
  businessType?: string;
  module?: string;
  startTime?: string;
  endTime?: string;
  status?: number;
  ipAddress?: string;
}

/**
 * 系统日志查询参数
 */
export interface SystemLogQuery extends PageQuery {
  level?: string;
  logger?: string;
  message?: string;
  startTime?: string;
  endTime?: string;
  thread?: string;
  className?: string;
  methodName?: string;
}

/**
 * 登录日志查询参数
 */
export interface LoginLogQuery extends PageQuery {
  username?: string;
  loginType?: string;
  startTime?: string;
  endTime?: string;
  status?: number;
  ipAddress?: string;
  userAgent?: string;
  location?: string;
}

/**
 * 审计日志数据
 */
export interface AuditLog {
  id: number;
  userId: number;
  username: string;
  operationType: string;
  businessType: string;
  module: string;
  description: string;
  method: string;
  requestUrl: string;
  requestMethod: string;
  requestParams: string;
  responseData: string;
  ipAddress: string;
  userAgent: string;
  status: number;
  errorMessage?: string;
  executionTime: number;
  createTime: string;
}

/**
 * 系统日志数据
 */
export interface SystemLog {
  id: number;
  level: string;
  logger: string;
  message: string;
  thread: string;
  className: string;
  methodName: string;
  lineNumber: number;
  exception?: string;
  createTime: string;
}

/**
 * 登录日志数据
 */
export interface LoginLog {
  id: number;
  username: string;
  loginType: string;
  ipAddress: string;
  userAgent: string;
  location: string;
  status: number;
  errorMessage?: string;
  loginTime: string;
}

/**
 * 日志统计数据
 */
export interface LogStatistics {
  [key: string]: {
    [key: string]: number;
  };
}

/**
 * 用户信息
 */
export interface UserInfo {
  id: number;
  username: string;
  email?: string;
  phone?: string;
  avatar?: string;
  roles: string[];
  permissions: string[];
}

/**
 * 登录请求参数
 */
export interface LoginRequest {
  username: string;
  password: string;
  captcha?: string;
  rememberMe?: boolean;
}

/**
 * 登录响应数据
 */
export interface LoginResponse {
  token: string;
  refreshToken: string;
  userInfo: UserInfo;
  expiresIn: number;
} 