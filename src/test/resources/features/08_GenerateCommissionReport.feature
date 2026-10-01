Feature: Generate Commission Report Module
  As a user  
  I want to generate commission reports
  So that I can validate commission details  

  Background:
    Given user is on login page
    When user logs into the application with valid credentials
    Then user navigates to "A/P Admin" login
    And AP dashboard should be loaded
    Then user navigate to "Generate Commission Report" from commissions Tile
    Then generate commission report page should be loaded

  @commission @e2e-flow2 @generatecommissionreport @positive @smoke @regression
  Scenario Outline: <TCID>:  Generate commission report with valid criteria
    
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
  	When the user enters valid end date
  	And the user selects account manager
  	And the user submits the commission report request
  	Then a report generation success message should be displayed

    Examples:
      | ExcelPath     | SheetName                    | TCID      |
      | Testdata.xlsx | GenerateCommissionReportData | TC_GC_001 |
