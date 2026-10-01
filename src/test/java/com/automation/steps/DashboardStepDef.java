package com.automation.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import com.automation.pages.web.ApDashboardPage;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.DriverFactory;
import com.automation.utils.LoggerUtils;
import com.automation.utils.PerformanceUtils;

import static org.testng.Assert.*;

import java.util.Map;

/**
 * DashboardStepDef - Step definitions for Dashboard scenarios
 */
public class DashboardStepDef {
    private static final LoggerUtils logger = LoggerUtils.getInstance();
    private WebDriver driver;
    private ApDashboardPage dashboardPage;

    public DashboardStepDef() {
    	  this.driver = DriverFactory.getDriver();
          this.dashboardPage = new ApDashboardPage(driver);
    }

    @Then("AP dashboard should be loaded")
    public void ap_dashboard_should_be_loaded() throws InterruptedException {
//         if (!ConfigurationManager.isParallelRun() && driver != null) {
//             if (driver.getCurrentUrl().contains("accounts") || driver.getCurrentUrl().contains("dashboard") ) return;
//         }
    	 logger.info("Navigating to Ap Dashboard page");
    	 assertTrue(dashboardPage.isDashboardLoaded());
    }

    @When("user clicks AP Account tile")
    public void user_clicks_ap_account_tile() throws InterruptedException {
//         if (!ConfigurationManager.isParallelRun() && driver != null) {
//             if (driver.getCurrentUrl().contains("accounts")) {
//                 logger.info("E2E Mode: Already on Ap Account page, skipping tile click");
//                 return;
//             }
//         }
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
//         if (!ConfigurationManager.isParallelRun() && driver != null) {
//             if (driver.getCurrentUrl().contains("accounts")) {
//                 logger.info("E2E Mode: Already on Payment Method page, skipping tile click");
//                 return;
//             }
//         }
    	 logger.info("user clicks Payment Method tile");
    	 dashboardPage.clickPaymentMethodTile();
    }
    
    @When("user clicks Payee tile")
    public void user_clicks_Payee_tile() throws InterruptedException {
//         if (!ConfigurationManager.isParallelRun() && driver != null) {
//             if (driver.getCurrentUrl().contains("accounts")) {
//                 logger.info("E2E Mode: Already on Payee page, skipping tile click");
//                 return;
//             }
//         }
    	 logger.info("user clicks Payment Method tile");
    	 dashboardPage.clickpayeesTile();
    }
    @When("user navigate to account manager from commissions Tile")
    public void user_navigate_to_account_manager_from_commissions_Tile() throws InterruptedException {
        logger.info("Navigating to Account Manager from Commission");
        dashboardPage.clickcommissionsDropdownButton();
        dashboardPage.clickaccountManagerOption();
     }
    @	When ("user navigate to account manager from apdashboard")
    public void user_navigate_to_account_manager_from_apdashboard() throws InterruptedException {
    	 logger.info("Navigating to Search Commission from ApDashboard");
         dashboardPage.clickcommissionsDropdownButton();
         dashboardPage.clickSearchCommisions();
    }
    
}
