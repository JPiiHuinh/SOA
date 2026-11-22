package com.example.reporting_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="orders_reports")
public class OrderReport {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="order_id", nullable=false, unique=true) private Long orderId;
    @Column(name="total_revenue", precision=10, scale=2, nullable=false) private BigDecimal totalRevenue;
    @Column(name="total_cost", precision=10, scale=2, nullable=false) private BigDecimal totalCost;
    @Column(name="total_profit", precision=10, scale=2, nullable=false) private BigDecimal totalProfit;
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Long getOrderId(){return orderId;} public void setOrderId(Long v){orderId=v;}
    public BigDecimal getTotalRevenue(){return totalRevenue;} public void setTotalRevenue(BigDecimal v){totalRevenue=v;}
    public BigDecimal getTotalCost(){return totalCost;} public void setTotalCost(BigDecimal v){totalCost=v;}
    public BigDecimal getTotalProfit(){return totalProfit;} public void setTotalProfit(BigDecimal v){totalProfit=v;}
}
