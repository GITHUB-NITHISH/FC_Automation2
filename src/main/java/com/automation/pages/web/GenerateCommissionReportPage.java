package com.automation.pages.web;
 
import org.openqa.selenium.WebDriver;
import com.automation.pages.base.BasePage;
 
public class GenerateCommissionReportPage extends BasePage {
 
	public GenerateCommissionReportPage (WebDriver driver) {
			super(driver);
	}

	public boolean isGenerateCommissionReportPageDisplayed() {
        logger.info("Checking Generate Commission Report page is displayed");
        return waitForElementVisible(getElement(getPageName(), "generateCommissionReportHeader"))
                .isDisplayed();
	}
	
	public void selectAccountManager(String accountManager) {
        logger.info("Selecting Account Manager: " + accountManager);

        click(getElement(getPageName(), "accountManagerDropDown"));
        setText(getElement(getPageName(), "accountManagerNamesearch"), accountManager);
        click(getElement(getPageName(), "accountManagerNameSearchResult", accountManager.trim()));
	}

	public void typeInvoiceDate(String date) {
	       logger.info("Typing invoice date: " + date);
	       setText(getElement(getPageName(), "invoiceDate"), date);
	}

	public void clickGenerateButton() {
        logger.info("Clicking generate button");
        click(getElement(getPageName(), "generateButton"));
	}
    
    public void validateGenerateReportModalMessage(String expected) {
   		logger.info("Expected modal Message : " + expected);
   		waitForInvisibilityOfLoader();
    	String actual = waitForElementVisible(getElement(getPageName(), "modalMessage")).getText().trim();
   		logger.info("Actual modal Message : " + actual);

    	validateEqualsText(actual, expected);
    }

    public void clickCloseModalButton() {
    	logger.info("Closing modal");
    	click(getElement(getPageName(), "closeButton"));
    }
    
    public String getGeneratedReportId() throws Exception {
    	logger.info("Return generated ID");
    	scrollToElement(getElement(getPageName(), "IDLink"));
    	return waitForElementVisible(getElement(getPageName(), "IDLink")).getText().trim();
    }
}

