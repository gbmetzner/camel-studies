package com.gbm.camel.processor;

import com.gbm.camel.model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper class from a row of customer data into a Customer data object. The
 * Camel route will match the list type from the exchange and use it as the
 * input to the process method. The return value from the process method
 * then becomes the new body of the exchange.
 */
@Component
public class AddressUpdateLineToCustomerMapper
{

	public Customer process( List<String> addressRow )
	{
		return new Customer( Integer.parseInt( addressRow.get( 0 ) ), addressRow.get( 1 ), addressRow.get( 2 ), addressRow.get( 3 ),
				addressRow.get( 4 ), addressRow.get( 5 ) );
	}

}
