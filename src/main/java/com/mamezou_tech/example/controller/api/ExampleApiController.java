package com.mamezou_tech.example.controller.api;

import com.mamezou_tech.example.application.HelloService;
import com.mamezou_tech.example.controller.model.Hello;

import com.mamezou_tech.example.domain.valueobject.HelloVoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@RestController
@RequestMapping("${openapi.exampleService.base-path:/example}")
public class ExampleApiController implements ExampleApi {

    private final NativeWebRequest request;

    private final HelloService helloService;

    @Autowired
    public ExampleApiController(NativeWebRequest request, HelloService helloService) {
        this.request = request;
        this.helloService = helloService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Hello> helloGet() {
        Optional<HelloVoice> maybeHelloVoice = getRequest().flatMap(request -> {
            String payload = request.getHeader("payload");
            return helloService.sayHello(payload);
        });
        return maybeHelloVoice.map(helloVoice -> {
            Hello hello = new Hello();
            hello.setMessage(helloVoice.message());
            return new ResponseEntity<>(hello, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(null, HttpStatus.FORBIDDEN));
    }
}
