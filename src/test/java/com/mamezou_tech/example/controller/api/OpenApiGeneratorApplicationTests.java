package com.mamezou_tech.example.controller.api;

import com.mamezou_tech.example.domain.domainevent.HelloEvent;
import com.mamezou_tech.example.domain.entity.HibernationPod;
import com.mamezou_tech.example.domain.repository.HelloEventRepository;
import com.mamezou_tech.example.domain.valueobject.EventId;
import com.mamezou_tech.example.domain.valueobject.HibernationPodId;
import com.mamezou_tech.example.domain.valueobject.Passenger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenApiGeneratorApplicationTests {

    private final String payload;

    @Autowired
    private HelloEventRepository repository;

    OpenApiGeneratorApplicationTests() {
        final String jsonPayload = "{\"iss\":\"http://localhost\",\"custom:firstname\":\"James\",\"aud\":\"APPCLIENTID\",\"exp\":1654758757,\"custom:type\":\"Human\"}";
        payload = Base64.getUrlEncoder().encodeToString(jsonPayload.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void contextLoads() {
    }

    @Test
    void publish() throws MalformedURLException {
        EventId eventId = new EventId(UUID.randomUUID().toString());
        HibernationPodId podId = new HibernationPodId("id-001");
        Passenger passenger = new Passenger("James");
        ZonedDateTime time = Instant.now().atZone(ZoneId.of("UTC"));
        URL url = new File("src/test/resources/james.wav").toURI().toURL();
        HelloEvent event = new HelloEvent(eventId, new HibernationPod(podId, passenger), url, time);
        repository.save(event);
    }
}
