# Biou Project

基于Spring Boot + MySQL + Redis + MyBatis-Plus的四层架构项目，集成JWT认证、钉钉登录、RBAC权限控制和完整的日志管理系统

## 🌍 跨平台支持

本项目提供完整的跨平台开发支持，所有开发规范和工具都已针对不同操作系统进行优化：

✅ **Windows** - 支持命令提示符、PowerShell、Git Bash  
✅ **macOS** - 支持Terminal、Zsh、Bash  
✅ **Linux** - 支持各种Shell环境  
✅ **IDE集成** - 完美兼容VSCode、IntelliJ IDEA、GitHub Desktop等

### 快速开始

**Windows用户：**
```cmd
# 方式1：批处理脚本（推荐新手）
scripts\setup-git-hooks.bat

# 方式2：PowerShell脚本（推荐高级用户）
.\scripts\setup-git-hooks.ps1

# 方式3：Git Bash（推荐开发者）
bash scripts/setup-git-hooks.sh
```

**Unix/Linux/macOS用户：**
```bash
# 一键安装Git钩子和环境配置
./scripts/setup-git-hooks.sh
```

详细的Windows配置指南请参考：[docs/WINDOWS_SETUP.md](docs/WINDOWS_SETUP.md)

## 项目特色

### 🏗️ 严格的四层架构
- **Controller → Service → Repository → Mapper** 清晰的层次分离
- **禁止跨层调用**，保证架构清洁
- **DTO数据传输**，不暴露ORM实现细节

### 🔐 完整的认证授权系统
- JWT无状态认证机制
- 钉钉OAuth2扫码登录集成
- 用户-角色-权限三级RBAC模型
- 支持权限继承和层级结构
- BCrypt密码安全加密存储
- 预置完整的权限数据和测试用户

### 📝 企业级日志管理
- 审计日志、系统日志、登录日志三套体系
- 基于注解的自动日志记录
- 完整的日志查询和统计功能

### 🗄️ 生产级SQL管理
- 版本化迁移脚本管理（按版本组织）
- 自动SQL语法验证和规范检查
- 完整的回滚脚本和安全检查机制
- 跨平台Git Hooks自动验证
- 企业级SQL开发规范和最佳实践

### 🚫 零Lombok依赖
- 手写所有getter/setter/constructor方法
- 代码可读性更强，调试更容易
- 避免IDE插件依赖问题

### 📊 规范的错误处理
- 统一的异常处理机制
- 标准化的API响应格式
- 完善的参数校验

### 🔧 完善的开发工具链
- Git Hooks自动代码质量检查
- 跨平台开发环境支持
- 自动化测试和验证脚本
- 详细的开发规范文档

## 技术架构

### 技术栈
- **Spring Boot 2.7.18** - 主框架
- **Spring Security** - 安全框架
- **MySQL 8.0.33** - 数据库
- **Redis** - 缓存
- **MyBatis-Plus 3.5.3.1** - ORM框架
- **Druid 1.2.20** - 数据库连接池
- **FastJSON2 2.0.43** - JSON处理
- **JJWT 0.11.5** - JWT处理
- **HttpClient 4.5.14** - HTTP客户端

### 四层架构设计
```
┌─────────────────┐
│   Controller层   │  ← HTTP请求处理，参数校验，响应封装
├─────────────────┤
│    Service层     │  ← 业务逻辑处理，事务控制，缓存管理
├─────────────────┤
│  Repository层    │  ← 数据访问封装，隐藏ORM实现细节
├─────────────────┤
│    Mapper层      │  ← 数据库操作，SQL映射
└─────────────────┘
```

### 数据传输对象（DTO）体系
```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   CreateDTO  │    │  QueryDTO    │    │   UpdateDTO  │
│   (创建请求)   │    │  (查询条件)   │    │  (更新请求)   │
└──────┬───────┘    └──────┬───────┘    └──────┬───────┘
       │                   │                   │
       ▼                   ▼                   ▼
┌─────────────────────────────────────────────────────┐
│                   Entity                           │
│                  (数据实体)                          │
└─────────────────────┬───────────────────────────────┘
                      │
                      ▼
              ┌──────────────┐
              │      VO      │
              │   (响应对象)   │
              └──────────────┘
```

### Repository层设计原则
Repository层采用**收敛设计**，提供标准化的数据访问接口，完全隐藏ORM实现：

**继承MyBatis-Plus IService的基础方法：**
- `getById()` - 根据ID查询
- `save()` / `saveBatch()` - 保存
- `updateById()` - 根据ID更新
- `removeById()` - 根据ID删除

