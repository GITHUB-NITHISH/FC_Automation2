package com.automation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigurationManager - Loads and manages configuration from properties file
 */
public class ConfigurationManager {
    private static final LoggerUtils logger = LoggerUtils.getInstance();
//    private static Properties properties;
    private static Properties properties = new Properties();
    
    private static final InheritableThreadLocal<String> browserThreadLocal = new InheritableThreadLocal<>();

    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        InputStream input = null;
        try {
            // Try 1: Standard ClassLoader (Most common)
            input = ConfigurationManager.class.getClassLoader().getResourceAsStream("config.properties");
            
            // Try 2: Class-relative path
            if (input == null) {
                input = ConfigurationManager.class.getResourceAsStream("/config.properties");
            }
            
            // Try 3: Direct File System path (Fallback for IDEs)
            if (input == null) {
                File configFile = new File("src/main/resources/config/config.properties");
                if (configFile.exists()) {
                    input = new FileInputStream(configFile);
                }
            }

            if (input == null) {
                System.err.println("FATAL: config.properties could not be found anywhere!");
                return;
            }

            properties.load(input);
            System.out.println("SUCCESS: Configuration loaded from config.properties");

        } catch (IOException e) {
            System.err.println("ERROR: Failed to load config.properties: " + e.getMessage());
        } finally {
            if (input != null) {
                try { input.close(); } catch (IOException e) {}
            }
        }
    }


    /**
     * Get property value
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property value with default
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get browser name
     */
    public static String getBrowserName() {
        String browser = browserThreadLocal.get(); 
        if (browser == null || browser.isEmpty()) {
//            browser = getProperty("browser", "chrome");
        	  return getProperty("browser", "chrome");
        }
        return browser;
    }

    public static void setBrowser(String browser) {
        browserThreadLocal.set(browser);
    }
    
    /**
     * Get base URL
     */
    public static String getBaseUrl() {
        String url = properties.getProperty("base.url");
        if (url == null) {
            throw new RuntimeException("CRITICAL ERROR: 'base.url' is missing in your config.properties file!");
        }
        return url;
    }

    /**
     * Get implicit wait timeout
     */
    public static long getImplicitWait() {
        return Long.parseLong(getProperty("implicit.wait", "10"));
    }

    /**
     * Get explicit wait timeout
     */
    public static long getExplicitWait() {
        return Long.parseLong(getProperty("explicit.wait", "15"));
    }

    /**
     * Check if headless mode is enabled
     */
    public static boolean isHeadlessMode() {
        return Boolean.parseBoolean(getProperty("headless.mode", "false"));
    }

    /**
     * Check if screenshot on failure is enabled
     */
    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure", "true"));
    }

    /**
     * Check if video recording is enabled
     */
    public static boolean isVideoRecordingEnabled() {
        return Boolean.parseBoolean(getProperty("video.recording", "false"));
    }

    /**
     * Get report directory
     */
    public static String getReportDirectory() {
        return getProperty("report.directory", "./reports");
    }

    /**
     * Get screenshot directory
     */
    public static String getScreenshotDirectory() {
        return getProperty("screenshot.directory", "./screenshots");
    }
    
    public static boolean isParallelRun() {
        // TestNG parallel threads are named "TestNG-PoolService-X"
        return Thread.currentThread().getName().contains("PoolService");
    }
    
	public static boolean isE2EMode() {
		return Boolean.parseBoolean(getProperty("e2e.mode", "false"));
	}

    
}
