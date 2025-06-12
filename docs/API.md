# API文档

## 1. 用户管理接口

### 1.1 创建用户
```http
POST /api/user
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com",
  "phone": "13800138000"
}
```

### 1.2 查询用户
```http
GET /api/user/{id}
```

### 1.3 分页查询用户
```http
GET /api/user/page?current=1&size=10
```

### 1.4 更新用户状态
```http
PUT /api/user/{id}/status
Content-Type: application/json

{
  "status": 1
}
```

## 2. 角色管理接口

### 2.1 创建角色
```http
POST /api/role
Content-Type: application/json

{
  "roleCode": "ADMIN",
  "roleName": "管理员",
  "description": "系统管理员"
}
```

### 2.2 查询角色
```http
GET /api/role/{id}
```

### 2.3 分页查询角色
```http
GET /api/role/page?current=1&size=10
```

### 2.4 分配角色权限
```http
POST /api/role/{roleId}/permissions
Content-Type: application/json

{
  "permissionIds": [1, 2, 3]
}
```

## 3. 权限管理接口

### 3.1 创建权限
```http
POST /api/permission
Content-Type: application/json

{
  "permissionCode": "SYSTEM:USER:CREATE",
  "permissionName": "创建用户",
  "resourceType": "api",
  "resourcePath": "/api/user",
  "parentId": 0
}
```

### 3.2 查询权限树
```http
GET /api/permission/tree
```

### 3.3 查询权限列表
```http
GET /api/permission/list
```

## 4. 日志管理接口

### 4.1 查询审计日志
```http
POST /api/log/audit/page
Content-Type: application/json

{
  "current": 1,
  "size": 10,
  "startTime": "2024-01-01 00:00:00",
  "endTime": "2024-12-31 23:59:59",
  "username": "admin",
  "operationType": "CREATE"
}
```

### 4.2 查询系统日志
```http
POST /api/log/system/page
Content-Type: application/json

{
  "current": 1,
  "size": 10,
  "level": "ERROR",
  "startTime": "2024-01-01 00:00:00",
  "endTime": "2024-12-31 23:59:59"
}
```

### 4.3 查询登录日志
```http
POST /api/log/login/page
Content-Type: application/json

{
  "current": 1,
  "size": 10,
  "username": "admin",
  "status": 1
}
```

## 5. 统计分析接口

### 5.1 用户统计
```http
GET /api/user/statistics
```

### 5.2 日志统计
```http
GET /api/log/statistics?days=7
```

## 6. 系统监控接口

### 6.1 服务器监控
```http
GET /api/monitor/server
```

### 6.2 数据库监控
```http
GET /api/monitor/database
```

### 6.3 缓存监控
```http
GET /api/monitor/cache
```

## 7. 通用响应格式

### 7.1 成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // 响应数据
  }
}
```

### 7.2 失败响应
```json
{
  "code": 500,
  "message": "操作失败",
  "data": null
}
```

### 7.3 分页响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

## 8. 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未认证 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 9. 接口认证

所有接口（除登录接口外）都需要在请求头中携带Token：

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 10. 接口限流

- 普通接口：10次/秒
- 登录接口：2次/分钟
- 验证码接口：1次/分钟 