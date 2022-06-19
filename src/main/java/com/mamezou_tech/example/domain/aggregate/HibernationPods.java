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

    public String sayHello(final HibernationPodId hibernationPodId, final Passenger passenger) {
        HibernationPod hibernationPod = new HibernationPod(hibernationPodId, passenger);
        HelloEvent helloEvent = helloEvents.publishEvent(hibernationPod);
        return helloEvent.helloVoice().toExternalForm();
    }
}
