#!/bin/bash

# Git钩子安装脚本
# 用于安装项目的Git钩子和配置Git环境

echo "🚀 开始配置Git环境和钩子..."

# 检查是否在Git仓库中
if [ ! -d ".git" ]; then
    echo "❌ 当前目录不是Git仓库根目录"
    echo "请在项目根目录下运行此脚本"
    exit 1
fi

# 创建.git/hooks目录（如果不存在）
mkdir -p .git/hooks

# 复制钩子文件
echo "📋 安装Git钩子..."

if [ -f "hooks/pre-commit" ]; then
    cp hooks/pre-commit .git/hooks/
    chmod +x .git/hooks/pre-commit
    echo "✅ 已安装pre-commit钩子"
else
    echo "⚠️ 未找到hooks/pre-commit文件"
fi

if [ -f "hooks/commit-msg" ]; then
    cp hooks/commit-msg .git/hooks/
    chmod +x .git/hooks/commit-msg
    echo "✅ 已安装commit-msg钩子"
else
    echo "⚠️ 未找到hooks/commit-msg文件"
fi

# 配置提交信息模板
echo ""
echo "📝 配置提交信息模板..."
if [ -f ".gitmessage" ]; then
    git config commit.template .gitmessage
    echo "✅ 已配置提交信息模板"
else
    echo "⚠️ 未找到.gitmessage文件"
fi

# 配置推荐的Git设置
echo ""
echo "⚙️ 配置推荐的Git设置..."

# 设置默认分支名
git config init.defaultBranch main
echo "✅ 设置默认分支名为main"

# 启用颜色输出
git config color.ui auto
echo "✅ 启用颜色输出"

# 设置推送策略
git config push.default simple
echo "✅ 设置推送策略为simple"

# 设置自动换行转换（根据操作系统）
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
    git config core.autocrlf true
    echo "✅ 设置Windows换行符转换"
else
    git config core.autocrlf input
    echo "✅ 设置Unix/Mac换行符转换"
fi

# 配置实用的Git别名
echo ""
echo "🔧 配置Git别名..."
git config alias.co checkout
git config alias.br branch
git config alias.ci commit
git config alias.st status
git config alias.lg "log --graph --oneline --all"
git config alias.last "log -1 HEAD"
git config alias.unstage "reset HEAD --"
echo "✅ 已配置Git别名"

# 显示当前Git配置
echo ""
echo "📊 当前Git配置:"
echo "用户名: $(git config user.name || echo '未设置')"
echo "邮箱: $(git config user.email || echo '未设置')"
echo "默认分支: $(git config init.defaultBranch)"
echo "提交模板: $(git config commit.template || echo '未设置')"

# 检查用户配置
if [ -z "$(git config user.name)" ] || [ -z "$(git config user.email)" ]; then
    echo ""
    echo "⚠️ 检测到Git用户信息未配置，请运行以下命令:"
    echo "git config user.name \"Your Name\""
    echo "git config user.email \"your.email@example.com\""
    echo ""
    echo "或者配置全局用户信息:"
    echo "git config --global user.name \"Your Name\""
    echo "git config --global user.email \"your.email@example.com\""
fi

echo ""
echo "🎉 Git环境配置完成!"
echo ""
echo "📖 使用提示:"
echo "1. 提交时会自动检查代码质量和提交信息格式"
echo "2. 使用 'git ci' 代替 'git commit'"
echo "3. 使用 'git st' 代替 'git status'"
echo "4. 使用 'git lg' 查看图形化日志"
echo "5. 提交信息格式: <type>(<scope>): <subject>"
echo ""
echo "📚 更多信息请查看: docs/GIT_COMMIT_STANDARDS.md"

# 测试钩子是否正常工作
echo ""
echo "🧪 测试钩子配置..."
if [ -x ".git/hooks/pre-commit" ]; then
    echo "✅ pre-commit钩子可执行"
else
    echo "❌ pre-commit钩子不可执行"
fi

if [ -x ".git/hooks/commit-msg" ]; then
    echo "✅ commit-msg钩子可执行"
else
    echo "❌ commit-msg钩子不可执行"
fi

echo ""
echo "完成! 现在可以开始使用规范的Git工作流了 🚀" 