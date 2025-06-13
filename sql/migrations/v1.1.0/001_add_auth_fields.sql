/*
================================================================================
脚本名称: 001_add_auth_fields.sql
创建时间: 2024-12-01
创建人员: 认证功能开发组
变更描述: 为用户表添加认证相关字段，支持钉钉登录和密码加密功能
关联需求: AUTH-001 钉钉认证集成
影响范围: t_user表结构
执行环境: 开发/测试/生产
预计耗时: 2分钟
注意事项: 执行前请备份用户表数据，会更新现有用户密码格式
================================================================================
*/

-- 检查数据库环境
USE `biou_db`;
SELECT DATABASE() as current_database;

-- 检查t_user表是否存在
SELECT COUNT(*) as table_exists FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 't_user';

-- 添加真实姓名字段
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 't_user' 
    AND COLUMN_NAME = 'real_name'
);

SET @sql = CASE 
    WHEN @column_exists = 0 THEN 
        'ALTER TABLE `t_user` ADD COLUMN `real_name` VARCHAR(100) COMMENT ''真实姓名'' AFTER `phone`'
    ELSE 
        'SELECT ''Column real_name already exists'' as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加钉钉用户ID字段
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 't_user' 
    AND COLUMN_NAME = 'dingtalk_user_id'
);

SET @sql = CASE 
    WHEN @column_exists = 0 THEN 
        'ALTER TABLE `t_user` ADD COLUMN `dingtalk_user_id` VARCHAR(100) COMMENT ''钉钉用户ID'' AFTER `real_name`'
    ELSE 
        'SELECT ''Column dingtalk_user_id already exists'' as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加钉钉用户唯一标识字段
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 't_user' 
    AND COLUMN_NAME = 'dingtalk_union_id'
);

SET @sql = CASE 
    WHEN @column_exists = 0 THEN 
        'ALTER TABLE `t_user` ADD COLUMN `dingtalk_union_id` VARCHAR(100) COMMENT ''钉钉用户唯一标识'' AFTER `dingtalk_user_id`'
    ELSE 
        'SELECT ''Column dingtalk_union_id already exists'' as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加最后登录时间字段
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 't_user' 
    AND COLUMN_NAME = 'last_login_time'
);

SET @sql = CASE 
    WHEN @column_exists = 0 THEN 
        'ALTER TABLE `t_user` ADD COLUMN `last_login_time` DATETIME COMMENT ''最后登录时间'' AFTER `dingtalk_union_id`'
    ELSE 
        'SELECT ''Column last_login_time already exists'' as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加最后登录IP字段
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 't_user' 
    AND COLUMN_NAME = 'last_login_ip'
);

SET @sql = CASE 
    WHEN @column_exists = 0 THEN 
        'ALTER TABLE `t_user` ADD COLUMN `last_login_ip` VARCHAR(50) COMMENT ''最后登录IP'' AFTER `last_login_time`'
    ELSE 
        'SELECT ''Column last_login_ip already exists'' as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 为钉钉字段添加索引
SET @index_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.STATISTICS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 't_user' 
    AND INDEX_NAME = 'idx_dingtalk_union_id'
);

SET @sql = CASE 
    WHEN @index_exists = 0 THEN 
        'CREATE INDEX `idx_dingtalk_union_id` ON `t_user` (`dingtalk_union_id`)'
    ELSE 
        'SELECT ''Index idx_dingtalk_union_id already exists'' as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @index_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.STATISTICS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 't_user' 
    AND INDEX_NAME = 'idx_dingtalk_user_id'
);

SET @sql = CASE 
    WHEN @index_exists = 0 THEN 
        'CREATE INDEX `idx_dingtalk_user_id` ON `t_user` (`dingtalk_user_id`)'
    ELSE 
        'SELECT ''Index idx_dingtalk_user_id already exists'' as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 备份现有密码（用于回滚）
CREATE TABLE IF NOT EXISTS `t_user_password_backup_v110` AS 
SELECT `id`, `username`, `password`, NOW() as backup_time 
FROM `t_user` 
WHERE `password` = '123456';

-- 更新现有用户密码为加密格式（仅针对默认密码）
-- 注意：生产环境执行前请确认是否需要此操作
UPDATE `t_user` 
SET `password` = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM4JLoMReo8yM1yfyayK' 
WHERE `password` = '123456' 
AND `username` != 'admin';

-- 创建/更新管理员用户（密码：admin123）
INSERT INTO `t_user` (`username`, `password`, `email`, `phone`, `real_name`, `status`, `create_time`, `update_time`, `deleted`) 
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
) ON DUPLICATE KEY UPDATE 
    `password` = '$2a$10$8.sXNJNTSE9lU/1m/IkZ9u5q2.xJGe3qx4TJt3Jl5oUCJj3fZgJGy',
    `real_name` = '系统管理员',
    `update_time` = NOW();

-- 创建/更新测试用户（密码：test123）
INSERT INTO `t_user` (`username`, `password`, `email`, `phone`, `real_name`, `status`, `create_time`, `update_time`, `deleted`) 
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
) ON DUPLICATE KEY UPDATE 
    `password` = '$2a$10$7qxQC3vXeO.HMqF4dFM8mO2J8Hy4L1F2E9kF5J3X8vY6Q2pW1nE7a',
    `real_name` = '测试用户',
    `update_time` = NOW();

-- 验证字段添加结果
DESC `t_user`;

-- 验证索引创建结果
SHOW INDEX FROM `t_user` WHERE Key_name IN ('idx_dingtalk_union_id', 'idx_dingtalk_user_id');

-- 验证数据更新结果
SELECT 
    COUNT(*) as total_users,
    COUNT(CASE WHEN `real_name` IS NOT NULL THEN 1 END) as users_with_real_name,
    COUNT(CASE WHEN `password` LIKE '$2a$%' THEN 1 END) as users_with_encrypted_password
FROM `t_user`;

/*
================================================================================
认证功能升级完成
字段添加：real_name, dingtalk_user_id, dingtalk_union_id, last_login_time, last_login_ip
索引添加：idx_dingtalk_union_id, idx_dingtalk_user_id
密码加密：默认密码已更新为BCrypt格式
管理员账户：admin/admin123
测试账户：test/test123
================================================================================
*/ 