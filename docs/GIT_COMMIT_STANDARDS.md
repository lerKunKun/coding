# Git提交规范

本文档规定了Biou项目的Git使用规范，包括提交信息格式、分支管理策略、代码审查流程等，确保项目版本控制的规范性和可维护性。

## 📋 目录

- [1. 提交信息规范](#1-提交信息规范)
- [2. 分支管理策略](#2-分支管理策略)
- [3. 代码审查流程](#3-代码审查流程)
- [4. 版本发布规范](#4-版本发布规范)
- [5. 常用Git命令](#5-常用git命令)
- [6. 提交前检查清单](#6-提交前检查清单)

## 1. 提交信息规范

### 1.1 提交信息格式

采用 **Conventional Commits** 规范，格式如下：

```
<type>(<scope>): <subject>

<body>

<footer>
```

#### 1.1.1 Type（类型）

| 类型 | 描述 | 示例 |
|------|------|------|
| **feat** | 新功能 | `feat(user): 添加用户注册功能` |
| **fix** | 修复bug | `fix(auth): 修复登录验证失败问题` |
| **docs** | 文档变更 | `docs(readme): 更新安装说明` |
| **style** | 代码格式修改 | `style(user): 统一代码缩进格式` |
| **refactor** | 代码重构 | `refactor(service): 重构用户服务层` |
| **perf** | 性能优化 | `perf(query): 优化数据库查询性能` |
| **test** | 测试相关 | `test(user): 添加用户服务单元测试` |
| **build** | 构建系统修改 | `build(maven): 升级Spring Boot版本` |
| **ci** | CI配置修改 | `ci(github): 添加自动化测试流程` |
| **chore** | 其他修改 | `chore(deps): 更新依赖版本` |
| **revert** | 回滚提交 | `revert: 回滚feat(user): 添加用户注册功能` |

#### 1.1.2 Scope（范围）

表示本次提交影响的范围，可选，常用范围：

| 范围 | 描述 | 示例 |
|------|------|------|
| **user** | 用户模块 | `feat(user): 添加用户头像上传` |
| **role** | 角色模块 | `fix(role): 修复角色权限分配问题` |
| **permission** | 权限模块 | `refactor(permission): 重构权限检查逻辑` |
| **log** | 日志模块 | `feat(log): 新增审计日志导出功能` |
| **auth** | 认证授权 | `fix(auth): 修复JWT过期处理` |
| **config** | 配置相关 | `chore(config): 调整数据库连接池配置` |
| **docs** | 文档相关 | `docs(api): 更新API接口文档` |
| **test** | 测试相关 | `test(integration): 添加集成测试用例` |
| **db** | 数据库相关 | `feat(db): 添加新的数据库表结构` |

#### 1.1.3 Subject（主题）

- 使用中文描述
- 不超过50个字符
- 使用动宾结构，如"添加XX功能"、"修复XX问题"
- 首字母不大写，结尾不加句号
- 使用现在时态

#### 1.1.4 Body（正文）

可选，用于详细描述提交内容：

```
feat(user): 添加用户头像上传功能

- 支持JPG、PNG、GIF格式
- 限制文件大小为2MB以内
- 自动压缩和裁剪为标准尺寸
- 添加头像上传进度显示
- 支持头像预览和重新选择

相关Issue: #123
```

#### 1.1.5 Footer（页脚）

用于关联Issue或说明Breaking Change：

```
# 关联Issue
Closes #123
Fixes #456

# Breaking Change
BREAKING CHANGE: 用户API接口响应格式发生变更，需要前端同步更新
```

### 1.2 提交信息示例

#### 1.2.1 新功能提交

```
feat(user): 添加用户密码重置功能

- 支持邮箱验证码重置密码
- 添加密码强度验证
- 集成短信验证服务
- 添加重置记录日志

Closes #234
```

#### 1.2.2 Bug修复提交

```
fix(auth): 修复JWT令牌刷新失败问题

修复在高并发情况下，JWT令牌刷新时可能出现的
竞态条件问题，导致用户需要重新登录的bug。

- 添加分布式锁机制
- 优化令牌刷新逻辑
- 增加异常处理和重试机制

Fixes #456
```

#### 1.2.3 文档更新提交

```
docs(readme): 更新快速开始指南

- 添加Docker部署说明
- 补充环境变量配置说明
- 更新API接口示例
- 修正安装步骤中的错误
```

#### 1.2.4 重构提交

```
refactor(service): 重构用户服务层架构

- 抽取公共的验证逻辑
- 优化Repository层接口设计
- 统一异常处理机制
- 移除重复代码，提高可维护性

No breaking changes
```

## 2. 分支管理策略

### 2.1 分支类型

采用 **Git Flow** 工作流：

```
main
├── develop
│   ├── feature/user-profile
│   ├── feature/role-management
│   └── feature/audit-log
├── release/v1.1.0
├── hotfix/critical-security-fix
└── hotfix/login-issue
```

#### 2.1.1 主要分支

| 分支 | 用途 | 生命周期 | 保护级别 |
|------|------|----------|----------|
| **main** | 生产环境代码 | 永久 | 高度保护 |
| **develop** | 开发主分支 | 永久 | 保护 |

#### 2.1.2 辅助分支

| 分支类型 | 命名规范 | 用途 | 源分支 | 合并目标 |
|----------|----------|------|--------|----------|
| **feature** | `feature/功能描述` | 新功能开发 | develop | develop |
| **release** | `release/v版本号` | 版本发布准备 | develop | main + develop |
| **hotfix** | `hotfix/问题描述` | 紧急bug修复 | main | main + develop |

### 2.2 分支命名规范

#### 2.2.1 Feature分支

```bash
# ✅ 正确命名
feature/user-registration
feature/role-permission-management
feature/audit-log-export
feature/password-reset

# ❌ 错误命名
feature/user          # 太宽泛
feature/bugfix        # 应该使用hotfix
feature/User-Login    # 使用了大写字母
```

#### 2.2.2 Release分支

```bash
# ✅ 正确命名
release/v1.0.0
release/v1.1.0
release/v2.0.0-beta1

# ❌ 错误命名
release/version1      # 没有遵循语义化版本
release/release1      # 重复单词
```

#### 2.2.3 Hotfix分支

```bash
# ✅ 正确命名
hotfix/login-timeout-issue
hotfix/security-vulnerability
hotfix/data-corruption-fix

# ❌ 错误命名
hotfix/fix            # 太宽泛
hotfix/improvement    # 应该使用feature
```

### 2.3 分支操作流程

#### 2.3.1 Feature开发流程

```bash
# 1. 从develop创建feature分支
git checkout develop
git pull origin develop
git checkout -b feature/user-profile

# 2. 开发功能并提交
git add .
git commit -m "feat(user): 添加用户个人资料管理功能"

# 3. 推送到远程
git push origin feature/user-profile

# 4. 创建Pull Request到develop分支
# 5. 代码审查通过后合并
# 6. 删除feature分支
git branch -d feature/user-profile
git push origin --delete feature/user-profile
```

#### 2.3.2 Release发布流程

```bash
# 1. 从develop创建release分支
git checkout develop
git pull origin develop
git checkout -b release/v1.1.0

# 2. 更新版本号、修复bug、完善文档
git commit -m "chore(release): 准备v1.1.0版本发布"

# 3. 合并到main分支
git checkout main
git merge --no-ff release/v1.1.0
git tag -a v1.1.0 -m "Release version 1.1.0"

# 4. 合并回develop分支
git checkout develop
git merge --no-ff release/v1.1.0

# 5. 删除release分支
git branch -d release/v1.1.0
```

#### 2.3.3 Hotfix修复流程

```bash
# 1. 从main创建hotfix分支
git checkout main
git pull origin main
git checkout -b hotfix/critical-security-fix

# 2. 修复问题并提交
git commit -m "fix(security): 修复SQL注入安全漏洞"

# 3. 合并到main分支
git checkout main
git merge --no-ff hotfix/critical-security-fix
git tag -a v1.0.1 -m "Hotfix version 1.0.1"

# 4. 合并回develop分支
git checkout develop
git merge --no-ff hotfix/critical-security-fix

# 5. 删除hotfix分支
git branch -d hotfix/critical-security-fix
```

## 3. 代码审查流程

### 3.1 Pull Request规范

#### 3.1.1 PR标题格式

```
<type>(<scope>): <description>

# 示例
feat(user): 添加用户头像上传功能
fix(auth): 修复登录状态检查问题
docs(api): 更新用户接口文档
```

#### 3.1.2 PR描述模板

```markdown
## 📝 变更说明
简要描述本次PR的主要变更内容

## 🎯 变更类型
- [ ] 新功能 (feat)
- [ ] Bug修复 (fix)  
- [ ] 文档更新 (docs)
- [ ] 代码重构 (refactor)
- [ ] 性能优化 (perf)
- [ ] 测试相关 (test)
- [ ] 构建相关 (build)
- [ ] 其他 (chore)

## 🔧 具体变更
- 变更点1
- 变更点2
- 变更点3

## 🧪 测试说明
- [ ] 单元测试通过
- [ ] 集成测试通过
- [ ] 手动测试通过
- [ ] 性能测试通过(如适用)

## 📋 检查清单
- [ ] 代码符合编写规范
- [ ] 添加了必要的注释
- [ ] 更新了相关文档
- [ ] 通过了所有测试
- [ ] 没有引入新的安全问题

## 🔗 相关链接
- Issue: #123
- 相关PR: #456
- 文档链接: https://example.com

## 📷 截图/演示
(如果有UI变更，请提供截图或演示视频)
```

### 3.2 代码审查标准

#### 3.2.1 必检项目

- [ ] **功能正确性** - 代码是否实现了预期功能
- [ ] **代码规范** - 是否符合项目编码规范
- [ ] **架构设计** - 是否遵循四层架构原则
- [ ] **异常处理** - 是否有完善的异常处理机制
- [ ] **性能考虑** - 是否有明显的性能问题
- [ ] **安全性** - 是否存在安全漏洞
- [ ] **测试覆盖** - 是否有足够的测试用例
- [ ] **文档完整** - 是否更新了相关文档

#### 3.2.2 审查评论规范

```markdown
# ✅ 建议类评论
💡 建议: 这里可以考虑使用Builder模式来构建复杂对象，提高代码可读性。

# ⚠️ 问题类评论  
⚠️ 问题: 这里缺少空指针检查，可能导致NullPointerException。

# 🔧 必须修改
🔧 必须修改: 违反了项目编码规范，Repository层不应该包含业务逻辑。

# 👍 认可类评论
👍 很好: 异常处理很完善，代码逻辑清晰。

# ❓ 疑问类评论
❓ 疑问: 这个魔法数字1000代表什么含义？建议定义为常量。
```

### 3.3 审查响应规范

#### 3.3.1 修改响应

```markdown
## 修改说明

### ✅ 已修改
- 添加了空指针检查 (commit: abc1234)
- 将魔法数字提取为常量 (commit: def5678)
- 补充了单元测试 (commit: ghi9012)

### 💭 讨论说明  
关于Builder模式的建议，考虑到当前对象结构相对简单，暂时保持现有实现。
如果后续对象复杂度增加，会考虑重构为Builder模式。

### 📋 待确认
- 关于性能优化的建议，需要进一步讨论技术方案
```

## 4. 版本发布规范

### 4.1 语义化版本控制

采用 **Semantic Versioning (SemVer)** 规范：

```
主版本号.次版本号.修订号 (MAJOR.MINOR.PATCH)

示例: v1.2.3
```

#### 4.1.1 版本号递增规则

| 版本类型 | 递增条件 | 示例 |
|----------|----------|------|
| **MAJOR** | 不兼容的API变更 | 1.0.0 → 2.0.0 |
| **MINOR** | 向后兼容的功能性新增 | 1.0.0 → 1.1.0 |
| **PATCH** | 向后兼容的问题修正 | 1.0.0 → 1.0.1 |

#### 4.1.2 预发布版本

```
1.0.0-alpha     # 内测版本
1.0.0-beta      # 公测版本  
1.0.0-rc.1      # 发布候选版本
```

### 4.2 发布标签规范

#### 4.2.1 标签格式

```bash
# 正式版本
git tag -a v1.0.0 -m "Release version 1.0.0"

# 预发布版本
git tag -a v1.1.0-beta1 -m "Beta release 1.1.0-beta1"

# 热修复版本
git tag -a v1.0.1 -m "Hotfix version 1.0.1"
```

#### 4.2.2 发布说明模板

```markdown
# Release v1.1.0

## 🚀 新功能
- feat(user): 添加用户头像上传功能
- feat(role): 支持角色权限批量管理
- feat(log): 新增审计日志导出功能

## 🐛 Bug修复
- fix(auth): 修复JWT令牌刷新失败问题
- fix(user): 修复用户信息更新时的验证问题

## 🔧 改进优化
- perf(query): 优化数据库查询性能
- refactor(service): 重构用户服务层架构

## 📚 文档更新
- docs(api): 更新API接口文档
- docs(guide): 添加部署指南

## 🔄 依赖更新
- build(deps): 升级Spring Boot到2.7.18
- chore(deps): 更新MyBatis-Plus到3.5.3.1

## ⚠️ 破坏性变更
- BREAKING CHANGE: 用户API响应格式调整，需要前端配合更新

## 📥 下载链接
- [Source code (zip)](https://github.com/example/biou/archive/v1.1.0.zip)
- [Source code (tar.gz)](https://github.com/example/biou/archive/v1.1.0.tar.gz)

## 🔍 完整变更日志
查看 [完整变更日志](https://github.com/example/biou/compare/v1.0.0...v1.1.0)
```

## 5. 常用Git命令

### 5.1 日常开发命令

```bash
# 查看状态
git status

# 查看变更
git diff
git diff --staged

# 添加文件
git add .
git add <file>

# 提交变更
git commit -m "feat(user): 添加用户注册功能"

# 推送到远程
git push origin feature/user-registration

# 拉取最新代码
git pull origin develop

# 查看提交历史
git log --oneline
git log --graph --oneline --all
```

### 5.2 分支操作命令

```bash
# 创建并切换分支
git checkout -b feature/new-feature

# 切换分支
git checkout develop

# 查看分支
git branch -a

# 删除本地分支
git branch -d feature/old-feature

# 删除远程分支
git push origin --delete feature/old-feature

# 合并分支
git merge --no-ff feature/user-profile

# 变基操作
git rebase develop
```

### 5.3 撤销操作命令

```bash
# 撤销工作区修改
git checkout -- <file>
git restore <file>

# 撤销暂存区修改
git reset HEAD <file>
git restore --staged <file>

# 撤销提交
git reset --soft HEAD~1    # 保留修改，撤销提交
git reset --hard HEAD~1    # 丢弃修改，撤销提交

# 回滚到指定提交
git revert <commit-hash>
```

### 5.4 远程仓库命令

```bash
# 查看远程仓库
git remote -v

# 添加远程仓库
git remote add upstream <url>

# 同步上游仓库
git fetch upstream
git merge upstream/main

# 推送标签
git push origin --tags
```

## 6. 提交前检查清单

### 6.1 代码质量检查

- [ ] 代码符合项目编码规范
- [ ] 没有使用Lombok注解
- [ ] 所有方法都有适当的注释
- [ ] 没有跨层调用违反架构原则
- [ ] 异常处理完整且合理
- [ ] 没有硬编码的配置信息
- [ ] 没有调试代码和无用注释

### 6.2 功能完整性检查

- [ ] 功能实现符合需求规格
- [ ] 添加了必要的参数校验
- [ ] 重要操作添加了审计日志
- [ ] 敏感操作添加了权限检查
- [ ] 数据库操作有事务控制
- [ ] 缓存策略合理有效

### 6.3 测试覆盖检查

- [ ] 编写了单元测试
- [ ] 测试覆盖率达到要求
- [ ] 集成测试通过
- [ ] 手动测试验证功能
- [ ] 性能测试通过(如适用)
- [ ] 安全测试通过(如适用)

### 6.4 文档更新检查

- [ ] 更新了API文档
- [ ] 更新了README文档
- [ ] 添加了代码注释
- [ ] 更新了部署文档(如适用)
- [ ] 更新了用户手册(如适用)

### 6.5 Git操作检查

- [ ] 提交信息符合规范
- [ ] 选择了正确的分支类型
- [ ] 没有提交敏感信息
- [ ] 文件权限设置正确
- [ ] .gitignore配置合理

## 💻 Windows支持

### Windows环境配置

本项目完全支持Windows开发环境，提供了多种安装方式：

#### 方案1：使用Git Bash（推荐）
```bash
# 1. 安装Git for Windows (包含Git Bash)
# 下载地址：https://git-scm.com/download/win

# 2. 右键项目文件夹 → Git Bash Here
# 3. 运行安装脚本
./scripts/setup-git-hooks.sh
```

#### 方案2：使用Windows批处理
```cmd
# 在Windows命令提示符或PowerShell中运行
scripts\setup-git-hooks.bat
```

#### 方案3：手动安装
```cmd
# 复制钩子文件
copy hooks\pre-commit .git\hooks\
copy hooks\commit-msg .git\hooks\

# 或者使用批处理版本
copy hooks\pre-commit.bat .git\hooks\pre-commit.bat
copy hooks\commit-msg.bat .git\hooks\commit-msg.bat
```

### Windows特有配置
```bash
# 设置换行符转换（Windows推荐）
git config core.autocrlf true

# 解决中文乱码问题
git config core.quotepath false
chcp 65001  # 设置UTF-8编码

# 长路径支持
git config core.longpaths true
```

### Windows钩子执行
- **Git Bash**: 执行原始的bash脚本 (hooks/pre-commit, hooks/commit-msg)
- **Windows命令行**: 执行批处理版本 (hooks/pre-commit.bat, hooks/commit-msg.bat)
- **IDEA等IDE**: 自动选择合适的执行方式

## 📝 Git配置建议

### 6.1 全局配置

```bash
# 设置用户信息
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# 设置默认编辑器
git config --global core.editor "vim"

# 设置默认分支名
git config --global init.defaultBranch main

# 启用颜色输出
git config --global color.ui auto

# 设置推送策略
git config --global push.default simple

# 启用自动换行转换
git config --global core.autocrlf input  # Mac/Linux
git config --global core.autocrlf true   # Windows
```

### 6.2 项目配置

```bash
# 设置项目特定的配置
git config user.name "Project Name"
git config user.email "project@company.com"

# 设置提交模板
git config commit.template .gitmessage

# 设置钩子脚本
cp hooks/pre-commit .git/hooks/
chmod +x .git/hooks/pre-commit
```

### 6.3 推荐的Git别名

```bash
# 添加实用的Git别名
git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.ci commit
git config --global alias.st status
git config --global alias.lg "log --graph --oneline --all"
git config --global alias.last "log -1 HEAD"
git config --global alias.unstage "reset HEAD --"
```

---

严格遵循本Git规范，可以确保项目版本控制的规范性和团队协作的高效性。规范的提交信息和分支管理有助于项目的长期维护和问题追溯。 