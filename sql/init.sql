-- 创建数据库
CREATE DATABASE IF NOT EXISTS `biou_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `biou_db`;

-- 创建用户表
DROP TABLE IF EXISTS `t_user`;
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
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建角色表
DROP TABLE IF EXISTS `t_role`;
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
  UNIQUE KEY `uk_role_code` (`role_code`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 创建权限表
DROP TABLE IF EXISTS `t_permission`;
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
  UNIQUE KEY `uk_permission_code` (`permission_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_resource_type` (`resource_type`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 创建用户角色关联表
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`),
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 创建角色权限关联表
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`),
  CONSTRAINT `fk_role_permission_role` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_role_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `t_permission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 创建审计日志表
DROP TABLE IF EXISTS `t_audit_log`;
CREATE TABLE `t_audit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '操作用户名',
  `operation_type` varchar(20) NOT NULL COMMENT '操作类型：CREATE-创建，UPDATE-更新，DELETE-删除，QUERY-查询，LOGIN-登录，LOGOUT-登出',
  `business_type` varchar(50) NOT NULL COMMENT '业务类型：USER-用户管理，ROLE-角色管理，PERMISSION-权限管理，SYSTEM-系统管理',
  `module` varchar(50) NOT NULL COMMENT '操作模块',
  `description` varchar(500) NOT NULL COMMENT '操作描述',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `request_url` varchar(500) DEFAULT NULL COMMENT '请求URL',
  `request_method` varchar(10) DEFAULT NULL COMMENT 'HTTP方法',
  `request_params` text DEFAULT NULL COMMENT '请求参数',
  `response_data` text DEFAULT NULL COMMENT '响应数据',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(1000) DEFAULT NULL COMMENT '用户代理',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '操作状态：0-失败，1-成功',
  `error_message` varchar(1000) DEFAULT NULL COMMENT '错误信息',
  `execution_time` bigint(20) DEFAULT NULL COMMENT '执行时间(毫秒)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_username` (`username`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_business_type` (`business_type`),
  KEY `idx_module` (`module`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_ip_address` (`ip_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审计日志表';

-- 创建系统日志表
DROP TABLE IF EXISTS `t_system_log`;
CREATE TABLE `t_system_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `trace_id` varchar(64) DEFAULT NULL COMMENT '链路追踪ID',
  `level` varchar(10) NOT NULL COMMENT '日志级别：DEBUG,INFO,WARN,ERROR',
  `logger_name` varchar(200) NOT NULL COMMENT '日志记录器名称',
  `message` text NOT NULL COMMENT '日志消息',
  `exception` text DEFAULT NULL COMMENT '异常信息',
  `thread_name` varchar(100) DEFAULT NULL COMMENT '线程名称',
  `class_name` varchar(200) DEFAULT NULL COMMENT '类名',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名',
  `line_number` int(11) DEFAULT NULL COMMENT '行号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_trace_id` (`trace_id`),
  KEY `idx_level` (`level`),
  KEY `idx_logger_name` (`logger_name`),
  KEY `idx_class_name` (`class_name`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';

-- 创建登录日志表
DROP TABLE IF EXISTS `t_login_log`;
CREATE TABLE `t_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `login_type` varchar(20) NOT NULL COMMENT '登录类型：LOGIN-登录，LOGOUT-登出',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(1000) DEFAULT NULL COMMENT '用户代理',
  `location` varchar(200) DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(100) DEFAULT NULL COMMENT '浏览器',
  `os` varchar(100) DEFAULT NULL COMMENT '操作系统',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '登录状态：0-失败，1-成功',
  `message` varchar(500) DEFAULT NULL COMMENT '提示消息',
  `login_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_username` (`username`),
  KEY `idx_login_type` (`login_type`),
  KEY `idx_ip_address` (`ip_address`),
  KEY `idx_status` (`status`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- 插入测试数据

-- 插入用户数据
INSERT INTO `t_user` (`username`, `password`, `email`, `phone`, `status`) VALUES
('admin', '123456', 'admin@biou.com', '13800138000', 1),
('user1', '123456', 'user1@biou.com', '13800138001', 1),
('user2', '123456', 'user2@biou.com', '13800138002', 1),
('user3', '123456', 'user3@biou.com', '13800138003', 0),
('user4', '123456', 'user4@biou.com', '13800138004', 1);

-- 插入角色数据
INSERT INTO `t_role` (`role_code`, `role_name`, `description`, `status`) VALUES
('ADMIN', '系统管理员', '拥有系统全部权限', 1),
('USER_MANAGER', '用户管理员', '负责用户管理相关功能', 1),
('GUEST', '访客', '只有基本的查看权限', 1),
('OPERATOR', '操作员', '负责日常业务操作', 1);

-- 插入权限数据
INSERT INTO `t_permission` (`permission_code`, `permission_name`, `resource_type`, `resource_path`, `parent_id`, `sort_order`, `description`, `status`) VALUES
-- 系统管理
('SYSTEM', '系统管理', 'menu', '/system', 0, 100, '系统管理模块', 1),
('SYSTEM:USER', '用户管理', 'menu', '/system/user', 1, 110, '用户管理菜单', 1),
('SYSTEM:USER:LIST', '用户列表', 'api', '/api/user/page', 2, 111, '查看用户列表', 1),
('SYSTEM:USER:CREATE', '创建用户', 'api', '/api/user', 2, 112, '创建新用户', 1),
('SYSTEM:USER:UPDATE', '更新用户', 'api', '/api/user/*', 2, 113, '更新用户信息', 1),
('SYSTEM:USER:DELETE', '删除用户', 'api', '/api/user/*', 2, 114, '删除用户', 1),
('SYSTEM:USER:STATUS', '用户状态', 'api', '/api/user/*/status', 2, 115, '修改用户状态', 1),

-- 角色管理
('SYSTEM:ROLE', '角色管理', 'menu', '/system/role', 1, 120, '角色管理菜单', 1),
('SYSTEM:ROLE:LIST', '角色列表', 'api', '/api/role/page', 8, 121, '查看角色列表', 1),
('SYSTEM:ROLE:CREATE', '创建角色', 'api', '/api/role', 8, 122, '创建新角色', 1),
('SYSTEM:ROLE:UPDATE', '更新角色', 'api', '/api/role/*', 8, 123, '更新角色信息', 1),
('SYSTEM:ROLE:DELETE', '删除角色', 'api', '/api/role/*', 8, 124, '删除角色', 1),
('SYSTEM:ROLE:ASSIGN', '分配权限', 'api', '/api/role/*/permissions', 8, 125, '为角色分配权限', 1),

-- 权限管理
('SYSTEM:PERMISSION', '权限管理', 'menu', '/system/permission', 1, 130, '权限管理菜单', 1),
('SYSTEM:PERMISSION:LIST', '权限列表', 'api', '/api/permission/list', 14, 131, '查看权限列表', 1),
('SYSTEM:PERMISSION:CREATE', '创建权限', 'api', '/api/permission', 14, 132, '创建新权限', 1),
('SYSTEM:PERMISSION:UPDATE', '更新权限', 'api', '/api/permission/*', 14, 133, '更新权限信息', 1),
('SYSTEM:PERMISSION:DELETE', '删除权限', 'api', '/api/permission/*', 14, 134, '删除权限', 1),

-- 日志管理
('SYSTEM:LOG', '日志管理', 'menu', '/system/log', 1, 140, '日志管理菜单', 1),
('SYSTEM:LOG:AUDIT', '审计日志', 'api', '/api/log/audit/*', 19, 141, '查看审计日志', 1),
('SYSTEM:LOG:SYSTEM', '系统日志', 'api', '/api/log/system/*', 19, 142, '查看系统日志', 1),
('SYSTEM:LOG:LOGIN', '登录日志', 'api', '/api/log/login/*', 19, 143, '查看登录日志', 1),
('SYSTEM:LOG:CLEAN', '清理日志', 'api', '/api/log/clean', 19, 144, '清理过期日志', 1),

-- 业务模块
('BUSINESS', '业务管理', 'menu', '/business', 0, 200, '业务管理模块', 1),
('BUSINESS:VIEW', '业务查看', 'api', '/api/business/*', 23, 210, '查看业务数据', 1),
('BUSINESS:OPERATE', '业务操作', 'api', '/api/business/operate/*', 23, 220, '执行业务操作', 1);

-- 插入用户角色关联数据
INSERT INTO `t_user_role` (`user_id`, `role_id`, `create_by`) VALUES
(1, 1, 1),  -- admin 用户分配 ADMIN 角色
(2, 2, 1),  -- user1 用户分配 USER_MANAGER 角色
(3, 4, 1),  -- user2 用户分配 OPERATOR 角色
(4, 3, 1),  -- user3 用户分配 GUEST 角色
(5, 3, 1);  -- user4 用户分配 GUEST 角色

-- 插入角色权限关联数据
-- ADMIN 角色拥有所有权限
INSERT INTO `t_role_permission` (`role_id`, `permission_id`, `create_by`)
SELECT 1, id, 1 FROM `t_permission`;

-- USER_MANAGER 角色拥有用户管理相关权限
INSERT INTO `t_role_permission` (`role_id`, `permission_id`, `create_by`) VALUES
(2, 1, 1),   -- SYSTEM
(2, 2, 1),   -- SYSTEM:USER
(2, 3, 1),   -- SYSTEM:USER:LIST
(2, 4, 1),   -- SYSTEM:USER:CREATE
(2, 5, 1),   -- SYSTEM:USER:UPDATE
(2, 6, 1),   -- SYSTEM:USER:DELETE
(2, 7, 1);   -- SYSTEM:USER:STATUS

-- GUEST 角色只有查看权限
INSERT INTO `t_role_permission` (`role_id`, `permission_id`, `create_by`) VALUES
(3, 1, 1),   -- SYSTEM
(3, 2, 1),   -- SYSTEM:USER
(3, 3, 1),   -- SYSTEM:USER:LIST
(3, 23, 1),  -- BUSINESS
(3, 24, 1);  -- BUSINESS:VIEW

-- OPERATOR 角色有业务操作权限
INSERT INTO `t_role_permission` (`role_id`, `permission_id`, `create_by`) VALUES
(4, 23, 1),  -- BUSINESS
(4, 24, 1),  -- BUSINESS:VIEW
(4, 25, 1);  -- BUSINESS:OPERATE