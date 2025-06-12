package com.biou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biou.entity.SystemLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 系统日志Mapper接口
 *
 * @author biou
 * @since 2025-01-07
 */
@Mapper
public interface SystemLogMapper extends BaseMapper<SystemLog> {

    /**
     * 清理指定日期之前的系统日志
     *
     * @param beforeDate 指定日期
     * @return 删除的记录数
     */
    int deleteByCreateTimeBefore(@Param("beforeDate") LocalDateTime beforeDate);

    /**
     * 统计指定日期之前的系统日志数量
     *
     * @param beforeDate 指定日期
     * @return 记录数量
     */
    long countByCreateTimeBefore(@Param("beforeDate") LocalDateTime beforeDate);
} 