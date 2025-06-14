# BIOU 前端管理系统

基于 React + TypeScript + Ant Design 构建的现代化后台管理系统，严格遵循字节跳动前端开发规范。

## 🚀 技术栈

- **框架**: React 19.1.0 + TypeScript 4.9.5
- **UI库**: Ant Design 5.26.0
- **路由**: React Router DOM 7.6.2
- **状态管理**: React Hooks
- **样式**: Styled Components + Less
- **HTTP客户端**: Axios 1.10.0
- **日期处理**: Day.js 1.11.13
- **构建工具**: Create React App + CRACO

## 📁 项目结构

```
src/
├── components/          # 组件目录
│   ├── common/         # 通用组件
│   ├── layout/         # 布局组件
│   └── business/       # 业务组件
├── pages/              # 页面目录
│   ├── login/          # 登录页面
│   ├── dashboard/      # 仪表盘
│   └── logs/           # 日志管理
├── services/           # API服务层
├── utils/              # 工具函数
├── types/              # TypeScript类型定义
├── constants/          # 常量定义
├── hooks/              # 自定义Hooks
├── stores/             # 状态管理
└── assets/             # 静态资源
    ├── images/         # 图片资源
    └── styles/         # 样式文件
```

## 🎨 设计规范

### 主题色彩
- **主色调**: #1664FF (字节跳动蓝)
- **成功色**: #00D6B9
- **警告色**: #FFAD1A
- **错误色**: #FF4757
- **信息色**: #1E90FF

### 组件规范
- 组件拆分：单个组件不超过 300 行代码
- 命名规范：PascalCase 命名组件，camelCase 命名函数和变量
- 类型安全：严格使用 TypeScript 类型定义
- 样式隔离：使用 Styled Components 避免样式冲突

## 🛠️ 开发指南

### 环境要求
- Node.js >= 16.0.0
- npm >= 8.0.0

### 安装依赖
```bash
npm install
```

### 启动开发服务器
```bash
npm start
```

### 构建生产版本
```bash
npm run build
```

### 运行测试
```bash
npm test
```

## 📋 功能模块

### 🔐 用户认证
- [x] 用户名密码登录
- [x] 登录状态保持
- [x] 自动登录过期处理
- [ ] 多因子认证（规划中）

### 📊 仪表盘
- [x] 系统概览统计
- [x] 实时数据展示
- [x] 快速操作入口
- [ ] 自定义仪表盘（规划中）

### 📝 日志管理
- [x] 审计日志查询
- [x] 高级搜索过滤
- [x] 日志详情查看
- [x] 数据导出功能
- [ ] 系统日志管理（开发中）
- [ ] 登录日志管理（开发中）

### 📈 统计分析
- [ ] 日志统计图表（规划中）
- [ ] 用户行为分析（规划中）
- [ ] 系统性能监控（规划中）

## 🔧 配置说明

### 环境变量
```bash
# API配置
REACT_APP_API_BASE_URL=http://localhost:8080/api

# 应用配置
REACT_APP_NAME=BIOU管理系统
REACT_APP_VERSION=1.0.0
```

### 主题定制
在 `craco.config.js` 中可以自定义 Ant Design 主题：

```javascript
modifyVars: {
  '@primary-color': '#1664FF',
  '@success-color': '#00D6B9',
  '@warning-color': '#FFAD1A',
  '@error-color': '#FF4757',
  // ... 更多主题变量
}
```

## 🚦 开发规范

### 代码规范
- 使用 ESLint + Prettier 进行代码格式化
- 遵循 React Hooks 最佳实践
- 组件必须包含 TypeScript 类型定义
- 使用函数式组件 + Hooks

### 提交规范
```bash
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 代码重构
test: 测试相关
chore: 构建工具或辅助工具的变动
```

### 组件开发规范
1. **单一职责**: 每个组件只负责一个功能
2. **可复用性**: 通用组件放在 `components/common`
3. **类型安全**: 所有 props 必须定义 TypeScript 接口
4. **性能优化**: 合理使用 `useMemo`、`useCallback`
5. **错误处理**: 组件内部要有错误边界处理

## 🔗 API 接口

### 认证接口
- `POST /auth/login` - 用户登录
- `POST /auth/logout` - 用户登出
- `GET /auth/current-user` - 获取当前用户信息

### 日志接口
- `POST /log/audit/page` - 分页查询审计日志
- `POST /log/system/page` - 分页查询系统日志
- `POST /log/login/page` - 分页查询登录日志
- `GET /log/statistics` - 获取日志统计信息

## 📱 响应式设计

系统采用响应式设计，支持以下设备：
- 桌面端：>= 1200px
- 平板端：768px - 1199px
- 手机端：< 768px

## 🔒 安全特性

- JWT Token 认证
- 请求拦截器自动添加认证头
- 敏感数据脱敏显示
- XSS 防护
- CSRF 防护

## 🐛 问题反馈

如果您在使用过程中遇到问题，请通过以下方式反馈：
1. 提交 Issue
2. 联系开发团队
3. 查看开发文档

## 📄 许可证

本项目采用 MIT 许可证，详情请查看 [LICENSE](LICENSE) 文件。

---

**开发团队**: BIOU Project Team  
**最后更新**: 2024年1月  
**版本**: v1.0.0
