@echo off
chcp 65001 >nul
echo 🚀 开始配置Git环境和钩子...

REM 检查是否在Git仓库中
if not exist ".git" (
    echo ❌ 当前目录不是Git仓库根目录
    echo 请在项目根目录下运行此脚本
    pause
    exit /b 1
)

REM 创建.git/hooks目录（如果不存在）
if not exist ".git\hooks" mkdir ".git\hooks"

REM 复制钩子文件
echo 📋 安装Git钩子...

if exist "hooks\pre-commit" (
    copy "hooks\pre-commit" ".git\hooks\" >nul
    echo ✅ 已安装pre-commit钩子
) else (
    echo ⚠️ 未找到hooks/pre-commit文件
)

if exist "hooks\commit-msg" (
    copy "hooks\commit-msg" ".git\hooks\" >nul
    echo ✅ 已安装commit-msg钩子
) else (
    echo ⚠️ 未找到hooks/commit-msg文件
)

REM 配置提交信息模板
echo.
echo 📝 配置提交信息模板...
if exist ".gitmessage" (
    git config commit.template .gitmessage
    echo ✅ 已配置提交信息模板
) else (
    echo ⚠️ 未找到.gitmessage文件
)

REM 配置推荐的Git设置
echo.
echo ⚙️ 配置推荐的Git设置...

REM 设置默认分支名
git config init.defaultBranch main
echo ✅ 设置默认分支名为main

REM 启用颜色输出
git config color.ui auto
echo ✅ 启用颜色输出

REM 设置推送策略
git config push.default simple
echo ✅ 设置推送策略为simple

REM 设置Windows换行符转换
git config core.autocrlf true
echo ✅ 设置Windows换行符转换

REM 配置实用的Git别名
echo.
echo 🔧 配置Git别名...
git config alias.co checkout
git config alias.br branch
git config alias.ci commit
git config alias.st status
git config alias.lg "log --graph --oneline --all"
git config alias.last "log -1 HEAD"
git config alias.unstage "reset HEAD --"
echo ✅ 已配置Git别名

REM 显示当前Git配置
echo.
echo 📊 当前Git配置:
for /f "tokens=*" %%a in ('git config user.name 2^>nul') do set username=%%a
for /f "tokens=*" %%a in ('git config user.email 2^>nul') do set useremail=%%a
for /f "tokens=*" %%a in ('git config init.defaultBranch 2^>nul') do set defaultbranch=%%a
for /f "tokens=*" %%a in ('git config commit.template 2^>nul') do set template=%%a

if defined username (echo 用户名: %username%) else (echo 用户名: 未设置)
if defined useremail (echo 邮箱: %useremail%) else (echo 邮箱: 未设置)
if defined defaultbranch (echo 默认分支: %defaultbranch%) else (echo 默认分支: 未设置)
if defined template (echo 提交模板: %template%) else (echo 提交模板: 未设置)

REM 检查用户配置
if not defined username (
    echo.
    echo ⚠️ 检测到Git用户信息未配置，请运行以下命令:
    echo git config user.name "Your Name"
    echo git config user.email "your.email@example.com"
    echo.
    echo 或者配置全局用户信息:
    echo git config --global user.name "Your Name"
    echo git config --global user.email "your.email@example.com"
)

echo.
echo 🎉 Git环境配置完成!
echo.
echo 📖 使用提示:
echo 1. 提交时会自动检查代码质量和提交信息格式
echo 2. 使用 'git ci' 代替 'git commit'
echo 3. 使用 'git st' 代替 'git status'
echo 4. 使用 'git lg' 查看图形化日志
echo 5. 提交信息格式: ^<type^>^(^<scope^>^): ^<subject^>
echo.
echo 📚 更多信息请查看: docs\GIT_COMMIT_STANDARDS.md

REM 测试钩子是否正常工作
echo.
echo 🧪 测试钩子配置...
if exist ".git\hooks\pre-commit" (
    echo ✅ pre-commit钩子已安装
) else (
    echo ❌ pre-commit钩子未安装
)

if exist ".git\hooks\commit-msg" (
    echo ✅ commit-msg钩子已安装
) else (
    echo ❌ commit-msg钩子未安装
)

echo.
echo 完成! 现在可以开始使用规范的Git工作流了 🚀
echo.
echo 💡 提示: 如果钩子在Windows中执行有问题，请使用Git Bash
pause 