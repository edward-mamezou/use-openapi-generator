package com.mamezou_tech.example.domain.domainevent;

import com.mamezou_tech.example.domain.entity.HibernationPod;
import com.mamezou_tech.example.domain.valueobject.EventId;

import java.net.URL;
import java.time.ZonedDateTime;

public record HelloEvent(EventId eventId, HibernationPod hibernationPod, URL helloVoice, ZonedDateTime time) {
}
