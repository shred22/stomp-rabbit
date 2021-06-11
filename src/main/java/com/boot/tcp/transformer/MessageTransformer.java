package com.boot.tcp.transformer;

import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

@Component
public class MessageTransformer implements GenericTransformer<String, String> {

    @Override
    public String transform(String input) {
        return "Custom Msg Transformer Hello " + input + "!";
    }
}
