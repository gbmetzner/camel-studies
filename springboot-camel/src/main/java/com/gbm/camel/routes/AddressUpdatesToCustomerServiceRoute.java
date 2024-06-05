package com.gbm.camel.routes;

import com.gbm.camel.processor.AddressUpdateLineToCustomerMapper;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AddressUpdatesToCustomerServiceRoute extends RouteBuilder
{
	public static String ROUTE_ID = "address-updates-to-customer-service";

	private final CsvDataFormat csvDataFormat;

	public AddressUpdatesToCustomerServiceRoute( @Qualifier( "csvDataFormatAddressUpdate" ) CsvDataFormat csvDataFormat )
	{
		this.csvDataFormat = csvDataFormat;
	}

	@Override
	public void configure()
	{
		from( "file" )
			.routeId( ROUTE_ID )
			.unmarshal( csvDataFormat )
			.split( body())
				.bean( AddressUpdateLineToCustomerMapper.class, "process" )
				.marshal()
				.json( true )
				.to( "rest:patch:customer?host=localhost" );
	}
}
