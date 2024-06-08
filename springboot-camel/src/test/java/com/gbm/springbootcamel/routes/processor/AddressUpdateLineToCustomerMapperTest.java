package com.gbm.springbootcamel.routes.processor;

import com.gbm.springbootcamel.common.InvalidCustomerAddressException;
import com.gbm.springbootcamel.processor.AddressUpdateLineToCustomerMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddressUpdateLineToCustomerMapperTest {

    private static final List<String> fixtureAddressRow =
            asList("1", "1060 W. Addison St.",
                    "Suite 100", "Chicago", "IL", "60605");

    @Test
    void testSuccessfulMapping() {
        var customer = new AddressUpdateLineToCustomerMapper().process(fixtureAddressRow);
        assertEquals(1, customer.id());
        assertEquals("1060 W. Addison St.", customer.addressLine1());
        assertEquals("Suite 100", customer.addressLine2());
        assertEquals("Chicago", customer.city());
        assertEquals("IL", customer.state());
        assertEquals("60605", customer.postalCode());
    }

    @Test
    void testSuccessfulValidation() throws InvalidCustomerAddressException
	{
        new AddressUpdateLineToCustomerMapper().validate(fixtureAddressRow);
    }

    @Test
    void testValidationFailedScenarios()  {

        var mapper = new AddressUpdateLineToCustomerMapper();
        
        assertThrows(InvalidCustomerAddressException.class, () -> mapper.validate(singletonList( fixtureAddressRow.get( 0 ) )) );

        assertThrows(InvalidCustomerAddressException.class, () -> mapper.validate(asList(null, "A1", "A2", "C", "S", "P")) );

        assertThrows(InvalidCustomerAddressException.class, () -> mapper.validate( asList("INVALID", "A1", "A2", "C", "S", "P")) );

        assertThrows(InvalidCustomerAddressException.class, () -> mapper.validate( asList("1", null, "A2", "C", "S", "P")) );

        assertThrows(InvalidCustomerAddressException.class, () -> mapper.validate( asList("1", "A1", "A2", null, "S", "P")) );

        assertThrows(InvalidCustomerAddressException.class, () -> mapper.validate( asList("1", "A1", "A2", "C", null, "P")) );

        assertThrows(InvalidCustomerAddressException.class, () -> mapper.validate( asList("1", "A1", "A2", "C", "S", null)) );
    }
}
