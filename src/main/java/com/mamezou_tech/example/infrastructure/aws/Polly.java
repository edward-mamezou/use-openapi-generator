package com.mamezou_tech.example.infrastructure.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.*;

@Component
public class Polly {

    private final PollyClient polly;

    public Polly(@Autowired PollyClient polly) {
        this.polly = polly;
    }

    public ResponseInputStream<SynthesizeSpeechResponse> speech(String text) {
        SynthesizeSpeechRequest.Builder builder = SynthesizeSpeechRequest.builder();
        builder.outputFormat(OutputFormat.MP3)
                .text(text)
                .textType(TextType.TEXT)
                .voiceId(VoiceId.JOANNA);
        SynthesizeSpeechRequest request = builder.build();
        return polly.synthesizeSpeech(request);
    }
}
