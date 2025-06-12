package com.biou.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biou.dto.AuditLogQueryDTO;
import com.biou.dto.LoginLogQueryDTO;
import com.biou.dto.SystemLogQueryDTO;
import com.biou.project.vo.Result;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 日志管理控制器
 *
 * @author Jax
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
    public Result<Page<AuditLogVO>> pageAuditLog(@Valid @RequestBody AuditLogQueryDTO queryDTO) {
        Page<AuditLogVO> result = logService.pageAuditLog(queryDTO);
        return Result.success(result);
    }

    /**
     * 分页查询系统日志
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @PostMapping("/system/page")
    public Result<Page<SystemLogVO>> pageSystemLog(@Valid @RequestBody SystemLogQueryDTO queryDTO) {
        Page<SystemLogVO> result = logService.pageSystemLog(queryDTO);
        return Result.success(result);
    }

    /**
     * 分页查询登录日志
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @PostMapping("/login/page")
    public Result<Page<LoginLogVO>> pageLoginLog(@Valid @RequestBody LoginLogQueryDTO queryDTO) {
        Page<LoginLogVO> result = logService.pageLoginLog(queryDTO);
        return Result.success(result);
    }

    /**
     * 清理过期日志
     *
     * @param retentionDays 保留天数
     * @return 清理结果
     */
    @DeleteMapping("/clean")
    public Result<Map<String, Long>> cleanExpiredLogs(
            @RequestParam @Min(value = 1, message = "保留天数不能少于1天") 
            @Max(value = 3650, message = "保留天数不能超过10年") Integer retentionDays) {
        Map<String, Long> result = logService.cleanExpiredLogs(retentionDays);
        return Result.success(result);
    }

    /**
     * 获取日志统计信息
     *
     * @param days 统计天数
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Map<String, Long>>> getLogStatistics(
            @RequestParam(defaultValue = "7") @Min(value = 1, message = "统计天数不能少于1天") 
            @Max(value = 365, message = "统计天数不能超过1年") Integer days) {
        Map<String, Map<String, Long>> result = logService.getLogStatistics(LocalDateTime.now().minusDays(days));
        return Result.success(result);
    }

    /**
     * 查询审计日志
     *
     * @param queryDTO 查询条件
     * @return 审计日志列表
     */
    @PostMapping("/audit")
    public Result<List<AuditLogVO>> queryAuditLogs(@RequestBody AuditLogQueryDTO queryDTO) {
        List<AuditLogVO> logs = logService.queryAuditLogs(queryDTO);
        return Result.success(logs);
    }

    /**
     * 查询系统日志
     *
     * @param queryDTO 查询条件
     * @return 系统日志列表
     */
    @PostMapping("/system")
    public Result<List<SystemLogVO>> querySystemLogs(@RequestBody SystemLogQueryDTO queryDTO) {
        List<SystemLogVO> logs = logService.querySystemLogs(queryDTO);
        return Result.success(logs);
    }

    /**
     * 查询登录日志
     *
     * @param queryDTO 查询条件
     * @return 登录日志列表
     */
    @PostMapping("/login")
    public Result<List<LoginLogVO>> queryLoginLogs(@RequestBody LoginLogQueryDTO queryDTO) {
        List<LoginLogVO> logs = logService.queryLoginLogs(queryDTO);
        return Result.success(logs);
    }

    /**
     * 清理审计日志
     *
     * @param days 保留天数
     * @return 清理结果
     */
    @DeleteMapping("/audit/clean/{days}")
    public Result<Integer> cleanAuditLogs(@PathVariable Integer days) {
        int count = logService.cleanAuditLogs(days);
        return Result.success(count);
    }

    /**
     * 清理所有类型的日志
     *
     * @param days 保留天数
     * @return 清理结果
     */
    @DeleteMapping("/clean/{days}")
    public Result<Integer> cleanAllLogs(@PathVariable Integer days) {
        int count = logService.cleanAllLogs(days);
        return Result.success(count);
    }
} 