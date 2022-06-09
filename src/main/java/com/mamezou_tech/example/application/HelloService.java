package com.mamezou_tech.example.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mamezou_tech.example.domain.factory.VoiceFactory;
import com.mamezou_tech.example.domain.valueobject.HelloVoice;
import com.mamezou_tech.example.domain.valueobject.Person;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public class HelloService {

    private final VoiceFactory voiceFactory;

    public HelloService(VoiceFactory voiceFactory) {
        this.voiceFactory = voiceFactory;
    }

    public Optional<HelloVoice> sayHello(final String jwtPayload) {
        Optional<String> maybePayload = Optional.ofNullable(jwtPayload);
        Optional<Person> maybePerson = maybePayload.map(this::parseRequest);
        return maybePerson.flatMap(person -> Optional.ofNullable(voiceFactory.sayHello(person)));
    }

    Person parseRequest(final String payload) {
        byte[] decodedPayload = Base64.getUrlDecoder().decode(payload);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<?, ?> mappedPayload = mapper.readValue(decodedPayload, Map.class);
            if (mappedPayload.get("custom:firstname") instanceof String firstName) {
                return new Person(firstName);
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}
