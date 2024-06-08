package com.gbm.rabbitmqcamel.customer.domainservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

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
		LOGGER.debug("Request to create customer");
		sendEvent( "create");
	}

	@PutMapping
	public void updateCustomer() {
		LOGGER.debug("Request to update customer");
		sendEvent( "update");
	}

	@DeleteMapping
	public void deleteCustomer() {
		LOGGER.debug("Request to delete customer");
		sendEvent( "delete");
	}

	private void sendEvent( String eventType) {
		LOGGER.debug( "Sending event with type: {}", eventType );

		customerIntegrationServiceClient
				.post()
				.contentType( MediaType.APPLICATION_JSON )
				.body(new CustomerEvent( 1, eventType))
				.retrieve();

		LOGGER.debug("Event sent successfully");
	}

}

