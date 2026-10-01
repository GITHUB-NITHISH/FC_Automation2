package com.automation.utils;

import io.restassured.response.Response;
import static org.testng.Assert.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Map;

import com.automation.dataprovider.JsonDataReader;
import com.automation.utils.ApiRequestBuilder;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScenarioContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ApiServiceUtil {

	private static final LoggerUtils logger = LoggerUtils.getInstance();

	private Response response;

	public void executeFullShipmentFlow() {

		logger.info("===== START SHIPMENT FLOW =====");

		String customerId = "3WccpAnIo6xbzpljkSpTIicj7JHZBi5O";
		String shipmentId;
		String bookingId;

		try {

			// ================= LOGIN =================
			String username = ScenarioContext.getTestData("UserName");
			String password = ScenarioContext.getTestData("Password");

			Map<String, String> loginBody = new HashMap<>();
			loginBody.put("login_id", username);
			loginBody.put("password", password);
			loginBody.put("turnstile", "XXXX.DUMMY.TOKEN.XXXX");
			loginBody.put("recaptcha", "XXXX.DUMMY.TOKEN.XXXX");

			response = ApiRequestBuilder.getRequest().body(loginBody).post("/auth/login");
			assertEquals(response.getStatusCode(), 200);

			String token = response.jsonPath().getString("auth_token");
			ApiRequestBuilder.setAuthToken(token);

			logger.info("✅ Login successful");

			// ================= CREATE SHIPMENT =================
			String createBody = JsonDataReader.getPayload("ShipmentPayload", "createShipment");

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(createBody);

			int year = Integer.parseInt(ScenarioContext.getTestData("Year"));
			int month = Integer.parseInt(ScenarioContext.getTestData("Month"));
			int day = Integer.parseInt(ScenarioContext.getTestData("Day"));

			ObjectNode shipmentNode = (ObjectNode) root.get("shipment");

			// shipping_details
			ObjectNode shippingDetails = (ObjectNode) shipmentNode.get("shipping_details");
			ObjectNode expectedDate = (ObjectNode) shippingDetails.get("expected_ship_date");
			expectedDate.put("year", year);
			expectedDate.put("month", month);
			expectedDate.put("day", day);

			// ltldispatch
			ObjectNode dispatchDate = (ObjectNode) shipmentNode.get("ltldispatch").get("date");
			dispatchDate.put("year", year);
			dispatchDate.put("month", month);
			dispatchDate.put("day", day);

			// ✅ EXCEL DATA
			String companyName = FakerUtil.generateCompanyName();
			String address1 = FakerUtil.generateStreetAddress();
			String city = FakerUtil.generatePostalCodeDetails().get("city");
			String province = FakerUtil.generatePostalCodeDetails().get("Province");
			String country = FakerUtil.generatePostalCodeDetails().get("Country");
			String postalCode = FakerUtil.generatePostalCodeDetails().get("postalCode");
			String contactName = FakerUtil.generateFirstName();
			String phone = FakerUtil.generatePhoneNumber();
			String email = FakerUtil.generatePersonalEmail();

			// ✅ MAIN DESTINATION
			ObjectNode destination = (ObjectNode) shipmentNode.get("shipping_details").get("destination");

			destination.put("name", companyName);

			ObjectNode address = (ObjectNode) destination.get("address");
			address.put("address_line1", address1);
			address.put("city", city);
			address.put("region", province);
			address.put("country", country);
			address.put("postal_code", postalCode);

			destination.put("contact_name", contactName);
			((ObjectNode) destination.get("phone_number")).put("number", phone);

			ArrayNode emails = (ArrayNode) destination.get("email_addresses");
			emails.removeAll();
			emails.add(email);

			// ✅ RATE REQUEST DESTINATION ALSO UPDATE
			ArrayNode rateRequests = (ArrayNode) shipmentNode.get("rate_requests");

			if (rateRequests != null) {
				for (JsonNode rate : rateRequests) {

					ObjectNode rateDest = (ObjectNode) rate.get("shipping_details").get("destination");

					rateDest.put("name", companyName);

					ObjectNode rateAddress = (ObjectNode) rateDest.get("address");
					rateAddress.put("address_line1", address1);
					rateAddress.put("city", city);
					rateAddress.put("region", province);
					rateAddress.put("country", country);
					rateAddress.put("postal_code", postalCode);

					rateDest.put("contact_name", contactName);
					((ObjectNode) rateDest.get("phone_number")).put("number", phone);

					ArrayNode rateEmails = (ArrayNode) rateDest.get("email_addresses");
					rateEmails.removeAll();
					rateEmails.add(email);
				}
			}

			String updatedCreateBody = mapper.writeValueAsString(root);

			response = ApiRequestBuilder.getRequest().body(updatedCreateBody)
					.post("/shipment/customer/" + customerId + "/shipments");

			assertEquals(response.getStatusCode(), 200);

			shipmentId = response.jsonPath().getString("shipment_id");
			ScenarioContext.set("ShipmentId", shipmentId);

			logger.info("✅ Shipment created: " + shipmentId);

			// ================= RATE REQUEST =================
			String rateBody = JsonDataReader.getPayload("ShipmentPayload", "RateRequestId");

			response = ApiRequestBuilder.getRequest().body(rateBody)
					.post("/shipment/customer/" + customerId + "/shipment/" + shipmentId + "/rate-request");

			assertEquals(response.getStatusCode(), 200);

			// ================= GET RATE =================
			response = ApiRequestBuilder.getRequest()
					.get("/shipment/customer/" + customerId + "/shipment/" + shipmentId + "/rate-request");
			assertEquals(response.getStatusCode(), 200);
			String rateId = response.jsonPath().getString("results.multi_rate_requests[0].rates[0].id");
			logger.info("✅ Rate ID: " + rateId);

//			String rateId = null;
//			for (int i = 0; i < 20; i++) {
//				response = ApiRequestBuilder.getRequest()
//						.get("/shipment/customer/" + customerId + "/shipment/" + shipmentId + "/rate-request");
//				assertEquals(response.getStatusCode(), 200);
//				response.prettyPrint();
//				rateId = response.jsonPath().getString("results.multi_rate_requests[0].rates[0].id");
//				if (rateId != null) {
//					logger.info("✅ Rate ID: " + rateId);
//					break;
//				}
//				logger.info("⏳ Waiting for rate generation...");
//			}
//
//   		✅ Fail if still null
//			if (rateId == null) {
//				throw new RuntimeException("❌ Rate not generated");
//			}

			// ================= UPDATE SHIPMENT =================
			String updateBody = JsonDataReader.getPayload("ShipmentPayload", "ShipmentUpdate");

			JsonNode updateRoot = mapper.readTree(updateBody);
			ObjectNode updateShipment = (ObjectNode) updateRoot.get("shipment");

			ObjectNode updShipDetails = (ObjectNode) updateShipment.get("shipping_details");
			ObjectNode updDate = (ObjectNode) updShipDetails.get("expected_ship_date");

			updDate.put("year", year);
			updDate.put("month", month);
			updDate.put("day", day);

			ObjectNode updDispatch = (ObjectNode) updateShipment.get("ltldispatch").get("date");
			updDispatch.put("year", year);
			updDispatch.put("month", month);
			updDispatch.put("day", day);

			// ✅ MAIN DESTINATION UPDATE
			ObjectNode updateDest = (ObjectNode) updateShipment.get("shipping_details").get("destination");

			updateDest.put("name", companyName);

			ObjectNode updAddress = (ObjectNode) updateDest.get("address");
			updAddress.put("address_line1", address1);
			updAddress.put("city", city);
			updAddress.put("region", province);
			updAddress.put("country", country);
			updAddress.put("postal_code", postalCode);

			updateDest.put("contact_name", contactName);
			((ObjectNode) updateDest.get("phone_number")).put("number", phone);

			ArrayNode updEmails = (ArrayNode) updateDest.get("email_addresses");
			updEmails.removeAll();
			updEmails.add(email);

			// ✅ RATE REQUEST ALSO UPDATE
			ArrayNode updRateRequests = (ArrayNode) updateShipment.get("rate_requests");

			if (updRateRequests != null) {
				for (JsonNode rate : updRateRequests) {

					ObjectNode rateDest = (ObjectNode) rate.get("shipping_details").get("destination");

					rateDest.put("name", companyName);

					ObjectNode rateAddress = (ObjectNode) rateDest.get("address");
					rateAddress.put("address_line1", address1);
					rateAddress.put("city", city);
					rateAddress.put("region", province);
					rateAddress.put("country", country);
					rateAddress.put("postal_code", postalCode);

					rateDest.put("contact_name", contactName);
					((ObjectNode) rateDest.get("phone_number")).put("number", phone);

					ArrayNode rateEmails = (ArrayNode) rateDest.get("email_addresses");
					rateEmails.removeAll();
					rateEmails.add(email);
				}
			}

			String updatedPayload = mapper.writeValueAsString(updateRoot);

			response = ApiRequestBuilder.getRequest().body(updatedPayload)
					.post("/shipment/customer/" + customerId + "/shipment/" + shipmentId);

			assertEquals(response.getStatusCode(), 200);

			logger.info("✅ Shipment updated");

			// ================= BOOK =================
			String bookBody = JsonDataReader.getPayload("ShipmentPayload", "BookShipment");

			response = ApiRequestBuilder.getRequest().body(bookBody)
					.post("/shipment/customer/" + customerId + "/shipment/" + shipmentId + "/book");

			assertEquals(response.getStatusCode(), 200);

			bookingId = response.jsonPath().getString("booking_request_id");

			logger.info("✅ Booking ID: " + bookingId);

			// ================= WAIT + VALIDATE =================
			int maxRetries = 10;

			for (int i = 0; i < maxRetries; i++) {

				response = ApiRequestBuilder.getRequest()
						.get("/customer/" + customerId + "/booking-request/" + bookingId);

				String status = response.jsonPath().getString("result.status");

				if ("done".equalsIgnoreCase(status)) {

					String bookedAt = response.jsonPath().getString("result.booking.booked_at");

					Instant instant = Instant.parse(bookedAt);
					ZonedDateTime zonedDate = instant.atZone(ZoneId.of("UTC"));

					String formattedDate = zonedDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));

					ScenarioContext.setGlobal("SHIPMENTDATE", formattedDate);

					logger.info("✅ Shipment Date: " + formattedDate);
					break;
				}

				if ("failed".equalsIgnoreCase(status)) {
					throw new RuntimeException("❌ Booking failed");
				}
			}

			// ================= TRANSACTION =================
			response = ApiRequestBuilder.getRequest()
					.get("/shipment/customer/" + customerId + "/shipment/" + shipmentId);

			assertEquals(response.getStatusCode(), 200);

			// ================= INVOICE =================
			response = ApiRequestBuilder.getRequest().get("/shipment/customer/" + customerId + "/shipment/" + shipmentId
					+ "/financial-documents/file-requests");

			assertEquals(response.getStatusCode(), 200);

			String invoiceNumber = response.jsonPath()
					.getString("results.find { it.document.type == 'shipment-credit-card-invoice' }.document.number");

			String transactionId = response.jsonPath()
					.getString("results.find { it.document.type == 'shipment-order-details' }.document.number");

			ScenarioContext.setGlobal("INVOICENUMBER", invoiceNumber);
			ScenarioContext.setGlobal("TRANSACTIONID", transactionId);

			logger.info("✅ Invoice: " + invoiceNumber);
			logger.info("✅ Transaction ID: " + transactionId);

			logger.info("===== ✅ SHIPMENT FLOW COMPLETED =====");

		} catch (Exception e) {
			throw new RuntimeException("❌ Shipment flow failed", e);
		}
	}

	public void executeCustomerSignupFlow() {

		logger.info("===== START CUSTOMER SIGNUP FLOW =====");

		try {

			// ✅ Load payload from JSON
			String signupBody = JsonDataReader.getPayload("SignupPayload", "customer");

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(signupBody);

			// ✅ Dynamic data (Faker / Excel / Context)
			String firstName = FakerUtil.generateFirstName();
			String lastName = FakerUtil.generateLastName();
			String email = FakerUtil.generatePersonalEmail();
			String phone = FakerUtil.generatePhoneNumber();
			String companyName = FakerUtil.generateCompanyName();

			// ✅ Update JSON fields
			ObjectNode userNode = (ObjectNode) root.get("user");

			userNode.put("first_name", firstName);
			userNode.put("last_name", lastName);
			userNode.put("email", email);
			userNode.put("phone_number", phone);
			userNode.put("company_name", companyName);

			// ✅ Final payload
			String updatedSignupBody = mapper.writeValueAsString(root);

			// ✅ API call
			response = ApiRequestBuilder.getRequest().body(updatedSignupBody).post("/customer/signup");

			assertEquals(response.getStatusCode(), 200);

			// ✅ Extract important values
			String customerId = response.jsonPath().getString("customer_id");
			String authToken = response.jsonPath().getString("auth_token");

			// ✅ Store in context
			ScenarioContext.setGlobal("CUSTOMER_ID", customerId);
			ScenarioContext.setGlobal("CUSTOMER_EMAIL", email);

			if (authToken != null) {
				ApiRequestBuilder.setAuthToken(authToken);
			}

			logger.info("✅ Customer created successfully");
			logger.info("✅ Customer ID: " + customerId);

			logger.info("===== ✅ CUSTOMER SIGNUP COMPLETED =====");

		} catch (Exception e) {
			throw new RuntimeException("❌ Customer signup failed", e);
		}
	}

}