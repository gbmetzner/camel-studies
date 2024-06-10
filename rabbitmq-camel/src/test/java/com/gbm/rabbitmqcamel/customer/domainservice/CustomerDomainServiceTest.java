package com.gbm.rabbitmqcamel.customer.domainservice;


import com.gbm.rabbitmqcamel.common.BaseTests;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;

public final class CustomerDomainServiceTest extends BaseTests
{

	@Test
	public void testShouldSendCreateEvent() {
		given().port( port )
				.when()
				.post("/customer")
				.then()
				.statusCode(200);
	}

	@Test
	public void testShouldSendUpdateEvent() {
		given().port( port )
				.when()
				.put("/customer")
				.then()
				.statusCode(200);
	}

	@Test
	public void testShouldSendDeleteEvent() {
		given().port( port )
				.when()
				.delete("/customer")
				.then()
				.statusCode(200);
	}


}
