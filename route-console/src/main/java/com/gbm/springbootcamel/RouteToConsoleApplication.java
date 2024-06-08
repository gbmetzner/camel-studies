package com.gbm.springbootcamel;

import org.apache.camel.main.Main;

import java.util.logging.Logger;

public class RouteToConsoleApplication {
    static Logger LOG = Logger.getLogger(RouteToConsoleApplication.class.getName());

    public static void main(String[] args) {
        var main = new Main();
        try (var configure = main.configure()) {
            configure.addLambdaRouteBuilder(
                    rb -> rb.from("stream://in?promptMessage=What should I repeat?: ")
                            .process(exchange -> exchange.getIn().setBody("You said: " + exchange.getIn().getBody(String.class)))
                            .to("stream://out"));
            main.run(args);
        } catch (Exception ex) {
            LOG.severe(ex::getMessage);
        }

    }
}