#!/bin/bash

# Git Hooks Setup Script for BIOU Project
# This script sets up Git hooks for SQL validation and commit message checking

set -e

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🔧 Setting up Git hooks for BIOU project...${NC}"

# Check if we're in a git repository
if [ ! -d ".git" ]; then
    echo -e "${RED}❌ Error: Not in a Git repository root directory${NC}"
    echo "Please run this script from the project root directory"
    exit 1
fi

# Create hooks directory if it doesn't exist
mkdir -p .git/hooks

# Function to backup existing hooks
backup_existing_hook() {
    local hook_name=$1
    if [ -f ".git/hooks/$hook_name" ] && [ ! -f ".git/hooks/$hook_name.backup" ]; then
        echo "📋 Backing up existing $hook_name hook..."
        cp ".git/hooks/$hook_name" ".git/hooks/$hook_name.backup"
    fi
}

# Function to verify a hook exists and make it executable
verify_hook() {
    local hook_name=$1
    local hook_description=$2
    
    echo "🔗 Verifying $hook_name hook ($hook_description)..."
    
    if [ -f ".git/hooks/$hook_name" ]; then
        # Make sure it's executable
        chmod +x ".git/hooks/$hook_name"
        echo -e "${GREEN}✅ $hook_name hook is installed and executable${NC}"
        return 0
    else
        echo -e "${RED}❌ $hook_name hook not found${NC}"
        echo "Please ensure the hook file exists at .git/hooks/$hook_name"
        return 1
    fi
}

# Function to test hooks
test_hooks() {
    echo ""
    echo -e "${BLUE}🧪 Testing Git hooks...${NC}"
    
    # Test pre-commit hook
    if [ -f ".git/hooks/pre-commit" ]; then
        echo "🔍 Testing pre-commit hook with no staged files..."
        if .git/hooks/pre-commit; then
            echo -e "${GREEN}✅ pre-commit hook test passed${NC}"
        else
            echo -e "${YELLOW}⚠️  pre-commit hook returned non-zero exit code (expected with no staged files)${NC}"
        fi
    fi
    
    # Test commit-msg hook
    if [ -f ".git/hooks/commit-msg" ]; then
        echo "🔍 Testing commit-msg hook..."
        echo "test: sample commit message" > /tmp/test-commit-msg
        if .git/hooks/commit-msg /tmp/test-commit-msg; then
            echo -e "${GREEN}✅ commit-msg hook test passed${NC}"
        else
            echo -e "${RED}❌ commit-msg hook test failed${NC}"
        fi
        rm -f /tmp/test-commit-msg
    fi
}

# Main installation process
echo ""
echo "🔍 Checking for existing hooks..."

# Backup existing hooks if they exist
backup_existing_hook "pre-commit"
backup_existing_hook "commit-msg"

# Verify hooks
echo ""
echo "📦 Verifying Git hooks..."

verify_hook "pre-commit" "SQL file validation"
verify_hook "commit-msg" "Commit message format validation"

# Test the installed hooks
test_hooks

# Create hook configuration documentation
echo ""
echo "📝 Creating hook documentation..."

cat > .git/hooks/README.md << 'EOF'
# Git Hooks for BIOU Project

This directory contains Git hooks that are automatically triggered during Git operations.

## Installed Hooks

### pre-commit
- **Purpose**: Validates SQL files before commit
- **Checks**:
  - SQL syntax validation
  - File naming conventions
  - Standards compliance (header format, existence checks)
  - Missing rollback scripts warning
- **File**: `.git/hooks/pre-commit`

### commit-msg
- **Purpose**: Validates commit message format
- **Checks**:
  - Conventional commit format for SQL changes
  - Version information in migration commits
  - Rollback commits are clearly marked
  - Message length recommendations
- **File**: `.git/hooks/commit-msg`

## Hook Management

### Disable hooks temporarily
```bash
git commit --no-verify  # Skip pre-commit and commit-msg hooks
```

### Re-enable hooks
Hooks are enabled by default. If disabled, run:
```bash
./scripts/setup-git-hooks.sh
```

### Update hooks
Run the setup script again to update hooks:
```bash
./scripts/setup-git-hooks.sh
```

## Troubleshooting

### Hook not running
- Check if hook file has execute permissions: `ls -la .git/hooks/`
- Make hook executable: `chmod +x .git/hooks/hook-name`

### Hook failing
- Check hook output for specific error messages
- Verify SQL syntax manually: `mysql -u user -p database < script.sql`
- Review SQL standards: `docs/SQL_STANDARDS.md`

### Bypass hooks (emergency only)
```bash
git commit --no-verify -m "emergency fix"
```

## Standards Reference

- SQL Standards: `docs/SQL_STANDARDS.md`
- Commit Message Format: Conventional Commits
- File Organization: `sql/README.md`
EOF

echo ""
echo -e "${GREEN}🎉 Git hooks setup completed successfully!${NC}"
echo ""
echo "📋 Summary of installed hooks:"
echo "  ✅ pre-commit: SQL file validation"
echo "  ✅ commit-msg: Commit message format validation"
echo ""
echo "📖 Documentation created at: .git/hooks/README.md"
echo ""
echo "💡 Tips:"
echo "  - Hooks will run automatically on git commit"
echo "  - Use 'git commit --no-verify' to bypass hooks (emergency only)"
echo "  - Update hooks by running this script again"
echo "  - Check hook documentation: cat .git/hooks/README.md"
echo ""
echo -e "${BLUE}🚀 You're all set! Happy coding with validated SQL commits!${NC}" 