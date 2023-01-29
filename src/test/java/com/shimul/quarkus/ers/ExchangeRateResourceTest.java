package com.shimul.quarkus.ers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.core.json.JsonObject;

import static com.shimul.quarkus.ers.TestUtils.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@QuarkusTestResource(WiremockExtensions.class)
@TestHTTPEndpoint(ExchangeRateResource.class)
public class ExchangeRateResourceTest {
    
    @Test
    @DisplayName("Given two supported currency return expected rate")
    void testRateEndpoint() {
    	given()
    		.queryParam("fromCurr", EUR_CURRENCY_CODE)
    		.queryParam("toCurr", USD_CURRENCY_CODE)
    		.when().get("/rate")
    			.then()
    				.statusCode(200)
    				.body("Rate", is(EUR_TO_USD_RATE));
    }
    
    @Test
    @DisplayName("Given an unsupported currency returns failure")
    void testRateEndpointWithNonSupportedCurrency() {
    	given()
    		.queryParam("fromCurr", EUR_CURRENCY_CODE)
    		.queryParam("toCurr", "BDT")
    		.when().get("/rate")
    			.then()
    				.statusCode(404)
    				.body("Error.Code", is("01"))
    				.body("Error.Description", containsString("Currency Combination is not supported"));
    }
    
    @Test
    @DisplayName("Returns succesfully the list of supported currencies")
    void testCurrenciesEndpoint() {
    	given()
			.when().get("/currencies")
				.then()
					.statusCode(200)
					.body(containsString(USD_CURRENCY_CODE), 
						  containsString(EUR_CURRENCY_CODE),
						  containsString(DKK_CURRENCY_CODE))
					.body("size()", is(3));
    }
    
    @Test
    @DisplayName("Given to supported currencies and an amount, it returns the expected conversion amount")
    void testConvertEndpoint() {
    	JsonObject inputJson = new JsonObject()
    								.put("fromCurr", USD_CURRENCY_CODE)
    								.put("toCurr", EUR_CURRENCY_CODE)
    								.put("amount", AMOUNT_TO_CONVERT);
    	given()
    		.contentType(ContentType.JSON)
    		.body(inputJson.toString())
    		.when().post("/convert")
    			.then()
    				.statusCode(200)
    				.body("ConvertedAmount", is(USD_TO_EUR_CONVERTED_AMT));
    		
    }
}
