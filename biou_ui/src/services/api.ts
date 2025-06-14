import request from '../utils/request';
import {
  ApiResponse,
  PageResult,
  AuditLog,
  SystemLog,
  LoginLog,
  AuditLogQuery,
  SystemLogQuery,
  LoginLogQuery,
  LogStatistics,
  LoginRequest,
  LoginResponse,
  UserInfo,
} from '../types/api';

/**
 * 认证相关API
 */
export const authApi = {
  /**
   * 用户登录
   */
  login: (data: LoginRequest): Promise<ApiResponse<LoginResponse>> => {
    return request.post('/auth/login', data);
  },

  /**
   * 用户登出
   */
  logout: (): Promise<ApiResponse<void>> => {
    return request.post('/auth/logout');
  },

  /**
   * 刷新Token
   */
  refreshToken: (): Promise<ApiResponse<{ token: string; expiresIn: number }>> => {
    return request.post('/auth/refresh-token');
  },

  /**
   * 获取当前用户信息
   */
  getCurrentUser: (): Promise<ApiResponse<UserInfo>> => {
    return request.get('/auth/current-user');
  },

  /**
   * 修改密码
   */
  changePassword: (data: {
    oldPassword: string;
    newPassword: string;
  }): Promise<ApiResponse<void>> => {
    return request.post('/auth/change-password', data);
  },
};

/**
 * 日志管理API
 */
export const logApi = {
  /**
   * 分页查询审计日志
   */
  getAuditLogs: (params: AuditLogQuery): Promise<ApiResponse<PageResult<AuditLog>>> => {
    return request.post('/log/audit/page', params);
  },

  /**
   * 分页查询系统日志
   */
  getSystemLogs: (params: SystemLogQuery): Promise<ApiResponse<PageResult<SystemLog>>> => {
    return request.post('/log/system/page', params);
  },

  /**
   * 分页查询登录日志
   */
  getLoginLogs: (params: LoginLogQuery): Promise<ApiResponse<PageResult<LoginLog>>> => {
    return request.post('/log/login/page', params);
  },

  /**
   * 查询审计日志（不分页）
   */
  queryAuditLogs: (params: Omit<AuditLogQuery, 'current' | 'size'>): Promise<ApiResponse<AuditLog[]>> => {
    return request.post('/log/audit', params);
  },

  /**
   * 查询系统日志（不分页）
   */
  querySystemLogs: (params: Omit<SystemLogQuery, 'current' | 'size'>): Promise<ApiResponse<SystemLog[]>> => {
    return request.post('/log/system', params);
  },

  /**
   * 查询登录日志（不分页）
   */
  queryLoginLogs: (params: Omit<LoginLogQuery, 'current' | 'size'>): Promise<ApiResponse<LoginLog[]>> => {
    return request.post('/log/login', params);
  },

  /**
   * 获取日志统计信息
   */
  getLogStatistics: (days?: number): Promise<ApiResponse<LogStatistics>> => {
    return request.get('/log/statistics', { days });
  },

  /**
   * 清理过期日志
   */
  cleanExpiredLogs: (retentionDays: number): Promise<ApiResponse<Record<string, number>>> => {
    return request.delete('/log/clean', { params: { retentionDays } });
  },

  /**
   * 清理审计日志
   */
  cleanAuditLogs: (days: number): Promise<ApiResponse<number>> => {
    return request.delete(`/log/audit/clean/${days}`);
  },

  /**
   * 清理所有类型日志
   */
  cleanAllLogs: (days: number): Promise<ApiResponse<number>> => {
    return request.delete(`/log/clean/${days}`);
  },

  /**
   * 导出审计日志
   */
  exportAuditLogs: (params: Omit<AuditLogQuery, 'current' | 'size'>): Promise<void> => {
    const queryString = new URLSearchParams(params as any).toString();
    return request.download(`/log/audit/export?${queryString}`, 'audit-logs.xlsx');
  },

  /**
   * 导出系统日志
   */
  exportSystemLogs: (params: Omit<SystemLogQuery, 'current' | 'size'>): Promise<void> => {
    const queryString = new URLSearchParams(params as any).toString();
    return request.download(`/log/system/export?${queryString}`, 'system-logs.xlsx');
  },

  /**
   * 导出登录日志
   */
  exportLoginLogs: (params: Omit<LoginLogQuery, 'current' | 'size'>): Promise<void> => {
    const queryString = new URLSearchParams(params as any).toString();
    return request.download(`/log/login/export?${queryString}`, 'login-logs.xlsx');
  },
};

/**
 * 系统管理API
 */
