package com.biou.aspect;

import com.alibaba.fastjson2.JSON;
import com.biou.annotation.AuditLog;
import com.biou.constant.LogConstants;
import com.biou.service.LogService;
import com.biou.util.LogUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 审计日志AOP切面
 *
 * @author Jax
 * @since 2025-01-07
 */
@Aspect
@Component
public class AuditLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogAspect.class);

    @Autowired
    private LogService logService;

    /**
     * 切点定义
     */
    @Pointcut("@annotation(com.biou.annotation.AuditLog)")
    public void auditLogPointcut() {
    }

    /**
     * 环绕通知
     */
    @Around("auditLogPointcut() && @annotation(auditLog)")
    public Object around(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            // 如果不是Web请求，直接执行方法
            return joinPoint.proceed();
        }
        
        HttpServletRequest request = attributes.getRequest();
        String operationType = auditLog.operationType();
        String businessType = auditLog.businessType();
        String module = auditLog.module();
        String description = auditLog.description();
        
        // 如果没有指定，则尝试从方法名推断
        if (!StringUtils.isNotBlank(operationType)) {
            operationType = inferOperationType(joinPoint.getSignature().getName());
        }
        
        if (!StringUtils.isNotBlank(module)) {
            module = joinPoint.getTarget().getClass().getSimpleName();
        }
        
        if (!StringUtils.isNotBlank(description)) {
            description = joinPoint.getSignature().getName();
        }

        Object result = null;
        String errorMessage = null;
        boolean success = true;

        try {
            // 执行目标方法
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            success = false;
            errorMessage = e.getMessage();
            throw e;
        } finally {
            try {
                // 记录审计日志
                recordAuditLog(request, operationType, businessType, module, description,
                               startTime, success, errorMessage);
            } catch (Exception e) {
                logger.error("记录审计日志失败", e);
            }
        }
    }

    /**
     * 记录审计日志
     */
    private void recordAuditLog(HttpServletRequest request, String operationType, String businessType,
                                String module, String description, long startTime, boolean success,
                                String errorMessage) {
        try {
            // 这里简化处理，实际项目中需要从Session或Token中获取用户信息
            Long userId = getCurrentUserId(request);
            String username = getCurrentUsername(request);
            
            String method = request.getMethod();
            String requestUrl = request.getRequestURI();
            String requestMethod = request.getMethod();
            String requestParams = JSON.toJSONString(request.getParameterMap());
            String responseData = null; // 在环绕通知中可以获取响应数据
            Integer status = success ? LogConstants.Status.SUCCESS : LogConstants.Status.FAIL;
            Long executionTime = System.currentTimeMillis() - startTime;

            logService.recordAuditLog(userId, username, operationType, businessType, module,
                                    description, method, requestUrl, requestMethod, requestParams,
                                    responseData, request, status, errorMessage, executionTime);
        } catch (Exception e) {
            logger.error("记录审计日志失败", e);
        }
    }

    /**
     * 从方法名推断操作类型
     */
    private String inferOperationType(String methodName) {
        String lowerMethodName = methodName.toLowerCase();
        
        if (lowerMethodName.startsWith("create") || lowerMethodName.startsWith("add") || 
            lowerMethodName.startsWith("insert") || lowerMethodName.startsWith("save")) {
            return LogConstants.OperationType.CREATE;
        } else if (lowerMethodName.startsWith("update") || lowerMethodName.startsWith("modify") || 
                   lowerMethodName.startsWith("edit")) {
            return LogConstants.OperationType.UPDATE;
        } else if (lowerMethodName.startsWith("delete") || lowerMethodName.startsWith("remove")) {
            return LogConstants.OperationType.DELETE;
        } else if (lowerMethodName.startsWith("get") || lowerMethodName.startsWith("find") || 
                   lowerMethodName.startsWith("list") || lowerMethodName.startsWith("page") ||
                   lowerMethodName.startsWith("query") || lowerMethodName.startsWith("search")) {
            return LogConstants.OperationType.QUERY;
        } else {
            return "UNKNOWN";
        }
    }

    /**
     * 获取当前用户ID（简化实现）
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        // 这里需要根据实际的认证机制来获取用户ID
        // 可以从JWT Token、Session或其他方式获取
        String userIdHeader = request.getHeader("X-User-Id");
        if (StringUtils.isNotBlank(userIdHeader)) {
            try {
                return Long.parseLong(userIdHeader);
            } catch (NumberFormatException e) {
                logger.warn("解析用户ID失败: {}", userIdHeader);
            }
        }
        return null;
    }

    /**
     * 获取当前用户名（简化实现）
     */
    private String getCurrentUsername(HttpServletRequest request) {
        // 这里需要根据实际的认证机制来获取用户名
        // 可以从JWT Token、Session或其他方式获取
        String usernameHeader = request.getHeader("X-Username");
        if (StringUtils.isNotBlank(usernameHeader)) {
            return usernameHeader;
        }
        return "anonymous";
    }
} 