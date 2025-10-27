package com.jojoldu.book.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class RosBridgeConfig {

    @Bean
    public RosBridgeClient rosBridgeClient() throws URISyntaxException {
        RosBridgeClient client = new RosBridgeClient(new URI("ws://192.168.0.100:9090"));
        client.connect();
        return client;
    }
}
