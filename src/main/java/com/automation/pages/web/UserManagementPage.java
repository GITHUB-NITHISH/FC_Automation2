package com.automation.pages.web;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.automation.pages.base.BasePage;
import com.automation.utils.LoggerUtils;

public class UserManagementPage extends BasePage {
	private static final LoggerUtils logger = LoggerUtils.getInstance();
	private static final String FILE_NAME = "userManagementPage";

	public UserManagementPage(WebDriver driver) {
		super(driver);
	}

	public void accessCreateNewUserSection() {
		logger.info("Accessing Create New User section");
		waitForInvisibilityOfLoader();
		click(getElement(FILE_NAME, "createNewUserLink"));
		waitForInvisibilityOfLoader();
	}

	public void enterUserDetails(String reportingManager, String firstName, String lastName, String fcEmailAddress,
			String primaryArea) throws Exception {
		logger.info("Entering user details for: " + firstName + " " + lastName);
		waitForInvisibilityOfLoader();

//		Thread.sleep(2000);
		selectDropdownByVisibleText(getElement(FILE_NAME, "reportingManagerDropdown"), reportingManager);
		setText(getElement(FILE_NAME, "firstNameInputField"), firstName);
		setText(getElement(FILE_NAME, "lastNameInputField"), lastName);
		setText(getElement(FILE_NAME, "fcEmailAddressInputField"), fcEmailAddress);
		selectDropdownByVisibleText(getElement(FILE_NAME, "primaryAreaDropdown"), primaryArea);
	}

	/**
	 * Submits the user details form
	 */
	public void submitUserDetails() {
		logger.info("Submitting new user details");
		click(getElement(FILE_NAME, "createNewUserButton"));
		waitForInvisibilityOfLoader();
	}

	public void clickCancelButton() {
		logger.info("Clicking cancel burton in modal");
		click(getElement(FILE_NAME, "modalCancelButton"));
	}

	public void verifyAutoPopulatedUsername(String expected) throws InterruptedException {
//    	Thread.sleep(1500);
		validateEqualsAttributeValue(getElement(FILE_NAME, "userNameInputField"), expected);
	}

	public void clickHomeButton() {
		waitForInvisibilityOfLoader();
		click(getElement(FILE_NAME, "homeButton"));
		waitForInvisibilityOfLoader();
	}

	public String getAutoPopulatedUsername() throws InterruptedException {
//    	Thread.sleep(1500);
		return getAttribute(getElement(FILE_NAME, "userNameInputField"), "value");
	}

}
