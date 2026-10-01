Feature: AP Accounts Module
  As a user
  I want to navigate to AP account page
  and validate its functionalities
 
  Background:
    Given user is on login page
    When user logs into the application with valid credentials
    Then Calculate page load time
    Then user navigates to "A/P Admin" login
    Then Calculate page load time
    And AP dashboard should be loaded
    When user clicks AP Account tile
    And AP Account page should be loaded
    
# Create Account Scenarios
 
  @apAccount @apAccount_create @e2e-flow @positive @smoke @apFlow
  Scenario Outline: <TCID>: Create AP Account Successfully
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    And user opens create new AP account modal
    When user enters unique account name
    And user saves the AP account
    Then Account creation success message should be displayed
    And user closes the popup modal
    And user navigates to dashboard page
 
    Examples:
      | ExcelPath     | SheetName     | TCID      |
      | Testdata.xlsx | apAccountData | TC_AP_001 |
 
  @apAccount @apAccount_create @negative @regression
  Scenario Outline: <TCID>: Prevent AP Account Creation With Duplicate Name
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    And user opens create new AP account modal
    When user enters existing account name in the account name field
    And user saves the AP account
    Then existing account error message should be displayed
    And user clicks cancel button
    And user navigates to dashboard page
 
    Examples:
      | ExcelPath     | SheetName     | TCID |
      | Testdata.xlsx | apAccountData | TC_AP_002 |
# Duplicate Account Scenarios
 
  @apAccount @apAccount_duplicate @positive @regression
  Scenario Outline: <TCID>: Duplicate AP Account Successfully
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    When user searches for account
    And user opens account ellipsis menu
    When user selects duplicate account in menu option
    And user enters the account name
    And user clicks save changes button in AP account module
    Then duplication success message should be displayed
    And user closes the popup modal
    Then user clicks clear filter button
    And user navigates to dashboard page
 
    Examples:
      | ExcelPath     | SheetName     | TCID      |
      | Testdata.xlsx | apAccountData | TC_AP_003 |
 
  @apAccount @apAccount_duplicate @negative @regression
  Scenario Outline: <TCID>: Prevent AP Account Duplication With Existing Name
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    When user searches for account
    And user opens account ellipsis menu
    When user selects duplicate account in menu option
    When user enters existing account name in the account name field
    And user clicks save changes button in AP account module
    Then duplication error message should be displayed
    And user closes the popup modal
    And user clicks cancel button
    Then user clicks clear filter button
    And user navigates to dashboard page
 
    Examples:
      | ExcelPath     | SheetName     | TCID      |
      | Testdata.xlsx | apAccountData | TC_AP_004 |
# Search Account Scenarios
 
  @apAccount @apAccount_search @positive @regression @searchApAccount
  Scenario Outline: <TCID>: Search AP Account By Account Name
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    When user enters AP account name in search Accounts field
    And user clicks the search icon button
    Then the search results should include the corresponding account
    Then user clicks clear filter button
    And user navigates to dashboard page
 
    Examples:
      | ExcelPath     | SheetName     | TCID      |
      | Testdata.xlsx | apAccountData | TC_AP_005 |
 
  @apAccount @apAccount_search @positive @regression
  Scenario Outline: <TCID>: Search AP Account By Payee Name
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    When user selects payee name from dropdown
    And user clicks the search button
    Then the search results should include the corresponding account
    Then user clicks clear filter button
    And user navigates to dashboard page
 
    Examples:
      | ExcelPath     | SheetName     | TCID      |
      | Testdata.xlsx | apAccountData | TC_AP_006 |
 
  @apAccount @apAccount_search @positive @regression
  Scenario Outline: <TCID>: Search AP Account Using Account & Payee Filters
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    When user enters AP account name in search Accounts field
    And user selects payee name from dropdown
    And user clicks the search button
    Then the search results should include the corresponding account
    Then user clicks clear filter button
    And user navigates to dashboard page
 
    Examples:
      | ExcelPath     | SheetName     | TCID      |
      | Testdata.xlsx | apAccountData | TC_AP_007 |
# Archive & Restore Scenarios
 
  @apAccount @apAccount_archive @positive @regression
  Scenario Outline: <TCID>: Archive Active AP Account Successfully
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    When user searches for account
    And user opens account ellipsis menu
    When user selects archive account in menu option
    Then Archived success message should be displayed
    And user closes the popup modal
    And the account should be displayed as archived
    Then user clicks clear filter button
    And user navigates to dashboard page
 
    Examples:
      | ExcelPath     | SheetName     | TCID      |
      | Testdata.xlsx | apAccountData | TC_AP_008 |
 
  @apAccount @apAccount_restore @positive @regression
  Scenario Outline: <TCID>: Restore Archived AP Account Successfully
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    When user searches for account
    And user opens account ellipsis menu
    Then user selects restore account in menu option
    Then restore success message should be displayed
    And user closes the popup modal
    And the account should be displayed as active
    Then user clicks clear filter button
    And user navigates to dashboard page
 
    Examples:
      | ExcelPath     | SheetName     | TCID      |
      | Testdata.xlsx | apAccountData | TC_AP_009 |
# Delete Scenarios
 
  @apAccount @apAccount_delete @positive @regression
  Scenario Outline: <TCID>: Delete Archived Non-Payable AP Account
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    When user searches for account
    And user opens account ellipsis menu
    Then delete option should not be visible for active account
    When user archives the account
    And user opens account ellipsis menu
    Then delete option should be visible for archived account
    And user selects delete account in menu option
    Then delete confirmation message should be displayed
    And user confirms account deletion
    Then deletion success message should be displayed
    And user closes the popup modal
    And user verifies account not listed in search results
    Then user clicks clear filter button
    And user navigates to dashboard page
 
    Examples:
      | ExcelPath     | SheetName     | TCID      |
      | Testdata.xlsx | apAccountData | TC_AP_010 |
 
  @apAccount @apAccount_delete @negative @regression
  Scenario Outline: <TCID>: Prevent Deletion Of Payable AP Account
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    When user creates new AP account
    And user creates payable with AP account
    When user searches for payable account
    And user opens account ellipsis menu of payable account
    When user archives the account
    And user opens account ellipsis menu of payable account
    And user selects delete account in menu option
    Then restriction message should be displayed
    And user closes the popup modal
    Then user clicks clear filter button
    And user navigates to dashboard page
 
    Examples:
      | ExcelPath     | SheetName     | TCID      |
      | Testdata.xlsx | apAccountData | TC_AP_011 |
 