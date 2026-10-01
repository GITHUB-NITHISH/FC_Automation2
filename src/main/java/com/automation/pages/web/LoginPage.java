package com.automation.pages.web;
 
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.automation.pages.base.BasePage;
import com.automation.utils.*;
import com.fasterxml.jackson.databind.JsonNode;
import static org.openqa.selenium.support.locators.RelativeLocator.with;

 
 
/**
* LoginPage - Page Object for Login functionality
*/
public class LoginPage extends BasePage {
    private static final LoggerUtils logger = LoggerUtils.getInstance();
    
//    JsonNode testData = JsonDataReader.readJsonData("src/main/resources/data/testdata.json");
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
 
//    // Locators using @FindBy annotation for PageFactory
//    @FindBy(xpath="//input[@id='loginIDInput']")
//	private WebElement usernameField;
// 
//    @FindBy(xpath="//input[@id='passwordInput']")
//	private WebElement passwordField;
//    
// 
//    @FindBy(xpath="//button[text()='Login']")
//	private WebElement loginButton;

	
//	  public void validLogin() {
//		  waitForInvisibilityOfLoader();
//		  String username = JsonDataReader.getStringValue(testData.get("users"),"username");
//		  String password  = JsonDataReader.getStringValue(testData.get("users"),"password");
//		  
//		  driver.findElement(with(By.xpath("//input[@id='passwordInput']")).below(usernameField));
//		  
//		  setText(usernameField, username);
//		  setText(passwordField, password);
//		   click(loginButton);
//		}
 
    /**
     * Enter username in username field
     */
    public LoginPage enterUsername(String username) {
        logger.info("Entering username : " + username);
        setText(getElement("loginPage", "usernameField"), username);
        return this;
    }
 
    /**
     * Enter password in password field
     */
    public LoginPage enterPassword(String password) {
        logger.info("Entering password : *****");
        setText(getElement("loginPage", "passwordField"), password);
        return this;
    }
 
    /**
     * Click on login button
     */
    public void clickLoginButton() {
        logger.info("Clicking Login button");
        click(getElement("loginPage", "loginButton"));
    }
 
    /**
     * Login with username and password
     */
    public void login(String username, String password) {
        logger.info("Performing login");
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
   
    /**
     * Verify login page is loaded
     */
    public boolean isLoginPageLoaded() {
        boolean isLoaded = isElementDisplayed(getElement("loginPage", "usernameField")) &&
                          isElementDisplayed(getElement("loginPage", "passwordField"));
        logger.info("Login page loaded: " + isLoaded);
        return isLoaded;
    }
}
 
 