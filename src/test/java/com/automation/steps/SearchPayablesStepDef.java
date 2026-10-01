package com.automation.steps;
 
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
 
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.automation.dataprovider.ExcelUtil;
import com.automation.dataprovider.JsonDataReader;
import com.automation.pages.web.CommonPage;
import com.automation.pages.web.SearchPayablesPage;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.DriverFactory;
import com.automation.utils.FakerUtil;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;
 
import static org.testng.Assert.*;

import java.util.Map;
 
public class SearchPayablesStepDef {
 
    private WebDriver driver;
    private SearchPayablesPage searchPayablesPage;
    private CommonPage commonPage;
    
	Map<String, String> paymentMethodData = ExcelUtil.getRowDataByTestCaseID("PaymentMethodData", "TC_PM_001");
	Map<String, String> payeeData = ExcelUtil.getRowDataByTestCaseID("PayeeData", "TC_PY_001");

 
    public SearchPayablesStepDef() {
        this.driver = DriverFactory.getDriver();
        this.searchPayablesPage = new SearchPayablesPage(driver);
        this.commonPage = new CommonPage(driver);
    }
 
 
    // ✅ Page Load
    @Then("Search Payables page should be loaded")
    public void search_payables_page_should_be_loaded() {
        assertTrue(searchPayablesPage.isSearchPayablesPageDisplayed());
    }
 
    // ✅ Enter Payee
    @When("user enters payee name and date for search payables")
    public void user_enters_payee_name_and_date_for_search_payables() {
//     	String payeeName = ScenarioContext.getTestData("PAYEENAME", "PayeeName");
    	String date = ScenarioContext.getTestData("DATE", "InvoiceDate");
    	String payeeName = ConfigurationManager.isE2EMode()?payeeData.get("PayeeName"):ScenarioContext.getTestData("PayeeName");
    	
        searchPayablesPage.enterPayeeName(payeeName);
        searchPayablesPage.selectDateRange(date);
 
        ScenarioContext.setGlobal("PAYEENAME", payeeName);
    }
 
 
    // ✅ Click Search
    @When("user clicks on Search button")
    public void user_clicks_on_search_button() {
        searchPayablesPage.clickSearchButton();
    }
    @Then("user validates search results")
    public void user_validates_search_results() throws InterruptedException {
    	
    	String payeeName = ScenarioContext.getTestData("PAYEENAME","PayeeName");
    	String invoice = ConfigurationManager.isE2EMode()? "Commission Report "+ScenarioContext.getGlobal("REPORTID").toString()
    			            :ScenarioContext.getTestData("Invoice");
        String invoiceDate = ScenarioContext.getTestData("DATE","InvoiceDate");
        String paymentDate = ScenarioContext.getTestData("PaymentDate");
        String invoiceAmounts = ScenarioContext.getTestData("TOTALCOMMISSION","InvoiceAmounts");
        String paymentTotal = ScenarioContext.getTestData("TOTALCOMMISSION","PaymentTotal");
//        String paymentMethod = ScenarioContext.getTestData("PAYMENTMETHODNAME","PaymentMethod");
        String paymentMethod = ConfigurationManager.isE2EMode()? paymentMethodData.get("PaymentMethod")
        		                       :ScenarioContext.getTestData("PaymentMethod");
 
        Assert.assertTrue(searchPayablesPage.verifyInvoiceNumber(invoice));
        searchPayablesPage.verifyPayeeName(payeeName,invoice);
        Assert.assertTrue(searchPayablesPage.isStatusUnpaidDisplayed());
        searchPayablesPage.verifyInvoiceDate(invoiceDate,invoice);
        searchPayablesPage.verifyPaymentDate(paymentDate,invoice);
        searchPayablesPage.verifyInvoiceAmounts(invoiceAmounts,invoice);
        Assert.assertTrue(searchPayablesPage.verifyNoTaxField(invoice));
        searchPayablesPage.verifyPaymentTotal(paymentTotal,invoice);
        searchPayablesPage.verifyPaymentMethod(paymentMethod,invoice);
        
        ScenarioContext.setGlobal("INVOICE", invoice);
    }
    
    @When("user selects payable")
    public void user_selects_payable() {
        String invoice = ScenarioContext.getFromContext("INVOICE");
        searchPayablesPage.selectPayableByInvoice(invoice);
    }

    @When("user clicks on Print Cheque button")
    public void user_clicks_on_print_cheque_button() {
        searchPayablesPage.clickPrintChequeButton();
    }
 
    @Then("print cheque screen should be displayed")
    public void print_cheque_screen_should_be_displayed() {
        Assert.assertTrue(searchPayablesPage.isPrintChequePageDisplayed());
    }
    
    @Then("user selects payable in print cheque")
    public void user_selects_payable_in_print_cheque() {
    	String payeeName = ScenarioContext.getTestData("PAYEENAME","PayeeName");
    	searchPayablesPage.clickPayableCheckbox(payeeName);
    }
 
    // ✅ Click Close Off
    @Then("user clicks on Close Off button")
    public void user_clicks_on_close_off_button() {
    	searchPayablesPage.clickCloseOffButton();
    }
 
    // ✅ Enter Cheque Number
    @Then("user enters cheque number")
    public void user_enters_cheque_number() {
    	String StartCheque = paymentMethodData.get("FirstChequeNumber");
    	String LastCheque = paymentMethodData.get("LastChequeNumber");
        String chequeNumber = ConfigurationManager.isE2EMode()?FakerUtil.generateRandomChequeNumber(StartCheque, LastCheque)
        		:ScenarioContext.getTestData("UnUsedChequeNumber");
        
        searchPayablesPage.enterChequeNumber(chequeNumber);
    }
 
    // ✅ Select Payment Date
    @Then("user selects payment Date")
    public void user_selects_payment_date() {
        String paymentDate = ScenarioContext.getTestData("PAYMENTDATE","PaymentDate");
        searchPayablesPage.selectPaymentDate(paymentDate);
    }
 
    // ✅ Click Generate
    @Then("user clicks on Generate button")
    public void user_clicks_on_generate_button() {
    	searchPayablesPage.clickGenerateButton();
    }
 
    // ✅ Verify Success
    @Then("cheque should be generated successfully")
    public void cheque_should_be_generated_successfully() {
    	String expected = JsonDataReader.getJsonNode("SearchPayablePrintChequeData").get("PrintChequeSuccessMessage").asText().trim();
    	searchPayablesPage.validateGenerateChequeSuccessMessage(expected);
    	commonPage.clickHomeButton();
    }

}
