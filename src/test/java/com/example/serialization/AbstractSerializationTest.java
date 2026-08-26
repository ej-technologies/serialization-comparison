package com.example.serialization;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractSerializationTest<T> {

    private final int iterations = Integer.getInteger("iterations", 2_000_000);

    private List<T> dataset;

    protected abstract List<T> createDataset();

    protected abstract String frameworkName();

    protected abstract byte[] serialize(T order) throws Exception;

    protected abstract T deserialize(byte[] data) throws Exception;

    private List<T> dataset() {
        if (dataset == null) {
            dataset = createDataset();
        }
        return dataset;
    }

    @Test
    void roundTrips() throws Exception {
        List<T> data = dataset();
        for (int i = 0; i < 10; i++) {
            T order = data.get(i);
            assertEquals(order, deserialize(serialize(order)), "round trip failed");
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
        List<T> data = dataset();
        for (int i = 0; i < count; i++) {
            T order = data.get(i % data.size());
            byte[] serialized = serialize(order);
            T back = deserialize(serialized);
            stats.totalBytes += serialized.length;
            stats.checksum += back.hashCode();
        }
        return stats;
    }

    private static class Stats {
        long totalBytes;
        long checksum;
    }
}
