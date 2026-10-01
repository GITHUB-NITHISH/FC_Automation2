package com.automation.pages.web;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
 
import com.automation.pages.base.BasePage;
import com.automation.utils.LoggerUtils;
import com.automation.utils.SelfHealingLocatorUtil;
 
public class PaymentMethodPage extends BasePage {
 
	private static final LoggerUtils logger = LoggerUtils.getInstance();
 
	public PaymentMethodPage (WebDriver driver) {
		super(driver);
	}
	public boolean ispaymentMethodPageDisplayed() {
        waitForInvisibilityOfLoader();
        return isElementDisplayed(getElement("paymentMethodPage", "paymentMethodsHeader"));
    }
	public void selectMaxPaginationValue(String pageName, String locatorName) {
 
		By dropdownLocator = SelfHealingLocatorUtil
		        .getLocators(pageName, locatorName)[0];
 
		WebElement dropdown = driver.findElement(dropdownLocator);
	    Select select = new Select(dropdown);
 
	    List<WebElement> options = select.getOptions();
 
	    int maxValue = options.stream()
	            .map(WebElement::getText)
	            .map(String::trim)
	            .mapToInt(Integer::parseInt)
	            .max()
	            .orElse(10);
 
	    select.selectByVisibleText(String.valueOf(maxValue));
 
	    System.out.println("Selected max pagination value: " + maxValue);
	}
	 public void clickPaymentMethodtile() {
    	waitForInvisibilityOfLoader();
	    click(getElement("apdashboardPage", "paymentMethodTile"));
    }
	 public void clickAddNewPaymentButton() {
		    click(getElement("paymentMethodPage", "addNewPaymentButton"));
	    }
	 public void clickPaymentTypeChequeRadioButton() {
		    click(getElement("paymentMethodPage", "paymentTypeChequeRadioButton"));
	    }
	 public void clickPaymentCurrencyRadioButton() {
		    click(getElement("paymentMethodPage", "paymentCurrencyRadioButton"));
	    }
	 public void typePaymentNameField(String paymentName) {
			setText(getElement("paymentMethodPage", "paymentNameField"), paymentName);
		}
	 public void typeFirstChequeInSeriesField(String firstChequeNumber) {
			setText(getElement("paymentMethodPage", "firstChequeInSeriesField"), firstChequeNumber);
		}
	 public String calculateChequeCount(String firstCheque, String lastCheque) {
		    int first = Integer.parseInt(firstCheque);
		    int last = Integer.parseInt(lastCheque);
		    return String.valueOf((last - first) + 1);
		}
	 public void typeLastChequeInSeriesField(String lastChequeNumber) {
			setText(getElement("paymentMethodPage", "lastChequeInSeriesField"), lastChequeNumber);
		}
	 public void typeSendReminderWhenChequesRemainField(String sendReminderValue) {
			setText(getElement("paymentMethodPage", "sendReminderWhenChequesRemainField"), sendReminderValue);
		}
	 public void clickSavePaymentMethodButton() {    	
	    	logger.info("Clicking Save Payment Method");
		    click(getElement("paymentMethodPage", "savePaymentMethodButton"));
	    }
	 public String getModalMessage() {
		        waitForInvisibilityOfLoader();
		        return waitForElementVisible(getElement("paymentMethodPage", "validateAddPaymentSuccessMessage")).getText().trim();
		    }
	 public void clickSuccessModalCloseButton() {
			    click(getElement("paymentMethodPage", "successModalCloseButton"));
		    }
	 public void clickSwitchTab() {
		 	waitForInvisibilityOfLoader();
		    click(getElement("paymentMethodPage", "switchTab"));
	    }
	 public boolean isPaymentMethodDisplayed(String paymentMethodName) {
 
		    waitForInvisibilityOfLoader();
 
//		    selectMaxPaginationValue("paymentMethodPage", "pageSizeDropdown");
 
		    By paymentMethodLocator = SelfHealingLocatorUtil
		            .getLocators(
		                    "paymentMethodPage",
		                    "paymentMethodtile",
		                    paymentMethodName)[0];
 
		    List<WebElement> paymentMethods = getElements(paymentMethodLocator);
 
		    return !paymentMethods.isEmpty();
		}
	 public void clickHomeButton() {
		    click(getElement("paymentMethodPage", "homeButton"));
 
	 	}
	 public String getChequeInSeries() {
		WebElement element = driver.findElement(getElement("paymentMethodPage", "chequesInSeriesField"));
		String actual = element.getAttribute("value");
		return actual;
	 }
}