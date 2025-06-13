# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 2.7.18 enterprise web application with a focus on user management (RBAC) and comprehensive logging capabilities. The project follows a 4-layer architecture pattern and uses MyBatis-Plus with MySQL for data persistence.

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

### Git Hooks Setup
```bash
# Linux/macOS
./scripts/setup-git-hooks.sh

# Windows
./scripts/setup-git-hooks.bat
```

## Architecture Overview

### Layer Structure
- **Controller Layer**: REST API endpoints with `@AuditLog` annotation for automatic audit logging
- **Service Layer**: Business logic with transaction management  
- **Repository Layer**: Data access abstraction using Repository pattern
- **Mapper Layer**: MyBatis-Plus mappers with XML configurations

### Core Modules
- **User Management**: RBAC implementation with User-Role-Permission model
- **Authentication**: JWT-based authentication with DingTalk OAuth2 integration
- **Logging System**: Multi-level logging (audit, system, login logs) with database persistence
- **Configuration**: Environment-specific configs in `application.yml`

### Key Technologies
- **Database**: MySQL 8.0.33 with Druid connection pool
- **ORM**: MyBatis-Plus 3.5.3.1 
- **Caching**: Redis with Spring Data Redis
- **JSON**: Alibaba FastJSON2
- **AOP**: Spring AOP for audit logging aspects
- **Security**: Spring Security with JWT authentication
- **OAuth**: DingTalk OAuth2 integration

## Database Architecture

The database follows RBAC pattern with these core tables:
- User management: `t_user`, `t_role`, `t_permission`
- Associations: `t_user_role`, `t_role_permission`  
- Logging: `t_audit_log`, `t_system_log`, `t_login_log`

All tables use soft delete pattern with `deleted` field and include audit fields (`create_time`, `update_time`).

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
- Existing users need password migration via `sql/auth_upgrade.sql`
- Default admin user: username `admin`, password `admin123`
- Default test user: username `test`, password `test123`

## Development Guidelines

### File Organization
- DTOs for API request/response in `dto/` packages
- VOs for view layer responses in `vo/` packages  
- Entity classes map directly to database tables
- Converters handle DTO/Entity transformations

### Configuration Management
- Environment-specific configs in `application.yml`
- MyBatis-Plus configuration in `MybatisPlusConfig`
- Redis serialization config in `RedisConfig`
- Security configuration in `SecurityConfig`

### Testing Strategy
- Test classes follow source package structure
- Integration tests in `BiouProjectApplicationTests`
- Database tests use embedded H2 or test profiles

## Error Handling

Global exception handling via `GlobalExceptionHandler` with standardized `Result<T>` response format. Custom business exceptions extend `BusinessException`.

## Build Configuration  

Maven-based build with Spring Boot plugin for JAR packaging. Static resources and templates supported under `src/main/resources/`.