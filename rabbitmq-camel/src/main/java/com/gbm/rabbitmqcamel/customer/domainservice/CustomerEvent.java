package com.gbm.rabbitmqcamel.customer.domainservice;

public record CustomerEvent(Integer customerId, String eventType)
{
}
