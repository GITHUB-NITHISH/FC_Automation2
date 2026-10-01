Feature: API Shipment Module

@apshipment_creation
Scenario Outline: End to End Shipment Flow

  Given user loads test data from "<ExcelPath>" sheet "<SheetName>" for testcase "<TCID>"
  Given user logs in with valid credentials
  When user creates shipment
  And user generates rate request
  And user gets rate details
  And user updates shipment with selected rate
  Then user books the shipment
  And user gets transaction details
  And user validates booking details
  And user fetches invoice number

    Examples:
      | ExcelPath     | SheetName     | TCID      |
      | Testdata.xlsx | ApiShipment   | TC_Sp_001 |