package com.automation.steps;
 
import static org.testng.Assert.assertTrue;
 
import java.util.Map;
 
import org.openqa.selenium.WebDriver;
 
import com.automation.dataprovider.ExcelUtil;
import com.automation.dataprovider.JsonDataReader;
import com.automation.pages.web.SalesPage;
import com.automation.utils.CommissionCalculator;
import com.automation.utils.CommissionCalculator.CommissionResult;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.DriverFactory;
import com.automation.utils.FakerUtil;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;
 
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
 
public class SalesStepDef {
	private static final LoggerUtils logger = LoggerUtils.getInstance();
	private WebDriver driver;
	private SalesPage salesPage;
 
	public SalesStepDef() {
		this.driver = DriverFactory.getDriver();
		this.salesPage = new SalesPage(driver);
	}
 
	@Then("Sales dashboard should be loaded")
	public void sales_dashboard_should_be_loaded() {
		if (!ConfigurationManager.isParallelRun() && driver != null) {
			if (driver.getCurrentUrl().contains("sales-commissions"))
				return;
		}
		logger.info("Navigating to Sales Dashboard");
		assertTrue(salesPage.isSalesDashboardLoaded());
	}
 
	@When("user navigates to Commissions under Sales menu")
	public void user_navigates_to_commissions_under_sales_menu() {
		salesPage.navigateToCommissionsModule();
	}
 
	@When("user accesses Create New Account Manager section")
	public void user_accesses_create_new_account_manager_section() {
		salesPage.clickCreateNewAccountManagerSection();
	}
 
	@When("user defines account manager role as {string}")
	public void user_defines_account_manager_role_as(String accountManagerType) {
		salesPage.selectAccountManagerType(accountManagerType);
	}
 
	@When("user associates an existing Freightcom user to the account manager")
	public void user_associates_an_existing_freightcom_user_to_the_account_manager() {
		String firstName = ScenarioContext.getTestData("FIRSTNAME","FirstName");
		String lastName = ScenarioContext.getTestData("LASTNAME","LastName");
		String existingFreightcomUser = firstName + " " + lastName;
 
		salesPage.selectExistingFreightcomUser(existingFreightcomUser);
	}
 
	@When("user assigns the account manager to a sales team")
	public void user_assigns_the_account_manager_to_a_sales_team() {
		String salesTeam = ScenarioContext.getTestData("SalesTeam");
 
		salesPage.assignToSalesTeam(salesTeam);
	}
 
	@When("user defines account manager start date")
	public void user_defines_account_manager_start_date() {
		String startDate = ScenarioContext.getTestData("StartDate");
 
		salesPage.enterStartDate(startDate);
	}
 
	@When("user verifies account manager personal details")
	public void user_verifies_account_manager_personal_details() {
		String firstName = ScenarioContext.getTestData("FIRSTNAME","FirstName");
		String lastName = ScenarioContext.getTestData("LASTNAME","LastName");
 
		salesPage.verifyAutopopulatedName(firstName,lastName);
 
		ScenarioContext.setGlobal("ACCOUNTMANAGER", firstName + " " + lastName);
	}
 
	@When("user provides account manager contact details")
	public void user_provides_account_manager_contact_details() {
		String workPhone = ScenarioContext.getTestData("WorkPhone");
		String extension = ScenarioContext.getTestData("Extension");
		String cellPhone = ScenarioContext.getTestData("CellPhone");
		String fcEmailAddress = ScenarioContext.getTestData("FCEMAILADDRESS","FCEmailAddress");
		String personalEmailAddress = ScenarioContext.getTestData("PersonalEmailAddress");
		salesPage.enterContactDetails(workPhone, extension, cellPhone, fcEmailAddress, personalEmailAddress);
 
	}
 
	@When("user verifies account manager login credentials")
	public void user_verifies_account_manager_login_credentials() {
		String userName = ScenarioContext.getTestData("USERNAME","UserName");
 
		salesPage.verifyAutoPopulatedUsername(userName);
	}
 
	@When("user assigns role permissions for the account manager")
	public void user_assigns_role_permissions_for_the_account_manager() {
		String role = ScenarioContext.getTestData("Role");
 
		salesPage.assignRolePermission(role);
	}
 
	@When("user configures initial signup settings")
	public void user_configures_initial_signup_settings() {
		String signupCode = ScenarioContext.getTestData("SignupCode");
 
		salesPage.configureInitialSignup(signupCode);
	}
 
	@When("user uploads account manager profile image")
	public void user_uploads_account_manager_profile_image() {
		String imagePath = ScenarioContext.getTestData("ImagePath");
 
		salesPage.uploadProfileImage(imagePath);
	}
 
