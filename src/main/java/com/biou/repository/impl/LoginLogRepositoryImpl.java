package com.biou.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biou.constant.LogConstants;
import com.biou.dto.LoginLogQueryDTO;
import com.biou.entity.LoginLog;
import com.biou.mapper.LoginLogMapper;
import com.biou.repository.LoginLogRepository;
import com.biou.util.QueryWrapperUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 登录日志Repository实现类
 *
 * @author biou
 * @since 2025-01-07
 */
@Repository
public class LoginLogRepositoryImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogRepository {

    @Override
    public int deleteByLoginTimeBefore(LocalDateTime beforeDate) {
        return this.baseMapper.deleteByLoginTimeBefore(beforeDate);
    }

    @Override
    public long countByLoginTimeBefore(LocalDateTime beforeDate) {
        return this.baseMapper.countByLoginTimeBefore(beforeDate);
    }

    @Override
    public long countByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(LoginLog::getLoginTime, startTime);
        if (endTime != null) {
            wrapper.le(LoginLog::getLoginTime, endTime);
        }
        return this.count(wrapper);
    }

    @Override
    public long countSuccessByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(LoginLog::getLoginTime, startTime);
        if (endTime != null) {
            wrapper.le(LoginLog::getLoginTime, endTime);
        }
        wrapper.eq(LoginLog::getStatus, LogConstants.Status.SUCCESS);
        return this.count(wrapper);
    }

    @Override
    public Page<LoginLog> pageByQuery(Page<LoginLog> page, LoginLogQueryDTO queryDTO) {
        LambdaQueryWrapper<LoginLog> wrapper = QueryWrapperUtils.buildLoginLogQueryWrapper(queryDTO);
        return this.page(page, wrapper);
    }
} 