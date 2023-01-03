package com.mamezou_tech.example.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mamezou_tech.example.application.HelloService;
import com.mamezou_tech.example.controller.model.Hello;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("${openapi.exampleService.base-path:/example}")
public class HibernationPodApiController implements HibernationPodApi {

    private static final Logger logger = LoggerFactory.getLogger(HibernationPodApiController.class);

    private final NativeWebRequest request;

    private final HelloService helloService;

    @Autowired
    public HibernationPodApiController(NativeWebRequest request, HelloService helloService) {
        this.request = request;
        this.helloService = helloService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Hello> hibernationPodPodIdHelloGet(
        @Parameter(name = "podId", description = "Identity of a hibernation pod", required = true) @PathVariable("podId") String podId
    ) {
        Optional<String> maybeFirstName = getRequest()
            .flatMap(request -> Optional.ofNullable(parseRequest(request.getHeader("payload"))));
        Optional<String> maybeHelloMessage = maybeFirstName
            .map(firstName -> helloService.sayHello(podId, firstName));
        return maybeHelloMessage.map(helloMessage -> {
            Hello hello = new Hello();
            hello.setMessage(helloMessage);
            return new ResponseEntity<>(hello, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
    }

    private String parseRequest(final String payload) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            byte[] decodedPayload = Base64.getUrlDecoder().decode(payload);
            Map<?, ?> mappedPayload = mapper.readValue(decodedPayload, Map.class);
            if (mappedPayload.get("custom:firstname") instanceof String firstName) {
                return firstName;
            }
        } catch (IOException e) {
            logger.warn("warn", e);
            return null;
        }
        return null;
    }
}
