/*
================================================================================
脚本名称: [脚本文件名，如：001_add_user_profile_table.sql]
创建时间: [YYYY-MM-DD]
创建人员: [开发者姓名]
变更描述: [详细描述本次变更的内容和目的]
关联需求: [需求单号或JIRA编号，如：PROJ-1234]
影响范围: [影响的表、字段、索引等，如：新增 user_profile 表]
执行环境: [开发/测试/生产]
预计耗时: [预估执行时间，如：2分钟]
注意事项: [特殊注意事项，如：需要先执行 xxx.sql]
================================================================================
*/

-- 检查数据库版本和环境
SELECT VERSION() as mysql_version;
SELECT DATABASE() as current_database;

-- 检查前置条件（如表是否存在等）
-- SELECT COUNT(*) as prerequisite_check FROM INFORMATION_SCHEMA.TABLES 
-- WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'prerequisite_table';

-- ==============================================
-- 开始数据库变更操作
-- ==============================================

-- 示例1：创建表
/*
CREATE TABLE IF NOT EXISTS `new_table` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '名称',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新表说明';
*/

-- 示例2：添加字段
/*
-- 检查字段是否存在
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'target_table' 
    AND COLUMN_NAME = 'new_field'
);

-- 条件添加字段
SET @sql = CASE 
    WHEN @column_exists = 0 THEN 
        'ALTER TABLE `target_table` ADD COLUMN `new_field` VARCHAR(100) NOT NULL DEFAULT '''' COMMENT ''新字段描述'' AFTER `existing_field`'
    ELSE 
        'SELECT ''Column new_field already exists'' as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
*/

-- 示例3：添加索引
/*
-- 检查索引是否存在
SET @index_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.STATISTICS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'target_table' 
    AND INDEX_NAME = 'idx_new_field'
);

-- 条件添加索引
SET @sql = CASE 
    WHEN @index_exists = 0 THEN 
        'CREATE INDEX `idx_new_field` ON `target_table` (`new_field`)'
    ELSE 
        'SELECT ''Index idx_new_field already exists'' as message'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
*/

-- 示例4：插入数据
/*
-- 检查数据是否已存在
SELECT COUNT(*) as data_exists FROM `target_table` WHERE `key_field` = 'target_value';

-- 条件插入数据
INSERT IGNORE INTO `target_table` (`field1`, `field2`, `created_at`) 
VALUES 
('value1', 'value2', NOW()),
('value3', 'value4', NOW());

-- 验证插入结果
SELECT COUNT(*) as inserted_count FROM `target_table` WHERE `field1` IN ('value1', 'value3');
*/

-- 示例5：更新数据（大批量）
/*
-- 分批更新（适用于大数据量）
SET @batch_size = 1000;
SET @total_updated = 0;

update_loop: LOOP
    SET @affected_rows = 0;
    
    UPDATE `target_table` 
    SET `field1` = 'new_value', `updated_at` = NOW() 
    WHERE `condition_field` = 'condition_value' 
    AND `field1` != 'new_value'
    LIMIT @batch_size;
    
    SET @affected_rows = ROW_COUNT();
    SET @total_updated = @total_updated + @affected_rows;
    
    IF @affected_rows = 0 THEN
        LEAVE update_loop;
    END IF;
    
    -- 避免长时间锁表
    SELECT SLEEP(0.1);
    
END LOOP;

-- 显示更新结果
SELECT @total_updated as total_updated_records;
*/

-- ==============================================
-- 验证变更结果
-- ==============================================

-- 验证表结构
-- DESC `new_table`;

-- 验证索引
-- SHOW INDEX FROM `new_table`;

-- 验证数据
-- SELECT COUNT(*) as record_count FROM `new_table`;

-- 验证约束
-- SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
-- WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'new_table';

/*
================================================================================
变更执行完成
请在生产环境执行前：
1. 在测试环境验证脚本
2. 准备对应的回滚脚本
3. 备份相关数据
4. 通知相关团队成员
================================================================================
*/ 