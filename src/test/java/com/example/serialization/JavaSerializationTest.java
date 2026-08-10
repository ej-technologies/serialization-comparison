package com.example.serialization;

import com.example.serialization.model.Order;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JavaSerializationTest extends AbstractSerializationTest {

    @Override
    protected String frameworkName() {
        return "java";
    }

    @Override
    protected byte[] serialize(Order order) throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(order);
        }
        return byteStream.toByteArray();
    }

    @Override
    protected Order deserialize(byte[] data) throws Exception {
        try (ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Order) objectStream.readObject();
        }
    }
}
