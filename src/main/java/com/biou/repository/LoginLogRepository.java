package com.biou.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.biou.dto.LoginLogQueryDTO;
import com.biou.entity.LoginLog;

import java.time.LocalDateTime;

/**
 * 登录日志Repository接口
 *
 * @author biou
 * @since 2025-01-07
 */
public interface LoginLogRepository extends IService<LoginLog> {

    /**
     * 清理指定日期之前的登录日志
     *
     * @param beforeDate 指定日期
     * @return 删除的记录数
     */
    int deleteByLoginTimeBefore(LocalDateTime beforeDate);

    /**
     * 统计指定日期之前的登录日志数量
     *
     * @param beforeDate 指定日期
     * @return 记录数量
     */
    long countByLoginTimeBefore(LocalDateTime beforeDate);

    /**
     * 统计指定时间范围内的登录日志总数
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 记录数量
     */
    long countByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计指定时间范围内的成功登录日志数量
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 记录数量
     */
    long countSuccessByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 分页查询登录日志
     *
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<LoginLog> pageByQuery(Page<LoginLog> page, LoginLogQueryDTO queryDTO);
} 