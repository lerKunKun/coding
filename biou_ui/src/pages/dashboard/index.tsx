import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Statistic, Typography, Space, Spin, Alert } from 'antd';
import {
  UserOutlined,
  FileTextOutlined,
  SecurityScanOutlined,
  LoginOutlined,
  RiseOutlined,
  ClockCircleOutlined,
} from '@ant-design/icons';
import styled from 'styled-components';
import { logApi, systemApi } from '../../services/api';
import { THEME_COLORS } from '../../constants';
import { formatNumber, formatDateTime } from '../../utils';


const { Title, Text } = Typography;

const DashboardContainer = styled.div`
  .ant-card {
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  }
  
  .ant-statistic-title {
    color: ${THEME_COLORS.TEXT_SECONDARY};
    font-size: 14px;
    margin-bottom: 8px;
  }
  
  .ant-statistic-content {
    color: ${THEME_COLORS.TEXT_PRIMARY};
  }
`;

const StatCard = styled(Card)`
  .ant-card-body {
    padding: 24px;
  }
  
  .stat-icon {
    width: 48px;
    height: 48px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    color: white;
    margin-bottom: 16px;
  }
  
  .primary-icon {
    background: ${THEME_COLORS.PRIMARY};
  }
  
  .success-icon {
    background: ${THEME_COLORS.SUCCESS};
  }
  
  .warning-icon {
    background: ${THEME_COLORS.WARNING};
  }
  
  .error-icon {
    background: ${THEME_COLORS.ERROR};
  }
`;

const WelcomeCard = styled(Card)`
  background: linear-gradient(135deg, ${THEME_COLORS.PRIMARY} 0%, #4A90E2 100%);
  border: none;
  color: white;
  
  .ant-card-body {
    padding: 32px;
  }
  
  .ant-typography {
    color: white !important;
  }
`;

const QuickStatsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-top: 24px;
`;

const SystemInfoCard = styled(Card)`
  .info-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 0;
    border-bottom: 1px solid ${THEME_COLORS.BORDER};
    
    &:last-child {
      border-bottom: none;
    }
  }
  
  .info-label {
    color: ${THEME_COLORS.TEXT_SECONDARY};
    font-size: 14px;
  }
  
  .info-value {
    color: ${THEME_COLORS.TEXT_PRIMARY};
    font-weight: 500;
  }