**扩展的通用方法（使用DTO封装）：**
- `findOne(QueryDTO)` - 根据条件查询单个实体
- `list(QueryDTO)` - 根据条件查询列表
- `page(Page, QueryDTO)` - 根据条件分页查询
- `count(QueryDTO)` - 根据条件统计数量
- `update(Entity, QueryDTO)` - 根据条件更新
- `remove(QueryDTO)` - 根据条件删除

**核心设计优势：**
- 🔒 **完全封装**：上层永不接触Wrapper等ORM概念
- 🎯 **业务导向**：通过DTO表达业务查询需求
- 🔄 **易于替换**：可无缝切换到其他ORM框架
- 📋 **标准统一**：所有Repository具有一致的接口

### 转换层架构
- **Convert层** - 负责DTO、Entity、VO之间的转换
- **静态方法设计** - 避免Spring Bean管理的复杂性
- **类型安全** - 编译期保证转换的正确性

## 认证授权系统

### JWT认证机制
采用无状态JWT认证，支持访问令牌和刷新令牌双重机制：

**JWT配置特性：**
- **访问令牌(Access Token)** - 默认24小时有效期
- **刷新令牌(Refresh Token)** - 默认7天有效期
- **令牌黑名单** - Redis存储已注销令牌，防止令牌重用
- **HMAC-SHA256签名** - 使用强密钥确保令牌安全性
- **自动刷新机制** - 临近过期时自动刷新访问令牌

### 钉钉OAuth2集成
集成钉钉企业级扫码登录，支持企业用户快速认证：

**钉钉登录特性：**
- **授权码模式** - 标准OAuth2授权码流程
- **CSRF防护** - 使用state参数防止跨站请求伪造
- **自动用户创建** - 首次登录自动创建系统用户
- **用户信息同步** - 同步钉钉用户基本信息
- **登录状态管理** - 与JWT认证系统无缝集成

**环境配置：**
```yaml
dingtalk:
  app-id: ${DINGTALK_APP_ID}      # 钉钉应用ID
  app-secret: ${DINGTALK_APP_SECRET}  # 钉钉应用密钥
  redirect-uri: ${DINGTALK_REDIRECT_URI}  # 登录回调地址
```

### 密码安全策略
采用BCrypt算法进行密码加密存储：

**安全特性：**
- **BCrypt加密** - 自适应哈希算法，防止彩虹表攻击
- **盐值随机** - 每个密码使用独特的盐值
- **强度可配置** - 默认强度为10，可根据需要调整
- **密码升级** - 提供数据库升级脚本迁移明文密码

**预置测试账户：**
| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | admin123 | 管理员 | 系统管理员账户 |
| test | test123 | 测试用户 | 普通测试账户 |

### 安全拦截机制
基于Spring Security的多层安全防护：

**拦截特性：**
- **JWT过滤器** - 自动验证和解析JWT令牌
- **权限验证** - 基于RBAC模型的细粒度权限控制
- **会话管理** - 无状态会话，避免CSRF攻击
- **异常处理** - 统一的认证授权异常处理
- **白名单机制** - 支持接口白名单配置

## RBAC权限控制系统

### 权限模型
采用标准RBAC（Role-Based Access Control）模型：
```
用户(User) → 角色(Role) → 权限(Permission)
```

### 数据库设计
- **t_user** - 用户表
- **t_role** - 角色表
- **t_permission** - 权限表
- **t_user_role** - 用户角色关联表
- **t_role_permission** - 角色权限关联表

### 权限类型
- **menu** - 菜单权限
- **button** - 按钮权限
- **api** - 接口权限

### 预置角色
- **ADMIN** - 系统管理员（拥有全部权限）
- **USER_MANAGER** - 用户管理员（用户管理相关权限）
- **GUEST** - 访客（只有查看权限）
- **OPERATOR** - 操作员（业务操作权限）

