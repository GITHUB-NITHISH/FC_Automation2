Feature: Search Commission Module
  As a user
  I want to navigate to Search commission page
  and validate commissions values after shipment

  Background:
    Given user is on login page
    When user logs into the application with valid credentials
    Then user navigates to "A/P Admin" login
    Then user navigate to "Search Commissions" from commissions Tile
    Then search commissions page should be loaded
    
      @commission @e2e-flow2 @apishipment 
  Scenario Outline: <TCID>: Initiate shipment and get transaction details
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
	Then user initiate the shipment
	    Examples:
      | ExcelPath     | SheetName     | TCID      |
      | Testdata.xlsx | ApiShipment   | TC_Sp_001 |

  @commission @e2e-flow2 @searchcommissions 
  Scenario Outline: <TCID>: Search for a Commission details and validate
    Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
    Then calculate commission details
    When user search commissions with transaction number and date
    Then validate Fc details for the shipment
    Then validate commission and shipment type
    Then validate the commission charges details for the shipment
   	Then validate customer and agent name
   	

    Examples:
      | ExcelPath     | SheetName               | TCID      |
      | Testdata.xlsx | SearchCommissionsData   | TC_SC_001 |
