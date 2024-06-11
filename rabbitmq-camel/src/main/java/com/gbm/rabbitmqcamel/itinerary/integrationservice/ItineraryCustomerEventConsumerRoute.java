package com.gbm.rabbitmqcamel.itinerary.integrationservice;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.springframework.stereotype.Component;

@Component
public class ItineraryCustomerEventConsumerRoute extends RouteBuilder
{
	@Override
	public void configure()
	{
		errorHandler(				deadLetterChannel(						"log:com.gbm.rabbitmqcamel.itinerary?level=ERROR"));

		from("rabbitmq:customer" +
				"?connectionFactory=#rabbitConnectionFactory" +
				"&autoDelete=false" +
				"&bridgeErrorHandler=true" +
				"&declare=false" +
				"&exchangeType=topic" +
				"&passive=true" +
				"&queue=itinerary_customer"
		)
				.choice()
				.when(header( RabbitMQConstants.ROUTING_KEY).isEqualToIgnoreCase("customer.delete"))
				.to("direct:postToItineraryEndpoint")
				.otherwise()
				.stop();

		from("direct:postToItineraryEndpoint")
				.to("rest:post:itinerary/customer?host={{app.base.host}}");
	}
}
