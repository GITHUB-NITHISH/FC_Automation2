package com.automation.pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.automation.pages.base.BasePage;

public class ApAccountManagerPage extends BasePage {

	public ApAccountManagerPage(WebDriver driver) {
		super(driver);
	}

	public boolean isaccountManagerPageDisplayed() {
		waitForInvisibilityOfLoader();
		return waitForElementVisible(getElement("apAccountManagerPage", "accountManagerHeader")).isDisplayed();
	}

	public void clickAccountManagerSearch() {
		waitForInvisibilityOfLoader();
		click(getElement("apAccountManagerPage", "accountManagerSearch"));

	}

	public void typeAccountManagerSearch(String AccountManagerSearchField) {
		setText(getElement("apAccountManagerPage", "accountManagerSearchfield"), AccountManagerSearchField);
	}

	public void clickAccountManagerDropdownOption(String selectAccountManager) {
		By accountManagerSearchResult = getElement("apAccountManagerPage", "accountManagerDropdownOption",
				selectAccountManager.trim());
		click(accountManagerSearchResult);
	}

	public void clickSearchButton() {
		click(getElement("apAccountManagerPage", "searchButton"));
	}

	public String emailAddressValidation() {

		By emailValidation = getElement("apAccountManagerPage", "emailAddressvalidation");

		return getAttribute(emailValidation, "value").trim();
	}

	public void clickEditButton() {
		click(getElement("apAccountManagerPage", "editButton"));
	}

	public void clickLinkedPayeeDropdown() {
		click(getElement("apAccountManagerPage", "linkedPayeeDropdown"));
	}

	public void typeLinkedPayeeDropdownValue(String linkedPayeeDropdownValue) {
		setText(getElement("apAccountManagerPage", "linkedPayeeDropdownValue"), linkedPayeeDropdownValue);
	}

	public void clickLinkedPayeeSelectOption(String selectLinkedPayee) {
		By linkedPayeeSearchResult = getElement("apAccountManagerPage", "linkedPayeeSelectOption",
				selectLinkedPayee.trim());
		click(linkedPayeeSearchResult);
	}

	public void clickSelectAccountDropdown() {
		click(getElement("apAccountManagerPage", "selectAccountDropdown"));
	}

	public void clickAccountDropdownOption(String selectAccount) {
		By selectAccountResult = getElement("apAccountManagerPage", "accountDropdownOption", selectAccount.trim());
		click(selectAccountResult);
	}

	public void isCountryFieldAutoPopulated(String email) {
		String actual = driver.findElement(getElement("payeesPage", "emailAddressField")).getText();
		validateEqualsText(actual, email);
	}

	public void clickPaymentSettingDropdown() {
		click(getElement("apAccountManagerPage", "paymentSettingDropdown"));
	}

	public void clickPaymentSettingDropdownOption(String paymentSettings) {
		By paymentSettingsResult = getElement("apAccountManagerPage", "paymentSettingDropdownOption",
				paymentSettings.trim());
		click(paymentSettingsResult);
	}

	public void clickCommissionFrequencyDropdown() {
		click(getElement("apAccountManagerPage", "commissionFrequencyDropdown"));
	}

	public void clickCommissionFrequencyDropdownOption(String commissionFrequency) {
		By commissionFrequencyResult = getElement("apAccountManagerPage", "commissionFrequencyDropdownOption",
				commissionFrequency.trim());
		click(commissionFrequencyResult);
	}

	public boolean isFieldDisabled(By locator) {
		return !driver.findElement(locator).isEnabled();
	}

	public void iscommissionBufferDisabled(String bufferDisabled) {
		By disabledField = getElement("apAccountManagerPage", "disabledCadConversionBufferField",
				bufferDisabled.trim());

		if (isFieldDisabled(disabledField)) {
			System.out.println("Field is disabled");
		}
	}

	public void typeCadInvoiceReferenceField(String cadInvoiceReference) {
		setText(getElement("apAccountManagerPage", "cadInvoiceReferenceField"), cadInvoiceReference);
	}

	public void clickCadPaymentMethodDropdown() {
		click(getElement("apAccountManagerPage", "cadPaymentMethodDropdown"));
	}

	public void clickCadPaymentMethodDropdownOption(String cadPaymentMethodDropdown) {
		By cadPaymentMethodResult = getElement("apAccountManagerPage", "cadPaymentMethodDropdownOption",
				cadPaymentMethodDropdown.trim());
		click(cadPaymentMethodResult);
	}

	public void clickSaveChangesButton() {
		click(getElement("apAccountManagerPage", "saveChangesButton"));
	}

	public String getModalMessage() {
		waitForInvisibilityOfLoader();
		return waitForElementVisible(getElement("apAccountManagerPage", "accountManagerUpdationSuccessModal")).getText()
				.trim();
	}

	public void clickAccountManagerModalCloseButton() {
		click(getElement("apAccountManagerPage", "accountManagerModalCloseButton"));
	}

	public void clickCommissionStatusButton(String commissionStatusButton) {
		By commissionStatusEditButton = getElement("apAccountManagerPage", "commissionStatusButton",
				commissionStatusButton.trim());
		click(commissionStatusEditButton);
	}

	public void clickEditCommissionStatus() {
		click(getElement("apAccountManagerPage", "editCommissionStatus"));
	}

	public void clickSaveCommissionStatus() {
		click(getElement("apAccountManagerPage", "saveCommissionStatus"));
	}

	public String getStatusModalMessage() {
		waitForInvisibilityOfLoader();
		return waitForElementVisible(getElement("apAccountManagerPage", "statusSuccessModal")).getText().trim();
	}

	public void clickStatusSuccessModalCloseButton() {
		click(getElement("apAccountManagerPage", "statusSuccessModalCloseButton"));
	}

	public String checkCommissionStatusButton(String commissionStatusButton) {
		By commissionStatusEditButton = getElement("apAccountManagerPage", "commissionStatusButton",
				commissionStatusButton.trim());
		click(commissionStatusEditButton);
		return commissionStatusButton;
	}

	public boolean isaccountActive() {
		waitForInvisibilityOfLoader();
		return isElementDisplayed(getElement("apAccountManagerPage", "ActiveCommissionStatus"));
	}

}