### 预置权限结构
```
系统管理(SYSTEM)
├── 用户管理(SYSTEM:USER)
│   ├── 用户列表(SYSTEM:USER:LIST)
│   ├── 创建用户(SYSTEM:USER:CREATE)
│   ├── 更新用户(SYSTEM:USER:UPDATE)
│   ├── 删除用户(SYSTEM:USER:DELETE)
│   └── 用户状态(SYSTEM:USER:STATUS)
├── 角色管理(SYSTEM:ROLE)
│   ├── 角色列表(SYSTEM:ROLE:LIST)
│   ├── 创建角色(SYSTEM:ROLE:CREATE)
│   ├── 更新角色(SYSTEM:ROLE:UPDATE)
│   ├── 删除角色(SYSTEM:ROLE:DELETE)
│   └── 分配权限(SYSTEM:ROLE:ASSIGN)
└── 权限管理(SYSTEM:PERMISSION)
    ├── 权限列表(SYSTEM:PERMISSION:LIST)
    ├── 创建权限(SYSTEM:PERMISSION:CREATE)
    ├── 更新权限(SYSTEM:PERMISSION:UPDATE)
    └── 删除权限(SYSTEM:PERMISSION:DELETE)

业务管理(BUSINESS)
├── 业务查看(BUSINESS:VIEW)
└── 业务操作(BUSINESS:OPERATE)
```

### 预置测试用户
| 用户名 | 密码 | 角色 | 权限范围 | 登录方式 |
|--------|------|------|----------|----------|
| admin | admin123 | ADMIN | 全部权限 | 用户名密码/钉钉 |
| test | test123 | 测试用户 | 基础权限 | 用户名密码/钉钉 |
| user1 | 123456 | USER_MANAGER | 用户管理权限 | 用户名密码 |
| user2 | 123456 | OPERATOR | 业务操作权限 | 用户名密码 |
| user3 | 123456 | GUEST | 查看权限 | 用户名密码 |
| user4 | 123456 | GUEST | 查看权限 | 用户名密码 |

**注意：** 
- `admin`和`test`用户使用BCrypt加密密码，支持安全登录
- 其他用户仍使用明文密码，建议运行升级脚本进行密码加密
- 钉钉登录用户会自动创建，用户名格式为`dingtalk_xxx`

## 日志管理系统

### 系统特性
- **审计日志** - 记录用户操作行为
- **系统日志存储** - 将应用日志保存到数据库
- **登录日志** - 记录用户登录/登出行为
- **日志保留策略** - 自动清理过期日志
- **日志查询** - 提供丰富的查询条件

### 日志类型

#### 1. 审计日志（t_audit_log）
记录用户的所有操作行为，包括：
- 用户信息（用户ID、用户名）
- 操作信息（操作类型、业务类型、模块、描述）
- 请求信息（请求方法、URL、参数、响应）
- 环境信息（IP地址、用户代理、执行时间）
- 状态信息（成功/失败、错误信息）

**操作类型：**
- `CREATE` - 创建操作
- `UPDATE` - 更新操作
- `DELETE` - 删除操作
- `QUERY` - 查询操作
- `LOGIN` - 登录操作
- `LOGOUT` - 登出操作

**业务类型：**
- `USER` - 用户管理
- `ROLE` - 角色管理
- `PERMISSION` - 权限管理
- `SYSTEM` - 系统管理
- `LOG` - 日志管理

#### 2. 系统日志（t_system_log）
记录应用运行时的日志信息，包括：
- 日志级别（DEBUG、INFO、WARN、ERROR）
- 日志消息和异常信息
- 线程信息和调用位置
- 链路追踪ID
- 日志记录器名称

#### 3. 登录日志（t_login_log）
记录用户登录/登出行为，包括：
- 用户信息（用户ID、用户名）
- 登录信息（登录类型、IP地址、地点）
- 环境信息（浏览器、操作系统、用户代理）
- 状态信息（成功/失败、提示消息）

### 使用方式

#### 1. 审计日志注解
使用 `@AuditLog` 注解自动记录操作日志：

```java
@PostMapping
@AuditLog(operationType = LogConstants.OperationType.CREATE, 
          businessType = LogConstants.BusinessType.USER, 
          module = "用户管理", 
          description = "创建用户")
public Result<UserVO> createUser(@Valid @RequestBody UserCreateDTO createDTO) {
    // 业务逻辑
}
```

#### 2. 手动记录日志
通过 LogService 手动记录日志：

```java
// 记录操作日志
logService.recordOperationLog(userId, username, operationType, businessType,
                              module, description, request, startTime, success, errorMessage);

// 记录登录日志
logService.recordLoginLog(userId, username, loginType, request, success, message);
```

#### 3. 查询日志
提供完整的REST API进行日志查询：

```bash
# 分页查询审计日志
POST /api/log/audit/page

# 分页查询系统日志
POST /api/log/system/page

# 分页查询登录日志
POST /api/log/login/page

# 获取日志统计信息
GET /api/log/statistics?days=7

# 清理过期日志
DELETE /api/log/clean?retentionDays=90
```

### 日志配置

