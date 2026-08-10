package com.example.serialization;

import com.example.serialization.model.Order;
import org.apache.fory.Fory;
import org.apache.fory.config.Language;

public class ForySerializationTest extends AbstractSerializationTest {

    private final Fory fory = Fory.builder()
            .withLanguage(Language.JAVA)
            .withRefTracking(true)
            .requireClassRegistration(false)
            .build();

    @Override
    protected String frameworkName() {
        return "fory";
    }

    @Override
    protected byte[] serialize(Order order) {
        return fory.serialize(order);
    }

    @Override
    protected Order deserialize(byte[] data) {
        return (Order) fory.deserialize(data);
    }
}
