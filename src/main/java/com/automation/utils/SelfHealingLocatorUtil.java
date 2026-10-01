//package com.automation.utils;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//import java.io.InputStream;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class SelfHealingLocatorUtil {
//
//    private static JsonNode rootNode;
//    private static final String LOCATOR_FILE =
//            "SelfHealingLocators/ApAccountLocators.json";
//
//    static {
//        loadLocators();
//    }
//
//    private static void loadLocators() {
//        InputStream is = null;
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            
//            // Try 1: ClassLoader
//            is = SelfHealingLocatorUtil.class.getClassLoader().getResourceAsStream(LOCATOR_FILE);
//            
//            // Try 2: Class-relative
//            if (is == null) {
//                is = SelfHealingLocatorUtil.class.getResourceAsStream("/" + LOCATOR_FILE);
//            }
//            
//            // Try 3: File System (Fallback for IDEs where target/classes is not updated)
//            if (is == null) {
//                java.io.File file = new java.io.File("src/main/resources/" + LOCATOR_FILE);
//                if (file.exists()) {
//                    is = new java.io.FileInputStream(file);
//                }
//            }
//
//            if (is == null) {
//                throw new RuntimeException("Locator JSON file not found: " + LOCATOR_FILE);
//            }
//
//            rootNode = mapper.readTree(is);
//            System.out.println("Self-Healing Locators loaded successfully from: " + LOCATOR_FILE);
//        } catch (Exception e) {
//            System.err.println("CRITICAL ERROR: Failed to load Self-Healing Locators: " + e.getMessage());
//            throw new RuntimeException("Failed to load SelfHealinglocator JSON", e);
//        } finally {
//            if (is != null) {
//                try { is.close(); } catch (Exception e) { /* ignore */ }
//            }
//        }
//    }
//
//    /**
//     * 🔹 MAIN METHOD (returns working WebElement using fallback)
//     */
//    public static By findElement(
//            WebDriver driver,
//            String pageName,
//            String elementName,
//            String... values) {
//
//        try {
//            JsonNode elementNode =
//                    rootNode.path(pageName).path(elementName);
//
//            String primary = resolveDynamic(elementNode.path("primary").asText(), values);
//            String secondary = resolveDynamic(elementNode.path("secondary").asText(), values);
//            String tertiary = resolveDynamic(elementNode.path("tertiary").asText(), values);
//
//            return tryLocators(driver, primary, secondary, tertiary);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to find element: " + elementName, e);
//        }
//    }
//
//    /**
//     * 🔹 Returns ALL locators as By[] (if you want By usage)
//     */
//    public static By[] getLocators(
//            String pageName,
//            String elementName,
//            String... values) {
//
//        JsonNode elementNode =
//                rootNode.path(pageName).path(elementName);
//
//        String primary = resolveDynamic(elementNode.path("primary").asText(), values);
//        String secondary = resolveDynamic(elementNode.path("secondary").asText(), values);
//        String tertiary = resolveDynamic(elementNode.path("tertiary").asText(), values);
//
//        return buildByList(primary, secondary, tertiary);
//    }
//
//    /**
//     * 🔥 Try locators one by one (SELF HEALING CORE)
//     */
//    private static By tryLocators(
//            WebDriver driver,
//            String... locators) {
//
//        // Disable implicit wait to ensure fast failover and precise control by WebDriverWait
//        long originalImplicitWait = ConfigurationManager.getImplicitWait();
//        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
//
//        try {
//            // Use a short timeout per locator attempt (600ms) 
//            // 3 attempts * 600ms = 1.8 seconds (Total under the 2s requirement)
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(600));
//
//            for (String locator : locators) {
//                if (locator == null || locator.isBlank()) continue;
//
//                try {
//                    By by = getBy(locator);
//                    wait.until(d -> d.findElement(by));
//                    System.out.println("✅ [SELF-HEALING SUCCESS] Found element using: " + locator);
//                    return by;
//
//                } catch (Exception e) {
//                    System.out.println("⚠️ [SELF-HEALING] Failed to find: " + locator + " (Moving to next...)");
//                }
//            }
//        } finally {
//            // Restore original implicit wait from configuration
//            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(originalImplicitWait));
//        }
//
//        throw new RuntimeException("Self-Healing failed: All locator strategies exhausted within 2 seconds.");
//    }
//
//    /**
//     * 🔹 Build By array (for By-based usage)
//     */
//    private static By[] buildByList(String... locators) {
//
//        List<By> list = new ArrayList<>();
//
//        for (String locator : locators) {
//
//            if (locator == null || locator.isBlank()) continue;
//
//            list.add(getBy(locator));
//        }
//
//        return list.toArray(new By[0]);
//    }
//
//    /**
//     * 🔥 Replace ${value}
//     */
//    private static String resolveDynamic(String locator, String... values) {
//        if (locator == null || locator.isBlank() || values == null) {
//            return locator;
//        }
//        String resolved = locator;
//        for (String value : values) {
//            resolved = resolved.replaceFirst("\\$\\{value\\}", value);
//        }
//        return resolved;
//    }
//
//    /**
//     * 🔹 Convert string → By
//     */
//    private static By getBy(String locator) {
//
//        String[] parts = locator.split("=", 2);
//
//        String type = parts[0].trim().toLowerCase();
//        String value = parts[1].trim();
//
//        switch (type) {
//            case "id": return By.id(value);
//            case "name": return By.name(value);
//            case "xpath": return By.xpath(value);
//            case "css": return By.cssSelector(value);
//            case "classname": return By.className(value);
//            case "tagname": return By.tagName(value);
//            case "linktext": return By.linkText(value);
//            case "partiallinktext": return By.partialLinkText(value);
//            default: throw new RuntimeException("Invalid locator type: " + type);
//        }
//    }
//}

