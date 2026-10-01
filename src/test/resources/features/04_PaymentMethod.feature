Feature: Payment Method Module
  As a user
  I want to navigate to Payment Method page
  and validate cheque payment method functionalities
 
  Background:
    Given user is on login page
    When user logs into the application with valid credentials
    And AP dashboard should be loaded
    When user clicks Payment Method tile
    Then Calculate page load time
    And Payment Method page should be loaded
 
# Create Cheque Payment Method Scenarios
 
  @paymentMethod @paymentMethod_create @e2e-flow @positive @apFlow @regression @smoke
  Scenario Outline: <TCID>: Create Cheque Payment Method Successfully
	Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>" 
    When user clicks add new payment button
    And user enters cheque payment name
    And user enters first cheque number
    And user enters last cheque number
    Then cheque in series count should be displayed correctly
    When user enters send reminder cheque count
    And user clicks save payment method button
    Then cheque payment method creation success message should be displayed
    And user closes success popup modal
    And user selects maximum pagination count
    Then newly created cheque payment method should be displayed
    And user clicks home button
 
    Examples:
      | ExcelPath     | SheetName          | TCID     |
      | Testdata.xlsx | PaymentMethodData  | TC_PM_01 |