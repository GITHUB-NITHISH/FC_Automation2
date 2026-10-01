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
 
public class PayeePage extends BasePage {
	private static final LoggerUtils logger = LoggerUtils.getInstance();
 
	public PayeePage (WebDriver driver) {
		super(driver);
	}
	public boolean ispayeePageDisplayed() {
        waitForInvisibilityOfLoader();
        return isElementDisplayed(getElement("payeesPage", "payeeHeader"));
    }
	public boolean isPayeeTileDisplayed() {
        waitForInvisibilityOfLoader();
        return isElementDisplayed(getElement("payeesPage", "payeeHeader"));
    }
	public void clickPayeeTile() {
		    click(getElement("payeesPage", "payeeTile"));
	    }
	public void clickAddNewPayeeButton() {
		waitForInvisibilityOfLoader();
		    click(getElement("payeesPage", "addNewPayeeButton"));
	    }
	public void typePayeeNameField(String payeeName) {
		setText(getElement("payeesPage", "payeeNameField"), payeeName);
	}
	public void typeMailingAddressField(String mailingAddress) {
		setText(getElement("payeesPage", "mailingAddressField"), mailingAddress);
	}
	public void clickValidateAddressButton() {
		    click(getElement("payeesPage", "validateAddressButton"));
	    }
	public void typePostalCodeField(String postalCode) {
		setText(getElement("payeesPage", "postalCodeField"), postalCode);
		waitForInvisibilityOfLoader();
	}
	public void typeContactNameField(String contactName) {
		setText(getElement("payeesPage", "contactNameField"), contactName);
		waitForInvisibilityOfLoader();
	}
	public void clickPreferredCadPaymentMethodDropdown() {
	    click(getElement("payeesPage", "preferredCadPaymentMethodDropdown"));
    }
	public void clickPreferredUsdPaymentMethodDropdown() {
	    click(getElement("payeesPage", "preferredUsdPaymentMethodDropdown"));
    }
	public void typeCadPaymentMethodDropdownSearch(String cadPaymentMethodDropdownSearch) {
		setText(getElement("payeesPage", "cadPaymentMethodDropdownSearch"), cadPaymentMethodDropdownSearch);
	}
	public void typeUsdPaymentMethodDropdownSearch(String usdPaymentMethodDropdownSearch) {
		setText(getElement("payeesPage", "usdPaymentMethodDropdownSearch"), usdPaymentMethodDropdownSearch);
	}
	public void clickCadPaymentMethodSearchResult(String paymentMethod) {
	    click(getElement("payeesPage", "cadPaymentMethodSearchResult", paymentMethod.trim()));
    }
	public void clickUsdPaymentMethodSearchResult(String paymentMethod) {
		By usdSearchResult = getElement("payeesPage", "usdPaymentMethodSearchResult", paymentMethod.trim());
	    click(usdSearchResult);
    }
	public void clickSelectAccountDropdown() {
	    click(getElement("payeesPage", "selectAccountDropdown"));
    }
	public void typeSelectAccountDropdownSearch(String selectAccountDropdownSearch) {
		setText(getElement("payeesPage", "selectAccountDropdownSearch"), selectAccountDropdownSearch);
	}
	public void clickSelectAccountSearchResult(String selectAccountSearchResult) {
		By accountSearchResult = getElement("payeesPage", "selectAccountSearchResult", selectAccountSearchResult.trim());
	    click(accountSearchResult);
    }
	public void clickSavePayeeButton() {
	    click(getElement("payeesPage", "savePayeeButton"));
    }
	public String getModalMessage() {
        waitForInvisibilityOfLoader();
        return waitForElementVisible(getElement("payeesPage", "payeeCreatedPopupModal")).getText().trim();
    }
	public void clickCloseButton() {
	    click(getElement("payeesPage", "closeButton"));
    }
	public void clickPayeeDetailsDropdown() {
	    click(getElement("payeesPage", "payeeDetailsDropdown"));
    }
	public void typePayeeDetailsSearchField(String payeeDetailsSearch) {
		setText(getElement("payeesPage", "payeeDetailsSearchField"), payeeDetailsSearch);
	}
	public void clickPayeeDetailsOption(String payeeResult) {
		By payeeSearchResult = getElement("payeesPage", "payeeDetailsOption", payeeResult.trim());
	    click(payeeSearchResult);
    }
	public void clickSearchButton() {
	    click(getElement("payeesPage", "searchButton"));
    }
	
	public boolean isPayeeDisplayed(String value) {
	    return isElementDisplayed(
	        getElement("payeesPage", "payeeAccountResult", value)
	    );
	}
	public void clickHomeButton() {
	    click(getElement("payeesPage", "homeButton"));
    }
	
	public void isCityFieldAutoPopulated(String city) {
		waitForInvisibilityOfLoader();
		validateEqualsAttributeValue(getElement("payeesPage", "cityField"), city);
	}
	public void isprovinceFieldAutoPopulated(String province) {
 
	    WebElement provinceElement =
	            driver.findElement(getElement("payeesPage", "provinceField"));
 
	    scrollIntoView(provinceElement);
 
	    waitForElementVisible(getElement("payeesPage", "provinceField"));
 
	    String actual = provinceElement.getText();
 
	    validateEqualsText(actual, province);
	}
	public void isCountryFieldAutoPopulated(String country) {
		waitForInvisibilityOfLoader();
		String actual = driver.findElement(getElement("payeesPage", "countryField")).getText();
		validateEqualsText(actual, country);
	}
}