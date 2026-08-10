package com.example.serialization;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.example.serialization.model.Order;

public class KryoSerializationTest extends AbstractSerializationTest {

    private final Kryo kryo = new Kryo();
    private final Output output = new Output(64 * 1024, 16 * 1024 * 1024);
    private final Input input = new Input();

    public KryoSerializationTest() {
        kryo.setRegistrationRequired(false);
        kryo.setReferences(true);
    }

    @Override
    protected String frameworkName() {
        return "kryo";
    }

    @Override
    protected byte[] serialize(Order order) {
        output.reset();
        kryo.writeClassAndObject(output, order);
        return output.toBytes();
    }

    @Override
    protected Order deserialize(byte[] data) {
        input.setBuffer(data);
        return (Order) kryo.readClassAndObject(input);
    }
}
