package com.gbm.rabbitmqcamel.common;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class AppConfig {


    @Bean(name = "customerIntegrationServiceClient")
    public RestClient customerRestClient(@Value("${app.customer-integration-service.host}") String customerIntegrationServiceUrl) {
        return RestClient.create(customerIntegrationServiceUrl);
    }

    @Bean
    public CachingConnectionFactory rabbitConnectionFactory(@Value("${app.rabbitmq.uri}") String rabbitMqURI) {
        var rabbitConnectionFactory = new CachingConnectionFactory();
        rabbitConnectionFactory.setUri(rabbitMqURI);
        return rabbitConnectionFactory;
    }

}
