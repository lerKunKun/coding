@echo off
chcp 65001 >nul

REM Git commit-msg hook for Windows
REM 验证提交信息是否符合Conventional Commits规范

set "commit_msg_file=%1"
if "%commit_msg_file%"=="" (
    echo ❌ 未提供提交信息文件
    exit /b 1
)

REM 读取提交信息的第一行
set /p commit_msg=<"%commit_msg_file%"

REM 检查是否是合并提交或回滚提交
echo %commit_msg% | findstr /b "Merge " >nul
if not errorlevel 1 (
    exit /b 0
)

echo %commit_msg% | findstr /b "Revert " >nul
if not errorlevel 1 (
    exit /b 0
)

REM 检查提交信息格式
REM 简化的正则表达式检查：type(scope): subject 或 type: subject
echo %commit_msg% | findstr /r "^feat\|^fix\|^docs\|^style\|^refactor\|^perf\|^test\|^build\|^ci\|^chore\|^revert" >nul
if errorlevel 1 (
    goto format_error
)

REM 检查是否包含冒号
echo %commit_msg% | findstr ":" >nul
if errorlevel 1 (
    goto format_error
)

REM 提取主题部分（冒号后的内容）
for /f "tokens=2* delims=:" %%a in ("%commit_msg%") do set "subject=%%a"

REM 去除主题前的空格
set "subject=%subject:~1%"

REM 检查主题长度（简化检查）
if "%subject%"=="" (
    echo ❌ 提交主题不能为空!
    goto usage_info
)

REM 检查主题长度（Windows批处理中字符串长度检查比较复杂，这里简化处理）
REM 如果主题超过80个字符，可能太长了
set "long_subject=%subject%xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
if not "%long_subject:~100%"=="" (
    echo ❌ 提交主题可能过长! 建议不超过50个字符
    echo 主题: %subject%
    echo.
    goto usage_info
)

REM 检查是否以句号结尾
if "%subject:~-1%"=="." (
    echo ❌ 提交主题不应以句号结尾!
    echo 主题: %subject%
    echo.
    goto usage_info
)

echo ✅ 提交信息格式正确!
exit /b 0

:format_error
echo ❌ 提交信息格式不符合规范!
echo.
echo 规范格式: ^<type^>^(^<scope^>^): ^<subject^>
echo.

:usage_info
echo 类型(type)必须是以下之一:
echo   feat:     新功能
echo   fix:      修复bug
echo   docs:     文档变更
echo   style:    代码格式修改
echo   refactor: 代码重构
echo   perf:     性能优化
echo   test:     测试相关
echo   build:    构建系统修改
echo   ci:       CI配置修改
echo   chore:    其他修改
echo   revert:   回滚提交
echo.
echo 示例:
echo   feat(user): 添加用户注册功能
echo   fix(auth): 修复登录验证失败问题
echo   docs(readme): 更新安装说明
echo.
echo 您的提交信息: %commit_msg%
echo.
exit /b 1 