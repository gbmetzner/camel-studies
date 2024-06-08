package com.gbm.rabbitmqcamel.customer.domainservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig
{
	@Bean(name = "customerIntegrationServiceClient")
	public RestClient restClient(@Value("${app.customer-integration-service.host}")	String customerIntegrationServiceUrl) {
		return RestClient.create(customerIntegrationServiceUrl);
	}

}
