package com.boot.tcp.converter;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

public class DateDeserializer extends StdScalarDeserializer<Date> {

    public DateDeserializer() {
        super(Date.class);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        try {
            return new Date(Date.parse(p.getValueAsString()));
        } catch (Exception e) {
            return null;
        }
    }
}