package com.biou.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import com.biou.entity.SystemLog;
import com.biou.service.LogService;
import com.biou.util.SpringContextUtils;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 数据库日志Appender
 * 将应用日志保存到数据库
 *
 * @author biou
 * @since 2025-01-07
 */
public class DatabaseLogAppender extends AppenderBase<ILoggingEvent> {

    private LogService logService;

    @Override
    public void start() {
        super.start();
        // 延迟获取Service，避免循环依赖
    }

    @Override
    protected void append(ILoggingEvent event) {
        try {
            // 延迟初始化LogService
            if (logService == null) {
                logService = SpringContextUtils.getBean(LogService.class);
                if (logService == null) {
                    return; // 如果无法获取服务，跳过记录
                }
            }

            SystemLog systemLog = convertToSystemLog(event);
            logService.saveSystemLog(systemLog);
        } catch (Exception e) {
            // 不能在这里使用logger，否则会造成循环调用
            System.err.println("保存系统日志失败: " + e.getMessage());
        }
    }

    /**
     * 将ILoggingEvent转换为SystemLog
     */
    private SystemLog convertToSystemLog(ILoggingEvent event) {
        SystemLog systemLog = new SystemLog();
        
        // 基本信息
        systemLog.setLevel(event.getLevel().toString());
        systemLog.setLoggerName(event.getLoggerName());
        systemLog.setMessage(event.getFormattedMessage());
        systemLog.setThreadName(event.getThreadName());
        systemLog.setCreateTime(LocalDateTime.ofInstant(
            Instant.ofEpochMilli(event.getTimeStamp()), ZoneId.systemDefault()));
        
        // 链路追踪ID
        String traceId = MDC.get("traceId");
        if (StringUtils.hasText(traceId)) {
            systemLog.setTraceId(traceId);
        }
        
        // 调用者信息
        StackTraceElement[] callerData = event.getCallerData();
        if (callerData != null && callerData.length > 0) {
            StackTraceElement caller = callerData[0];
            systemLog.setClassName(caller.getClassName());
            systemLog.setMethodName(caller.getMethodName());
            systemLog.setLineNumber(caller.getLineNumber());
        }
        
        // 异常信息
        ThrowableProxy throwableProxy = (ThrowableProxy) event.getThrowableProxy();
        if (throwableProxy != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwableProxy.getThrowable().printStackTrace(pw);
            systemLog.setException(sw.toString());
        }
        
        return systemLog;
    }
} 