#### 1. 自动清理配置
```yaml
biou:
  log:
    # 日志保留天数，默认90天
    retention-days: 90
    # 自动清理配置
    auto-cleanup:
      # 是否启用自动清理，默认启用
      enabled: true
      # 清理任务执行时间，默认每天凌晨2点
      cron: "0 0 2 * * ?"
```

#### 2. Logback配置
系统集成了自定义的DatabaseLogAppender，可以将应用日志自动保存到数据库：

```xml
<!-- 数据库日志存储（仅存储WARN及以上级别的日志） -->
<appender name="DATABASE" class="com.biou.logging.DatabaseLogAppender">
    <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
        <level>WARN</level>
    </filter>
</appender>
```

### 日志权限
日志管理相关权限：
- `SYSTEM:LOG:AUDIT` - 查看审计日志
- `SYSTEM:LOG:SYSTEM` - 查看系统日志
- `SYSTEM:LOG:LOGIN` - 查看登录日志
- `SYSTEM:LOG:CLEAN` - 清理过期日志

### 性能优化
- **异步写入** - 使用AsyncAppender异步写入数据库日志
- **批量处理** - 支持批量保存和删除日志
- **索引优化** - 在关键字段上建立索引
- **分级存储** - 只将WARN及以上级别的日志存储到数据库
- **定时清理** - 自动清理过期日志，避免数据量过大

## Git提交规范

本项目严格遵循Git提交规范，确保代码版本控制的规范性和可维护性。

### 🔧 快速配置（跨平台支持）

**Unix/Linux/MacOS:**
```bash
# 安装Git钩子和配置环境
./scripts/setup-git-hooks.sh
```

**Windows（三种安装方式）:**

方式1：批处理脚本（推荐新手）
```cmd
scripts\setup-git-hooks.bat
```

方式2：PowerShell脚本（推荐高级用户）
```powershell
.\scripts\setup-git-hooks.ps1
```

方式3：Git Bash（推荐开发者）
```bash
bash scripts/setup-git-hooks.sh
```

### 🧪 验证安装

**Unix/Linux/MacOS:**
```bash
# 运行测试验证
./scripts/setup-git-hooks.sh
```

**Windows:**
```cmd
# 运行Windows专用测试
scripts\test-git-hooks-windows.bat
```

### 📝 提交信息格式
采用 **Conventional Commits** 规范：
```
<type>(<scope>): <subject>

<body>

<footer>
```

**示例：**
```bash
feat(user): 添加用户头像上传功能

- 支持JPG、PNG、GIF格式
- 限制文件大小为2MB以内
- 自动压缩和裁剪为标准尺寸

Closes #123
```

### 🌳 分支管理
采用 **Git Flow** 工作流：
- `main` - 生产环境代码
- `develop` - 开发主分支
- `feature/*` - 新功能开发
- `release/*` - 版本发布准备
- `hotfix/*` - 紧急bug修复

### 📋 Git Hooks 自动检查

**代码提交前检查：**
- ✅ 提交信息格式规范（Conventional Commits）
- ✅ 代码质量（禁止Lombok使用）
- ✅ 架构层次（防止跨层调用）
- ✅ 代码格式（行尾空格、Tab字符）
- ✅ 敏感信息检查

**SQL文件专项检查：**
- ✅ SQL语法验证
- ✅ 文件命名规范
- ✅ 头部格式验证
- ✅ 缺失回滚脚本警告

### 🌍 跨平台兼容性

✅ **Windows** (Command Prompt, PowerShell, Git Bash)  
✅ **macOS** (Terminal, Zsh, Bash)  
✅ **Linux** (Bash, Zsh, Fish)  
✅ **IDE集成** (VSCode, IntelliJ, GitHub Desktop等)

