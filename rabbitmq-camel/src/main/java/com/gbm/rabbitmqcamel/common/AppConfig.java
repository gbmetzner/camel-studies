package com.gbm.rabbitmqcamel.common;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class AppConfig
{

	@Value("${app.rabbitmq.uri}")
	private String rabbitMqURI;

	@Bean(name = "customerIntegrationServiceClient")
	public RestClient customerRestClient(@Value("${app.customer-integration-service.host}")	String customerIntegrationServiceUrl) {
		return RestClient.create(customerIntegrationServiceUrl);
	}

	@Bean(name = "itineraryIntegrationServiceClient")
	public RestClient itineraryRestClient(@Value("${app.customer-integration-service.host}")	String customerIntegrationServiceUrl) {
		return RestClient.create(customerIntegrationServiceUrl);
	}

	@Bean
	public ConnectionFactory rabbitConnectionFactory() throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
		var rabbitConnectionFactory = new ConnectionFactory();
		rabbitConnectionFactory.setUri( rabbitMqURI );
		return rabbitConnectionFactory;
	}

}
