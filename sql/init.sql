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

-- 业务模块
('BUSINESS', '业务管理', 'menu', '/business', 0, 200, '业务管理模块', 1),
('BUSINESS:VIEW', '业务查看', 'api', '/api/business/*', 19, 210, '查看业务数据', 1),
('BUSINESS:OPERATE', '业务操作', 'api', '/api/business/operate/*', 19, 220, '执行业务操作', 1);

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
(3, 19, 1),  -- BUSINESS
(3, 20, 1);  -- BUSINESS:VIEW

-- OPERATOR 角色有业务操作权限
INSERT INTO `t_role_permission` (`role_id`, `permission_id`, `create_by`) VALUES
(4, 19, 1),  -- BUSINESS
(4, 20, 1),  -- BUSINESS:VIEW
(4, 21, 1);  -- BUSINESS:OPERATE