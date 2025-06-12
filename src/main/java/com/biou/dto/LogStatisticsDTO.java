package com.biou.dto;

import java.time.LocalDateTime;

/**
 * 日志统计查询DTO
 */
public class LogStatisticsDTO {
    
    /**
     * 统计开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 统计结束时间
     */
    private LocalDateTime endTime;
    
    public LogStatisticsDTO() {
    }
    
    public LogStatisticsDTO(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public String toString() {
        return "LogStatisticsDTO{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
} 