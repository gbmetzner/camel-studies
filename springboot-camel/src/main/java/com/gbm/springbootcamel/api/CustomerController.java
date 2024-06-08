package com.gbm.springbootcamel.api;

import com.gbm.springbootcamel.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @PatchMapping(path = "/customer/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") int id, @RequestBody Customer customer) {
        log.debug("Received customer request patch: id{}, customer{}", id, customer);
        return ResponseEntity.ok(customer);
    }

}
