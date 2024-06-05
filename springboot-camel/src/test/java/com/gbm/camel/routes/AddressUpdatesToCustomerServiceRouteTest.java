package com.gbm.camel.routes;

import com.gbm.camel.config.AppConfig;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ToDynamicDefinition;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpointsAndSkip;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static com.gbm.camel.routes.AddressUpdatesToCustomerServiceRoute.ROUTE_ID;
import static org.apache.camel.builder.AdviceWith.adviceWith;

@CamelSpringBootTest
@SpringBootApplication
@ContextConfiguration(classes = AppConfig.class)
@TestPropertySource(locations = "classpath:/application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpointsAndSkip("file:.*|rest:.*")
@UseAdviceWith
@SpringBootTest
public class AddressUpdatesToCustomerServiceRouteTest {

    @Autowired
    private CamelContext camelContext;
    @Autowired
    private ProducerTemplate producerTemplate;
    @Value("classpath:/data/customer-address-update-valid.csv")
    private Resource customerAddressResource;

    @Test
    public void testValidRoute() throws Exception {
        var file = new GenericFile<>();

        file.setBody(customerAddressResource.getFile());

        MockEndpoint restEndpoint = camelContext.getEndpoint("mock://rest:patch:customer", MockEndpoint.class);
        // Replaces ALL dynamic to definitions
        adviceWith(camelContext, ROUTE_ID, rb -> rb.weaveByType(ToDynamicDefinition.class)
                .replace()
                .toD("mock://rest:patch:customer"));
        // Replaces the from definition
        adviceWith(camelContext, ROUTE_ID, rb -> rb.replaceFromWith("direct:file:start"));
        // Once advice with is used, the camel context has to be started manually
        camelContext.start();
        restEndpoint.expectedMessageCount(1);
        producerTemplate.sendBody("direct:file:start", file);
        restEndpoint.assertIsSatisfied();
    }

}
