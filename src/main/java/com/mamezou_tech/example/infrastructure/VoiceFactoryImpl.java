package com.mamezou_tech.example.infrastructure;

import com.mamezou_tech.example.domain.factory.VoiceFactory;
import com.mamezou_tech.example.domain.valueobject.HelloVoice;
import com.mamezou_tech.example.domain.valueobject.Person;

public class VoiceFactoryImpl implements VoiceFactory {

    @Override
    public HelloVoice sayHello(Person person) {
        String message = String.format("Good Morning, %s!", person.firstName());
        return new HelloVoice(message);
    }
}
