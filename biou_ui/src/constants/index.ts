/**
 * API相关常量
 */
export const API_CONFIG = {
  BASE_URL: process.env.REACT_APP_API_BASE_URL || '/api',
  TIMEOUT: 10000,
};

/**
 * HTTP状态码
 */
export const HTTP_STATUS = {
  OK: 200,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  INTERNAL_SERVER_ERROR: 500,
};

/**
 * 业务状态码
 */
export const BUSINESS_CODE = {
  SUCCESS: 200,
  PARAM_ERROR: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  INTERNAL_ERROR: 500,
};

/**
 * 存储键名
 */
export const STORAGE_KEYS = {
  TOKEN: 'biou_token',
  REFRESH_TOKEN: 'biou_refresh_token',
  USER_INFO: 'biou_user_info',
  THEME: 'biou_theme',
  LANGUAGE: 'biou_language',
  SIDEBAR_COLLAPSED: 'biou_sidebar_collapsed',
};

/**
 * 操作类型枚举
 */
export const OPERATION_TYPE = {
  CREATE: 'CREATE',
  UPDATE: 'UPDATE',
  DELETE: 'DELETE',
  QUERY: 'QUERY',
  LOGIN: 'LOGIN',
  LOGOUT: 'LOGOUT',
  EXPORT: 'EXPORT',
  IMPORT: 'IMPORT',
};

/**
 * 业务类型枚举
 */
export const BUSINESS_TYPE = {
  USER: 'USER',
  ROLE: 'ROLE',
  PERMISSION: 'PERMISSION',
  SYSTEM: 'SYSTEM',
  LOG: 'LOG',
};

/**
 * 日志级别
 */
export const LOG_LEVEL = {
  TRACE: 'TRACE',
  DEBUG: 'DEBUG',
  INFO: 'INFO',
  WARN: 'WARN',
  ERROR: 'ERROR',
};

/**
 * 登录类型
 */
export const LOGIN_TYPE = {
  PASSWORD: 'PASSWORD',
  PHONE: 'PHONE',
  EMAIL: 'EMAIL',
  WECHAT: 'WECHAT',
  QQ: 'QQ',
};

/**
 * 状态枚举
 */
export const STATUS = {
  SUCCESS: 1,
  FAILED: 0,
  PENDING: 2,
  DISABLED: -1,
};

/**
 * 分页默认配置
 */
export const PAGINATION_CONFIG = {
  DEFAULT_PAGE_SIZE: 20,
  PAGE_SIZE_OPTIONS: ['10', '20', '50', '100'],
  SHOW_SIZE_CHANGER: true,
  SHOW_QUICK_JUMPER: true,
  SHOW_TOTAL: (total: number, range: [number, number]) =>
    `显示 ${range[0]}-${range[1]} 条，共 ${total} 条`,
};

/**
 * 表格默认配置
 */
export const TABLE_CONFIG = {
  SCROLL_Y: 'calc(100vh - 320px)',
  ROW_KEY: 'id',
  SIZE: 'middle' as const,
  BORDERED: false,
};

/**
 * 表单默认配置
 */
export const FORM_CONFIG = {
  LAYOUT: 'horizontal' as const,
  LABEL_COL: { span: 6 },
  WRAPPER_COL: { span: 18 },
  SIZE: 'middle' as const,
  VALIDATE_TRIGGER: ['onChange', 'onBlur'],
};

/**
 * 颜色主题
 */
export const THEME_COLORS = {
  PRIMARY: '#1664FF',
  SUCCESS: '#00D6B9',
  WARNING: '#FFAD1A',
  ERROR: '#FF4757',
  INFO: '#1E90FF',
  TEXT_PRIMARY: '#1A1A1A',
  TEXT_SECONDARY: '#666666',
  BORDER: '#E5E5E5',
  BACKGROUND: '#F7F8FA',
};

/**
 * 布局配置
 */
export const LAYOUT_CONFIG = {
  HEADER_HEIGHT: 64,
  SIDEBAR_WIDTH: 240,
  SIDEBAR_COLLAPSED_WIDTH: 80,
  FOOTER_HEIGHT: 48,
  CONTENT_PADDING: 24,
};

/**
 * 路由路径
 */
export const ROUTES = {
  LOGIN: '/login',
  DASHBOARD: '/dashboard',
  LOGS: '/logs',
  AUDIT_LOGS: '/logs/audit',
  SYSTEM_LOGS: '/logs/system',
  LOGIN_LOGS: '/logs/login',
  STATISTICS: '/statistics',
  SETTINGS: '/settings',
  PROFILE: '/profile',
};

/**
 * 菜单配置
 */
export const MENU_CONFIG = [
  {
    key: 'dashboard',
    icon: 'DashboardOutlined',
    label: '仪表盘',
    path: ROUTES.DASHBOARD,
  },
  {
    key: 'logs',
    icon: 'FileTextOutlined',
    label: '日志管理',
    children: [
      {
        key: 'audit-logs',
        label: '审计日志',
        path: ROUTES.AUDIT_LOGS,
      },
      {
        key: 'system-logs',
        label: '系统日志',
        path: ROUTES.SYSTEM_LOGS,
      },
      {
        key: 'login-logs',
        label: '登录日志',
        path: ROUTES.LOGIN_LOGS,
      },
    ],
  },
  {
    key: 'statistics',
    icon: 'BarChartOutlined',
    label: '统计分析',
    path: ROUTES.STATISTICS,
  },
];

/**
 * 正则表达式
 */
export const REGEX = {
  USERNAME: /^[a-zA-Z0-9_]{2,50}$/,
  PASSWORD: /^.{6,100}$/,
  EMAIL: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
  PHONE: /^1[3-9]\d{9}$/,
  IP: /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/,
}; 