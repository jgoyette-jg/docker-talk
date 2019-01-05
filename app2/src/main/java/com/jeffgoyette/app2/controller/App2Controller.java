package com.jeffgoyette.app2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class App2Controller {

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello(@RequestParam String fromApp) {
        log.info("Hello in app2 via {}", fromApp);
        return new ResponseEntity<String>("Acknowledged " + fromApp, HttpStatus.ACCEPTED);
    }

}
