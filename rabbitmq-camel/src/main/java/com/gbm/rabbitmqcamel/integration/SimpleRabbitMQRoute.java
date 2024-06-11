package com.gbm.rabbitmqcamel.integration;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleRabbitMQRoute extends RouteBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger( SimpleRabbitMQRoute.class);

    @Override
    public void configure() {
        errorHandler(defaultErrorHandler().log( LOGGER ));

        from("direct:simpleStart")
            .to("rabbitmq:simple" +
                "?connectionFactory=#rabbitConnectionFactory" +
                "&autoDelete=false" +
                "&bridgeErrorHandler=true" +
                "&declare=false" +
                "&exchangeType=topic"
            );

        from("rabbitmq:simple" +
            "?connectionFactory=#rabbitConnectionFactory" +
            "&autoDelete=false" +
            "&bridgeErrorHandler=true" +
            "&declare=false" +
            "&exchangeType=topic" +
            "&passive=true" +
            "&queue=simple_a"
        )            .log(LoggingLevel.ERROR, "Queue A: ${body}")
            .to("rest:post:simple?host={{app.simple-service.host}}");

        from("rabbitmq:simple" +
            "?connectionFactory=#rabbitConnectionFactory" +
            "&autoDelete=false" +
            "&bridgeErrorHandler=true" +
            "&declare=false" +
            "&exchangeType=topic" +
            "&passive=true" +
            "&queue=simple_b"
        )            .log(LoggingLevel.ERROR, "Queue B: ${body}")
            .to("rest:post:simple?host={{app.simple-service.host}}");

        from("rabbitmq:simple_nomatch" +
            "?connectionFactory=#rabbitConnectionFactory" +
            "&autoDelete=false" +
            "&bridgeErrorHandler=true" +
            "&declare=false" +
            "&exchangeType=fanout" +
            "&passive=true" +
            "&queue=simple_nomatch"
        )            .log(LoggingLevel.ERROR, "Queue No Match: ${body}")
            .to("rest:post:simple?host={{app.simple-service.host}}");

    }
}
