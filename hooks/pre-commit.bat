@echo off
chcp 65001 >nul

echo 🔍 执行提交前检查...

REM 检查是否有文件被添加到暂存区
git diff --cached --quiet
if %errorlevel% equ 0 (
    echo ❌ 没有文件被暂存，无法提交
    exit /b 1
)

REM 获取暂存的Java文件
for /f "delims=" %%i in ('git diff --cached --name-only --diff-filter=ACM ^| findstr "\.java$"') do (
    set "has_java=true"
    echo 📁 检查的Java文件: %%i
)

if not defined has_java (
    echo ℹ️ 没有Java文件变更，跳过Java相关检查
    goto check_sensitive
)

echo.
echo 🚫 检查Lombok使用...
set lombok_violations=0

for /f "delims=" %%i in ('git diff --cached --name-only --diff-filter=ACM ^| findstr "\.java$"') do (
    findstr /c:"import lombok." "%%i" >nul 2>&1
    if not errorlevel 1 (
        echo ❌ 发现Lombok使用: %%i
        set /a lombok_violations+=1
    )
    findstr /c:"@Data\|@Getter\|@Setter\|@Builder\|@AllArgsConstructor\|@NoArgsConstructor" "%%i" >nul 2>&1
    if not errorlevel 1 (
        echo ❌ 发现Lombok使用: %%i
        set /a lombok_violations+=1
    )
)

if %lombok_violations% gtr 0 (
    echo.
    echo ❌ 发现 %lombok_violations% 个文件使用了Lombok，项目禁止使用Lombok
    echo 请手动实现getter、setter、constructor等方法
    exit /b 1
)
echo ✅ 未发现Lombok使用

echo.
echo 🏗️ 检查架构层次...
set arch_violations=0

for /f "delims=" %%i in ('git diff --cached --name-only --diff-filter=ACM ^| findstr "\.java$"') do (
    echo %%i | findstr "controller" >nul
    if not errorlevel 1 (
        findstr /c:"import.*\.mapper\.\|import.*\.dao\." "%%i" >nul 2>&1
        if not errorlevel 1 (
            echo ❌ Controller层直接引用Mapper/DAO: %%i
            set /a arch_violations+=1
        )
    )
    
    echo %%i | findstr "service" | findstr /v "impl" >nul
    if not errorlevel 1 (
        findstr /c:"import.*\.mapper\.\|import.*\.dao\." "%%i" >nul 2>&1
        if not errorlevel 1 (
            echo ❌ Service接口层引用Mapper/DAO: %%i
            set /a arch_violations+=1
        )
    )
    
    echo %%i | findstr "repository" | findstr /v "impl" >nul
    if not errorlevel 1 (
        findstr /c:"import com.baomidou.mybatisplus" "%%i" >nul 2>&1
        if not errorlevel 1 (
            echo ❌ Repository接口暴露MyBatis-Plus类型: %%i
            set /a arch_violations+=1
        )
    )
)

if %arch_violations% gtr 0 (
    echo.
    echo ❌ 发现 %arch_violations% 个架构层次违规
    echo 请检查四层架构原则: Controller -^> Service -^> Repository -^> Mapper
    exit /b 1
)
echo ✅ 架构层次检查通过

echo.
echo 📏 检查代码格式...
set format_violations=0

for /f "delims=" %%i in ('git diff --cached --name-only --diff-filter=ACM ^| findstr "\.java$"') do (
    REM 检查行尾空格（简化版本）
    findstr /c:" $" "%%i" >nul 2>&1
    if not errorlevel 1 (
        echo ❌ 发现行尾空格: %%i
        set /a format_violations+=1
    )
)

if %format_violations% gtr 0 (
    echo.
    echo ❌ 发现 %format_violations% 个格式问题
    echo 请修复代码格式问题后重新提交
    exit /b 1
)
echo ✅ 代码格式检查通过

:check_sensitive
echo.
echo 🔐 检查敏感信息...
set sensitive_violations=0

for /f "delims=" %%i in ('git diff --cached --name-only --diff-filter=ACM') do (
    findstr /i /c:"password.*=.*[^*]" "%%i" >nul 2>&1
    if not errorlevel 1 (
        echo ❌ 可能包含敏感信息: %%i
        set /a sensitive_violations+=1
        goto end_sensitive_check
    )
    findstr /i /c:"secret.*=.*[^*]" "%%i" >nul 2>&1
    if not errorlevel 1 (
        echo ❌ 可能包含敏感信息: %%i
        set /a sensitive_violations+=1
        goto end_sensitive_check
    )
    findstr /i /c:"key.*=.*[^*]" "%%i" >nul 2>&1
    if not errorlevel 1 (
        echo ❌ 可能包含敏感信息: %%i
        set /a sensitive_violations+=1
        goto end_sensitive_check
    )
)

:end_sensitive_check
if %sensitive_violations% gtr 0 (
    echo.
    echo ❌ 发现 %sensitive_violations% 个文件可能包含敏感信息
    echo 请检查并移除密码、密钥等敏感信息
    exit /b 1
)
echo ✅ 敏感信息检查通过

echo.
echo 📝 检查TODO/FIXME注释...
set todo_count=0

for /f "delims=" %%i in ('git diff --cached --name-only --diff-filter=ACM') do (
    findstr /i /c:"TODO\|FIXME\|XXX" "%%i" >nul 2>&1
    if not errorlevel 1 (
        set /a todo_count+=1
    )
)

if %todo_count% gtr 0 (
    echo ⚠️ 发现 %todo_count% 个文件包含TODO/FIXME注释
    echo 建议在提交前处理这些待办项
)

echo.
echo ✅ 所有检查通过，允许提交!
exit /b 0 