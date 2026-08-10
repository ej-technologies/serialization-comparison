package com.example.serialization.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class OrderLine implements Serializable {
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;
    private double discount;

    public OrderLine() {
    }

    public OrderLine(Product product, int quantity, BigDecimal unitPrice, double discount) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discount = discount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderLine orderLine)) return false;
        return quantity == orderLine.quantity && Double.compare(discount, orderLine.discount) == 0
                && Objects.equals(product, orderLine.product) && Objects.equals(unitPrice, orderLine.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, unitPrice, discount);
    }
}
