package com.automation.utils;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * FileUtils - Utility methods for file operations like screenshots
 */
public class FileUtils {
    private static final LoggerUtils logger = LoggerUtils.getInstance();

    /**
     * Take screenshot and save to file
     */
    public static String takeScreenshot(WebDriver driver, String screenshotName) {
        try {
            String screenshotDir = ConfigurationManager.getScreenshotDirectory();
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs();
                logger.info("Screenshot directory created: " + screenshotDir);
            }

            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            String filePath = screenshotDir + File.separator + screenshotName + "_" + System.currentTimeMillis() + ".png";
            org.apache.commons.io.FileUtils.copyFile(srcFile, new File(filePath));
//          FileUtils.copyFile(srcFile, new File(filePath));
            logger.info("Screenshot saved: " + filePath);
            return filePath;
        } catch (IOException e) {
            logger.error("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }
    
    public static byte[] takeScreenshot(WebDriver driver) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);

            logger.info("Screenshot captured successfully (byte array)");
            return screenshotBytes;

        } catch (Exception e) {
            logger.error("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Create directory if not exists
     */
    public static boolean createDirectory(String path) {
        try {
            Files.createDirectories(Paths.get(path));
            logger.info("Directory created: " + path);
            return true;
        } catch (IOException e) {
            logger.error("Failed to create directory: " + path);
            return false;
        }
    }

    /**
     * Check if file exists
     */
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Delete file
     */
    public static boolean deleteFile(String filePath) {
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            logger.error("Failed to delete file: " + filePath);
            return false;
        }
    }
}
