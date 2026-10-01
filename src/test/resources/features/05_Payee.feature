Feature: Payee Module
  As a user
  I want to navigate to Payee page
  and validate payee creation functionalities
 
  Background:
    Given user is on login page
    When user logs into the application with valid credentials
    And AP dashboard should be loaded
    When user clicks Payee tile
    Then Calculate page load time
    And Payee page should be loaded
 
# Create Payee Scenarios
 
  @payee @c @positive @e2e-flow @apFlow @smoke @regression
  Scenario Outline: <TCID>: Create Payee Successfully
	Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>" 
    When user clicks add new payee button
    And user enters payee name
	When user enters mailing address	
    And user validates mailing address
    And user enters postal code   
    Then adress should be auto populated correctly
    When user enters contact details
    When user selects preferred payment method    
    When user selects AP account
    And user clicks save payee button
    Then payee creation success message should be displayed
    And user closes success popup modal
    And user searches for created payee
    Then created payee should be displayed in search results
    And user clicks home button
 
    Examples:
      | ExcelPath     | SheetName        | TCID     |
      | Testdata.xlsx | PayeeData        | TC_PY_01 |