package com.biou.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.biou.dto.AuditLogQueryDTO;
import com.biou.dto.LoginLogQueryDTO;
import com.biou.dto.SystemLogQueryDTO;
import com.biou.project.dto.UserQueryDTO;
import com.biou.entity.AuditLog;
import com.biou.entity.LoginLog;
import com.biou.entity.SystemLog;
import com.biou.project.entity.User;
import org.apache.commons.lang3.StringUtils;

/**
 * QueryWrapper工具类
 * 负责将DTO转换为MyBatis-Plus的QueryWrapper
 *
 * @author Jax
 * @since 2024-01-01
 */
public class QueryWrapperUtils {

    /**
     * 将UserQueryDTO转换为LambdaQueryWrapper
     *
     * @param queryDTO 查询DTO
     * @return LambdaQueryWrapper
     */
    public static LambdaQueryWrapper<User> buildUserQueryWrapper(UserQueryDTO queryDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (queryDTO == null) {
            return wrapper;
        }

        // ID条件
        if (queryDTO.getId() != null) {
            wrapper.eq(User::getId, queryDTO.getId());
        }

        // 用户名条件
        if (StringUtils.isNotBlank(queryDTO.getUsername())) {
            wrapper.eq(User::getUsername, queryDTO.getUsername());
        }

        // 邮箱条件
        if (StringUtils.isNotBlank(queryDTO.getEmail())) {
            wrapper.eq(User::getEmail, queryDTO.getEmail());
        }

        // 手机号条件
        if (StringUtils.isNotBlank(queryDTO.getPhone())) {
            wrapper.eq(User::getPhone, queryDTO.getPhone());
        }

        // 状态条件
        if (queryDTO.getStatus() != null) {
            wrapper.eq(User::getStatus, queryDTO.getStatus());
        }

        // 删除状态条件
        if (queryDTO.getDeleted() != null) {
            wrapper.eq(User::getDeleted, queryDTO.getDeleted());
        }

        // 创建时间范围
        if (queryDTO.getCreateTimeStart() != null) {
            wrapper.ge(User::getCreateTime, queryDTO.getCreateTimeStart());
        }
        if (queryDTO.getCreateTimeEnd() != null) {
            wrapper.le(User::getCreateTime, queryDTO.getCreateTimeEnd());
        }

        // 排序
        if (StringUtils.isNotBlank(queryDTO.getOrderBy())) {
            boolean isAsc = !"DESC".equalsIgnoreCase(queryDTO.getOrderDirection());
            
            switch (queryDTO.getOrderBy().toLowerCase()) {
                case "id":
                    wrapper.orderBy(true, isAsc, User::getId);
                    break;
                case "username":
                    wrapper.orderBy(true, isAsc, User::getUsername);
                    break;
                case "email":
                    wrapper.orderBy(true, isAsc, User::getEmail);
                    break;
                case "status":
                    wrapper.orderBy(true, isAsc, User::getStatus);
                    break;
                case "createtime":
                case "create_time":
                    wrapper.orderBy(true, isAsc, User::getCreateTime);
                    break;
                case "updatetime":
                case "update_time":
                    wrapper.orderBy(true, isAsc, User::getUpdateTime);
                    break;
                default:
                    // 默认按创建时间倒序
                    wrapper.orderByDesc(User::getCreateTime);
                    break;
            }
        } else {
            // 默认按创建时间倒序
            wrapper.orderByDesc(User::getCreateTime);
        }

        return wrapper;
    }

    /**
     * 将UserQueryDTO转换为LambdaUpdateWrapper
     *
     * @param queryDTO 查询DTO
     * @return LambdaUpdateWrapper
     */
    public static LambdaUpdateWrapper<User> buildUserUpdateWrapper(UserQueryDTO queryDTO) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        
        if (queryDTO == null) {
            return wrapper;
        }

        // ID条件
        if (queryDTO.getId() != null) {
            wrapper.eq(User::getId, queryDTO.getId());
        }

        // 用户名条件
        if (StringUtils.isNotBlank(queryDTO.getUsername())) {
            wrapper.eq(User::getUsername, queryDTO.getUsername());
        }

        // 邮箱条件
        if (StringUtils.isNotBlank(queryDTO.getEmail())) {
            wrapper.eq(User::getEmail, queryDTO.getEmail());
        }

        // 手机号条件
        if (StringUtils.isNotBlank(queryDTO.getPhone())) {
            wrapper.eq(User::getPhone, queryDTO.getPhone());
        }

        // 状态条件
        if (queryDTO.getStatus() != null) {
            wrapper.eq(User::getStatus, queryDTO.getStatus());
        }

        // 删除状态条件
        if (queryDTO.getDeleted() != null) {
            wrapper.eq(User::getDeleted, queryDTO.getDeleted());
        }

        // 创建时间范围
        if (queryDTO.getCreateTimeStart() != null) {
            wrapper.ge(User::getCreateTime, queryDTO.getCreateTimeStart());
        }
        if (queryDTO.getCreateTimeEnd() != null) {
            wrapper.le(User::getCreateTime, queryDTO.getCreateTimeEnd());
        }

