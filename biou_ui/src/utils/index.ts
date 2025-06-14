import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
import relativeTime from 'dayjs/plugin/relativeTime';
import { REGEX } from '../constants';

// 配置dayjs
dayjs.locale('zh-cn');
dayjs.extend(relativeTime);

/**
 * 格式化日期时间
 */
export const formatDateTime = (
  date: string | Date | undefined,
  format: string = 'YYYY-MM-DD HH:mm:ss'
): string => {
  if (!date) return '-';
  return dayjs(date).format(format);
};

/**
 * 格式化日期
 */
export const formatDate = (date: string | Date | undefined): string => {
  return formatDateTime(date, 'YYYY-MM-DD');
};

/**
 * 格式化时间
 */
export const formatTime = (date: string | Date | undefined): string => {
  return formatDateTime(date, 'HH:mm:ss');
};

/**
 * 获取相对时间
 */
export const getRelativeTime = (date: string | Date): string => {
  return dayjs(date).fromNow();
};

/**
 * 获取时间范围
 */
export const getTimeRange = (days: number): [string, string] => {
  const end = dayjs().format('YYYY-MM-DD HH:mm:ss');
  const start = dayjs().subtract(days, 'day').format('YYYY-MM-DD HH:mm:ss');
  return [start, end];
};

/**
 * 防抖函数
 */
export const debounce = <T extends (...args: any[]) => any>(
  func: T,
  delay: number
): ((...args: Parameters<T>) => void) => {
  let timeoutId: NodeJS.Timeout;
  return (...args: Parameters<T>) => {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => func(...args), delay);
  };
};

/**
 * 节流函数
 */
export const throttle = <T extends (...args: any[]) => any>(
  func: T,
  delay: number
): ((...args: Parameters<T>) => void) => {
  let lastCall = 0;
  return (...args: Parameters<T>) => {
    const now = Date.now();
    if (now - lastCall >= delay) {
      lastCall = now;
      func(...args);
    }
  };
};

/**
 * 深拷贝
 */
export const deepClone = <T>(obj: T): T => {
  if (obj === null || typeof obj !== 'object') return obj;
  if (obj instanceof Date) return new Date(obj.getTime()) as any;
  if (obj instanceof Array) return obj.map(item => deepClone(item)) as any;
  if (typeof obj === 'object') {
    const clonedObj: any = {};
    for (const key in obj) {
      if (obj.hasOwnProperty(key)) {
        clonedObj[key] = deepClone(obj[key]);
      }
    }
    return clonedObj;
  }
  return obj;
};

/**
 * 判断是否为空值
 */
export const isEmpty = (value: any): boolean => {
  if (value === null || value === undefined) return true;
  if (typeof value === 'string') return value.trim() === '';
  if (Array.isArray(value)) return value.length === 0;
  if (typeof value === 'object') return Object.keys(value).length === 0;
  return false;
};

/**
 * 数据脱敏
 */
export const maskData = {
  // 手机号脱敏
  phone: (phone: string): string => {
    if (!phone || phone.length !== 11) return phone;
    return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
  },
  
  // 邮箱脱敏
  email: (email: string): string => {
    if (!email) return email;
    const [username, domain] = email.split('@');
    if (!username || !domain) return email;
    const maskedUsername = username.length > 2 
      ? username.slice(0, 2) + '***' + username.slice(-1)
      : username;
    return maskedUsername + '@' + domain;
  },
  
  // IP地址脱敏
  ip: (ip: string): string => {
    if (!ip) return ip;
    const parts = ip.split('.');
    if (parts.length !== 4) return ip;
    return parts[0] + '.' + parts[1] + '.***.' + parts[3];
  },
  
  // 身份证脱敏
  idCard: (idCard: string): string => {
    if (!idCard) return idCard;
    if (idCard.length === 15) {
      return idCard.replace(/(\d{6})\d{6}(\d{3})/, '$1******$2');
    } else if (idCard.length === 18) {
      return idCard.replace(/(\d{6})\d{8}(\d{4})/, '$1********$2');
    }
    return idCard;
  },
};

/**
 * 表单验证规则
 */
export const validateRules = {
  // 用户名验证
  username: {
    required: true,
    pattern: REGEX.USERNAME,
    message: '用户名必须是2-50位字母、数字或下划线',
  },
  
  // 密码验证
  password: {
    required: true,
    pattern: REGEX.PASSWORD,
    message: '密码长度必须在6-100位之间',
  },
  
  // 邮箱验证
  email: {
    pattern: REGEX.EMAIL,
    message: '邮箱格式不正确',
  },
  
  // 手机号验证
  phone: {
    pattern: REGEX.PHONE,
    message: '手机号格式不正确',
  },
  
  // IP地址验证
  ip: {
    pattern: REGEX.IP,
    message: 'IP地址格式不正确',
  },
};

/**
 * 文件大小格式化
 */
export const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

/**
 * 数字格式化
 */
export const formatNumber = (num: number, digits: number = 2): string => {
  if (num < 1000) return num.toString();
  if (num < 1000000) return (num / 1000).toFixed(digits) + 'K';
  if (num < 1000000000) return (num / 1000000).toFixed(digits) + 'M';
  return (num / 1000000000).toFixed(digits) + 'B';
};

/**
 * 生成随机字符串
 */
export const generateRandomString = (length: number = 8): string => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return result;
};

/**
 * 获取文件扩展名
 */
export const getFileExtension = (filename: string): string => {
  return filename.slice(((filename.lastIndexOf('.') - 1) >>> 0) + 2);
};

/**
 * 下载文件
 */
