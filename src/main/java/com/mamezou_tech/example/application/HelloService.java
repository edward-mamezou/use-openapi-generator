package com.mamezou_tech.example.application;

import com.mamezou_tech.example.domain.aggregate.HelloEvents;
import com.mamezou_tech.example.domain.aggregate.HibernationPods;
import com.mamezou_tech.example.domain.factory.HelloEventFactory;
import com.mamezou_tech.example.domain.repository.HelloEventRepository;
import com.mamezou_tech.example.domain.valueobject.HibernationPodId;
import com.mamezou_tech.example.domain.valueobject.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelloService {

    private final HibernationPods hibernationPods;

    public HelloService(@Autowired HelloEventFactory helloEventFactory, HelloEventRepository helloEventRepository) {
        HelloEvents helloEvents = new HelloEvents(helloEventFactory, helloEventRepository);
        this.hibernationPods = new HibernationPods(helloEvents);
    }

    public String sayHello(final String podId, final String firstName) {
        HibernationPodId hibernationPodId = new HibernationPodId(podId);
        Passenger passenger = new Passenger(firstName);
        return hibernationPods.sayHello(hibernationPodId, passenger);
    }
}
