# Biou Project

基于Spring Boot + MySQL + Redis + MyBatis-Plus的四层架构项目，集成RBAC权限控制系统

## 项目架构

### 技术栈
- **Spring Boot 2.7.18** - 主框架
- **MySQL 8.0.33** - 数据库
- **Redis** - 缓存
- **MyBatis-Plus 3.5.3.1** - ORM框架
- **Druid 1.2.20** - 数据库连接池
- **FastJSON2 2.0.43** - JSON处理

### 四层架构
```
├── Controller层 - 控制器层，处理HTTP请求
├── Service层 - 业务逻辑层，处理业务逻辑
├── Repository层 - 数据访问层，继承MyBatis-Plus的IService，提供收敛的通用方法
└── Mapper层 - 数据映射层，继承MyBatis-Plus的BaseMapper
```

### 转换层
- **Convert层** - 实体转换层，负责DTO、Entity、VO之间的转换

### Repository层设计原则
Repository层采用收敛设计，提供通用的数据访问方法，不向上层暴露ORM框架的具体实现：

**MyBatis-Plus IService 提供的基础方法：**
- `getById()` - 根据ID查询
- `save()` / `saveBatch()` - 保存
- `updateById()` - 根据ID更新
- `removeById()` - 根据ID删除

**自定义扩展方法（使用DTO封装查询条件）：**
- `findOne(QueryDTO)` - 根据条件查询单个实体
- `list(QueryDTO)` - 根据条件查询列表
- `page(PageQueryDTO, QueryDTO)` - 根据条件分页查询
- `count(QueryDTO)` - 根据条件统计数量
- `update(Entity, QueryDTO)` - 根据条件更新
- `remove(QueryDTO)` - 根据条件删除

**设计优势：**
- **解耦性**：上层不依赖MyBatis-Plus的Wrapper
- **抽象性**：通过DTO定义查询条件更清晰
- **可替换性**：如果将来换ORM框架，上层代码不需要改动
- **封装性**：Wrapper的组装逻辑封装在Repository层内部

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
| 用户名 | 密码 | 角色 | 权限范围 |
|--------|------|------|----------|
| admin | 123456 | ADMIN | 全部权限 |
| user1 | 123456 | USER_MANAGER | 用户管理权限 |
| user2 | 123456 | OPERATOR | 业务操作权限 |
| user3 | 123456 | GUEST | 查看权限 |
| user4 | 123456 | GUEST | 查看权限 |

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/biou/project/
│   │       ├── controller/          # 控制器层
│   │       ├── service/             # 服务层
│   │       │   └── impl/           # 服务实现层
│   │       ├── repository/         # 仓储层
│   │       │   └── impl/           # 仓储实现层
│   │       ├── mapper/             # 映射器层
│   │       ├── convert/            # 转换层
│   │       ├── entity/             # 实体类
│   │       │   ├── User.java       # 用户实体
│   │       │   ├── Role.java       # 角色实体
│   │       │   ├── Permission.java # 权限实体
│   │       │   ├── UserRole.java   # 用户角色关联
│   │       │   └── RolePermission.java # 角色权限关联
│   │       ├── dto/                # 数据传输对象
│   │       │   ├── UserCreateDTO.java
│   │       │   ├── RoleCreateDTO.java
│   │       │   ├── PermissionCreateDTO.java
│   │       │   └── RolePermissionDTO.java
│   │       ├── vo/                 # 视图对象
│   │       │   ├── UserVO.java
│   │       │   ├── RoleVO.java
│   │       │   └── PermissionVO.java
│   │       ├── config/             # 配置类
│   │       ├── exception/          # 异常处理
│   │       └── utils/              # 工具类
│   └── resources/
│       ├── mapper/                 # MyBatis XML映射文件
│       │   ├── UserMapper.xml
│       │   ├── RoleMapper.xml
│       │   ├── PermissionMapper.xml
│       │   ├── UserRoleMapper.xml
│       │   └── RolePermissionMapper.xml
│       ├── application.yml         # 应用配置
│       ├── static/                 # 静态资源
│       └── templates/              # 模板文件
├── test/                           # 测试目录
└── sql/                            # SQL脚本
    └── init.sql                   # 数据库初始化脚本（包含RBAC表和数据）
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

### 用户管理接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/user` | 创建用户 |
| GET | `/api/user/{id}` | 根据ID查询用户 |
| GET | `/api/user/username/{username}` | 根据用户名查询用户 |
| GET | `/api/user/page` | 分页查询用户 |
| GET | `/api/user/enabled` | 查询启用用户 |
| PUT | `/api/user/{id}/status` | 更新用户状态 |
| DELETE | `/api/user/{id}` | 删除用户 |
| GET | `/api/user/statistics` | 用户统计 |
| GET | `/api/user/check/username` | 检查用户名 |
| GET | `/api/user/check/email` | 检查邮箱 |
| GET | `/api/user/check/phone` | 检查手机号 |

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

#### 创建用户
```bash
curl -X POST http://localhost:8080/api/user \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com",
    "phone": "13800138888"
  }'
```

#### 查询用户
```bash
curl http://localhost:8080/api/user/1
```

#### 分页查询
```bash
curl "http://localhost:8080/api/user/page?current=1&size=10"
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
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`)
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

## 监控

### Druid监控
访问 `http://localhost:8080/api/druid` 查看数据库连接池监控
- 用户名：admin
- 密码：123456

## 开发规范

### 1. 代码规范
- 禁止使用Lombok
- 所有类必须有完整的getter/setter方法
- 必须有构造函数
- 必须有toString方法

### 2. 分层规范
- **Controller层**：只负责接收请求参数，调用Service层，返回响应结果
- **Service层**：处理业务逻辑，事务控制，组装查询条件
- **Repository层**：继承MyBatis-Plus的IService，只提供收敛的通用数据访问方法
- **Mapper层**：继承MyBatis-Plus的BaseMapper，提供基础的CRUD操作

### 3. 命名规范
- 类名使用大驼峰命名法
- 方法名和变量名使用小驼峰命名法
- 常量使用全大写加下划线
- 包名使用小写

### 4. 异常处理
- 使用全局异常处理器统一处理异常
- 业务异常使用BusinessException
- 返回统一格式的Result

### 5. RBAC权限规范
- 权限编码采用层级结构，使用冒号分隔，如：`SYSTEM:USER:CREATE`
- 角色编码采用全大写，如：`ADMIN`、`USER_MANAGER`
- 权限类型标准化：`menu`、`button`、`api`
- 权限检查应在Controller层或Service层进行

## 扩展开发

### 添加新权限
1. 在数据库中插入新权限记录
2. 为相应角色分配权限
3. 在代码中添加权限检查逻辑

### 添加新角色
1. 在数据库中插入新角色记录
2. 为角色分配相应权限
3. 为用户分配新角色

### 自定义权限检查
可以通过扩展Permission相关的Service层方法来实现复杂的权限检查逻辑。

## 贡献指南

1. Fork项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建Pull Request

## 许可证

MIT License 