package com.mamezou_tech.example.domain.repository;

import com.mamezou_tech.example.domain.domainevent.HelloEvent;

public interface HelloEventRepository {

    void save(HelloEvent helloEvent);
}
