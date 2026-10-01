package com.automation.steps;
 
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
 
import org.openqa.selenium.WebDriver;
 
import com.automation.dataprovider.ExcelUtil;
import com.automation.dataprovider.JsonDataReader;
import com.automation.pages.web.CommissionsPage;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.DriverFactory;
import com.automation.utils.FakerUtil;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
 
public class CommissionsStepDef {
	private static final LoggerUtils logger = LoggerUtils.getInstance();
	private WebDriver driver;
	private CommissionsPage commissionPage;
 
	public CommissionsStepDef() {
		this.driver = DriverFactory.getDriver();
		this.commissionPage = new CommissionsPage(driver);
	}
 
	@When("user navigates to Commissions under Sales menu")
	public void user_navigates_to_commissions_under_sales_menu() {
		commissionPage.navigateToCommissionsModule();
		
	}
 
	@When("user accesses Create New Account Manager section")
	public void user_accesses_create_new_account_manager_section() {
		commissionPage.clickCreateNewAccountManagerSection();
	}
 
	@When("user defines account manager role as {string}")
	public void user_defines_account_manager_role_as(String accountManagerType) {
		commissionPage.selectAccountManagerType(accountManagerType);
	}
 
	@When("user associates an existing Freightcom user to the account manager")
	public void user_associates_an_existing_freightcom_user_to_the_account_manager() {
		
		String firstName = ScenarioContext.getFromContext("FIRSTNAME");
		String lastName = ScenarioContext.getFromContext("LASTNAME");

		String existingFreightcomUser = (commissionPage.isNullOrEmpty(firstName) && commissionPage.isNullOrEmpty(lastName)
						? ScenarioContext.getTestData("ExistingFCUser")
						: (firstName + " " + lastName).trim());
		
		commissionPage.selectExistingFreightcomUser(existingFreightcomUser);
	}
 
	@When("user assigns the account manager to a sales team")
	public void user_assigns_the_account_manager_to_a_sales_team() {
		String salesTeam = ScenarioContext.getTestData("SalesTeam");
 
		commissionPage.assignToSalesTeam(salesTeam);
	}
 
	@When("user verifies account manager personal details")
	public void user_verifies_account_manager_personal_details() {
		String firstName = ScenarioContext.getTestData("FIRSTNAME","FirstName");
		String lastName = ScenarioContext.getTestData("LASTNAME","LastName");
		String startDate = ScenarioContext.getTestData("STARTDATE","StartDate");
 
		commissionPage.verifyAutoPopulatedNames(firstName,lastName);
		commissionPage.verifyAutoPopulatedStartDate(startDate);
 
		ScenarioContext.setGlobal("ACCOUNTMANAGER", firstName + " " + lastName);
	}
 
	@When("user verifies account manager contact details")
	public void user_verifies_account_manager_contact_details() {

String workPhone = ScenarioContext.getTestData("WORKPHONENUMBER", "WorkPhoneNumber");
String extension = ScenarioContext.getTestData("EXTENSION", "Extension");
String cellPhone = ScenarioContext.getTestData("CELLPHONENUMBER", "CellPhoneNumber");
String fcEmailAddress = ScenarioContext.getTestData("FCEMAILADDRESS", "FCEmailAddress");
String personalEmailAddress = ScenarioContext.getTestData("PERSONALEMAILADDRESS", "PersonalEmailAddress");

		commissionPage.verifyAutoPopulatedWorkPhoneNumber(workPhone);
		commissionPage.verifyAutoPopulatedExtension(extension);
		commissionPage.verifyAutoPopulatedCellPhoneNumber(cellPhone);
		commissionPage.verifyAutoPopulatedFCEmailAddress(fcEmailAddress);
		commissionPage.verifyAutoPopulatedPersonalEmailAddress(personalEmailAddress);
	}
 
	@When("user verifies account manager login credentials")
	public void user_verifies_account_manager_login_credentials() {
		String userName = ScenarioContext.getTestData("USERNAME","UserName");
 
		commissionPage.verifyAutoPopulatedUsername(userName);
	}
 
	@When("user assigns role permissions for the account manager")
	public void user_assigns_role_permissions_for_the_account_manager() {
		String role = ScenarioContext.getTestData("Role");
 
		commissionPage.assignRolePermission(role);
	}
 
	@When("user configures initial signup settings")
	public void user_configures_initial_signup_settings() {
		String signupCode = FakerUtil.generateAlphanumericSignupCode();
 
		commissionPage.configureInitialSignup(signupCode);
	}
	
	@When("user uploads account manager profile image")
	public void user_uploads_account_manager_profile_image() {
		String imagePath = ScenarioContext.getTestData("ImagePath");
 
		commissionPage.uploadProfileImage(imagePath);
	}
 
	@When("user submits the account manager details")
	public void user_submits_the_account_manager_details() throws InterruptedException {
		commissionPage.clickSubmitAccountManager();
	}
 
	@Then("account manager should be created successfully")
	public void account_manager_should_be_created_successfully() {
		logger.info("Validating Account Manager Creation Success message");
		String expected = JsonDataReader.getJsonNode("AccountManagerData").get("AccountManagerCreationSuccessMessage").asText();
		String actual = commissionPage.getModalMessage();
 
		commissionPage.validateEqualsText(actual, expected);
		commissionPage.clickModalCloseButton();
		commissionPage.clickHomeButton();
	}
	@When("user navigates to Edit Account Manager section")
	public void user_navigates_to_edit_Account_Manager_section() {
		String opCostPerShipmentCost = ScenarioContext.getTestData("OpPerShipment$");
		String opCostRevenuePercent = ScenarioContext.getTestData("OpRevenue%");
		if (opCostPerShipmentCost.isEmpty() && opCostRevenuePercent.isEmpty()) {
		    logger.info("Skipping Ops Cost configuration as no data is provided in Excel.");
		    return;
		}
		commissionPage.navigateToEditAccountManagerSection();
	}
 
