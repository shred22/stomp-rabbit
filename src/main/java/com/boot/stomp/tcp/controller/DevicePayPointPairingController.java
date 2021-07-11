package com.boot.stomp.tcp.controller;



import java.util.List;

import org.javatuples.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.boot.stomp.rabbit.model.DevicePayPointPair;
import com.boot.stomp.rabbit.repository.DevicePayPointPairRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class DevicePayPointPairingController {

    private final DevicePayPointPairRepository repository;

    @GetMapping(path = "/device/{deviceId}/payPoint/{paypointId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DevicePayPointPair>> markPair(@PathVariable("deviceId") String deviceId,
                                                             @PathVariable("paypointId")String paypointId) {

        if(repository.findByDeviceId(deviceId) != null) {
            throw new RuntimeException("Pair Already Registered");
        }
        DevicePayPointPair devicePayPointPair = new DevicePayPointPair();
        devicePayPointPair.setPayPointId(paypointId);
        devicePayPointPair.setDeviceId(deviceId);
        repository.save(devicePayPointPair);

        return ResponseEntity.ok(repository.findAll());

    }

    @GetMapping(path = "/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DevicePayPointPair> getPair(@PathVariable("deviceId") String deviceId) {
        return ResponseEntity.ok(repository.findByDeviceId(deviceId));

    }

}
