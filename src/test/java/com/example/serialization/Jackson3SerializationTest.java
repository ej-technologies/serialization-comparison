package com.example.serialization;

import com.example.serialization.model.Order;
import tools.jackson.databind.json.JsonMapper;

public class Jackson3SerializationTest extends AbstractSerializationTest<Order> {

    private final JsonMapper mapper = JsonMapper.builder().build();

    @Override
    protected java.util.List<Order> createDataset() {
        return DatasetGenerator.create();
    }

    @Override
    protected String frameworkName() {
        return "jackson3";
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
