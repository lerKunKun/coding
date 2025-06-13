# 2025年10月补丁脚本

本目录用于存放2025年10月的数据库补丁脚本。

## 命名规范

文件命名格式：`{序号}_{功能描述}_{类型}.sql`

示例：
- `001_fix_user_index_performance.sql` - 修复用户索引性能问题
- `002_add_missing_auth_fields.sql` - 添加缺失的认证字段
- `003_update_default_permissions.sql` - 更新默认权限配置

## 脚本要求

1. **必须包含完整的头部信息**
2. **必须有对应的回滚脚本**
3. **必须在测试环境验证**
4. **支持重复执行**

## 使用说明

```bash
# 执行补丁脚本
mysql -h host -u user -p database < 001_fix_user_index_performance.sql

# 如需回滚
mysql -h host -u user -p database < ../../rollback/rollback_2025_05_001_fix_user_index_performance.sql
``` 