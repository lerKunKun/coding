-- 认证功能数据库升级脚本
-- 为用户表添加认证相关字段

-- 添加真实姓名字段
ALTER TABLE t_user ADD COLUMN real_name VARCHAR(100) COMMENT '真实姓名' AFTER phone;

-- 添加钉钉用户ID字段
ALTER TABLE t_user ADD COLUMN dingtalk_user_id VARCHAR(100) COMMENT '钉钉用户ID' AFTER real_name;

-- 添加钉钉用户唯一标识字段
ALTER TABLE t_user ADD COLUMN dingtalk_union_id VARCHAR(100) COMMENT '钉钉用户唯一标识' AFTER dingtalk_user_id;

-- 添加最后登录时间字段
ALTER TABLE t_user ADD COLUMN last_login_time DATETIME COMMENT '最后登录时间' AFTER dingtalk_union_id;

-- 添加最后登录IP字段
ALTER TABLE t_user ADD COLUMN last_login_ip VARCHAR(50) COMMENT '最后登录IP' AFTER last_login_time;

-- 为钉钉字段添加索引
ALTER TABLE t_user ADD INDEX idx_dingtalk_union_id (dingtalk_union_id);
ALTER TABLE t_user ADD INDEX idx_dingtalk_user_id (dingtalk_user_id);

-- 更新现有用户密码为加密格式（注意：这会把现有明文密码加密）
-- 如果需要保留测试用户，请在运行此脚本前备份数据
-- UPDATE t_user SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM4JLoMReo8yM1yfyayK' WHERE password = '123456';

-- 创建默认管理员用户（密码：admin123）
INSERT INTO t_user (username, password, email, phone, real_name, status, create_time, update_time, deleted) 
VALUES (
    'admin', 
    '$2a$10$8.sXNJNTSE9lU/1m/IkZ9u5q2.xJGe3qx4TJt3Jl5oUCJj3fZgJGy', 
    'admin@biou.com', 
    '13800138000', 
    '系统管理员',
    1, 
    NOW(), 
    NOW(), 
    0
) ON DUPLICATE KEY UPDATE password = '$2a$10$8.sXNJNTSE9lU/1m/IkZ9u5q2.xJGe3qx4TJt3Jl5oUCJj3fZgJGy';

-- 创建测试用户（密码：test123）
INSERT INTO t_user (username, password, email, phone, real_name, status, create_time, update_time, deleted) 
VALUES (
    'test', 
    '$2a$10$7qxQC3vXeO.HMqF4dFM8mO2J8Hy4L1F2E9kF5J3X8vY6Q2pW1nE7a', 
    'test@biou.com', 
    '13800138001', 
    '测试用户',
    1, 
    NOW(), 
    NOW(), 
    0
) ON DUPLICATE KEY UPDATE password = '$2a$10$7qxQC3vXeO.HMqF4dFM8mO2J8Hy4L1F2E9kF5J3X8vY6Q2pW1nE7a';