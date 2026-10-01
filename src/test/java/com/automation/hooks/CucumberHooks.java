package com.automation.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.cucumber.java.After;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import com.automation.utils.DriverFactory;
import com.automation.dataprovider.TestDataIterator;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;
import com.automation.utils.SessionManager;
import com.automation.utils.VideoRecorderUtil;
import com.automation.utils.FileUtils;
import java.io.ByteArrayInputStream;

/**
 * CucumberHooks - Contains setup and teardown logic for Cucumber scenarios
 */
public class CucumberHooks {
    private static final LoggerUtils logger = LoggerUtils.getInstance();
    private WebDriver driver;
    
    @BeforeAll
    public static void beforeAll() throws Exception {
        if (ConfigurationManager.isVideoRecordingEnabled()) {
            VideoRecorderUtil.startRecording("TestSuite_Run");
        }
    }

    /**
     * Setup before each scenario
     * @throws Exception 
     */
    @Before
	public void setup(io.cucumber.java.Scenario scenario) throws Exception {
		logger.info("========== Starting Test Execution ==========");		

        if (!ConfigurationManager.isE2EMode()) {
            SessionManager.reset();
        }
       
		String browserName = ConfigurationManager.getBrowserName();	
		scenario.log("Browser: " + browserName);
		
		driver = DriverFactory.getOrCreateDriver(browserName);
		driver.manage().window().maximize();
		logger.info("Browser initialized: " + browserName);

    }

    /**
     * Teardown after each scenario
     * @throws Exception 
     */
    @After
    public void teardown(io.cucumber.java.Scenario scenario) throws Exception {
        logger.info("========== Ending Test Execution ==========");
        
        ScenarioContext.clear();
        TestDataIterator.clear();
        
        if (scenario.isFailed()) {
            logger.error("Scenario FAILED: " + scenario.getName());
            
            // Take screenshot on failure if enabled
            if (ConfigurationManager.isScreenshotOnFailure()) {
                  byte[] screenshot = FileUtils.takeScreenshot(driver);
                if (screenshot != null) {
                    scenario.attach(screenshot, "image/png", "Failure Screenshot");
                    Allure.addAttachment("Failure Screenshot",  new ByteArrayInputStream(screenshot));
                }
            }
        } else {
            logger.info("Scenario PASSED: " + scenario.getName());
        }
        

        if (ConfigurationManager.isParallelRun()) {
            logger.info("Parallel Mode: Closing browser");
            DriverFactory.quitDriver();
            SessionManager.reset();
        } else {
            logger.info("E2E Mode: Keeping session alive");
        }

    }

    /**
     * Final teardown after all scenarios in the suite
     * @throws Exception 
     */
    @io.cucumber.java.AfterAll
    public static void afterAll() throws Exception {
        if (ConfigurationManager.isVideoRecordingEnabled()) {
            VideoRecorderUtil.stopRecording();
        }      
        logger.info("========== All Scenarios Completed - Final Cleanup ==========");
        logger.info("Triggering final driver cleanup");
        SessionManager.reset();
        DriverFactory.quitDriver();
    }
}
