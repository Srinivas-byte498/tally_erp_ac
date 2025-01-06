package com.get.erpintegration.config;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {

    @Bean
    public CamelContext camelContext() {
        return new DefaultCamelContext(); // Providing CamelContext as a bean
    }

    @Bean
    public CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                System.out.println("CamelContext is about to start.");
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                System.out.println("CamelContext has started.");
            }
        };
    }
}
