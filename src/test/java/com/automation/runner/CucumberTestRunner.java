package com.automation.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.automation.dataprovider.ExcelUtil;
import com.automation.utils.ConfigurationManager;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;

@CucumberOptions(
	  features = {"src/test/resources/features" },
      glue = { "com.automation.steps","com.automation.hooks" }, 
      plugin = { "pretty", "html:target/cucumber-reports/cucumber.html",
				"json:target/cucumber-reports/cucumber.json",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" }, 
      tags = "@e2e-flow",
      monochrome = true, 
      dryRun = false
      )
public class CucumberTestRunner extends AbstractTestNGCucumberTests {

	@BeforeClass
	@Parameters("browser")
	public void setupBrowser(@Optional("chrome") String browser) {
		ConfigurationManager.setBrowser(browser);

		System.setProperty("cucumber.plugin", "html:target/cucumber-reports/cucumber-" + browser + ".html,"
				+ "json:target/cucumber-reports/cucumber-" + browser + ".json");
	}
	
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        Object[][] originalScenarios = super.scenarios();
        List<Object[]> expandedScenarios = new ArrayList<>();

        for (Object[] scenario : originalScenarios) {
            PickleWrapper pickleWrapper = (PickleWrapper) scenario[0];
            FeatureWrapper featureWrapper = (FeatureWrapper) scenario[1];
            String scenarioName = pickleWrapper.getPickle().getName();

         // Matches TC_AM_001, TC01, TC01_createAccount, etc.
            Matcher m = Pattern.compile("^(TC[a-zA-Z0-9_]+)\\s*:").matcher(scenarioName);

            String testCaseID = null;
            String sheetName = null;

            if (m.find()) {
                testCaseID = m.group(1);
                sheetName = ExcelUtil.findSheetNameForTestCase(testCaseID);
            }

            if (sheetName != null && testCaseID != null) {
                int iterations = ExcelUtil.getIterationCount(sheetName, testCaseID);
                for (int i = 1; i <= iterations; i++) {
                    expandedScenarios.add(new Object[] {
                            pickleWrapper,
                            featureWrapper });
                }
            } else {
                expandedScenarios.add(scenario);
            }
        }
        return expandedScenarios.toArray(new Object[0][0]);
    }
	
}
