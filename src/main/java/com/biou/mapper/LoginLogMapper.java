package com.biou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biou.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 登录日志Mapper接口
 *
 * @author biou
 * @since 2025-01-07
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    /**
     * 清理指定日期之前的登录日志
     *
     * @param beforeDate 指定日期
     * @return 删除的记录数
     */
    int deleteByLoginTimeBefore(@Param("beforeDate") LocalDateTime beforeDate);

    /**
     * 统计指定日期之前的登录日志数量
     *
     * @param beforeDate 指定日期
     * @return 记录数量
     */
    long countByLoginTimeBefore(@Param("beforeDate") LocalDateTime beforeDate);
} 