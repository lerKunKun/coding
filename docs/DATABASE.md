# 数据库设计文档

## 1. 用户权限相关表

### 1.1 用户表 (t_user)
```sql
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

### 1.2 角色表 (t_role)
```sql
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_code` varchar(50) NOT NULL COMMENT '角色编码',
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `description` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';
```

### 1.3 权限表 (t_permission)
```sql
CREATE TABLE `t_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `permission_code` varchar(100) NOT NULL COMMENT '权限编码',
  `permission_name` varchar(100) NOT NULL COMMENT '权限名称',
  `resource_type` varchar(20) NOT NULL COMMENT '资源类型：menu-菜单，button-按钮，api-接口',
  `resource_path` varchar(200) DEFAULT NULL COMMENT '资源路径',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父权限ID，0表示顶级权限',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序号',
  `description` varchar(200) DEFAULT NULL COMMENT '权限描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';
```

### 1.4 用户角色关联表 (t_user_role)
```sql
CREATE TABLE `t_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';
```

### 1.5 角色权限关联表 (t_role_permission)
```sql
CREATE TABLE `t_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';
```

## 2. 日志相关表

### 2.1 审计日志表 (t_audit_log)
```sql
CREATE TABLE `t_audit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation_type` varchar(20) NOT NULL COMMENT '操作类型',
  `business_type` varchar(20) NOT NULL COMMENT '业务类型',
  `module` varchar(50) NOT NULL COMMENT '模块名称',
  `description` varchar(200) DEFAULT NULL COMMENT '操作描述',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方法',
  `request_url` varchar(200) DEFAULT NULL COMMENT '请求URL',
  `request_params` text COMMENT '请求参数',
  `response_result` text COMMENT '响应结果',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `execution_time` bigint(20) DEFAULT NULL COMMENT '执行时间(毫秒)',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-失败，1-成功',
  `error_message` text COMMENT '错误信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审计日志表';
```

### 2.2 系统日志表 (t_system_log)
```sql
CREATE TABLE `t_system_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `level` varchar(10) NOT NULL COMMENT '日志级别',
  `thread` varchar(100) NOT NULL COMMENT '线程名',
  `logger` varchar(200) NOT NULL COMMENT '日志记录器名',
  `message` text NOT NULL COMMENT '日志消息',
  `trace_id` varchar(50) DEFAULT NULL COMMENT '链路追踪ID',
  `stack_trace` text COMMENT '堆栈信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_level` (`level`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';
```

### 2.3 登录日志表 (t_login_log)
```sql
CREATE TABLE `t_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `login_type` varchar(20) NOT NULL COMMENT '登录类型',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `location` varchar(100) DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(50) DEFAULT NULL COMMENT '浏览器',
  `os` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-失败，1-成功',
  `message` varchar(200) DEFAULT NULL COMMENT '提示消息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';
```

## 3. 索引设计

### 3.1 用户表索引
- 主键索引：id
- 唯一索引：username, email, phone
- 普通索引：status, create_time

### 3.2 角色表索引
- 主键索引：id
- 唯一索引：role_code
- 普通索引：status, create_time

### 3.3 权限表索引
- 主键索引：id
- 唯一索引：permission_code
- 普通索引：parent_id, status, create_time

### 3.4 日志表索引
- 主键索引：id
- 普通索引：user_id, create_time, level

## 4. 字段说明

### 4.1 通用字段
- id：主键，自增长
- create_time：创建时间，自动设置
- update_time：更新时间，自动更新
- deleted：逻辑删除标记

### 4.2 状态字段
- status：通用状态字段
  - 0：禁用
  - 1：启用

### 4.3 操作类型
- CREATE：创建
- UPDATE：更新
- DELETE：删除
- QUERY：查询
- LOGIN：登录
- LOGOUT：登出

### 4.4 资源类型
- menu：菜单
- button：按钮
- api：接口

## 5. 数据初始化

### 5.1 初始角色
```sql
INSERT INTO t_role (role_code, role_name, description) VALUES
('ADMIN', '系统管理员', '系统管理员，拥有所有权限'),
('USER_MANAGER', '用户管理员', '用户管理相关权限'),
('GUEST', '访客', '基本查看权限');
```

### 5.2 初始权限
```sql
INSERT INTO t_permission (permission_code, permission_name, resource_type, resource_path) VALUES
('SYSTEM:USER:CREATE', '创建用户', 'api', '/api/user'),
('SYSTEM:USER:UPDATE', '更新用户', 'api', '/api/user/*'),
('SYSTEM:USER:DELETE', '删除用户', 'api', '/api/user/*'),
('SYSTEM:USER:VIEW', '查看用户', 'api', '/api/user/*');
```

### 5.3 初始用户
```sql
INSERT INTO t_user (username, password, email, phone) VALUES
('admin', '{bcrypt}$2a$10$xxx', 'admin@example.com', '13800138000');
```

## 6. 性能优化

### 6.1 索引优化
- 合理使用索引
- 避免索引失效
- 定期维护索引
- 控制索引数量

### 6.2 表优化
- 合理的字段类型
- 适当的字段长度
- 避免过多字段
- 定期维护表

### 6.3 SQL优化
- 避免全表扫描
- 使用批量操作
- 合理使用子查询
- 避免过多关联 