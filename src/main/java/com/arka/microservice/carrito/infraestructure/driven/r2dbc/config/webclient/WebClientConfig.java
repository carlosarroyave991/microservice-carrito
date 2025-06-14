package com.arka.microservice.carrito.infraestructure.driven.r2dbc.config.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${products.service.url}")
    private String productServiceUrl;

    private final SendTokenWebClient sendTokenWebClient;

    public WebClientConfig(SendTokenWebClient sendTokenWebClient) {
        this.sendTokenWebClient = sendTokenWebClient;
    }

    @Bean
    public WebClient productWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(productServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                //.filter(logRequest())
                .filter(sendTokenWebClient.authHeaderFilter())
                .build();
    }
}