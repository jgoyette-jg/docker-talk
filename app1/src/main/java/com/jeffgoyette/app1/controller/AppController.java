package com.jeffgoyette.app1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${apiHost}")
    private String apiHost;

    @GetMapping("/hello")
    public ResponseEntity<Void> sayHello() {
        System.out.println("Hello in app1");
        restTemplate.getForEntity("http://"+ apiHost +"/api/hello?fromApp=app1", ResponseEntity.class);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
