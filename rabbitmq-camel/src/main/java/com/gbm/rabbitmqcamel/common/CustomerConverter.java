package com.gbm.rabbitmqcamel.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Converter;
import org.apache.camel.TypeConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomerConverter implements TypeConverters {

    private final ObjectMapper mapper;

    @Autowired
    public CustomerConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Converter
    public byte[] myPackageToByteArray(CustomerEvent source) {
        try {
            return mapper.writeValueAsBytes(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Converter
    public CustomerEvent byteArrayToMyPackage(byte[] source) {
        try {
            return mapper.readValue(source, CustomerEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
