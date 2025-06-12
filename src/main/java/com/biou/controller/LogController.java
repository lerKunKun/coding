package com.biou.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biou.common.ApiResponse;
import com.biou.dto.AuditLogQueryDTO;
import com.biou.dto.LoginLogQueryDTO;
import com.biou.dto.SystemLogQueryDTO;
import com.biou.service.LogService;
import com.biou.vo.AuditLogVO;
import com.biou.vo.LoginLogVO;
import com.biou.vo.SystemLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Map;

/**
 * 日志管理控制器
 *
 * @author biou
 * @since 2025-01-07
 */
@RestController
@RequestMapping("/api/log")
@Validated
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 分页查询审计日志
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @PostMapping("/audit/page")
    public ApiResponse<Page<AuditLogVO>> pageAuditLog(@Valid @RequestBody AuditLogQueryDTO queryDTO) {
        Page<AuditLogVO> result = logService.pageAuditLog(queryDTO);
        return ApiResponse.success(result);
    }

    /**
     * 分页查询系统日志
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @PostMapping("/system/page")
    public ApiResponse<Page<SystemLogVO>> pageSystemLog(@Valid @RequestBody SystemLogQueryDTO queryDTO) {
        Page<SystemLogVO> result = logService.pageSystemLog(queryDTO);
        return ApiResponse.success(result);
    }

    /**
     * 分页查询登录日志
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @PostMapping("/login/page")
    public ApiResponse<Page<LoginLogVO>> pageLoginLog(@Valid @RequestBody LoginLogQueryDTO queryDTO) {
        Page<LoginLogVO> result = logService.pageLoginLog(queryDTO);
        return ApiResponse.success(result);
    }

    /**
     * 清理过期日志
     *
     * @param retentionDays 保留天数
     * @return 清理结果
     */
    @DeleteMapping("/clean")
    public ApiResponse<Map<String, Long>> cleanExpiredLogs(
            @RequestParam @Min(value = 1, message = "保留天数不能少于1天") 
            @Max(value = 3650, message = "保留天数不能超过10年") Integer retentionDays) {
        Map<String, Long> result = logService.cleanExpiredLogs(retentionDays);
        return ApiResponse.success(result);
    }

    /**
     * 获取日志统计信息
     *
     * @param days 统计天数
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public ApiResponse<Map<String, Object>> getLogStatistics(
            @RequestParam(defaultValue = "7") @Min(value = 1, message = "统计天数不能少于1天") 
            @Max(value = 365, message = "统计天数不能超过1年") Integer days) {
        Map<String, Object> result = logService.getLogStatistics(days);
        return ApiResponse.success(result);
    }
} 