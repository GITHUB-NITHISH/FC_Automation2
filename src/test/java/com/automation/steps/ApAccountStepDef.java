package com.automation.steps;
 
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
 
import org.openqa.selenium.WebDriver;
 
import com.automation.dataprovider.JsonDataReader;
import com.automation.pages.web.ApAccountPage;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.DriverFactory;
import com.automation.utils.FakerUtil;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;
 
public class ApAccountStepDef {
	private static final LoggerUtils logger = LoggerUtils.getInstance();
	private WebDriver driver;
	private ApAccountPage apAccountPage;
 
	public ApAccountStepDef() {
		this.driver = DriverFactory.getDriver();
		this.apAccountPage = new ApAccountPage(driver);
	}
 
	@Then("AP Account page should be loaded")
	public void ap_account_page_should_be_loaded() {
//		if (!ConfigurationManager.isParallelRun() && driver != null) {
//			if (driver.getCurrentUrl().contains("accounts"))
//				return;
//		}
		logger.info("Navigating to Ap Account page");
		assertTrue(apAccountPage.isApAccountsPageDisplayed());
	}
 
	@Given("user opens create new AP account modal")
	public void user_opens_create_new_ap_account_modal() {
		logger.info("User clicks create ap account button");
		apAccountPage.openCreateNewAccountModal();
	}
 
	@When("user enters unique account name")
	public void user_enters_unique_account_name() {
//    	 Map<String, String> data = ScenarioContext.getMap("TC01_data");
//    	 String accountName = data.getOrDefault("AccountName", "");
 
//    	Map<String, String> data = ScenarioContext.getCurrentTestData();
		String accountName = ScenarioContext.getTestData("AccountName");
 
		if (ConfigurationManager.isParallelRun()) {
			accountName = FakerUtil.generateAccountName();
//    	        data.put("AccountName", accountName);
			logger.info("Using Faker Account Name: " + accountName);
			ScenarioContext.set("ACCOUNTNAME", accountName);
		}
		logger.info("User enter valid account Name: " + accountName);
		apAccountPage.enterAccountName(accountName);
 
		ScenarioContext.setGlobal("ACCOUNTNAME", accountName);
	}
 
	@When("user saves the AP account")
	public void user_saves_the_ap_account() {
		logger.info("Clicking save account button");
		apAccountPage.clickSaveAccount();
	}
 
	@Then("Account creation success message should be displayed")
	public void account_creation_success_message_should_be_displayed() {
		logger.info("Validating Account Creation Success message");
		String accountName = ScenarioContext.getGlobal("ACCOUNTNAME").toString();
 
		String expected = JsonDataReader.getJsonNode("ApAccountData").get("AccountCreationSuccessMessage").asText()
				.replace("<AccountName>", accountName);
		String actual = apAccountPage.getModalMessage();
 
		apAccountPage.validateEqualsText(actual, expected);
	}
 
	@Then("user closes the popup modal")
	public void user_closes_the_popup_modal() {
		logger.info("Click close button");
		apAccountPage.clickCloseButton();
	}
 
	@Then("user navigates to dashboard page")
	public void user_navigates_to_dashboard_page() {
		apAccountPage.clickHomeButton();
	}
 
	@Then("user clicks clear filter button")
	public void user_clicks_clear_filter_button() {
		apAccountPage.clickClearFilter();
	}
 
	@When("user enters existing account name in the account name field")
	public void user_enters_existing_account_name_in_the_account_name_field() {
//          String AccountName = ScenarioContext.getMap("TC01_data").getOrDefault("AccountName", "");
		String accountName = ScenarioContext.getTestData("ACCOUNTNAME", "AccountName");
 
		logger.info("User enters existing account name: " + accountName);
		apAccountPage.enterAccountName(accountName);
	}
 
	@Then("existing account error message should be displayed")
	public void existing_account_error_message_should_be_displayed() {
		logger.info("Validating existing account error message");
 
		String actual = apAccountPage.getExistingAccountErrorMessage();
		String expected = JsonDataReader.getJsonNode("ApAccountData").get("ExistingAccountErrorMessage").asText();
 
		apAccountPage.validateEqualsText(actual, expected);
	}
 
	@When("user searches for account")
	public void user_searches_for_account() throws InterruptedException {
		String accountName = ScenarioContext.getTestData("ACCOUNTNAME", "AccountName");
		apAccountPage.searchForAccount(accountName);
 
		int linkedPayeesCount = apAccountPage.getLinkedPayeesCount(accountName);
		ScenarioContext.set("LINKEDPAYEESCOUNT", linkedPayeesCount);
	}
 
