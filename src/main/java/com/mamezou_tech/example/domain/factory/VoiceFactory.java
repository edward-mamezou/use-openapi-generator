package com.mamezou_tech.example.domain.factory;

import com.mamezou_tech.example.domain.valueobject.HelloVoice;
import com.mamezou_tech.example.domain.valueobject.Person;

public interface VoiceFactory {

    HelloVoice sayHello(final Person person);
}
