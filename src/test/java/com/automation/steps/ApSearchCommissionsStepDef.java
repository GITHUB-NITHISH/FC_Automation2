package com.automation.steps;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import static org.testng.Assert.assertTrue;

import com.automation.dataprovider.JsonDataReader;
import com.automation.pages.web.ApSearchCommissionsPage;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.DriverFactory;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ApSearchCommissionsStepDef {
	private static final LoggerUtils logger = LoggerUtils.getInstance();
    private WebDriver driver;
    private ApSearchCommissionsPage  apSearchCommissionsPage;
    public ApSearchCommissionsStepDef() {
        this.driver = DriverFactory.getDriver();
        this.apSearchCommissionsPage = new ApSearchCommissionsPage(driver);
    }
    
    @Then("search commissions page should be loaded")
    public void search_commissions_page_should_be_loaded() {
    	 logger.info("Navigating to Search Commissions page");
    	 apSearchCommissionsPage.issearchCommissionspageDisplayed();
    }

    @Then("user search commissions with transaction number and date")
    public void user_search_commissions_with_transaction_number_and_date() {
    	logger.info("Searching for Commissions");
    	apSearchCommissionsPage.clickDueRadioButton();
    }

}
