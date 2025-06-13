# Windows 环境配置指南

本文档详细说明如何在Windows环境下配置和使用BIOU项目的Git Hooks和SQL规范。

## 📋 前置要求

### 必需软件
- **Git for Windows** - 包含Git Bash、Git GUI等工具
  - 下载：https://git-scm.com/download/win
  - 建议选择"Git Bash Here"和"Git GUI Here"选项
- **Windows PowerShell 5.1+** 或 **PowerShell Core 7+**
- **命令提示符** (cmd.exe) - Windows内置

### 推荐软件
- **Windows Terminal** - 现代化终端体验
- **Visual Studio Code** - 支持Git集成
- **MySQL Workbench** - MySQL数据库管理

## 🚀 快速安装

### 方式1：批处理脚本（推荐新手）

1. 打开命令提示符或PowerShell
2. 导航到项目根目录
3. 运行安装脚本：

```cmd
scripts\setup-git-hooks.bat
```

### 方式2：PowerShell脚本（推荐高级用户）

1. 打开PowerShell（建议以管理员身份）
2. 确保执行策略允许脚本运行：

```powershell
# 检查当前策略
Get-ExecutionPolicy

# 如果是Restricted，设置为RemoteSigned
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

3. 运行安装脚本：

```powershell
.\scripts\setup-git-hooks.ps1
```

### 方式3：Git Bash（推荐开发者）

1. 在项目文件夹右键选择"Git Bash Here"
2. 运行Unix风格的安装脚本：

```bash
./scripts/setup-git-hooks.sh
```

## 🔧 Hook文件说明

安装完成后，以下文件将被创建：

### Unix格式（Git标准）
- `.git/hooks/pre-commit` - 主要的pre-commit hook
- `.git/hooks/commit-msg` - 主要的commit-msg hook

### Windows批处理格式
- `.git/hooks/pre-commit.bat` - Windows cmd兼容版本
- `.git/hooks/commit-msg.bat` - Windows cmd兼容版本

### PowerShell格式
- `.git/hooks/pre-commit.ps1` - PowerShell版本
- `.git/hooks/commit-msg.ps1` - PowerShell版本

### Shell脚本格式
- `.git/hooks/pre-commit.sh` - Bash版本
- `.git/hooks/commit-msg.sh` - Bash版本

### 文档
- `.git/hooks/README-Windows.md` - Windows特定说明

## 🧪 测试安装

运行测试脚本验证安装：

```cmd
scripts\test-git-hooks-windows.bat
```

预期输出示例：
```
🧪 Testing Git Hooks on Windows...

📋 Checking hook files...
✅ pre-commit hook found
✅ commit-msg hook found

🔍 Testing commit-msg hook...
✅ Valid commit message test passed
✅ Invalid commit message test passed (correctly rejected)

🔍 Testing pre-commit hook...
✅ Pre-commit hook test passed (no staged files)

📁 Checking Windows compatibility files...
✅ Windows batch wrapper found: pre-commit.bat
✅ Windows batch wrapper found: commit-msg.bat
✅ Windows documentation found

🔧 Testing Git Bash availability...
✅ Git Bash is available
```

## 🌍 跨环境兼容性

### 命令提示符 (cmd.exe)
- 使用 `.bat` 文件执行hooks
- 支持基本的SQL验证和提交信息检查
- 兼容所有Windows版本

### PowerShell
- 优先使用 `.ps1` 文件
- 支持彩色输出和高级功能
- 需要适当的执行策略

### Git Bash
- 直接使用Unix格式的hook文件
- 完整的Linux命令支持
- 最佳的开发体验

### IDE集成
- **Visual Studio Code**: 自动检测并使用合适的hook格式
- **IntelliJ IDEA**: 支持Git hooks，可能需要配置Git路径
- **GitHub Desktop**: 完全兼容，透明执行hooks

## 🔍 故障排除

### 问题1：Hooks不执行

**症状**: Git提交时没有运行验证检查

**解决方案**:
```cmd
# 检查hook文件是否存在
dir .git\hooks\pre-commit*
dir .git\hooks\commit-msg*

# 检查Git配置
git config core.hooksPath
git config --global core.hooksPath

# 重新安装hooks
scripts\setup-git-hooks.bat
```

### 问题2：PowerShell执行策略错误

**症状**: `无法加载文件，因为在此系统上禁止运行脚本`

**解决方案**:
```powershell
# 查看当前策略
Get-ExecutionPolicy

# 设置用户级策略
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# 或者临时绕过
PowerShell -ExecutionPolicy Bypass -File scripts\setup-git-hooks.ps1
```

### 问题3：Git Bash找不到

**症状**: `'bash' 不是内部或外部命令`

**解决方案**:
1. 确保安装了Git for Windows
2. 检查环境变量PATH中包含Git目录
3. 重新安装Git for Windows，选择"Git from the command line and also from 3rd-party software"选项

### 问题4：中文乱码

**解决方案**:
```cmd
# 设置控制台编码为UTF-8
chcp 65001

# 配置Git支持中文
git config --global core.quotepath false
git config --global gui.encoding utf-8
git config --global i18n.commit.encoding utf-8
git config --global i18n.logoutputencoding utf-8
```

### 问题5：长路径支持

**症状**: `Filename too long` 错误

**解决方案**:
```cmd
# 启用长路径支持
git config --global core.longpaths true

# 在Windows 10/11上启用长路径支持（需管理员权限）
# 组策略: 计算机配置 > 管理模板 > 系统 > 文件系统 > 启用长路径
```

## 📝 最佳实践

### 1. 终端选择

- **日常开发**: Git Bash（最接近Linux体验）
- **系统管理**: PowerShell（强大的脚本能力）
- **快速操作**: 命令提示符（启动快速）

### 2. 编辑器配置

Visual Studio Code设置：
```json
{
    "git.enableSmartCommit": true,
    "git.confirmSync": false,
    "terminal.integrated.defaultProfile.windows": "Git Bash",
    "files.eol": "\n"
}
```

### 3. Git配置建议

```bash
# 统一换行符处理
git config --global core.autocrlf true

# 支持中文文件名
git config --global core.quotepath false

# 启用颜色输出
git config --global color.ui auto

# 设置默认编辑器
git config --global core.editor "code --wait"
```

## 🎯 Windows特有功能

### 1. 批处理脚本增强

- 彩色输出支持
- 错误处理和日志记录
- 环境检测和自动配置

### 2. PowerShell集成

- 丰富的对象模型
- 强类型参数验证
- 详细的错误报告

### 3. 系统集成

- 右键菜单集成
- 文件关联支持
- Windows服务集成

## 📚 相关文档

- [SQL规范文档](SQL_STANDARDS.md) - 完整的SQL开发规范
- [Git提交规范](GIT_COMMIT_STANDARDS.md) - 提交信息格式要求
- [项目快速开始](../README.md) - 项目总体说明

## 🆘 获取帮助

如果在Windows环境下遇到问题：

1. 查看Windows特定文档：`.git/hooks/README-Windows.md`
2. 运行测试脚本检查环境：`scripts\test-git-hooks-windows.bat`
3. 检查相关issue或提交新issue
4. 联系项目维护者

---

**注意**: Windows环境下的Git Hooks已经过全面测试，支持主流的Windows版本和Git客户端。如果遇到兼容性问题，请及时反馈。 