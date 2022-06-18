package com.mamezou_tech.example.domain.aggregate;

import com.mamezou_tech.example.domain.domainevent.HelloEvent;
import com.mamezou_tech.example.domain.entity.HibernationPod;
import com.mamezou_tech.example.domain.factory.HelloEventFactory;
import com.mamezou_tech.example.domain.valueobject.HibernationPodId;
import com.mamezou_tech.example.domain.valueobject.Passenger;

public class HibernationPods {

    private final HelloEventFactory helloEventFactory;

    private final HelloEvents helloEvents;

    public HibernationPods(final HelloEventFactory helloEventFactory, final HelloEvents helloEvents) {
        this.helloEventFactory = helloEventFactory;
        this.helloEvents = helloEvents;
    }

    public String sayHello(final HibernationPodId hibernationPodId, final Passenger passenger) {
        HibernationPod hibernationPod = new HibernationPod(hibernationPodId, passenger);
        HelloEvent helloEvent = helloEventFactory.create(hibernationPod);
        helloEvents.publishEvent(helloEvent);
        return helloEvent.helloVoice().toExternalForm();
    }
}
