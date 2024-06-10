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
	@Value("${app.rabbitmq.username}")
	private String rabbitMqUsername;

	@Value("${app.rabbitmq.password}")
	private String rabbitMqPassword;

	@Value("${app.rabbitmq.host}")
	private String rabbitMqHost;

	@Bean(name = "customerIntegrationServiceClient")
	public RestClient restClient(@Value("${app.customer-integration-service.host}")	String customerIntegrationServiceUrl) {
		return RestClient.create(customerIntegrationServiceUrl);
	}

	@Bean
	public ConnectionFactory rabbitConnectionFactory() throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
		var rabbitConnectionFactory = new ConnectionFactory();
		rabbitConnectionFactory.setUsername(rabbitMqUsername);
		rabbitConnectionFactory.setPassword(rabbitMqPassword);
		rabbitConnectionFactory.setUri(rabbitMqHost);
		return rabbitConnectionFactory;
	}

}
