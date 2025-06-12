# 项目编写规范

本文档规定了Biou项目的编写规范，所有开发人员必须严格遵循这些规范，以确保代码质量和项目的可维护性。

## 📋 目录

- [1. 总体架构规范](#1-总体架构规范)
- [2. 代码编写规范](#2-代码编写规范)
- [3. 分层架构规范](#3-分层架构规范)
- [4. 数据传输规范](#4-数据传输规范)
- [5. 异常处理规范](#5-异常处理规范)
- [6. 数据库设计规范](#6-数据库设计规范)
- [7. RBAC权限规范](#7-rbac权限规范)
- [8. 日志记录规范](#8-日志记录规范)
- [9. 测试规范](#9-测试规范)
- [10. 文档规范](#10-文档规范)

## 1. 总体架构规范

### 1.1 四层架构原则

**🚫 严格禁止跨层调用**

```
✅ 正确的调用链：
Controller → Service → Repository → Mapper

❌ 禁止的调用：
Controller → Repository  (跨过Service层)
Controller → Mapper     (跨过Service和Repository层)
Service → Mapper        (跨过Repository层)
```

### 1.2 层级职责定义

| 层级 | 职责 | 限制 |
|------|------|------|
| **Controller** | HTTP请求处理、参数校验、响应封装 | 不得包含业务逻辑 |
| **Service** | 业务逻辑处理、事务控制、缓存管理 | 不得直接操作数据库 |
| **Repository** | 数据访问封装、查询条件组装 | 不得包含业务逻辑 |
| **Mapper** | 数据库操作、SQL映射 | 仅提供基础CRUD |

### 1.3 依赖关系

```java
// ✅ 正确的依赖注入
@RestController
public class UserController {
    @Autowired
    private UserService userService; // 只依赖Service层
}

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository; // 只依赖Repository层
}

@Repository
public class UserRepositoryImpl extends ServiceImpl<UserMapper, User> 
    implements UserRepository {
    // 继承ServiceImpl，自动获得Mapper依赖
}
```

## 2. 代码编写规范

### 2.1 Lombok使用限制

**🚫 严格禁止使用Lombok**

```java
// ❌ 禁止使用Lombok注解
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String username;
}

// ✅ 手动编写所有方法
public class User {
    private Long id;
    private String username;
    
    // 无参构造函数
    public User() {
    }
    
    // 全参构造函数
    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    // toString方法
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
```

### 2.2 命名规范

#### 2.2.1 类命名
```java
// ✅ 正确命名
public class UserController {}        // Controller层
public class UserService {}           // Service接口
public class UserServiceImpl {}       // Service实现
public class UserRepository {}        // Repository接口
public class UserRepositoryImpl {}    // Repository实现
public class UserMapper {}            // Mapper接口
public class UserCreateDTO {}         // 创建DTO
public class UserQueryDTO {}          // 查询DTO
public class UserVO {}                // 视图对象
```

#### 2.2.2 方法命名
```java
// ✅ Controller层方法命名
public Result<UserVO> createUser(@RequestBody UserCreateDTO createDTO) {}
public Result<UserVO> getUserById(@PathVariable Long id) {}
public Result<Page<UserVO>> pageUsers(@RequestBody UserQueryDTO queryDTO) {}
public Result<Void> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO updateDTO) {}
public Result<Void> deleteUser(@PathVariable Long id) {}

// ✅ Service层方法命名
public UserVO createUser(UserCreateDTO createDTO) {}
public UserVO getUserById(Long id) {}
public Page<UserVO> pageUsers(UserQueryDTO queryDTO) {}
public void updateUser(Long id, UserUpdateDTO updateDTO) {}
public void deleteUser(Long id) {}

// ✅ Repository层方法命名
public User findOne(UserQueryDTO queryDTO) {}
public List<User> list(UserQueryDTO queryDTO) {}
public Page<User> page(Page<User> page, UserQueryDTO queryDTO) {}
public Long count(UserQueryDTO queryDTO) {}
public void update(User entity, UserQueryDTO queryDTO) {}
public void remove(UserQueryDTO queryDTO) {}
```

#### 2.2.3 变量命名
```java
// ✅ 变量命名规范
private UserService userService;           // 服务注入
private static final String DEFAULT_STATUS = "ACTIVE";  // 常量
private List<UserVO> userList;             // 集合变量
private UserQueryDTO queryCondition;       // DTO变量
private Page<User> userPage;               // 分页变量
```

### 2.3 注释规范

#### 2.3.1 类注释
```java
/**
 * 用户管理Controller
 * 
 * 提供用户相关的CRUD接口：
 * - 创建用户
 * - 查询用户
 * - 更新用户
 * - 删除用户
 *
 * @author Jax
 * @since 2025-01-07
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
}
```

#### 2.3.2 方法注释
```java
/**
 * 创建用户
 *
 * @param createDTO 用户创建请求参数
 * @return 创建成功的用户信息
 * @throws BusinessException 当用户名已存在时抛出
 */
@PostMapping
public Result<UserVO> createUser(@Valid @RequestBody UserCreateDTO createDTO) {
    // 方法实现
}
```

## 3. 分层架构规范

### 3.1 Controller层规范

```java
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * ✅ 标准Controller方法结构
     */
    @PostMapping
    @AuditLog(operationType = CREATE, businessType = USER, 
              module = "用户管理", description = "创建用户")
    public Result<UserVO> createUser(@Valid @RequestBody UserCreateDTO createDTO) {
        try {
            // 1. 参数校验（通过@Valid自动完成）
            // 2. 调用Service层
            UserVO userVO = userService.createUser(createDTO);
            // 3. 返回统一格式
            return Result.success(userVO);
        } catch (BusinessException e) {
            // 4. 业务异常由全局异常处理器处理
            throw e;
        }
    }
    
    /**
     * ❌ 错误示例：包含业务逻辑
     */
    @PostMapping("/wrong")
    public Result<UserVO> createUserWrong(@RequestBody UserCreateDTO createDTO) {
        // ❌ Controller层不应该包含业务逻辑
        if (createDTO.getUsername() == null) {
            return Result.error("用户名不能为空");
        }
        // ❌ Controller层不应该直接操作缓存
        redisTemplate.delete("user:list");
        // ❌ Controller层不应该有复杂的业务处理
        User user = new User();
        user.setUsername(createDTO.getUsername());
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        
        return Result.success(null);
    }
}
```

### 3.2 Service层规范

```java
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RedisUtils redisUtils;
    
    /**
     * ✅ 标准Service方法结构
     */
    @Override
    public UserVO createUser(UserCreateDTO createDTO) {
        // 1. 业务校验
        validateUser(createDTO);
        
        // 2. 业务逻辑处理
        User user = UserConvert.toEntity(createDTO);
        user.setStatus(UserConstants.Status.ACTIVE);
        user.setCreateTime(LocalDateTime.now());
        
        // 3. 调用Repository层
        userRepository.save(user);
        
        // 4. 缓存处理
        redisUtils.delete(RedisKeyConstants.USER_LIST);
        redisUtils.set(RedisKeyConstants.USER_PREFIX + user.getId(), user, 3600);
        
        // 5. 返回结果转换
        return UserConvert.toVO(user);
    }
    
    /**
     * 业务校验方法
     */
    private void validateUser(UserCreateDTO createDTO) {
        // 检查用户名是否已存在
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setUsername(createDTO.getUsername());
        User existUser = userRepository.findOne(queryDTO);
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        queryDTO = new UserQueryDTO();
        queryDTO.setEmail(createDTO.getEmail());
        existUser = userRepository.findOne(queryDTO);
        if (existUser != null) {
            throw new BusinessException("邮箱已存在");
        }
    }
}
```

### 3.3 Repository层规范

```java
/**
 * ✅ 标准Repository接口
 */
public interface UserRepository extends IService<User> {
    
    /**
     * 根据条件查询单个用户
     */
    User findOne(UserQueryDTO queryDTO);
    
    /**
     * 根据条件查询用户列表
     */
    List<User> list(UserQueryDTO queryDTO);
    
    /**
     * 根据条件分页查询用户
     */
    Page<User> page(Page<User> page, UserQueryDTO queryDTO);
    
    /**
     * 根据条件统计用户数量
     */
    Long count(UserQueryDTO queryDTO);
    
    /**
     * 根据条件更新用户
     */
    void update(User entity, UserQueryDTO queryDTO);
    
    /**
     * 根据条件删除用户
     */
    void remove(UserQueryDTO queryDTO);
}

/**
 * ✅ 标准Repository实现
 */
@Repository
public class UserRepositoryImpl extends ServiceImpl<UserMapper, User> 
    implements UserRepository {
    
    @Override
    public User findOne(UserQueryDTO queryDTO) {
        LambdaQueryWrapper<User> wrapper = QueryWrapperUtils.buildUserQueryWrapper(queryDTO);
        return baseMapper.selectOne(wrapper);
    }
    
    @Override
    public List<User> list(UserQueryDTO queryDTO) {
        LambdaQueryWrapper<User> wrapper = QueryWrapperUtils.buildUserQueryWrapper(queryDTO);
        return baseMapper.selectList(wrapper);
    }
    
    @Override
    public Page<User> page(Page<User> page, UserQueryDTO queryDTO) {
        LambdaQueryWrapper<User> wrapper = QueryWrapperUtils.buildUserQueryWrapper(queryDTO);
        return baseMapper.selectPage(page, wrapper);
    }
    
    // 其他方法实现...
}
```

## 4. 数据传输规范

### 4.1 DTO设计规范

#### 4.1.1 CreateDTO (创建请求)
```java
/**
 * 用户创建DTO
 */
public class UserCreateDTO {
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;
    
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    // 构造函数、getter、setter、toString...
}
```

#### 4.1.2 QueryDTO (查询条件)
```java
/**
 * 用户查询DTO
 */
public class UserQueryDTO extends PageQueryDTO {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名（模糊查询）
     */
    private String username;
    
    /**
     * 邮箱（精确查询）
     */
    private String email;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 创建时间开始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeStart;
    
    /**
     * 创建时间结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;
    
    /**
     * 排序字段
     */
    private String sortField;
    
    /**
     * 排序方向
     */
    private String sortOrder;
    
    // 构造函数、getter、setter、toString...
}
```

#### 4.1.3 UpdateDTO (更新请求)
```java
/**
 * 用户更新DTO
 */
public class UserUpdateDTO {
    
    @Size(max = 100, message = "昵称长度不能超过100")
    private String nickname;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    
    // 构造函数、getter、setter、toString...
}
```

#### 4.1.4 VO (响应对象)
```java
/**
 * 用户视图对象
 */
public class UserVO {
    
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private Integer status;
    private String statusName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 用户角色列表
     */
    private List<RoleVO> roles;
    
    /**
     * 用户权限列表
     */
    private List<PermissionVO> permissions;
    
    // 构造函数、getter、setter、toString...
}
```

### 4.2 转换规范

```java
/**
 * 用户转换工具类
 */
public class UserConvert {
    
    /**
     * CreateDTO转Entity
     */
    public static User toEntity(UserCreateDTO createDTO) {
        if (createDTO == null) {
            return null;
        }
        
        User user = new User();
        user.setUsername(createDTO.getUsername());
        user.setPassword(createDTO.getPassword());
        user.setEmail(createDTO.getEmail());
        user.setPhone(createDTO.getPhone());
        return user;
    }
    
    /**
     * Entity转VO
     */
    public static UserVO toVO(User user) {
        if (user == null) {
            return null;
        }
        
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setEmail(user.getEmail());
        userVO.setPhone(user.getPhone());
        userVO.setStatus(user.getStatus());
        userVO.setStatusName(UserConstants.getStatusName(user.getStatus()));
        userVO.setCreateTime(user.getCreateTime());
        userVO.setUpdateTime(user.getUpdateTime());
        return userVO;
    }
    
    /**
     * Entity列表转VO列表
     */
    public static List<UserVO> toVOList(List<User> users) {
        if (users == null || users.isEmpty()) {
            return new ArrayList<>();
        }
        
        return users.stream()
                .map(UserConvert::toVO)
                .collect(Collectors.toList());
    }
}
```

## 5. 异常处理规范

### 5.1 异常定义

```java
/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {
    
    private Integer code;
    private String message;
    
    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR;
        this.message = message;
    }
    
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    // getter、setter方法...
}
```

### 5.2 全局异常处理

```java
/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 参数校验异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMsg = new StringBuilder();
        
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errorMsg.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        });
        
        log.warn("参数校验失败：{}", errorMsg.toString());
        return Result.error(ResultCode.PARAM_ERROR, errorMsg.toString());
    }
    
    /**
     * 系统异常处理
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error(ResultCode.SYSTEM_ERROR, "系统异常，请联系管理员");
    }
}
```

## 6. 数据库设计规范

### 6.1 表命名规范

```sql
-- ✅ 正确的表命名
CREATE TABLE `t_user` (...);          -- 用户表
CREATE TABLE `t_role` (...);          -- 角色表
CREATE TABLE `t_permission` (...);    -- 权限表
CREATE TABLE `t_user_role` (...);     -- 用户角色关联表
CREATE TABLE `t_audit_log` (...);     -- 审计日志表

-- ❌ 错误的表命名
CREATE TABLE `user` (...);            -- 缺少前缀
CREATE TABLE `users` (...);           -- 使用复数
CREATE TABLE `UserInfo` (...);        -- 使用驼峰命名
```

### 6.2 字段规范

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
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

**字段规范要求：**
- 所有字段必须有注释
- 主键使用`id`，类型为`bigint(20) AUTO_INCREMENT`
- 状态字段使用`tinyint(1)`
- 时间字段使用`datetime`类型
- 必须有`create_time`和`update_time`字段
- 必须有`deleted`字段实现逻辑删除
- 字符集使用`utf8mb4`

## 7. RBAC权限规范

### 7.1 权限编码规范

```java
/**
 * 权限编码采用层级结构，使用冒号分隔
 */
public class PermissionConstants {
    
    // 系统管理权限
    public static final String SYSTEM = "SYSTEM";
    public static final String SYSTEM_USER = "SYSTEM:USER";
    public static final String SYSTEM_USER_LIST = "SYSTEM:USER:LIST";
    public static final String SYSTEM_USER_CREATE = "SYSTEM:USER:CREATE";
    public static final String SYSTEM_USER_UPDATE = "SYSTEM:USER:UPDATE";
    public static final String SYSTEM_USER_DELETE = "SYSTEM:USER:DELETE";
    
    // 角色管理权限
    public static final String SYSTEM_ROLE = "SYSTEM:ROLE";
    public static final String SYSTEM_ROLE_LIST = "SYSTEM:ROLE:LIST";
    public static final String SYSTEM_ROLE_CREATE = "SYSTEM:ROLE:CREATE";
    public static final String SYSTEM_ROLE_UPDATE = "SYSTEM:ROLE:UPDATE";
    public static final String SYSTEM_ROLE_DELETE = "SYSTEM:ROLE:DELETE";
    public static final String SYSTEM_ROLE_ASSIGN = "SYSTEM:ROLE:ASSIGN";
    
    // 业务权限
    public static final String BUSINESS = "BUSINESS";
    public static final String BUSINESS_VIEW = "BUSINESS:VIEW";
    public static final String BUSINESS_OPERATE = "BUSINESS:OPERATE";
}
```

### 7.2 角色编码规范

```java
/**
 * 角色编码使用全大写，用下划线分隔
 */
public class RoleConstants {
    
    public static final String ADMIN = "ADMIN";                // 系统管理员
    public static final String USER_MANAGER = "USER_MANAGER";  // 用户管理员
    public static final String OPERATOR = "OPERATOR";          // 操作员
    public static final String GUEST = "GUEST";                // 访客
}
```

### 7.3 权限检查规范

```java
/**
 * 权限检查注解使用示例
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @PostMapping
    @PreAuthorize("hasAuthority('SYSTEM:USER:CREATE')")
    public Result<UserVO> createUser(@RequestBody UserCreateDTO createDTO) {
        // 创建用户逻辑
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SYSTEM:USER:DELETE')")
    public Result<Void> deleteUser(@PathVariable Long id) {
        // 删除用户逻辑
    }
}
```

## 8. 日志记录规范

### 8.1 审计日志注解

```java
/**
 * 审计日志注解使用规范
 */
@PostMapping
@AuditLog(
    operationType = LogConstants.OperationType.CREATE,  // 操作类型
    businessType = LogConstants.BusinessType.USER,      // 业务类型
    module = "用户管理",                                   // 模块名称
    description = "创建用户"                              // 操作描述
)
public Result<UserVO> createUser(@RequestBody UserCreateDTO createDTO) {
    // 方法实现
}
```

### 8.2 操作类型规范

```java
public class LogConstants {
    
    /**
     * 操作类型
     */
    public static class OperationType {
        public static final String CREATE = "CREATE";   // 创建
        public static final String UPDATE = "UPDATE";   // 更新
        public static final String DELETE = "DELETE";   // 删除
        public static final String QUERY = "QUERY";     // 查询
        public static final String LOGIN = "LOGIN";     // 登录
        public static final String LOGOUT = "LOGOUT";   // 登出
    }
    
    /**
     * 业务类型
     */
    public static class BusinessType {
        public static final String USER = "USER";           // 用户管理
        public static final String ROLE = "ROLE";           // 角色管理
        public static final String PERMISSION = "PERMISSION"; // 权限管理
        public static final String SYSTEM = "SYSTEM";       // 系统管理
        public static final String LOG = "LOG";             // 日志管理
    }
}
```

## 9. 测试规范

### 9.1 单元测试规范

```java
/**
 * Service层单元测试示例
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @MockBean
    private UserRepository userRepository;
    
    @Test
    @DisplayName("创建用户成功")
    void testCreateUserSuccess() {
        // Given
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setUsername("testuser");
        createDTO.setEmail("test@example.com");
        createDTO.setPhone("13800138000");
        
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        
        when(userRepository.findOne(any(UserQueryDTO.class))).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // When
        UserVO result = userService.createUser(createDTO);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("testuser");
    }
    
    @Test
    @DisplayName("创建用户失败-用户名已存在")
    void testCreateUserFailUsernameDuplicate() {
        // Given
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setUsername("existuser");
        
        User existUser = new User();
        existUser.setId(1L);
        existUser.setUsername("existuser");
        
        when(userRepository.findOne(any(UserQueryDTO.class))).thenReturn(existUser);
        
        // When & Then
        assertThatThrownBy(() -> userService.createUser(createDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("用户名已存在");
    }
}
```

### 9.2 集成测试规范

```java
/**
 * Controller层集成测试示例
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    @DisplayName("创建用户接口测试")
    void testCreateUserAPI() {
        // Given
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setUsername("testuser");
        createDTO.setEmail("test@example.com");
        createDTO.setPhone("13800138000");
        
        // When
        ResponseEntity<Result> response = restTemplate.postForEntity(
                "/api/user", createDTO, Result.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getCode()).isEqualTo(200);
        assertThat(response.getBody().getMessage()).isEqualTo("操作成功");
    }
}
```

## 10. 文档规范

### 10.1 API文档规范

每个Controller类都必须提供完整的API文档注释：

```java
/**
 * 用户管理API
 * 
 * 提供用户相关的增删改查接口
 * 
 * @author Jax
 * @since 2025-01-07
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    /**
     * 创建用户
     * 
     * 创建新用户时需要校验用户名和邮箱的唯一性
     * 
     * @param createDTO 用户创建请求参数
     * @return 创建成功的用户信息
     * 
     * @apiNote 示例请求：
     * POST /api/user
     * {
     *   "username": "testuser",
     *   "password": "123456",
     *   "email": "test@example.com",
     *   "phone": "13800138000"
     * }
     * 
     * @apiNote 示例响应：
     * {
     *   "code": 200,
     *   "message": "操作成功",
     *   "data": {
     *     "id": 1,
     *     "username": "testuser",
     *     "email": "test@example.com",
     *     "phone": "13800138000",
     *     "status": 1,
     *     "createTime": "2025-01-07 10:00:00"
     *   }
     * }
     */
    @PostMapping
    public Result<UserVO> createUser(@Valid @RequestBody UserCreateDTO createDTO) {
        // 方法实现
    }
}
```

### 10.2 数据库文档规范

每张表都要在对应的SQL文件中详细说明：

```sql
-- 用户表 (t_user)
-- 
-- 用途：存储系统用户的基本信息
-- 特性：支持逻辑删除，自动维护创建和更新时间
-- 索引：username(唯一)、email(唯一)、phone(唯一)、status、create_time
-- 
-- 关联表：
-- - t_user_role: 用户角色关联
-- 
-- 字段说明：
-- - id: 主键，自增
-- - username: 登录用户名，全局唯一
-- - password: 登录密码，BCrypt加密存储
-- - email: 邮箱地址，全局唯一
-- - phone: 手机号码，全局唯一
-- - status: 用户状态，0-禁用 1-启用
-- - create_time: 创建时间，自动设置
-- - update_time: 更新时间，自动维护
-- - deleted: 逻辑删除标记，0-未删除 1-已删除

CREATE TABLE `t_user` (
  -- 表结构定义
);
```

## 📝 检查清单

在提交代码前，请确保以下检查项都已通过：

### 代码质量检查
- [ ] 没有使用Lombok注解
- [ ] 所有类都有完整的getter/setter/constructor/toString方法
- [ ] 命名符合规范（驼峰命名法）
- [ ] 所有公共方法都有JavaDoc注释
- [ ] 没有跨层调用
- [ ] 异常处理符合规范

### 架构检查
- [ ] Controller层只处理HTTP请求
- [ ] Service层包含业务逻辑
- [ ] Repository层使用DTO封装查询条件
- [ ] 没有在上层暴露Wrapper或其他ORM概念
- [ ] 转换方法使用Convert工具类

### 数据传输检查
- [ ] DTO类有适当的校验注解
- [ ] QueryDTO继承自PageQueryDTO
- [ ] VO类有JSON格式化注解
- [ ] 转换方法处理了null值

### 权限和日志检查
- [ ] 敏感操作添加了权限检查
- [ ] 重要操作添加了@AuditLog注解
- [ ] 权限编码符合层级结构
- [ ] 日志描述清晰明确

### 测试检查
- [ ] 编写了单元测试
- [ ] 测试覆盖了主要的业务场景
- [ ] 测试方法有清晰的DisplayName
- [ ] 集成测试使用了测试数据

---

严格遵循以上规范，可以确保代码质量和项目的可维护性。如有疑问，请参考现有代码示例或咨询项目负责人。 