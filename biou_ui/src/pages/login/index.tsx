import React, { useState } from 'react';
import { Form, Input, Button, Card, Typography, Checkbox, message, Space } from 'antd';
import { UserOutlined, LockOutlined, EyeInvisibleOutlined, EyeTwoTone } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { authApi } from '../../services/api';
import { STORAGE_KEYS, ROUTES, THEME_COLORS } from '../../constants';

import { validateRules } from '../../utils';

const { Title, Text } = Typography;

const LoginContainer = styled.div`
  min-height: 100vh;
  background: linear-gradient(135deg, ${THEME_COLORS.PRIMARY} 0%, #4A90E2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
`;

const LoginCard = styled(Card)`
  width: 100%;
  max-width: 400px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  border-radius: 12px;
  border: none;
  
  .ant-card-body {
    padding: 40px;
  }
`;

const LogoSection = styled.div`
  text-align: center;
  margin-bottom: 32px;
`;

const Logo = styled.div`
  width: 64px;
  height: 64px;
  background: ${THEME_COLORS.PRIMARY};
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  color: white;
  font-size: 24px;
  font-weight: bold;
`;

const StyledTitle = styled(Title)`
  &.ant-typography {
    color: ${THEME_COLORS.TEXT_PRIMARY};
    margin-bottom: 8px !important;
    font-weight: 600;
  }
`;

const StyledText = styled(Text)`
  color: ${THEME_COLORS.TEXT_SECONDARY};
  font-size: 14px;
`;

const LoginForm = styled(Form)`
  .ant-form-item {
    margin-bottom: 20px;
  }
  
  .ant-input-affix-wrapper {
    height: 44px;
    border-radius: 8px;
    border: 1px solid ${THEME_COLORS.BORDER};
    
    &:hover, &:focus, &.ant-input-affix-wrapper-focused {
      border-color: ${THEME_COLORS.PRIMARY};
      box-shadow: 0 0 0 2px rgba(22, 100, 255, 0.1);
    }
  }
  
  .ant-input {
    font-size: 14px;
  }
`;

const LoginButton = styled(Button)`
  width: 100%;
  height: 44px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  margin-top: 8px;
`;

const RememberSection = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
`;

const ForgotLink = styled.a`
  color: ${THEME_COLORS.PRIMARY};
  font-size: 14px;
  
  &:hover {
    color: ${THEME_COLORS.PRIMARY};
    text-decoration: underline;
  }
`;

const FooterSection = styled.div`
  text-align: center;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid ${THEME_COLORS.BORDER};
`;

const Login: React.FC = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (values: any) => {
    try {
      setLoading(true);
      const response = await authApi.login(values);
      
      if (response.data) {
        const { token, refreshToken, userInfo } = response.data;
        
        // 存储认证信息
        localStorage.setItem(STORAGE_KEYS.TOKEN, token);
        localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN, refreshToken);
        localStorage.setItem(STORAGE_KEYS.USER_INFO, JSON.stringify(userInfo));
        
        message.success('登录成功');
        navigate(ROUTES.DASHBOARD);
      }
    } catch (error: any) {
      console.error('Login error:', error);
      message.error(error.message || '登录失败，请检查用户名和密码');
    } finally {
      setLoading(false);
    }
  };

  const handleForgotPassword = () => {
    message.info('请联系管理员重置密码');
  };

  return (
    <LoginContainer>
      <LoginCard>
        <LogoSection>
          <Logo>B</Logo>
          <StyledTitle level={2}>BIOU 管理系统</StyledTitle>
          <StyledText>欢迎回来，请登录您的账户</StyledText>
        </LogoSection>

        <LoginForm
          form={form}
          name="login"
          onFinish={handleLogin}
          autoComplete="off"
          size="large"
        >
          <Form.Item
            name="username"
            rules={[
              { required: true, message: '请输入用户名' },
              validateRules.username,
            ]}
          >
            <Input
              prefix={<UserOutlined />}
              placeholder="请输入用户名"
              autoComplete="username"
            />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[
              { required: true, message: '请输入密码' },
              validateRules.password,
            ]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="请输入密码"
              autoComplete="current-password"
              iconRender={(visible) => 
                visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />
              }
            />
          </Form.Item>

          <RememberSection>
            <Form.Item name="rememberMe" valuePropName="checked" noStyle>
              <Checkbox>记住我</Checkbox>
            </Form.Item>
            <ForgotLink onClick={handleForgotPassword}>
              忘记密码？
            </ForgotLink>
          </RememberSection>

          <Form.Item>
            <LoginButton
              type="primary"
              htmlType="submit"
              loading={loading}
            >
              {loading ? '登录中...' : '登录'}
            </LoginButton>
          </Form.Item>
        </LoginForm>

        <FooterSection>
          <Space direction="vertical" size={4}>
            <StyledText>
              © 2024 BIOU Project. All rights reserved.
            </StyledText>
            <StyledText type="secondary">
              Version 1.0.0
            </StyledText>
          </Space>
        </FooterSection>
      </LoginCard>
    </LoginContainer>
  );
};

export default Login; 