### 📚 详细文档
- [Git提交规范文档](docs/GIT_COMMIT_STANDARDS.md) - 完整的Git规范说明
- [SQL开发规范文档](docs/SQL_STANDARDS.md) - SQL文件编写和管理规范
- [Windows环境配置](docs/WINDOWS_SETUP.md) - Windows系统专用配置指南

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   ├── com/biou/project/       # 原有项目包
│   │   │   ├── controller/         # 控制器层
│   │   │   │   ├── UserController.java    # 用户管理控制器
│   │   │   │   └── AuthController.java    # 认证控制器
│   │   │   ├── service/            # 服务层
│   │   │   │   ├── UserService.java       # 用户服务接口
│   │   │   │   ├── AuthService.java       # 认证服务接口
│   │   │   │   └── impl/          # 服务实现层
│   │   │   │       ├── UserServiceImpl.java   # 用户服务实现
│   │   │   │       └── AuthServiceImpl.java   # 认证服务实现
│   │   │   ├── repository/        # 仓储层
│   │   │   │   └── impl/          # 仓储实现层
│   │   │   ├── mapper/            # 映射器层
│   │   │   ├── convert/           # 转换层
│   │   │   ├── entity/            # 实体类
│   │   │   │   ├── User.java      # 用户实体
│   │   │   │   ├── Role.java      # 角色实体
│   │   │   │   ├── Permission.java # 权限实体
│   │   │   │   ├── UserRole.java  # 用户角色关联
│   │   │   │   └── RolePermission.java # 角色权限关联
│   │   │   ├── dto/               # 数据传输对象
│   │   │   │   ├── UserCreateDTO.java
│   │   │   │   ├── RoleCreateDTO.java
│   │   │   │   ├── PermissionCreateDTO.java
│   │   │   │   ├── RolePermissionDTO.java
│   │   │   │   ├── LoginDTO.java          # 登录请求
│   │   │   │   ├── DingTalkLoginDTO.java  # 钉钉登录请求
│   │   │   │   └── TokenRefreshDTO.java   # Token刷新请求
│   │   │   ├── vo/                # 视图对象
│   │   │   │   ├── UserVO.java
│   │   │   │   ├── RoleVO.java
│   │   │   │   ├── PermissionVO.java
│   │   │   │   ├── LoginVO.java           # 登录响应
│   │   │   │   └── DingTalkLoginUrlVO.java # 钉钉登录URL响应
│   │   │   ├── config/            # 配置类
│   │   │   │   ├── MybatisPlusConfig.java
│   │   │   │   ├── RedisConfig.java
│   │   │   │   └── SecurityConfig.java    # Spring Security配置
│   │   │   ├── filter/            # 过滤器
│   │   │   │   └── JwtAuthenticationFilter.java # JWT认证过滤器
│   │   │   ├── exception/         # 异常处理
│   │   │   └── utils/             # 工具类
│   │   │       ├── JwtUtils.java          # JWT工具类
│   │   │       └── DingTalkUtils.java     # 钉钉API工具类
│   │   └── com/biou/              # 日志系统包
│   │       ├── controller/        # 日志控制器
│   │       │   └── LogController.java
│   │       ├── service/           # 日志服务层
│   │       │   ├── LogService.java
│   │       │   └── impl/
│   │       │       └── LogServiceImpl.java
│   │       ├── repository/        # 日志仓储层
│   │       │   ├── AuditLogRepository.java
│   │       │   ├── SystemLogRepository.java
│   │       │   ├── LoginLogRepository.java
│   │       │   └── impl/
│   │       │       ├── AuditLogRepositoryImpl.java
│   │       │       ├── SystemLogRepositoryImpl.java
│   │       │       └── LoginLogRepositoryImpl.java
│   │       ├── mapper/            # 日志映射器
│   │       │   ├── AuditLogMapper.java
│   │       │   ├── SystemLogMapper.java
│   │       │   └── LoginLogMapper.java
│   │       ├── entity/            # 日志实体类
│   │       │   ├── AuditLog.java  # 审计日志
│   │       │   ├── SystemLog.java # 系统日志
│   │       │   └── LoginLog.java  # 登录日志
│   │       ├── dto/               # 日志DTO
│   │       │   ├── AuditLogQueryDTO.java
│   │       │   ├── SystemLogQueryDTO.java
│   │       │   └── LoginLogQueryDTO.java
│   │       ├── vo/                # 日志VO
│   │       │   ├── AuditLogVO.java
│   │       │   ├── SystemLogVO.java
│   │       │   └── LoginLogVO.java
│   │       ├── convert/           # 日志转换类
│   │       │   └── LogConvert.java
│   │       ├── annotation/        # 注解
│   │       │   └── AuditLog.java  # 审计日志注解
│   │       ├── aspect/            # AOP切面
│   │       │   └── AuditLogAspect.java
│   │       ├── logging/           # 日志组件
│   │       │   └── DatabaseLogAppender.java
│   │       ├── task/              # 定时任务
│   │       │   └── LogCleanupTask.java
│   │       ├── constant/          # 常量
│   │       │   └── LogConstants.java
│   │       └── util/              # 工具类
│   │           ├── LogUtils.java
│   │           ├── QueryWrapperUtils.java
│   │           └── SpringContextUtils.java
│   └── resources/
│       ├── mapper/                 # MyBatis XML映射文件
│       │   ├── UserMapper.xml      # 用户映射
│       │   ├── RoleMapper.xml      # 角色映射
│       │   ├── PermissionMapper.xml # 权限映射
│       │   ├── UserRoleMapper.xml  # 用户角色关联映射
│       │   ├── RolePermissionMapper.xml # 角色权限关联映射
│       │   ├── AuditLogMapper.xml  # 审计日志映射
│       │   ├── SystemLogMapper.xml # 系统日志映射
│       │   └── LoginLogMapper.xml  # 登录日志映射
│       ├── application.yml         # 应用配置
│       ├── logback-spring.xml      # 日志配置
│       ├── static/                 # 静态资源
│       └── templates/              # 模板文件
├── test/                           # 测试目录
├── docs/                           # 项目文档
│   ├── GIT_COMMIT_STANDARDS.md    # Git提交规范
│   ├── SQL_STANDARDS.md           # SQL开发规范
│   ├── WINDOWS_SETUP.md           # Windows环境配置指南
│   ├── CODING_STANDARDS.md        # 编写规范文档
│   └── FEATURE_EXTENSION_GUIDE.md # 功能扩展指南
├── scripts/                        # 工具脚本
│   ├── setup-git-hooks.sh         # Git钩子安装脚本(Unix/Linux/Mac)
│   ├── setup-git-hooks.bat        # Git钩子安装脚本(Windows批处理)
│   ├── setup-git-hooks.ps1        # Git钩子安装脚本(Windows PowerShell)
│   └── test-git-hooks-windows.bat # Windows Git钩子测试脚本
├── .gitmessage                     # Git提交信息模板
└── sql/                            # SQL脚本（规范化目录结构）
    ├── README.md                   # SQL目录使用说明
    ├── migrations/                 # 数据库迁移脚本
    │   ├── v1.0.0/                 # 版本1.0.0迁移脚本
    │   │   ├── 001_create_database_and_tables.sql  # 创建数据库和表
    │   │   └── 002_insert_initial_data.sql         # 插入初始数据
    │   └── v1.1.0/                 # 版本1.1.0迁移脚本
    │       └── 001_add_auth_fields.sql             # 添加认证字段
    ├── rollback/                   # 回滚脚本
    │   ├── rollback_v1.0.0_001_create_database_and_tables.sql
    │   ├── rollback_v1.0.0_002_insert_initial_data.sql
    │   └── rollback_v1.1.0_001_add_auth_fields.sql
    ├── patches/                    # 补丁脚本
    ├── procedures/                 # 存储过程
    ├── functions/                  # 函数
    ├── views/                      # 视图
    └── templates/                  # SQL模板文件
        ├── migration_template.sql  # 迁移脚本模板
        └── rollback_template.sql   # 回滚脚本模板
