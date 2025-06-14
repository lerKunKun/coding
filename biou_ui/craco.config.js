const CracoLessPlugin = require('craco-less');

module.exports = {
  plugins: [
    {
      plugin: CracoLessPlugin,
      options: {
        lessLoaderOptions: {
          lessOptions: {
            modifyVars: {
              // 字节跳动设计语言主题色
              '@primary-color': '#1664FF', // 主色调
              '@success-color': '#00D6B9', // 成功色
              '@warning-color': '#FFAD1A', // 警告色
              '@error-color': '#FF4757', // 错误色
              '@info-color': '#1E90FF', // 信息色
              '@heading-color': '#1A1A1A', // 标题色
              '@text-color': '#333333', // 主文本色
              '@text-color-secondary': '#666666', // 次要文本色
              '@disabled-color': '#CCCCCC', // 禁用色
              '@border-radius-base': '6px', // 基础圆角
              '@border-color-base': '#E5E5E5', // 边框色
              '@component-background': '#FFFFFF', // 组件背景色
              '@body-background': '#F7F8FA', // 页面背景色
              '@layout-header-background': '#FFFFFF', // 头部背景
              '@layout-sider-background': '#FFFFFF', // 侧边栏背景
              '@table-header-bg': '#FAFAFA', // 表格头部背景
              '@table-row-hover-bg': '#F7F8FA', // 表格行悬停背景
              // 字体相关
              '@font-size-base': '14px',
              '@font-size-lg': '16px',
              '@font-size-sm': '12px',
              '@line-height-base': '1.5715',
              // 阴影
              '@shadow-1-up': '0 -6px 16px -8px rgba(0, 0, 0, 0.08), 0 -9px 28px 0 rgba(0, 0, 0, 0.05), 0 -12px 48px 16px rgba(0, 0, 0, 0.03)',
              '@shadow-1-down': '0 6px 16px -8px rgba(0, 0, 0, 0.08), 0 9px 28px 0 rgba(0, 0, 0, 0.05), 0 12px 48px 16px rgba(0, 0, 0, 0.03)',
              '@shadow-1-left': '-6px 0 16px -8px rgba(0, 0, 0, 0.08), -9px 0 28px 0 rgba(0, 0, 0, 0.05), -12px 0 48px 16px rgba(0, 0, 0, 0.03)',
              '@shadow-1-right': '6px 0 16px -8px rgba(0, 0, 0, 0.08), 9px 0 28px 0 rgba(0, 0, 0, 0.05), 12px 0 48px 16px rgba(0, 0, 0, 0.03)',
              '@shadow-2': '0 3px 6px -4px rgba(0, 0, 0, 0.12), 0 6px 16px 0 rgba(0, 0, 0, 0.08), 0 9px 28px 8px rgba(0, 0, 0, 0.05)'
            },
            javascriptEnabled: true,
          },
        },
      },
    },
  ],
}; 