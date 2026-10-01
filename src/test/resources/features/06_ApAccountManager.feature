Feature: Account Manager Commission Module
  As a user
  I want to navigate to Account Manager commission page
  and validate commission configuration functionalities

  Background:
    Given user is on login page
    When user logs into the application with valid credentials
    Then Calculate page load time
    #Then user navigates to "A/P Admin" login
    Then Calculate page load time
    And AP dashboard should be loaded

# Account Manager Commission Scenarios

  @commission @apaccountmanager @positive @apFlow @e2e-flow @smoke @regression
  Scenario Outline: <TCID>: Update Account Manager Commission Successfully

   	Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"

    When user navigate to account manager from commissions Tile
	Then account manager page should be loaded
    When user search for account manager
    When user clicks edit button
    And user selects linked payee 
    And user selects account
    And email address should be auto populated correctly
    And user selects payment setting
	And user selects commission frequency 
	And Conversion Buffer is disabled for Pay in CAD option 
	And user enters CAD invoice reference number
	And user select preferred payment method
	And user clicks save changes button
	Then Edit success modal message should display
	When user activate commission status of the account manager
	Then  Activation success modal message should display
	And validate the active commission status
	And user navigates to dashboard page

    Examples:
      | ExcelPath     | SheetName               | TCID        |
      | Testdata.xlsx | ApAccountManagerData    | TC_APAM_01  |