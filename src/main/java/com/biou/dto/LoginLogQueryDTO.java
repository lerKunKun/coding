package com.biou.dto;

import java.time.LocalDateTime;

/**
 * 登录日志查询DTO
 *
 * @author Jax
 * @since 2025-01-07
 */
public class LoginLogQueryDTO extends PageQueryDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 登录状态
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

    public LoginLogQueryDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
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
        return "LoginLogQueryDTO{" +
                "username='" + username + '\'' +
                ", loginType='" + loginType + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", page=" + getPage() +
                ", size=" + getSize() +
                '}';
    }
} 