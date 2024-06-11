package com.gbm.springbootcamel.model;

public record Customer(int id, String addressLine1, String addressLine2, String city, String state, String postalCode) {
}
