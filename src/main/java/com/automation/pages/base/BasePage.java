package com.automation.pages.base;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.LoggerUtils;
import com.automation.utils.SelfHealingLocatorUtil;

import io.cucumber.cienvironment.internal.com.eclipsesource.json.ParseException;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * BasePage class - Contains common methods and utilities for all page objects
 * Implements PageFactory pattern with WebDriverWait for element interactions
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final LoggerUtils logger = LoggerUtils.getInstance();
    private static final int TIMEOUT = 27;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        PageFactory.initElements(driver, this);
    }

   // Wait utilities
    
    protected WebElement waitForElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element not visible within timeout: " + locator);
            throw e;
        } catch (Exception e) {
            logger.error("FAILED BEFORE TIMEOUT: " + e.getClass() + " -> " + e.getMessage());
            throw e;
        }
    }
    
    protected WebElement waitForElementClickable(By locator) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            logger.error("Element not clickable within timeout: " + locator);
            throw e;
        }
    }

    // Action methods
    protected void click(By locator) {
    	try {
			waitForInvisibilityOfLoader();
			
			WebElement element = waitForElementClickable(locator);
			scrollIntoView(element);
			boolean isClicked = false;
			
			try {
				element.click();
				logger.info("Native click succeeded");
				isClicked = true;
				return;
			} catch (Exception e1) {
				if (isStale(element)) {
					logger.info("♻️ Stale detected → Re-locating");
					element = wait.until(ExpectedConditions.elementToBeClickable(element));
					element.click();
					isClicked = true;
					return;
				}
				if (isIntercepted(element)) {
					logger.info("🧠 Intercepted → Using JS click");
					jsClick(element);
					isClicked = true;
					return;
				}
				if (!element.isDisplayed() || !element.isEnabled()) {
					logger.info("👁️ Not interactable → Waiting again");
					element = wait.until(ExpectedConditions.elementToBeClickable(element));
					element.click();
					isClicked = true;
					return;
				}
				// ================= FINAL FALLBACK =================
				logger.info("🚨 fallback → Actions click");
				new Actions(driver).moveToElement(element).pause(Duration.ofMillis(150)).click().perform();
				isClicked = true;
			}
			waitForInvisibilityOfLoader();
			if (!isClicked) {
				logger.error("❌ Post click failed on: " + element);
				throw new RuntimeException(" Post click failed on: " + element);
			}
		} catch (Exception e) {
			logger.error("Failed to click element: " + locator  + " Error: " + e.getMessage());
			throw e;
		}
    }
    
    protected void clickByJS(By locator) throws Exception{
        try{
            WebElement element = driver.findElement(locator);
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);
            logger.info("JS click successfully : " + locator);
        }catch(Exception e){
            logger.error("Failed to perform JS click : " + locator + " Error : " + e.getMessage());
            throw e;
        }
    }
    
    protected void clickToggle(By locator) throws Exception{
        try{
            WebElement element = driver.findElement(locator);
            scrollIntoView(element);
            try{
            	jsClick(element);
                logger.info("Toggle clicked using JS : " + locator);
            }catch(Exception e){
            	element.click();
                logger.info("Toggle clicked normally : " + locator);
            }
        }catch(Exception e){
            logger.error("Failed to click toggle : " + locator + " Error : " + e.getMessage());
            throw e;
        }
    }

    protected void setText(By locator, String text) {
        try {
            WebElement element = waitForElementVisible(locator);
            element.clear();
            element.sendKeys(text);
            logger.info("✅ Entered: " + text);
        } catch (Exception e) {
            logger.error("❌ Set text failed: " + e.getMessage());
            throw e;
        }
    }

    protected String getText(By locator) {
        try {
            WebElement element = waitForElementVisible(locator);
            String text = element.getText();
            logger.info("✅ Text retrieved");
            return text;
        } catch (Exception e) {
            logger.error("❌ Get text failed: " + e.getMessage());
            throw e;
        }
    }

	protected String getAttribute(By locator, String attribute) {
		try {
			WebElement element = waitForElementVisible(locator);

			wait.withTimeout(Duration.ofSeconds(5)).until(driver -> {
				String value = element.getAttribute(attribute);
				return value != null && !value.trim().isEmpty();
			});

			String attributeValue = element.getAttribute(attribute);
	        logger.info("✅ Attribute retrieved: " + attribute);
	        
			return attributeValue;
		} catch (Exception e) {
	        logger.error("❌ Get attribute failed: " + e.getMessage());
			throw e;
		}
	}
 
    protected boolean isElementDisplayed(By locator) {
    	 WebElement element = driver.findElement(locator);
        try {
            boolean isDisplayed = element.isDisplayed();
	        logger.info("✅ Element Displayed");
            return isDisplayed;
        } catch (StaleElementReferenceException e) {
            logger.warn("Element became stale: " + element);
            return false;
        } catch (Exception e) {
            logger.warn("Error checking element visibility: " + e.getMessage());
            return false;
        }
    }

