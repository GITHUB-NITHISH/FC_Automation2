package com.automation.runner;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.automation.utils.ConfigurationManager;
import com.aventstack.extentreports.service.ExtentService;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * CucumberTestRunner - TestNG runner for Cucumber scenarios
 */
@CucumberOptions(features = "src/test/resources/features", 
		glue = { "com.automation.steps","com.automation.hooks" }, 
		plugin = { "pretty", "html:target/cucumber-reports/cucumber.html",
				"json:target/cucumber-reports/cucumber.json",
//				"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
				 "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" },
		tags = "@apAccount_create and @positive",
		monochrome = true, 
		dryRun = false	
		)
public class CucumberParallelTestRunner extends AbstractTestNGCucumberTests {
	
    @Override
    @DataProvider(parallel = true) // Enables parallel execution of scenarios
    public Object[][] scenarios() {
        return super.scenarios();
    }
    @BeforeClass
    @Parameters("browser")
    public void setupBrowser(@Optional("chrome") String browser) {
        // Set the browser specifically for THIS thread
    	  ConfigurationManager.setBrowser(browser);
    	  
//    	   ExtentService.getInstance().setSystemInfo("Browser", browser.toUpperCase());
    	   
    	   ExtentService.getInstance().addTestRunnerOutput("Executing Suite on Browser: " + browser.toUpperCase());
    	  
          System.setProperty("cucumber.plugin", 
                  "html:target/cucumber-reports/cucumber-" + browser + ".html," +
                  "json:target/cucumber-reports/cucumber-" + browser + ".json"
              );
    }
}
