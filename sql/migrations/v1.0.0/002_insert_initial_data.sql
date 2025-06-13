/*
================================================================================
脚本名称: 002_insert_initial_data.sql
创建时间: 2024-12-01
创建人员: 系统初始化
变更描述: 插入系统运行所需的基础数据，包括角色、权限、用户等
关联需求: 系统初始化
影响范围: t_user, t_role, t_permission, t_user_role, t_role_permission 表
执行环境: 开发/测试/生产
预计耗时: 1分钟
注意事项: 在001_create_database_and_tables.sql执行成功后执行
================================================================================
*/

-- 检查数据库环境
USE `biou_db`;

-- 检查表是否存在
SELECT COUNT(*) as table_count FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = 'biou_db' AND TABLE_NAME IN ('t_user', 't_role', 't_permission');

-- 插入角色数据
INSERT IGNORE INTO `t_role` (`role_code`, `role_name`, `description`, `status`) VALUES
('ADMIN', '系统管理员', '拥有系统全部权限', 1),
('USER_MANAGER', '用户管理员', '负责用户管理相关功能', 1),
('GUEST', '访客', '只有基本的查看权限', 1),
('OPERATOR', '操作员', '负责日常业务操作', 1);

-- 插入权限数据
INSERT IGNORE INTO `t_permission` (`permission_code`, `permission_name`, `resource_type`, `resource_path`, `parent_id`, `sort_order`, `description`, `status`) VALUES
-- 系统管理
('SYSTEM', '系统管理', 'menu', '/system', 0, 100, '系统管理模块', 1),
('SYSTEM:USER', '用户管理', 'menu', '/system/user', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM') as temp), 110, '用户管理菜单', 1),
('SYSTEM:USER:LIST', '用户列表', 'api', '/api/user/page', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:USER') as temp), 111, '查看用户列表', 1),
('SYSTEM:USER:CREATE', '创建用户', 'api', '/api/user', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:USER') as temp), 112, '创建新用户', 1),
('SYSTEM:USER:UPDATE', '更新用户', 'api', '/api/user/*', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:USER') as temp), 113, '更新用户信息', 1),
('SYSTEM:USER:DELETE', '删除用户', 'api', '/api/user/*', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:USER') as temp), 114, '删除用户', 1),
('SYSTEM:USER:STATUS', '用户状态', 'api', '/api/user/*/status', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:USER') as temp), 115, '修改用户状态', 1),

-- 角色管理
('SYSTEM:ROLE', '角色管理', 'menu', '/system/role', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM') as temp), 120, '角色管理菜单', 1),
('SYSTEM:ROLE:LIST', '角色列表', 'api', '/api/role/page', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:ROLE') as temp), 121, '查看角色列表', 1),
('SYSTEM:ROLE:CREATE', '创建角色', 'api', '/api/role', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:ROLE') as temp), 122, '创建新角色', 1),
('SYSTEM:ROLE:UPDATE', '更新角色', 'api', '/api/role/*', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:ROLE') as temp), 123, '更新角色信息', 1),
('SYSTEM:ROLE:DELETE', '删除角色', 'api', '/api/role/*', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:ROLE') as temp), 124, '删除角色', 1),
('SYSTEM:ROLE:ASSIGN', '分配权限', 'api', '/api/role/*/permissions', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:ROLE') as temp), 125, '为角色分配权限', 1),

-- 权限管理
('SYSTEM:PERMISSION', '权限管理', 'menu', '/system/permission', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM') as temp), 130, '权限管理菜单', 1),
('SYSTEM:PERMISSION:LIST', '权限列表', 'api', '/api/permission/list', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:PERMISSION') as temp), 131, '查看权限列表', 1),
('SYSTEM:PERMISSION:CREATE', '创建权限', 'api', '/api/permission', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:PERMISSION') as temp), 132, '创建新权限', 1),
('SYSTEM:PERMISSION:UPDATE', '更新权限', 'api', '/api/permission/*', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:PERMISSION') as temp), 133, '更新权限信息', 1),
('SYSTEM:PERMISSION:DELETE', '删除权限', 'api', '/api/permission/*', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:PERMISSION') as temp), 134, '删除权限', 1),

-- 日志管理
('SYSTEM:LOG', '日志管理', 'menu', '/system/log', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM') as temp), 140, '日志管理菜单', 1),
('SYSTEM:LOG:AUDIT', '审计日志', 'api', '/api/log/audit/*', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:LOG') as temp), 141, '查看审计日志', 1),
('SYSTEM:LOG:SYSTEM', '系统日志', 'api', '/api/log/system/*', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:LOG') as temp), 142, '查看系统日志', 1),
('SYSTEM:LOG:LOGIN', '登录日志', 'api', '/api/log/login/*', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:LOG') as temp), 143, '查看登录日志', 1),
('SYSTEM:LOG:CLEAN', '清理日志', 'api', '/api/log/clean', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'SYSTEM:LOG') as temp), 144, '清理过期日志', 1),

-- 业务模块
('BUSINESS', '业务管理', 'menu', '/business', 0, 200, '业务管理模块', 1),
('BUSINESS:VIEW', '业务查看', 'api', '/api/business/*', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'BUSINESS') as temp), 210, '查看业务数据', 1),
('BUSINESS:OPERATE', '业务操作', 'api', '/api/business/operate/*', (SELECT id FROM (SELECT id FROM t_permission WHERE permission_code = 'BUSINESS') as temp), 220, '执行业务操作', 1);

-- 插入测试用户数据（开发/测试环境使用）
INSERT IGNORE INTO `t_user` (`username`, `password`, `email`, `phone`, `status`) VALUES
('admin', '123456', 'admin@biou.com', '13800138000', 1),
('user1', '123456', 'user1@biou.com', '13800138001', 1),
('user2', '123456', 'user2@biou.com', '13800138002', 1),
('user3', '123456', 'user3@biou.com', '13800138003', 0),
('user4', '123456', 'user4@biou.com', '13800138004', 1);

-- 插入用户角色关联数据
INSERT IGNORE INTO `t_user_role` (`user_id`, `role_id`, `create_by`) 
SELECT u.id, r.id, 1 
FROM `t_user` u, `t_role` r 
WHERE (u.username = 'admin' AND r.role_code = 'ADMIN')
   OR (u.username = 'user1' AND r.role_code = 'USER_MANAGER')
   OR (u.username = 'user2' AND r.role_code = 'OPERATOR')
   OR (u.username = 'user3' AND r.role_code = 'GUEST')
   OR (u.username = 'user4' AND r.role_code = 'GUEST');

-- 插入角色权限关联数据
-- ADMIN 角色拥有所有权限
INSERT IGNORE INTO `t_role_permission` (`role_id`, `permission_id`, `create_by`)
SELECT r.id, p.id, 1 
FROM `t_role` r, `t_permission` p 
WHERE r.role_code = 'ADMIN';

-- USER_MANAGER 角色拥有用户管理相关权限
INSERT IGNORE INTO `t_role_permission` (`role_id`, `permission_id`, `create_by`)
SELECT r.id, p.id, 1 
FROM `t_role` r, `t_permission` p 
WHERE r.role_code = 'USER_MANAGER' 
AND p.permission_code IN ('SYSTEM', 'SYSTEM:USER', 'SYSTEM:USER:LIST', 'SYSTEM:USER:CREATE', 'SYSTEM:USER:UPDATE', 'SYSTEM:USER:DELETE', 'SYSTEM:USER:STATUS');

-- GUEST 角色只有查看权限
INSERT IGNORE INTO `t_role_permission` (`role_id`, `permission_id`, `create_by`)
SELECT r.id, p.id, 1 
FROM `t_role` r, `t_permission` p 
WHERE r.role_code = 'GUEST' 
AND p.permission_code IN ('SYSTEM', 'SYSTEM:USER', 'SYSTEM:USER:LIST', 'BUSINESS', 'BUSINESS:VIEW');

-- OPERATOR 角色有业务操作权限
INSERT IGNORE INTO `t_role_permission` (`role_id`, `permission_id`, `create_by`)
SELECT r.id, p.id, 1 
FROM `t_role` r, `t_permission` p 
WHERE r.role_code = 'OPERATOR' 
AND p.permission_code IN ('BUSINESS', 'BUSINESS:VIEW', 'BUSINESS:OPERATE');

-- 验证数据插入结果
SELECT '用户数据' as data_type, COUNT(*) as count FROM `t_user`
UNION ALL
SELECT '角色数据' as data_type, COUNT(*) as count FROM `t_role`
UNION ALL
SELECT '权限数据' as data_type, COUNT(*) as count FROM `t_permission`
UNION ALL
SELECT '用户角色关联' as data_type, COUNT(*) as count FROM `t_user_role`
UNION ALL
SELECT '角色权限关联' as data_type, COUNT(*) as count FROM `t_role_permission`;

/*
================================================================================
基础数据插入完成
注意：生产环境部署时请修改默认用户密码并删除测试用户
下一步：根据业务需求创建具体的业务表结构
================================================================================
*/ 