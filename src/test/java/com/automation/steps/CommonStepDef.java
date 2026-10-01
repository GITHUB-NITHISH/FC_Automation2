package com.automation.steps;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import com.automation.dataprovider.ExcelUtil;
import com.automation.pages.web.CommonPage;
import com.automation.utils.DriverFactory;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class CommonStepDef {
	
    private CommonPage commonPage;
    private static final LoggerUtils logger = LoggerUtils.getInstance();

    public CommonStepDef() {
        WebDriver driver = DriverFactory.getDriver();
        this.commonPage = new CommonPage(driver);
    }
	
    
	@Given("user loads test data from {string} sheet {string} for testcase {string}")
	public void user_loads_test_data_from_sheet_for_testcase(String excelPath, String sheetName, String testCaseId) {
        int iteration = ScenarioContext.getNextIteration(testCaseId);
        logger.info("Resolving Data from Step Def -> Excel: " + excelPath + " | Sheet: " + sheetName + " | TCID: " + testCaseId + " | Iteration: " + iteration);
        
        Map<String, String> excelData = ExcelUtil.getIterationData(sheetName, testCaseId, iteration);
        ScenarioContext.set(testCaseId, excelData); 
        
        ScenarioContext.set("CURRENT_TCID", testCaseId);
        ScenarioContext.set("CURRENT_SHEETNAME", sheetName);
        
        logger.info("Data loaded successfully for " + testCaseId + " iteration " + iteration );
	}
	
    @Then("user navigates to {string} login")
    public void user_navigates_to_login(String loginType) throws InterruptedException {
    	commonPage.navigateToUserLogin(loginType);
    }


}
