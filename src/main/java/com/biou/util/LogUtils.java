package com.biou.util;

import com.biou.entity.AuditLog;
import com.biou.entity.LoginLog;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日志工具类
 *
 * @author Jax
 * @since 2025-01-07
 */
public class LogUtils {

    private static final String UNKNOWN = "unknown";

    /**
     * 获取客户端IP地址
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (!StringUtils.hasText(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (!StringUtils.hasText(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (!StringUtils.hasText(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // 如果通过多个代理，第一个IP为客户端真实IP
        if (StringUtils.hasText(ipAddress) && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }

    /**
     * 获取用户代理信息
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    /**
     * 解析浏览器信息
     */
    public static String parseBrowser(String userAgent) {
        if (!StringUtils.hasText(userAgent)) {
            return UNKNOWN;
        }
        
        if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        } else if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("Opera")) {
            return "Opera";
        } else if (userAgent.contains("Internet Explorer")) {
            return "Internet Explorer";
        } else {
            return UNKNOWN;
        }
    }

    /**
     * 解析操作系统信息
     */
    public static String parseOS(String userAgent) {
        if (!StringUtils.hasText(userAgent)) {
            return UNKNOWN;
        }
        
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Mac")) {
            return "Mac OS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else if (userAgent.contains("Android")) {
            return "Android";
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            return "iOS";
        } else {
            return UNKNOWN;
        }
    }

    /**
     * 获取地理位置（简单实现，可以集成第三方IP地址库）
     */
    public static String getLocation(String ipAddress) {
        if (!StringUtils.hasText(ipAddress) || "127.0.0.1".equals(ipAddress) || "localhost".equals(ipAddress)) {
            return "本地";
        }
        
        // 简单的内网IP判断
        if (isInternalIp(ipAddress)) {
            return "内网";
        }
        
        // 这里可以集成第三方IP地址库，如IP2Location、GeoLite2等
        return "未知地区";
    }

    /**
     * 判断是否为内网IP
     */
    private static boolean isInternalIp(String ip) {
        if (!StringUtils.hasText(ip)) {
            return false;
        }
        
        Pattern pattern = Pattern.compile(
            "^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$"
        );
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * 创建审计日志实例
     */
    public static AuditLog createAuditLog(Long userId, String username, String operationType, String businessType,
                                          String module, String description, String method, String requestUrl,
                                          String requestMethod, String requestParams, String responseData,
                                          String ipAddress, String userAgent, Integer status, String errorMessage,
                                          Long executionTime) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUserId(userId);
        auditLog.setUsername(username);
        auditLog.setOperationType(operationType);
        auditLog.setBusinessType(businessType);
        auditLog.setModule(module);
        auditLog.setDescription(description);
        auditLog.setMethod(method);
        auditLog.setRequestUrl(requestUrl);
        auditLog.setRequestMethod(requestMethod);
        auditLog.setRequestParams(requestParams);
        auditLog.setResponseData(responseData);
        auditLog.setIpAddress(ipAddress);
        auditLog.setUserAgent(userAgent);
        auditLog.setStatus(status);
        auditLog.setErrorMessage(errorMessage);
        auditLog.setExecutionTime(executionTime);
        auditLog.setCreateTime(LocalDateTime.now());
        return auditLog;
    }

    /**
     * 创建登录日志实例
     */
    public static LoginLog createLoginLog(Long userId, String username, String loginType, String ipAddress,
                                          String userAgent, Integer status, String message) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setUsername(username);
        loginLog.setLoginType(loginType);
        loginLog.setIpAddress(ipAddress);
        loginLog.setUserAgent(userAgent);
        loginLog.setLocation(getLocation(ipAddress));
        loginLog.setBrowser(parseBrowser(userAgent));
        loginLog.setOs(parseOS(userAgent));
        loginLog.setStatus(status);
        loginLog.setMessage(message);
        loginLog.setLoginTime(LocalDateTime.now());
        return loginLog;
    }

    /**
     * 截断长文本
     */
    public static String truncateText(String text, int maxLength) {
        if (!StringUtils.hasText(text) || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
} 