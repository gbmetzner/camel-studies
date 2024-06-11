package com.gbm.springbootcamel.routes;

import com.gbm.springbootcamel.common.InvalidCustomerAddressException;
import com.gbm.springbootcamel.processor.AddressUpdateLineToCustomerMapper;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFileOperationFailedException;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.management.PublishEventNotifier;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;

@Component
public class AddressUpdatesToCustomerServiceRoute extends RouteBuilder {
    public static String ROUTE_ID = "address-updates-to-customer-service";

    private final CsvDataFormat csvDataFormat;

    public AddressUpdatesToCustomerServiceRoute(@Qualifier("csvDataFormatAddressUpdate") CsvDataFormat csvDataFormat) {
        this.csvDataFormat = csvDataFormat;
    }

    @Override
    public void configure() {
        setupEventNotifier();

        errorHandler(defaultErrorHandler().log(log));

        setupExceptionFlow();

        setupRoute();
    }

    private void setupExceptionFlow() {
        onException(GenericFileOperationFailedException.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "File component failed due to error: ${exception.message}")
                .doTry()
                .process(exchange -> exchange.getContext().getRouteController().stopRoute("address-updates-to-customer-service-route"))
                .doCatch(Exception.class)
                .log(LoggingLevel.ERROR, "Could not stop route")
                .end();

        onException(InvalidCustomerAddressException.class)
                .handled(true)
                .log(LoggingLevel.ERROR,
                        "File had invalid row: ${exception.message}")
                .end();

        onException(HttpOperationFailedException.class, SocketTimeoutException.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "Failed to patch: ${exception.message}")
                .maximumRedeliveries(2)
                .redeliveryDelay(5000)
                .logExhausted(true)
                .logExhaustedMessageHistory(true)
                .logRetryAttempted(true)
                .end();
    }

    private void setupRoute() {
        from("file:{{app.directory}}?include={{app.includefile}}&move={{app.movedirectory}}&autoCreate=false&directoryMustExist=true&bridgeErrorHandler=true")
                .onCompletion()
                .log(LoggingLevel.DEBUG, "MyLog", "Finished processing file: ${{app.includefile}}")
                .end()
                .routeId(ROUTE_ID)
                .unmarshal(csvDataFormat)
                .split(body())
                .parallelProcessing()
                .bean(AddressUpdateLineToCustomerMapper.class, "validate")
                .bean(AddressUpdateLineToCustomerMapper.class, "process")
                .setProperty("customerId", simple("${body.id}"))
                .marshal()
                .json()
                .toD("rest:patch:customer/${exchangeProperty.customerId}?host={{app.customer-service.host}}");

        from("direct:event")
                .log(LoggingLevel.DEBUG, "MyLog", "EVENT: ${body}");
    }

    private void setupEventNotifier() {
        var notifier = new PublishEventNotifier();
        notifier.setCamelContext(getContext());
        notifier.setEndpointUri("direct:event");
        notifier.setIgnoreCamelContextEvents(true);
        getContext().getManagementStrategy().addEventNotifier(notifier);
    }
}