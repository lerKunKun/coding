import React from 'react';
import { Card, Typography, Space } from 'antd';
import { BarChartOutlined } from '@ant-design/icons';

const { Title, Text } = Typography;

const Statistics: React.FC = () => {
  return (
    <Card>
      <Space direction="vertical" size="large" style={{ width: '100%', textAlign: 'center' }}>
        <BarChartOutlined style={{ fontSize: '64px', color: '#1664FF' }} />
        <Title level={3}>统计分析</Title>
        <Text type="secondary">统计分析页面正在开发中...</Text>
      </Space>
    </Card>
  );
};

export default Statistics; 