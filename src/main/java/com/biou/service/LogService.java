package com.biou.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biou.dto.AuditLogQueryDTO;
import com.biou.dto.LoginLogQueryDTO;
import com.biou.dto.SystemLogQueryDTO;
import com.biou.entity.AuditLog;
import com.biou.entity.LoginLog;
import com.biou.entity.SystemLog;
import com.biou.vo.AuditLogVO;
import com.biou.vo.LoginLogVO;
import com.biou.vo.SystemLogVO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 日志服务接口
 *
 * @author Jax
 * @since 2025-01-07
 */
public interface LogService {

    /**
     * 分页查询审计日志
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<AuditLogVO> pageAuditLog(AuditLogQueryDTO queryDTO);

    /**
     * 分页查询系统日志
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<SystemLogVO> pageSystemLog(SystemLogQueryDTO queryDTO);

    /**
     * 分页查询登录日志
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<LoginLogVO> pageLoginLog(LoginLogQueryDTO queryDTO);

    /**
     * 查询审计日志列表
     *
     * @param queryDTO 查询条件
     * @return 审计日志列表
     */
    List<AuditLogVO> queryAuditLogs(AuditLogQueryDTO queryDTO);

    /**
     * 查询系统日志列表
     *
     * @param queryDTO 查询条件
     * @return 系统日志列表
     */
    List<SystemLogVO> querySystemLogs(SystemLogQueryDTO queryDTO);

    /**
     * 查询登录日志列表
     *
     * @param queryDTO 查询条件
     * @return 登录日志列表
     */
    List<LoginLogVO> queryLoginLogs(LoginLogQueryDTO queryDTO);

    /**
     * 清理审计日志
     *
     * @param days 保留天数
     * @return 清理数量
     */
    int cleanAuditLogs(Integer days);

    /**
     * 清理所有类型的日志
     *
     * @param days 保留天数
     * @return 清理数量
     */
    int cleanAllLogs(Integer days);

    /**
     * 保存审计日志
     *
     * @param auditLog 审计日志
     */
    void saveAuditLog(AuditLog auditLog);

    /**
     * 保存系统日志
     *
     * @param systemLog 系统日志
     */
    void saveSystemLog(SystemLog systemLog);

    /**
     * 保存登录日志
     *
     * @param loginLog 登录日志
     */
    void saveLoginLog(LoginLog loginLog);

    /**
     * 记录审计日志
     *
     * @param userId        用户ID
     * @param username      用户名
     * @param operationType 操作类型
     * @param businessType  业务类型
     * @param module        模块
     * @param description   描述
     * @param method       方法名
     * @param requestUrl   请求URL
     * @param requestMethod 请求方法
     * @param requestParams 请求参数
     * @param responseData  响应数据
     * @param request      请求对象
     * @param status       状态
     * @param errorMessage 错误信息
     * @param executionTime 执行时间
     */
    void recordAuditLog(Long userId, String username, String operationType, String businessType,
                        String module, String description, String method, String requestUrl,
                        String requestMethod, String requestParams, String responseData,
                        HttpServletRequest request, Integer status, String errorMessage,
                        Long executionTime);

    /**
     * 记录登录日志
     *
     * @param userId    用户ID
     * @param username  用户名
     * @param loginType 登录类型
     * @param request   请求对象
     * @param status   状态
     * @param message   消息
     */
    void recordLoginLog(Long userId, String username, String loginType, HttpServletRequest request,
                        Integer status, String message);

    /**
     * 记录系统日志
     *
     * @param level      日志级别
     * @param loggerName 日志记录器名称
     * @param message    日志消息
     * @param exception  异常信息
     * @param threadName 线程名称
     * @param className  类名
     * @param methodName 方法名
     * @param lineNumber 行号
     */
    void recordSystemLog(String level, String loggerName, String message, String exception,
                        String threadName, String className, String methodName, Integer lineNumber);

    /**
     * 清理过期日志
     *
     * @param days 保留天数
     * @return 清理结果统计
     */
    Map<String, Long> cleanExpiredLogs(Integer days);

    /**
     * 获取日志统计信息
     *
     * @param startTime 统计开始时间
     * @return 统计信息
     */
    Map<String, Map<String, Long>> getLogStatistics(LocalDateTime startTime);
} 