package com.example.serialization;

import com.example.serialization.model.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractSerializationTest {

    protected static final List<Order> DATASET = DatasetGenerator.create();

    private final int iterations = Integer.getInteger("iterations", 2_000_000);

    protected abstract String frameworkName();

    protected abstract byte[] serialize(Order order) throws Exception;

    protected abstract Order deserialize(byte[] data) throws Exception;

    @Test
    void roundTrips() throws Exception {
        for (int i = 0; i < 10; i++) {
            Order order = DATASET.get(i);
            assertEquals(order, deserialize(serialize(order)), "round trip failed for " + order.getId());
        }

        int warmup = Math.clamp(iterations / 20, 10_000, 100_000);
        run(warmup);

        long start = System.nanoTime();
        Stats stats = run(iterations);
        long elapsed = System.nanoTime() - start;

        System.out.printf("%s: %d iterations in %d ms, %.0f ns/op, %d avg bytes%n",
                frameworkName(), iterations, elapsed / 1_000_000,
                (double) elapsed / iterations, stats.totalBytes / iterations);
        if (stats.checksum == 42) {
            System.out.println("impossible: " + stats.checksum);
        }
    }

    private Stats run(int count) throws Exception {
        Stats stats = new Stats();
        for (int i = 0; i < count; i++) {
            Order order = DATASET.get(i % DATASET.size());
            byte[] data = serialize(order);
            Order back = deserialize(data);
            stats.totalBytes += data.length;
            stats.checksum += back.getLines().size() + back.getId().length();
        }
        return stats;
    }

    private static class Stats {
        long totalBytes;
        long checksum;
    }
}
