package com.boot.stomp.rabbit.controller.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.stomp.rabbit.service.JwtTokenService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
public class AccessTokenController {

    private final JwtTokenService jwtTokenService;

    @GetMapping("/token")
    public ResponseEntity<String> getAccessToken() {
        return ResponseEntity.ok(jwtTokenService.createJwt());
    }
}
