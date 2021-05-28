package com.boot.stomp.rabbit.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class GreetingResponse {
    public String message;
}
