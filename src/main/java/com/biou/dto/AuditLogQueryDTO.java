package com.biou.dto;

import com.biou.project.dto.PageQueryDTO;
import java.time.LocalDateTime;

/**
 * 审计日志查询DTO
 *
 * @author Jax
 * @since 2025-01-07
 */
public class AuditLogQueryDTO extends PageQueryDTO {

    /**
     * 操作用户名
     */
    private String username;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 操作模块
     */
    private String module;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 操作状态
     */
    private Integer status;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    public AuditLogQueryDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        return "AuditLogQueryDTO{" +
                "username='" + username + '\'' +
                ", operationType='" + operationType + '\'' +
                ", businessType='" + businessType + '\'' +
                ", module='" + module + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", current=" + getCurrent() +
                ", size=" + getSize() +
                '}';
    }
} 