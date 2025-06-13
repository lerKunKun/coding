/*
================================================================================
回滚脚本: rollback_[版本号]_[序号]_[功能描述].sql
创建时间: [YYYY-MM-DD]
创建人员: [开发者姓名]
回滚版本: [要回滚的脚本版本，如：v1.2.0/001_add_user_profile_table.sql]
回滚原因: [回滚原因说明]
影响范围: [回滚将影响的表、字段、索引等]
执行环境: [开发/测试/生产]
预计耗时: [预估执行时间]
注意事项: [回滚的特殊注意事项，如：会丢失数据]
================================================================================
*/

-- 检查数据库版本和环境
SELECT VERSION() as mysql_version;
SELECT DATABASE() as current_database;

-- 检查回滚前提条件
-- SELECT COUNT(*) as check_condition FROM `target_table` WHERE condition;

-- ==============================================
-- 开始回滚操作
-- ==============================================

-- 示例1：删除表
/*
-- 备份要删除的表（可选）
CREATE TABLE `deleted_table_backup_$(date +%Y%m%d)` AS SELECT * FROM `target_table`;

-- 删除表
DROP TABLE IF EXISTS `target_table`;
*/

-- 示例2：删除字段
/*
-- 备份字段数据（可选）
CREATE TABLE `field_backup_$(date +%Y%m%d)` AS 
SELECT `id`, `field_to_delete` FROM `target_table` WHERE `field_to_delete` IS NOT NULL;

-- 删除字段
ALTER TABLE `target_table` DROP COLUMN IF EXISTS `field_to_delete`;
*/

-- 示例3：删除索引
/*
DROP INDEX IF EXISTS `idx_field_name` ON `target_table`;
*/

-- 示例4：恢复数据
/*
-- 从备份表恢复数据
INSERT INTO `target_table` 
SELECT * FROM `target_table_backup_YYYYMMDD` 
WHERE `id` NOT IN (SELECT `id` FROM `target_table`);

-- 或者恢复特定字段的值
UPDATE `target_table` t1
INNER JOIN `field_backup_YYYYMMDD` t2 ON t1.id = t2.id
SET t1.field_name = t2.original_value;
*/

-- 示例5：回滚数据更新
/*
-- 恢复更新前的数据状态
UPDATE `target_table` 
SET `field1` = 'original_value', `updated_at` = NOW() 
WHERE `field1` = 'new_value' 
AND `updated_at` >= '2024-01-01 00:00:00'; -- 基于时间范围回滚
*/

-- ==============================================
-- 验证回滚结果
-- ==============================================

-- 验证表是否删除
-- SELECT COUNT(*) as table_exists FROM INFORMATION_SCHEMA.TABLES 
-- WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'target_table';

-- 验证字段是否删除
-- SELECT COUNT(*) as field_exists FROM INFORMATION_SCHEMA.COLUMNS 
-- WHERE TABLE_SCHEMA = DATABASE() 
-- AND TABLE_NAME = 'target_table' 
-- AND COLUMN_NAME = 'deleted_field';

-- 验证索引是否删除
-- SELECT COUNT(*) as index_exists FROM INFORMATION_SCHEMA.STATISTICS 
-- WHERE TABLE_SCHEMA = DATABASE() 
-- AND TABLE_NAME = 'target_table' 
-- AND INDEX_NAME = 'deleted_index';

-- 验证数据恢复
-- SELECT COUNT(*) as restored_count FROM `target_table` WHERE condition;

-- 删除备份表（确认回滚成功后）
-- DROP TABLE IF EXISTS `target_table_backup_YYYYMMDD`;
-- DROP TABLE IF EXISTS `field_backup_YYYYMMDD`;

/*
================================================================================
回滚执行完成
请验证以下项目：
1. 检查应用程序是否正常运行
2. 验证业务功能是否恢复正常
3. 确认数据完整性
4. 检查相关日志是否有异常
5. 通知相关团队回滚已完成
================================================================================
*/ 