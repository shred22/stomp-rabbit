package com.boot.stomp.rabbit.service;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class PayPointService {
    private final Set<String> registeredPayPoints = new LinkedHashSet<>();

    public void addPayPoint(String payPointId) {
        registeredPayPoints.add(payPointId);
    }
    public Set<String> getRegisteredPayPoints() {
        return registeredPayPoints;
    }
}
