package com.example.serialization;

import com.example.serialization.model.Order;
import tools.jackson.dataformat.cbor.CBORMapper;

public class Jackson3CborSerializationTest extends AbstractSerializationTest<Order> {

    private final CBORMapper mapper = CBORMapper.builder().build();

    @Override
    protected java.util.List<Order> createDataset() {
        return DatasetGenerator.create();
    }

    @Override
    protected String frameworkName() {
        return "jackson3-cbor";
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
