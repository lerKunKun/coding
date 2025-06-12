package com.biou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biou.entity.AuditLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 审计日志Mapper接口
 *
 * @author Jax
 * @since 2025-01-07
 */
@Mapper
public interface AuditLogMapper extends BaseMapper<AuditLog> {

    /**
     * 清理指定日期之前的审计日志
     *
     * @param beforeDate 指定日期
     * @return 删除的记录数
     */
    int deleteByCreateTimeBefore(@Param("beforeDate") LocalDateTime beforeDate);

    /**
     * 统计指定日期之前的审计日志数量
     *
     * @param beforeDate 指定日期
     * @return 记录数量
     */
    long countByCreateTimeBefore(@Param("beforeDate") LocalDateTime beforeDate);
} 