```

## 环境要求

- **JDK 8+**
- **Maven 3.6+**
- **MySQL 8.0+**
- **Redis 6.0+**

## 快速开始

### 1. 克隆项目
```bash
git clone <repository-url>
cd biou_01
```

### 2. 配置数据库
```bash
# 创建数据库并导入SQL脚本（包含RBAC权限数据）
mysql -u root -p < sql/init.sql

# 升级数据库支持认证功能（添加认证相关字段和加密用户）
mysql -u root -p biou_db < sql/auth_upgrade.sql
```

### 3. 修改配置
编辑 `src/main/resources/application.yml`，修改数据库和Redis连接信息：

```yaml
spring:
  datasource:
    druid:
      username: root          # 修改为你的数据库用户名
      password: 123456        # 修改为你的数据库密码
      url: jdbc:mysql://localhost:3306/biou_db # 修改数据库连接地址
  redis:
    host: localhost           # 修改Redis地址
    port: 6379               # 修改Redis端口
    password:                # 修改Redis密码（如果有）

# 配置JWT和钉钉登录（可选）
jwt:
  secret: your-jwt-secret-key    # JWT密钥
  expiration: 86400             # 访问令牌过期时间（秒）
  refresh-expiration: 604800    # 刷新令牌过期时间（秒）

dingtalk:
  app-id: ${DINGTALK_APP_ID}          # 钉钉应用ID
  app-secret: ${DINGTALK_APP_SECRET}  # 钉钉应用密钥
  redirect-uri: ${DINGTALK_REDIRECT_URI}  # 钉钉回调地址
```

### 4. 启动Redis
```bash
redis-server
```

### 5. 启动项目
```bash
mvn spring-boot:run
```

### 6. 测试接口
项目启动后，可以使用以下方式测试接口：
- 使用Postman或curl工具调用API
- 项目基础路径：`http://localhost:8080/api`

