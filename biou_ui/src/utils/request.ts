import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { message } from 'antd';
import { API_CONFIG, BUSINESS_CODE, STORAGE_KEYS, HTTP_STATUS } from '../constants';
import { ApiResponse } from '../types/api';

/**
 * 请求实例类
 */
class RequestClient {
  private instance: AxiosInstance;

  constructor() {
    this.instance = axios.create({
      baseURL: API_CONFIG.BASE_URL,
      timeout: API_CONFIG.TIMEOUT,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    this.setupInterceptors();
  }

  /**
   * 设置拦截器
   */
  private setupInterceptors(): void {
    // 请求拦截器
    this.instance.interceptors.request.use(
      (config) => {
        // 添加token
        const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }

        // 添加时间戳防止缓存
        if (config.method === 'get') {
          config.params = {
            ...config.params,
            _t: Date.now(),
          };
        }

        return config;
      },
      (error) => {
        console.error('Request interceptor error:', error);
        return Promise.reject(error);
      }
    );

    // 响应拦截器
    this.instance.interceptors.response.use(
      (response: AxiosResponse<ApiResponse>) => {
        const { data } = response;

        // 处理业务逻辑错误
        if (data.code !== BUSINESS_CODE.SUCCESS) {
          this.handleBusinessError(data);
          return Promise.reject(new Error(data.message));
        }

        // 返回修改后的response，保持AxiosResponse结构
        return { ...response, data };
      },
      (error) => {
        this.handleHttpError(error);
        return Promise.reject(error);
      }
    );
  }

  /**
   * 处理业务逻辑错误
   */
  private handleBusinessError(data: ApiResponse): void {
    const { code, message: errorMessage } = data;

    switch (code) {
      case BUSINESS_CODE.UNAUTHORIZED:
        this.handleUnauthorized();
        break;
      case BUSINESS_CODE.FORBIDDEN:
        message.error('权限不足，请联系管理员');
        break;
      case BUSINESS_CODE.NOT_FOUND:
        message.error('请求的资源不存在');
        break;
      default:
        message.error(errorMessage || '请求失败');
        break;
    }
  }

  /**
   * 处理HTTP错误
   */
  private handleHttpError(error: any): void {
    const { response } = error;

    if (!response) {
      message.error('网络连接异常，请检查网络设置');
      return;
    }

    const { status } = response;

    switch (status) {
      case HTTP_STATUS.UNAUTHORIZED:
        this.handleUnauthorized();
        break;
      case HTTP_STATUS.FORBIDDEN:
        message.error('权限不足，请联系管理员');
        break;
      case HTTP_STATUS.NOT_FOUND:
        message.error('请求的接口不存在');
        break;
      case HTTP_STATUS.INTERNAL_SERVER_ERROR:
        message.error('服务器内部错误，请稍后重试');
        break;
      default:
        message.error(`请求失败: ${status}`);
        break;
    }
  }

  /**
   * 处理未授权错误
   */
  private handleUnauthorized(): void {
    message.error('登录已过期，请重新登录');
    // 清除本地存储
    localStorage.removeItem(STORAGE_KEYS.TOKEN);
    localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
    localStorage.removeItem(STORAGE_KEYS.USER_INFO);
    // 跳转到登录页
    window.location.href = '/login';
  }

  /**
   * GET请求
   */
  public get<T = any>(
    url: string,
    params?: any,
    config?: AxiosRequestConfig
  ): Promise<ApiResponse<T>> {
    return this.instance.get(url, { params, ...config });
  }

  /**
   * POST请求
   */
  public post<T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<ApiResponse<T>> {
    return this.instance.post(url, data, config);
  }

  /**
   * PUT请求
   */
  public put<T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<ApiResponse<T>> {
    return this.instance.put(url, data, config);
  }

  /**
   * DELETE请求
   */
  public delete<T = any>(
    url: string,
    config?: AxiosRequestConfig
  ): Promise<ApiResponse<T>> {
    return this.instance.delete(url, config);
  }

  /**
   * PATCH请求
   */
  public patch<T = any>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig
  ): Promise<ApiResponse<T>> {
    return this.instance.patch(url, data, config);
  }

  /**
   * 上传文件
   */
  public upload<T = any>(
    url: string,
    file: File,
    onUploadProgress?: (progressEvent: any) => void
  ): Promise<ApiResponse<T>> {
    const formData = new FormData();
    formData.append('file', file);

    return this.instance.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress,
    });
  }

  /**
   * 下载文件
   */
  public download(url: string, filename?: string): Promise<void> {
    return this.instance.get(url, { responseType: 'blob' }).then((response) => {
      const blob = new Blob([response.data]);
      const downloadUrl = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = downloadUrl;
      link.download = filename || 'download';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(downloadUrl);
    });
  }
}

// 创建请求实例
const request = new RequestClient();

export default request; 