	@When("user opens account ellipsis menu")
	public void user_opens_account_ellipsis_menu() {
		String accountName = ScenarioContext.getTestData("ACCOUNTNAME", "AccountName");
		apAccountPage.clickEllipsisButton(accountName);
	}
 
	@When("user selects duplicate account in menu option")
	public void user_selects_duplicate_account_in_menu_option() {
		apAccountPage.clickDuplicateAccountOption();
	}
 
	@When("user enters the account name")
	public void user_enters_the_account_name() {
		String duplicateAccountName = ScenarioContext.getTestData("DuplicateAccountName");
		apAccountPage.enterAccountNameInDuplicateModal(duplicateAccountName);
	}
 
	@When("user clicks save changes button in AP account module")
	public void user_clicks_save_changes_button_in_ap_account_module() {
		apAccountPage.clickSaveChanges();
	}
 
	@Then("duplication success message should be displayed")
	public void duplication_success_message_should_be_displayed() {
		String duplicateAccountName = ScenarioContext.getTestData("DuplicateAccountName");
 
		String actual = apAccountPage.getModalMessage();
		String expected = JsonDataReader.getJsonNode("ApAccountData").get("DuplicateModalMessage").asText()
				.replace("<DuplicateAccountName>", duplicateAccountName);
 
		apAccountPage.validateEqualsText(actual, expected);
	}
 
	@Then("user clicks cancel button")
	public void user_clicks_cancel_button() {
		logger.info("Click cancel button");
		apAccountPage.clickCancelButton();
	}
 
	@Then("duplication error message should be displayed")
	public void duplication_error_message_should_be_displayed() {
		logger.info("Validating duplication error message");
 
		String actual = apAccountPage.getErrorModalMessage();
		String expected = JsonDataReader.getJsonNode("ApAccountData").get("DuplicateAccountErrorMessage").asText();
 
		apAccountPage.validateEqualsText(actual, expected);
	}
 
	@When("user enters AP account name in search Accounts field")
	public void user_enters_ap_account_name_in_search_accounts_field() {
		String accountName = ScenarioContext.getTestData("AccountName");
 
		apAccountPage.enterSearchAccountName(accountName);
	}
 
	@When("user clicks the search icon button")
	public void user_clicks_the_search_icon_button() throws InterruptedException {
		apAccountPage.clickSearchIcon();
	}
 
	@Then("the search results should include the corresponding account")
	public void the_search_results_should_include_the_corresponding_account() throws InterruptedException {
		String accountName = ScenarioContext.getTestData("AccountName");
 
		assertTrue(apAccountPage.isAccountListed(accountName), "Account not found in search results: " + accountName);
	}
 
	@When("user selects payee name from dropdown")
	public void user_selects_payee_name_from_dropdown() {
		String payeeName = ScenarioContext.getTestData("PayeeNames");
 
		apAccountPage.openPayeeDropdown();
		apAccountPage.selectPayee(payeeName);
	}
 
	@When("user clicks the search button")
	public void user_clicks_the_search_button() throws InterruptedException {
		apAccountPage.clickSearchButton();
	}
 
	@When("user selects archive account in menu option")
	public void user_selects_archive_account_in_menu_option() {
		apAccountPage.clickArchiveAccountOption();
	}
 
	@Then("Archived success message should be displayed")
	public void archived_success_message_should_be_displayed() {
		String actual = apAccountPage.getModalMessage();
		String expected = JsonDataReader.getJsonNode("ApAccountData").get("ArchiveModalMessage").asText();
 
		apAccountPage.validateEqualsText(actual, expected);
	}
 
	@Then("the account should be displayed as archived")
	public void the_account_should_be_displayed_as_archived() {
		String accountName = ScenarioContext.getTestData("ACCOUNTNAME", "AccountName");
 
		boolean isGreyed = apAccountPage.isAccountGreyedOut(accountName);
		assertTrue(isGreyed, "Account is not greyed out after archive: " + accountName);
	}
 
	@Then("user selects restore account in menu option")
	public void user_selects_restore_account_in_menu_option() {
		logger.info("Clicking restore account option");
		apAccountPage.clickRestore();
	}
 
	@Then("restore success message should be displayed")
	public void restore_success_message_should_be_displayed() {
		logger.info("Validating restore success message");
		String actual = apAccountPage.getModalMessage();
		String expected = JsonDataReader.getJsonNode("ApAccountData").get("RestoreModalMessage").asText();
 
		apAccountPage.validateEqualsText(actual, expected);
	}
 