## API接口

### 认证授权接口

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| POST | `/api/auth/login` | 用户名密码登录 | 无 |
| GET | `/api/auth/dingtalk/login-url` | 获取钉钉登录URL | 无 |
| POST | `/api/auth/dingtalk/callback` | 钉钉登录回调 | 无 |
| POST | `/api/auth/refresh` | 刷新访问令牌 | 无 |
| POST | `/api/auth/logout` | 用户登出 | JWT |
| GET | `/api/auth/validate` | 验证令牌有效性 | 无 |

### 用户管理接口

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| POST | `/api/user` | 创建用户 | JWT |
| GET | `/api/user/{id}` | 根据ID查询用户 | JWT |
| GET | `/api/user/username/{username}` | 根据用户名查询用户 | JWT |
| GET | `/api/user/page` | 分页查询用户 | JWT |
| GET | `/api/user/enabled` | 查询启用用户 | JWT |
| PUT | `/api/user/{id}/status` | 更新用户状态 | JWT |
| DELETE | `/api/user/{id}` | 删除用户 | JWT |
| GET | `/api/user/statistics` | 用户统计 | JWT |
| GET | `/api/user/check/username` | 检查用户名 | 无 |
| GET | `/api/user/check/email` | 检查邮箱 | 无 |
| GET | `/api/user/check/phone` | 检查手机号 | 无 |

### RBAC权限接口（待实现）

#### 角色管理
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/role` | 创建角色 |
| GET | `/api/role/{id}` | 根据ID查询角色 |
| GET | `/api/role/page` | 分页查询角色 |
| PUT | `/api/role/{id}` | 更新角色 |
| DELETE | `/api/role/{id}` | 删除角色 |
| POST | `/api/role/{roleId}/permissions` | 为角色分配权限 |
| GET | `/api/role/{roleId}/permissions` | 查询角色权限 |

#### 权限管理
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/permission` | 创建权限 |
| GET | `/api/permission/{id}` | 根据ID查询权限 |
| GET | `/api/permission/tree` | 查询权限树 |
| GET | `/api/permission/list` | 查询权限列表 |
| PUT | `/api/permission/{id}` | 更新权限 |
| DELETE | `/api/permission/{id}` | 删除权限 |

#### 用户权限
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/user/{userId}/roles` | 为用户分配角色 |
| GET | `/api/user/{userId}/roles` | 查询用户角色 |
| GET | `/api/user/{userId}/permissions` | 查询用户权限 |

### 示例请求

#### 认证登录
```bash
# 用户名密码登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'

# 获取钉钉登录URL
curl http://localhost:8080/api/auth/dingtalk/login-url

# 刷新访问令牌
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "your-refresh-token"
  }'

# 用户登出
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer your-access-token"
```

#### 创建用户
```bash
curl -X POST http://localhost:8080/api/user \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your-access-token" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com",
    "phone": "13800138888"
  }'
```

#### 查询用户
```bash
curl http://localhost:8080/api/user/1 \
  -H "Authorization: Bearer your-access-token"
```

#### 分页查询
```bash
curl "http://localhost:8080/api/user/page?current=1&size=10" \
  -H "Authorization: Bearer your-access-token"
```

#### 创建角色
```bash
curl -X POST http://localhost:8080/api/role \
  -H "Content-Type: application/json" \
  -d '{
    "roleCode": "DEVELOPER",
    "roleName": "开发人员",
    "description": "系统开发人员角色"
  }'
```

#### 创建权限
```bash
curl -X POST http://localhost:8080/api/permission \
  -H "Content-Type: application/json" \
  -d '{
    "permissionCode": "SYSTEM:LOG:VIEW",
    "permissionName": "查看日志",
    "resourceType": "api",
    "resourcePath": "/api/log/*",
    "parentId": 1,
    "sortOrder": 100,
    "description": "查看系统日志权限"
  }'
```

## 数据库设计

### RBAC权限表结构

#### 用户表 (t_user)
```sql
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码（BCrypt加密）',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `real_name` varchar(100) DEFAULT NULL COMMENT '真实姓名',
  `dingtalk_user_id` varchar(100) DEFAULT NULL COMMENT '钉钉用户ID',
  `dingtalk_union_id` varchar(100) DEFAULT NULL COMMENT '钉钉用户唯一标识',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_dingtalk_union_id` (`dingtalk_union_id`),
  KEY `idx_dingtalk_user_id` (`dingtalk_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

