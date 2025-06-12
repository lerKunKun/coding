package com.biou.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biou.constant.LogConstants;
import com.biou.dto.AuditLogQueryDTO;
import com.biou.entity.AuditLog;
import com.biou.mapper.AuditLogMapper;
import com.biou.repository.AuditLogRepository;
import com.biou.util.QueryWrapperUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 审计日志Repository实现类
 *
 * @author Jax
 * @since 2025-01-07
 */
@Repository
public class AuditLogRepositoryImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements AuditLogRepository {

    @Override
    public int deleteByCreateTimeBefore(LocalDateTime beforeDate) {
        return this.baseMapper.deleteByCreateTimeBefore(beforeDate);
    }

    @Override
    public long countByCreateTimeBefore(LocalDateTime beforeDate) {
        return this.baseMapper.countByCreateTimeBefore(beforeDate);
    }

    @Override
    public long countByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<AuditLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(AuditLog::getCreateTime, startTime);
        if (endTime != null) {
            wrapper.le(AuditLog::getCreateTime, endTime);
        }
        return this.count(wrapper);
    }

    @Override
    public long countSuccessByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<AuditLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(AuditLog::getCreateTime, startTime);
        if (endTime != null) {
            wrapper.le(AuditLog::getCreateTime, endTime);
        }
        wrapper.eq(AuditLog::getStatus, LogConstants.Status.SUCCESS);
        return this.count(wrapper);
    }

    @Override
    public Page<AuditLog> pageByQuery(Page<AuditLog> page, AuditLogQueryDTO queryDTO) {
        LambdaQueryWrapper<AuditLog> wrapper = QueryWrapperUtils.buildAuditLogQueryWrapper(queryDTO);
        return this.page(page, wrapper);
    }
} 