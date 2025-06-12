package com.biou.util;

import com.alibaba.fastjson2.JSON;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * 日志工具类
 *
 * @author Jax
 * @since 2025-01-07
 */
public class LogUtils {

    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST = "127.0.0.1";
    private static final String SEPARATOR = ",";

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (!StringUtils.isNotBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.isNotBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.isNotBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (!StringUtils.isNotBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (!StringUtils.isNotBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCALHOST.equals(ipAddress)) {
                try {
                    InetAddress inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    logger.error("获取IP地址失败", e);
                }
            }
        }

        if (StringUtils.isNotBlank(ipAddress) && ipAddress.contains(SEPARATOR)) {
            ipAddress = ipAddress.split(SEPARATOR)[0];
        }

        return ipAddress;
    }

    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (!StringUtils.isNotBlank(userAgent)) {
            return UNKNOWN;
        }
        return userAgent;
    }

    public static String getBrowser(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (!StringUtils.isNotBlank(userAgent)) {
            return UNKNOWN;
        }
        return UserAgent.parseUserAgentString(userAgent).getBrowser().getName();
    }

    public static String getOs(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (!StringUtils.isNotBlank(userAgent)) {
            return UNKNOWN;
        }
        return UserAgent.parseUserAgentString(userAgent).getOperatingSystem().getName();
    }

    public static String getLocation(HttpServletRequest request) {
        String ipAddress = getIpAddress(request);
        if (!StringUtils.isNotBlank(ipAddress) || LOCALHOST.equals(ipAddress) || "localhost".equals(ipAddress)) {
            return "内网IP";
        }
        try {
            // TODO: 调用IP地址查询API获取地理位置
            return "未知位置";
        } catch (Exception e) {
            logger.error("获取地理位置失败", e);
            return "未知位置";
        }
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isNotBlank(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String truncate(String text, int maxLength) {
        if (!StringUtils.isNotBlank(text) || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }

    public static String formatParams(Object[] params) {
        if (params == null || params.length == 0) {
            return "";
        }
        try {
            return JSON.toJSONString(params);
        } catch (Exception e) {
            logger.error("格式化参数失败", e);
            return "参数格式化失败";
        }
    }

    public static String formatResult(Object result) {
        if (result == null) {
            return "";
        }
        try {
            return JSON.toJSONString(result);
        } catch (Exception e) {
            logger.error("格式化结果失败", e);
            return "结果格式化失败";
        }
    }

    public static String formatHeaders(HttpServletRequest request) {
        Map<String, String[]> headerMap = request.getParameterMap();
        try {
            return JSON.toJSONString(headerMap);
        } catch (Exception e) {
            logger.error("格式化请求头失败", e);
            return "请求头格式化失败";
        }
    }
} 