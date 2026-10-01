package com.automation.pages.web;

import static org.testng.Assert.assertTrue;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.automation.pages.base.BasePage;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;

public class SalesPage extends BasePage{
	
	private static final LoggerUtils logger = LoggerUtils.getInstance();
	
	 Map<String, String> data = ScenarioContext.getMap("TC01_AM_data");

	public SalesPage(WebDriver driver) {
		super(driver);
	}

	public boolean isSalesDashboardLoaded() {
		waitForInvisibilityOfLoader();
		boolean isLoaded = isElementDisplayed(getElement("salesPage", "salesDashboardHeader"));
		logger.info("Sales Dashboard loaded: " + isLoaded);
        return isLoaded;
	}
	
	public void navigateToCommissionsModule() {
		waitForInvisibilityOfLoader();
		click(getElement("salesPage", "salesDropdown"));
		click(getElement("salesPage", "salesDropdownOption", "Commissions"));
	}
	
	public void clickCreateNewAccountManagerSection() {
		click(getElement("salesPage", "createNewAccountManagerSection"));
	}
	
	public boolean isCreateNewAccountManagerPageLoaded() {
		waitForInvisibilityOfLoader();
		boolean isLoaded = isElementDisplayed(getElement("salesPage", "salesDashboardHeader"));
		logger.info("Sales Dashboard loaded: " + isLoaded);
        return isLoaded;
	}
	
	public void selectAccountManagerType(String AMtype) {
		WebElement element = webElement(getElement("salesPage", "accountManagerTypeRadioButton",AMtype));
		if(element.isSelected()) return;
		
		click(getElement("salesPage", "accountManagerTypeRadioButton",AMtype));
	}
	
	public void selectExistingFreightcomUser(String fcUser) {
		logger.info("Associating existing Freightcom user: " + fcUser);

		click(getElement("salesPage", "existingFreightcomUserDropdown"));
		setText(getElement("salesPage", "existingFreightcomUserDropdownSearchField"), fcUser);
		click(getElement("salesPage", "existingFreightcomUserDropdownOptions", fcUser));
	}
	
	public void assignToSalesTeam(String teamName) {
	    click(getElement("salesPage", "addtoSalesTeamDropdown"));
	    setText(getElement("salesPage", "addtoSalesTeamDropdownSearchField"), teamName);
	    click(getElement("salesPage", "addtoSalesTeamDropdownOptions", teamName));
	    
	    logger.info("Successfully assigned to Sales Team: " + teamName);
	}
	
	public void enterStartDate(String startDate) {
	    logger.info("Entering start date: " + startDate);
	    setText(getElement("salesPage", "startDateField"), startDate);
	}
	
	public void verifyAutopopulatedName(String firstName, String lastName) {
	    logger.info("Verifying personal details: " + firstName + " " + lastName);
	    
	    validateEqualsAttributeValue(getElement("salesPage", "firstNameField"), firstName);
	    validateEqualsAttributeValue(getElement("salesPage", "lastNameField"), lastName);
//	    setText(getElement("salesPage", "firstNameField"), firstName);
//	    setText(getElement("salesPage", "lastNameField"), lastName);
	}
	
	public void enterContactDetails(String workPhone, String extension, String cellPhone, String fcEmailAddress, String personalEmailAddress) {
	    logger.info("Entering contact details: Work=" + workPhone + ", Ext=" + extension + ", Cell=" + cellPhone);
	    setText(getElement("salesPage", "workPhoneNumberField"), workPhone);
	    setText(getElement("salesPage", "extensionField"), extension);
	    setText(getElement("salesPage", "cellPhoneNumberField"), cellPhone);
	    validateEqualsAttributeValue(getElement("salesPage", "freightcomEmailAddressField"), fcEmailAddress);
	    setText(getElement("salesPage", "personalEmailAddressField"), personalEmailAddress);
	}
	
	public void verifyAutoPopulatedUsername(String username) {
	    logger.info("Verifying login credentials (Username): " + username);
	    validateEqualsAttributeValue(getElement("salesPage", "usernameField"), username);
	}
	
	public void assignRolePermission(String role) {
	    logger.info("Assigning role: " + role);
	    selectDropdownByVisibleText(getElement("salesPage", "assignRoleDropdown"), role);
	}
	
	public void configureInitialSignup(String signupCode) {
	    logger.info("Configuring initial signup code: " + signupCode);
	    setText(getElement("salesPage", "initialSignupCodeField"), signupCode);
	}
	
	public void uploadProfileImage(String imagePath) {
	    logger.info("Uploading profile image from path: " + imagePath);

	    String absolutePath = new java.io.File(imagePath).getAbsolutePath();
	    webElement(getElement("salesPage", "uploadImageLink")).sendKeys(absolutePath);
	}
	
	public void clickOperationalCostToggle() throws Exception {
		clickToggle(getElement("salesPage", "operationalCostToggle"));
	}
	
