package com.automation.pages.web;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.pages.base.BasePage;
import com.automation.utils.LoggerUtils;

public class CommonPage extends BasePage{
	    private static final LoggerUtils logger = LoggerUtils.getInstance();
	    private static final String FILE_NAME = "commonPage";

	    public CommonPage(WebDriver driver) {
	        super(driver);
	    }

	    public void navigateToUserLogin(String loginType) throws InterruptedException {
	        logger.info("Navigating to " + loginType + " login");
	        waitForInvisibilityOfLoader();
	        click(getElement(FILE_NAME, "userMenuLink"));	    
//	        Thread.sleep(27000);
	   	    click(getSlowLoadingElement(FILE_NAME, "userMenuLoginButton",27,  loginType));
	        waitForInvisibilityOfLoader();
	    }


}
