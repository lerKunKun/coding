package com.biou.vo;

import java.time.LocalDateTime;

/**
 * 系统日志VO
 *
 * @author Jax
 * @since 2025-01-07
 */
public class SystemLogVO {

    /**
     * 主键ID
     */
    private Long id;

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
     * 日志消息
     */
    private String message;

    /**
     * 异常信息
     */
    private String exception;

    /**
     * 线程名称
     */
    private String threadName;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 行号
     */
    private Integer lineNumber;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    public SystemLogVO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SystemLogVO{" +
                "id=" + id +
                ", traceId='" + traceId + '\'' +
                ", level='" + level + '\'' +
                ", loggerName='" + loggerName + '\'' +
                ", message='" + message + '\'' +
                ", exception='" + exception + '\'' +
                ", threadName='" + threadName + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", lineNumber=" + lineNumber +
                ", createTime=" + createTime +
                '}';
    }
} 