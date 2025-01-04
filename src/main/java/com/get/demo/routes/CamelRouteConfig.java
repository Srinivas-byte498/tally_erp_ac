package com.get.demo.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelRouteConfig extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:fetchCompanies?period=5000")
            .log("Timer triggered: Fetching companies...");
    }
}
