package com.automation.pages.web;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.automation.pages.base.BasePage;
import com.automation.utils.LoggerUtils;
import com.automation.utils.SelfHealingLocatorUtil;

public class ApSearchCommissionsPage extends BasePage  {
	
	private static final LoggerUtils logger = LoggerUtils.getInstance();

	public ApSearchCommissionsPage (WebDriver driver) {
			super(driver);
		}
	
	public boolean issearchCommissionspageDisplayed() {
	    waitForInvisibilityOfLoader();
	    return waitForElementVisible(
	            getElement("apSearchCommissionsPage", "searchCommissionsHeader")
	    ).isDisplayed();
	}
	public void clickDueRadioButton() {	
		    click(getElement("apSearchCommissionsPage", "dueRadioButton"));		    
	    }

}
