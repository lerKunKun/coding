# SQL 目录说明

本目录包含 BIOU 项目的所有数据库脚本，按照 [SQL编写规范](../docs/SQL_STANDARDS.md) 进行组织。

## 目录结构

```
sql/
├── migrations/          # 数据库迁移脚本
│   ├── v1.0.0/         # v1.0.0 版本脚本
│   │   ├── 001_create_database_and_tables.sql  # 创建数据库和表结构
│   │   └── 002_insert_initial_data.sql         # 插入基础数据
│   └── v1.1.0/         # v1.1.0 版本脚本
│       └── 001_add_auth_fields.sql             # 添加认证相关字段
├── patches/            # 补丁脚本（按年月组织）
│   └── 2025/
│       ├── 05/ ... 12/  # 按月份组织的补丁脚本（从2025年5月开始）
├── rollback/           # 回滚脚本
│   └── rollback_v1.1.0_001_add_auth_fields.sql
├── procedures/         # 存储过程
├── functions/          # 函数
├── views/             # 视图
├── templates/          # 脚本模板
│   ├── migration_template.sql     # 迁移脚本模板
│   └── rollback_template.sql      # 回滚脚本模板
└── README.md          # 本文档
```

## 脚本执行顺序

### 初始化部署

1. **v1.0.0 - 系统初始化**
   ```bash
   # 1. 创建数据库和表结构
   mysql -h host -u user -p database < migrations/v1.0.0/001_create_database_and_tables.sql
   
   # 2. 插入基础数据
   mysql -h host -u user -p database < migrations/v1.0.0/002_insert_initial_data.sql
   ```

2. **v1.1.0 - 认证功能升级**
   ```bash
   # 添加认证相关字段
   mysql -h host -u user -p database < migrations/v1.1.0/001_add_auth_fields.sql
   ```

### 回滚操作

如果需要回滚v1.1.0版本的认证功能：
```bash
mysql -h host -u user -p database < rollback/rollback_v1.1.0_001_add_auth_fields.sql
```

## 版本说明

### v1.0.0 - 系统基础版本
- **数据库结构**: 创建用户、角色、权限、日志等核心表
- **基础数据**: 插入默认角色、权限和测试用户数据
- **功能范围**: 基础的用户权限管理系统

### v1.1.0 - 认证功能增强
- **新增字段**: 
  - `real_name` - 真实姓名
  - `dingtalk_user_id` - 钉钉用户ID
  - `dingtalk_union_id` - 钉钉用户唯一标识
  - `last_login_time` - 最后登录时间
  - `last_login_ip` - 最后登录IP
- **新增索引**: 钉钉相关字段索引
- **密码加密**: 升级为BCrypt加密格式
- **默认账户**: 
  - 管理员：admin/admin123
  - 测试用户：test/test123

## 使用注意事项

1. **执行前准备**
   - 备份现有数据库
   - 在测试环境先验证
   - 检查依赖关系

2. **生产环境部署**
   - 修改默认密码
   - 删除测试用户
   - 根据实际需求调整权限配置

3. **脚本特性**
   - 所有脚本支持重复执行
   - 包含存在性检查，避免重复操作
   - 提供详细的执行反馈

## 命名规范

- **版本目录**: `v主版本.次版本.修订版本`
- **脚本文件**: `序号_功能描述.sql`
- **回滚脚本**: `rollback_版本_序号_功能描述.sql`

## 相关文档

- [SQL编写规范](../docs/SQL_STANDARDS.md) - 详细的SQL脚本编写标准
- [数据库设计文档](../docs/DATABASE.md) - 数据库表结构设计说明

## 快速开始

### 创建新的迁移脚本

1. **使用模板创建脚本**：
   ```bash
   # 创建新版本目录
   mkdir -p sql/migrations/v1.2.0
   
   # 复制模板
   cp sql/templates/migration_template.sql sql/migrations/v1.2.0/001_add_user_profile.sql
   
   # 编辑脚本内容
   vim sql/migrations/v1.2.0/001_add_user_profile.sql
   ```

2. **创建对应的回滚脚本**：
   ```bash
   # 复制回滚模板
   cp sql/templates/rollback_template.sql sql/rollback/rollback_v1.2.0_001_add_user_profile.sql
   
   # 编辑回滚脚本
   vim sql/rollback/rollback_v1.2.0_001_add_user_profile.sql
   ```

3. **本地测试**：
   ```bash
   # 创建测试数据库
   mysql -u root -p -e "CREATE DATABASE test_biou_db;"
   
   # 执行基础脚本
   mysql -u root -p test_biou_db < sql/migrations/v1.0.0/001_create_database_and_tables.sql
   mysql -u root -p test_biou_db < sql/migrations/v1.0.0/002_insert_initial_data.sql
   mysql -u root -p test_biou_db < sql/migrations/v1.1.0/001_add_auth_fields.sql
   
   # 测试新脚本
   mysql -u root -p test_biou_db < sql/migrations/v1.2.0/001_add_user_profile.sql
   
   # 测试回滚
   mysql -u root -p test_biou_db < sql/rollback/rollback_v1.2.0_001_add_user_profile.sql
   ```

### 脚本编写检查清单

在提交脚本前，请确认以下项目：

- [ ] 包含完整的头部信息
- [ ] 有环境检查语句
- [ ] 包含存在性验证（防重复执行）
- [ ] 有详细的注释说明
- [ ] 包含结果验证语句
- [ ] 创建了对应的回滚脚本
- [ ] 在本地环境测试通过
- [ ] 在测试环境验证通过

## 维护记录

| 版本 | 日期 | 维护人员 | 变更说明 |
|------|------|----------|----------|
| v1.0.0 | 2024-12-01 | 系统初始化 | 创建基础表结构和数据 |
| v1.1.0 | 2024-12-01 | 认证功能开发组 | 添加认证相关功能 | 