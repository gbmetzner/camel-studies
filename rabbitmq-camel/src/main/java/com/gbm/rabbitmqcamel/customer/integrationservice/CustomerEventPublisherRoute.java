package com.gbm.rabbitmqcamel.customer.integrationservice;

import com.fasterxml.jackson.core.JsonParseException;
import com.gbm.rabbitmqcamel.common.CustomerEvent;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class CustomerEventPublisherRoute extends RouteBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerEventPublisherRoute.class);

    private final String HOST_FULL_URL;

    public CustomerEventPublisherRoute(@Value("${app.customer-integration-service.host}") String hostFullUrl) {
        this.HOST_FULL_URL = hostFullUrl;
    }

    @Override
    public void configure() throws Exception {
        var url = new URL(HOST_FULL_URL);

        restConfiguration()
                .host(url.getHost())
                .port(url.getPort())
                .bindingMode(RestBindingMode.json);

        errorHandler(defaultErrorHandler().log(log));

        onException(InvalidEventTypeException.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "An invalid event type was sent")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody().constant("Invalid event type sent");

        onException(JsonParseException.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "An exception occurred parsing the request body")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody().constant("Json pare exception was thrown");

        rest("/customer-integration")
                .post("/event")
                .type(CustomerEvent.class)
                .consumes(APPLICATION_JSON_VALUE)
                .to("direct:sendEventToRabbitMQ")
                .responseMessage(200,  "Request processed successfully");

        from("direct:sendEventToRabbitMQ")
                .routeId("send-to-rabbitmq")
                .choice()
                    .when().simple("${body.eventType} =~ 'create'")
                        .setProperty("routingKey", constant("customer.create"))
                    .when().simple("${body.eventType} =~ 'update'")
                        .setProperty("routingKey", constant("customer.update"))
                    .when().simple("${body.eventType} =~ 'delete'")
                        .setProperty("routingKey", constant("customer.delete"))
                    .otherwise()
                        .throwException(new InvalidEventTypeException("Event type is invalid"))
                .setHeader(RabbitMQConstants.ROUTING_KEY, exchangeProperty("routingKey"))
                .marshal().json()
                .to("rabbitmq:customer" +
                        "?connectionFactory=#rabbitConnectionFactory" +
                        "&autoDelete=false" +
                        "&bridgeErrorHandler=true" +
                        "&declare=false" +
                        "&exchangePattern=InOnly" +
                        "&exchangeType=topic"
                );

    }
}
