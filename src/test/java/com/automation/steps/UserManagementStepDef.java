package com.automation.steps;

import org.openqa.selenium.WebDriver;
import com.automation.pages.web.UserManagementPage;
import com.automation.utils.DriverFactory;
import com.automation.utils.ScenarioContext;
import io.cucumber.java.en.Then;

public class UserManagementStepDef {
    private UserManagementPage userManagementPage;

    public UserManagementStepDef() {
        WebDriver driver = DriverFactory.getDriver();
        this.userManagementPage = new UserManagementPage(driver);
    }
    
    @Then("user accesses Create New User section")
    public void user_accesses_create_new_user_section() {
        userManagementPage.accessCreateNewUserSection();
    }

    @Then("user enters internal user details")
    public void user_enters_internal_user_details() throws Exception {
    	String reportingManager = ScenarioContext.getTestData("ReportingManager");
    	String firstName = ScenarioContext.getTestData("FirstName");
    	String lastName = ScenarioContext.getTestData("LastName");
    	String fcEmailAddress = ScenarioContext.getTestData("FCEmailAddress");
    	String primaryArea = ScenarioContext.getTestData("PrimaryArea");
		 
        userManagementPage.enterUserDetails(reportingManager, firstName, lastName, fcEmailAddress, primaryArea);
        String username = userManagementPage.getAutoPopulatedUsername();
        
        ScenarioContext.setGlobal("USERNAME", username);
        ScenarioContext.setGlobal("FIRSTNAME", firstName);
        ScenarioContext.setGlobal("LASTNAME", lastName);
        ScenarioContext.setGlobal("FCEMAILADDRESS", fcEmailAddress);       
    }
    
    @Then("user submits the new user details")
    public void user_submits_the_new_user_details() throws InterruptedException {
        userManagementPage.submitUserDetails();
        userManagementPage.clickCancelButton();
//        Thread.sleep(2000);
        userManagementPage.clickHomeButton();
    }

}
