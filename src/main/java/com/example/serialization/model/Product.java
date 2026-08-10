package com.example.serialization.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class Product implements Serializable {
    private String sku;
    private String name;
    private String description;
    private Category category;
    private BigDecimal price;
    private int weightGrams;
    private Set<String> tags;
    private byte[] thumbnail;

    public Product() {
    }

    public Product(String sku, String name, String description, Category category, BigDecimal price, int weightGrams, Set<String> tags, byte[] thumbnail) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.weightGrams = weightGrams;
        this.tags = tags;
        this.thumbnail = thumbnail;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getWeightGrams() {
        return weightGrams;
    }

    public void setWeightGrams(int weightGrams) {
        this.weightGrams = weightGrams;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return weightGrams == product.weightGrams && Objects.equals(sku, product.sku)
                && Objects.equals(name, product.name) && Objects.equals(description, product.description)
                && Objects.equals(category, product.category) && Objects.equals(price, product.price)
                && Objects.equals(tags, product.tags) && Arrays.equals(thumbnail, product.thumbnail);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(sku, name, description, category, price, weightGrams, tags);
        result = 31 * result + Arrays.hashCode(thumbnail);
        return result;
    }
}
