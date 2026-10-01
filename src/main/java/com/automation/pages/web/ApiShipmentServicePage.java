package com.automation.pages.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.automation.dataprovider.JsonDataReader;
import com.automation.utils.ApiRequestBuilder;
import com.automation.utils.ScenarioContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;


public class ApiShipmentServicePage {

	//  LOGIN API
	public static Response login(String loginId, String password) {

		Map<String, String> body = new HashMap<>();
		body.put("login_id", loginId);
		body.put("password", password);
		body.put("turnstile", "XXXX.DUMMY.TOKEN.XXXX");
		body.put("recaptcha", "XXXX.DUMMY.TOKEN.XXXX");

		return ApiRequestBuilder.getRequest().body(body).post("/auth/login");
	}

	//  CREATE SHIPMENT
	public static Response createShipment(String customerId) {

		String body = JsonDataReader.getPayload("ShipmentPayload", "createShipment");
		return ApiRequestBuilder.getRequest().body(body).post("/shipment/customer/" + customerId + "/shipments");
	}

	//  GENERATE RATE REQUEST ID
	public static Response createRateRequest(String customerId, String shipmentId) {

		String body = JsonDataReader.getPayload("ShipmentPayload", "RateRequestId");
		return ApiRequestBuilder.getRequest().body(body)
				.post("/shipment/customer/" + customerId + "/shipment/" + shipmentId + "/rate-request");
	}

	//  GET RATE REQUEST
	public static Response getRateRequest(String customerId, String shipmentId) {

		return ApiRequestBuilder.getRequest()
				.get("/shipment/customer/" + customerId + "/shipment/" + shipmentId + "/rate-request");
	}

	//  UPDATE SHIPMENT AFTER RATE
	public static Response updateShipment(String customerId, String shipmentId) {

		String body = JsonDataReader.getPayload("ShipmentPayload", "ShipmentUpdate");

		return ApiRequestBuilder.getRequest().body(body)
				.post("/shipment/customer/" + customerId + "/shipment/" + shipmentId);
	}

	//  BOOK SHIPMENT

	public static Response bookShipment(String customerId, String shipmentId) {

		String body = JsonDataReader.getPayload("ShipmentPayload", "BookShipment");

		return ApiRequestBuilder.getRequest().body(body)
				.post("/shipment/customer/" + customerId + "/shipment/" + shipmentId + "/book");
	}

	//  GET BOOKING REQUEST DETAILS
	public static Response getBookingDetails(String customerId, String bookingRequestId) {

		return ApiRequestBuilder.getRequest().get("/customer/" + customerId + "/booking-request/" + bookingRequestId);
	}

	//  GET TRANSACTION DETAILS
	public static Response getTransactionDetails(String customerId, String shipmentId) {

		Response response = ApiRequestBuilder.getRequest()
				.get("/shipment/customer/" + customerId + "/shipment/" + shipmentId);

		System.out.println("TRANSACTION DETAILS RESPONSE:");
//	    response.prettyPrint();

		return response;
	}

	//  GET SHIPMENT DETAILS (TNX / FULL DATA)
	public static Response getShipment(String customerId, String shipmentId) {

		return ApiRequestBuilder.getRequest().get("/shipment/customer/" + customerId + "/shipment/" + shipmentId);
	}

	// GET FINANCIAL DOCUMENTS (INVOICE NUMBER)
	public static Response getInvoiceNumber(String customerId, String shipmentId) {

		return ApiRequestBuilder.getRequest().get(
				"/shipment/customer/" + customerId + "/shipment/" + shipmentId + "/financial-documents/file-requests");
	}

//	public static Response waitForRateRequestCompletion(String customerId, String shipmentId) {
//
//		int maxRetries = 25;
//		for (int i = 0; i < maxRetries; i++) {
//
//			Response response = getRateRequest(customerId, shipmentId);
//
//			String status = response.jsonPath().getString("status");
//
//			System.out.println("Rate status: " + status);
//
//			//  Case 1: Completed explicitly
//			if (status != null && (status.equalsIgnoreCase("completed") || status.equalsIgnoreCase("success"))) {
//				return response;
//			}
//
//			//  Case 2: Status EMPTY but rates exist
//			if (status == null || status.trim().isEmpty()) {
//
//				List<Object> rates = response.jsonPath().getList("rates");
//
//				if (rates != null && !rates.isEmpty()) {
//					System.out.println(" Rates found, treating as SUCCESS");
//					return response;
//				}
//
//				System.out.println("⚠️ Empty status but no rates yet");
//			}
//
//			//  Case 3: Fail fast
//			if ("failed".equalsIgnoreCase(status)) {
//				throw new RuntimeException("Rate request failed");
//			}
//
//		}
//
//		throw new RuntimeException("Rate request not completed after retries");
//	}

