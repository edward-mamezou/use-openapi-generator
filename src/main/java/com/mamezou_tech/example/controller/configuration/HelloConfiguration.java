package com.mamezou_tech.example.controller.configuration;

import com.mamezou_tech.example.application.HelloService;
import com.mamezou_tech.example.domain.factory.HelloEventFactory;
import com.mamezou_tech.example.domain.repository.HelloEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import software.amazon.awssdk.services.polly.PollyClient;

import java.util.UUID;

@Configuration
@ComponentScan(basePackages = {"com.mamezou_tech.example.infrastructure"})
@ConfigurationPropertiesScan(basePackages = {"com.mamezou_tech.example.controller.configuration",
    "com.mamezou_tech.example.controller.api"})
public class HelloConfiguration {

    @Value("${openapi.exampleService.paho.broker-url:tcp://localhost:1883}")
    private String brokerUrl;

    @Value("${openapi.exampleService.paho.defaultTopic:hibernation-pod/hello}")
    private String defaultTopic;

    @Bean
    public IntegrationFlow getHibernationPodHelloFlow() {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(brokerUrl, UUID.randomUUID().toString());
        handler.setDefaultTopic(defaultTopic);
        return flow -> flow.handle(handler);
    }

    @Bean
    public PollyClient getPollyClient() {
        return PollyClient.builder().build();
    }

    @Bean
    public HelloService getHelloService(@Autowired HelloEventFactory helloEventFactory, HelloEventRepository helloEventRepository) {
        return new HelloService(helloEventFactory, helloEventRepository);
    }
}
