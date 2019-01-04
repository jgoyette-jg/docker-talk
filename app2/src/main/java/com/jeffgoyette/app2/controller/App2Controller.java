package com.jeffgoyette.app2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class App2Controller {

    @GetMapping("/hello")
    public ResponseEntity<Void> sayHello(@RequestParam String fromApp) {
        System.out.println("Hello in app2 via " + fromApp);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
