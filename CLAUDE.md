# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 2.7.18 enterprise web application featuring JWT authentication, DingTalk OAuth2 integration, RBAC user management, and comprehensive logging capabilities. The project strictly follows a 4-layer architecture pattern with production-ready security features and cross-platform development support.

## Common Commands

### Build and Run
```bash
# Clean and compile
mvn clean compile

# Run application
mvn spring-boot:run

# Package JAR
mvn clean package

# Run tests
mvn test

# Run specific test class
mvn test -Dtest=BiouProjectApplicationTests
```

### Git Hooks Setup (Cross-Platform)
```bash
# Linux/macOS
./scripts/setup-git-hooks.sh

# Windows (Multiple Options)
scripts\setup-git-hooks.bat          # Batch script (recommended for beginners)
.\scripts\setup-git-hooks.ps1        # PowerShell script (recommended for advanced users)
bash scripts/setup-git-hooks.sh      # Git Bash (recommended for developers)
```

## Architecture Overview

### Layer Structure
- **Controller Layer**: REST API endpoints with `@AuditLog` annotation for automatic audit logging
- **Service Layer**: Business logic with transaction management  
- **Repository Layer**: Data access abstraction using Repository pattern
- **Mapper Layer**: MyBatis-Plus mappers with XML configurations

### Core Modules
- **User Management**: RBAC implementation with User-Role-Permission model
- **Authentication**: JWT-based authentication with DingTalk OAuth2 integration and BCrypt password encryption
- **Logging System**: Multi-level logging (audit, system, login logs) with database persistence
- **Configuration**: Environment-specific configs in `application.yml`
- **Security**: Spring Security integration with JWT filters and CORS support
- **Cross-Platform Development**: Full Windows/macOS/Linux support with automated Git hooks

### Key Technologies
- **Database**: MySQL 8.0.33 with Druid connection pool and comprehensive monitoring
- **ORM**: MyBatis-Plus 3.5.3.1 with automatic CRUD generation
- **Caching**: Redis with Spring Data Redis and custom serialization
- **JSON**: Alibaba FastJSON2 for high-performance processing
- **AOP**: Spring AOP for audit logging aspects
- **Security**: Spring Security with JWT authentication and BCrypt password encoding
- **OAuth**: DingTalk OAuth2 integration with automatic user creation
- **HTTP Client**: Apache HttpClient for external API integration
- **Build**: Maven with cross-platform scripts and automated Git hooks

## Database Architecture

### Core Tables
The database follows RBAC pattern with these core tables:
- **User management**: `t_user` (with authentication fields), `t_role`, `t_permission`
- **Associations**: `t_user_role`, `t_role_permission`  
- **Logging**: `t_audit_log`, `t_system_log`, `t_login_log`

### Key Features
- **Soft delete pattern** with `deleted` field for data safety
- **Audit fields** (`create_time`, `update_time`) for tracking
- **Authentication fields** in user table: `dingtalk_user_id`, `dingtalk_union_id`, `last_login_time`, `last_login_ip`
- **BCrypt password encryption** for security
- **Optimized indexes** for performance

### SQL Management
- **Version-controlled migrations** in `sql/migrations/v1.x.x/` directories
- **Rollback scripts** in `sql/rollback/` for safe deployments
- **Production-ready templates** in `sql/templates/`
- **Automated validation** via Git hooks

## Logging System

### Audit Logging
- Use `@AuditLog` annotation on controller methods for automatic audit trail
- Logs are persisted to database via `DatabaseLogAppender`
- AOP aspect (`AuditLogAspect`) handles cross-cutting logging concerns

### Log Management
- Scheduled cleanup via `LogCleanupTask`
- Multiple log levels: audit, system, login
- Structured JSON serialization for complex data

## Authentication System

### JWT Configuration
- Access tokens expire in 24 hours by default
- Refresh tokens expire in 7 days by default
- Tokens stored in Redis blacklist upon logout
- JWT secret configurable via `jwt.secret` property

### DingTalk Integration
- OAuth2 authorization code flow
- Automatic user creation for new DingTalk users
- State parameter validation for CSRF protection
- Environment variables: `DINGTALK_APP_ID`, `DINGTALK_APP_SECRET`, `DINGTALK_REDIRECT_URI`

### Password Security
- BCrypt password encoding with strength 10
- Password migration available via SQL migration scripts
- Default admin user: username `admin`, password `admin123` (BCrypt encrypted)
- Default test user: username `test`, password `test123` (BCrypt encrypted)
- Legacy users can be migrated using provided SQL scripts

## Development Guidelines

### File Organization
- **DTOs** for API request/response in `dto/` packages (LoginDTO, DingTalkLoginDTO, TokenRefreshDTO)
- **VOs** for view layer responses in `vo/` packages (LoginVO, DingTalkLoginUrlVO)
- **Entity classes** map directly to database tables with authentication fields
- **Converters** handle DTO/Entity transformations
- **Filters** for JWT authentication (`JwtAuthenticationFilter`)
- **Utils** for JWT operations (`JwtUtils`) and DingTalk integration (`DingTalkUtils`)

### Configuration Management
- Environment-specific configs in `application.yml` with JWT and DingTalk settings
- MyBatis-Plus configuration in `MybatisPlusConfig`
- Redis serialization config in `RedisConfig`
- Security configuration in `SecurityConfig` with JWT filters
- Cross-platform Git hooks in `scripts/` directory

### Testing Strategy
- Test classes follow source package structure
- Integration tests in `BiouProjectApplicationTests`
- Database tests use embedded H2 or test profiles
- Authentication tests should verify JWT token generation and validation

### SQL Development
- Use version-controlled migration scripts in `sql/migrations/`
- Always create corresponding rollback scripts
- Follow SQL standards defined in `docs/SQL_STANDARDS.md`
- Validate scripts locally before committing

## Error Handling

Global exception handling via `GlobalExceptionHandler` with standardized `Result<T>` response format. Custom business exceptions extend `BusinessException`. Authentication errors are handled by Spring Security filters.

## API Endpoints

### Authentication APIs
- `POST /api/auth/login` - Username/password login
- `GET /api/auth/dingtalk/login-url` - Get DingTalk login URL
- `POST /api/auth/dingtalk/callback` - DingTalk login callback
- `POST /api/auth/refresh` - Refresh access token
- `POST /api/auth/logout` - User logout (JWT required)
- `GET /api/auth/validate` - Validate token

### User Management APIs
- All user management endpoints require JWT authentication
- Use `Authorization: Bearer <token>` header

## Cross-Platform Development

### Windows Support
- Full Windows 10/11 compatibility
- Multiple installation options: Batch files, PowerShell, Git Bash
- Windows-specific documentation in `docs/WINDOWS_SETUP.md`
- Automated Git hooks work in all Windows environments

### Development Tools
- Git hooks automatically validate code quality and SQL scripts
- Cross-platform scripts in `scripts/` directory
- IDE integration tested with VSCode, IntelliJ IDEA, and GitHub Desktop

## Build Configuration  

Maven-based build with Spring Boot plugin for JAR packaging. Includes JWT, Spring Security, and HTTP client dependencies. Static resources and templates supported under `src/main/resources/`.

## Quick Setup

1. **Database Setup**: Run migration scripts from `sql/migrations/v1.0.0/` and `sql/migrations/v1.1.0/`
2. **Configuration**: Update `application.yml` with database, Redis, and DingTalk settings
3. **Git Hooks**: Run appropriate setup script from `scripts/` directory
4. **Testing**: Use provided admin/test accounts or create new users via API