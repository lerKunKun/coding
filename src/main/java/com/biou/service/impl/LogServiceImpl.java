package com.biou.service.impl;

import com.alibaba.fastjson2.JSON;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biou.constant.LogConstants;
import com.biou.convert.LogConvert;
import com.biou.dto.AuditLogQueryDTO;
import com.biou.dto.LoginLogQueryDTO;
import com.biou.dto.SystemLogQueryDTO;
import com.biou.entity.AuditLog;
import com.biou.entity.LoginLog;
import com.biou.entity.SystemLog;
import com.biou.mapper.AuditLogMapper;
import com.biou.mapper.LoginLogMapper;
import com.biou.mapper.SystemLogMapper;
import com.biou.service.LogService;
import com.biou.util.LogUtils;
import com.biou.util.QueryWrapperUtils;

import com.biou.vo.AuditLogVO;
import com.biou.vo.LoginLogVO;
import com.biou.vo.SystemLogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private AuditLogMapper auditLogMapper;

    @Autowired
    private SystemLogMapper systemLogMapper;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public Page<AuditLogVO> pageAuditLog(AuditLogQueryDTO queryDTO) {
        try {
            Page<AuditLog> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
            page = auditLogMapper.selectPage(page, QueryWrapperUtils.buildAuditLogQueryWrapper(queryDTO));
            
            Page<AuditLogVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
            result.setRecords(page.getRecords().stream()
                    .map(log -> {
                        AuditLogVO vo = new AuditLogVO();
                        BeanUtils.copyProperties(log, vo);
                        return vo;
                    })
                    .collect(Collectors.toList()));
            return result;
        } catch (Exception e) {
            logger.error("分页查询审计日志失败", e);
            throw new RuntimeException("分页查询审计日志失败", e);
        }
    }

    @Override
    public Page<SystemLogVO> pageSystemLog(SystemLogQueryDTO queryDTO) {
        try {
            Page<SystemLog> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
            page = systemLogMapper.selectPage(page, QueryWrapperUtils.buildSystemLogQueryWrapper(queryDTO));
            
            Page<SystemLogVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
            result.setRecords(page.getRecords().stream()
                    .map(log -> {
                        SystemLogVO vo = new SystemLogVO();
                        BeanUtils.copyProperties(log, vo);
                        return vo;
                    })
                    .collect(Collectors.toList()));
            return result;
        } catch (Exception e) {
            logger.error("分页查询系统日志失败", e);
            throw new RuntimeException("分页查询系统日志失败", e);
        }
    }

    @Override
    public Page<LoginLogVO> pageLoginLog(LoginLogQueryDTO queryDTO) {
        try {
            Page<LoginLog> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
            page = loginLogMapper.selectPage(page, QueryWrapperUtils.buildLoginLogQueryWrapper(queryDTO));
            
            Page<LoginLogVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
            result.setRecords(page.getRecords().stream()
                    .map(log -> {
                        LoginLogVO vo = new LoginLogVO();
                        BeanUtils.copyProperties(log, vo);
                        return vo;
                    })
                    .collect(Collectors.toList()));
            return result;
        } catch (Exception e) {
            logger.error("分页查询登录日志失败", e);
            throw new RuntimeException("分页查询登录日志失败", e);
        }
    }

    @Override
    public void saveAuditLog(AuditLog auditLog) {
        try {
            auditLogMapper.insert(auditLog);
        } catch (Exception e) {
            logger.error("保存审计日志失败", e);
            // 这里不抛出异常，避免影响主业务流程
        }
    }

    @Override
    public void saveSystemLog(SystemLog systemLog) {
        try {
            systemLogMapper.insert(systemLog);
        } catch (Exception e) {
            logger.error("保存系统日志失败", e);
            // 这里不抛出异常，避免影响主业务流程
        }
    }

    @Override
    public void saveLoginLog(LoginLog loginLog) {
        try {
            loginLogMapper.insert(loginLog);
        } catch (Exception e) {
            logger.error("保存登录日志失败", e);
            // 这里不抛出异常，避免影响主业务流程
        }
    }

    @Override
    public void recordAuditLog(Long userId, String username, String operationType, String businessType,
                              String module, String description, String method, String requestUrl,
                              String requestMethod, String requestParams, String responseData,
                              HttpServletRequest request, Integer status, String errorMessage,
                              Long executionTime) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setUserId(userId);
            auditLog.setUsername(username);
            auditLog.setOperationType(operationType);
            auditLog.setBusinessType(businessType);
            auditLog.setModule(module);
            auditLog.setDescription(description);
            auditLog.setMethod(method);
            auditLog.setRequestUrl(requestUrl);
            auditLog.setRequestMethod(requestMethod);
            auditLog.setRequestParams(requestParams);
            auditLog.setResponseData(responseData);
            auditLog.setIpAddress(LogUtils.getIpAddress(request));
            auditLog.setUserAgent(LogUtils.getUserAgent(request));
            auditLog.setStatus(status);
            auditLog.setErrorMessage(errorMessage);
            auditLog.setExecutionTime(executionTime);
            auditLog.setCreateTime(LocalDateTime.now());

            auditLogMapper.insert(auditLog);
        } catch (Exception e) {
            logger.error("记录审计日志失败", e);
        }
    }

    @Override
    public void recordLoginLog(Long userId, String username, String loginType, HttpServletRequest request,
                             Integer status, String message) {
        try {
            LoginLog loginLog = new LoginLog();
            loginLog.setUserId(userId);
            loginLog.setUsername(username);
            loginLog.setLoginType(loginType);
            loginLog.setIpAddress(LogUtils.getIpAddress(request));
            loginLog.setUserAgent(LogUtils.getUserAgent(request));
            loginLog.setLocation(LogUtils.getLocation(request));
            loginLog.setBrowser(LogUtils.getBrowser(request));
            loginLog.setOs(LogUtils.getOs(request));
            loginLog.setStatus(status);
            loginLog.setMessage(message);
            loginLog.setLoginTime(LocalDateTime.now());

            loginLogMapper.insert(loginLog);
        } catch (Exception e) {
            logger.error("记录登录日志失败", e);
        }
    }

    @Override
    public void recordSystemLog(String level, String loggerName, String message, String exception,
                              String threadName, String className, String methodName, Integer lineNumber) {
        try {
            SystemLog systemLog = new SystemLog();
            systemLog.setLevel(level);
            systemLog.setLoggerName(loggerName);
            systemLog.setMessage(LogUtils.truncate(message, 1000));
            systemLog.setException(LogUtils.truncate(exception, 2000));
            systemLog.setThreadName(threadName);
            systemLog.setClassName(className);
            systemLog.setMethodName(methodName);
            systemLog.setLineNumber(lineNumber);
            systemLog.setCreateTime(LocalDateTime.now());

            systemLogMapper.insert(systemLog);
        } catch (Exception e) {
            logger.error("记录系统日志失败", e);
        }
    }

    @Override
    public Map<String, Long> cleanExpiredLogs(Integer days) {
        Map<String, Long> result = new HashMap<>();
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(days);

        try {
            // 清理审计日志
            long auditLogCount = auditLogMapper.selectCount(new LambdaQueryWrapper<AuditLog>().lt(AuditLog::getCreateTime, beforeDate));
            int deletedAuditLogs = auditLogMapper.delete(new LambdaQueryWrapper<AuditLog>().lt(AuditLog::getCreateTime, beforeDate));
            result.put("auditLog", (long) deletedAuditLogs);
            
            // 清理系统日志
            long systemLogCount = systemLogMapper.selectCount(new LambdaQueryWrapper<SystemLog>().lt(SystemLog::getCreateTime, beforeDate));
            int deletedSystemLogs = systemLogMapper.delete(new LambdaQueryWrapper<SystemLog>().lt(SystemLog::getCreateTime, beforeDate));
            result.put("systemLog", (long) deletedSystemLogs);
            
            // 清理登录日志
            long loginLogCount = loginLogMapper.selectCount(new LambdaQueryWrapper<LoginLog>().lt(LoginLog::getLoginTime, beforeDate));
            int deletedLoginLogs = loginLogMapper.delete(new LambdaQueryWrapper<LoginLog>().lt(LoginLog::getLoginTime, beforeDate));
            result.put("loginLog", (long) deletedLoginLogs);
            
            logger.info("清理过期日志完成，审计日志：{}/{}，系统日志：{}/{}，登录日志：{}/{}",
                    deletedAuditLogs, auditLogCount,
                    deletedSystemLogs, systemLogCount,
                    deletedLoginLogs, loginLogCount);
        } catch (Exception e) {
            logger.error("清理过期日志失败", e);
            throw new RuntimeException("清理过期日志失败", e);
        }

        return result;
    }

    @Override
    public Map<String, Map<String, Long>> getLogStatistics(LocalDateTime startTime) {
        Map<String, Map<String, Long>> statistics = new HashMap<>();

        try {
            // 统计审计日志
            long auditLogTotal = auditLogMapper.selectCount(new LambdaQueryWrapper<AuditLog>().ge(AuditLog::getCreateTime, startTime));
            long auditLogSuccess = auditLogMapper.selectCount(new LambdaQueryWrapper<AuditLog>().ge(AuditLog::getCreateTime, startTime).eq(AuditLog::getStatus, 1));
            Map<String, Long> auditStats = new HashMap<>();
            auditStats.put("total", auditLogTotal);
            auditStats.put("success", auditLogSuccess);
            auditStats.put("fail", auditLogTotal - auditLogSuccess);
            
            // 统计系统日志
            long systemLogTotal = systemLogMapper.selectCount(new LambdaQueryWrapper<SystemLog>().ge(SystemLog::getCreateTime, startTime));
            long systemLogError = systemLogMapper.selectCount(new LambdaQueryWrapper<SystemLog>().ge(SystemLog::getCreateTime, startTime).eq(SystemLog::getStatus, 0));
            Map<String, Long> systemStats = new HashMap<>();
            systemStats.put("total", systemLogTotal);
            systemStats.put("error", systemLogError);
            systemStats.put("normal", systemLogTotal - systemLogError);
            
            // 统计登录日志
            long loginLogTotal = loginLogMapper.selectCount(new LambdaQueryWrapper<LoginLog>().ge(LoginLog::getLoginTime, startTime));
            long loginLogSuccess = loginLogMapper.selectCount(new LambdaQueryWrapper<LoginLog>().ge(LoginLog::getLoginTime, startTime).eq(LoginLog::getStatus, 1));
            Map<String, Long> loginStats = new HashMap<>();
            loginStats.put("total", loginLogTotal);
            loginStats.put("success", loginLogSuccess);
            loginStats.put("fail", loginLogTotal - loginLogSuccess);

            statistics.put("auditLog", auditStats);
            statistics.put("systemLog", systemStats);
            statistics.put("loginLog", loginStats);
        } catch (Exception e) {
            logger.error("获取日志统计信息失败", e);
            throw new RuntimeException("获取日志统计信息失败", e);
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
            return LogUtils.truncate(jsonParams, 1000); // 限制长度
        } catch (Exception e) {
            logger.warn("获取请求参数失败", e);
            return null;
        }
    }

    @Override
    public List<AuditLogVO> queryAuditLogs(AuditLogQueryDTO queryDTO) {
        LambdaQueryWrapper<AuditLog> wrapper = QueryWrapperUtils.buildAuditLogQueryWrapper(queryDTO);
        List<AuditLog> logs = auditLogMapper.selectList(wrapper);
        return logs.stream()
                .map(log -> {
                    AuditLogVO vo = new AuditLogVO();
                    BeanUtils.copyProperties(log, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemLogVO> querySystemLogs(SystemLogQueryDTO queryDTO) {
        LambdaQueryWrapper<SystemLog> wrapper = QueryWrapperUtils.buildSystemLogQueryWrapper(queryDTO);
        List<SystemLog> logs = systemLogMapper.selectList(wrapper);
        return logs.stream()
                .map(log -> {
                    SystemLogVO vo = new SystemLogVO();
                    BeanUtils.copyProperties(log, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LoginLogVO> queryLoginLogs(LoginLogQueryDTO queryDTO) {
        LambdaQueryWrapper<LoginLog> wrapper = QueryWrapperUtils.buildLoginLogQueryWrapper(queryDTO);
        List<LoginLog> logs = loginLogMapper.selectList(wrapper);
        return logs.stream()
                .map(log -> {
                    LoginLogVO vo = new LoginLogVO();
                    BeanUtils.copyProperties(log, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public int cleanAuditLogs(Integer days) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(days);
        LambdaQueryWrapper<AuditLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(AuditLog::getCreateTime, threshold);
        return auditLogMapper.delete(wrapper);
    }

    @Override
    public int cleanAllLogs(Integer days) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(days);
        
        LambdaQueryWrapper<AuditLog> auditWrapper = new LambdaQueryWrapper<>();
        auditWrapper.lt(AuditLog::getCreateTime, threshold);
        int auditCount = auditLogMapper.delete(auditWrapper);

        LambdaQueryWrapper<SystemLog> systemWrapper = new LambdaQueryWrapper<>();
        systemWrapper.lt(SystemLog::getCreateTime, threshold);
        int systemCount = systemLogMapper.delete(systemWrapper);

        LambdaQueryWrapper<LoginLog> loginWrapper = new LambdaQueryWrapper<>();
        loginWrapper.lt(LoginLog::getLoginTime, threshold);
        int loginCount = loginLogMapper.delete(loginWrapper);

        return auditCount + systemCount + loginCount;
    }
} 