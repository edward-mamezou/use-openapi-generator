package com.mamezou_tech.example.application;

import com.mamezou_tech.example.infrastructure.oidc.CodeFlow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.Optional;

public class AuthServiceTests {

    private final AuthService authService;

    public AuthServiceTests() throws URISyntaxException {
        CodeFlow codeFlow = new CodeFlow("https://localhost:8080/auth/token", "hibernation-pod", "SECRET");
        this.authService = new AuthService(codeFlow);
    }

    @Test
    public void createImage() {
        Optional<byte[]> maybeImage = authService.createImage("http://localhost:8080/example/login?podId=id-001");
        Assertions.assertTrue(maybeImage.isPresent());
    }

    @Test
    public void generateState() {
        Optional<State> maybeState = authService.generateState();
        Assertions.assertTrue(maybeState.isPresent());
    }

    @Test
    public void validate() {
        Optional<State> maybeState = authService.generateState();
        Assertions.assertTrue(maybeState.map(state -> authService.validate(state.state(), state.hashedState())).orElse(false));
    }
}
