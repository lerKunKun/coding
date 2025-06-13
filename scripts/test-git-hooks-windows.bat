@echo off
setlocal enabledelayedexpansion

echo [96m🧪 Testing Git Hooks on Windows...[0m
echo.

:: Check if we're in a git repository
if not exist ".git" (
    echo [91m❌ Error: Not in a Git repository[0m
    exit /b 1
)

:: Check if hooks exist
echo [93m📋 Checking hook files...[0m

set "hooks_exist=true"

if not exist ".git\hooks\pre-commit" (
    echo [91m❌ pre-commit hook not found[0m
    set "hooks_exist=false"
) else (
    echo [92m✅ pre-commit hook found[0m
)

if not exist ".git\hooks\commit-msg" (
    echo [91m❌ commit-msg hook not found[0m
    set "hooks_exist=false"
) else (
    echo [92m✅ commit-msg hook found[0m
)

if "%hooks_exist%"=="false" (
    echo.
    echo [93m💡 Install hooks first:[0m
    echo   scripts\setup-git-hooks.bat
    echo.
    pause
    exit /b 1
)

echo.
echo [93m🔍 Testing commit-msg hook...[0m

:: Test valid commit message
echo test: valid commit message format > test-commit-msg.tmp
if exist ".git\hooks\commit-msg.bat" (
    call .git\hooks\commit-msg.bat test-commit-msg.tmp
) else (
    bash .git\hooks\commit-msg test-commit-msg.tmp
)

if %errorlevel% equ 0 (
    echo [92m✅ Valid commit message test passed[0m
) else (
    echo [91m❌ Valid commit message test failed[0m
)

echo.

:: Test invalid commit message
echo invalid commit message format > test-commit-msg.tmp
if exist ".git\hooks\commit-msg.bat" (
    call .git\hooks\commit-msg.bat test-commit-msg.tmp >nul 2>&1
) else (
    bash .git\hooks\commit-msg test-commit-msg.tmp >nul 2>&1
)

if %errorlevel% neq 0 (
    echo [92m✅ Invalid commit message test passed (correctly rejected)[0m
) else (
    echo [91m❌ Invalid commit message test failed (should be rejected)[0m
)

:: Clean up
del test-commit-msg.tmp >nul 2>&1

echo.
echo [93m🔍 Testing pre-commit hook...[0m

:: Test pre-commit hook with no staged files
if exist ".git\hooks\pre-commit.bat" (
    call .git\hooks\pre-commit.bat >nul 2>&1
) else (
    bash .git\hooks\pre-commit >nul 2>&1
)

if %errorlevel% equ 0 (
    echo [92m✅ Pre-commit hook test passed (no staged files)[0m
) else (
    echo [93m⚠️  Pre-commit hook returned non-zero (expected with no staged files)[0m
)

echo.
echo [93m📁 Checking Windows compatibility files...[0m

:: Check for Windows-specific hook files
if exist ".git\hooks\pre-commit.bat" (
    echo [92m✅ Windows batch wrapper found: pre-commit.bat[0m
) else (
    echo [93m⚠️  Windows batch wrapper not found: pre-commit.bat[0m
)

if exist ".git\hooks\commit-msg.bat" (
    echo [92m✅ Windows batch wrapper found: commit-msg.bat[0m
) else (
    echo [93m⚠️  Windows batch wrapper not found: commit-msg.bat[0m
)

if exist ".git\hooks\README-Windows.md" (
    echo [92m✅ Windows documentation found[0m
) else (
    echo [93m⚠️  Windows documentation not found[0m
)

echo.
echo [93m🔧 Testing Git Bash availability...[0m

bash --version >nul 2>&1
if %errorlevel% equ 0 (
    echo [92m✅ Git Bash is available[0m
) else (
    echo [91m❌ Git Bash not found in PATH[0m
    echo [93m💡 Please install Git for Windows: https://git-scm.com/download/win[0m
)

echo.
echo [93m📊 Environment Information:[0m
echo   OS: %OS%
echo   Processor: %PROCESSOR_ARCHITECTURE%
echo   Shell: %ComSpec%

if defined POWERSHELL_VERSION (
    echo   PowerShell: %POWERSHELL_VERSION%
)

echo.
echo [96m🎉 Windows Git Hooks test completed![0m
echo.
echo [93m💡 Usage Tips:[0m
echo   - Use 'git commit -m "type: message"' for normal commits
echo   - Use 'git commit --no-verify' to bypass hooks (emergency only)
echo   - Run setup script again to update hooks
echo   - Check .git\hooks\README-Windows.md for detailed documentation
echo.
pause 