	@Then("the account should be displayed as active")
	public void the_account_should_be_displayed_as_active() {
		String accountName = ScenarioContext.getTestData("ACCOUNTNAME", "AccountName");
		logger.info("Validating account is active (white state): " + accountName);
		boolean isActive = !apAccountPage.isAccountGreyedOut(accountName);
 
		assertTrue(isActive, "Account is not active (white) after restore: " + accountName);
	}
 
	@Then("delete option should not be visible for active account")
	public void delete_option_should_not_be_visible_for_active_account() {
		assertFalse(apAccountPage.isDeleteOptionVisible(), "Delete option is visible for active account");
	}
 
	@When("user archives the account")
	public void user_archives_the_account() {
		apAccountPage.clickArchiveAccountOption();
		apAccountPage.clickCloseButton();
	}
 
	@Then("delete option should be visible for archived account")
	public void delete_option_should_be_visible_for_archived_account() {
		assertTrue(apAccountPage.isDeleteOptionVisible(), "Delete option is not visible for archived account");
	}
 
	@Then("user selects delete account in menu option")
	public void user_selects_delete_account_in_menu_option() {
		apAccountPage.clickDeleteAccountOption();
	}
 
	@Then("delete confirmation message should be displayed")
	public void delete_confirmation_message_should_be_displayed() {
		int linkedPayeesCount = ScenarioContext.get("LINKEDPAYEESCOUNT");
 
		String actual = apAccountPage.getModalMessage().replaceAll("\\s+", " ").trim();
		String expected = JsonDataReader.getJsonNode("ApAccountData").get("AccountDeletionConfirmationMessage").asText()
				.replaceAll("\\s+", " ").replace("<count>", String.valueOf(linkedPayeesCount)).trim();
 
		apAccountPage.validateEqualsText(actual, expected);
	}
 
	@Then("user confirms account deletion")
	public void user_confirms_account_deletion() {
		apAccountPage.clickDeleteAccountButton();
	}
 
	@Then("deletion success message should be displayed")
	public void deletion_success_message_should_be_displayed() {
		String actual = apAccountPage.getModalMessage();
		String expected = JsonDataReader.getJsonNode("ApAccountData").get("AccountDeletionSuccessMessage").asText();
 
		apAccountPage.validateEqualsText(actual, expected);
	}
 
	@Then("user verifies account not listed in search results")
	public void user_verifies_account_not_listed_in_search_results() throws InterruptedException {
		String accountName = ScenarioContext.getTestData("ACCOUNTNAME", "AccountName");
 
		apAccountPage.searchForAccount(accountName);
		assertFalse(apAccountPage.isAccountListed(accountName), "Account not found in search results: " + accountName);
	}
 
	@When("user creates new AP account")
	public void user_creates_new_ap_account() throws InterruptedException {
		String accountName = ScenarioContext.getTestData("AccountName");
		apAccountPage.createApAccount(accountName);
		apAccountPage.clickHomeButton();
		
		ScenarioContext.set("PAYABLEACCOUNTNAME", accountName);
	}
 
	@When("user creates payable with AP account")
	public void user_creates_payable_with_ap_account() throws InterruptedException {
		logger.info("Creating payable with ap account");
 
		String payeeName = ScenarioContext.getTestData("PayeeName");
		String accountName = ScenarioContext.getTestData("AccountName");
		String invoiceDate = ScenarioContext.getTestData("InvoiceDate");
		String payDate = ScenarioContext.getTestData("PayDate");
		String invoiceNumber = ScenarioContext.getTestData("InvoiceNumber");
		String referenceValue = ScenarioContext.getTestData("ReferenceValue");
		String invoiceAmount = ScenarioContext.getTestData("InvoiceAmount");
 
		apAccountPage.createPayable(payeeName, accountName, invoiceDate, payDate, invoiceNumber, referenceValue,
				invoiceAmount);
	}
	
	@When("user searches for payable account")
	public void user_searches_for_payable_account() throws InterruptedException {
		String accountName = ScenarioContext.getTestData("AccountName");
		apAccountPage.searchForAccount(accountName);
	}
	
	@When("user opens account ellipsis menu of payable account")
	public void user_opens_account_ellipsis_menu_of_payable_account() {
		String accountName = ScenarioContext.getTestData("AccountName");
		apAccountPage.clickEllipsisButton(accountName);
	}
 
	@Then("restriction message should be displayed")
	public void restriction_message_should_be_displayed() {
		logger.info("Validating restriction message for payable account deletion");
		String actual = apAccountPage.getModalMessage();
		String expected = JsonDataReader.getJsonNode("ApAccountData").get("DeleteRestrictionMessage").asText();
 
		apAccountPage.validateEqualsText(actual, expected);
	}
}