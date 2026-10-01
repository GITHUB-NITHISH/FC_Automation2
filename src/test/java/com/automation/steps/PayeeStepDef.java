package com.automation.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.bouncycastle.jcajce.provider.symmetric.AES.AlgParamGenCCM;
import org.openqa.selenium.WebDriver;

import com.automation.dataprovider.ExcelUtil;
import com.automation.dataprovider.JsonDataReader;
import com.automation.pages.web.ApAccountPage;
import com.automation.pages.web.PayeePage;
import com.automation.pages.web.PaymentMethodPage;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.DriverFactory;
import com.automation.utils.FakerUtil;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;
import java.util.Map;

public class PayeeStepDef {
	private static final LoggerUtils logger = LoggerUtils.getInstance();
	private WebDriver driver;
	private PayeePage payeePage;

	public PayeeStepDef() {
		this.driver = DriverFactory.getDriver();
		this.payeePage = new PayeePage(driver);
	}

	@Then("Payee page should be loaded")
	public void Payee_page_should_be_loaded() {
		logger.info("Navigating to Payment Method page");
		assertTrue(payeePage.ispayeePageDisplayed());
	}

	@When("user clicks add new payee button")
	public void user_clicks_add_new_payee_button() {
		logger.info("Clicking save account button");
		payeePage.clickAddNewPayeeButton();
	}

	@And("user enters payee name")
	public void user_enters_payee_name() {
		String payeeName = ScenarioContext.getTestData("PayeeName");
		payeePage.typePayeeNameField(payeeName);
		
		ScenarioContext.setGlobal("PAYEENAME", payeeName);
	}

	@When("user enters mailing address")
	public void user_enters_mailing_address() {
		String mailingAddress = ScenarioContext.getTestData("MailingAddress");

		logger.info("Mailing Address from Excel : " + mailingAddress);
		payeePage.typeMailingAddressField(mailingAddress);
	}

	@And("user validates mailing address")
	public void user_validates_mailing_address() {
		logger.info("Validating mailing address");

		payeePage.clickValidateAddressButton();
	}

	@When("user enters postal code")
	public void user_enters_postal_code() {
		String postalCode = ScenarioContext.getTestData("PostalCode");

		logger.info("Postal Code from Excel : " + postalCode);

		payeePage.typePostalCodeField(postalCode);
	}

	@Then("adress should be auto populated correctly")
	public void adress_should_be_auto_populated_correctly() {

		String city = ScenarioContext.getTestData("City");
		String province = ScenarioContext.getTestData("Province");
		String country = ScenarioContext.getTestData("Country");

		logger.info("Validating auto populated fields");
		payeePage.isCityFieldAutoPopulated(city);
		payeePage.isprovinceFieldAutoPopulated(province);
		payeePage.isCountryFieldAutoPopulated(country);
	}

	@When("user enters contact details")
	public void user_enters_contact_details() {

		String contactName = ScenarioContext.getTestData("ContactName");
		logger.info("Contact Name from Excel : " + contactName);
		payeePage.typeContactNameField(contactName);
	}

	@When("user selects preferred payment method")
	public void user_selects_preferred_payment_method() {

		String paymentMethod = ScenarioContext.getTestData("PAYMENTNAME","PaymentMethod");

		logger.info("Selecting preferred payment methods");

		payeePage.clickPreferredCadPaymentMethodDropdown();
		payeePage.typeCadPaymentMethodDropdownSearch(paymentMethod);
		payeePage.clickCadPaymentMethodSearchResult(paymentMethod);
	}

	@When("user selects AP account")
	public void user_selects_AP_account() {
		String account = ScenarioContext.getTestData("ACCOUNTNAME","ApAccount");

		logger.info("AP Account  : " + account);
		payeePage.clickSelectAccountDropdown();
		payeePage.typeSelectAccountDropdownSearch(account);
		payeePage.clickSelectAccountSearchResult(account);
	}

	@When("user clicks save payee button")
	public void user_clicks_save_payee_button() {
		logger.info("Clicking Save Payee button");
		payeePage.clickSavePayeeButton();
	}

	@Then("payee creation success message should be displayed")
	public void payee_creation_success_message_should_be_displayed() {
		String payeeName = ScenarioContext.getTestData("PAYEENAME","PayeeName");
		String expectedMessage = JsonDataReader.getJsonNode("PayeeData").get("PayeeCreationSuccessMessage").asText()
				.replace("<PayeeName>", payeeName);

		logger.info("Validating payee creation success message");

		String actualMessage = payeePage.getModalMessage();
		payeePage.validateContainsText(actualMessage, expectedMessage);
	}

	@Then("user closes success popup modal")
	public void user_closes_success_popup_modal() {

		logger.info("Closing success popup modal");

		payeePage.clickCloseButton();
	}

	@And("user searches for created payee")
	public void user_searches_for_created_payee() {

		String payeeName = ScenarioContext.getTestData("PAYEENAME","PayeeName");

		logger.info("Searching created payee : " + payeeName);

		payeePage.clickPayeeDetailsDropdown();
		payeePage.typePayeeDetailsSearchField(payeeName);
		payeePage.clickPayeeDetailsOption(payeeName);
		payeePage.clickSearchButton();
	}

	@Then("created payee should be displayed in search results")
	public void created_payee_should_be_displayed_in_search_results() {

		String payeeName = ScenarioContext.getTestData("PAYEENAME","PayeeName");

		logger.info("Validating created payee in search results");

		assertTrue(payeePage.isPayeeDisplayed(payeeName));
	}

}
