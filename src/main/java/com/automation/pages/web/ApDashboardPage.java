package com.automation.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.automation.pages.base.BasePage;
import com.automation.utils.LoggerUtils;

/**
 * DashboardPage - Page Object for Dashboard functionality
 */
public class ApDashboardPage extends BasePage {
    private static final LoggerUtils logger = LoggerUtils.getInstance();

//    private By dashboardHeader = By.xpath("//h1[normalize-space()='A/P Dashboard']");
//    private By apAccountTile = By.xpath("//a[contains(@href,'accounts') and normalize-space()='A/P Accounts']");

    public ApDashboardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Verify dashboard is loaded
     * @throws InterruptedException 
     */
    public boolean isDashboardLoaded() throws InterruptedException {
    	waitForInvisibilityOfLoader();
//    	Thread.sleep(4000);
        boolean isLoaded = isElementDisplayed(getElement("apDashboardPage", "dashboardHeader"));
        logger.info("Dashboard loaded: " + isLoaded);
        return isLoaded;
    }
    
    public void clickApAccountTile() throws InterruptedException {
//    	Thread.sleep(3000);
    	click(getElement("apDashboardPage", "apAccountTile"));
    }
    
    public void clickCreatePayableTile() throws InterruptedException {
    	click(getElement("apDashboardPage", "apAccountTile"));
    }
    
    public void clickPaymentMethodTile() {
    	click(getElement("apDashboardPage", "paymentMethodTile"));
    }
    
    public void clickpayeesTile() throws InterruptedException {
    	click(getElement("apDashboardPage", "payeesTile"));
    }
    public void clickcommissionsDropdownButton() throws InterruptedException {
    	click(getElement("apDashboardPage", "commissionsDropdownButton"));
    }
    public void clickaccountManagerOption() throws InterruptedException {
    	click(getElement("apDashboardPage", "accountManagerOption"));
    	waitForInvisibilityOfLoader();
    }
    public void clickSearchCommisions() throws InterruptedException {
    	click(getElement("apDashboardPage", "apSearchCommisions"));
    	waitForInvisibilityOfLoader();
    
    }
}
