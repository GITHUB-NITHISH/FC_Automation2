package com.automation.pages.web;

import static org.testng.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.automation.pages.base.BasePage;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.DatabaseUtils;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;

public class CommissionsPage extends BasePage {

	private static final LoggerUtils logger = LoggerUtils.getInstance();

	public CommissionsPage(WebDriver driver) {
		super(driver);
	}

	public boolean isSalesDashboardLoaded() {
		logger.info("Checking Sales Dashboard page");
		waitForInvisibilityOfLoader();

		return isElementDisplayed(getElement(getPageName(), "salesDashboardHeader"));
	}

	public void navigateToCommissionsModule() {
		logger.info("Navigating to Commissions module");

		waitForInvisibilityOfLoader();
		click(getElement(getPageName(), "salesDropdown"));
		click(getElement(getPageName(), "salesDropdownOption", "Commissions"));
		waitForInvisibilityOfLoader();
	}

	public void clickCreateNewAccountManagerSection() {
		logger.info("Opening Create Account Manager section");
		click(getElement(getPageName(), "createNewAccountManagerSection"));
	}

	public boolean isCreateNewAccountManagerPageLoaded() {
		waitForInvisibilityOfLoader();
		boolean isLoaded = isElementDisplayed(getElement(getPageName(), "salesDashboardHeader"));
		logger.info("Sales Dashboard loaded: " + isLoaded);
		return isLoaded;
	}

	public void selectAccountManagerType(String AMtype) {
		logger.info("Selecting Account Manager Type: " + AMtype);
		WebElement element = webElement(getElement(getPageName(), "accountManagerTypeRadioButton", AMtype));
		if (element.isSelected()) {
			return;
		} else
			click(getElement(getPageName(), "accountManagerTypeRadioButton", AMtype));
	}

	public void selectExistingFreightcomUser(String fcUser) {
		logger.info("Selecting existing Freightcom user: " + fcUser);

		click(getElement(getPageName(), "existingFreightcomUserDropdown"));
		setText(getElement(getPageName(), "existingFreightcomUserDropdownSearchField"), fcUser);
		click(getElement(getPageName(), "existingFreightcomUserDropdownOptions", fcUser));
	}

	public void assignToSalesTeam(String teamName) {
		logger.info("Assigning team: " + teamName);

		click(getElement(getPageName(), "addtoSalesTeamDropdown"));
		setText(getElement(getPageName(), "addtoSalesTeamDropdownSearchField"), teamName);
		click(getElement(getPageName(), "addtoSalesTeamDropdownOptions", teamName));

		logger.info("Successfully assigned to Sales Team: " + teamName);
	}

	public void enterStartDate(String startDate) {
		logger.info("Entering start date: " + startDate);
		setText(getElement(getPageName(), "startDateField"), startDate);
	}

	public void verifyAutoPopulatedNames(String firstName, String lastName) {
		logger.info("Verifying personal details: " + firstName + " " + lastName);

		validateEqualsAttributeValue(getElement(getPageName(), "firstNameField"), firstName);
		validateEqualsAttributeValue(getElement(getPageName(), "lastNameField"), lastName);
	}

	public void verifyAutoPopulatedStartDate(String startDate) {
		logger.info("Verifying StartDate :" + startDate);

		validateEqualsAttributeValue(getElement(getPageName(), "startDateField"), startDate);
	}

	public void verifyAutoPopulatedWorkPhoneNumber(String workPhoneNumber) {
		logger.info("Verifying WorkPhoneNumber :" + workPhoneNumber);

		validateEqualsAttributeValue(getElement(getPageName(), "workPhoneNumberField"), workPhoneNumber);
	}

	public void verifyAutoPopulatedExtension(String extension) {
		logger.info("Verifying Extension :" + extension);

		validateEqualsAttributeValue(getElement(getPageName(), "extensionField"), extension);
	}

	public void verifyAutoPopulatedCellPhoneNumber(String cellPhoneNumber) {
		logger.info("Verifying CellPhoneNumber :" + cellPhoneNumber);

		validateEqualsAttributeValue(getElement(getPageName(), "cellPhoneNumberField"), cellPhoneNumber);
	}

	public void verifyAutoPopulatedFCEmailAddress(String fcEmailAddress) {
		logger.info("Verifying FCEmailAddress :" + fcEmailAddress);

		validateEqualsAttributeValue(getElement(getPageName(), "freightcomEmailAddressField"), fcEmailAddress);
	}

	public void verifyAutoPopulatedPersonalEmailAddress(String personalEmailAddress) {
		logger.info("Verifying PersonalEmailAddress :" + personalEmailAddress);

		validateEqualsAttributeValue(getElement(getPageName(), "personalEmailAddressField"), personalEmailAddress);
	}

	public void verifyAutoPopulatedUsername(String username) {
		logger.info("Verifying login credentials (Username): " + username);
		validateEqualsAttributeValue(getElement(getPageName(), "usernameField"), username);
	}

