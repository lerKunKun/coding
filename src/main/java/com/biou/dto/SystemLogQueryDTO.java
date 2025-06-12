package com.biou.dto;

import java.time.LocalDateTime;

/**
 * 系统日志查询DTO
 *
 * @author biou
 * @since 2025-01-07
 */
public class SystemLogQueryDTO extends PageQueryDTO {

    /**
     * 链路追踪ID
     */
    private String traceId;

    /**
     * 日志级别
     */
    private String level;

    /**
     * 日志记录器名称
     */
    private String loggerName;

    /**
     * 类名
     */
    private String className;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 关键字搜索
     */
    private String keyword;

    public SystemLogQueryDTO() {
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "SystemLogQueryDTO{" +
                "traceId='" + traceId + '\'' +
                ", level='" + level + '\'' +
                ", loggerName='" + loggerName + '\'' +
                ", className='" + className + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", keyword='" + keyword + '\'' +
                ", page=" + getPage() +
                ", size=" + getSize() +
                '}';
    }
} 