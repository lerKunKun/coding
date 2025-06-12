package com.biou.service.impl;

import com.alibaba.fastjson2.JSON;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biou.constant.LogConstants;
import com.biou.convert.LogConvert;
import com.biou.dto.AuditLogQueryDTO;
import com.biou.dto.LoginLogQueryDTO;
import com.biou.dto.SystemLogQueryDTO;
import com.biou.entity.AuditLog;
import com.biou.entity.LoginLog;
import com.biou.entity.SystemLog;
import com.biou.repository.AuditLogRepository;
import com.biou.repository.LoginLogRepository;
import com.biou.repository.SystemLogRepository;
import com.biou.service.LogService;
import com.biou.util.LogUtils;

import com.biou.vo.AuditLogVO;
import com.biou.vo.LoginLogVO;
import com.biou.vo.SystemLogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志服务实现类
 *
 * @author Jax
 * @since 2025-01-07
 */
@Service
public class LogServiceImpl implements LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Autowired
    private LoginLogRepository loginLogRepository;

    @Override
    public Page<AuditLogVO> pageAuditLog(AuditLogQueryDTO queryDTO) {
        try {
            Page<AuditLog> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
            page = auditLogRepository.pageByQuery(page, queryDTO);
            
            Page<AuditLogVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
            result.setRecords(LogConvert.toVOList(page.getRecords()));
            
            return result;
        } catch (Exception e) {
            logger.error("分页查询审计日志失败", e);
            throw new RuntimeException("分页查询审计日志失败：" + e.getMessage());
        }
    }

    @Override
    public Page<SystemLogVO> pageSystemLog(SystemLogQueryDTO queryDTO) {
        try {
            Page<SystemLog> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
            page = systemLogRepository.pageByQuery(page, queryDTO);
            
            Page<SystemLogVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
            result.setRecords(LogConvert.toSystemLogVOList(page.getRecords()));
            
            return result;
        } catch (Exception e) {
            logger.error("分页查询系统日志失败", e);
            throw new RuntimeException("分页查询系统日志失败：" + e.getMessage());
        }
    }

    @Override
    public Page<LoginLogVO> pageLoginLog(LoginLogQueryDTO queryDTO) {
        try {
            Page<LoginLog> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
            page = loginLogRepository.pageByQuery(page, queryDTO);
            
            Page<LoginLogVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
            result.setRecords(LogConvert.toLoginLogVOList(page.getRecords()));
            
            return result;
        } catch (Exception e) {
            logger.error("分页查询登录日志失败", e);
            throw new RuntimeException("分页查询登录日志失败：" + e.getMessage());
        }
    }

    @Override
    public void saveAuditLog(AuditLog auditLog) {
        try {
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            logger.error("保存审计日志失败", e);
            // 这里不抛出异常，避免影响主业务流程
        }
    }

    @Override
    public void saveSystemLog(SystemLog systemLog) {
        try {
            systemLogRepository.save(systemLog);
        } catch (Exception e) {
            logger.error("保存系统日志失败", e);
            // 这里不抛出异常，避免影响主业务流程
        }
    }

    @Override
    public void saveLoginLog(LoginLog loginLog) {
        try {
            loginLogRepository.save(loginLog);
        } catch (Exception e) {
            logger.error("保存登录日志失败", e);
            // 这里不抛出异常，避免影响主业务流程
        }
    }

    @Override
    public void recordOperationLog(Long userId, String username, String operationType, String businessType,
                                   String module, String description, HttpServletRequest request,
                                   long startTime, boolean success, String errorMessage) {
        try {
            String ipAddress = LogUtils.getClientIpAddress(request);
            String userAgent = LogUtils.getUserAgent(request);
            String method = request.getMethod();
            String requestUrl = request.getRequestURI();
            Long executionTime = System.currentTimeMillis() - startTime;
            
            // 获取请求参数（简化处理）
            String requestParams = getRequestParams(request);
            
            AuditLog auditLog = LogUtils.createAuditLog(
                userId, username, operationType, businessType, module, description,
                method, requestUrl, request.getMethod(), requestParams, null,
                ipAddress, userAgent, success ? LogConstants.Status.SUCCESS : LogConstants.Status.FAIL,
                errorMessage, executionTime
            );
            
            saveAuditLog(auditLog);
        } catch (Exception e) {
            logger.error("记录操作日志失败", e);
        }
    }

    @Override
    public void recordLoginLog(Long userId, String username, String loginType, HttpServletRequest request,
                               boolean success, String message) {
        try {
            String ipAddress = LogUtils.getClientIpAddress(request);
            String userAgent = LogUtils.getUserAgent(request);
            
            LoginLog loginLog = LogUtils.createLoginLog(
                userId, username, loginType, ipAddress, userAgent,
                success ? LogConstants.Status.SUCCESS : LogConstants.Status.FAIL, message
            );
            
            saveLoginLog(loginLog);
        } catch (Exception e) {
            logger.error("记录登录日志失败", e);
        }
    }

    @Override
    public Map<String, Long> cleanExpiredLogs(int retentionDays) {
        Map<String, Long> result = new HashMap<>();
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(retentionDays);
        
        try {
            // 清理审计日志
            long auditLogCount = auditLogRepository.countByCreateTimeBefore(beforeDate);
            int deletedAuditLogs = auditLogRepository.deleteByCreateTimeBefore(beforeDate);
            result.put("auditLog", (long) deletedAuditLogs);
            
            // 清理系统日志
            long systemLogCount = systemLogRepository.countByCreateTimeBefore(beforeDate);
            int deletedSystemLogs = systemLogRepository.deleteByCreateTimeBefore(beforeDate);
            result.put("systemLog", (long) deletedSystemLogs);
            
            // 清理登录日志
            long loginLogCount = loginLogRepository.countByLoginTimeBefore(beforeDate);
            int deletedLoginLogs = loginLogRepository.deleteByLoginTimeBefore(beforeDate);
            result.put("loginLog", (long) deletedLoginLogs);
            
            logger.info("清理过期日志完成，保留天数：{}，清理结果：{}", retentionDays, result);
            
        } catch (Exception e) {
            logger.error("清理过期日志失败", e);
            throw new RuntimeException("清理过期日志失败：" + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getLogStatistics(int days) {
        Map<String, Object> statistics = new HashMap<>();
        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        
        try {
            // 统计审计日志
            long auditLogTotal = auditLogRepository.countByTimeRange(startTime, null);
            long auditLogSuccess = auditLogRepository.countSuccessByTimeRange(startTime, null);
            
            // 统计系统日志
            long systemLogTotal = systemLogRepository.countByTimeRange(startTime, null);
            long systemLogError = systemLogRepository.countErrorByTimeRange(startTime, null);
            
            // 统计登录日志
            long loginLogTotal = loginLogRepository.countByTimeRange(startTime, null);
            long loginLogSuccess = loginLogRepository.countSuccessByTimeRange(startTime, null);
            
            statistics.put("auditLog", Map.of("total", auditLogTotal, "success", auditLogSuccess, "fail", auditLogTotal - auditLogSuccess));
            statistics.put("systemLog", Map.of("total", systemLogTotal, "error", systemLogError));
            statistics.put("loginLog", Map.of("total", loginLogTotal, "success", loginLogSuccess, "fail", loginLogTotal - loginLogSuccess));
            statistics.put("statisticsDays", days);
            statistics.put("startTime", startTime);
            
        } catch (Exception e) {
            logger.error("获取日志统计信息失败", e);
            throw new RuntimeException("获取日志统计信息失败：" + e.getMessage());
        }
        
        return statistics;
    }

    /**
     * 获取请求参数
     */
    private String getRequestParams(HttpServletRequest request) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (parameterMap.isEmpty()) {
                return null;
            }
            
            Map<String, Object> params = new HashMap<>();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String[] values = entry.getValue();
                if (values.length == 1) {
                    params.put(entry.getKey(), values[0]);
                } else {
                    params.put(entry.getKey(), values);
                }
            }
            
            String jsonParams = JSON.toJSONString(params);
            return LogUtils.truncateText(jsonParams, 1000); // 限制长度
        } catch (Exception e) {
            logger.warn("获取请求参数失败", e);
            return null;
        }
    }
} 