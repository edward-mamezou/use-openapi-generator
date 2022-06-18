package com.mamezou_tech.example.domain.aggregate;

import com.mamezou_tech.example.domain.domainevent.HelloEvent;
import com.mamezou_tech.example.domain.repository.HelloEventRepository;

public class HelloEvents {

    private final HelloEventRepository helloEventRepository;

    public HelloEvents(HelloEventRepository helloEventRepository) {
        this.helloEventRepository = helloEventRepository;
    }

    public void publishEvent(final HelloEvent helloEvent) {
        helloEventRepository.save(helloEvent);
    }
}