//    protected void selectDropdownByVisibleText(By locator, String text) {
//        try {
//            WebElement element = waitForElementVisible(locator);
//            Select dropdown = new Select(element);
//            logger.info("Selecting dropdown option: " + text + " from: " + locator);
//            dropdown.selectByVisibleText(text);
//        } catch (Exception e) {
//            logger.error("Failed to select dropdown option: " + text + " Error: " + e.getMessage());
//            throw e;
//        }
//    }
    
    protected void selectDropdownByVisibleText(By locator, String text) {

        try {        	
        	WebElement element = waitForElementClickable(locator);
            scrollIntoView(element);
            
            Select dropdown = new Select(element);

            wait.until(driver -> !dropdown.getOptions().isEmpty());

            dropdown.selectByVisibleText(text.trim());

            logger.info("✅ Selected: " + text);
        } catch (Exception e) {
            logger.info("⚠️ Normal Select failed → Falling back to Angular JS selection for: " + text);
            try {
            	WebElement dropdown = waitForElementClickable(locator);
                dropdown.click();

                ((JavascriptExecutor) driver).executeScript(
                    "var sel = arguments[0];" +
                    "var txt = arguments[1].trim().toLowerCase();" +
                    "var found = false;" +

                    "for (var i = 0; i < sel.options.length; i++) {" +
                    "  var opt = sel.options[i];" +
                    "  if (opt.text.trim().toLowerCase() === txt || opt.value.toLowerCase() === txt) {" +
                    "    sel.selectedIndex = i;" +
                    "    sel.value = opt.value;" +   
                    "    found = true;" +
                    "    break;" +
                    "  }" +
                    "}" +

                    "if (found) {" +
                    "  sel.dispatchEvent(new Event('change', { bubbles: true }));" +
                    "  sel.dispatchEvent(new Event('input', { bubbles: true }));" +
                    "}",

                    dropdown, text);

                logger.info("✅ Selected using JS: " + text);

            } catch (Exception ex) {
            	logger.error("❌ Dropdown selection failed: " + text);
                throw ex;
            }
        }
    }

    protected void selectDropdownByValue(By locator, String value) {
        try {
            WebElement element = waitForElementVisible(locator);
            Select dropdown = new Select(element);
            dropdown.selectByValue(value);
            logger.info("✅ Selected value: " + value);
        } catch (Exception e) {
            logger.error("❌ Dropdown selection failed: " + value);
            throw e;
        }
    }

    protected List<WebElement> getDropdownOptions(By locator) {
        try {
            WebElement element = waitForElementVisible(locator);
            Select dropdown = new Select(element);
            List<WebElement> options = dropdown.getOptions();
            logger.info("Retrieved dropdown options count: " + options.size() + " from: " + locator);
            return options;
        } catch (Exception e) {
            logger.error("❌ Failed to get Dropdown options");
            throw e;
        }
    }
    
    public void selectDropdownElement(By locator, String value) {
    	try {
    		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    		By dynamicLocator = resolveDynamicLocator(locator, value);

    		logger.info("Selecting dropdown value: " + value);

    		WebElement element = wait.until(
    				ExpectedConditions.elementToBeClickable(dynamicLocator)
    		);
    		element.click();
    	} catch (Exception e) {
    		throw new RuntimeException("Failed to select dropdown value: " + value, e);
    	}
    }
    
    public void selectMultipleDropdownElements(By locator, String allvalues) {
    	for (String value : allvalues.split(",")) {
    		selectDropdownElement(locator, value.trim());
    	}
    }

    protected List<WebElement> getElements(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            logger.info("Found " + elements.size() + " elements for: " + locator);
            return elements;
        } catch (Exception e) {
            logger.error("Failed to get elements for: " + locator + " Error: " + e.getMessage());
            throw e;
        }
    }

    protected void switchToIframe(int index) {
        try {
            logger.info("Switching to iframe with index: " + index);
            driver.switchTo().frame(index);
        } catch (Exception e) {
            logger.error("Failed to switch to iframe with index: " + index + " Error: " + e.getMessage());
            throw e;
        }
    }

    protected void switchToIframe(By locator) {
        try {
            logger.info("Switching to iframe: " + locator);
            WebElement iframeElement = waitForElementVisible(locator);
            driver.switchTo().frame(iframeElement);
        } catch (Exception e) {
            logger.error("Failed to switch to iframe: " + locator + " Error: " + e.getMessage());
            throw e;
        }
    }

    protected void switchOutOfIframe() {
        try {
            logger.info("Switching out of iframe");
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            logger.error("Failed to switch out of iframe. Error: " + e.getMessage());
            throw e;
        }
    }

    protected void scrollToElement(By locator) throws Exception {
        try {
            WebElement element = driver.findElement(locator);
            logger.info("Scrolling to element: " + locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(500);
        } catch (Exception e) {
            logger.error("Failed to scroll to element: " + locator + " Error: " + e.getMessage());
            throw e;
        }
    }

    protected Object executeJavaScript(String script, Object... args) {
        try {
            logger.info("Executing JavaScript: " + script);
            return ((JavascriptExecutor) driver).executeScript(script, args);
        } catch (Exception e) {
            logger.error("Failed to execute JavaScript. Error: " + e.getMessage());
            throw e;
        }
    } 
    
    public void waitForInvisibilityOfLoader() {
        By loader = By.xpath("//div[@class='waiting-overlay']");

        long stableTime = 300;
        long lastSeenTime = System.currentTimeMillis();
        long maxWaitTime = 30000;
        long startTime = System.currentTimeMillis();

        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        try {
            // Small buffer time for loader to appear
            boolean loaderDetected = false;
            while (System.currentTimeMillis() - startTime < 500) {
                List<WebElement> loaders = driver.findElements(loader);
                loaderDetected = loaders.stream().anyMatch(element -> {
                    try {return element.isDisplayed(); } 
                    catch (Exception e) {return false; }
                });
                if (loaderDetected) {  break; }
                Thread.sleep(50);
            }
            // If loader never appeared, still wait briefly for page stabilization
            if (!loaderDetected) {
                Thread.sleep(200);
                return;
            }

            // Wait until loader disappears and remains stable
            while (System.currentTimeMillis() - startTime < maxWaitTime) {
                List<WebElement> loaders = driver.findElements(loader);
                boolean visible = loaders.stream().anyMatch(element -> {
                    try {
                        return element.isDisplayed();
                    } catch (Exception e) {
                        return false;
                    }
                });
                if (visible) {
                    lastSeenTime = System.currentTimeMillis();
                } else {
                    long stableDuration =
                            System.currentTimeMillis() - lastSeenTime;
                    if (stableDuration >= stableTime) {
                        return;
                    }
                }
                Thread.sleep(50);
            }
        } catch (Exception e) { } 
        finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigurationManager.getImplicitWait()));
        }
    }
  
    protected String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.info("Current URL: " + url);
        return url;
    }

    protected void navigateTo(String url) {
        try {
            logger.info("Navigating to URL: " + url);
            driver.navigate().to(url);
            waitForInvisibilityOfLoader();
        } catch (Exception e) {
            logger.error("Failed to navigate to URL: " + url + " Error: " + e.getMessage());
            throw e;
        }
    }
    
    public static By resolveDynamicLocator(By locator, String value) {
    	String xpath = locator.toString().replace("By.xpath: ", "");
    	xpath = xpath.replace("${value}", value);
    	return By.xpath(xpath);
    }
    
    protected void scrollIntoView(WebElement element) {
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
	}
	private void jsClick(WebElement element) {
	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}
	private boolean isStale(WebElement element) {
	    try {
	        element.isEnabled();
	        return false;
	    } catch (StaleElementReferenceException e) {
	        return true;
	    }
	}
	private boolean isIntercepted(WebElement element) {
	    try {
	        String script =
	                "const el = arguments[0];" +
	                "const r = el.getBoundingClientRect();" +
	                "const x = r.left + 5;" +
	                "const y = r.top + 5;" +
	                "const e = document.elementFromPoint(x, y);" +
	                "return e !== el;";

	        return Boolean.TRUE.equals(
	                ((JavascriptExecutor) driver).executeScript(script, element)
	        );
	    } catch (Exception e) {
	        return false;
	    }
	}
	
   //SelfHealing Method
    
    protected By getElement(String page, String element) {
        return SelfHealingLocatorUtil.findElement(driver, page, element);
    }

    protected By getElement(String page, String element, String... values) {
        return SelfHealingLocatorUtil.findElement(driver, page, element, values);
    }
    
    protected By getSlowLoadingElement(String page, String element, int timeout, String... values) {

        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

        return customWait.until(driver -> {
            try {
                By locator = SelfHealingLocatorUtil.findElement(driver, page, element, values);

                if (!driver.findElements(locator).isEmpty()) {
                    return locator;
                }

                return null;

            } catch (Exception e) {
                return null; // retry until timeout
            }
        });
    }
    
    // Validation methods

    public void validateEqualsText(String actualText, String expectedText) {
        validateText(actualText, "equals", expectedText);
    }

    public void validateContainsText(String actualText, String expectedText) {
        validateText(actualText, "contains", expectedText);
    }

    public void validateText( String actualText, String type,String expectedText) {
        logger.info("Expected Text  : " + expectedText);
        logger.info("Actual Text    : " + actualText);

        switch (type.toLowerCase()) {
            case "equals":
                Assert.assertEquals(actualText.trim(), expectedText.trim(), "Text validation failed - Text does not match exactly");
                logger.info("✅ Exact text validation passed");
                break;
                
            case "contains":
                Assert.assertTrue(
                        actualText.trim().contains(expectedText.trim()),
                        "Text validation failed - Expected text not found");
                logger.info("✅ Contains text validation passed");
                break;
            default:
                throw new IllegalArgumentException(
                        "Invalid validation type: " + type);
        }
    }
    
    public void validateEqualsAttributeValue(By by, String expectedValue) {
        validateAttributeValue(by, "equals", expectedValue);
    }

    public void validateContainsAttributeValue(By by, String expectedValue) {
        validateAttributeValue(by, "contains", expectedValue);
    }

    public void validateAttributeValue(By by, String type, String expectedValue) {
    	
        String actualValue = waitForElementVisible(by).getAttribute("value");

        if (actualValue == null) {
            actualValue = "";
        }

        logger.info("Expected Attribute Value : " + expectedValue);
        logger.info("Actual Attribute Value   : " + actualValue);

        switch (type.toLowerCase()) {

            case "equals":
                Assert.assertEquals(
                        actualValue.trim(),
                        expectedValue.trim(),
                        "Attribute value validation failed - values do not match exactly"
                );
                logger.info("✅ Exact attribute value validation passed");
                break;

            case "contains":
                Assert.assertTrue(
                        actualValue.trim().contains(expectedValue.trim()),
                        "Attribute value validation failed - expected value not found"
                );
                logger.info("✅ Contains attribute value validation passed");
                break;

            default:
                throw new IllegalArgumentException("Invalid validation type: " + type);
        }
    }
    
    public int extractNumber(By element) {
        String text = getText(element).trim();
        String number = text.replaceAll("[^0-9]", "");
        int value = number.isEmpty() ? 0 : Integer.parseInt(number);
        logger.info("Extracted numeric value: " + value);
        return value;
    }
    
    public WebElement webElement(By by) {
    	return driver.findElement(by);
    }
    
    public boolean isFieldDisabled(By locator ) {
        boolean status = !driver.findElement(locator).isEnabled();
        System.out.println(" Field Disabled: " + status);
        return status;
    }

    public boolean isFieldEnabled(By locator) {
        boolean status = driver.findElement(locator).isEnabled();
        System.out.println(" Field Enabled: " + status);
        return status;
    }
    
    //Date picker
    
    public void selectDateRange(
            String page,
            String calendarElement,
            String monthElement,
            String yearElement,
            String dayElement,
            String dateInput
    ) throws ParseException, java.text.ParseException {

        logger.info("📅 Selecting Date Range : " + dateInput);

        if (dateInput == null || dateInput.isBlank()) {
            throw new RuntimeException("Date value cannot be null or blank");
        }

        // Open calendar
        click(getElement(page, calendarElement));

        if (dateInput.contains(" - ")) {

            String[] dates = dateInput.split("\\s*-\\s*");

            Calendar startDate = parseDate(dates[0]);
            Calendar endDate   = parseDate(dates[1]);

            selectSingleDate(
                    page,
                    monthElement,
                    yearElement,
                    dayElement,
                    startDate
            );

            selectSingleDate(
                    page,
                    monthElement,
                    yearElement,
                    dayElement,
                    endDate
            );

        } else {

            Calendar singleDate = parseDate(dateInput);

            selectSingleDate(
                    page,
                    monthElement,
                    yearElement,
                    dayElement,
                    singleDate
            );
        }
    }
    
    private void selectSingleDate(
            String page,
            String monthElement,
            String yearElement,
            String dayElement,
            Calendar calendar
    ) {

        String month =
                new SimpleDateFormat("MMM", Locale.ENGLISH)
                        .format(calendar.getTime());

        String year =
                String.valueOf(calendar.get(Calendar.YEAR));

        String day =
                String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        logger.info(
                "Selecting Date -> Month: "
                        + month
                        + " Year: "
                        + year
                        + " Day: "
                        + day
        );

        // Select Month
        selectDropdownIfNotSelected(
                getElement(page, monthElement),
                month
        );

        // Select Year
        selectDropdownIfNotSelected(
                getElement(page, yearElement),
                year
        );

        // Dynamic Day Locator
        By dayLocator =
                getElement(page, dayElement, day);

        click(dayLocator);
    }
    private void selectDropdownIfNotSelected(
            By locator,
            String value
    ) {

        WebElement element =
                waitForElementVisible(locator);

        Select dropdown = new Select(element);

        String selectedText =
                dropdown.getFirstSelectedOption()
                        .getText()
                        .trim();

        if (!selectedText.equalsIgnoreCase(value)) {

            logger.info(
                    "Selecting dropdown value : " + value
            );

            dropdown.selectByVisibleText(value);

        } else {

            logger.info(
                    "Dropdown already selected : " + value
            );
        }
    }
    private Calendar parseDate(String date) throws ParseException, java.text.ParseException {

        SimpleDateFormat formatter =
                new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

        Date parsedDate = formatter.parse(date.trim());

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(parsedDate);

        return calendar;
    }
    
    
}
