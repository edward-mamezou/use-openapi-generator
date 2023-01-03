package com.mamezou_tech.example.domain.aggregate;

import com.mamezou_tech.example.domain.domainevent.HelloEvent;
import com.mamezou_tech.example.domain.entity.HibernationPod;
import com.mamezou_tech.example.domain.factory.HelloEventFactory;
import com.mamezou_tech.example.domain.repository.HelloEventRepository;

public class HelloEvents {

    private final HelloEventFactory helloEventFactory;

    private final HelloEventRepository helloEventRepository;

    public HelloEvents(HelloEventFactory helloEventFactory, HelloEventRepository helloEventRepository) {
        this.helloEventFactory = helloEventFactory;
        this.helloEventRepository = helloEventRepository;
    }

    public HelloEvent publishEvent(final HibernationPod hibernationPod) {
        HelloEvent helloEvent = helloEventFactory.create(hibernationPod);
        helloEventRepository.save(helloEvent);
        return helloEvent;
    }
}
