package com.example.order_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="customer_name", nullable=false)
    private String customerName;
    @Column(name="customer_email", nullable=false)
    private String customerEmail;
    @Column(name="total_amount", precision=10, scale=2, nullable=false)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    @Column(nullable=false, length=50)
    private String status = "pending";
    @Column(name="created_at", updatable=false)
    private LocalDateTime createdAt;
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    void onCreate() { createdAt=LocalDateTime.now(); updatedAt=createdAt; }
    @PreUpdate
    void onUpdate() { updatedAt=LocalDateTime.now(); }

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getCustomerName(){return customerName;} public void setCustomerName(String v){customerName=v;}
    public String getCustomerEmail(){return customerEmail;} public void setCustomerEmail(String v){customerEmail=v;}
    public BigDecimal getTotalAmount(){return totalAmount;} public void setTotalAmount(BigDecimal v){totalAmount=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
    public List<OrderItem> getItems(){return items;} public void setItems(List<OrderItem> v){items=v;}
}