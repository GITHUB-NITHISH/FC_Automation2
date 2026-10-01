Feature: Commission Workflow End To End Flow
  As a finance and operations user
  I want to validate complete commission lifecycle flow
  from account manager creation until payable generation

  Background:
    Given user is on login page
    When user logs into the application with valid credentials
    Then  dashboard should be loaded

# ============================================================
# TC01 - FLAT FEE ONLY
# ============================================================

  @commission @integration @flatFee @domesticCA @domesticUS @ltl @courier @regression
  Scenario Outline: <TestCaseId>: Validate Flat Fee Commission Flow Without Ops Cost And Sub Agent

    Given user loads test data from excel "<ExcelPath>" sheet "<SheetName>" and testcase "<TestCaseId>"

    # STEP 1 - CREATE ACCOUNT MANAGER
    When user navigates to commissions module
    And user creates account manager using existing user
    And user generates and assigns signup code
    Then account manager should be created successfully

    # STEP 2 - CONFIGURE OPERATIONAL COST
    When user opens edit account manager screen
    And user skips operational cost configuration
    Then operational cost configuration should remain empty

    # STEP 3 - CONFIGURE COMMISSION STRUCTURE
    When user navigates to edit commissions page
    And user configures flat fee shipment amount commission
    Then commission structure should be saved successfully

    # STEP 4 - AP CONFIGURATION
    When user creates payment method
    Then payment method should be created successfully

    When user creates AP account
    Then AP account should be created successfully

    When user creates payee for account manager
    Then payee should be created successfully

    # STEP 5 - VERIFY ACCOUNT MANAGER IN AP
    When user navigates to AP commissions account managers screen
    Then created account manager should be listed

    # STEP 6 - CONFIGURE COMMISSION PAYOUT SETTINGS
    When user edits account manager payout configuration
    And user configures account manager type
    And user links payee and AP account
    And user configures payment settings
    And user configures commission frequency
    And user configures CAD and USD payout settings
    Then payout configuration should be saved successfully

    # STEP 7 - CUSTOMER SIGNUP
    When customer signs up using generated signup code
    Then customer signup should be completed successfully

    # STEP 8 - CUSTOMER ACTIVATION
    When user verifies customer email
    And user activates customer from database
    And user marks customer as credit card customer from database
    Then customer should become active successfully

    # STEP 9 - SHIPMENT CREATION
    When customer creates shipment
    And shipment type is "<ShipmentType>"
    And shipment region is "<Region>"
    Then shipment should be booked successfully

    # STEP 10 - INVOICE VALIDATION
    Then invoice should be auto generated and paid

    # STEP 11 - COMMISSION DUE VALIDATION
    When user navigates to search commissions due screen
    Then generated commission should appear under due status
    And commission calculation should be correct

    # STEP 12 - COMMISSION REPORT GENERATION
    When user generates commission report manually
    Then generated commission report should appear under generated status

    # STEP 13 - PAYABLE CREATION
    When commission payable generation process executes
    Then payable should be created successfully

    Examples:
      | ExcelPath     | SheetName       | TestCaseId            | ShipmentType | Region  |
      | Testdata.xlsx | CommissionFlow  | TC01_FlatFee_LTL_CA  | LTL          | DOM_CA  |
      | Testdata.xlsx | CommissionFlow  | TC02_FlatFee_US      | Courier      | DOM_US  |



