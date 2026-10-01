Feature: Search Commission Module
  As a user
  I want to navigate to Search commission page
  and validate commissions values after shipment

  Background:
    Given user is on login page
    When user logs into the application with valid credentials
    Then Calculate page load time
    And AP dashboard should be loaded
    
     @commission @apsearchcommissions @positive @smoke @regression
  Scenario Outline: <TestCaseId>: Update Account Manager Commission Successfully

   	Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
   	
   	When user navigate to account manager from apdashboard
   	Then search commissions page should be loaded
   	Then user search commissions with transaction number and date
   	
   	    Examples:
      | ExcelPath     | SheetName               | TCID      |
      | Testdata.xlsx | apSearchCommissionsData | TC_SC_001 |
 
   	
   	