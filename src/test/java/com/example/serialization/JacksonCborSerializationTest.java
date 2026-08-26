package com.example.serialization;

import com.example.serialization.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;

public class JacksonCborSerializationTest extends AbstractSerializationTest<Order> {

    private final ObjectMapper mapper = new CBORMapper();

    @Override
    protected java.util.List<Order> createDataset() {
        return DatasetGenerator.create();
    }

    @Override
    protected String frameworkName() {
        return "jackson-cbor";
    }

    @Override
    protected byte[] serialize(Order order) throws Exception {
        return mapper.writeValueAsBytes(order);
    }

    @Override
    protected Order deserialize(byte[] data) throws Exception {
        return mapper.readValue(data, Order.class);
    }
}
