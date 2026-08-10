package com.example.serialization;

import com.example.serialization.model.*;

import java.math.BigDecimal;
import java.util.*;

public class DatasetGenerator {

    private static final String[] FIRST_NAMES = {"Alice", "Bruno", "Carla", "David", "Elena", "Frank", "Greta", "Henry", "Ingrid", "Jonas"};
    private static final String[] LAST_NAMES = {"Anderson", "Bergmann", "Costa", "Dubois", "Eriksen", "Ferrari", "Gruber", "Hansen", "Ivanov", "Jensen"};
    private static final String[] CITIES = {"Berlin", "Munich", "Hamburg", "Cologne", "Vienna", "Zurich", "Amsterdam", "Prague"};
    private static final String[] STREETS = {"Hauptstrasse", "Bahnhofstrasse", "Ringstrasse", "Parkallee", "Lindenweg", "Bergstrasse"};
    private static final String[] ROOT_CATEGORIES = {"Electronics", "Clothing", "Books", "Garden"};
    private static final String[] SUB_CATEGORIES = {"Accessories", "Premium", "Budget"};
    private static final String[] PRODUCT_WORDS = {"Ultra", "Pro", "Lite", "Max", "Eco", "Smart", "Classic", "Prime"};
    private static final String[] TAGS = {"new", "sale", "bestseller", "limited", "organic", "imported", "clearance", "featured"};
    private static final String[] ATTRIBUTE_KEYS = {"channel", "campaign", "salesRep", "priority", "giftWrap", "source"};
    private static final String[] LOREM = ("Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt " +
            "ut labore et dolore magna aliqua enim ad minim veniam quis nostrud exercitation ullamco laboris nisi").split(" ");

    private final Random random;

    private DatasetGenerator(long seed) {
        random = new Random(seed);
    }

    public static List<Order> create() {
        return new DatasetGenerator(42).generate();
    }

    private List<Order> generate() {
        List<Category> categories = createCategories();
        List<Product> products = createProducts(categories);
        List<Customer> customers = createCustomers();
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            orders.add(createOrder(i, products, customers));
        }
        return orders;
    }

    private List<Category> createCategories() {
        List<Category> categories = new ArrayList<>();
        long id = 1;
        for (String rootName : ROOT_CATEGORIES) {
            Category root = new Category(id++, rootName, null);
            categories.add(root);
            for (String subName : SUB_CATEGORIES) {
                categories.add(new Category(id++, rootName + " " + subName, root));
            }
        }
        return categories;
    }

    private List<Product> createProducts(List<Category> categories) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            String name = pick(PRODUCT_WORDS) + " " + pick(PRODUCT_WORDS) + " " + (1000 + i);
            Set<String> tags = new LinkedHashSet<>();
            int tagCount = 2 + random.nextInt(4);
            for (int j = 0; j < tagCount; j++) {
                tags.add(pick(TAGS));
            }
            byte[] thumbnail = new byte[200 + random.nextInt(1800)];
            random.nextBytes(thumbnail);
            products.add(new Product(
                    "SKU-" + (100000 + i),
                    name,
                    lorem(20 + random.nextInt(60)),
                    categories.get(random.nextInt(categories.size())),
                    BigDecimal.valueOf(199 + random.nextInt(50000)).movePointLeft(2),
                    50 + random.nextInt(20000),
                    tags,
                    thumbnail));
        }
        return products;
    }

    private List<Customer> createCustomers() {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String firstName = pick(FIRST_NAMES);
            String lastName = pick(LAST_NAMES);
            List<Address> addresses = new ArrayList<>();
            addresses.add(createAddress());
            if (random.nextBoolean()) {
                addresses.add(createAddress());
            }
            customers.add(new Customer(
                    10000L + i,
                    firstName,
                    lastName,
                    firstName.toLowerCase() + "." + lastName.toLowerCase() + i + "@example.com",
                    new Date(500000000000L + random.nextInt(1_000_000_000) * 1000L),
                    random.nextInt(5) == 0,
                    addresses));
        }
        return customers;
    }

    private Address createAddress() {
        return new Address(
                pick(STREETS) + " " + (1 + random.nextInt(150)),
                pick(CITIES),
                String.format("%05d", random.nextInt(100000)),
                "Germany");
    }

    private Order createOrder(int index, List<Product> products, List<Customer> customers) {
        Customer customer = customers.get(random.nextInt(customers.size()));
        int lineCount = 1 + random.nextInt(8);
        List<OrderLine> lines = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            Product product = products.get(random.nextInt(products.size()));
            lines.add(new OrderLine(
                    product,
                    1 + random.nextInt(5),
                    product.getPrice(),
                    random.nextInt(4) == 0 ? random.nextInt(30) / 100.0 : 0.0));
        }
        Map<String, String> attributes = new LinkedHashMap<>();
        int attributeCount = 2 + random.nextInt(5);
        for (int i = 0; i < attributeCount; i++) {
            attributes.put(pick(ATTRIBUTE_KEYS) + i, lorem(1 + random.nextInt(3)));
        }
        return new Order(
                "ORD-2026-" + String.format("%06d", index),
                customer,
                lines,
                OrderStatus.values()[random.nextInt(OrderStatus.values().length)],
                new Date(1_750_000_000_000L + random.nextInt(30_000_000) * 1000L),
                customer.getAddresses().getFirst(),
                attributes,
                random.nextInt(3) == 0 ? lorem(10 + random.nextInt(40)) : null);
    }

    private String pick(String[] values) {
        return values[random.nextInt(values.length)];
    }

    private String lorem(int wordCount) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            if (i > 0) {
                builder.append(' ');
            }
            builder.append(pick(LOREM));
        }
        return builder.toString();
    }
}
