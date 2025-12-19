package org.yearup.models;

import java.math.BigDecimal;

public class OrderLineItem {
    private int orderLineIdItem;
    private int orderId;
    private int productId;
    private BigDecimal salesPrice;
    private int quantity;
    private BigDecimal discount;


    public OrderLineItem(int orderLineIdItem, int orderId, int productId, BigDecimal salesPrice, int quantity, BigDecimal discount) {
        this.orderLineIdItem = orderLineIdItem;
        this.orderId = orderId;
        this.productId = productId;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public OrderLineItem(int orderId, int productId, BigDecimal salesPrice, int quantity, BigDecimal discount) {
        this.orderId = orderId;
        this.productId = productId;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public int getOrderLineIdItem() {
        return orderLineIdItem;
    }

    public void setOrderLineIdItem(int orderLineIdItem) {
        this.orderLineIdItem = orderLineIdItem;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
