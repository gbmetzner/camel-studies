package com.gbm.rabbitmqcamel.customer.domainservice;

import com.gbm.rabbitmqcamel.common.CustomerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.io.IOException;

@RestController
@RequestMapping("customer")
public class CustomerController
{

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

	private final RestClient customerIntegrationServiceClient;

	@Autowired
	public CustomerController(@Qualifier("customerIntegrationServiceClient") RestClient customerIntegrationServiceClient) {
		this.customerIntegrationServiceClient = customerIntegrationServiceClient;
	}

	@PostMapping
	public void createCustomer() {
		LOGGER.info("Request to create customer");
		sendEvent( "create");
	}

	@PutMapping
	public void updateCustomer() {
		LOGGER.info("Request to update customer");
		sendEvent( "update");
	}

	@DeleteMapping
	public void deleteCustomer() {
		LOGGER.debug("Request to delete customer");
		sendEvent( "delete");
	}

	private void sendEvent( String eventType) {
		LOGGER.info( "Sending event with type: {}", eventType );

		customerIntegrationServiceClient
				.post()
				.contentType( MediaType.APPLICATION_JSON )
				.body(new CustomerEvent( 1, eventType))
				.retrieve().onStatus(HttpStatusCode::isError, (request, response) -> {
					throw new ErrorResponseException(response.getStatusCode());
				});

		LOGGER.info("Event sent successfully");
	}

}

