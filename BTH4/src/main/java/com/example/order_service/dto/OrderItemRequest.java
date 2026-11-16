package com.example.order_service.dto;

public class OrderItemRequest {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    public Long getOrderId(){return orderId;} public void setOrderId(Long v){orderId=v;}
    public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
}