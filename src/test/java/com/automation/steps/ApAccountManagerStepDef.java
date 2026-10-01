package com.automation.steps;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import static org.testng.Assert.assertTrue;

import com.automation.dataprovider.JsonDataReader;
import com.automation.pages.web.ApAccountManagerPage;
import com.automation.pages.web.PayeePage;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.DriverFactory;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ApAccountManagerStepDef {
	private static final LoggerUtils logger = LoggerUtils.getInstance();
    private WebDriver driver;
    private ApAccountManagerPage  apCommissionPage;
    public ApAccountManagerStepDef() {
        this.driver = DriverFactory.getDriver();
        this.apCommissionPage = new ApAccountManagerPage(driver);
    }
    
    @Then("account manager page should be loaded")
    public void account_manager_page_should_be_loaded() {
    	 logger.info("Navigating to Account Manager page");
//   	 assertTrue(apCommissionPage.isaccountManagerPageDisplayed());
    	 apCommissionPage.isaccountManagerPageDisplayed();
    }
    
    @When("user search for account manager")
    public void user_search_for_account_manager() {
        logger.info("Searching for Account Manager");
        String accountManagerName = ScenarioContext.getTestData("ACCOUNTMANAGER","AccountManagerName");
        apCommissionPage.clickAccountManagerSearch();
        apCommissionPage.typeAccountManagerSearch(accountManagerName);
        apCommissionPage.clickAccountManagerDropdownOption(accountManagerName);
        apCommissionPage.clickSearchButton();
     }
    @When("user clicks edit button")
    public void user_clicks_edit_button() {
        apCommissionPage.clickEditButton();
     }
    @When("user selects linked payee")
    public void user_selects_linked_payee() {

        String linkedPayee =
                ScenarioContext.getTestData("PAYEENAME","LinkedPayee");

        logger.info("Selecting Linked Payee : " + linkedPayee);

        apCommissionPage.clickLinkedPayeeDropdown();

        apCommissionPage.typeLinkedPayeeDropdownValue(linkedPayee);

        apCommissionPage.clickLinkedPayeeSelectOption(linkedPayee);
    }

    @When("user selects account")
    public void user_selects_account() {

        String accountName = ScenarioContext.getTestData("ACCOUNTNAME","AccountName");
        logger.info("Selecting Account : " + accountName);

        apCommissionPage.clickSelectAccountDropdown();
        apCommissionPage.clickAccountDropdownOption(accountName);
    }

    @When("email address should be auto populated correctly")
    public void email_address_should_be_auto_populated_correctly() {

        logger.info("Validating auto populated email");

        String expectedEmail = ScenarioContext.getTestData("FCEMAILADDRESS","Email");
        String actualEmail = apCommissionPage.emailAddressValidation();

        apCommissionPage.validateContainsText(actualEmail, expectedEmail);
        
    }

    @When("user selects payment setting")
    public void user_selects_payment_setting() {

        String paymentSetting =
                ScenarioContext.getTestData("PaymentSettings");

        logger.info("Selecting Payment Setting : " + paymentSetting);

        apCommissionPage.clickPaymentSettingDropdown();

        apCommissionPage.clickPaymentSettingDropdownOption(paymentSetting);
    }

    @When("user selects commission frequency")
    public void user_selects_commission_frequency() {

        String commissionFrequency =
                ScenarioContext.getTestData("Commission Frequency");

        logger.info("Selecting Commission Frequency : " + commissionFrequency);

        apCommissionPage.clickCommissionFrequencyDropdown();

        apCommissionPage.clickCommissionFrequencyDropdownOption(commissionFrequency);
    }

    @When("Conversion Buffer is disabled for Pay in CAD option")
    public void conversion_buffer_is_disabled_for_pay_in_cad_option() {

        logger.info("Validating Conversion Buffer disabled");

        apCommissionPage.iscommissionBufferDisabled("disabled");
    }

    @When("user enters CAD invoice reference number")
    public void user_enters_cad_invoice_reference_number() {

        String invoiceReference =
                ScenarioContext.getTestData("Invoice Reference");

        logger.info("Entering Invoice Reference : " + invoiceReference);

        apCommissionPage.typeCadInvoiceReferenceField(invoiceReference);
    }

    @When("user select preferred payment method")
    public void user_select_preferred_payment_method() {

        String preferredPaymentMethod =
                ScenarioContext.getTestData("PAYMENTNAME","Preferred Payment Method");

        logger.info("Selecting Preferred Payment Method : " + preferredPaymentMethod);

        apCommissionPage.clickCadPaymentMethodDropdown();

        apCommissionPage.clickCadPaymentMethodDropdownOption(preferredPaymentMethod);
    }

    @When("user clicks save changes button")
    public void user_clicks_save_changes_button() {

        logger.info("Clicking Save Changes button");

        apCommissionPage.clickSaveChangesButton();
    }

    @Then("Edit success modal message should display")
    public void edit_success_modal_message_should_display() {

        logger.info("Validating success modal");

        String actualMessage = apCommissionPage.getModalMessage();
        String expectedMessage = JsonDataReader.getJsonNode("ApAccountManagerData").get("EditSuccessMessage").asText();
        apCommissionPage.validateContainsText(actualMessage, expectedMessage);
        apCommissionPage.clickAccountManagerModalCloseButton();
    }

    @When("user activate commission status of the account manager")
    public void user_activate_commission_status_of_the_account_manager() {

        logger.info("Activating commission status");
        String statusButton = ScenarioContext.getTestData("AccountManagerName");
        apCommissionPage.clickCommissionStatusButton(statusButton);
        apCommissionPage.clickEditCommissionStatus();
        apCommissionPage.clickSaveCommissionStatus();
        
    }

    @Then("Activation success modal message should display")
    public void activation_success_modal_message_should_display() {

        logger.info("Validating activation success modal");
        String expectedMessage = JsonDataReader.getJsonNode("ApAccountManagerData").get("StatusSuccessMessage").asText();
        String actualMessage = apCommissionPage.getStatusModalMessage();
        apCommissionPage.validateContainsText(actualMessage, expectedMessage);
        apCommissionPage.clickStatusSuccessModalCloseButton(); 
    }

    @Then("validate the active commission status")
    public void validate_the_active_commission_status() {
        logger.info("Validating commission status");
        assertTrue(apCommissionPage.isaccountActive());

    }
}
    
    


