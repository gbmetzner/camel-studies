package com.gbm.rabbitmqcamel.sales.integrationservice;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.apache.camel.component.springrabbit.SpringRabbitMQConstants.ROUTING_KEY;

/**
 * Route that consumes customer events for the Sales domain
 */
@Component
public class SalesCustomerEventConsumerRoute extends RouteBuilder {

    private static final Logger log =
            LoggerFactory.getLogger(SalesCustomerEventConsumerRoute.class);

    @Override
    public void configure() throws Exception {
        errorHandler(deadLetterChannel("log:com.gbm.rabbitmqcamel.integrationservice?level=ERROR"));

        from("spring-rabbitmq:customer" +
                "?bridgeErrorHandler=true" +
                "&exchangeType=topic" +
                "&arg.queue.durable=true" +
                "&queues=sales_customer"
        )
                .choice()
                .when(header(ROUTING_KEY).isEqualToIgnoreCase("customer.create"))
                .to("direct:postToSalesEndpoint")
                .when(header(ROUTING_KEY).isEqualToIgnoreCase("customer.update"))
                .to("direct:postToSalesEndpoint")
                .otherwise()
                .stop()
                .end();

        from("direct:postToSalesEndpoint")
                .to("rest:post:sales/customer?host={{app.base.host}}");
    }
}
