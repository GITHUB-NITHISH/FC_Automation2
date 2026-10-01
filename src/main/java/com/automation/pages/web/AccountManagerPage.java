package com.automation.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.automation.pages.base.BasePage;

public class AccountManagerPage extends BasePage {
	
	public AccountManagerPage(WebDriver driver) {
		super(driver);
	}

    public boolean isAccountManagerPageDisplayed() {
        logger.info("Checking Account Manager page is displayed");
        waitForInvisibilityOfLoader();
        return waitForElementVisible(getElement(getPageName(), "accountManagerHeader")) .isDisplayed();
    }

    public void selectAccountManager(String name) {
        logger.info("Selecting Account Manager from dropdown: " + name);
        click(getElement(getPageName(), "accountManagerSearch"));
        setText(getElement(getPageName(), "accountManagerSearchfield"), name);
        click(getElement(getPageName(), "accountManagerDropdownOption", name.trim()));
    }

    public void clickSearchButton() {
        logger.info("Clicking search button");
        click(getElement(getPageName(), "searchButton"));
    }

    public void clickEditButton() {
        logger.info("Clicking edit button");
        click(getElement(getPageName(), "editButton"));
    }

    public void selectLinkedPayee(String linkedPayee) {
        logger.info("Selecting linked payee: " + linkedPayee);

        click(getElement(getPageName(), "linkedPayeeDropdown"));
        setText(getElement(getPageName(), "linkedPayeeDropdownValue"), linkedPayee);
        click(getElement(getPageName(), "linkedPayeeSelectOption", linkedPayee.trim()));
    }

    public void selectAccount(String accountName) {
        logger.info("Selecting account: " + accountName);

        click(getElement(getPageName(), "selectAccountDropdown"));
        click(getElement(getPageName(), "accountDropdownOption", accountName.trim()));
    }

    public void validateEmailAddress(String expectedEmail) {
        logger.info("Validating auto populated email");

        String actual = getAttribute(
                getElement(getPageName(), "emailAddressvalidation"), "value"
        ).trim();

        validateContainsText(actual, expectedEmail);
    }

    public void selectPaymentSetting(String paymentSetting) {
        logger.info("Selecting payment setting: " + paymentSetting);

        click(getElement(getPageName(), "paymentSettingDropdown"));
        click(getElement(getPageName(), "paymentSettingDropdownOption", paymentSetting.trim()));
    }

    public void selectCommissionFrequency(String frequency) {
         logger.info("Selecting commission frequency: " + frequency);

         click(getElement(getPageName(), "commissionFrequencyDropdown"));
         click(getElement(getPageName(), "commissionFrequencyDropdownOption", frequency.trim()));
     }

    public void validateCommissionBufferDisabled(String value) {
        logger.info("Validating commission buffer disabled");

        By locator = getElement(getPageName(), "disabledCadConversionBufferField", value.trim());
        boolean isDisabled = !driver.findElement(locator).isEnabled();

        validateTrue(isDisabled, "Conversion buffer is not disabled");
    }

    public void typeCadInvoiceReference(String reference) {
        logger.info("Typing invoice reference: " + reference);
        setText(getElement(getPageName(), "cadInvoiceReferenceField"), reference);
    }

    public void selectPreferredPaymentMethod(String method) {
        logger.info("Selecting preferred payment method: " + method);

        click(getElement(getPageName(), "cadPaymentMethodDropdown"));
        click(getElement(getPageName(), "cadPaymentMethodDropdownOption", method.trim()));
    }

    public void clickSaveChangesButton() {
        logger.info("Clicking save changes button");
        click(getElement(getPageName(), "saveChangesButton"));
    }

    public void validateEditSuccessMessage(String expected) {
        logger.info("Validating edit success modal message");

        waitForInvisibilityOfLoader();

        String actual = waitForElementVisible(
                getElement(getPageName(), "accountManagerUpdationSuccessModal"))
                .getText().trim();

        validateContainsText(actual, expected);
    }

    public void clickAccountManagerModalCloseButton() {
        logger.info("Closing edit modal");
        click(getElement(getPageName(), "accountManagerModalCloseButton"));
    }


    public void activateCommissionStatus(String accountManager) {
          logger.info("Activating commission status");

          click(getElement(getPageName(), "commissionStatusButton", accountManager.trim()));
          click(getElement(getPageName(), "editCommissionStatus"));
          click(getElement(getPageName(), "saveCommissionStatus"));
      }

    public void validateStatusSuccessMessage(String expected) {
        logger.info("Validating status success message");

        waitForInvisibilityOfLoader();

        String actual = waitForElementVisible(
                getElement(getPageName(), "statusSuccessModal"))
                .getText().trim();

        validateContainsText(actual, expected);
    }

    public void clickStatusSuccessModalCloseButton() {
        logger.info("Closing status modal");
        click(getElement(getPageName(), "statusSuccessModalCloseButton"));
    }

    public boolean isAccountActive() {
        logger.info("Checking account is active");
        waitForInvisibilityOfLoader();
        return isElementDisplayed(getElement(getPageName(), "ActiveCommissionStatus"));
    }
}