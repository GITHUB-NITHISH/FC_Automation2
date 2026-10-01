package com.automation.pages.web;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.automation.pages.base.BasePage;
import com.automation.utils.LoggerUtils;

public class ApAccountPage extends BasePage {
	
	private static final LoggerUtils logger = LoggerUtils.getInstance();

	public ApAccountPage(WebDriver driver) {
		super(driver);
	}

	private static final By allAccountNames = 
			By.xpath("//div[contains(@class,'customer-data')]//div[@class='value-label']");

	
    public void openCreateNewAccountModal() {
    	waitForInvisibilityOfLoader();
	    click(getElement("apAccountPage", "createNewAccountButton"));
    }

	public void enterAccountName(String accountName) {
		waitForInvisibilityOfLoader();
		setText(getElement("apAccountPage", "accountNameField"), accountName);
	}

    public void clickSaveAccount() {
    	click(getElement("apAccountPage", "saveAccount"));
        waitForInvisibilityOfLoader();
    }

    public void clickSaveChanges() {
        logger.info("Clicking Save Changes");
        click(getElement("apAccountPage", "saveChanges"));
    }

    public void clickCloseButton() {
    	waitForInvisibilityOfLoader();
    	click(getElement("apAccountPage", "closeButton"));
    }

    public void clickCancelButton() {
        waitForInvisibilityOfLoader();
        click(getElement("apAccountPage", "cancelButton"));
    }
    
    public void clickHomeButton() {
    	  waitForInvisibilityOfLoader();
          click(getElement("apAccountPage", "homeButton"));
          waitForInvisibilityOfLoader();
    }

    public void clickClearFilter() {
        click(getElement("apAccountPage", "clearFilterButton"));
    }

    public void clickSearchButton() {
        click(getElement("apAccountPage", "searchButton"));
        waitForInvisibilityOfLoader();
    }

    public void clickSearchIcon() {
        click(getElement("apAccountPage","searchAccountIconButton"));
    }

    public void enterSearchAccountName(String accountName) {
        waitForInvisibilityOfLoader();
        setText(getElement("apAccountPage", "searchAccountsField"), accountName);
    }

    public void searchForAccount(String accountName) throws InterruptedException {
        logger.info("Searching for account: " + accountName);
        waitForInvisibilityOfLoader();
        setText(getElement("apAccountPage","searchAccountsField"), accountName);
        click(getElement("apAccountPage","searchAccountIconButton"));
        waitForInvisibilityOfLoader();
    }
    
    public void createApAccount(String accountName) throws InterruptedException {
    	 openCreateNewAccountModal();
    	 waitForInvisibilityOfLoader();
    	 setText(getElement("apAccountPage","accountNameField"), accountName);
    	 click(getElement("apAccountPage","saveAccount"));
         waitForInvisibilityOfLoader();
         click(getElement("apAccountPage","closeButton"));
         waitForInvisibilityOfLoader();
    }
    

    //  DROPDOWN ACTIONS

    public void openPayeeDropdown() {
    	click(getElement("apAccountPage","payeesDropdown"));
    }

    public void selectPayee(String payees) {
        selectMultipleDropdownElements(getElement("apAccountPage", "payeeDropdownOptions"), payees);
    }

    //  ACCOUNT ACTIONS

    public void clickEllipsisButton(String accountName) {
        waitForInvisibilityOfLoader();
        
        By ellipsisButton = getElement("apAccountPage", "ellipsisButton", accountName.trim());

//        By ellipsis = By.xpath(String.format(
//                "//div[normalize-space()='%s']//parent::div//following-sibling::app-ellipsis-menu//button[@type='button']",
//                accountName.trim()));

        logger.info("Clicking ellipsis for: " + accountName);
        click(ellipsisButton);
    }

    public void clickDuplicateAccountOption() {
        logger.info("Clicking Duplicate option");
        click(getElement("apAccountPage","duplicateAccountOption"));
    }

    public void clickArchiveAccountOption() {
    	click(getElement("apAccountPage","archiveAccountOption"));
    }

    public void clickDeleteAccountOption() {
    	click(getElement("apAccountPage","deleteAccountOption"));
    }
    
    public void clickDeleteAccountButton() {
    	click(getElement("apAccountPage","deleteAccountButton"));
    }
    
    public boolean isDeleteOptionVisible() {
        try {
            waitForInvisibilityOfLoader();
            return driver.findElement(getElement("apAccountPage", "deleteAccountOption")).isDisplayed();
            		
        } catch (Exception e) {
            return false; 
        }
    }

    public void confirmDelete() {
    	click(getElement("apAccountPage","deleteAccountButton"));
    }

    public void clickRestore() {
    	click(getElement("apAccountPage","restoreAccountOption"));
    }

