package com.mamezou_tech.example.controller.api;

import com.mamezou_tech.example.application.AuthService;
import com.mamezou_tech.example.application.State;
import com.mamezou_tech.example.controller.configuration.AuthProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("${openapi.exampleService.base-path:/example}")
public class AuthApiController implements AuthApi {

    private static final Logger logger = LoggerFactory.getLogger(AuthApiController.class);

    private final NativeWebRequest request;

    private final AuthService authService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Value("${openapi.exampleService.base-path:/example}")
    private String basePath;

    private final String authorizationEndpoint;

    private final String clientId;

    private final String callback;

    @Autowired
    public AuthApiController(NativeWebRequest request,
                             AuthService authService,
                             AuthProperties properties) {
        this.request = request;
        this.authService = authService;
        this.authorizationEndpoint = properties.authorizationEndpoint();
        this.clientId = properties.clientId();
        this.callback = properties.callback();
    }

    @Override
    public ResponseEntity<Resource> indexGet(String podId) {
        return getRequest().flatMap(request -> {
                HttpServletRequest httpServletRequest = request.getNativeRequest(HttpServletRequest.class);
                ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromServletMapping(httpServletRequest);
                String loginUri = builder.replaceQuery("podId=" + podId).replacePath(basePath).path("/login").build().toUriString();
                return authService.createImage(loginUri);
            }).map(image -> new ResponseEntity<Resource>(new ByteArrayResource(image), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private void addStateCookie(HttpServletResponse httpServletResponse, State state) {
        Cookie cookie = new Cookie("state", state.hashedState());
        cookie.setHttpOnly(true);
        cookie.setPath(basePath);
        cookie.setMaxAge(300); // 5 minutes
        httpServletResponse.addCookie(cookie);
    }

    private void addPodIdCookie(HttpServletResponse httpServletResponse, String podId) {
        Cookie cookie = new Cookie("podId", podId);
        cookie.setHttpOnly(true);
        cookie.setPath(basePath);
        cookie.setMaxAge(300); // 5 minutes
        httpServletResponse.addCookie(cookie);
    }

    private String redirectUri(HttpServletRequest httpServletRequest) {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromServletMapping(httpServletRequest);
        return URLEncoder.encode(
                builder.replaceQuery("").replacePath(basePath).path("/callback").build().toUriString(),
                StandardCharsets.UTF_8);
    }

    @Override
    public ResponseEntity<Void> loginGet(@NotNull @Valid String podId) {
        return getRequest().flatMap(request -> authService.generateState())
            .map(state -> {
                HttpServletRequest httpServletRequest = request.getNativeRequest(HttpServletRequest.class);
                HttpServletResponse httpServletResponse = request.getNativeResponse(HttpServletResponse.class);

                String redirectUri = redirectUri(httpServletRequest);

                String template = "%s?response_type=code&scope=openid+email&client_id=%s&redirect_uri=%s&state=%s";
                String authUrl = String.format(template, authorizationEndpoint, clientId, redirectUri, state.state());

                addPodIdCookie(httpServletResponse, podId);
                addStateCookie(httpServletResponse, state);

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.LOCATION, authUrl);
                return new ResponseEntity<Void>(headers, HttpStatus.FOUND);
            }).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private String podId(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("podId")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String state(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("state")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<String> callbackGet() {
        return getRequest().flatMap(request -> {
            HttpServletRequest httpServletRequest = request.getNativeRequest(HttpServletRequest.class);
            ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromServletMapping(httpServletRequest);
            Map<String, String[]> parameters = httpServletRequest.getParameterMap();
            if (parameters.containsKey("error")) {
                return Optional.empty();
            }

            Cookie[] cookies = httpServletRequest.getCookies();
            String hashedState = state(cookies);
            String podId = podId(cookies);
            if (hashedState == null || podId == null) {
                return Optional.empty();
            }

            String state = httpServletRequest.getParameter("state");
            String code = httpServletRequest.getParameter("code");

            if (state == null || code == null) {
                return Optional.empty();
            }

            if (!authService.validate(state, hashedState)) {
                return Optional.empty();
            }
            return authService.callback(code, redirectUri(httpServletRequest)).map(
                idToken -> {
                    String helloUrl = builder
                            .replaceQuery("")
                            .queryParam("access_token", idToken)
                            .replacePath(basePath)
                            .path("/hibernation-pod")
                            .path("/" + podId)
                            .path("/hello")
                            .build().toUriString();
                    String template = "<html><body><ul><li>ID Token: %s</li><li>Pod ID: %s</li></ul><a href=\"%s\">%s</a></body></html>";
                    return String.format(template, idToken, podId, helloUrl, helloUrl);
                }
            );
        }).map(html -> new ResponseEntity<>(html, HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.FORBIDDEN));
    }
}
