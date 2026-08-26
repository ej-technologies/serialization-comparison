package com.example.serialization;

import com.example.serialization.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonSerializationTest extends AbstractSerializationTest<Order> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected java.util.List<Order> createDataset() {
        return DatasetGenerator.create();
    }

    @Override
    protected String frameworkName() {
        return "jackson";
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
