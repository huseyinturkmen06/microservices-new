package com.programmingtech.orderservice.Config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    //web client isn't a part of spring mvc but the part of spring web-flux
    //it can be confusing to make a request to inventory-serveie which is running
    //on different ports simultaneously, so we have to confuge it by loadbalancing
    @LoadBalanced
    //it will aoutomatically create an instance of the order service
    //which named "client side balancing"
    public WebClient.Builder webClient(){
        return WebClient.builder();
    }
    //bean methods must be overridable, so make it public



}


