package com.boot.stomp.rabbit.controller.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.boot.stomp.rabbit.service.PayPointService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@RestController
@AllArgsConstructor
public class PayPointRegistrationController {

    private final PayPointService payPointService;


    @GetMapping(path = "/register/{payPointId}")
    public ResponseEntity<?> registerPayPoint(@PathVariable("payPointId") String payPointId) {
        payPointService.addPayPoint(payPointId);
        return  ResponseEntity.ok("{\"message\": \"registered\"}");
    }

    @SneakyThrows
    @GetMapping(path = "/paypoints")
    public ResponseEntity<?> getPayPaoints() {
        return  ResponseEntity.ok(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(payPointService.getRegisteredPayPoints()));
    }
}
