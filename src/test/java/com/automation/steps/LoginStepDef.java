package com.automation.steps;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import com.automation.pages.web.LoginPage;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.DriverFactory;
import com.automation.utils.LoggerUtils;
import com.automation.utils.SessionManager;

import static org.testng.Assert.assertTrue;

/**
 * LoginStepDef - Step definitions for Login scenarios
 */
public class LoginStepDef {
    private static final LoggerUtils logger = LoggerUtils.getInstance();
    private WebDriver driver;
    private LoginPage loginPage;
    
//    JsonNode testData = JsonDataReader.readJsonData("src/main/resources/data/testdata.json");
	
    public LoginStepDef() {
        this.driver = DriverFactory.getDriver();
    }
    
    @Given("user is on login page")
    public void user_is_on_login_page() {
//         if (!ConfigurationManager.isParallelRun() && driver != null) {
//             try {
//                 if (driver.getCurrentUrl().contains("dashboard") || driver.getCurrentUrl().contains("accounts")) {
//                     logger.info("E2E Mode: Already logged in, skipping navigation to login page");
//                     return;
//                 }
//             } catch (Exception e) { }
//         }

        if (SessionManager.isLoggedIn()) {
            logger.info("✅ Already logged in → skipping navigation to login page");
            return;
        }


    	 logger.info("Navigating to login page");
         String baseUrl = ConfigurationManager.getBaseUrl();
         driver.navigate().to(baseUrl);
         
         loginPage = new LoginPage(driver);
         assertTrue(loginPage.isLoginPageLoaded(), "Login page not loaded");
    }

    @When("user logs into the application with valid credentials")
    public void user_logs_into_the_application_with_valid_credentials() {
//        if (!ConfigurationManager.isParallelRun() && driver != null) {
//            if (driver.getCurrentUrl().contains("dashboard") || driver.getCurrentUrl().contains("accounts")) 
//            	return;
//        }	
    	
    	   if (SessionManager.isLoggedIn()) {
    	        logger.info("✅ Already logged in → skipping login");
    	        return;
    	    }

    	String username = ConfigurationManager.getProperty("superAdmin.username");
    	String password = ConfigurationManager.getProperty("superAdmin.password");
    	
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
        
        SessionManager.setLoggedIn(true);
        
    }
}