#### 角色表 (t_role)
```sql
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_code` varchar(50) NOT NULL COMMENT '角色编码',
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `description` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';
```

#### 权限表 (t_permission)
```sql
CREATE TABLE `t_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `permission_code` varchar(100) NOT NULL COMMENT '权限编码',
  `permission_name` varchar(100) NOT NULL COMMENT '权限名称',
  `resource_type` varchar(20) NOT NULL COMMENT '资源类型：menu-菜单，button-按钮，api-接口',
  `resource_path` varchar(200) DEFAULT NULL COMMENT '资源路径',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父权限ID，0表示顶级权限',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序号',
  `description` varchar(200) DEFAULT NULL COMMENT '权限描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';
```

## 🔧 系统监控

### Druid数据库监控
- **访问地址**: `http://localhost:8080/api/druid`
- **用户名**: admin
- **密码**: 123456
- **监控内容**: 数据库连接池状态、SQL执行统计、慢SQL分析

### 系统日志监控
- **审计日志**: 记录所有用户操作行为
- **系统日志**: 记录应用运行状态和错误信息
- **登录日志**: 记录用户登录/登出情况
- **日志统计**: 提供丰富的日志分析功能

## 📚 相关文档

本项目包含详细的开发规范和扩展指南：

- **[Git提交规范](docs/GIT_COMMIT_STANDARDS.md)** - Git使用规范、提交信息格式、分支管理
- **[SQL开发规范](docs/SQL_STANDARDS.md)** - SQL文件编写、管理规范、数据库迁移最佳实践
- **[Windows环境配置](docs/WINDOWS_SETUP.md)** - Windows系统专用配置指南、故障排除
- **[项目编写规范](docs/CODING_STANDARDS.md)** - 代码规范、架构规范、命名规范
- **[新功能拓展规范](docs/FEATURE_EXTENSION_GUIDE.md)** - 功能扩展流程、最佳实践

### 🚀 快速上手指南

**新开发者推荐阅读顺序：**

1. **[Windows环境配置](docs/WINDOWS_SETUP.md)** - Windows用户必读，包含完整的环境配置指南
2. **[Git提交规范](docs/GIT_COMMIT_STANDARDS.md)** - 了解项目的Git工作流和提交规范
3. **[SQL开发规范](docs/SQL_STANDARDS.md)** - 数据库开发和SQL脚本管理规范
4. **[项目编写规范](docs/CODING_STANDARDS.md)** - 代码编写规范和架构原则
5. **[新功能拓展规范](docs/FEATURE_EXTENSION_GUIDE.md)** - 功能开发和扩展指南

### 🔧 开发工具配置

**Git Hooks自动化:**
- 自动检查代码质量和提交信息格式
- 自动验证SQL文件语法和规范
- 支持所有主流开发环境和IDE

**跨平台兼容:**
- Windows: `scripts\setup-git-hooks.bat` 或 `scripts\setup-git-hooks.ps1`
- macOS/Linux: `./scripts/setup-git-hooks.sh`

## 🏗️ 架构优势

### 1. 分层清晰
- 严格的四层架构，职责分明
- 禁止跨层调用，保证代码整洁
- 通过DTO传递数据，隐藏ORM实现

### 2. 扩展性强
- Repository层使用收敛设计，易于扩展
- 基于RBAC的权限控制，灵活配置
- 完整的日志体系，便于监控和审计

### 3. 维护性好
- 零Lombok依赖，代码直观易读
- 统一的异常处理和响应格式
- 完善的注释和文档

### 4. 生产就绪
- 企业级的权限控制体系
- 完整的操作审计功能
- 规范的错误处理机制

## 🤝 参与贡献

欢迎提交 Issue 和 Pull Request 来帮助改进项目。

### 贡献流程
1. **Fork** 本项目
2. **创建特性分支** (`git checkout -b feature/AmazingFeature`)
3. **提交更改** (`git commit -m 'Add some AmazingFeature'`)
4. **推送到分支** (`git push origin feature/AmazingFeature`)
5. **提交 Pull Request**

### 贡献指南
- 遵循项目的编码规范
- 添加必要的测试用例
- 更新相关文档
- 确保通过所有测试

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 技术支持

如有问题，请通过以下方式联系：

- 🐛 **Bug报告**: [GitHub Issues](https://github.com/your-repo/issues)
- 💡 **功能建议**: [GitHub Discussions](https://github.com/your-repo/discussions)
- 📧 **邮件咨询**: zhurunfeng3226@gmail.com

---

⭐ 如果这个项目对你有帮助，请给它一个星标！ 
## 许可证
MIT License