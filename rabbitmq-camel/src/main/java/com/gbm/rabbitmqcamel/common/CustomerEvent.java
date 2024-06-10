package com.gbm.rabbitmqcamel.common;

public record CustomerEvent(Integer customerId, String eventType)
{
}
