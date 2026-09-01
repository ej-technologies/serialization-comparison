package com.example.serialization;

import com.example.serialization.model.Order;
import org.apache.fory.json.ForyJson;

public class ForyJsonSerializationTest extends AbstractSerializationTest<Order> {

    private final ForyJson json = ForyJson.builder().withCodegen(true).withAsyncCompilation(false).writeNullFields(false).build();

    @Override
    protected java.util.List<Order> createDataset() {
        return DatasetGenerator.create();
    }

    @Override
    protected String frameworkName() {
        return "fory-json";
    }

    @Override
    protected byte[] serialize(Order order) {
        return json.toJsonBytes(order);
    }

    @Override
    protected Order deserialize(byte[] data) {
        return json.fromJson(data, Order.class);
    }
}
