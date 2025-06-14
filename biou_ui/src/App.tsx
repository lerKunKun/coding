import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ConfigProvider } from 'antd';
import zhCN from 'antd/locale/zh_CN';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
import { ROUTES, STORAGE_KEYS } from './constants';
import MainLayout from './components/layout/MainLayout';
import Login from './pages/login';
import Dashboard from './pages/dashboard';
import './assets/styles/global.css';

// 配置dayjs中文
dayjs.locale('zh-cn');

// 路由守卫组件
const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
  
  if (!token) {
    return <Navigate to={ROUTES.LOGIN} replace />;
  }
  
  return <>{children}</>;
};

// 公共路由组件（已登录用户重定向到仪表盘）
const PublicRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
  
  if (token) {
    return <Navigate to={ROUTES.DASHBOARD} replace />;
  }
  
  return <>{children}</>;
};

// 懒加载组件
const AuditLogs = React.lazy(() => import('./pages/logs/AuditLogs'));
const SystemLogs = React.lazy(() => import('./pages/logs/SystemLogs'));
const LoginLogs = React.lazy(() => import('./pages/logs/LoginLogs'));
const Statistics = React.lazy(() => import('./pages/dashboard/Statistics'));

const App: React.FC = () => {
  return (
    <ConfigProvider
      locale={zhCN}
      theme={{
        token: {
          colorPrimary: '#1664FF',
          colorSuccess: '#00D6B9',
          colorWarning: '#FFAD1A',
          colorError: '#FF4757',
          colorInfo: '#1E90FF',
          borderRadius: 6,
          fontSize: 14,
          fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',
        },
        components: {
          Layout: {
            headerBg: '#ffffff',
            siderBg: '#ffffff',
            bodyBg: '#f7f8fa',
          },
          Menu: {
            itemBg: 'transparent',
            itemSelectedBg: 'rgba(22, 100, 255, 0.06)',
            itemSelectedColor: '#1664FF',
            itemHoverBg: 'rgba(22, 100, 255, 0.06)',
            itemHoverColor: '#1664FF',
          },
          Button: {
            borderRadius: 6,
            controlHeight: 36,
          },
          Input: {
            borderRadius: 6,
            controlHeight: 36,
          },
          Card: {
            borderRadius: 8,
            boxShadow: '0 2px 8px rgba(0, 0, 0, 0.06)',
          },
          Table: {
            borderRadius: 8,
            headerBg: '#fafafa',
          },
        },
      }}
    >
      <Router>
        <Routes>
          {/* 公共路由 */}
          <Route
            path={ROUTES.LOGIN}
            element={
              <PublicRoute>
                <Login />
              </PublicRoute>
            }
          />

          {/* 受保护的路由 */}
          <Route
            path="/"
            element={
              <ProtectedRoute>
                <MainLayout />
              </ProtectedRoute>
            }
          >
            {/* 默认重定向到仪表盘 */}
            <Route index element={<Navigate to={ROUTES.DASHBOARD} replace />} />
            
            {/* 仪表盘 */}
            <Route path={ROUTES.DASHBOARD.slice(1)} element={<Dashboard />} />
            
            {/* 日志管理 */}
            <Route
              path={ROUTES.AUDIT_LOGS.slice(1)}
              element={
                <React.Suspense fallback={<div>加载中...</div>}>
                  <AuditLogs />
                </React.Suspense>
              }
            />
            <Route
              path={ROUTES.SYSTEM_LOGS.slice(1)}
              element={
                <React.Suspense fallback={<div>加载中...</div>}>
                  <SystemLogs />
                </React.Suspense>
              }
            />
            <Route
              path={ROUTES.LOGIN_LOGS.slice(1)}
              element={
                <React.Suspense fallback={<div>加载中...</div>}>
                  <LoginLogs />
                </React.Suspense>
              }
            />
            
            {/* 统计分析 */}
            <Route
              path={ROUTES.STATISTICS.slice(1)}
              element={
                <React.Suspense fallback={<div>加载中...</div>}>
                  <Statistics />
                </React.Suspense>
              }
            />
          </Route>

          {/* 404 页面 */}
          <Route path="*" element={<Navigate to={ROUTES.DASHBOARD} replace />} />
        </Routes>
      </Router>
    </ConfigProvider>
  );
};

export default App;
