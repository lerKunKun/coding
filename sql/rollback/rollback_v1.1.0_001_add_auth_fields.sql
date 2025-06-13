/*
================================================================================
回滚脚本: rollback_v1.1.0_001_add_auth_fields.sql
创建时间: 2024-12-01
创建人员: 认证功能开发组
回滚版本: v1.1.0/001_add_auth_fields.sql
回滚原因: 回滚认证功能升级，移除添加的字段和索引
影响范围: t_user表结构
执行环境: 开发/测试/生产
预计耗时: 1分钟
注意事项: 会删除认证相关字段，请确认数据可以丢失
================================================================================
*/

-- 检查数据库环境
USE `biou_db`;
SELECT DATABASE() as current_database;

-- 从备份恢复密码（如果备份表存在）
UPDATE `t_user` u
INNER JOIN `t_user_password_backup_v110` b ON u.id = b.id
SET u.password = b.password
WHERE EXISTS (
    SELECT 1 FROM INFORMATION_SCHEMA.TABLES 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 't_user_password_backup_v110'
);

-- 删除钉钉相关索引
DROP INDEX IF EXISTS `idx_dingtalk_union_id` ON `t_user`;
DROP INDEX IF EXISTS `idx_dingtalk_user_id` ON `t_user`;

-- 删除添加的字段
ALTER TABLE `t_user` DROP COLUMN IF EXISTS `last_login_ip`;
ALTER TABLE `t_user` DROP COLUMN IF EXISTS `last_login_time`;
ALTER TABLE `t_user` DROP COLUMN IF EXISTS `dingtalk_union_id`;
ALTER TABLE `t_user` DROP COLUMN IF EXISTS `dingtalk_user_id`;
ALTER TABLE `t_user` DROP COLUMN IF EXISTS `real_name`;

-- 删除备份表
DROP TABLE IF EXISTS `t_user_password_backup_v110`;

-- 删除测试用户（如果存在）
DELETE FROM `t_user` WHERE `username` = 'test';

-- 验证回滚结果
DESC `t_user`;

-- 检查被删除的字段是否还存在
SELECT 
    COLUMN_NAME 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
AND TABLE_NAME = 't_user' 
AND COLUMN_NAME IN ('real_name', 'dingtalk_user_id', 'dingtalk_union_id', 'last_login_time', 'last_login_ip');

/*
================================================================================
回滚完成
已删除字段：real_name, dingtalk_user_id, dingtalk_union_id, last_login_time, last_login_ip
已删除索引：idx_dingtalk_union_id, idx_dingtalk_user_id
已恢复密码：从备份表恢复原始密码格式
已删除用户：test测试用户
================================================================================
*/ 