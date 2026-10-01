package com.automation.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * WaitUtils - Utility class for wait operations
 */
public class WaitUtils {
    private static final LoggerUtils logger = LoggerUtils.getInstance();

    /**
     * Wait for specified seconds
     */
    public static void waitForSeconds(int seconds) {
        logger.info("Waiting for " + seconds + " seconds");
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            logger.error("Wait interrupted: " + e.getMessage());
        }
    }

    /**
     * Wait until condition is true
     */
    public static void waitUntil(Condition condition, int timeoutInSeconds) {
        logger.info("Waiting for condition with timeout: " + timeoutInSeconds + " seconds");
        long startTime = System.currentTimeMillis();
        while (!condition.isTrue()) {
            if (System.currentTimeMillis() - startTime > timeoutInSeconds * 1000L) {
                logger.error("Condition not met within timeout");
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.error("Wait interrupted: " + e.getMessage());
                break;
            }
        }
    }

    /**
     * Retry operation with specified attempts
     * @throws Exception 
     */
    public static <T> T retry(Operation<T> operation, int maxAttempts) throws Exception {
        logger.info("Retrying operation with max attempts: " + maxAttempts);
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                logger.info("Attempt " + attempt + " of " + maxAttempts);
                return operation.execute();
            } catch (Exception e) {
                logger.warn("Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt == maxAttempts) {
                    logger.error("All retry attempts failed");
                    throw e;
                }
                waitForSeconds(2);
            }
        }
        return null;
    }

    /**
     * Functional interface for conditions
     */
    public interface Condition {
        boolean isTrue();
    }

    /**
     * Functional interface for operations
     */
    public interface Operation<T> {
        T execute() throws Exception;
    }
}