export const systemApi = {
  /**
   * 获取系统信息
   */
  getSystemInfo: (): Promise<ApiResponse<{
    name: string;
    version: string;
    description: string;
    author: string;
    buildTime: string;
    javaVersion: string;
    osName: string;
    osVersion: string;
    cpuCount: number;
    totalMemory: string;
    freeMemory: string;
    usedMemory: string;
  }>> => {
    return request.get('/system/info');
  },

  /**
   * 获取系统健康状态
   */
  getHealthStatus: (): Promise<ApiResponse<{
    status: string;
    database: string;
    redis: string;
    diskSpace: string;
    uptime: number;
  }>> => {
    return request.get('/system/health');
  },

  /**
   * 获取系统监控数据
   */
  getMonitorData: (): Promise<ApiResponse<{
    cpu: {
      usage: number;
      load: number[];
    };
    memory: {
      total: number;
      used: number;
      free: number;
      usage: number;
    };
    disk: {
      total: number;
      used: number;
      free: number;
      usage: number;
    };
    network: {
      received: number;
      sent: number;
    };
  }>> => {
    return request.get('/system/monitor');
  },
};

/**
 * 用户管理API
 */
export const userApi = {
  /**
   * 获取用户列表
   */
  getUserList: (params: {
    current: number;
    size: number;
    username?: string;
    email?: string;
    status?: number;
  }): Promise<ApiResponse<PageResult<{
    id: number;
    username: string;
    email: string;
    phone: string;
    avatar: string;
    status: number;
    createTime: string;
    lastLoginTime: string;
    roles: string[];
  }>>> => {
    return request.post('/user/page', params);
  },

  /**
   * 创建用户
   */
  createUser: (data: {
    username: string;
    password: string;
    email: string;
    phone?: string;
    roles: string[];
  }): Promise<ApiResponse<void>> => {
    return request.post('/user', data);
  },

  /**
   * 更新用户
   */
  updateUser: (id: number, data: {
    email?: string;
    phone?: string;
    status?: number;
    roles?: string[];
  }): Promise<ApiResponse<void>> => {
    return request.put(`/user/${id}`, data);
  },

  /**
   * 删除用户
   */
  deleteUser: (id: number): Promise<ApiResponse<void>> => {
    return request.delete(`/user/${id}`);
  },

  /**
   * 重置用户密码
   */
  resetPassword: (id: number): Promise<ApiResponse<{ password: string }>> => {
    return request.post(`/user/${id}/reset-password`);
  },

  /**
   * 启用/禁用用户
   */
  toggleUserStatus: (id: number, status: number): Promise<ApiResponse<void>> => {
    return request.patch(`/user/${id}/status`, { status });
  },
};

/**
 * 文件上传API
 */
export const uploadApi = {
  /**
   * 上传头像
   */
  uploadAvatar: (file: File): Promise<ApiResponse<{ url: string }>> => {
    return request.upload('/upload/avatar', file);
  },

  /**
   * 上传文件
   */
  uploadFile: (file: File, type?: string): Promise<ApiResponse<{ 
    url: string; 
    filename: string; 
    size: number; 
  }>> => {
    const formData = new FormData();
    formData.append('file', file);
    if (type) {
      formData.append('type', type);
    }
    return request.post('/upload/file', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  },
};

/**
 * 配置管理API
 */
export const configApi = {
  /**
   * 获取系统配置
   */
  getSystemConfig: (): Promise<ApiResponse<{
    siteName: string;
    siteDescription: string;
    siteLogo: string;
    registrationEnabled: boolean;
    emailVerificationRequired: boolean;
    maxLoginAttempts: number;
    lockoutDuration: number;
    passwordPolicy: {
      minLength: number;
      requireUppercase: boolean;
      requireLowercase: boolean;
      requireNumbers: boolean;
      requireSpecialChars: boolean;
    };
    sessionTimeout: number;
    logRetentionDays: number;
  }>> => {
    return request.get('/config/system');
  },

  /**
   * 更新系统配置
   */
  updateSystemConfig: (data: any): Promise<ApiResponse<void>> => {
    return request.put('/config/system', data);
  },

  /**
   * 获取邮件配置
   */
  getEmailConfig: (): Promise<ApiResponse<{
    enabled: boolean;
    host: string;
    port: number;
    username: string;
    ssl: boolean;
    fromName: string;
    fromEmail: string;
  }>> => {
    return request.get('/config/email');
  },

  /**
   * 更新邮件配置
   */
  updateEmailConfig: (data: any): Promise<ApiResponse<void>> => {
    return request.put('/config/email', data);
  },

  /**
   * 测试邮件配置
   */
  testEmailConfig: (email: string): Promise<ApiResponse<void>> => {
    return request.post('/config/email/test', { email });
  },
}; 