package com.programmingtechie.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
//our api or module can seem by eureka server (port 8761) thanks to this anotation
public class ApıGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApıGatewayApplication.class,args);
    }
}
