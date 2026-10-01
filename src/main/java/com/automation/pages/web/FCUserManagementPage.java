package com.automation.pages.web;

import org.openqa.selenium.WebDriver;
import com.automation.pages.base.BasePage;
import com.automation.utils.LoggerUtils;

public class FCUserManagementPage extends BasePage {
	private static final LoggerUtils logger = LoggerUtils.getInstance();

	public FCUserManagementPage(WebDriver driver) {
		super(driver);
	}

	public void accessCreateNewUserSection() {
		logger.info("Accessing Create New User section");
		
		waitForInvisibilityOfLoader();
		click(getElement(getPageName(), "createNewUserLink"));
		waitForInvisibilityOfLoader();
	}

	public void configureUserDetails(String startDate, String reportingManager, String firstName, String lastName,
	        String workPhone, String extension, String cellPhone, String fcEmailAddress,
	        String personalEmailAddress, String primaryArea) throws Exception {

	    logger.info("Entering user details for: " + firstName + " " + lastName);
	    waitForInvisibilityOfLoader();

	    logger.info("Setting Start Date: " + startDate);
	    setText(getElement(getPageName(),"startDateInputField"), startDate);

	    logger.info("Selecting Reporting Manager: " + reportingManager);
	    selectDropdownByVisibleText(getElement(getPageName(), "reportingManagerDropdown"), reportingManager);

	    logger.info("Entering First Name: " + firstName);
	    setText(getElement(getPageName(), "firstNameInputField"), firstName);

	    logger.info("Entering Last Name: " + lastName);
	    setText(getElement(getPageName(), "lastNameInputField"), lastName);

	    logger.info("Entering Work Phone: " + workPhone);
	    setText(getElement(getPageName(), "workPhoneInputField"), workPhone);

	    logger.info("Entering Extension: " + extension);
	    setText(getElement(getPageName(), "extensionInputField"), extension);

	    logger.info("Entering Personal Phone: " + cellPhone);
	    setText(getElement(getPageName(), "personalPhoneInputField"), cellPhone);

	    logger.info("Entering Official Email: " + fcEmailAddress);
	    setText(getElement(getPageName(), "fcEmailAddressInputField"), fcEmailAddress);

	    logger.info("Entering Personal Email: " + personalEmailAddress);
	    setText(getElement(getPageName(), "personalEmailAddressInputField"), personalEmailAddress);

	    logger.info("Selecting Primary Area: " + primaryArea);
	    selectDropdownByVisibleText(getElement(getPageName(), "primaryAreaDropdown"), primaryArea);
	}

	
	public boolean isUserNameAutoPopulated() {
		logger.info("Checking Username autopopulated");
		return isFieldAutoPopulated(getElement(getPageName(), "userNameInputField"));
	}
	
	public String getAutoPopulatedUserName() {
		return waitForElementVisible(getElement(getPageName(), "userNameInputField")).getAttribute("value").trim();
	}
	/**
	 * Submits the user details form
	 */
	public void submitUserDetails() {
		logger.info("Submitting new user details");
		click(getElement(getPageName(), "createNewUserButton"));
		waitForInvisibilityOfLoader();
	}

	public void clickCancelButton() {
		logger.info("Clicking cancel button in modal");
		click(getElement(getPageName(), "modalCancelButton"));
	}

	public void verifyAutoPopulatedUsername(String expected) throws InterruptedException {
        logger.info("Validating auto populated username: " + expected);
		validateEqualsAttributeValue(getElement(getPageName(), "userNameInputField"), expected);
	}
}
