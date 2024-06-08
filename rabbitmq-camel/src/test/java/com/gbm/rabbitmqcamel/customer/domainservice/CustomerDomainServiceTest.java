package com.gbm.rabbitmqcamel.customer.domainservice;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
		"app.customer-integration-service.host=http://localhost:${wiremock.server.port}/customer-integration/event"
})
public class CustomerDomainServiceTest
{

	@LocalServerPort
	private Integer port;

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
