package com.example.serialization;

import com.example.serialization.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

public class JacksonCborAfterburnerSerializationTest extends JacksonCborSerializationTest {

    private final ObjectMapper afterburnerMapper = new CBORMapper().registerModule(new AfterburnerModule());

    @Override
    protected String frameworkName() {
        return "jackson-cbor-afterburner";
    }

    @Override
    protected byte[] serialize(Order order) throws Exception {
        return afterburnerMapper.writeValueAsBytes(order);
    }

    @Override
    protected Order deserialize(byte[] data) throws Exception {
        return afterburnerMapper.readValue(data, Order.class);
    }
}
