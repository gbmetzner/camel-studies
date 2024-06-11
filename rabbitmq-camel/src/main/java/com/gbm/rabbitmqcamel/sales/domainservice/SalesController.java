package com.gbm.rabbitmqcamel.sales.domainservice;

import com.gbm.rabbitmqcamel.common.CustomerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Represents the endpoints for the Sales Domain Service
 */
@RestController
@RequestMapping("sales")
public class SalesController
{
    private static final Logger LOGGER =        LoggerFactory.getLogger(SalesController.class);

    @PostMapping("/customer")
    public void processCustomerEvent(        @RequestBody CustomerEvent customerEvent    ) {
		LOGGER.info( "Received customer event: {}", customerEvent );
    }

}
