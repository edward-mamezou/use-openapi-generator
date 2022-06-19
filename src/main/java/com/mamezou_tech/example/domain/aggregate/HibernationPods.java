package com.mamezou_tech.example.domain.aggregate;

import com.mamezou_tech.example.domain.domainevent.HelloEvent;
import com.mamezou_tech.example.domain.entity.HibernationPod;
import com.mamezou_tech.example.domain.valueobject.HibernationPodId;
import com.mamezou_tech.example.domain.valueobject.Passenger;

public class HibernationPods {

    private final HelloEvents helloEvents;

    public HibernationPods(final HelloEvents helloEvents) {
        this.helloEvents = helloEvents;
    }

    public HelloEvent sayHello(final String podId, final String firstName) {
        HibernationPodId hibernationPodId = new HibernationPodId(podId);
        Passenger passenger = new Passenger(firstName);
        HibernationPod hibernationPod = new HibernationPod(hibernationPodId, passenger);
        HelloEvent helloEvent = helloEvents.publishEvent(hibernationPod);
        return helloEvent;
    }
}
