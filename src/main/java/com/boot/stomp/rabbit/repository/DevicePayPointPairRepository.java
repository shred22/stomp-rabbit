package com.boot.stomp.rabbit.repository;

import org.javatuples.Pair;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.boot.stomp.rabbit.model.DevicePayPointPair;

@Repository
public interface DevicePayPointPairRepository extends MongoRepository<DevicePayPointPair, String> {

    DevicePayPointPair  findByDeviceId(String deviceId);

    DevicePayPointPair  findByPayPointId(String payPointId);

}
