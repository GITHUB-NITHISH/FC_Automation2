package com.automation.steps;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.automation.pages.web.CommonPage;
import com.automation.pages.web.FCUserManagementPage;
import com.automation.utils.DriverFactory;
import com.automation.utils.FakerUtil;
import com.automation.utils.ScenarioContext;
import io.cucumber.java.en.Then;

public class FCUserManagementStepDef {
    private FCUserManagementPage userManagementPage;
    private CommonPage commonPage;

    public FCUserManagementStepDef() {
        WebDriver driver = DriverFactory.getDriver();
        this.userManagementPage = new FCUserManagementPage(driver);
        this.commonPage = new CommonPage(driver);
    }
    
    @Then("user accesses Create New User section")
    public void user_accesses_create_new_user_section() {
        userManagementPage.accessCreateNewUserSection();
    }

    @Then("user enters internal user details")
    public void user_enters_internal_user_details() throws Exception {
    	String startDate = ScenarioContext.getTestData("StartDate");
    	String reportingManager = ScenarioContext.getTestData("ReportingManager");
    	String firstName = FakerUtil.generateFirstName();
    	String lastName = FakerUtil.generateLastName();
    	String workPhoneNumber = FakerUtil.generatePhoneNumber();
    	String extension = FakerUtil.generateRandomCountryExtension();
		String cellPhoneNumber = FakerUtil.generatePhoneNumber();
    	String fcEmailAddress = FakerUtil.generateOfficialEmail();
		String personalEmailAddress = FakerUtil.generatePersonalEmail();
    	String primaryArea = ScenarioContext.getTestData("PrimaryArea");
		 
        userManagementPage.configureUserDetails(startDate, reportingManager, firstName, lastName, workPhoneNumber, extension, 
        									cellPhoneNumber, fcEmailAddress, personalEmailAddress, primaryArea);
        Assert.assertTrue(userManagementPage.isUserNameAutoPopulated());
        
        ScenarioContext.setGlobal("FIRSTNAME", firstName);
        ScenarioContext.setGlobal("LASTNAME", lastName);
        ScenarioContext.setGlobal("STARTDATE", startDate);    
        ScenarioContext.setGlobal("WORKPHONENUMBER", workPhoneNumber);   
        ScenarioContext.setGlobal("EXTENSION", extension);   
        ScenarioContext.setGlobal("CELLPHONENUMBER", cellPhoneNumber);   
        ScenarioContext.setGlobal("FCEMAILADDRESS", fcEmailAddress);   
        ScenarioContext.setGlobal("PERSONALEMAILADDRESS", personalEmailAddress);  
        ScenarioContext.setGlobal("USERNAME", userManagementPage.getAutoPopulatedUserName());
    }
    
    
    @Then("user submits the new user details")
    public void user_submits_the_new_user_details() throws InterruptedException {
        userManagementPage.submitUserDetails();
        userManagementPage.clickCancelButton();
        commonPage.clickHomeButton();
    }

}
