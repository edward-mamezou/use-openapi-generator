package com.mamezou_tech.example.controller.api;

import com.mamezou_tech.example.controller.model.Hello;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${openapi.exampleService.base-path:/example}")
public class ExampleApiController implements ExampleApi {

    @Override
    public ResponseEntity<Hello> helloGet() {
        Hello hello = new Hello();
        hello.setMessage("Hello World!");
        return new ResponseEntity<>(hello, HttpStatus.OK);
    }
}
