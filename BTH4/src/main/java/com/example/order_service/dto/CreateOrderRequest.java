package com.example.order_service.dto;

import java.util.List;

public class CreateOrderRequest {
    private String customerName;
    private String customerEmail;
    private List<ItemRequest> items;

    public static class ItemRequest {
        private Long productId;
        private Integer quantity;
        public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
        public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    }
    public String getCustomerName(){return customerName;} public void setCustomerName(String v){customerName=v;}
    public String getCustomerEmail(){return customerEmail;} public void setCustomerEmail(String v){customerEmail=v;}
    public List<ItemRequest> getItems(){return items;} public void setItems(List<ItemRequest> v){items=v;}
}