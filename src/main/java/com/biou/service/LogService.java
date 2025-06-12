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
import java.util.Map;

/**
 * 日志服务接口
 *
 * @author biou
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
     * 记录操作日志
     *
     * @param userId        用户ID
     * @param username      用户名
     * @param operationType 操作类型
     * @param businessType  业务类型
     * @param module        模块
     * @param description   描述
     * @param request       请求对象
     * @param startTime     开始时间
     * @param success       是否成功
     * @param errorMessage  错误信息
     */
    void recordOperationLog(Long userId, String username, String operationType, String businessType,
                            String module, String description, HttpServletRequest request,
                            long startTime, boolean success, String errorMessage);

    /**
     * 记录登录日志
     *
     * @param userId    用户ID
     * @param username  用户名
     * @param loginType 登录类型
     * @param request   请求对象
     * @param success   是否成功
     * @param message   消息
     */
    void recordLoginLog(Long userId, String username, String loginType, HttpServletRequest request,
                        boolean success, String message);

    /**
     * 清理过期日志
     *
     * @param retentionDays 保留天数
     * @return 清理结果统计
     */
    Map<String, Long> cleanExpiredLogs(int retentionDays);

    /**
     * 获取日志统计信息
     *
     * @param days 统计天数
     * @return 统计信息
     */
    Map<String, Object> getLogStatistics(int days);
} 