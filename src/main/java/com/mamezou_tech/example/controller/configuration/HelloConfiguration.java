package com.mamezou_tech.example.controller.configuration;

import com.mamezou_tech.example.application.HelloService;
import com.mamezou_tech.example.domain.factory.VoiceFactory;
import com.mamezou_tech.example.infrastructure.VoiceFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloConfiguration {

    private final VoiceFactory voiceFactory = new VoiceFactoryImpl();

    @Bean
    public VoiceFactory getVoiceFactory() {
        return voiceFactory;
    }

    @Bean
    public HelloService getHello() {
        return new HelloService(voiceFactory);
    }

}
