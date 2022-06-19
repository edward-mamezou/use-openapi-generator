package com.mamezou_tech.example.controller.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import software.amazon.awssdk.services.polly.PollyClient;

import java.util.UUID;

@Configuration
@ComponentScan(basePackages = {"com.mamezou_tech.example.application",
    "com.mamezou_tech.example.infrastructure"})
public class HelloConfiguration {

    @Value("${openapi.exampleService.paho.broker-url:tcp://localhost:1883}")
    private String url;

    @Value("${openapi.exampleService.paho.defaultTopic:hibernation-pod/hello}")
    private String defaultTopic;

    @Bean
    public PollyClient getPollyClient() {
        return PollyClient.builder().build();
    }

    @Bean
    public IntegrationFlow getHibernationPodHelloFlow() {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(url, UUID.randomUUID().toString());
        handler.setDefaultTopic(defaultTopic);
        return flow -> flow.handle(handler);
    }
}
