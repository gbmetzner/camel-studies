package com.gbm.rabbitmqcamel.itinerary.integrationservice;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.component.springrabbit.SpringRabbitMQConstants.ROUTING_KEY;

@Component
public class ItineraryCustomerEventConsumerRoute extends RouteBuilder {
    @Override
    public void configure() {

        errorHandler(deadLetterChannel("log:com.gbm.rabbitmqcamel.itinerary?level=ERROR"));

        from("spring-rabbitmq:customer" +
                "?bridgeErrorHandler=true" +
                "&exchangeType=topic" +
                "&arg.queue.durable=true" +
                "&queues=itinerary_customer"
        )
                .choice()
                .when(header(ROUTING_KEY).isEqualToIgnoreCase("customer.delete"))
                .to("direct:postToItineraryEndpoint")
                .otherwise()
                .stop()
                .end();

        from("direct:postToItineraryEndpoint")
                .to("rest:post:itinerary/customer?host={{app.base.host}}");
    }
}
