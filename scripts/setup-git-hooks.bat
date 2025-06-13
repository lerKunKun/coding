@echo off
setlocal enabledelayedexpansion

:: Git Hooks Setup Script for BIOU Project (Windows)
:: This script sets up Git hooks for SQL validation and commit message checking

echo [92m🔧 Setting up Git hooks for BIOU project (Windows)...[0m

:: Check if we're in a git repository
if not exist ".git" (
    echo [91m❌ Error: Not in a Git repository root directory[0m
    echo Please run this script from the project root directory
    pause
    exit /b 1
)

:: Create hooks directory if it doesn't exist
if not exist ".git\hooks" mkdir ".git\hooks"

:: Function to backup existing hooks
:backup_hook
set "hook_name=%~1"
if exist ".git\hooks\%hook_name%" (
    if not exist ".git\hooks\%hook_name%.backup" (
        echo 📋 Backing up existing %hook_name% hook...
        copy ".git\hooks\%hook_name%" ".git\hooks\%hook_name%.backup" >nul
    )
)
goto :eof

:: Function to create Windows-compatible hooks
:create_windows_hook
set "hook_name=%~1"
set "hook_description=%~2"

echo 🔗 Creating %hook_name% hook (%hook_description%)...

:: Create Windows batch wrapper for the hook
echo @echo off > ".git\hooks\%hook_name%.bat"
echo :: Windows wrapper for %hook_name% hook >> ".git\hooks\%hook_name%.bat"
echo bash ".git\hooks\%hook_name%.sh" %%* >> ".git\hooks\%hook_name%.bat"

:: Create the actual hook script
echo #!/bin/bash > ".git\hooks\%hook_name%.sh"

if "%hook_name%"=="pre-commit" (
    call :create_precommit_script
) else if "%hook_name%"=="commit-msg" (
    call :create_commitmsg_script
)

:: Create the hook without extension (for Git compatibility)
copy ".git\hooks\%hook_name%.sh" ".git\hooks\%hook_name%" >nul

echo [92m✅ %hook_name% hook created successfully[0m
goto :eof

:: Create pre-commit script content
:create_precommit_script
(
echo.
echo # Git pre-commit hook for SQL file validation
echo # This hook will run before each commit to validate SQL files
echo.
echo echo "🔍 Running pre-commit SQL validation..."
echo.
echo # Color codes for output
echo RED='\033[0;31m'
echo GREEN='\033[0;32m'
echo YELLOW='\033[1;33m'
echo NC='\033[0m' # No Color
echo.
echo # Get list of staged SQL files
echo staged_sql_files=$^(git diff --cached --name-only --diff-filter=ACM ^| grep -E '\.\^(sql\^)$'^)
echo.
echo if [ -z "$staged_sql_files" ]; then
echo     echo "✅ No SQL files to validate"
echo     exit 0
echo fi
echo.
echo echo "📁 Found SQL files to validate:"
echo echo "$staged_sql_files"
echo.
echo # Function to check SQL standards compliance
echo check_sql_standards^(^) {
echo     local file=$1
echo     echo "📋 Checking standards compliance: $file"
echo     
echo     if [ ! -f "$file" ]; then
echo         return 0
echo     fi
echo     
echo     local errors=0
echo     
echo     # Check for required header in migration files
echo     if [[ "$file" == sql/migrations/* ]]; then
echo         if ! grep -q "脚本名称:" "$file"; then
echo             echo -e "${RED}❌ Missing script header in migration file: $file${NC}"
echo             echo "   Migration files must include proper header with 脚本名称, 创建时间, etc."
echo             errors=$^(^(errors + 1^)^)
echo         fi
echo     fi
echo     
echo     # Check for potentially dangerous operations without safeguards
echo     if grep -q "DROP TABLE" "$file" ^&^& ! grep -q "IF EXISTS" "$file"; then
echo         echo -e "${YELLOW}⚠️  Found DROP TABLE without IF EXISTS in: $file${NC}"
echo         echo "   Consider using 'DROP TABLE IF EXISTS' for safer operations"
echo     fi
echo     
echo     return $errors
echo }
echo.
echo # Function to check file naming convention
echo check_naming_convention^(^) {
echo     local file=$1
echo     
echo     # Check migration file naming
echo     if [[ "$file" == sql/migrations/* ]]; then
echo         local filename=$^(basename "$file"^)
echo         if ! [[ "$filename" =~ ^[0-9]{3}_[a-z0-9_]+\.sql$ ]]; then
echo             echo -e "${RED}❌ Invalid migration file naming: $file${NC}"
echo             echo "   Migration files should follow pattern: 001_description.sql"
echo             return 1
echo         fi
echo     fi
echo     
echo     return 0
echo }
echo.
echo # Main validation loop
echo validation_failed=false
echo.
echo for file in $staged_sql_files; do
echo     echo ""
echo     echo "🔍 Validating: $file"
echo     
echo     # Check naming convention
echo     if ! check_naming_convention "$file"; then
echo         validation_failed=true
echo         continue
echo     fi
echo     
echo     # Check standards compliance
echo     if ! check_sql_standards "$file"; then
echo         validation_failed=true
echo         continue
echo     fi
echo     
echo     echo -e "${GREEN}✅ $file passed validation${NC}"
echo done
echo.
echo if [ "$validation_failed" = true ]; then
echo     echo -e "${RED}❌ SQL validation failed. Please fix the issues above before committing.${NC}"
echo     echo ""
echo     echo "💡 Tips:"
echo     echo "   - Use sql/templates/migration_template.sql for new migration scripts"
echo     echo "   - Use sql/templates/rollback_template.sql for rollback scripts"
echo     echo "   - Review the SQL standards: docs/SQL_STANDARDS.md"
echo     exit 1
echo else
echo     echo -e "${GREEN}✅ All SQL files passed validation!${NC}"
echo     exit 0
echo fi
) >> ".git\hooks\%hook_name%.sh"
goto :eof

:: Create commit-msg script content
:create_commitmsg_script
(
echo.
echo # Git commit-msg hook for commit message validation
echo # This hook validates commit message format and content
echo.
echo commit_msg_file=$1
echo commit_msg=$^(cat "$commit_msg_file"^)
echo.
echo # Color codes for output
echo RED='\033[0;31m'
echo GREEN='\033[0;32m'
echo YELLOW='\033[1;33m'
echo NC='\033[0m' # No Color
echo.
echo echo "📝 Validating commit message..."
echo.
echo # Check if commit message is empty
echo if [ -z "$^(echo "$commit_msg" ^| tr -d '[:space:]'^)" ]; then
echo     echo -e "${RED}❌ Empty commit message is not allowed${NC}"
echo     exit 1
echo fi
echo.
echo # Check commit message format for SQL changes
echo staged_sql_files=$^(git diff --cached --name-only --diff-filter=ACM ^| grep -E '\.\^(sql\^)$'^)
echo.
echo if [ -n "$staged_sql_files" ]; then
echo     echo "🔍 SQL files detected in commit, checking message format..."
echo     
echo     # Check if commit message follows conventional commit format for SQL changes
echo     if echo "$commit_msg" ^| grep -qE "^\^(feat^|fix^|docs^|style^|refactor^|test^|chore^|sql\^)\^(\^\^(.+\^\^)\^)?: .+"; then
echo         echo -e "${GREEN}✅ Commit message format is valid${NC}"
echo     else
echo         echo -e "${RED}❌ Invalid commit message format for SQL changes${NC}"
echo         echo ""
echo         echo "For commits containing SQL files, please use one of these formats:"
echo         echo "  sql: add user profile table"
echo         echo "  sql^(migration^): add authentication fields to user table"
echo         echo "  sql^(rollback^): rollback user profile table creation"
echo         echo ""
echo         echo "Current message: $commit_msg"
echo         exit 1
echo     fi
echo fi
echo.
echo # Success message
echo echo -e "${GREEN}✅ Commit message validation passed${NC}"
echo exit 0
) >> ".git\hooks\%hook_name%.sh"
goto :eof

echo.
echo 🔍 Checking for existing hooks...

:: Backup existing hooks if they exist
call :backup_hook "pre-commit"
call :backup_hook "commit-msg"

:: Create hooks
echo.
echo 📦 Creating Git hooks for Windows...

call :create_windows_hook "pre-commit" "SQL file validation"
call :create_windows_hook "commit-msg" "Commit message format validation"

:: Test Git Bash availability
echo.
echo 🧪 Testing Git Bash availability...
bash --version >nul 2>&1
if errorlevel 1 (
    echo [93m⚠️  Git Bash not found in PATH[0m
    echo Please ensure Git for Windows is installed and Git Bash is available
    echo Download from: https://git-scm.com/download/win
) else (
    echo [92m✅ Git Bash is available[0m
)

:: Create hook configuration documentation
echo.
echo 📝 Creating hook documentation...

(
echo # Git Hooks for BIOU Project ^(Windows^)
echo.
echo This directory contains Git hooks for Windows environment.
echo.
echo ## Windows-Specific Setup
echo.
echo ### Requirements
echo - Git for Windows ^(includes Git Bash^)
echo - Windows PowerShell or Command Prompt
echo.
echo ### Hook Files
echo - `.git/hooks/pre-commit` - Main hook file ^(Unix format^)
echo - `.git/hooks/pre-commit.bat` - Windows batch wrapper
echo - `.git/hooks/pre-commit.sh` - Shell script version
echo - `.git/hooks/commit-msg` - Main hook file ^(Unix format^)
echo - `.git/hooks/commit-msg.bat` - Windows batch wrapper  
echo - `.git/hooks/commit-msg.sh` - Shell script version
echo.
echo ### Usage
echo Hooks work automatically with Git commands. Git will use the appropriate format based on your environment.
echo.
echo ### Troubleshooting Windows Issues
echo.
echo #### Hook not running
echo ```cmd
echo # Check if Git Bash is installed
echo bash --version
echo.
echo # Make sure hooks are executable
echo git config core.hooksPath .git/hooks
echo ```
echo.
echo #### Permission issues
echo ```cmd
echo # Run as Administrator if needed
echo # Or use Git Bash instead of Command Prompt
echo ```
echo.
echo #### Line ending issues
echo ```cmd
echo git config core.autocrlf true
echo git config core.eol crlf
echo ```
echo.
echo ### Manual Hook Installation
echo If automatic setup fails:
echo ```cmd
echo # Run the setup script
echo scripts\setup-git-hooks.bat
echo.
echo # Or run in Git Bash
echo bash scripts/setup-git-hooks.sh
echo ```
) > ".git\hooks\README-Windows.md"

echo.
echo [92m🎉 Git hooks setup completed successfully for Windows![0m
echo.
echo 📋 Summary of installed hooks:
echo   ✅ pre-commit: SQL file validation ^(Windows compatible^)
echo   ✅ commit-msg: Commit message format validation ^(Windows compatible^)
echo.
echo 📖 Windows documentation created at: .git\hooks\README-Windows.md
echo.
echo 💡 Windows Tips:
echo   - Hooks will work in Git Bash, PowerShell, and Command Prompt
echo   - Use Git for Windows for best compatibility
echo   - Run 'git commit --no-verify' to bypass hooks if needed
echo   - Update hooks by running this script again
echo.
echo [94m🚀 You're all set! Happy coding with validated SQL commits on Windows![0m
echo.
pause 