	public void clickOpCostEditButton(String shipmentType) throws Exception {
		click(getElement("salesPage", "OpCostEditButton",shipmentType));
	}
	
	public void enterOpCostPerShipmentCost(String perShipment,String shipmentType) throws InterruptedException {
//		Thread.sleep(2000);
	    logger.info("Entering Per shipment cost: " + perShipment);
	    setText(getElement("salesPage", "OpCostPerShipmentCostField",shipmentType), perShipment);
	}
	
	public void enterOpCostRevenuePercent(String revenue,String shipmentType) throws InterruptedException {
//		Thread.sleep(2000);
	    logger.info("Entering revenue percentage: " + revenue);
	    setText(getElement("salesPage", "OpCostRevenuePercentField",shipmentType), revenue);
	}
	
	public void clickSubmitAccountManager() throws InterruptedException {
	    logger.info("Submitting Account Manager details");
//	    Thread.sleep(1000);
	    click(getElement("salesPage", "saveNewAccountManagerButton"));
	}
	
	public String getModalMessage() {
	    waitForInvisibilityOfLoader();	    // Using the common modalMessage locator
	    return getText(getElement("salesPage", "modalMessage"));
	}
	
	public void navigateToEditCommissions() {
	    logger.info("Navigating to Edit Commissions section");
	    click(getElement("salesPage", "editCommissionSection"));
	    waitForInvisibilityOfLoader();
	}
	
	public void navigateToEditAccountManagerSection() {
	    logger.info("Navigating to Edit Account Manager section");
	    click(getElement("salesPage", "editAccountManagerSection"));
	    waitForInvisibilityOfLoader();
	}
	
	public void selectAccountManager(String accountManager) {
		logger.info("Selecting Account Manager ");
		click(getElement("salesPage", "AccountManagerDropdown"));
		setText(getElement("salesPage", "AccountManagerSearchInput"), accountManager);
		click(getElement("salesPage", "AccountManagerDropdownOption", accountManager));
		waitForInvisibilityOfLoader();
	}
	
	public void selectCommissionStructureType(String type) {
		logger.info("Selecting commission structure type");
		waitForInvisibilityOfLoader();
		click(getElement("salesPage", "CommissionStructureType", type));
	}
	
	
//	public void enterCommissionData(String mode, String area, String revenue, String amount) {
//	    setText(getElement("salesPage", "RevenueField", mode, area), revenue);
//	    setText(getElement("salesPage", "ShipmentAmountField", mode, area), amount);
//	}
	
	public void enterRevenueAndShipmentCommission(String mode, String area, String revenue, String shipmentAmount) {
	    enterRevenue(mode, area , revenue);
	    enterShipmentAmount(mode, area , shipmentAmount);
	    
	    Assert.assertTrue(isFieldDisabled(getElement("salesPage", "GrossProfitField", mode, area)));	
	}
	
	public void enterGrossProfitAndShipmentCommission(String mode, String area, String grossProfit,String shipmentAmount) {
	    enterGrossProfit(mode, area , grossProfit);
	    enterShipmentAmount(mode, area , shipmentAmount);
	    
	    Assert.assertTrue(isFieldDisabled(getElement("salesPage", "RevenueField", mode, area)));
	}
	
	public void enterRevenue(String mode, String area, String revenue) {
	    setText(getElement("salesPage", "RevenueField", mode, area), revenue);
	    logger.info("Entered Revenue % : " + revenue + " for area : " + area);
	}
	
	public void enterShipmentAmount(String mode, String area, String shipmentAmount) {
	    setText(getElement("salesPage", "ShipmentAmountField", mode, area), shipmentAmount);
	    logger.info("Entered Shipment Amount : " + shipmentAmount +" for area : " + area);
	}
	
	public void enterGrossProfit(String mode, String area, String grossProfit) {
	    setText(getElement("salesPage", "GrossProfitField", mode, area),grossProfit);
	    logger.info("Entered Gross Profit % : " + grossProfit + " for area : " + area);
	}
	
	
	public void clickSaveCommissions() {
	    logger.info("Clicking Save Commissions button");
	    click(getElement("salesPage", "saveCommissionButton"));
	}
	
	public void clickSaveChangesButton() {
	    logger.info("Clicking Save Changes button");
	    click(getElement("salesPage", "saveChangesButton"));
	}
	
	public void clickCommissionRulesSaveChangesButton(String shipmentType) {
	    logger.info("Clicking Save Changes button in commission rule");
	    click(getElement("salesPage", "commissionRuleSaveChangesButton",shipmentType));
	}
	
	
	public void clickModalCloseButton() {
		waitForInvisibilityOfLoader();
		click(getElement("salesPage", "modalCloseButton"));
		waitForInvisibilityOfLoader();
	}
	
    public void clickHomeButton() {
    	  waitForInvisibilityOfLoader();
          click(getElement("salesPage", "homeButton"));
          waitForInvisibilityOfLoader();
    }
    
}