	public static Response waitForBookingStatusDone(String customerId, String bookingRequestId) {

		int maxRetries = 10;

		Response response = null;

		for (int i = 0; i < maxRetries; i++) {

			response = ApiRequestBuilder.getRequest()
					.get("/customer/" + customerId + "/booking-request/" + bookingRequestId);

			//  Debug log
			System.out.println("BOOKING RESPONSE:");
			response.prettyPrint();

			String status = response.jsonPath().getString("result.status");

			System.out.println("Booking status: " + status);

			//  STRICT condition (your requirement)
			if ("done".equalsIgnoreCase(status)) {
				System.out.println(" Booking reached DONE state");
				return response;
			}

			// ❌ Fail case
			if ("failed".equalsIgnoreCase(status)) {
				String errorMsg = response.jsonPath().getString("result.error_message");
				throw new RuntimeException("❌ Booking failed: " + errorMsg);
			}

		}

		throw new RuntimeException("❌ Booking did not reach DONE status after retries");
	}

	public static String shipmentDates(String body, String year, String month, String day) {

		try {
			ObjectMapper mapper = new ObjectMapper();

			//  Convert to JsonNode (NO casting needed)
			JsonNode root = mapper.readTree(body);

			//  Get values from ScenarioContext
			int shipmentYear = Integer.parseInt(year);
			int shipmentMonth = Integer.parseInt(month);
			int shipmentDay = Integer.parseInt(day);

			//  shipment node
			ObjectNode shipment = (ObjectNode) root
					.get("shipment");

			//  1. shipping_details.expected_ship_date
			ObjectNode shippingDetails = (ObjectNode) shipment
					.get("shipping_details");

			updateDateNodeJson(shippingDetails, shipmentYear, shipmentMonth, shipmentDay);

			//  2. rate_requests[]
			ArrayNode rateRequests = (ArrayNode) shipment
					.get("rate_requests");

			if (rateRequests != null) {
				for (JsonNode rate : rateRequests) {

					ObjectNode rateShipping = (ObjectNode) rate
							.get("shipping_details");

					updateDateNodeJson(rateShipping, shipmentYear, shipmentMonth, shipmentDay);
				}
			}

			//  3. ltldispatch.date
			ObjectNode ltldispatch = (ObjectNode) shipment
					.get("ltldispatch");

			ObjectNode dispatchDate = (ObjectNode) ltldispatch
					.get("date");

			dispatchDate.put("year", shipmentYear);
			dispatchDate.put("month", shipmentMonth);
			dispatchDate.put("day", shipmentDay);

			//  Convert back to String
			return mapper.writeValueAsString(root);

		} catch (Exception e) {
			throw new RuntimeException("Failed to update shipment dates", e);
		}
	}
	
	public static String updatedShipmentDates(String body, String year, String month, String day) {

		try {
			ObjectMapper mapper = new ObjectMapper();

			//  Convert to JsonNode (NO casting needed)
			JsonNode root = mapper.readTree(body);

			//  Get values from ScenarioContext
			int shipmentYear = Integer.parseInt(year);
			int shipmentMonth = Integer.parseInt(month);
			int shipmentDay = Integer.parseInt(day);

			//  shipment node
			ObjectNode shipment = (ObjectNode) root
					.get("shipment");

			//  1. shipping_details.expected_ship_date
			ObjectNode shippingDetails = (ObjectNode) shipment
					.get("shipping_details");

			updateDateNodeJson(shippingDetails, shipmentYear, shipmentMonth, shipmentDay);

			//  2. rate_requests[]
			ArrayNode rateRequests = (ArrayNode) shipment
					.get("rate_requests");

			if (rateRequests != null) {
				for (JsonNode rate : rateRequests) {

					ObjectNode rateShipping = (ObjectNode) rate
							.get("shipping_details");

					updateDateNodeJson(rateShipping, shipmentYear, shipmentMonth, shipmentDay);
				}
			}

			//  3. ltldispatch.date
			ObjectNode ltldispatch = (ObjectNode) shipment
					.get("ltldispatch");

			ObjectNode dispatchDate = (ObjectNode) ltldispatch
					.get("date");

			dispatchDate.put("year", shipmentYear);
			dispatchDate.put("month", shipmentMonth);
			dispatchDate.put("day", shipmentDay);

			//  Convert back to String
			return mapper.writeValueAsString(root);

		} catch (Exception e) {
			throw new RuntimeException("Failed to update shipment dates", e);
		}
	}


	private static void updateDateNodeJson(ObjectNode shippingDetails, int year,
			int month, int day) {

		ObjectNode expectedDate = (ObjectNode) shippingDetails
				.get("expected_ship_date");

		expectedDate.put("year", year);
		expectedDate.put("month", month);
		expectedDate.put("day", day);
	}

}
