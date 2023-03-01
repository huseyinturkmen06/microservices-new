package com.programmingtech.orderservice.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    //web client isn't a part of spring mvc but the part of spring web-flux
    public WebClient webClient(){
        return WebClient.builder().build();
    }
    //bean methods must be overridable, so make it public



}


