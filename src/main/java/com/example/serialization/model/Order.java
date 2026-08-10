package com.example.serialization.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Order implements Serializable {
    private String id;
    private Customer customer;
    private List<OrderLine> lines;
    private OrderStatus status;
    private Date created;
    private Address shippingAddress;
    private Map<String, String> attributes;
    private String notes;

    public Order() {
    }

    public Order(String id, Customer customer, List<OrderLine> lines, OrderStatus status, Date created, Address shippingAddress, Map<String, String> attributes, String notes) {
        this.id = id;
        this.customer = customer;
        this.lines = lines;
        this.status = status;
        this.created = created;
        this.shippingAddress = shippingAddress;
        this.attributes = attributes;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public void setLines(List<OrderLine> lines) {
        this.lines = lines;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id) && Objects.equals(customer, order.customer)
                && Objects.equals(lines, order.lines) && status == order.status
                && Objects.equals(created, order.created) && Objects.equals(shippingAddress, order.shippingAddress)
                && Objects.equals(attributes, order.attributes) && Objects.equals(notes, order.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, lines, status, created, shippingAddress, attributes, notes);
    }
}
