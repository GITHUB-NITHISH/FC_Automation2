package com.automation.pages.web;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.dataprovider.ExcelUtil;
import com.automation.pages.base.BasePage;
import com.automation.utils.ScenarioContext;

public class SearchCommissionsPage extends BasePage  {

	public SearchCommissionsPage (WebDriver driver) {
			super(driver);
	}
	
	public boolean isSearchCommissionspageDisplayed() {
		logger.info("Checking Search Commissions page is displayed");
        waitForInvisibilityOfLoader();

	    return waitForElementVisible(getElement(getPageName(), "searchCommissionsHeader")).isDisplayed();
	}
	
	public void clickDueRadioButton() {	
		logger.info("Clicking Due radio button");
		click(getElement(getPageName(), "dueRadioButton"));		    
	}
 
	public void selectDate(String dateInput) {
        logger.info("Selecting date: " + dateInput);

        click(getElement(getPageName(), "calendarButton"));
        By month = getElement(getPageName(), "calendarMonth");
        By year  = getElement(getPageName(), "calendarYear");
        waitForElementVisible(month);
        selectDateRange(month, year, "calendarDate", getPageName(), dateInput);
	}
	
	public void typeTransactionId(String transactionId) {
		logger.info("Entering Transaction ID: " + transactionId);
		setText(getElement(getPageName(),"searchByTransactionField"), transactionId);
	}
	
	public void validateTransactionIdDisplayed(String  transactionId) {
        logger.info("Validating Transaction ID: " + transactionId);
        waitForInvisibilityOfLoader();
        WebElement element = driver.findElement(getElement(getPageName(), "transactionId", transactionId));
        scrollIntoView(element);
     
        validateTrue(element.isDisplayed(), "Transaction ID not displayed");	
	}

    public void validateCustomerName(String expectedName, String transactionId) {
        String actual = driver.findElement(
                getElement(getPageName(), "customerNameField", transactionId)
        ).getText().trim();

        validateEqualsText(actual, expectedName);
    }

    public void validateTotalCharges(String expected, String transactionId) {
    	waitForInvisibilityOfLoader();
        String actual = String.valueOf(
                extractAmount(getElement(getPageName(), "totalChargesField", transactionId))
        );
        validateEqualsText(actual, expected);
    }

    public void validateNetRevenue(String expected, String transactionId) {
        String actual = String.valueOf(
                extractAmount(getElement(getPageName(), "netRevenueValueField", transactionId))
        );
        validateContainsText(actual, expected);
    }

    public void validateOpsCost(String expected, String transactionId) {
        String actual = String.valueOf(
                extractAmount(getElement(getPageName(), "opsCostValueField", transactionId))
        );
        validateContainsText(actual, expected);
    }
    
    public void validateGrossProfit(String expected, String transactionId) {

        String commissionType = ScenarioContext.get("COMMISSIONTYPE");

        String actualText = driver.findElement(getElement(getPageName(), "grossProfitField", transactionId)).getText().trim();

        if (commissionType != null && commissionType.toLowerCase().contains("revenue")) {
            validateContainsText(actualText, "-");
        } else {
            validateContainsText(actualText, expected);
        }
    }

    public void validateFlatFee(String transactionId, String expected) {
            verifyIfPresentAndValidate(getElement(getPageName(), "flatFeeField", transactionId),expected);
    }
    
    public void validateCommissionableAmount(String transactionId, String expected) {
        String actual = String.valueOf(
                extractAmount(getElement(getPageName(), "commissionableAmountField", transactionId))
        );
        validateEqualsText(actual, expected);
    }


    public void validateCommissionTotal(String transactionId, String expected) {
         String actual = String.valueOf(
                 extractAmount(getElement(getPageName(), "commTotalValueField", transactionId))
         );
         validateEqualsText(actual, expected);
     }


    public void validateAccountManager(String expected, String transactionId) {
        String actual = driver.findElement(
                getElement(getPageName(), "agentNameField", transactionId)
        ).getText().trim();

        validateContainsText(actual, expected);
    }

    public void validateShipmentType(String expected, String transactionId) {
        String actual = driver.findElement(
                getElement(getPageName(), "shipmentTypeField", transactionId)
        ).getText().trim();

        validateContainsText(actual, expected);
    }

    public void validateBOLNumber(String expected, String transactionId) {
        String actual = driver.findElement(
                getElement(getPageName(), "bolNumberField", transactionId)
        ).getText().trim();

        validateContainsText(actual, expected);
    }
    
    public void validateInvoiceNumber(String expected, String transactionId) {
        String actual = driver.findElement(
                getElement(getPageName(), "invNumberField", transactionId)
        ).getText().trim();

        validateContainsText(actual, expected);
    }

    public void validateShipmentDate(String expected, String transactionId) {
        String actual = driver.findElement(
                getElement(getPageName(), "shipmentDateField", transactionId)
        ).getText().trim();

        validateContainsText(actual, expected);
    }
    
    public void validateCommissionsTypeField(String commissionType, String transactionId) {
    	logger.info("Validating Commission Type");

    	commissionType = commissionType.replace(" and Shipment Amount","");
    	String actualType = driver.findElement(getElement(getPageName(), "commissionsTypeField", transactionId)).getText().trim();

        validateContainsText(actualType, commissionType);
        
        ScenarioContext.set("COMMISSIONTYPE", commissionType);
        
        Map<String, String> data = new HashMap<>();
	    data.put("CommissionType", commissionType); 
	    ExcelUtil.writeCurrentIterationData(data);
    }
    
    public void validateCommissionsTypePercent(String gp, String rev, String transactionId) {
    	logger.info("Validating Commission Type Percent");
    	
    	String commissionType = ScenarioContext.get("COMMISSIONTYPE");
    	String expectedPercent = "Gross Profit".equals(commissionType) ? gp : rev;	
    	String actualPercent = String.valueOf(extractNumber(getElement(getPageName(), "commissionsTypeField", transactionId)));

        Map<String, String> data = new HashMap<>();
	    data.put("CommissionTypePercent", expectedPercent); 
	    ExcelUtil.writeCurrentIterationData(data);
	    
    	validateContainsText(actualPercent, expectedPercent);
    }
 
	public void clickSearchButton() {
		logger.info("Clicking search button");
		click(getElement(getPageName(),"searchButton"));
	}
}