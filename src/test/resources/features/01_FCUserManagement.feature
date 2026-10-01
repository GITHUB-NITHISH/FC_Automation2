Feature: Commissions - Account Manager Module
  As a user
  I want to manage account managers
  So that sales operations can be handled effectively

  Background:
    Given user is on login page
    When user logs into the application with valid credentials

  @internalUser @internalUser_create @e2e-flow
  Scenario Outline: <TCID>: Create Freightcom internal user Successfully
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    Then user navigates to "Admin Users Super Admin" login
    And user accesses Create New User section
    And user enters internal user details
    And user submits the new user details

    Examples:
      | TCID       | ExcelPath     | SheetName            |
      | TC_USR_001 | Testdata.xlsx | FCUserManagementData |