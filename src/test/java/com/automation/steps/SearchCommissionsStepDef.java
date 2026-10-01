package com.automation.steps;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import com.automation.dataprovider.ExcelUtil;
import com.automation.pages.web.SearchCommissionsPage;
import com.automation.pages.web.CommissionsPage;
import com.automation.pages.web.CommonPage;
import com.automation.utils.ApiServiceUtil;
import com.automation.utils.CommissionCalculator;
import com.automation.utils.DriverFactory;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;
import com.automation.utils.ApiServiceUtil;
import com.automation.utils.CommissionCalculator.CommissionResult;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SearchCommissionsStepDef {
	private static final LoggerUtils logger = LoggerUtils.getInstance();
	private WebDriver driver;
	private SearchCommissionsPage searchCommissionsPage;
	private CommonPage commonPage;
	private ApiServiceUtil apiServiceUtil;
	
	Map<String, String> commissionSetupData = ExcelUtil.getRowDataByTestCaseID("CommissionData", "TC_CM_001");

	public SearchCommissionsStepDef() {
		this.driver = DriverFactory.getDriver();
		this.searchCommissionsPage = new SearchCommissionsPage(driver);
		this.commonPage = new CommonPage(driver);
		this.apiServiceUtil = new ApiServiceUtil();
	}
	
	@Then("user initiate the shipment")
	public void user_initiate_the_shipment() {
		apiServiceUtil.executeFullShipmentFlow();
	}

	@Then("search commissions page should be loaded")
	public void search_commissions_page_should_be_loaded() {
        Assert.assertTrue(searchCommissionsPage.isSearchCommissionspageDisplayed());
	}

	@Then("calculate commission details")
	public void calculate_commission_details() {
		ScenarioContext.set("CommissionSetupData", commissionSetupData);

		String txnNumber = ScenarioContext.getTestData("TRANSACTIONID","TxnNo");

		Map<String, Object> costData = CommissionsPage.getDataFromDB(txnNumber);

		CommissionResult result = CommissionCalculator.calculateCommission(costData, commissionSetupData);

		logger.info("Calculated Commission: $" + result.getCommission());
		logger.info("Calculated Commissionable Amount: $" + result.getCommissionableAmount());
		logger.info("Net Revenue : $" + result.getNetRevenue());
		logger.info("Gross Profit : $" + result.getGrossProfit());
		logger.info("OPS amount : $" + result.getOpsAmount());
		logger.info("Customer Name : " + result.getCustomerName());
		logger.info("BOL Number : " + result.getBolNumber());
		logger.info("Total Cost $ " + result.getTotalCost());
		logger.info("Revenue% : " + result.getRevenueStr());
		logger.info("GrossProfit% : " + result.getGrossProfitStr());

		// Write back to Excel
		ExcelUtil.writeCurrentIterationData(result.resultMap());
		
		ScenarioContext.set("CalculatedResult", result);
		ScenarioContext.setGlobal("TOTALCOMMISSION", result.getCommission());
	}

    @When("user search commissions with transaction number and date")
    public void user_search_commissions_with_transaction_number_and_date() {
    	
        String transactionId = ScenarioContext.getTestData("TRANSACTIONID","TxnNo");
        String date = ScenarioContext.getTestData("SHIPMENTDATE","Date");

        searchCommissionsPage.clickDueRadioButton();
        searchCommissionsPage.typeTransactionId(transactionId);
        searchCommissionsPage.selectDate(date);
        searchCommissionsPage.clickSearchButton();
        
        ScenarioContext.setGlobal("DATE", date);
    }
    
    @Then ("validate Fc details for the shipment")
    public void validate_Fc_details_for_the_shipment() {  

        String txnId = ScenarioContext.getTestData("TRANSACTIONID","TxnNo");
        CommissionResult result = ScenarioContext.get("CalculatedResult");
        String shipmentType = commissionSetupData.get("ShipmentType");

        searchCommissionsPage.validateTransactionIdDisplayed(txnId);
        if ("LTL".equalsIgnoreCase(shipmentType)) {
            searchCommissionsPage.validateBOLNumber(result.getBolNumber(), txnId);
        }
        searchCommissionsPage.validateInvoiceNumber(ScenarioContext.getTestData("INVOICENUMBER","InvoiceNumber"), txnId);
        searchCommissionsPage.validateShipmentDate(ScenarioContext.getTestData("SHIPMENTDATE","Date"), txnId);	
    }
    
    @Then("validate commission and shipment type")
    public void validate_commission_and_shipment_type() {
        String txnId = ScenarioContext.getTestData("TRANSACTIONID","TxnNo");
        CommissionResult result = ScenarioContext.get("CalculatedResult");

      searchCommissionsPage.validateShipmentType(commissionSetupData.get("ShipmentType"), txnId);
      searchCommissionsPage.validateCommissionsTypeField(commissionSetupData.get("CommissionStructure"), txnId);
      searchCommissionsPage.validateCommissionsTypePercent(result.getGrossProfitStr(),result.getRevenueStr(),txnId);
        
    }
    
    @Then("validate the commission charges details for the shipment")
    public void validate_the_commission_charges_details_for_the_shipment() {

    	 String txnId = ScenarioContext.getTestData("TRANSACTIONID","TxnNo");
    	 CommissionResult result = ScenarioContext.get("CalculatedResult");
   
    	 searchCommissionsPage.validateTotalCharges(String.format("%.2f", result.getTotalCost()), txnId);
    	 searchCommissionsPage.validateNetRevenue(String.format("%.2f", result.getNetRevenue()), txnId);
    	 searchCommissionsPage.validateOpsCost(String.format("%.2f", result.getOpsAmount()), txnId);
    	 searchCommissionsPage.validateFlatFee(txnId, commissionSetupData.get("ShipmentAmount$"));
         searchCommissionsPage.validateCommissionableAmount(txnId, String.format("%.2f", result.getCommissionableAmount()));
         searchCommissionsPage.validateCommissionTotal(txnId, String.format("%.2f", result.getCommission()));
         searchCommissionsPage.validateGrossProfit(String.format("%.2f", result.getGrossProfit()), txnId);
    }
    
    @Then("validate customer and agent name")
    public void Then_validate_customer_and_agent_name() {	

        String txnId = ScenarioContext.getTestData("TRANSACTIONID","TxnNo");
        CommissionResult result = ScenarioContext.get("CalculatedResult");

        searchCommissionsPage.validateAccountManager(commissionSetupData.get("AccountManager"), txnId);
        searchCommissionsPage.validateCustomerName(result.getCustomerName(), txnId);
        
        ScenarioContext.setGlobal("ACCOUNTMANAGER",commissionSetupData.get("AccountManager"));
        
        Map<String, String> data = new HashMap<>();
	    data.put("AccountManager", commissionSetupData.get("AccountManager")); 
	    ExcelUtil.writeCurrentIterationData(data);
        
        commonPage.clickHomeButton();

    } 
   
}