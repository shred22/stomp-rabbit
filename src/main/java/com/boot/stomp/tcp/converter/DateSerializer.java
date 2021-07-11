package com.boot.stomp.tcp.converter;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class DateSerializer extends StdScalarSerializer<Date> {

    public DateSerializer() {
        super(Date.class);
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        String output = value.toString();
        gen.writeString(output);
    }
}