	@When("user configures operational costs for shipment types")
	public void user_configures_operational_costs_for_shipment_types() throws Exception {
		String opCostPerShipmentCost = ScenarioContext.getTestData("OpPerShipment$");
		String opCostRevenuePercent = ScenarioContext.getTestData("OpRevenue%");
		String shipmentType = ScenarioContext.getTestData("ShipmentType");
 
		if (opCostPerShipmentCost.isEmpty() && opCostRevenuePercent.isEmpty()) {
		    logger.info("Skipping Ops Cost configuration as no data is provided in Excel.");
		    return;
		}
		commissionPage.clickOperationalCostToggle();
		commissionPage.clickOpCostEditButton(shipmentType);
		
	    if (opCostPerShipmentCost != null && !opCostPerShipmentCost.trim().isEmpty()) {
	        commissionPage.enterOpCostPerShipmentCost(opCostPerShipmentCost,shipmentType);
	    }
	    if (opCostRevenuePercent != null && !opCostRevenuePercent.trim().isEmpty()) {
	        commissionPage.enterOpCostRevenuePercent(opCostRevenuePercent,shipmentType);
	    }
	    commissionPage.clickSaveChangesButton();
	}
	@Then("account manager should be updated successfully")
	public void account_manager_should_be_updated_successfully() {
		logger.info("Validating Account Manager Updaton Success message");
		String opCostPerShipmentCost = ScenarioContext.getTestData("OpPerShipment$");
		String opCostRevenuePercent = ScenarioContext.getTestData("OpRevenue%");
 
		if (opCostPerShipmentCost.isEmpty() && opCostRevenuePercent.isEmpty()) {
		    logger.info("Skipping Ops Cost configuration as no data is provided in Excel.");
		    return;
		}
		String expected = JsonDataReader.getJsonNode("AccountManagerData").get("AccountManagerUpdationSuccessMessage").asText();
		String actual = commissionPage.getModalMessage();
 
		commissionPage.validateEqualsText(actual, expected);
 
		commissionPage.clickModalCloseButton();
	}
	@When("user navigates to Edit Commissions section")
	public void user_navigates_to_edit_commissions_section() {
		commissionPage.navigateToEditCommissions();
	}
 
	@And("user selects account manager in edit account manager")
	public void user_selects_account_manager_in_edit_account_manager() {
		String opCostPerShipmentCost = ScenarioContext.getTestData("OpPerShipment$");
		String opCostRevenuePercent = ScenarioContext.getTestData("OpRevenue%");
		if (opCostPerShipmentCost.isEmpty() && opCostRevenuePercent.isEmpty()) {
		    logger.info("Skipping Ops Cost configuration as no data is provided in Excel.");
		    return;
		}
		
		String accountManager = ScenarioContext.getTestData("ACCOUNTMANAGER","AccountManager");
 
		commissionPage.selectAccountManager(accountManager);
		
		//Write accountManager in excel
	    Map<String, String> data = new HashMap<>();
	    data.put("AccountManager", accountManager); 
	    ExcelUtil.writeCurrentIterationData(data);
	}
 
	@When("user selects commission structure")
	public void user_selects_commission_type() {
		String type = ScenarioContext.getTestData("CommissionStructure");
 
		commissionPage.selectCommissionStructureType(type);
	}
	
	@And("user selects account manager in edit commission")
	public void user_selects_account_manager_in_edit_commission() {
		String accountManager = ScenarioContext.getTestData("ACCOUNTMANAGER","AccountManager");
 
		commissionPage.selectAccountManager(accountManager);
	}
 
	@And("user enters commission details")
	public void user_enters_commission_details() {
		String commissionStructure = ScenarioContext.getTestData("CommissionStructure");
		String grossProfit = ScenarioContext.getTestData("GrossProfit%");
		String revenue = ScenarioContext.getTestData("Revenue%");
		String shipmentAmount = ScenarioContext.getTestData("ShipmentAmount$");
		String shipmentType = ScenarioContext.getTestData("ShipmentType");
		String shipmentArea = ScenarioContext.getTestData("ShipmentArea");	
	    if (commissionStructure.equalsIgnoreCase("Revenue and Shipment Amount")) {
	        commissionPage.enterRevenueAndShipmentCommission(shipmentType, shipmentArea ,revenue,shipmentAmount);
	    } else if (commissionStructure.equalsIgnoreCase("Gross Profit and Shipment Amount")) {
	        commissionPage.enterGrossProfitAndShipmentCommission(shipmentType, shipmentArea,grossProfit,shipmentAmount);
	    }
	}
 
	@When("user saves the commission details")
	public void user_saves_the_commission_details() {
		String shipmentType = ScenarioContext.getTestData("ShipmentType");
		
		commissionPage.clickCommissionRulesSaveChangesButton(shipmentType);
	}
 
	@Then("commission details should be updated successfully")
	public void commission_details_should_be_updated_successfully() {
		logger.info("Validating Commission Update Success message");
 
		String expected = JsonDataReader.getJsonNode("AccountManagerData").get("CommissionRuleUpdationMessage").asText();
		String actual = commissionPage.getModalMessage();
 
		commissionPage.validateEqualsText(actual, expected);		

		commissionPage.clickModalCloseButton();
		commissionPage.clickHomeButton();
	}
 
}