	public void assignRolePermission(String role) {
		logger.info("Assigning role: " + role);
		selectDropdownByVisibleText(getElement(getPageName(), "assignRoleDropdown"), role);
	}

	public void configureInitialSignup(String signupCode) {
		logger.info("Configuring initial signup code: " + signupCode);
		setText(getElement(getPageName(), "initialSignupCodeField"), signupCode);
	}

	public void uploadProfileImage(String imagePath) {
		logger.info("Uploading profile image from path: " + imagePath);

		String absolutePath = new java.io.File(imagePath).getAbsolutePath();
		webElement(getElement(getPageName(), "uploadImageLink")).sendKeys(absolutePath);

		wait.until(driver -> {
			WebElement img = driver.findElement(getSlowLoadingElement(getPageName(), "uploadedImage", 7));
			return img.isDisplayed() && img.getAttribute("src") != null && !img.getAttribute("src").isEmpty();
		});

		logger.info("✅ Profile image uploaded and displayed successfully");

	}

	public void clickOperationalCostToggle() throws Exception {
		clickToggle(getElement(getPageName(), "operationalCostToggle"));
	}

	public void clickOpCostEditButton(String shipmentType) throws Exception {
		click(getElement(getPageName(), "OpCostEditButton", shipmentType));
	}

	public void enterOpCostPerShipmentCost(String perShipment, String shipmentType) throws InterruptedException {
		logger.info("Entering Per shipment cost: " + perShipment);
		setText(getElement(getPageName(), "OpCostPerShipmentCostField", shipmentType), perShipment);
	}

	public void enterOpCostRevenuePercent(String revenue, String shipmentType) throws InterruptedException {
//		Thread.sleep(2000);
		logger.info("Entering revenue percentage: " + revenue);
		setText(getElement(getPageName(), "OpCostRevenuePercentField", shipmentType), revenue);
	}

	public void clickSubmitAccountManager() throws InterruptedException {
		logger.info("Submitting Account Manager details");
		click(getElement(getPageName(), "saveNewAccountManagerButton"));
	}

	public String getModalMessage() {
		waitForInvisibilityOfLoader();
		return getText(getElement(getPageName(), "modalMessage"));
	}

	public void navigateToEditCommissions() {
		logger.info("Navigating to Edit Commissions section");
		click(getElement(getPageName(), "editCommissionSection"));
		waitForInvisibilityOfLoader();
	}

	public void navigateToEditAccountManagerSection() {
		logger.info("Navigating to Edit Account Manager section");
		click(getElement(getPageName(), "editAccountManagerSection"));
		waitForInvisibilityOfLoader();
	}

	public void selectAccountManager(String accountManager) {
		logger.info("Selecting Account Manager ");
		click(getElement(getPageName(), "AccountManagerDropdown"));
		setText(getElement(getPageName(), "AccountManagerSearchInput"), accountManager);
		click(getElement(getPageName(), "AccountManagerDropdownOption", accountManager));
		waitForInvisibilityOfLoader();
	}

	public void selectCommissionStructureType(String type) {
		logger.info("Selecting commission structure type");
		waitForInvisibilityOfLoader();
		click(getElement(getPageName(), "CommissionStructureType", type));
	}

	public void enterRevenueAndShipmentCommission(String mode, String area, String revenue, String shipmentAmount) {
		enterRevenue(mode, area, revenue);
		enterShipmentAmount(mode, area, shipmentAmount);

		Assert.assertTrue(isFieldDisabled(getElement(getPageName(), "GrossProfitField", mode, area)));
	}

	public void enterGrossProfitAndShipmentCommission(String mode, String area, String grossProfit,
			String shipmentAmount) {
		enterGrossProfit(mode, area, grossProfit);
		enterShipmentAmount(mode, area, shipmentAmount);

		Assert.assertTrue(isFieldDisabled(getElement(getPageName(), "RevenueField", mode, area)));
	}

	public void enterRevenue(String mode, String area, String revenue) {
		setText(getElement(getPageName(), "RevenueField", mode, area), revenue);
		logger.info("Entered Revenue % : " + revenue + " for area : " + area);
	}

	public void enterShipmentAmount(String mode, String area, String shipmentAmount) {
		setText(getElement(getPageName(), "ShipmentAmountField", mode, area), shipmentAmount);
		logger.info("Entered Shipment Amount : " + shipmentAmount + " for area : " + area);
	}

	public void enterGrossProfit(String mode, String area, String grossProfit) {
		setText(getElement(getPageName(), "GrossProfitField", mode, area), grossProfit);
		logger.info("Entered Gross Profit % : " + grossProfit + " for area : " + area);
	}

	public void clickSaveCommissions() {
		logger.info("Clicking Save Commissions button");
		click(getElement(getPageName(), "saveCommissionButton"));
	}

	public void clickSaveChangesButton() {
		logger.info("Clicking Save Changes button");
		click(getElement(getPageName(), "saveChangesButton"));
	}

