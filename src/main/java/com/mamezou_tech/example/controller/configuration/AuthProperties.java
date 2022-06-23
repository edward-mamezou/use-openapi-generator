package com.mamezou_tech.example.controller.configuration;

import com.mamezou_tech.example.application.AuthService;
import com.mamezou_tech.example.infrastructure.oidc.CodeFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;

import java.net.URISyntaxException;

@ConstructorBinding
@ConfigurationProperties(prefix = "auth")
public record AuthProperties(
     String authorizationEndpoint,
     String tokenEndpoint,
     String clientId,
     String clientSecret,
     String callback) {

    @Bean
    public CodeFlow getCodeFlow() throws URISyntaxException {
        return new CodeFlow(tokenEndpoint, clientId, clientSecret);
    }

    @Bean
    public AuthService getAuthService(@Autowired CodeFlow codeFlow) throws URISyntaxException {
        return new AuthService(codeFlow);
    }
}
