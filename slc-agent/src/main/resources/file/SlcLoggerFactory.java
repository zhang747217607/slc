package com.slc.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 日志
 *
 * @author zrh
 */
public class SlcLoggerFactory {

    private static final String SERVICE_LOG_PATH = "D:\\logs\\slc_service.log";
    private static final String SERVICE_LOG_PATH_HISTORY = "D:\\logs\\slc_service.log.%d";
    private static Logger service_logger;


    private static final String HTTP_LOG_PATH = "D:\\logs\\slc_http.log";
    private static final String HTTP_LOG_PATH_HISTORY = "D:\\logs\\slc_http.log.%d";
    private static Logger http_logger;

    public static Logger getServiceLogger() {
        return service_logger;
    }

    public static Logger getHttpLogger() {
        return http_logger;
    }


    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        //service logger
        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setContext(loggerContext);
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setName("serviceLogAppender");
        rollingFileAppender.setFile(SERVICE_LOG_PATH);

        TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy<>();
        rollingPolicy.setFileNamePattern(SERVICE_LOG_PATH_HISTORY);
        rollingPolicy.setMaxHistory(10);
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.start();
        rollingFileAppender.setRollingPolicy(rollingPolicy);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%msg%n");
        encoder.setCharset(Charset.forName(StandardCharsets.UTF_8.toString()));
        encoder.setContext(loggerContext);
        encoder.start();
        rollingFileAppender.setEncoder(encoder);

        rollingFileAppender.start();

        ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger("service-logger");
        rootLogger.setLevel(Level.INFO);
        rootLogger.addAppender(rollingFileAppender);
        service_logger = rootLogger;

        //http logger
        RollingFileAppender<ILoggingEvent> rollingFileAppender1 = new RollingFileAppender<>();
        rollingFileAppender1.setContext(loggerContext);
        rollingFileAppender1.setAppend(true);
        rollingFileAppender1.setName("httpLogAppender");
        rollingFileAppender1.setFile(HTTP_LOG_PATH);

        TimeBasedRollingPolicy rollingPolicy1 = new TimeBasedRollingPolicy<>();
        rollingPolicy1.setFileNamePattern(HTTP_LOG_PATH_HISTORY);
        rollingPolicy1.setMaxHistory(10);
        rollingPolicy1.setContext(loggerContext);
        rollingPolicy1.setParent(rollingFileAppender1);
        rollingPolicy1.start();
        rollingFileAppender1.setRollingPolicy(rollingPolicy1);

        PatternLayoutEncoder encoder1 = new PatternLayoutEncoder();
        encoder1.setPattern("%msg%n");
        encoder1.setCharset(Charset.forName(StandardCharsets.UTF_8.toString()));
        encoder1.setContext(loggerContext);
        encoder1.start();
        rollingFileAppender1.setEncoder(encoder1);

        rollingFileAppender1.start();

        ch.qos.logback.classic.Logger rootLogger1 = loggerContext.getLogger("http-logger");
        rootLogger1.setLevel(Level.INFO);
        rootLogger1.addAppender(rollingFileAppender1);
        http_logger = rootLogger1;
    }

}