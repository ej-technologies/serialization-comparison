package com.example.serialization;

import com.example.serialization.model.Order;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class AllocCheckMain {

    public static void main(String[] args) throws Exception {
        List<Order> dataset = DatasetGenerator.create();
        byte[][] blobs = new byte[dataset.size()][];
        for (int i = 0; i < dataset.size(); i++) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
                objectStream.writeObject(dataset.get(i));
            }
            blobs[i] = byteStream.toByteArray();
        }

        long seconds = args.length > 0 ? Long.parseLong(args[0]) : 60;
        long deadline = System.nanoTime() + seconds * 1_000_000_000L;
        long iterations = 0;
        long checksum = 0;
        while (System.nanoTime() < deadline) {
            byte[] blob = blobs[(int) (iterations % blobs.length)];
            try (ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(blob))) {
                Order order = (Order) objectStream.readObject();
                checksum += order.getLines().size();
            }
            iterations++;
        }
        System.out.println("iterations: " + iterations + ", checksum: " + checksum);
    }
}
