package com.get.demo;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws Exception {
        // Initialize the Spring ApplicationContext
        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);

        // Retrieve the CamelContext bean from the Spring context
        CamelContext camelContext = context.getBean(CamelContext.class);

        // Explicitly start the Camel context
        camelContext.start();
        System.out.println("Camel context started...");
    }
}
