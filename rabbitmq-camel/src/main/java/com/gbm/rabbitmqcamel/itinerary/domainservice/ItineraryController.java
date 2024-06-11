package com.gbm.rabbitmqcamel.itinerary.domainservice;

import com.gbm.rabbitmqcamel.common.CustomerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("itinerary")
public class ItineraryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItineraryController.class);

    @PostMapping("/customer")
    public void processCustomerEvent(@RequestBody CustomerEvent customerEvent) {
        LOGGER.info("Received customer event: {}", customerEvent);
    }

}
