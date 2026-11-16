package com.example.order_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_id", nullable=false)
    private Order order;
    @Column(name="product_id", nullable=false)
    private Long productId;
    @Column(name="product_name", nullable=false)
    private String productName;
    @Column(nullable=false)
    private Integer quantity;
    @Column(name="unit_price", precision=10, scale=2, nullable=false)
    private BigDecimal unitPrice;
    @Column(name="total_price", precision=10, scale=2, nullable=false)
    private BigDecimal totalPrice;

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Order getOrder(){return order;} public void setOrder(Order v){order=v;}
    public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
    public String getProductName(){return productName;} public void setProductName(String v){productName=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public BigDecimal getUnitPrice(){return unitPrice;} public void setUnitPrice(BigDecimal v){unitPrice=v;}
    public BigDecimal getTotalPrice(){return totalPrice;} public void setTotalPrice(BigDecimal v){totalPrice=v;}
}