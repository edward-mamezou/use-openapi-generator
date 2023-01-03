package com.mamezou_tech.example.infrastructure.factory;

import com.mamezou_tech.example.domain.domainevent.HelloEvent;
import com.mamezou_tech.example.domain.entity.HibernationPod;
import com.mamezou_tech.example.domain.factory.HelloEventFactory;
import com.mamezou_tech.example.domain.valueobject.EventId;
import com.mamezou_tech.example.infrastructure.audio.AudioConverter;
import com.mamezou_tech.example.infrastructure.aws.Polly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechResponse;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Component
public class HelloEventFactoryImpl implements HelloEventFactory {

    private static final Logger logger = LoggerFactory.getLogger(HelloEventFactoryImpl.class);

    private final Polly polly;

    public HelloEventFactoryImpl(@Autowired Polly polly) {
        this.polly = polly;
    }

    @Override
    public HelloEvent create(HibernationPod hibernationPod) {
        try {
            String uuid = UUID.randomUUID().toString();
            URL url = new URL(String.format("file:///tmp/%s.wav", uuid));
            String text = String.format("Good Morning, %s", hibernationPod.passenger().firstName());
            try (ResponseInputStream<SynthesizeSpeechResponse> is = polly.speech(text)) {
                new AudioConverter().convert(is, new File(url.getFile()));
            }
            ZonedDateTime time = Instant.now().atZone(ZoneId.of("UTC"));
            return new HelloEvent(new EventId(uuid), hibernationPod, url, time);
        } catch (IOException | UnsupportedAudioFileException e) {
            logger.warn("error", e);
            return null;
        }
    }
}
