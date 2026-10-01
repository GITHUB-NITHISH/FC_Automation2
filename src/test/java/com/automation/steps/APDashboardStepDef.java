package com.automation.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import com.automation.pages.web.APDashboardPage;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.DriverFactory;
import com.automation.utils.LoggerUtils;
import com.automation.utils.PerformanceUtils;

import static org.testng.Assert.*;

import java.util.Map;

/**
 * DashboardStepDef - Step definitions for Dashboard scenarios
 */
public class APDashboardStepDef {
    private static final LoggerUtils logger = LoggerUtils.getInstance();
    private WebDriver driver;
    private APDashboardPage dashboardPage;

    public APDashboardStepDef() {
    	  this.driver = DriverFactory.getDriver();
          this.dashboardPage = new APDashboardPage(driver);
    }

    @Then("AP dashboard should be loaded")
    public void ap_dashboard_should_be_loaded() throws InterruptedException {

    	 logger.info("Navigating to Ap Dashboard page");
    	 assertTrue(dashboardPage.isDashboardLoaded());
    }

    @When("user clicks AP Account tile")
    public void user_clicks_ap_account_tile() throws InterruptedException {

    	 logger.info("User clicks ap account tile");
    	 dashboardPage.clickApAccountTile();
    }
    
    @Then("Calculate page load time")
    public void validatePerformance() {

        Map<String, Object> metrics = PerformanceUtils.getMetrics(driver);
        long loadTime = (Long) metrics.get("PageLoadTime");

        logger.info("Load Time : " +  loadTime);
//        logger.info("Threshold : " + threshold);
//        Assert.assertTrue(loadTime < threshold, "Slow load: " + loadTime);
    }
    
    @When("user clicks Payment Method tile")
    public void user_clicks_Payment_Method_tile() throws InterruptedException {

    	 logger.info("user clicks Payment Method tile");
    	 dashboardPage.clickPaymentMethodTile();
    }
    
    @When("user clicks Payee tile")
    public void user_clicks_Payee_tile() throws InterruptedException {

    	 logger.info("user clicks Payment Method tile");
    	 dashboardPage.clickpayeesTile();
    }
    
    @Then("user navigate to {string} from commissions Tile")
    public void user_navigate_to_from_commissions_tile(String string) {
    	dashboardPage.clickCommissionsTile();
        dashboardPage.clickLinksUnderCommissionsDropdown(string);
    }
    
    @When("user clicks Search Payables tile")
    public void user_clicks_search_payables_tile() throws InterruptedException {
        logger.info("User clicks Search Payables tile");
        dashboardPage.clickSearchPayablesTile();
    }
    
}
