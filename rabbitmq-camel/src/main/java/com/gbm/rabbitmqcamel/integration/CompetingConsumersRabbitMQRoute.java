package com.gbm.rabbitmqcamel.integration;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CompetingConsumersRabbitMQRoute extends RouteBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(            CompetingConsumersRabbitMQRoute.class);

    @Override
    public void configure()  {

        errorHandler(defaultErrorHandler().log( LOGGER ));

        from("direct:produceEvent")
            .to("rabbitmq:competing.consumer" +
                "?connectionFactory=#rabbitConnectionFactory" +
                "&autoDelete=false" +
                "&bridgeErrorHandler=true" +
                "&declare=false" +
                "&exchangeType=direct"
            );

        from("rabbitmq:competing.consumer" +
            "?connectionFactory=#rabbitConnectionFactory" +
            "&autoDelete=false" +
            "&bridgeErrorHandler=true" +
            "&declare=false" +
            "&exchangeType=direct" +
            "&passive=true" +
            "&queue=competing.consumer" +
            "&autoAck=true" +
            "&concurrentConsumers=3"
        ).log(LoggingLevel.ERROR, "Processed: ${body}").to("rest:post:simple?host={{app.simple-service.host}}");

    }
}
