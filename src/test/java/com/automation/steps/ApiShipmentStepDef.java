package com.automation.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.WebDriver;

import com.automation.pages.web.ApiShipmentServicePage;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import com.automation.dataprovider.JsonDataReader;
import com.automation.utils.ApiRequestBuilder;

import com.automation.utils.ConfigurationManager;
import com.automation.utils.DriverFactory;
import com.automation.utils.FakerUtil;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;

public class ApiShipmentStepDef {
	private static final LoggerUtils logger = LoggerUtils.getInstance();

	Response response;
	String customerId = "3WccpAnIo6xbzpljkSpTIicj7JHZBi5O";
	String shipmentId;
	String bookingId;

	//  LOGIN
	@Given("user logs in with valid credentials")
	public void user_logs_in() {
		logger.info(" logs in with valid credentials");
	    String username = ScenarioContext.getTestData("UserName");
	    String password = ScenarioContext.getTestData("Password");
	    response = ApiShipmentServicePage.login(username, password);

		assertEquals(response.getStatusCode(),200);
		String token = response.jsonPath().getString("auth_token");
		ApiRequestBuilder.setAuthToken(token);
		System.out.println("Token set: " + token);
	}

	//  CREATE SHIPMENT

	@When("user creates shipment")
	public void user_creates_shipment() {

		logger.info("creating shipment");

		//  1. Get payload
		String body = JsonDataReader.getPayload("ShipmentPayload", "createShipment");

		//  2. Get dynamic values from ScenarioContext
		String year = ScenarioContext.getTestData("Year");
		String month = ScenarioContext.getTestData("Month");
		String day = ScenarioContext.getTestData("Day");

		//  3. Update payload dynamically
		body = ApiShipmentServicePage.shipmentDates(body, year, month, day);

		//  4. Call API with updated payload
		response = ApiRequestBuilder.getRequest().body(body).post("/shipment/customer/" + customerId + "/shipments");

		//  5. Validation
		assertEquals(response.getStatusCode(),200);
		shipmentId = response.jsonPath().getString("shipment_id");

		//  Store for later steps
		ScenarioContext.set("ShipmentId", shipmentId);

		System.out.println("Shipment ID: " + shipmentId);
	}

	//  CREATE RATE REQUEST
	@And("user generates rate request")
	public void user_generates_rate_request() {
		logger.info(" generating rate request");
		response = ApiShipmentServicePage.createRateRequest(customerId, shipmentId);

		assertEquals(response.getStatusCode(),200);	}

	//  GET RATE
	@And("user gets rate details")
	public void user_gets_rate_details() {

		response = ApiShipmentServicePage.getRateRequest(customerId, shipmentId);
		String rateId = response.jsonPath().getString("rates[0].id");
		System.out.println("Rate ID: " + rateId);
		response.prettyPrint();
		assertEquals(response.getStatusCode(),200);
		}

	//  UPDATE SHIPMENT

	@And("user updates shipment with selected rate")
	public void user_updates_shipment_with_selected_rate() {
		logger.info(" initiating shipment after rate request");
	    //  1. Get raw payload
	    String body = JsonDataReader.getPayload("ShipmentPayload", "ShipmentUpdate");

	    //  2. Get dynamic values from ScenarioContext
	    String year = ScenarioContext.getTestData("Year");
	    String month = ScenarioContext.getTestData("Month");
	    String day = ScenarioContext.getTestData("Day");

	    //  3. Update payload dynamically
	    body = ApiShipmentServicePage.updatedShipmentDates(body, year, month, day);

	    //  4. Call API
	    response = ApiRequestBuilder.getRequest()
	            .body(body)
	            .post("/shipment/customer/" + customerId + "/shipment/" + shipmentId);

	    //  5. Validation
	    assertEquals(response.getStatusCode(),200);
	    response.asPrettyString();

	    System.out.println("✅ Shipment updated successfully with dynamic dates");

	}

	//  BOOK SHIPMENT
	@Then("user books the shipment")
	public void user_books_the_shipment() {
		logger.info(" booking the shipment");

		response = ApiShipmentServicePage.bookShipment(customerId, shipmentId);
		response.then().log().all();

		assertEquals(response.getStatusCode(),200);

		bookingId = response.jsonPath().getString("booking_request_id");
		System.out.println("Booking ID: " + bookingId);

	}

	//  GET BOOKING DETAILS
	@And("user validates booking details")
	public void user_validates_booking_details() {
		logger.info(" Getting booking details");

		response = ApiShipmentServicePage.waitForBookingStatusDone(customerId, bookingId);
		response.then().log().all();
		String bookedAt = response.jsonPath().getString("result.booking.booked_at");

	    Instant instant = Instant.parse(bookedAt);
	    ZonedDateTime zonedDate = instant.atZone(ZoneId.of("UTC"));

	    String formattedDate = zonedDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));

		ScenarioContext.setGlobal("SHIPMENTDATE", formattedDate);
		logger.info("✅ Shipment Date (booked_at): " + formattedDate);

		assertEquals(response.getStatusCode(),200);
	}

	//  GET TRANSACTION DETAILS
	@Then("user gets transaction details")
	public void user_gets_transaction_details() {
		logger.info("Getting Transaction details ");
		response = ApiShipmentServicePage.getShipment(customerId, shipmentId);
		response.then().log().all();
		assertEquals(response.getStatusCode(),200);

	}

	//  GET INVOICE NUMBER
	@And("user fetches invoice number")
	public void user_fetches_invoice_number() {
		logger.info(" Getting invoice number");

		response = ApiShipmentServicePage.getInvoiceNumber(customerId, shipmentId);
		response.then().log().all();

		assertEquals(response.getStatusCode(),200);

		String invoiceNumber = response.jsonPath()
				.getString("results.find { it.document.type == 'shipment-credit-card-invoice' }.document.number");
	    String transactionId = response.jsonPath()
		        .getString("results.find { it.document.type == 'shipment-order-details' }.document.number");
		ScenarioContext.setGlobal("INVOICENUMBER", invoiceNumber);

		    ScenarioContext.setGlobal("TRANSACTIONID", transactionId);
		    System.out.println("✅ Transaction ID: " + transactionId);

		
		logger.info("Invoice Number: " + invoiceNumber);

		System.out.println(response.asPrettyString());
	}

}