export const downloadFile = (url: string, filename: string): void => {
  const link = document.createElement('a');
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

/**
 * 复制到剪贴板
 */
export const copyToClipboard = async (text: string): Promise<boolean> => {
  try {
    await navigator.clipboard.writeText(text);
    return true;
  } catch (err) {
    console.error('Failed to copy text: ', err);
    return false;
  }
};

/**
 * 获取查询参数
 */
export const getQueryParams = (url?: string): Record<string, string> => {
  const urlToUse = url || window.location.href;
  const params: Record<string, string> = {};
  const urlObj = new URL(urlToUse);
  
  urlObj.searchParams.forEach((value, key) => {
    params[key] = value;
  });
  
  return params;
};

/**
 * 安全的 JSON 解析
 */
export const safeJsonParse = <T = any>(jsonString: string | null, defaultValue: T | null = null): T | null => {
  if (!jsonString || jsonString === 'undefined' || jsonString === 'null') {
    return defaultValue;
  }
  
  try {
    return JSON.parse(jsonString);
  } catch (error) {
    console.error('Failed to parse JSON:', error, 'Input:', jsonString);
    return defaultValue;
  }
};

/**
 * 安全的 localStorage 获取和解析
 */
export const getStorageItem = <T = any>(key: string, defaultValue: T | null = null): T | null => {
  try {
    const item = localStorage.getItem(key);
    return safeJsonParse<T>(item, defaultValue);
  } catch (error) {
    console.error('Failed to get item from localStorage:', error, 'Key:', key);
    return defaultValue;
  }
};

/**
 * 设置查询参数
 */
export const setQueryParams = (params: Record<string, string>): void => {
  const url = new URL(window.location.href);
  Object.entries(params).forEach(([key, value]) => {
    if (value) {
      url.searchParams.set(key, value);
    } else {
      url.searchParams.delete(key);
    }
  });
  window.history.replaceState({}, '', url.toString());
};

/**
 * 获取浏览器信息
 */
export const getBrowserInfo = (): {
  name: string;
  version: string;
  os: string;
} => {
  const userAgent = navigator.userAgent;
  let browserName = 'Unknown';
  let browserVersion = 'Unknown';
  let osName = 'Unknown';

  // 检测浏览器
  if (userAgent.indexOf('Chrome') > -1) {
    browserName = 'Chrome';
    browserVersion = userAgent.match(/Chrome\/(\d+)/)?.[1] || 'Unknown';
  } else if (userAgent.indexOf('Firefox') > -1) {
    browserName = 'Firefox';
    browserVersion = userAgent.match(/Firefox\/(\d+)/)?.[1] || 'Unknown';
  } else if (userAgent.indexOf('Safari') > -1) {
    browserName = 'Safari';
    browserVersion = userAgent.match(/Version\/(\d+)/)?.[1] || 'Unknown';
  } else if (userAgent.indexOf('Edge') > -1) {
    browserName = 'Edge';
    browserVersion = userAgent.match(/Edge\/(\d+)/)?.[1] || 'Unknown';
  }

  // 检测操作系统
  if (userAgent.indexOf('Windows') > -1) {
    osName = 'Windows';
  } else if (userAgent.indexOf('Mac') > -1) {
    osName = 'macOS';
  } else if (userAgent.indexOf('Linux') > -1) {
    osName = 'Linux';
  } else if (userAgent.indexOf('Android') > -1) {
    osName = 'Android';
  } else if (userAgent.indexOf('iOS') > -1) {
    osName = 'iOS';
  }

  return {
    name: browserName,
    version: browserVersion,
    os: osName,
  };
};

/**
 * 树形数据处理
 */
export const treeUtils = {
  // 数组转树形结构
  arrayToTree: <T extends { id: any; parentId?: any }>(
    array: T[],
    options: {
      idKey?: string;
      parentIdKey?: string;
      childrenKey?: string;
      rootValue?: any;
    } = {}
  ): T[] => {
    const {
      idKey = 'id',
      parentIdKey = 'parentId',
      childrenKey = 'children',
      rootValue = null,
    } = options;

    const tree: T[] = [];
    const map = new Map();

    // 创建映射
    array.forEach(item => {
      map.set(item[idKey as keyof T], { ...item, [childrenKey]: [] });
    });

    // 构建树形结构
    array.forEach(item => {
      const parentId = item[parentIdKey as keyof T];
      if (parentId === rootValue) {
        tree.push(map.get(item[idKey as keyof T]));
      } else {
        const parent = map.get(parentId);
        if (parent) {
          parent[childrenKey].push(map.get(item[idKey as keyof T]));
        }
      }
    });

    return tree;
  },

  // 树形结构转数组
  treeToArray: <T extends { children?: T[] }>(
    tree: T[],
    childrenKey: string = 'children'
  ): T[] => {
    const result: T[] = [];
    
    const traverse = (nodes: T[]) => {
      nodes.forEach(node => {
        const { [childrenKey]: children, ...rest } = node as any;
        result.push(rest);
        if (children && children.length > 0) {
          traverse(children);
        }
      });
    };

    traverse(tree);
    return result;
  },

  // 查找树节点
  findNode: <T extends { children?: T[] }>(
    tree: T[],
    predicate: (node: T) => boolean,
    childrenKey: string = 'children'
  ): T | null => {
    for (const node of tree) {
      if (predicate(node)) {
        return node;
      }
      const children = node[childrenKey as keyof T] as T[] | undefined;
      if (children && children.length > 0) {
        const found = treeUtils.findNode(children, predicate, childrenKey);
        if (found) {
          return found;
        }
      }
    }
    return null;
  },
}; 