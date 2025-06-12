package com.biou.convert;

import com.biou.entity.AuditLog;
import com.biou.entity.LoginLog;
import com.biou.entity.SystemLog;
import com.biou.vo.AuditLogVO;
import com.biou.vo.LoginLogVO;
import com.biou.vo.SystemLogVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志转换类
 *
 * @author Jax
 * @since 2025-01-07
 */
public class LogConvert {

    /**
     * AuditLog 转 AuditLogVO
     */
    public static AuditLogVO toVO(AuditLog auditLog) {
        if (auditLog == null) {
            return null;
        }
        
        AuditLogVO vo = new AuditLogVO();
        vo.setId(auditLog.getId());
        vo.setUserId(auditLog.getUserId());
        vo.setUsername(auditLog.getUsername());
        vo.setOperationType(auditLog.getOperationType());
        vo.setBusinessType(auditLog.getBusinessType());
        vo.setModule(auditLog.getModule());
        vo.setDescription(auditLog.getDescription());
        vo.setMethod(auditLog.getMethod());
        vo.setRequestUrl(auditLog.getRequestUrl());
        vo.setRequestMethod(auditLog.getRequestMethod());
        vo.setIpAddress(auditLog.getIpAddress());
        vo.setStatus(auditLog.getStatus());
        vo.setErrorMessage(auditLog.getErrorMessage());
        vo.setExecutionTime(auditLog.getExecutionTime());
        vo.setCreateTime(auditLog.getCreateTime());
        
        return vo;
    }

    /**
     * AuditLog 列表转 AuditLogVO 列表
     */
    public static List<AuditLogVO> toVOList(List<AuditLog> auditLogList) {
        if (auditLogList == null || auditLogList.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<AuditLogVO> voList = new ArrayList<>();
        for (AuditLog auditLog : auditLogList) {
            voList.add(toVO(auditLog));
        }
        
        return voList;
    }

    /**
     * SystemLog 转 SystemLogVO
     */
    public static SystemLogVO toVO(SystemLog systemLog) {
        if (systemLog == null) {
            return null;
        }
        
        SystemLogVO vo = new SystemLogVO();
        vo.setId(systemLog.getId());
        vo.setTraceId(systemLog.getTraceId());
        vo.setLevel(systemLog.getLevel());
        vo.setLoggerName(systemLog.getLoggerName());
        vo.setMessage(systemLog.getMessage());
        vo.setException(systemLog.getException());
        vo.setThreadName(systemLog.getThreadName());
        vo.setClassName(systemLog.getClassName());
        vo.setMethodName(systemLog.getMethodName());
        vo.setLineNumber(systemLog.getLineNumber());
        vo.setCreateTime(systemLog.getCreateTime());
        
        return vo;
    }

    /**
     * SystemLog 列表转 SystemLogVO 列表
     */
    public static List<SystemLogVO> toSystemLogVOList(List<SystemLog> systemLogList) {
        if (systemLogList == null || systemLogList.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<SystemLogVO> voList = new ArrayList<>();
        for (SystemLog systemLog : systemLogList) {
            voList.add(toVO(systemLog));
        }
        
        return voList;
    }

    /**
     * LoginLog 转 LoginLogVO
     */
    public static LoginLogVO toVO(LoginLog loginLog) {
        if (loginLog == null) {
            return null;
        }
        
        LoginLogVO vo = new LoginLogVO();
        vo.setId(loginLog.getId());
        vo.setUserId(loginLog.getUserId());
        vo.setUsername(loginLog.getUsername());
        vo.setLoginType(loginLog.getLoginType());
        vo.setIpAddress(loginLog.getIpAddress());
        vo.setLocation(loginLog.getLocation());
        vo.setBrowser(loginLog.getBrowser());
        vo.setOs(loginLog.getOs());
        vo.setStatus(loginLog.getStatus());
        vo.setMessage(loginLog.getMessage());
        vo.setLoginTime(loginLog.getLoginTime());
        
        return vo;
    }

    /**
     * LoginLog 列表转 LoginLogVO 列表
     */
    public static List<LoginLogVO> toLoginLogVOList(List<LoginLog> loginLogList) {
        if (loginLogList == null || loginLogList.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<LoginLogVO> voList = new ArrayList<>();
        for (LoginLog loginLog : loginLogList) {
            voList.add(toVO(loginLog));
        }
        
        return voList;
    }
} 