package com.mamezou_tech.example.infrastructure.factory;

import com.mamezou_tech.example.controller.configuration.HelloConfiguration;
import com.mamezou_tech.example.domain.domainevent.HelloEvent;
import com.mamezou_tech.example.domain.entity.HibernationPod;
import com.mamezou_tech.example.domain.valueobject.HibernationPodId;
import com.mamezou_tech.example.domain.valueobject.Passenger;
import com.mamezou_tech.example.infrastructure.aws.Polly;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class HelloEventFactoryTest {

    @Test
    public void create() {
        HelloConfiguration config = new HelloConfiguration();
        HelloEventFactoryImpl factory = new HelloEventFactoryImpl(new Polly(new HelloConfiguration().getPollyClient()));
        HibernationPodId podId = new HibernationPodId(UUID.randomUUID().toString());
        Passenger passenger = new Passenger("James");
        HibernationPod pod = new HibernationPod(podId, passenger);
        HelloEvent event = factory.create(pod);
        String url = event.helloVoice().toString();
        Assertions.assertNotNull(event);
    }
}
