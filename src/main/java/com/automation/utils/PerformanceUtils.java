package com.automation.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import java.util.HashMap;
import java.util.Map;

public class PerformanceUtils {

    public static Map<String, Object> getMetrics(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        Map<String, Object> metrics = new HashMap<>();

        long navStart = (Long) js.executeScript("return performance.timing.navigationStart");
        long loadEnd = (Long) js.executeScript("return performance.timing.loadEventEnd");

        metrics.put("PageLoadTime", loadEnd - navStart);

        return metrics;
    }
}
