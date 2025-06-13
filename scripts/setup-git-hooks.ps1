# Git Hooks Setup Script for BIOU Project (PowerShell)
# This script sets up Git hooks for SQL validation and commit message checking on Windows

# Set strict mode for better error handling
Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

Write-Host "🔧 Setting up Git hooks for BIOU project (PowerShell)..." -ForegroundColor Cyan

# Check if we're in a git repository
if (-not (Test-Path ".git")) {
    Write-Host "❌ Error: Not in a Git repository root directory" -ForegroundColor Red
    Write-Host "Please run this script from the project root directory" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Create hooks directory if it doesn't exist
if (-not (Test-Path ".git\hooks")) {
    New-Item -ItemType Directory -Path ".git\hooks" -Force | Out-Null
}

# Function to backup existing hooks
function Backup-Hook {
    param($HookName)
    
    $hookPath = ".git\hooks\$HookName"
    $backupPath = ".git\hooks\$HookName.backup"
    
    if ((Test-Path $hookPath) -and (-not (Test-Path $backupPath))) {
        Write-Host "📋 Backing up existing $HookName hook..." -ForegroundColor Yellow
        Copy-Item $hookPath $backupPath
    }
}

# Function to create Windows-compatible hooks
function Create-WindowsHook {
    param($HookName, $HookDescription)
    
    Write-Host "🔗 Creating $HookName hook ($HookDescription)..." -ForegroundColor Green
    
    # Create Windows batch wrapper for the hook
    $batContent = @"
@echo off
:: Windows wrapper for $HookName hook
bash ".git\hooks\$HookName.sh" %*
"@
    $batContent | Out-File -FilePath ".git\hooks\$HookName.bat" -Encoding ASCII
    
    # Create PowerShell wrapper
    $ps1Content = @"
# PowerShell wrapper for $HookName hook
& bash ".git\hooks\$HookName.sh" @args
"@
    $ps1Content | Out-File -FilePath ".git\hooks\$HookName.ps1" -Encoding UTF8
    
    # Create the actual hook script
    $shellScript = "#!/bin/bash`n"
    
    if ($HookName -eq "pre-commit") {
        $shellScript += Get-PreCommitScript
    } elseif ($HookName -eq "commit-msg") {
        $shellScript += Get-CommitMsgScript
    }
    
    # Write shell script
    $shellScript | Out-File -FilePath ".git\hooks\$HookName.sh" -Encoding UTF8 -NoNewline
    
    # Create the hook without extension (for Git compatibility)
    Copy-Item ".git\hooks\$HookName.sh" ".git\hooks\$HookName"
    
    Write-Host "✅ $HookName hook created successfully" -ForegroundColor Green
}

# Pre-commit script content
function Get-PreCommitScript {
    return @'

# Git pre-commit hook for SQL file validation
# This hook will run before each commit to validate SQL files

echo "🔍 Running pre-commit SQL validation..."

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Get list of staged SQL files
staged_sql_files=$(git diff --cached --name-only --diff-filter=ACM | grep -E '\.(sql)$')

if [ -z "$staged_sql_files" ]; then
    echo "✅ No SQL files to validate"
    exit 0
fi

echo "📁 Found SQL files to validate:"
echo "$staged_sql_files"

# Function to check SQL standards compliance
check_sql_standards() {
    local file=$1
    echo "📋 Checking standards compliance: $file"
    
    if [ ! -f "$file" ]; then
        return 0
    fi
    
    local errors=0
    
    # Check for required header in migration files
    if [[ "$file" == sql/migrations/* ]]; then
        if ! grep -q "脚本名称:" "$file"; then
            echo -e "${RED}❌ Missing script header in migration file: $file${NC}"
            echo "   Migration files must include proper header with 脚本名称, 创建时间, etc."
            errors=$((errors + 1))
        fi
    fi
    
    # Check for potentially dangerous operations without safeguards
    if grep -q "DROP TABLE" "$file" && ! grep -q "IF EXISTS" "$file"; then
        echo -e "${YELLOW}⚠️  Found DROP TABLE without IF EXISTS in: $file${NC}"
        echo "   Consider using 'DROP TABLE IF EXISTS' for safer operations"
    fi
    
    return $errors
}

# Function to check file naming convention
check_naming_convention() {
    local file=$1
    
    # Check migration file naming
    if [[ "$file" == sql/migrations/* ]]; then
        local filename=$(basename "$file")
        if ! [[ "$filename" =~ ^[0-9]{3}_[a-z0-9_]+\.sql$ ]]; then
            echo -e "${RED}❌ Invalid migration file naming: $file${NC}"
            echo "   Migration files should follow pattern: 001_description.sql"
            return 1
        fi
    fi
    
    return 0
}

# Main validation loop
validation_failed=false

for file in $staged_sql_files; do
    echo ""
    echo "🔍 Validating: $file"
    
    # Check naming convention
    if ! check_naming_convention "$file"; then
        validation_failed=true
        continue
    fi
    
    # Check standards compliance
    if ! check_sql_standards "$file"; then
        validation_failed=true
        continue
    fi
    
    echo -e "${GREEN}✅ $file passed validation${NC}"
done

if [ "$validation_failed" = true ]; then
    echo -e "${RED}❌ SQL validation failed. Please fix the issues above before committing.${NC}"
    echo ""
    echo "💡 Tips:"
    echo "   - Use sql/templates/migration_template.sql for new migration scripts"
    echo "   - Use sql/templates/rollback_template.sql for rollback scripts"
    echo "   - Review the SQL standards: docs/SQL_STANDARDS.md"
    exit 1
else
    echo -e "${GREEN}✅ All SQL files passed validation!${NC}"
    exit 0
fi
'@
}

# Commit-msg script content
function Get-CommitMsgScript {
    return @'

# Git commit-msg hook for commit message validation
# This hook validates commit message format and content

commit_msg_file=$1
commit_msg=$(cat "$commit_msg_file")

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "📝 Validating commit message..."

# Check if commit message is empty
if [ -z "$(echo "$commit_msg" | tr -d '[:space:]')" ]; then
    echo -e "${RED}❌ Empty commit message is not allowed${NC}"
    exit 1
fi

# Check commit message format for SQL changes
staged_sql_files=$(git diff --cached --name-only --diff-filter=ACM | grep -E '\.(sql)$')

if [ -n "$staged_sql_files" ]; then
    echo "🔍 SQL files detected in commit, checking message format..."
    
    # Check if commit message follows conventional commit format for SQL changes
    if echo "$commit_msg" | grep -qE "^(feat|fix|docs|style|refactor|test|chore|sql)(\(.+\))?: .+"; then
        echo -e "${GREEN}✅ Commit message format is valid${NC}"
    else
        echo -e "${RED}❌ Invalid commit message format for SQL changes${NC}"
        echo ""
        echo "For commits containing SQL files, please use one of these formats:"
        echo "  sql: add user profile table"
        echo "  sql(migration): add authentication fields to user table"
        echo "  sql(rollback): rollback user profile table creation"
        echo ""
        echo "Current message: $commit_msg"
        exit 1
    fi
fi

# Success message
echo -e "${GREEN}✅ Commit message validation passed${NC}"
exit 0
'@
}

Write-Host ""
Write-Host "🔍 Checking for existing hooks..." -ForegroundColor Yellow

# Backup existing hooks if they exist
Backup-Hook "pre-commit"
Backup-Hook "commit-msg"

# Create hooks
Write-Host ""
Write-Host "📦 Creating Git hooks for Windows..." -ForegroundColor Cyan

Create-WindowsHook "pre-commit" "SQL file validation"
Create-WindowsHook "commit-msg" "Commit message format validation"

# Test Git Bash availability
Write-Host ""
Write-Host "🧪 Testing Git Bash availability..." -ForegroundColor Yellow

try {
    $gitBashVersion = & bash --version 2>$null
    Write-Host "✅ Git Bash is available" -ForegroundColor Green
    Write-Host "   Version: $($gitBashVersion[0])" -ForegroundColor Gray
} catch {
    Write-Host "⚠️  Git Bash not found in PATH" -ForegroundColor Yellow
    Write-Host "Please ensure Git for Windows is installed and Git Bash is available" -ForegroundColor Yellow
    Write-Host "Download from: https://git-scm.com/download/win" -ForegroundColor Yellow
}

# Test PowerShell execution policy
Write-Host ""
Write-Host "🧪 Checking PowerShell execution policy..." -ForegroundColor Yellow

$executionPolicy = Get-ExecutionPolicy
if ($executionPolicy -eq "Restricted") {
    Write-Host "⚠️  PowerShell execution policy is Restricted" -ForegroundColor Yellow
    Write-Host "You may need to run: Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser" -ForegroundColor Yellow
} else {
    Write-Host "✅ PowerShell execution policy: $executionPolicy" -ForegroundColor Green
}

# Create hook configuration documentation
Write-Host ""
Write-Host "📝 Creating hook documentation..." -ForegroundColor Cyan

$windowsDocContent = @'
# Git Hooks for BIOU Project (Windows)

This directory contains Git hooks for Windows environment.

## Windows-Specific Setup

### Requirements
- Git for Windows (includes Git Bash)
- Windows PowerShell 5.1+ or PowerShell Core 7+
- Command Prompt support

### Hook Files
- `.git/hooks/pre-commit` - Main hook file (Unix format)
- `.git/hooks/pre-commit.bat` - Windows batch wrapper
- `.git/hooks/pre-commit.ps1` - PowerShell wrapper
- `.git/hooks/pre-commit.sh` - Shell script version
- `.git/hooks/commit-msg` - Main hook file (Unix format)
- `.git/hooks/commit-msg.bat` - Windows batch wrapper
- `.git/hooks/commit-msg.ps1` - PowerShell wrapper
- `.git/hooks/commit-msg.sh` - Shell script version

### Usage
Hooks work automatically with Git commands. Git will use the appropriate format based on your environment:
- Git Bash: Uses shell scripts directly
- Command Prompt: Uses .bat wrappers
- PowerShell: Can use .ps1 wrappers

### Installation Options

#### Option 1: Run Batch Script (Recommended)
```cmd
scripts\setup-git-hooks.bat
```

#### Option 2: Run PowerShell Script
```powershell
.\scripts\setup-git-hooks.ps1
```

#### Option 3: Run in Git Bash
```bash
bash scripts/setup-git-hooks.sh
```

### Troubleshooting Windows Issues

#### Hook not running
```cmd
# Check if Git Bash is installed
bash --version

# Check Git configuration
git config core.hooksPath
git config --global core.hooksPath
```

#### Permission issues
```cmd
# Run as Administrator if needed
# Or use Git Bash instead of Command Prompt
```

#### PowerShell execution policy
```powershell
# Check current policy
Get-ExecutionPolicy

# Set policy for current user (if needed)
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

#### Line ending issues
```cmd
git config core.autocrlf true
git config core.eol crlf
```

### Manual Hook Testing

#### Test pre-commit hook
```bash
# In Git Bash
.git/hooks/pre-commit

# Or in PowerShell
& .git/hooks/pre-commit.ps1
```

#### Test commit-msg hook
```bash
# Create test message file
echo "test: sample commit message" > test-msg.txt

# Test in Git Bash
.git/hooks/commit-msg test-msg.txt

# Or in PowerShell
& .git/hooks/commit-msg.ps1 test-msg.txt
```

### Common Windows Git Clients
- Git for Windows (Git Bash) ✅ Supported
- GitHub Desktop ✅ Supported
- GitKraken ✅ Supported
- SourceTree ✅ Supported
- Visual Studio ✅ Supported
- VSCode ✅ Supported

### Environment Variables
The hooks will work in these Windows environments:
- Command Prompt (`cmd.exe`)
- PowerShell (`powershell.exe`)
- PowerShell Core (`pwsh.exe`)
- Git Bash (`bash.exe`)
- Windows Terminal
- IDE integrated terminals
'@

$windowsDocContent | Out-File -FilePath ".git\hooks\README-Windows.md" -Encoding UTF8

Write-Host ""
Write-Host "🎉 Git hooks setup completed successfully for Windows!" -ForegroundColor Green
Write-Host ""
Write-Host "📋 Summary of installed hooks:" -ForegroundColor Cyan
Write-Host "  ✅ pre-commit: SQL file validation (Windows compatible)" -ForegroundColor Green
Write-Host "  ✅ commit-msg: Commit message format validation (Windows compatible)" -ForegroundColor Green
Write-Host ""
Write-Host "📖 Windows documentation created at: .git\hooks\README-Windows.md" -ForegroundColor Cyan
Write-Host ""
Write-Host "💡 Windows Tips:" -ForegroundColor Yellow
Write-Host "  - Hooks will work in Git Bash, PowerShell, and Command Prompt" -ForegroundColor Gray
Write-Host "  - Use Git for Windows for best compatibility" -ForegroundColor Gray
Write-Host "  - Run 'git commit --no-verify' to bypass hooks if needed" -ForegroundColor Gray
Write-Host "  - Update hooks by running this script again" -ForegroundColor Gray
Write-Host ""
Write-Host "🚀 You're all set! Happy coding with validated SQL commits on Windows!" -ForegroundColor Blue
Write-Host ""
Read-Host "Press Enter to exit" 