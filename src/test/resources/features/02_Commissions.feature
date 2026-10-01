Feature: Sales Team & Commissions 
  As a user
  I want to manage account managers
  So that sales operations can be handled effectively

  Background:
    Given user is on login page
    When user logs into the application with valid credentials

  @accountManager @accountManager_create @e2e-flow
  Scenario Outline: <TCID>: Create Account Manager Successfully
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    Then user navigates to "Sales Admin" login
    When user navigates to Commissions under Sales menu
    And user accesses Create New Account Manager section
    When user defines account manager role as "<UserRole>"
    And user associates an existing Freightcom user to the account manager
    And user assigns the account manager to a sales team
    And user verifies account manager personal details
    And user verifies account manager contact details
    And user verifies account manager login credentials
    And user assigns role permissions for the account manager
    And user configures initial signup settings
    And user uploads account manager profile image
    And user submits the account manager details
    Then account manager should be created successfully

    Examples:
      | TCID      | ExcelPath     | SheetName               | UserRole        |
      | TC_AM_001 | Testdata.xlsx | AccountManagerData | Account Manager |

  @commissions @editCommissions @e2e-flow
  Scenario Outline: <TCID>: Edit Account Manager Commission Structure Successfully
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    When user navigates to Commissions under Sales menu
    When user navigates to Edit Account Manager section
    And user selects account manager in edit account manager
    And user configures operational costs for shipment types
    Then account manager should be updated successfully
    When user navigates to Edit Commissions section
    And user selects account manager in edit commission
    And user selects commission structure
    And user enters commission details
    And user saves the commission details
    Then commission details should be updated successfully

    Examples:
      | TCID        | ExcelPath     | SheetName       |
      | TC_CM_001   | Testdata.xlsx | CommissionData  |
      
