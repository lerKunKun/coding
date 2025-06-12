package com.biou.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.biou.dto.SystemLogQueryDTO;
import com.biou.entity.SystemLog;

import java.time.LocalDateTime;

/**
 * 系统日志Repository接口
 *
 * @author biou
 * @since 2025-01-07
 */
public interface SystemLogRepository extends IService<SystemLog> {

    /**
     * 清理指定日期之前的系统日志
     *
     * @param beforeDate 指定日期
     * @return 删除的记录数
     */
    int deleteByCreateTimeBefore(LocalDateTime beforeDate);

    /**
     * 统计指定日期之前的系统日志数量
     *
     * @param beforeDate 指定日期
     * @return 记录数量
     */
    long countByCreateTimeBefore(LocalDateTime beforeDate);

    /**
     * 统计指定时间范围内的系统日志总数
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 记录数量
     */
    long countByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计指定时间范围内的错误系统日志数量
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 记录数量
     */
    long countErrorByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 分页查询系统日志
     *
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<SystemLog> pageByQuery(Page<SystemLog> page, SystemLogQueryDTO queryDTO);
} 