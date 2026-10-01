package com.automation.steps;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.automation.dataprovider.JsonDataReader;
import com.automation.pages.web.CommonPage;
import com.automation.pages.web.GenerateCommissionReportPage;
import com.automation.utils.DriverFactory;
import com.automation.utils.ScenarioContext;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GenerateCommissionReportStepDef {
	
    private WebDriver driver;
    private GenerateCommissionReportPage  generateCommissionReportPage;
    private CommonPage commonPage;
    
    public GenerateCommissionReportStepDef() {
        this.driver = DriverFactory.getDriver();
        this.generateCommissionReportPage = new GenerateCommissionReportPage(driver);
        this.commonPage = new CommonPage(driver);
    }

    @Then("generate commission report page should be loaded")
   	public void generate_commission_report_page_should_be_loaded() {
    	Assert.assertTrue(generateCommissionReportPage.isGenerateCommissionReportPageDisplayed());   	}

    @When("the user enters valid end date")
   	public void the_user_enters_valid_end_date() {
        String date = ScenarioContext.getTestData("DATE","Date");
         generateCommissionReportPage.typeInvoiceDate(date);
   	}
   	@When("the user selects account manager")
   	public void the_user_selects_account_manager() {
        String accountManager = ScenarioContext.getTestData("ACCOUNTMANAGER", "AccountManager");
        generateCommissionReportPage.selectAccountManager(accountManager);
    }  
   	@When("the user submits the commission report request")
   	public void the_user_submits_the_commission_report_request() {
   		generateCommissionReportPage.clickGenerateButton();
   	}
   	@Then("a report generation success message should be displayed")
   	public void a_report_generation_success_message_should_be_displayed() throws Exception {
   		String expected = JsonDataReader.getJsonNode("GenerateCommissionReportData").get("GenerateCommissionReportSuccessMessage").asText();

   		generateCommissionReportPage.validateGenerateReportModalMessage(expected);
   		generateCommissionReportPage.clickCloseModalButton();
   		
   		ScenarioContext.setGlobal("REPORTID", generateCommissionReportPage.getGeneratedReportId()); 
   		commonPage.clickHomeButton();
   	}
}
