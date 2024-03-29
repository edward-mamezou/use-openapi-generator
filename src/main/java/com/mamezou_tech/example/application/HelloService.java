package com.mamezou_tech.example.application;

import com.mamezou_tech.example.domain.aggregate.HelloEvents;
import com.mamezou_tech.example.domain.aggregate.Passengers;
import com.mamezou_tech.example.domain.domainevent.HelloEvent;
import com.mamezou_tech.example.domain.factory.HelloEventFactory;
import com.mamezou_tech.example.domain.repository.HelloEventRepository;

public class HelloService {

    private final Passengers passengers;

    public HelloService(HelloEventFactory helloEventFactory, HelloEventRepository helloEventRepository) {
        HelloEvents helloEvents = new HelloEvents(helloEventFactory, helloEventRepository);
        this.passengers = new Passengers(helloEvents);
    }

    public String sayHello(final String podId, final String firstName) {
        HelloEvent helloEvent = passengers.sayHello(podId, firstName);
        return helloEvent.eventId().eventId();
    }
}
