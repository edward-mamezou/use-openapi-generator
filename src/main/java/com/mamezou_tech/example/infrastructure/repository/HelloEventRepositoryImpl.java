package com.mamezou_tech.example.infrastructure.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mamezou_tech.example.domain.domainevent.HelloEvent;
import com.mamezou_tech.example.domain.repository.HelloEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class HelloEventRepositoryImpl implements HelloEventRepository {

    private final Logger logger = LoggerFactory.getLogger(HelloEventRepositoryImpl.class);

    private final IntegrationFlow mqttOutbound;

    private final ObjectMapper objectMapper;

    public HelloEventRepositoryImpl(@Autowired IntegrationFlow mqttOutbound) {
        this.mqttOutbound = mqttOutbound;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void save(HelloEvent helloEvent) {
        Map<String, String> json = new HashMap<>();
        json.put("eventId", helloEvent.eventId().eventId());
        json.put("podId", helloEvent.hibernationPod().podId().podId());
        json.put("helloVoice", helloEvent.helloVoice().toExternalForm());
        json.put("time", DateTimeFormatter.ISO_DATE_TIME.format(helloEvent.time()));
        try {
            String event = objectMapper.writeValueAsString(json);
            Message<String> message = MessageBuilder.withPayload(event).build();
            mqttOutbound.getInputChannel().send(message);
        } catch (JsonProcessingException e) {
            logger.error("error", e);
        }
    }
}