    public void enterAccountNameInDuplicateModal(String accountName) {
        logger.info("Entering duplicate account name: " + accountName);
        waitForInvisibilityOfLoader();
        setText(getElement("apAccountPage", "accountNameField"), accountName);
    }

//    public void openAccountOptions(String accountName) {
//        By dynamicOptionMenu = By.xpath(String.format(
//                "//div[contains(@class,'customer-data') and .//div[@class='value-label' and normalize-space()='%s']]//button[contains(@class,'supporting-text')]",
//                accountName.trim()));
//
//        click(dynamicOptionMenu);
//    }

    public void deleteAccount(String accountName) {
    	clickEllipsisButton(accountName);
//        openAccountOptions(accountName);
        click(getElement("apAccountPage","deleteAccountOption"));
        click(getElement("apAccountPage","deleteAccountButton"));
    }

    // VALIDATIONS

    public boolean isApAccountsPageDisplayed() {
        waitForInvisibilityOfLoader();
        return isElementDisplayed(getElement("apAccountPage", "apAccountsHeader"));
    }

    public boolean isAccountListed(String accountName) throws InterruptedException {
        waitForInvisibilityOfLoader();
        List<String> accounts = getAllAccountNames();

        if (accounts == null || accounts.isEmpty()) {
            return false;
        }
        return accounts.stream()
                .anyMatch(name -> name.equalsIgnoreCase(accountName));
    }

    public boolean isAccountPresent(String accountName) {
        return getAllAccountNames().stream()
                .anyMatch(name -> name.equalsIgnoreCase(accountName));
    }

    public boolean isCreateAccountModalDisplayed() {
        return isElementDisplayed(getElement("apAccountPage", "createNewApAccountHeader"));
    }

    public boolean isAccountGreyedOut(String accountName) {

    	By greyOut = getElement("apAccountPage", "greyOut", accountName);
//        By locator = By.xpath(String.format(
//                "//div[contains(@class,'priority-customer-data-tile')][.//div[@class='value-label' and normalize-space()='%s']]/child::div",
//                accountName.trim()));

        String classAttr = waitForElementVisible(greyOut).getAttribute("class"); 
        return classAttr != null && classAttr.contains("account-grey");
    }

    public String getModalMessage() {
        waitForInvisibilityOfLoader();
        return waitForElementVisible(getElement("apAccountPage", "modalMessage")).getText().trim();
    }

    public String getErrorModalMessage() {
        waitForInvisibilityOfLoader();
        return waitForElementVisible(getElement("apAccountPage", "duplicateError")).getText().trim();   
    }
    
    public String getExistingAccountErrorMessage() {
    	waitForInvisibilityOfLoader();
        return waitForElementVisible(getElement("apAccountPage", "existingAccountError")).getText().trim();   
    }

    //  DATA METHODS

    public List<String> getAllAccountNames() {
        return driver.findElements(allAccountNames).stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());
    }

    public int getLinkedPayeesCount(String accountName) {
    	By linkedPayeesCount = getElement("apAccountPage","linkedPayeesCount", accountName.trim());

        return extractNumber(linkedPayeesCount);
    }
    
    public void createPayable( String payeeName,  String accountName, String invoiceDate,
    		String payDate, String invoiceNumber, String referenceValue, String invoiceAmount) throws InterruptedException {
    	
    	waitForInvisibilityOfLoader();
        click(getElement("apDashboardPage", "createPayableTile"));
        waitForInvisibilityOfLoader();
        click(getElement("apAccountPage", "payeeDropdown"));
        setText(getElement("apAccountPage", "payeeSearchField"),payeeName);
        click(getElement("apAccountPage", "payeeValue", payeeName));
        click(getElement("apAccountPage", "continueButton"));
        waitForInvisibilityOfLoader();

        click(getElement("apAccountPage", "accountDropdown"));
        setText(getElement("apAccountPage", "accountSearchField"),accountName);
        click(getElement("apAccountPage","accountValue", accountName));

        setText(getElement("apAccountPage", "invoiceDate"),invoiceDate);

        setText(getElement("apAccountPage", "payDate"),payDate);

//        click(getElement("apAccountPage", "invoiceNumber"));
        setText(getElement("apAccountPage", "invoiceNumber"),invoiceNumber);
        waitForInvisibilityOfLoader();
        
        setText(getElement("apAccountPage", "referenceField"),referenceValue);

        setText(getElement("apAccountPage", "invoiceAmountField"),invoiceAmount);
        click(getElement("apAccountPage", "createPayableButton"));
        waitForInvisibilityOfLoader();
        click(getElement("apAccountPage", "modalCloseButton"));
        waitForInvisibilityOfLoader();
        click(getElement("apAccountPage", "homeButton"));
        waitForInvisibilityOfLoader();
        click(getElement("apDashboardPage", "apAccountTile"));
    }

    //  COUNT METHODS
    public int getAllCount() {
        return extractNumber(getElement("apAccountPage", "allSection"));
    }

    public int getActiveCount() {
        return extractNumber(getElement("apAccountPage", "activeSection"));
    }

    public int getArchivedCount() {
        return extractNumber(getElement("apAccountPage", "archivedSection"));
    }

}