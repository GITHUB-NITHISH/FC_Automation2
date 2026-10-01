package com.automation.steps;
 
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
import com.automation.pages.web.PaymentMethodPage;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.DriverFactory;
import com.automation.utils.FakerUtil;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;
import java.util.Map;
 
public class PaymentMethodStepDef {
	private static final LoggerUtils logger = LoggerUtils.getInstance();
    private WebDriver driver;
    private PaymentMethodPage  paymentMethodPage;
    
    public PaymentMethodStepDef() {
        this.driver = DriverFactory.getDriver();
        this.paymentMethodPage = new PaymentMethodPage(driver);
    }
    @Then("Payment Method page should be loaded")
    public void Payment_Method_page_should_be_loaded() {
    	 logger.info("Navigating to Payment Method page");
    	 assertTrue(paymentMethodPage.ispaymentMethodPageDisplayed());
    }
    @When("user clicks add new payment button")
    public void user_clicks_add_new_payment_button() {
        logger.info("Clicking save account button");
        paymentMethodPage.clickAddNewPaymentButton();
     }
    @When("user enters cheque payment name")
    public void user_enters_cheque_payment_name() {
    	String paymentName = ScenarioContext.getTestData("PaymentName");
    	logger.info("Entering payment name: " + paymentName);
    	paymentMethodPage.typePaymentNameField(paymentName);
    	
    	ScenarioContext.setGlobal("PAYMENTNAME", paymentName);
    }
    @When("user enters first cheque number")
    public void user_enters_first_cheque_number() {
    	String firstChequeNumber = ScenarioContext.getTestData("FirstChequeNumber");
        paymentMethodPage.typeFirstChequeInSeriesField(firstChequeNumber);
    }
    @When("user enters last cheque number")
    public void user_enters_last_cheque_number() {
    	String lastChequeNumber = ScenarioContext.getTestData("LastChequeNumber");
        paymentMethodPage.typeLastChequeInSeriesField(lastChequeNumber);
    }
    @Then("cheque in series count should be displayed correctly")
    public void cheque_in_series_count_should_be_displayed_correctly() {
        logger.info("Validating cheque in series count");
        String firstChequeNumber = ScenarioContext.getTestData("FirstChequeNumber");
    	String lastChequeNumber = ScenarioContext.getTestData("LastChequeNumber");
 
    	String expected = paymentMethodPage.calculateChequeCount(firstChequeNumber, lastChequeNumber);
    	String actual = paymentMethodPage.getChequeInSeries();
    	logger.info("Cheque validation: Expected:" + expected + "|Actual:" + actual);
    	paymentMethodPage.validateEqualsText(actual, expected);
    }
    @When("user enters send reminder cheque count")
    public void user_enters_send_reminder_cheque_count() {
    	String sendReminderChequeCount = ScenarioContext.getTestData("SendReminderChequeCount");
        paymentMethodPage.typeSendReminderWhenChequesRemainField(sendReminderChequeCount);
    }
    @When("user clicks save payment method button")
    public void user_clicks_save_payment_method_button() {
        logger.info("Clicking save payment method button");
        paymentMethodPage.clickSavePaymentMethodButton();
    }
    @Then("cheque payment method creation success message should be displayed")
    public void cheque_payment_method_creation_success_message_should_be_displayed() {
        logger.info("Validating payment method creation success message");
        
    	String expectedMessage = JsonDataReader.getJsonNode("PaymentMethodData").get("PaymentMethodCreationSuccessMessage").asText();
        String actualMessage = paymentMethodPage.getModalMessage();
 
        assertEquals(actualMessage, expectedMessage);
    }
//    @Then("user closes success popup modal")
//    public void user_closes_success_popup_modal() {
//        logger.info("Closing success popup modal");
//        paymentMethodPage.clickSuccessModalCloseButton();
//    }
//    @When("user switches payment tab using currency")
//    public void user_switches_payment_tab_using_currency() {
//        logger.info("Switching payment tab using currency");
//        paymentMethodPage.clickSwitchTab();
//    }
    @When("user selects maximum pagination count")
    public void user_selects_maximum_pagination_count() {
        logger.info("Selecting maximum pagination count");
        paymentMethodPage.selectMaxPaginationValue( "paymentMethodPage", "pageSizeDropdown");
    }
    @Then("newly created cheque payment method should be displayed")
    public void newly_created_cheque_payment_method_should_be_displayed() {
        logger.info("Validating newly created payment method in grid");
        
        String paymentName = ScenarioContext.getTestData("PaymentName");
 
        assertTrue(
                paymentMethodPage.isPaymentMethodDisplayed(paymentName));
    }
    @Then("user clicks home button")
    public void user_clicks_home_button() {
        logger.info("Clicking home button");
        paymentMethodPage.clickHomeButton();
    }
 
}