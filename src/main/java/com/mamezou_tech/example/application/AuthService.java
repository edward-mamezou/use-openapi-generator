package com.mamezou_tech.example.application;

import com.google.zxing.WriterException;
import com.mamezou_tech.example.infrastructure.barcode.BarcodeGenerator;
import com.mamezou_tech.example.infrastructure.oidc.CodeFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

public class AuthService {

    private static Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final byte[] seed = SecureRandom.getSeed(32); // 256 bytes

    private final CodeFlow codeFlow;

    public AuthService(CodeFlow codeFlow) {
        this.codeFlow = codeFlow;
    }

    public Optional<byte[]> createImage(String url) {
        try {
            return Optional.of(new BarcodeGenerator(300, 300).generate(url));
        } catch (IOException | WriterException e) {
            logger.error("error", e);
            return Optional.empty();
        }
    }

    public Optional<String> digest(String state) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
            messageDigest.update(seed);
            messageDigest.update(state.getBytes(StandardCharsets.UTF_8));
            String hashedState = Base64.getUrlEncoder().encodeToString(messageDigest.digest());
            return Optional.of(hashedState);
        } catch (NoSuchAlgorithmException e) {
            logger.error("error", e);
            return Optional.empty();
        }
    }

    public Optional<State> generateState() {
        String state = Base64.getUrlEncoder().encodeToString(SecureRandom.getSeed(32));
        return digest(state).map(hashedState -> new State(state, hashedState));
    }

    public boolean validate(String state, String hashedState) {
        return digest(state).map(hash -> hash.equals(hashedState)).orElse(false);
    }

    public Optional<String> callback(String code, String redirectUri) {
        try {
            return Optional.ofNullable(codeFlow.tokenRequest(code, redirectUri));
        } catch (IOException | InterruptedException e) {
            logger.error("error", e);
        }
        return Optional.empty();
    }
}
