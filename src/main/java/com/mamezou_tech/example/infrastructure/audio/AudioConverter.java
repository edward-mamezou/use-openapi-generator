package com.mamezou_tech.example.infrastructure.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AudioConverter {

    private AudioFormat target(AudioFormat format) {
        return new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                format.getSampleRate(),
                16,
                format.getChannels(),
                format.getChannels() * 2,
                format.getSampleRate(),
                false
        );
    }

    public void convert(InputStream in, File out) throws UnsupportedAudioFileException, IOException {
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(in)) {
            AudioFormat target = target(ais.getFormat());
            AudioInputStream converted = AudioSystem.getAudioInputStream(target, ais);
            AudioSystem.write(converted, AudioFileFormat.Type.WAVE, out);
        }
    }
}
