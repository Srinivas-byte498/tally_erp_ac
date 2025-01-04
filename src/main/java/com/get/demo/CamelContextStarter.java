package com.get.demo;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CamelContextStarter {

    @Autowired
    private CamelContext camelContext;

    @PostConstruct
    public void startCamelContext() throws Exception {
        camelContext.start();
        System.out.println("Camel context started...");
    }
}