        return wrapper;
    }

    /**
     * 将AuditLogQueryDTO转换为LambdaQueryWrapper
     *
     * @param queryDTO 查询DTO
     * @return LambdaQueryWrapper
     */
    public static LambdaQueryWrapper<AuditLog> buildAuditLogQueryWrapper(AuditLogQueryDTO queryDTO) {
        LambdaQueryWrapper<AuditLog> wrapper = new LambdaQueryWrapper<>();
        
        if (queryDTO == null) {
            return wrapper.orderByDesc(AuditLog::getCreateTime);
        }

        // 操作用户名条件
        if (StringUtils.isNotBlank(queryDTO.getUsername())) {
            wrapper.like(AuditLog::getUsername, queryDTO.getUsername());
        }

        // 操作类型条件
        if (StringUtils.isNotBlank(queryDTO.getOperationType())) {
            wrapper.eq(AuditLog::getOperationType, queryDTO.getOperationType());
        }

        // 业务类型条件
        if (StringUtils.isNotBlank(queryDTO.getBusinessType())) {
            wrapper.eq(AuditLog::getBusinessType, queryDTO.getBusinessType());
        }

        // 操作模块条件
        if (StringUtils.isNotBlank(queryDTO.getModule())) {
            wrapper.like(AuditLog::getModule, queryDTO.getModule());
        }

        // IP地址条件
        if (StringUtils.isNotBlank(queryDTO.getIpAddress())) {
            wrapper.like(AuditLog::getIpAddress, queryDTO.getIpAddress());
        }

        // 操作状态条件
        if (queryDTO.getStatus() != null) {
            wrapper.eq(AuditLog::getStatus, queryDTO.getStatus());
        }

        // 创建时间范围
        if (queryDTO.getStartTime() != null) {
            wrapper.ge(AuditLog::getCreateTime, queryDTO.getStartTime());
        }
        if (queryDTO.getEndTime() != null) {
            wrapper.le(AuditLog::getCreateTime, queryDTO.getEndTime());
        }

        // 默认按创建时间倒序
        wrapper.orderByDesc(AuditLog::getCreateTime);

        return wrapper;
    }

    /**
     * 将SystemLogQueryDTO转换为LambdaQueryWrapper
     *
     * @param queryDTO 查询DTO
     * @return LambdaQueryWrapper
     */
    public static LambdaQueryWrapper<SystemLog> buildSystemLogQueryWrapper(SystemLogQueryDTO queryDTO) {
        LambdaQueryWrapper<SystemLog> wrapper = new LambdaQueryWrapper<>();
        
        if (queryDTO == null) {
            return wrapper.orderByDesc(SystemLog::getCreateTime);
        }

        // 链路追踪ID条件
        if (StringUtils.isNotBlank(queryDTO.getTraceId())) {
            wrapper.eq(SystemLog::getTraceId, queryDTO.getTraceId());
        }

        // 日志级别条件
        if (StringUtils.isNotBlank(queryDTO.getLevel())) {
            wrapper.eq(SystemLog::getLevel, queryDTO.getLevel());
        }

        // 日志记录器名称条件
        if (StringUtils.isNotBlank(queryDTO.getLoggerName())) {
            wrapper.like(SystemLog::getLoggerName, queryDTO.getLoggerName());
        }

        // 类名条件
        if (StringUtils.isNotBlank(queryDTO.getClassName())) {
            wrapper.like(SystemLog::getClassName, queryDTO.getClassName());
        }

        // 关键字搜索（在消息和异常信息中搜索）
        if (StringUtils.isNotBlank(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like(SystemLog::getMessage, queryDTO.getKeyword())
                             .or()
                             .like(SystemLog::getException, queryDTO.getKeyword()));
        }

        // 创建时间范围
        if (queryDTO.getStartTime() != null) {
            wrapper.ge(SystemLog::getCreateTime, queryDTO.getStartTime());
        }
        if (queryDTO.getEndTime() != null) {
            wrapper.le(SystemLog::getCreateTime, queryDTO.getEndTime());
        }

        // 默认按创建时间倒序
        wrapper.orderByDesc(SystemLog::getCreateTime);

        return wrapper;
    }

    /**
     * 将LoginLogQueryDTO转换为LambdaQueryWrapper
     *
     * @param queryDTO 查询DTO
     * @return LambdaQueryWrapper
     */
    public static LambdaQueryWrapper<LoginLog> buildLoginLogQueryWrapper(LoginLogQueryDTO queryDTO) {
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        
        if (queryDTO == null) {
            return wrapper.orderByDesc(LoginLog::getLoginTime);
        }

        // 用户名条件
        if (StringUtils.isNotBlank(queryDTO.getUsername())) {
            wrapper.like(LoginLog::getUsername, queryDTO.getUsername());
        }

        // 登录类型条件
        if (StringUtils.isNotBlank(queryDTO.getLoginType())) {
            wrapper.eq(LoginLog::getLoginType, queryDTO.getLoginType());
        }

        // IP地址条件
        if (StringUtils.isNotBlank(queryDTO.getIpAddress())) {
            wrapper.like(LoginLog::getIpAddress, queryDTO.getIpAddress());
        }

        // 登录状态条件
        if (queryDTO.getStatus() != null) {
            wrapper.eq(LoginLog::getStatus, queryDTO.getStatus());
        }

        // 登录时间范围
        if (queryDTO.getStartTime() != null) {
            wrapper.ge(LoginLog::getLoginTime, queryDTO.getStartTime());
        }
        if (queryDTO.getEndTime() != null) {
            wrapper.le(LoginLog::getLoginTime, queryDTO.getEndTime());
        }

        // 默认按登录时间倒序
        wrapper.orderByDesc(LoginLog::getLoginTime);

        return wrapper;
    }
} 