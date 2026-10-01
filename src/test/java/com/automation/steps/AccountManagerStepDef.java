package com.automation.steps;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import static org.testng.Assert.assertTrue;

import com.automation.dataprovider.JsonDataReader;
import com.automation.pages.web.AccountManagerPage;
import com.automation.pages.web.PayeePage;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.DriverFactory;
import com.automation.utils.FakerUtil;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccountManagerStepDef {
    private WebDriver driver;
    private AccountManagerPage  accountManagerPage;
    
    public AccountManagerStepDef() {
        this.driver = DriverFactory.getDriver();
        this.accountManagerPage = new AccountManagerPage(driver);
    }
    
    @Then("account manager page should be loaded")
    public void account_manager_page_should_be_loaded() {
         Assert.assertTrue(accountManagerPage.isAccountManagerPageDisplayed());
    }
    
    @When("user search for account manager")
    public void user_search_for_account_manager() {
        String accountManagerName =
                ScenarioContext.getTestData("ACCOUNTMANAGER", "AccountManagerName");

        accountManagerPage.selectAccountManager(accountManagerName);
        accountManagerPage.clickSearchButton();

    }
  
    @When("user clicks edit account manager button")
    public void user_clicks_edit_account_manager_button() {
        accountManagerPage.clickEditButton();
    }
    
    @When("user selects linked payee")
    public void user_selects_linked_payee() {
        String linkedPayee = ScenarioContext.getTestData("PAYEENAME", "LinkedPayee");
        accountManagerPage.selectLinkedPayee(linkedPayee);
    }

    @When("user selects account")
    public void user_selects_account() {
        String accountName = ScenarioContext.getTestData("ACCOUNTNAME", "AccountName");
        accountManagerPage.selectAccount(accountName);
    }

    @When("email address should be auto populated correctly")
    public void email_address_should_be_auto_populated_correctly() {
        String expectedEmail =ScenarioContext.getTestData("FCEMAILADDRESS", "Email");
        accountManagerPage.validateEmailAddress(expectedEmail);
    }

    @When("user selects payment setting")
    public void user_selects_payment_setting() {
    	String paymentSetting = ScenarioContext.getTestData("PaymentSettings");
        accountManagerPage.selectPaymentSetting(paymentSetting);
    }

    @When("user selects commission frequency")
    public void user_selects_commission_frequency() {
        String commissionFrequency = ScenarioContext.getTestData("Commission Frequency");
        accountManagerPage.selectCommissionFrequency(commissionFrequency);
    }

    @When("Conversion Buffer is disabled for Pay in CAD option")
    public void conversion_buffer_is_disabled_for_pay_in_cad_option() {
        accountManagerPage.validateCommissionBufferDisabled("disabled");
    }

    @When("user enters CAD invoice reference number")
    public void user_enters_cad_invoice_reference_number() {
        String invoiceReference = FakerUtil.generateInvoiceNumber();
        accountManagerPage.typeCadInvoiceReference(invoiceReference);
    }

    @When("user select preferred payment method")
    public void user_select_preferred_payment_method() {
    	String preferredPaymentMethod = ScenarioContext.getTestData("PAYMENTNAME", "PaymentMethod");
        accountManagerPage.selectPreferredPaymentMethod(preferredPaymentMethod);
    }

    @When("user clicks save changes button")
    public void user_clicks_save_changes_button() {
        accountManagerPage.clickSaveChangesButton();
    }

    @Then("Edit success modal message should display")
    public void edit_success_modal_message_should_display() {
    	String expectedMessage = JsonDataReader.getJsonNode("ApAccountManagerData").get("EditSuccessMessage").asText();
        accountManagerPage.validateEditSuccessMessage(expectedMessage);
        accountManagerPage.clickAccountManagerModalCloseButton();
    }

    @When("user activate commission status of the account manager")
    public void user_activate_commission_status_of_the_account_manager() {
    	String accountManager = ScenarioContext.getTestData("AccountManagerName");
        accountManagerPage.activateCommissionStatus(accountManager);
    }

    @Then("Activation success modal message should display")
    public void activation_success_modal_message_should_display() {
    	String expectedMessage = JsonDataReader.getJsonNode("ApAccountManagerData").get("StatusSuccessMessage").toString();

        accountManagerPage.validateStatusSuccessMessage(expectedMessage);
        accountManagerPage.clickStatusSuccessModalCloseButton();
    }

    @Then("validate the active commission status")
    public void validate_the_active_commission_status() {
        Assert.assertTrue(accountManagerPage.isAccountActive());
    }
}
    
    


