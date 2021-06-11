package com.boot.tcp.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class DevicePayPointPairingController {

    private final Map<String, String> devicePayPointPair;

    @GetMapping(path = "/device/{deviceId}/payPoint/{paypointId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> markPair(@PathVariable("deviceId") String deviceId,
                                           @PathVariable("paypointId")String paypointId) {
        devicePayPointPair.put(deviceId, paypointId);
        return ResponseEntity.ok(devicePayPointPair);

    }

}
