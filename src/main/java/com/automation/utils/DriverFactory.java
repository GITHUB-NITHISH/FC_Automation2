package com.automation.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;

/**
 * DriverFactory - Manages WebDriver creation and lifecycle
 */
public class DriverFactory {
    private static final LoggerUtils logger = LoggerUtils.getInstance();
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private static WebDriver sharedDriver = null;

    /**
     * Initialize WebDriver based on browser type
     */
    public static WebDriver initializeDriver(String browserName) {
        // In Sequential (E2E) mode, we use a shared static driver to ensure reuse across scenarios
        if (!ConfigurationManager.isParallelRun() && sharedDriver != null) {
            logger.info("E2E Mode: Returning shared browser instance.");
            driverThreadLocal.set(sharedDriver);
            return sharedDriver;
        } 
        // In Parallel (Isolated) mode, we check the ThreadLocal
        else if (ConfigurationManager.isParallelRun() && driverThreadLocal.get() != null) {
            logger.info("Isolated Mode: Returning thread-local driver instance.");
            return driverThreadLocal.get();
        }

        logger.info("Initializing NEW WebDriver for browser: " + browserName);
        WebDriver driver = null;

        switch (browserName.toLowerCase()) {
            case "chrome":
                driver = initializeChromeDriver();
                break;
            case "firefox":
                driver = initializeFirefoxDriver();
                break;
            case "edge":
                driver = initializeEdgeDriver();
                break;
            case "safari":
                driver = initializeSafariDriver();
                break;
            default:
                logger.error("Unsupported browser: " + browserName);
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        if (!ConfigurationManager.isParallelRun()) {
            sharedDriver = driver;
        }
        driverThreadLocal.set(driver);
        logger.info("WebDriver initialized successfully");
        return driver;
    }
    
    public static WebDriver getOrCreateDriver(String browserName) {

        // E2E mode (shared)
        if (!ConfigurationManager.isParallelRun() && sharedDriver != null) {
            logger.info("E2E Mode: Returning shared browser instance.");
            driverThreadLocal.set(sharedDriver);
            return sharedDriver;
        }

        // Parallel mode (thread-local)
        if (ConfigurationManager.isParallelRun() && driverThreadLocal.get() != null) {
            logger.info("Parallel Mode: Returning thread-local driver.");
            return driverThreadLocal.get();
        }

        // Otherwise create new
        return initializeDriver(browserName);
    }

    /**
     * Initialize Chrome WebDriver
     */
    private static WebDriver initializeChromeDriver() {
//        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        // Common options for better performance
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        
        // Headless mode (uncomment if needed)
        // options.addArguments("--headless");
        
        return new ChromeDriver(options);
    }

    /**
     * Initialize Firefox WebDriver
     */
    private static WebDriver initializeFirefoxDriver() {
//        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        options.addArguments("--start-maximized");
        // Headless mode (uncomment if needed)
        // options.addArguments("--headless");
        
        return new FirefoxDriver(options);
    }

    /**
     * Initialize Edge WebDriver
     */
    private static WebDriver initializeEdgeDriver() {
//        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        return new EdgeDriver(options);
    }

    /**
     * Initialize Safari WebDriver
     */
    private static WebDriver initializeSafariDriver() {
//        WebDriverManager.safaridriver().setup();
        return new SafariDriver();
    }

    /**
     * Get WebDriver from thread local
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Quit WebDriver and remove from thread local
     */
    public static void quitDriver() {

        if (ConfigurationManager.isParallelRun()) {
            WebDriver driver = driverThreadLocal.get();
            if (driver != null) {
                logger.info("Closing thread-local driver");
                driver.quit();
                driverThreadLocal.remove();
            }
        }
        
        // E2E mode (shared driver)
        else {
            if (sharedDriver != null) {
                logger.info("Closing shared driver (E2E Mode)");
                sharedDriver.quit();
                sharedDriver = null;
            }
        }


//        WebDriver driver = driverThreadLocal.get();
//        if (driver != null) {
//            logger.info("Closing WebDriver");
//            driver.quit();
//            driverThreadLocal.remove();
//        }
//        if (sharedDriver != null) {
//            sharedDriver = null;
//        }
    }
}
