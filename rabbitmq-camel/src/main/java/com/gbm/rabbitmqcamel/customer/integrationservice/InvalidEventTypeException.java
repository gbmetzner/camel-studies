package com.gbm.rabbitmqcamel.customer.integrationservice;

public class InvalidEventTypeException extends RuntimeException {

    public InvalidEventTypeException(String message) {
        super(message);
    }

}
