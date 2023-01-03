package com.mamezou_tech.example.infrastructure.oidc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class CodeFlow {

    private static final Logger logger = LoggerFactory.getLogger(CodeFlow.class);

    private final URI tokenEndpoint;

    private final String clientId;

    private final String clientSecret;

    private final ObjectMapper mapper;

    public CodeFlow(String tokenEndpoint, String clientId, String clientSecret) throws URISyntaxException {
        this.mapper = new ObjectMapper();
        this.tokenEndpoint = new URI(tokenEndpoint);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    private String clientSecretBasic() {
        return Base64.getUrlEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
    }

    public String tokenRequest(String code, String redirectUri) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder()
                .build();

        String credential = String.format("Basic %s", clientSecretBasic());

        String template = "grant_type=authorization_code&code=%s&redirect_uri=%s";
        String body = String.format(template, code, redirectUri);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(tokenEndpoint)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", credential)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

        Map<String, Object> json = mapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
        if (json.containsKey("error")) {
            logger.error(json.get("error").toString());
        }
        return json.get("id_token") instanceof String idToken ? idToken : null;
    }
}
