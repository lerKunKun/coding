# Git规范快速开始

本文档帮助团队成员快速配置和使用项目的Git规范。

## 📋 前置条件

确保你已经完成了以下准备工作：
- ✅ 已克隆项目到本地
- ✅ 已安装Git工具
- ✅ 已配置Git用户信息

## 🚀 快速配置

### 1. 一键安装Git钩子

在项目根目录执行以下命令：

```bash
./scripts/setup-git-hooks.sh
```

这个脚本会自动完成：
- 安装pre-commit和commit-msg钩子
- 配置提交信息模板
- 设置推荐的Git配置
- 配置实用的Git别名

### 2. 配置用户信息（如果未配置）

```bash
# 配置项目级别用户信息
git config user.name "Your Name"
git config user.email "your.email@company.com"

# 或配置全局用户信息
git config --global user.name "Your Name"
git config --global user.email "your.email@company.com"
```

## 📝 提交规范演示

### 正确的提交信息格式

```bash
# 新功能提交
git commit -m "feat(user): 添加用户头像上传功能"

# Bug修复提交
git commit -m "fix(auth): 修复登录验证失败问题"

# 文档更新提交
git commit -m "docs(readme): 更新安装说明"

# 代码重构提交
git commit -m "refactor(service): 重构用户服务层"
```

### 详细提交信息示例

```bash
git commit -m "feat(user): 添加用户密码重置功能

- 支持邮箱验证码重置密码
- 添加密码强度验证
- 集成短信验证服务
- 添加重置记录日志

Closes #234"
```

### 错误的提交信息（会被钩子阻止）

```bash
# ❌ 缺少类型
git commit -m "添加用户功能"

# ❌ 类型错误
git commit -m "add(user): 添加用户功能"

# ❌ 主题过长
git commit -m "feat(user): 添加用户头像上传功能并且支持多种格式包括JPG和PNG以及GIF等格式的图片"

# ❌ 以句号结尾
git commit -m "feat(user): 添加用户功能。"
```

## 🌳 分支管理演示

### 功能开发流程

```bash
# 1. 切换到develop分支并拉取最新代码
git checkout develop
git pull origin develop

# 2. 创建功能分支
git checkout -b feature/user-profile

# 3. 开发功能并提交
git add .
git commit -m "feat(user): 添加用户个人资料管理功能"

# 4. 推送功能分支
git push origin feature/user-profile

# 5. 创建Pull Request到develop分支
# (在GitHub/GitLab等平台上创建PR)

# 6. 合并后删除功能分支
git checkout develop
git pull origin develop
git branch -d feature/user-profile
git push origin --delete feature/user-profile
```

### 热修复流程

```bash
# 1. 从main分支创建hotfix分支
git checkout main
git pull origin main
git checkout -b hotfix/critical-security-fix

# 2. 修复问题并提交
git add .
git commit -m "fix(security): 修复SQL注入安全漏洞"

# 3. 合并到main分支
git checkout main
git merge --no-ff hotfix/critical-security-fix
git tag -a v1.0.1 -m "Hotfix version 1.0.1"
git push origin main --tags

# 4. 合并回develop分支
git checkout develop
git merge --no-ff hotfix/critical-security-fix
git push origin develop

# 5. 删除hotfix分支
git branch -d hotfix/critical-security-fix
git push origin --delete hotfix/critical-security-fix
```

## 🔧 实用Git别名

安装脚本会自动配置以下别名：

```bash
# 查看状态
git st                    # 代替 git status

# 提交代码
git ci -m "message"       # 代替 git commit -m "message"

# 切换分支
git co branch-name        # 代替 git checkout branch-name

# 查看分支
git br                    # 代替 git branch

# 查看图形化日志
git lg                    # 代替 git log --graph --oneline --all

# 查看最后一次提交
git last                  # 代替 git log -1 HEAD

# 取消暂存
git unstage file          # 代替 git reset HEAD file
```

## 🧪 钩子功能测试

### 测试提交信息检查

```bash
# 测试错误的提交信息（会被拒绝）
echo "test" > test.txt
git add test.txt
git commit -m "错误的提交信息格式"

# 输出示例：
# ❌ 提交信息格式不符合规范!
# 规范格式: <type>(<scope>): <subject>
# ...
```

### 测试代码质量检查

```bash
# 创建包含Lombok的测试文件
echo "import lombok.Data;" > TestFile.java
git add TestFile.java
git commit -m "test(example): 测试Lombok检查"

# 输出示例：
# 🔍 执行提交前检查...
# 🚫 检查Lombok使用...
# ❌ 发现Lombok使用: TestFile.java
# ❌ 发现 1 个文件使用了Lombok，项目禁止使用Lombok
```

## 📋 常见问题

### Q: 如何跳过钩子检查？

A: 在特殊情况下，可以使用 `--no-verify` 参数跳过钩子检查：

```bash
git commit --no-verify -m "emergency fix"
```

**注意**: 仅在紧急情况下使用，正常开发请遵循规范。

### Q: 钩子不工作怎么办？

A: 检查以下几点：

1. 确保钩子文件有执行权限：
```bash
ls -la .git/hooks/
```

2. 重新运行安装脚本：
```bash
./scripts/setup-git-hooks.sh
```

3. 检查Git配置：
```bash
git config --list | grep commit.template
```

### Q: 如何更新钩子？

A: 当项目钩子有更新时，重新运行安装脚本即可：

```bash
./scripts/setup-git-hooks.sh
```

### Q: 提交信息模板在哪里？

A: 提交时会自动显示模板，你也可以手动查看：

```bash
cat .gitmessage
```

## 🎯 最佳实践

### 1. 提交频率
- 功能完成一个小模块就提交一次
- 每次提交保持原子性，只包含相关的修改
- 避免一次提交包含多个不相关的修改

### 2. 提交信息
- 使用中文描述，简洁明了
- 第一行不超过50个字符
- 如需详细说明，使用空行分隔正文

### 3. 分支命名
- 使用小写字母和连字符
- 包含功能描述：`feature/user-authentication`
- 避免使用个人名字：`feature/zhangsan-login`

### 4. 代码审查
- 创建有意义的PR标题和描述
- 及时回应审查意见
- 保持PR粒度合适，避免过大的PR

## 📚 进一步学习

- [完整Git提交规范](GIT_COMMIT_STANDARDS.md)
- [项目编写规范](CODING_STANDARDS.md)
- [功能扩展指南](FEATURE_EXTENSION_GUIDE.md)

---

🎉 恭喜！你已经掌握了项目的Git规范。开始规范的团队协作吧！ 