package com.automation.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SelfHealingLocatorUtil {

    // Cache to store page-wise JsonNodes: Map<PageName, JsonNode>
    private static final Map<String, JsonNode> pageCache = new ConcurrentHashMap<>();
    private static final String LOCATOR_BASE_PATH = "SelfHealingLocators/";
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Internal method to get or load a page's JSON structure
     */
    private static JsonNode getPageNode(String pageName) {
        // Return from cache if already loaded
        if (pageCache.containsKey(pageName)) {
            return pageCache.get(pageName);
        }

        // Synchronized loading to handle parallel execution
        synchronized (pageCache) {
            if (pageCache.containsKey(pageName)) {
            	return pageCache.get(pageName);
            }

            String fileName = LOCATOR_BASE_PATH + pageName + ".json";
            InputStream is = null;
            try {
                // Try 1: ClassLoader
                is = SelfHealingLocatorUtil.class.getClassLoader().getResourceAsStream(fileName);

                // Try 2: Class-relative
                if (is == null) {
                    is = SelfHealingLocatorUtil.class.getResourceAsStream("/" + fileName);
                }

                // Try 3: File System Fallback
                if (is == null) {
                    java.io.File file = new java.io.File("src/main/resources/" + fileName);
                    if (file.exists()) {
                        is = new java.io.FileInputStream(file);
                    }
                }

                if (is == null) {
                    System.err.println("⚠️ Locator file not found for page: " + pageName + " (looked for " + fileName + ")");
                    return null;
                }

                JsonNode node = mapper.readTree(is);
                pageCache.put(pageName, node);
                System.out.println("🚀 Loaded locators for: " + pageName);
                return node;

            } catch (Exception e) {
                System.err.println("❌ Failed to load JSON for page: " + pageName + " Error: " + e.getMessage());
                return null;
            } finally {
                if (is != null) {
                    try { is.close(); } catch (Exception e) { /* ignore */ }
                }
            }
        }
    }

    /**
     * 🔹 MAIN METHOD (returns working WebElement using fallback)
     */
    public static By findElement(
            WebDriver driver,
            String pageName,
            String elementName,
            String... values) {

        try {
            JsonNode pageNode = getPageNode(pageName);
            if (pageNode == null) {
                throw new RuntimeException("No locator data found for page: " + pageName);
            }

            JsonNode elementNode = pageNode.path(elementName);
            if (elementNode.isMissingNode()) {
                // Check if the structure is root -> pageName -> elementName
                // or if the file IS the pageName structure already.
                elementNode = pageNode.path(pageName).path(elementName);
            }

            String primary = resolveDynamic(elementNode.path("primary").asText(), values);
            String secondary = resolveDynamic(elementNode.path("secondary").asText(), values);
            String tertiary = resolveDynamic(elementNode.path("tertiary").asText(), values);

            return tryLocators(driver, primary, secondary, tertiary);

        } catch (Exception e) {
            throw new RuntimeException("Failed to find element: " + elementName + " on page: " + pageName, e);
        }
    }

    /**
     * 🔹 Returns ALL locators as By[]
     */
    public static By[] getLocators(
            String pageName,
            String elementName,
            String... values) {

        JsonNode pageNode = getPageNode(pageName);
        if (pageNode == null) return new By[0];

        JsonNode elementNode = pageNode.path(elementName);
        if (elementNode.isMissingNode()) {
            elementNode = pageNode.path(pageName).path(elementName);
        }

        String primary = resolveDynamic(elementNode.path("primary").asText(), values);
        String secondary = resolveDynamic(elementNode.path("secondary").asText(), values);
        String tertiary = resolveDynamic(elementNode.path("tertiary").asText(), values);

		return buildByList(primary, secondary, tertiary);
	}

	/**
	 * 🔥 Try locators one by one (SELF HEALING CORE)
	 */
    private static By tryLocators(WebDriver driver, String... locators) {
        long originalImplicitWait = ConfigurationManager.getImplicitWait();
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(600));
            String[] levels = {"PRIMARY", "SECONDARY", "TERTIARY"};
            for (int i = 0; i < locators.length; i++) {
            	String locator = locators[i];
            	if (locator == null || locator.isBlank()) continue;

                String level = levels[i];

                try {
                    By by = getBy(locator);
                    wait.until(d -> d.findElement(by));
//                    System.out.println("✅ [SELF-HEALING]" + level + " locator FOUND " );
                    return by;
                } catch (Exception e) {
                    System.out.println("❌ [SELF-HEALING]" + level + " locator NOT FOUND -> " + locator);
                }
            }
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(originalImplicitWait));
        }

        throw new RuntimeException("Self-Healing failed: All strategies exhausted.");
    }


    private static By[] buildByList(String... locators) {
        List<By> list = new ArrayList<>();
        for (String locator : locators) {
            if (locator == null || locator.isBlank()) continue;
            list.add(getBy(locator));
        }
        return list.toArray(new By[0]);
    }

    private static String resolveDynamic(String locator, String... values) {
        if (locator == null || locator.isBlank() || values == null) return locator;
        String resolved = locator;
        for (String value : values) {
            resolved = resolved.replaceFirst("\\$\\{value\\}", value);
        }
        return resolved;
    }

    private static By getBy(String locator) {
        String[] parts = locator.split("=", 2);
        String type = parts[0].trim().toLowerCase();
        String value = parts[1].trim();

        switch (type) {
            case "id": return By.id(value);
            case "name": return By.name(value);
            case "xpath": return By.xpath(value);
            case "css": return By.cssSelector(value);
            case "classname": return By.className(value);
            case "tagname": return By.tagName(value);
            case "linktext": return By.linkText(value);
            case "partiallinktext": return By.partialLinkText(value);
            default: throw new RuntimeException("Invalid locator type: " + type);
        }
    }
}