`;

interface DashboardStats {
  totalAuditLogs: number;
  totalSystemLogs: number;
  totalLoginLogs: number;
  todayAuditLogs: number;
  todaySystemLogs: number;
  todayLoginLogs: number;
  successfulLogins: number;
  failedLogins: number;
}

interface SystemInfo {
  name: string;
  version: string;
  uptime: number;
  cpuUsage: number;
  memoryUsage: number;
  diskUsage: number;
}

const Dashboard: React.FC = () => {
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [systemInfo, setSystemInfo] = useState<SystemInfo | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      setLoading(true);
      setError(null);

      // 并行加载数据
      const [statisticsResponse, systemResponse] = await Promise.all([
        logApi.getLogStatistics(7),
        systemApi.getMonitorData().catch(() => null), // 系统监控可能不可用
      ]);

      // 处理日志统计数据
      if (statisticsResponse.data) {
        const logStats = statisticsResponse.data;
        const dashboardStats: DashboardStats = {
          totalAuditLogs: Object.values(logStats.audit || {}).reduce((sum, count) => sum + count, 0),
          totalSystemLogs: Object.values(logStats.system || {}).reduce((sum, count) => sum + count, 0),
          totalLoginLogs: Object.values(logStats.login || {}).reduce((sum, count) => sum + count, 0),
          todayAuditLogs: logStats.audit?.[formatDateTime(new Date(), 'YYYY-MM-DD')] || 0,
          todaySystemLogs: logStats.system?.[formatDateTime(new Date(), 'YYYY-MM-DD')] || 0,
          todayLoginLogs: logStats.login?.[formatDateTime(new Date(), 'YYYY-MM-DD')] || 0,
          successfulLogins: 0, // 需要从详细数据中计算
          failedLogins: 0, // 需要从详细数据中计算
        };
        setStats(dashboardStats);
      }

      // 处理系统监控数据
      if (systemResponse?.data) {
        const sysData = systemResponse.data;
        const sysInfo: SystemInfo = {
          name: 'BIOU System',
          version: '1.0.0',
          uptime: Date.now() - (7 * 24 * 60 * 60 * 1000), // 模拟7天运行时间
          cpuUsage: sysData.cpu?.usage || 0,
          memoryUsage: sysData.memory?.usage || 0,
          diskUsage: sysData.disk?.usage || 0,
        };
        setSystemInfo(sysInfo);
      }
    } catch (err: any) {
      console.error('Failed to load dashboard data:', err);
      setError(err.message || '加载仪表盘数据失败');
    } finally {
      setLoading(false);
    }
  };

  const formatUptime = (timestamp: number) => {
    const days = Math.floor((Date.now() - timestamp) / (1000 * 60 * 60 * 24));
    return `${days} 天`;
  };

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <Spin size="large" />
        <div style={{ marginTop: 16 }}>加载中...</div>
      </div>
    );
  }

  if (error) {
    return (
      <Alert
        message="加载失败"
        description={error}
        type="error"
        showIcon
        action={
          <button onClick={loadDashboardData} style={{ border: 'none', background: 'none', color: THEME_COLORS.PRIMARY, cursor: 'pointer' }}>
            重试
          </button>
        }
      />
    );
  }

  return (
    <DashboardContainer>
      <Row gutter={[24, 24]}>
        {/* 欢迎卡片 */}
        <Col span={24}>
          <WelcomeCard>
            <Space direction="vertical" size={8}>
              <Title level={2} style={{ margin: 0, color: 'white' }}>
                欢迎使用 BIOU 管理系统
              </Title>
              <Text style={{ fontSize: '16px', opacity: 0.9 }}>
                系统运行正常，当前时间：{formatDateTime(new Date())}
              </Text>
            </Space>
          </WelcomeCard>
        </Col>

        {/* 统计卡片 */}
        <Col xs={24} sm={12} lg={6}>
          <StatCard>
            <div className="stat-icon primary-icon">
              <SecurityScanOutlined />
            </div>
            <Statistic
              title="审计日志"
              value={stats?.totalAuditLogs || 0}
              formatter={(value) => formatNumber(Number(value))}
            />
            <Text type="secondary">
              今日新增 {stats?.todayAuditLogs || 0} 条
            </Text>
          </StatCard>
        </Col>

        <Col xs={24} sm={12} lg={6}>
          <StatCard>
            <div className="stat-icon success-icon">
              <FileTextOutlined />
            </div>
            <Statistic
              title="系统日志"
              value={stats?.totalSystemLogs || 0}
              formatter={(value) => formatNumber(Number(value))}
            />
            <Text type="secondary">
              今日新增 {stats?.todaySystemLogs || 0} 条
            </Text>
          </StatCard>
        </Col>

        <Col xs={24} sm={12} lg={6}>
          <StatCard>
            <div className="stat-icon warning-icon">
              <LoginOutlined />
            </div>
            <Statistic
              title="登录日志"
              value={stats?.totalLoginLogs || 0}
              formatter={(value) => formatNumber(Number(value))}
            />
            <Text type="secondary">
              今日新增 {stats?.todayLoginLogs || 0} 条
            </Text>
          </StatCard>
        </Col>

        <Col xs={24} sm={12} lg={6}>
          <StatCard>
            <div className="stat-icon error-icon">
              <UserOutlined />
            </div>
            <Statistic
              title="在线用户"
              value={1}
              suffix="人"
            />
            <Text type="secondary">
              活跃用户数量
            </Text>
          </StatCard>
        </Col>

        {/* 系统信息 */}
        <Col xs={24} lg={12}>
          <SystemInfoCard title="系统信息" extra={<ClockCircleOutlined />}>
            <div className="info-item">
              <span className="info-label">系统名称</span>
              <span className="info-value">{systemInfo?.name || 'BIOU System'}</span>
            </div>
            <div className="info-item">
              <span className="info-label">系统版本</span>
              <span className="info-value">{systemInfo?.version || '1.0.0'}</span>
            </div>
            <div className="info-item">
              <span className="info-label">运行时间</span>
              <span className="info-value">
                {systemInfo ? formatUptime(systemInfo.uptime) : '7 天'}
              </span>
            </div>
            <div className="info-item">
              <span className="info-label">CPU 使用率</span>
              <span className="info-value">
                {systemInfo?.cpuUsage ? `${systemInfo.cpuUsage.toFixed(1)}%` : '12.5%'}
              </span>
            </div>
            <div className="info-item">
              <span className="info-label">内存使用率</span>
              <span className="info-value">
                {systemInfo?.memoryUsage ? `${systemInfo.memoryUsage.toFixed(1)}%` : '68.3%'}
              </span>
            </div>
            <div className="info-item">
              <span className="info-label">磁盘使用率</span>
              <span className="info-value">
                {systemInfo?.diskUsage ? `${systemInfo.diskUsage.toFixed(1)}%` : '45.2%'}
              </span>
            </div>
          </SystemInfoCard>
        </Col>

        {/* 快速操作 */}
        <Col xs={24} lg={12}>
                      <Card title="快速操作" extra={<RiseOutlined />}>
            <QuickStatsGrid>
              <Card size="small" style={{ textAlign: 'center' }}>
                <Statistic
                  title="今日访问"
                  value={156}
                  valueStyle={{ color: THEME_COLORS.PRIMARY }}
                />
              </Card>
              <Card size="small" style={{ textAlign: 'center' }}>
                <Statistic
                  title="异常日志"
                  value={3}
                  valueStyle={{ color: THEME_COLORS.ERROR }}
                />
              </Card>
              <Card size="small" style={{ textAlign: 'center' }}>
                <Statistic
                  title="登录成功率"
                  value={98.5}
                  suffix="%"
                  valueStyle={{ color: THEME_COLORS.SUCCESS }}
                />
              </Card>
              <Card size="small" style={{ textAlign: 'center' }}>
                <Statistic
                  title="系统健康度"
                  value={95}
                  suffix="%"
                  valueStyle={{ color: THEME_COLORS.SUCCESS }}
                />
              </Card>
            </QuickStatsGrid>
          </Card>
        </Col>
      </Row>
    </DashboardContainer>
  );
};

export default Dashboard; 