	@When("user submits the account manager details")
	public void user_submits_the_account_manager_details() throws InterruptedException {
		salesPage.clickSubmitAccountManager();
	}
 
	@Then("account manager should be created successfully")
	public void account_manager_should_be_created_successfully() {
		logger.info("Validating Account Manager Creation Success message");
		String expected = JsonDataReader.getJsonNode("AccountManagerData").get("AccountManagerCreationSuccessMessage").asText();
		String actual = salesPage.getModalMessage();
 
		salesPage.validateEqualsText(actual, expected);
		salesPage.clickModalCloseButton();
		salesPage.clickHomeButton();
	}
	@When("user navigates to Edit Account Manager section")
	public void user_navigates_to_edit_Account_Manager_section() {
		String opCostPerShipmentCost = ScenarioContext.getTestData("OpPerShipment$");
		String opCostRevenuePercent = ScenarioContext.getTestData("OpRevenue%");
		if (opCostPerShipmentCost.isEmpty() && opCostRevenuePercent.isEmpty()) {
		    logger.info("Skipping Ops Cost configuration as no data is provided in Excel.");
		    return;
		}
		salesPage.navigateToEditAccountManagerSection();
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
		salesPage.clickOperationalCostToggle();
		salesPage.clickOpCostEditButton(shipmentType);
	    if (opCostPerShipmentCost != null && !opCostPerShipmentCost.trim().isEmpty()) {
	        salesPage.enterOpCostPerShipmentCost(opCostPerShipmentCost,shipmentType);
	    }
	    if (opCostRevenuePercent != null && !opCostRevenuePercent.trim().isEmpty()) {
	        salesPage.enterOpCostRevenuePercent(opCostRevenuePercent,shipmentType);
	    }
	    salesPage.clickSaveChangesButton();
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
		String actual = salesPage.getModalMessage();
 
		salesPage.validateEqualsText(actual, expected);
 
		salesPage.clickModalCloseButton();
	}
	@When("user navigates to Edit Commissions section")
	public void user_navigates_to_edit_commissions_section() {
		salesPage.navigateToEditCommissions();
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
 
		salesPage.selectAccountManager(accountManager);
	}
 
	@When("user selects commission structure")
	public void user_selects_commission_type() {
		String type = ScenarioContext.getTestData("CommissionStructure");
 
		salesPage.selectCommissionStructureType(type);
	}
	
	@And("user selects account manager in edit commission")
	public void user_selects_account_manager_in_edit_commission() {
		String accountManager = ScenarioContext.getTestData("ACCOUNTMANAGER","AccountManager");
 
		salesPage.selectAccountManager(accountManager);
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
	        salesPage.enterRevenueAndShipmentCommission(shipmentType, shipmentArea ,revenue,shipmentAmount);
	    } else if (commissionStructure.equalsIgnoreCase("Gross Profit and Shipment Amount")) {
	        salesPage.enterGrossProfitAndShipmentCommission(shipmentType, shipmentArea,grossProfit,shipmentAmount);
	    }
	}
 
	@When("user saves the commission details")
	public void user_saves_the_commission_details() {
		String shipmentType = ScenarioContext.getTestData("ShipmentType");
		
		salesPage.clickCommissionRulesSaveChangesButton(shipmentType);
	}
 
	@Then("commission details should be updated successfully")
	public void commission_details_should_be_updated_successfully() {
		logger.info("Validating Commission Update Success message");
 
		String expected = JsonDataReader.getJsonNode("AccountManagerData").get("CommissionRuleUpdationMessage").asText();
		String actual = salesPage.getModalMessage();
 
		salesPage.validateEqualsText(actual, expected);
		
		double totalCost = 1000.0;
		String tax = "5.0";
		double carrierCost = 400.0;
		
		Map<String, String> rowData = ScenarioContext.getCurrentTestData();
		
		CommissionResult result = CommissionCalculator.calculateFromMap(totalCost, tax, carrierCost, rowData);
		
		logger.info("Calculated Commission: $" + result.getCommission());
		logger.info("Calculated Commissionable Amount: $" + result.getCommissionableAmount());
		logger.info("Net Revenue : $" + result.getNetRevenue());
		logger.info("Gross Profit : $" + result.getGrossProfit());
		logger.info("OPS amount : $" + result.getOpsAmount());
		
		// Write back to Excel 
		ExcelUtil.writeCurrentIterationData(result.toExcelDataMap());
		
		salesPage.clickModalCloseButton();
		salesPage.clickHomeButton();
	}
 
}