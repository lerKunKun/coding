package com.biou.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biou.constant.LogConstants;
import com.biou.dto.SystemLogQueryDTO;
import com.biou.entity.SystemLog;
import com.biou.mapper.SystemLogMapper;
import com.biou.repository.SystemLogRepository;
import com.biou.util.QueryWrapperUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 系统日志Repository实现类
 *
 * @author biou
 * @since 2025-01-07
 */
@Repository
public class SystemLogRepositoryImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogRepository {

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
        LambdaQueryWrapper<SystemLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(SystemLog::getCreateTime, startTime);
        if (endTime != null) {
            wrapper.le(SystemLog::getCreateTime, endTime);
        }
        return this.count(wrapper);
    }

    @Override
    public long countErrorByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<SystemLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(SystemLog::getCreateTime, startTime);
        if (endTime != null) {
            wrapper.le(SystemLog::getCreateTime, endTime);
        }
        wrapper.eq(SystemLog::getLevel, LogConstants.LogLevel.ERROR);
        return this.count(wrapper);
    }

    @Override
    public Page<SystemLog> pageByQuery(Page<SystemLog> page, SystemLogQueryDTO queryDTO) {
        LambdaQueryWrapper<SystemLog> wrapper = QueryWrapperUtils.buildSystemLogQueryWrapper(queryDTO);
        return this.page(page, wrapper);
    }
} 