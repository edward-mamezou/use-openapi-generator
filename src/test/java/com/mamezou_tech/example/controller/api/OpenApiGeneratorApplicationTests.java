package com.mamezou_tech.example.controller.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenApiGeneratorApplicationTests {

    private final String payload;

    OpenApiGeneratorApplicationTests() {
        final String jsonPayload = "{\"iss\":\"http://localhost\",\"custom:firstname\":\"James\",\"aud\":\"APPCLIENTID\",\"exp\":1654758757,\"custom:type\":\"Human\"}";
        payload = Base64.getUrlEncoder().encodeToString(jsonPayload.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void contextLoads() {
    }

    @Test
    void sayHello(@Autowired TestRestTemplate restTemplate) {
        RequestEntity<?> request = RequestEntity.get("/example/hello").header("payload", payload).build();

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }
}
