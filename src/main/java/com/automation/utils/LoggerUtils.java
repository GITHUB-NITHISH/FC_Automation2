package com.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * LoggerUtils - Singleton pattern for logging across the framework
 */
public class LoggerUtils {
    private static LoggerUtils instance;
    private static Logger logger;

    private LoggerUtils() {
        logger = LogManager.getLogger(LoggerUtils.class);
    }

    /**
     * Get singleton instance of LoggerUtils
     */
    public static synchronized LoggerUtils getInstance() {
        if (instance == null) {
            instance = new LoggerUtils();
        }
        return instance;
    }

    public void info(String message) {
        String browserName = ConfigurationManager.getBrowserName();
        // Use "UNKNOWN" if browserName is somehow null to avoid crashes
        String browser = (browserName != null) ? browserName.toUpperCase() : "INIT";
        
        String threadInfo =  Thread.currentThread().getName() + "-"  + Thread.currentThread().threadId();
        
        String formattedMessage = "[" + browser + "][" + threadInfo + "] " + message;
        
        // This will definitely show in the console even if Log4j is failing
        System.out.println(formattedMessage);
        
        logger.info(formattedMessage);    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void error(String message) {
        String browser = ConfigurationManager.getBrowserName().toUpperCase();
        String thread = Thread.currentThread().getName();
        logger.error("[" + browser + "][" + thread + "] " + message);
    }

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public void fatal(String message) {
        logger.fatal(message);
    }
}
