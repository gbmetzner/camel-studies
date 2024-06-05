package com.gbm.camel.routes.processor;

import com.gbm.camel.processor.AddressUpdateLineToCustomerMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the mapper class.
 */
public class AddressUpdateLineToCustomerMapperTest {

    private static final List<String> fixtureAddressRow =
            Arrays.asList("1", "1060 W. Addison St.",
                    "Suite 100", "Chicago", "IL", "60605");

    @Test
    void estSuccessfulMapping() {
        var customer = new AddressUpdateLineToCustomerMapper().process(fixtureAddressRow);
        assertEquals(1, customer.id());
        assertEquals("1060 W. Addison St.", customer.addressLine1());
        assertEquals("Suite 100", customer.addressLine2());
        assertEquals("Chicago", customer.city());
        assertEquals("IL", customer.state());
        assertEquals("60605", customer.postalCode());
    }
}
