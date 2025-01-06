package com.get.erpintegration;
import org.apache.camel.CamelContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ERPIntegrationApplication {

    public static void main(String[] args) throws Exception {
        // Initialize the Spring ApplicationContext
        ApplicationContext context = SpringApplication.run(ERPIntegrationApplication.class, args);

        // Retrieve the CamelContext bean from the Spring context
        CamelContext camelContext = context.getBean(CamelContext.class);

        // Explicitly start the Camel context
        camelContext.start();
        System.out.println("Camel context started...");
    }
}
