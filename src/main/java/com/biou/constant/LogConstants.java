package com.biou.constant;

/**
 * 日志常量
 *
 * @author biou
 * @since 2025-01-07
 */
public class LogConstants {

    /**
     * 操作类型
     */
    public static class OperationType {
        public static final String CREATE = "CREATE";
        public static final String UPDATE = "UPDATE";
        public static final String DELETE = "DELETE";
        public static final String QUERY = "QUERY";
        public static final String LOGIN = "LOGIN";
        public static final String LOGOUT = "LOGOUT";
    }

    /**
     * 业务类型
     */
    public static class BusinessType {
        public static final String USER = "USER";
        public static final String ROLE = "ROLE";
        public static final String PERMISSION = "PERMISSION";
        public static final String SYSTEM = "SYSTEM";
        public static final String LOG = "LOG";
    }

    /**
     * 登录类型
     */
    public static class LoginType {
        public static final String LOGIN = "LOGIN";
        public static final String LOGOUT = "LOGOUT";
    }

    /**
     * 日志级别
     */
    public static class LogLevel {
        public static final String DEBUG = "DEBUG";
        public static final String INFO = "INFO";
        public static final String WARN = "WARN";
        public static final String ERROR = "ERROR";
    }

    /**
     * 状态
     */
    public static class Status {
        public static final int FAIL = 0;
        public static final int SUCCESS = 1;
    }
} 