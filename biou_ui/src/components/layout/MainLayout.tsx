import React, { useState, useEffect } from 'react';
import { Layout, Menu, Avatar, Dropdown, Typography, Space, Button, Badge } from 'antd';
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserOutlined,
  LogoutOutlined,
  SettingOutlined,
  BellOutlined,
  DashboardOutlined,
  FileTextOutlined,
  BarChartOutlined,
} from '@ant-design/icons';
import { useNavigate, useLocation, Outlet } from 'react-router-dom';
import styled from 'styled-components';
import { LAYOUT_CONFIG, THEME_COLORS, STORAGE_KEYS, ROUTES } from '../../constants';
import { authApi } from '../../services/api';
import { UserInfo } from '../../types/api';
import { getStorageItem } from '../../utils';

const { Header, Sider, Content } = Layout;
const { Text } = Typography;

const StyledLayout = styled(Layout)`
  min-height: 100vh;
`;

const StyledSider = styled(Sider)`
  .ant-layout-sider-trigger {
    background: ${THEME_COLORS.PRIMARY};
    color: white;
    
    &:hover {
      background: #0f4fb8;
    }
  }
`;

const StyledHeader = styled(Header)`
  background: white;
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: sticky;
  top: 0;
  z-index: 100;
`;

const HeaderLeft = styled.div`
  display: flex;
  align-items: center;
  gap: 16px;
`;

const HeaderRight = styled.div`
  display: flex;
  align-items: center;
  gap: 16px;
`;

const TriggerButton = styled(Button)`
  border: none;
  box-shadow: none;
  color: ${THEME_COLORS.TEXT_PRIMARY};
  
  &:hover, &:focus {
    color: ${THEME_COLORS.PRIMARY};
    background: rgba(22, 100, 255, 0.06);
  }
`;

const Logo = styled.div`
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  border-bottom: 1px solid ${THEME_COLORS.BORDER};
  
  .logo-icon {
    width: 32px;
    height: 32px;
    background: ${THEME_COLORS.PRIMARY};
    border-radius: 6px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-weight: bold;
    font-size: 16px;
    margin-right: 8px;
  }
  
  .logo-text {
    font-size: 18px;
    font-weight: 600;
    color: ${THEME_COLORS.TEXT_PRIMARY};
  }
`;

const StyledContent = styled(Content)`
  margin: 24px;
  padding: 24px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  min-height: calc(100vh - ${LAYOUT_CONFIG.HEADER_HEIGHT + 48}px);
`;

const UserSection = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 6px;
  transition: background-color 0.2s;
  
  &:hover {
    background: rgba(22, 100, 255, 0.06);
  }
`;

const NotificationButton = styled(Button)`
  border: none;
  box-shadow: none;
  
  &:hover, &:focus {
    background: rgba(22, 100, 255, 0.06);
  }
`;

interface MainLayoutProps {
  children?: React.ReactNode;
}

const MainLayout: React.FC<MainLayoutProps> = ({ children }) => {
  const [collapsed, setCollapsed] = useState(false);
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    // 获取用户信息
    const userInfo = getStorageItem<UserInfo>(STORAGE_KEYS.USER_INFO);
    if (userInfo) {
      setUserInfo(userInfo);
    }

    // 获取侧边栏折叠状态
    const collapsed = getStorageItem<boolean>(STORAGE_KEYS.SIDEBAR_COLLAPSED, false);
    setCollapsed(collapsed || false);
  }, []);

  const handleCollapse = (collapsed: boolean) => {
    setCollapsed(collapsed);
    localStorage.setItem(STORAGE_KEYS.SIDEBAR_COLLAPSED, JSON.stringify(collapsed));
  };

  const handleLogout = async () => {
    try {
      await authApi.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      // 清除本地存储
      localStorage.removeItem(STORAGE_KEYS.TOKEN);
      localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
      localStorage.removeItem(STORAGE_KEYS.USER_INFO);
      navigate(ROUTES.LOGIN);
    }
  };

  const handleProfile = () => {
    navigate(ROUTES.PROFILE);
  };

  const handleSettings = () => {
    navigate(ROUTES.SETTINGS);
  };

  const userMenuItems = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: '个人资料',
      onClick: handleProfile,
    },
    {
      key: 'settings',
      icon: <SettingOutlined />,
      label: '系统设置',
      onClick: handleSettings,
    },
    {
      type: 'divider' as const,
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: handleLogout,
    },
  ];

  const menuItems = [
    {
      key: ROUTES.DASHBOARD,
      icon: <DashboardOutlined />,
      label: '仪表盘',
    },
    {
      key: 'logs',
      icon: <FileTextOutlined />,
      label: '日志管理',
      children: [
        {
          key: ROUTES.AUDIT_LOGS,
          label: '审计日志',
        },
        {
          key: ROUTES.SYSTEM_LOGS,
          label: '系统日志',
        },
        {
          key: ROUTES.LOGIN_LOGS,
          label: '登录日志',
        },
      ],
    },
    {
      key: ROUTES.STATISTICS,
      icon: <BarChartOutlined />,
      label: '统计分析',
    },
  ];

  const handleMenuClick = ({ key }: { key: string }) => {
    navigate(key);
  };

  const getSelectedKeys = () => {
    const path = location.pathname;
    if (path.startsWith('/logs/')) {
      return [path];
    }
    return [path];
  };

  const getOpenKeys = () => {
    const path = location.pathname;
    if (path.startsWith('/logs/')) {
      return ['logs'];
    }
    return [];
  };

  return (
    <StyledLayout>
      <StyledSider
        trigger={null}
        collapsible
        collapsed={collapsed}
        onCollapse={handleCollapse}
        width={LAYOUT_CONFIG.SIDEBAR_WIDTH}
        collapsedWidth={LAYOUT_CONFIG.SIDEBAR_COLLAPSED_WIDTH}
      >
        <Logo>
          <div className="logo-icon">B</div>
          {!collapsed && <div className="logo-text">BIOU</div>}
        </Logo>
        <Menu
          theme="light"
          mode="inline"
          selectedKeys={getSelectedKeys()}
          defaultOpenKeys={getOpenKeys()}
          items={menuItems}
          onClick={handleMenuClick}
          style={{ borderRight: 0 }}
        />
      </StyledSider>

      <Layout>
        <StyledHeader>
          <HeaderLeft>
            <TriggerButton
              type="text"
              icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
              onClick={() => handleCollapse(!collapsed)}
            />
          </HeaderLeft>

          <HeaderRight>
            <NotificationButton
              type="text"
              icon={
                <Badge count={0} size="small">
                  <BellOutlined />
                </Badge>
              }
            />

            <Dropdown
              menu={{ items: userMenuItems }}
              placement="bottomRight"
              arrow
            >
              <UserSection>
                <Avatar
                  size="small"
                  icon={<UserOutlined />}
                  src={userInfo?.avatar}
                />
                {userInfo && (
                  <Space direction="vertical" size={0}>
                    <Text strong style={{ fontSize: '14px' }}>
                      {userInfo.username}
                    </Text>
                  </Space>
                )}
              </UserSection>
            </Dropdown>
          </HeaderRight>
        </StyledHeader>

        <StyledContent>
          {children || <Outlet />}
        </StyledContent>
      </Layout>
    </StyledLayout>
  );
};

export default MainLayout; 