	public void clickCommissionRulesSaveChangesButton(String shipmentType) {
		logger.info("Clicking Save Changes button in commission rule");
		click(getElement(getPageName(), "commissionRuleSaveChangesButton", shipmentType));
	}

	public void clickModalCloseButton() {
		waitForInvisibilityOfLoader();
		click(getElement(getPageName(), "modalCloseButton"));
		waitForInvisibilityOfLoader();
	}

	public void clickHomeButton() {
		waitForInvisibilityOfLoader();
		click(getElement(getPageName(), "homeButton"));
		waitForInvisibilityOfLoader();
	}

	public static Map<String, Object> getDataFromDB(String txnNumber) {

		Map<String, Object> costData = new HashMap<>();

		double totalCost = 0.0;
		double revenue = 0.0;
		double carrierCost = 0.0;
		double taxAmount = 0.0;
		double nonCommissionableAmount = 0.0;

		try (Connection conn = DatabaseUtils.getConnection("localhost", ConfigurationManager.getInt("ssh.local.port"),
				ConfigurationManager.getProperty("db.name"), ConfigurationManager.getProperty("db.user"),
				ConfigurationManager.getProperty("db.password"))) {

			String shipmentId = null;
			String customerName = null;
			String bolNumber = null;

			// ✅ Step 1: Get shipment details using PreparedStatement
			try (PreparedStatement ps = conn.prepareStatement(
					"SELECT id, bol_number, customer_name FROM freightcom.shipments WHERE transaction_number = ?")) {

				ps.setString(1, txnNumber);

				try (ResultSet rs1 = ps.executeQuery()) {
					if (rs1.next()) {
						shipmentId = rs1.getString("id");
						bolNumber = rs1.getString("bol_number");
						customerName = rs1.getString("customer_name");
					}
				}
			}

			if (shipmentId == null) {
				logger.error("No shipment found for txn: {} " + txnNumber);
				throw new RuntimeException("No shipment found for txn: " + txnNumber);
			}

			Map<String, Integer> commissionRules = new HashMap<>();

			try (PreparedStatement psRules = conn
					.prepareStatement("SELECT sub_type, apply_commission FROM freightcom.commission_charges")) {

				try (ResultSet rsRules = psRules.executeQuery()) {
					while (rsRules.next()) {
						commissionRules.put(rsRules.getString("sub_type"), rsRules.getInt("apply_commission"));
					}
				}
			}

			// ✅ Step 2: Fetch charges
			try (PreparedStatement ps2 = conn.prepareStatement(
					"SELECT type, sub_type, amount, converted_cost FROM freightcom.shipment_billed_charge_items WHERE shipment_id = ?")) {

				ps2.setString(1, shipmentId);

				try (ResultSet rs2 = ps2.executeQuery()) {

					while (rs2.next()) {

						String type = rs2.getString("type");

						double amount = extractAndConvert(rs2.getString("amount"));
						double convertedCost = extractAndConvert(rs2.getString("converted_cost"));

						totalCost += amount;

						boolean isTax = "tax".equalsIgnoreCase(type);

						if (isTax) {
							taxAmount += amount;
						} else {
							revenue += amount;
							carrierCost += convertedCost;
						}

						// Capture noncommissionable amount

						String subType = rs2.getString("sub_type");

						// Get apply_commission flag (default = 1 if not found)
						int applyCommission = commissionRules.getOrDefault(subType, 1);

						// ✅ If commission NOT applicable → subtract
						if (applyCommission == 0) {
							nonCommissionableAmount += amount;
						}

					}
				}
			}

			System.out.println("Total Cost (Dollar): " + totalCost);
			System.out.println("Revenue (Dollar): " + revenue);
			System.out.println("Carrier Cost (Dollar): " + carrierCost);
			System.out.println("Tax Amount (Dollar): " + taxAmount);
			System.out.println("Non Commissionable Amount (Dollar): " + nonCommissionableAmount);
			System.out.println("Customer Name: " + customerName);
			System.out.println("BOL Number: " + bolNumber);

			costData.put("totalCost", totalCost);
			costData.put("revenue", revenue);
			costData.put("carrierCost", carrierCost);
			costData.put("taxAmount", taxAmount);
			costData.put("customerName", customerName);
			costData.put("nonCommissionableAmount", nonCommissionableAmount);
			costData.put("bolNumber", bolNumber);

		} catch (Exception e) {
			logger.error("Calculation failed", e);
		}

		return costData;
	}

	private static double extractAndConvert(String value) {
		try {
			if (value == null || value.isEmpty())
				return 0.0;

			// CAD:4532 → split and take 4532
			String[] parts = value.split(":");
			double cents = Double.parseDouble(parts[1]);

			// Convert cents → dollars
			return cents / 100.0;

		} catch (Exception e) {
			logger.error("Conversion failed for value: " + value);
			return 0.0;
		}
	}

}
