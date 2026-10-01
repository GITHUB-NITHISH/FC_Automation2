Feature: Search Payables Functionality
  As a user
  I want to search payables using invoice date and payee name
  So that I can verify the correct payable records are displayed
 
 
Background:
  Given user is on login page
  When user logs into the application with valid credentials
  Then user navigates to "A/P Admin" login
  And AP dashboard should be loaded
  When user clicks Search Payables tile
  And Search Payables page should be loaded
 
  @SearchPayable @e2e-flow2 
  Scenario Outline: <TCID>: Validate payable search using Invoice Date and Payee Name
  Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
  And user enters payee name and date for search payables
  And user clicks on Search button
  Then user validates search results
  When user selects payable
  And user clicks on Print Cheque button
  Then print cheque screen should be displayed
  And user selects payable in print cheque
  And user clicks on Close Off button
  And user enters cheque number
  And user selects payment Date
  And user clicks on Generate button
  Then cheque should be generated successfully
 
    Examples:
      | TCID       | ExcelPath     | SheetName          |
      | TC_PAY_001 | TestData.xlsx | SearchPayablesData |
      