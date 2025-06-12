package com.biou.task;

import com.biou.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 日志清理定时任务
 *
 * @author Jax
 * @since 2025-01-07
 */
@Component
public class LogCleanupTask {

    private static final Logger logger = LoggerFactory.getLogger(LogCleanupTask.class);

    @Autowired
    private LogService logService;

    /**
     * 日志保留天数，默认90天
     */
    @Value("${biou.log.retention-days:90}")
    private int retentionDays;

    /**
     * 是否启用自动清理，默认启用
     */
    @Value("${biou.log.auto-cleanup.enabled:true}")
    private boolean autoCleanupEnabled;

    /**
     * 每天凌晨2点执行日志清理任务
     */
    @Scheduled(cron = "${biou.log.auto-cleanup.cron:0 0 2 * * ?}")
    public void cleanupExpiredLogs() {
        if (!autoCleanupEnabled) {
            logger.debug("日志自动清理已禁用，跳过执行");
            return;
        }

        try {
            logger.info("开始执行日志清理任务，保留天数：{}", retentionDays);
            
            Map<String, Long> result = logService.cleanExpiredLogs(retentionDays);
            
            long totalCleaned = result.values().stream().mapToLong(Long::longValue).sum();
            
            logger.info("日志清理任务完成，共清理 {} 条记录，详情：{}", totalCleaned, result);
            
        } catch (Exception e) {
            logger.error("日志清理任务执行失败", e);
        }
    }

    /**
     * 每小时执行一次日志统计（可选）
     */
    @Scheduled(cron = "${biou.log.statistics.cron:0 0 * * * ?}")
    public void logStatistics() {
        try {
            Map<String, Map<String, Long>> statistics = logService.getLogStatistics(LocalDateTime.now().minusDays(1)); // 统计最近1天
            logger.info("日志统计信息：{}", statistics);
        } catch (Exception e) {
            logger.debug("获取日志统计信息失败", e);
        }
    }
} 