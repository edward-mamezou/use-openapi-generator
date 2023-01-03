package com.mamezou_tech.example.domain.factory;

import com.mamezou_tech.example.domain.domainevent.HelloEvent;
import com.mamezou_tech.example.domain.entity.HibernationPod;

public interface HelloEventFactory {

    HelloEvent create(HibernationPod hibernationPod);
}
