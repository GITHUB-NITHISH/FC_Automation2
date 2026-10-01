package com.automation.pages.web;
 
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automation.dataprovider.ExcelUtil;
import com.automation.pages.base.BasePage;
import com.automation.utils.ConfigurationManager;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;
 
public class SearchPayablesPage extends BasePage {
 
	private static final LoggerUtils logger = LoggerUtils.getInstance();
 
	public SearchPayablesPage(WebDriver driver) {
		super(driver);
	}
 
	// ✅ Page Load
	public boolean isSearchPayablesPageDisplayed() {
		waitForInvisibilityOfLoader();
		logger.info("Validating Search Payables Page loaded");
		return isElementDisplayed(getElement(getPageName(), "searchButton"));
	}
 
	// ✅ Select Option
	public void selectSearchByOption(String option) {
		click(getElement(getPageName(), "searchByRadioButton", option));
	}
 
	// ✅ Enter Payee
	public void enterPayeeName(String payeeName) {
		click(getElement(getPageName(), "payeeNameDropdown"));
		setText(getElement(getPageName(), "payeeNameSearchField"), payeeName);
		click(getElement(getPageName(), "payeeNameDropdownOptions", payeeName));
		logger.info("Entering Payee Name: " + payeeName);
	}
 
	// ✅ Click Search
	public void clickSearchButton() {
		click(getElement(getPageName(), "searchButton"));
		waitForInvisibilityOfLoader();
	}
 
	// ✅ STATUS VALIDATION (UNPAID BY LOCATOR)
	public boolean isStatusUnpaidDisplayed() {
		return isElementDisplayed(getElement(getPageName(), "isUnpaidStatusDisplayed"));
	}
 
	public void verifyPayeeName(String expectedPayee, String invoiceNumber) {
		String actual = driver.findElement(getElement(getPageName(), "resultPayee", invoiceNumber)).getText().trim();
		validateEqualsText(actual, expectedPayee);
	}
 
	public boolean verifyInvoiceNumber(String invoiceNumber) {
		return isElementDisplayed(getElement(getPageName(), "resultInvoice", invoiceNumber));
	}
 
	public void verifyInvoiceDate(String expectedDate ,String invoiceNumber) {
        ScenarioContext.set("INVOICEDATE", expectedDate);
		String actual = driver.findElement(getElement(getPageName(), "resultInvDate" , invoiceNumber)).getText().trim();
		validateEqualsText(actual, expectedDate);
	}
 
	public void verifyPaymentDate(String expectedDate, String invoiceNumber) {
		expectedDate = addDaysToDate(ScenarioContext.get("INVOICEDATE"), 7);
		String actual = driver.findElement(getElement(getPageName(), "resultPaymentDate" , invoiceNumber)).getText().trim();
		validateEqualsText(actual, expectedDate);
		
		ScenarioContext.set("PAYMENTDATE", expectedDate);
	}

	public String addDaysToDate(String dateStr, int days) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
		LocalDate date = LocalDate.parse(dateStr, formatter);
		return date.plusDays(days).format(formatter);
	}

	public void verifyInvoiceAmounts(String expectedAmountText,String invoiceNumber) {
		String actualText = extractAmount(getElement(getPageName(), "resultAmount" ,invoiceNumber));
		validateEqualsText(actualText, expectedAmountText);
	}
 
	public boolean verifyNoTaxField(String invoiceNumber) throws InterruptedException {
		return isElementDisplayed(getElement(getPageName(), "resultTax" , invoiceNumber));
	}
 
	public void verifyPaymentTotal(String expectedTotalText,String invoiceNumber) {
		String actualText = extractAmount(getElement(getPageName(), "resultTotal" ,invoiceNumber));
		validateEqualsText(actualText, expectedTotalText);
	}
 
	public void verifyPaymentMethod(String expectedMethod,String invoiceNumber) {
		String actual = driver.findElement(getElement(getPageName(), "resultMethod", invoiceNumber)).getText().trim();
		validateEqualsText(actual, expectedMethod);
	}

 
	    // ✅ Page Load Validation
	    public boolean isPrintChequePageDisplayed() {
	        waitForInvisibilityOfLoader();
	        logger.info("Validating Print Cheque Page is displayed");
	       return isElementDisplayed(getElement(getPageName(), "printChequesHeader"));
	    }
 
	    // ✅ Click Print Cheque Button
	    public void clickPrintChequeButton() {
	        click(getElement(getPageName(), "printChequeButton"));
	        waitForInvisibilityOfLoader();
	        logger.info("Clicked on Print Cheque button");
	    }
	    public void selectPayableByInvoice(String invoice) {
	        click(getElement(getPageName(), "payableCheckboxByInvoice", invoice));
	        logger.info("Selected payable for Invoice: " + invoice);
	    }
 
	    // ✅ Click Close Off Button
	    public void clickCloseOffButton() {
	        click(getElement(getPageName(), "closeOffAndPrintButton"));
	        waitForInvisibilityOfLoader();
	        logger.info("Clicked on Close Off button");
	    }
 
	    // ✅ Enter Cheque Number
	    public void enterChequeNumber(String chequeNumber) {
	        setText(getElement(getPageName(), "chequeField"), chequeNumber);
	        logger.info("Entered Cheque Number: " + chequeNumber);
	    }
 
	    // ✅ Select Payment Date
	    public void selectPaymentDate(String paymentDate) {
	        setText(getElement(getPageName(), "paymentDateField"), paymentDate);
	        logger.info("Selected Payment Date: " + paymentDate);
	    }
	    public void clickPayableCheckbox(String payeeName) {
	        click(getElement(getPageName(), "payableCheckbox", payeeName));
	        logger.info("Checkbox clicked for Payee: " + payeeName);
	    }
 
	    // ✅ Click Generate Button
	    public void clickGenerateButton() {
	        click(getElement(getPageName(), "generateChequeButton"));
	        waitForInvisibilityOfLoader();
	        logger.info("Clicked on Generate button");
	    }
 
	    // ✅ Get Payment Status
	    public String getPaymentStatus() {
	        String status = getText(getElement(getPageName(), "paymentStatus")).trim();
	        logger.info("Captured Payment Status: " + status);
	        return status;
	    }
 
	    // ✅ Validate Success Message 
	    public void validateGenerateChequeSuccessMessage(String expected) {
 
	        logger.info("Validating Generate Cheque Success Message");
 
	        waitForInvisibilityOfLoader();
 
	        String actual = waitForElementVisible(
	                getElement(getPageName(), "generateChequeSuccessMessage")
	        ).getText().trim();
 
	        validateEqualsText(actual, expected);
	    }
	    public void selectPayable(String payeeName) {
	        click(getElement(getPageName(), "payableCheckbox", payeeName));
	        logger.info("Selected payable for Payee Name: " + payeeName);
	    }
	    
	    public void selectDateRange(String date) {
	            logger.info("Selecting date: " + date);
	            click(getElement(getPageName(), "calendarButton"));
	            By month = getElement(getPageName(), "calendarMonth");
	            By year  = getElement(getPageName(), "calendarYear");
	            waitForElementVisible(month);
	            selectDateRange(month, year, "calendarDate", getPageName(), date);
	    }
	}